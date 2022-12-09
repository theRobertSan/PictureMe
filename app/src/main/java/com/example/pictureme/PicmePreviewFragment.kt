package com.example.pictureme

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentLoginBinding
import com.example.pictureme.databinding.FragmentPicmePreviewBinding
import com.example.pictureme.viewmodels.AuthViewModel
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
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicmePreviewBinding.inflate(inflater, container, false);

        // OnClickListeners

        binding.buttonSave.setOnClickListener {
            picmeViewModel.addPicme(uri!!)
//            Navigation.findNavController(binding.root).navigate(R.id.action_picmePreviewFragment_to_navFragment)
        }

        binding.buttonRetake.setOnClickListener {
            invokeCamera()
        }

        invokeCamera()

        return (binding.root)
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
            val takenPicture = BitmapFactory.decodeFile(currentImagePath)
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