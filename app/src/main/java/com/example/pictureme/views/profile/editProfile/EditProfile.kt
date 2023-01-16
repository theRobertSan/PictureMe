package com.example.pictureme.views.profile.editProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentEditProfileBinding
import com.example.pictureme.databinding.FragmentFriendsBinding
import com.example.pictureme.viewmodels.UserViewModel

class EditProfile : Fragment() {

    private val IMAGE_CHOOSE = 1000
    private val REQUEST_CODE = 2

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    var imageUri: Uri? = null

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.imageProfile.setOnClickListener {
            chooseImageFromGallery()
        }

        binding.buttonSave.setOnClickListener {
            save()
        }

        return (binding.root)
    }

    private fun save() {
        userViewModel.updateProfile(imageUri)
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.imageProfile.setImageURI(data?.data)
        }
    }
}