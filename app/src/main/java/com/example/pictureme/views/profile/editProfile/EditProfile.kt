package com.example.pictureme.views.profile.editProfile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentEditProfileBinding
import com.example.pictureme.utils.CredentialValidation
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel


class EditProfile : Fragment() {

    private val IMAGE_CHOOSE = 1000
    private val REQUEST_CODE = 2

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    var imageUri: Uri? = null

    private val userViewModel by activityViewModels<UserViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        // Load user profile picture
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            Pictures.loadProfilePicture(
                user.profilePicturePath,
                binding.imageProfile,
                binding.imageLoadingBar
            )
            binding.fragmentEditProfileEtUsername.setText(user.fullName)
        }

        binding.floatingActionButton.setOnClickListener {
            chooseImageFromGallery()
        }

        binding.buttonSave.setOnClickListener{
            save()
        }

        setGoBackButton()

        return (binding.root)
    }

    private fun setGoBackButton() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun save() {

        val newName = binding.fragmentEditProfileEtUsername.text.toString()
        val validation = CredentialValidation.validFullName(newName)

        // If new name is valid, save everything, send a toast and go back
        if(validation == null) {
            userViewModel.updateProfile(
                binding.fragmentEditProfileEtUsername.text.toString(),
                imageUri
            )
            // Reload picmes so that icons update
            userViewModel.userLiveData.observe(viewLifecycleOwner) {
                picmeViewModel.loadPicmes()
            }

            Navigation.findNavController(binding.root).popBackStack()
            Toast.makeText(context, "Profile successfully updated", Toast.LENGTH_SHORT).show()

        // Else set a listener for the name editor
        } else {

            binding.fragmentEditProfileEtUsernameLayout.helperText = validation

            binding.fragmentEditProfileEtUsername.doAfterTextChanged {
                val newNameInside = binding.fragmentEditProfileEtUsername.text.toString()
                val validationInside = CredentialValidation.validFullName(newNameInside)
                if(validationInside == null) {
                    binding.fragmentEditProfileEtUsernameLayout.helperText = null
                } else {
                    binding.fragmentEditProfileEtUsernameLayout.helperText = validation
                }
            }
        }

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