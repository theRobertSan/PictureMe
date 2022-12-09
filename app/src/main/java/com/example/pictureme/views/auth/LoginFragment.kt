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
import com.example.pictureme.databinding.FragmentLoginBinding
import com.example.pictureme.viewmodels.AuthViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false);

        // Observers

        authViewModel.authLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    // Load user from Firestore
                    userViewModel.loadUser()
                    Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_navFragment)
                }
                is Resource.Failure -> {
                    Toast.makeText(context, "Failure!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        // OnClickListeners

        binding.textRegister.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener {
            authViewModel.login(binding.editEmail.text.toString(), binding.editPassword.text.toString())
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}