package com.example.pictureme.views.preview

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentPicmePreviewBinding
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.PreviewPicmeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PicmePreviewFragment : Fragment() {

    private var _binding: FragmentPicmePreviewBinding? = null
    private val binding get() = _binding!!

    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val previewViewModel by activityViewModels<PreviewPicmeViewModel>()

    private lateinit var currentImagePath: String
    private lateinit var strUri: String
    private lateinit var uri: Uri
    private var takenPicture: Bitmap? = null
    private var takenPictureRotated: Bitmap? = null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onAttach(context: Context) {
        super.onAttach(context)
        invokeCamera()
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        saveCurrentLocation()
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun saveCurrentLocation() {
        mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                previewViewModel.setLocation(GeoPoint(location!!.latitude, location!!.longitude))
            } else {
                throw Exception("No location")
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicmePreviewBinding.inflate(inflater, container, false)

        setUpListeners()
        setUpObservers()

        // Load picture if it has already been taken
        if (takenPictureRotated != null) {
            binding.imagePicme.setImageBitmap(takenPictureRotated)
        }

        return (binding.root)
    }

    private fun setUpListeners() {
        // Save PicMe
        binding.buttonSave.setOnClickListener {
            val previewPicme = previewViewModel.previewLiveData.value
            picmeViewModel.addPicme(previewPicme!!)
            // Clear preview
            previewViewModel.clear()

            // When created picme is created & added, go back to nav
            picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_picmePreviewFragment_to_navFragment)
            }

        }

        binding.buttonExit.setOnClickListener{
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_picmePreviewFragment_to_navFragment)
        }

        // Retake PicMe
        binding.buttonRetake.setOnClickListener {
            invokeCamera()
        }

        // Add Friends
        binding.buttonAddFriends.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_picmePreviewFragment_to_addFriendsFragment)
        }

        // Add Feeling
        binding.buttonAddFeeling.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_picmePreviewFragment_to_addFeelingFragment)
        }
    }

    private fun setUpObservers() {
//        // Observe if picme creation was successful
//        picmeViewModel.picmeCreationLiveData.observe(viewLifecycleOwner) {
//            if (it) {
//                Toast.makeText(requireContext(), "PicMe Saved Successfully!", Toast.LENGTH_SHORT).show()
//                Navigation.findNavController(binding.root).navigate(R.id.action_picmePreviewFragment_to_navFragment)
//            } else {
//                Toast.makeText(requireContext(), "Picme couldn't be saved. Try again later", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.pictureme.fileprovider",
                file
            )
            // Save it to preview view model
            previewViewModel.updateImageUri(uri)
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File
            .createTempFile("Picme_$timestamp", ".jpg", imageDirectory)
            .apply {
                currentImagePath = absolutePath
            }
    }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                strUri = uri.toString()

                // Matrix for rotating the picture taken
                val matrix = Matrix()
                matrix.postRotate(90F)
                takenPicture = BitmapFactory.decodeFile(currentImagePath)
                val picture = takenPicture

                // Rotate the picture taken
                takenPictureRotated = Bitmap.createBitmap(picture!!, 0, 0, picture.width, picture.height, matrix, true)
                binding.imagePicme.setImageBitmap(takenPictureRotated)

            } else {
                Log.e(TAG, "Image not saved")
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}