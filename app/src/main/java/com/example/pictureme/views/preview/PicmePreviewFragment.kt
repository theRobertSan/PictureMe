package com.example.pictureme.views.preview

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentPicmePreviewBinding
import com.example.pictureme.viewmodels.PicmeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PicmePreviewFragment : Fragment() {

    private var _binding: FragmentPicmePreviewBinding? = null
    private val binding get() = _binding!!

    private val picmeViewModel by activityViewModels<PicmeViewModel>()

    private lateinit var currentImagePath: String
    private lateinit var strUri: String
    private lateinit var uri: Uri
    private var takenPicture: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        invokeCamera()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicmePreviewBinding.inflate(inflater, container, false)

        setUpListeners()
        setUpObservers()

        // Load picture if it has already been taken
        if (takenPicture != null) {
            binding.imagePicme.setImageBitmap(takenPicture)
        }

        return (binding.root)
    }

    private fun setUpListeners() {
        // Save PicMe
        binding.buttonSave.setOnClickListener {
            picmeViewModel.addPicme(uri!!)
        }

        // Retake PicMe
        binding.buttonRetake.setOnClickListener {
            invokeCamera()
        }

        // Add Friends
        binding.buttonAddFriends.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_picmePreviewFragment_to_addFriendsFragment)
        }
    }

    private fun setUpObservers() {
        // Observe if picme creation was successful
        picmeViewModel.picmeCreationLiveData.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "PicMe Saved Successfully!", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(binding.root).navigate(R.id.action_picmePreviewFragment_to_navFragment)
            } else {
                Toast.makeText(requireContext(), "Picme couldn't be saved. Try again later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(requireContext(), "com.example.pictureme.fileprovider", file)
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

    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success) {
            Log.i(TAG, "Image Location: $uri")
            strUri = uri.toString()
            takenPicture = BitmapFactory.decodeFile(currentImagePath)
            Log.i(TAG, "Image obtained from: $currentImagePath")
            binding.imagePicme.setImageBitmap(takenPicture)

//            val photo = Picme(localUri = uri.toString())
        } else {
            Log.e(TAG, "Image not saved")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}