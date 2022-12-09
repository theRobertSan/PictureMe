package com.example.pictureme.views.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.data.Resource
import com.example.pictureme.data.models.User
import com.example.pictureme.databinding.FragmentLoginBinding
import com.example.pictureme.databinding.FragmentProfileBinding
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false);

        setUpObservers()



        return (binding.root)
    }

    private fun setUpObservers() {
        // Observe user
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.textName.text = it.result.username
                }
                else -> {}
            }
        }
        // Testing (observe picmes)
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    val takenPicture = BitmapFactory.decodeFile(it.result[0].imageFile?.absolutePath)
                    binding.imageProfilePicture.setImageBitmap(takenPicture)
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}