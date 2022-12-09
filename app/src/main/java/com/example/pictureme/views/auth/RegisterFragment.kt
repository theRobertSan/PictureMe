package com.example.pictureme.views.auth

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
import com.example.pictureme.databinding.FragmentRegisterBinding
import com.example.pictureme.viewmodels.AuthViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false);

        // Observers

        authViewModel.authLiveData.observe(viewLifecycleOwner) {

            when(it) {
                is Resource.Success -> {
                    // Save user to firestore
                    userViewModel.addUser(binding.editUsername.text.toString())
                    Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_navFragment)
                }
                is Resource.Failure -> {
                    Toast.makeText(context, "Failure!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        // OnClickListeners

        binding.textLogin.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonSignup.setOnClickListener {
            authViewModel?.signup(binding.editUsername.text.toString(), binding.editEmail.text.toString(), binding.editPassword.text.toString())
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}