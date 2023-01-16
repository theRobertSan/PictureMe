package com.example.pictureme.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.data.Response
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
            when (it) {
                is Response.Success -> {
                    val username = binding.editUsername.text.toString()
                    val fullName = binding.editName.text.toString()
                    // Save user to firestore
                    userViewModel.addUser(username, fullName)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_registerFragment_to_navFragment)
                }
                is Response.Failure -> {
                    Toast.makeText(activity, "Failure!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        // OnClickListeners

        binding.textLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.buttonSignUp.setOnClickListener {
            authViewModel?.signup(
                binding.editUsername.text.toString(),
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            )
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}