package com.example.pictureme.views.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.data.Response
import com.example.pictureme.databinding.FragmentLoginBinding
import com.example.pictureme.viewmodels.AuthViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()

    private var startedTypingEmail = false
    private var startedTypingPassword = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Observers
        authViewModel.authLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Success -> {
                    // Load user & his friends from Firestore
                    userViewModel.loadUser()
                    // Load user picmes
                    picmeViewModel.loadPicmes()

                    // Load feelings
                    picmeViewModel.loadFeelings()

                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_loginFragment_to_navFragment)
                }
                is Response.Failure -> {
                    Toast.makeText(context, "Failure!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        // OnClickListeners

        binding.textRegister.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener {
            authViewModel.login(
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            )
        }

        emailValidation()
        passwordValidation()

        authViewModel.login("2@gmail.com", "123456")

        return (binding.root)
    }

    private fun passwordValidation() {
        binding.editPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.editPasswordLayout.helperText = validPassword()
                startedTypingPassword = true
            }
        }
        binding.editPassword.doOnTextChanged { text, start, before, count ->
            val validity = validPassword()
            if (validity == null) {
                binding.editPasswordLayout.helperText = null
                handleButton()
            } else {
                if (startedTypingPassword) {
                    binding.editPasswordLayout.helperText = validity
                    handleButton()
                }
            }
        }
    }

    private fun emailValidation() {
        binding.editEmail.doOnTextChanged { text, start, before, count ->
            val validity = validEmail()
            if (validEmail() == null) {
                binding.editEmailLayout.helperText = null
                handleButton()
            } else {
                if (startedTypingEmail) {
                    binding.editEmailLayout.helperText = validity
                    handleButton()
                }
            }
        }
        binding.editEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.editEmailLayout.helperText = validEmail()
                startedTypingEmail = true
            }
        }
    }

    private fun handleButton() {
        if (validPassword() == null && validEmail() == null) {
            binding.buttonLogin.isEnabled = true
            binding.buttonLogin.setBackgroundColor(resources.getColor(R.color.primary))
            binding.buttonLogin.setTextColor(resources.getColor(R.color.textOnPrimary))
        } else {
            binding.buttonLogin.isEnabled = false
            binding.buttonLogin.setBackgroundColor(resources.getColor(R.color.disabledBackground))
            binding.buttonLogin.setTextColor(resources.getColor(R.color.disabledText))
        }
    }

    private fun validPassword(): CharSequence? {
        val passwordText = binding.editPassword.text.toString()
        if (passwordText.length < 6) {
            return "Password must have at least 6 characters"
        }
        return null
    }

    private fun validEmail(): String? {
        val emailText = binding.editEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}