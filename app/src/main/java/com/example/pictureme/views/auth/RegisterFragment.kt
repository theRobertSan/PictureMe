package com.example.pictureme.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.data.Resource
import com.example.pictureme.databinding.FragmentRegisterBinding
import com.example.pictureme.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false);

        // Observers

        viewModel.authLiveData.observe(viewLifecycleOwner) {

            when(it) {
                is Resource.Success -> {
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
            viewModel?.signup(binding.editUsername.text.toString(), binding.editEmail.text.toString(), binding.editPassword.text.toString())
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}