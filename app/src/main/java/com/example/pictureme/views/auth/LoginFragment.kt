package com.example.pictureme.views.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.data.Response
import com.example.pictureme.databinding.FragmentLoginBinding
import com.example.pictureme.utils.CredentialValidation
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

        // OnClickListeners

        binding.textRegister.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.textForgotPassword.setOnClickListener {
            Toast.makeText(it.context, "Oh well!", Toast.LENGTH_SHORT).show()
        }

        binding.buttonLogin.setOnClickListener {
            authViewModel.login(
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            )
        }

        emailValidation()
        passwordValidation()

        // Observers
        authViewModel.authLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Success -> {
                    loadUserDataAndEnterApp()
                }
                is Response.Failure -> {
                    Toast.makeText(context, "Failure!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        //authViewModel.login("1@gmail.com", "123456")

        return (binding.root)
    }

    override fun onStart() {
        super.onStart()

        checkIfUserIsAlreadyLoggedIn()
    }

    private fun loadUserDataAndEnterApp() {
        // Load user & his friends from Firestore
        userViewModel.loadUser()
        // Load user picmes
        picmeViewModel.loadPicmes()
        // Load feelings
        picmeViewModel.loadFeelings()

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_loginFragment_to_navFragment)
    }

    private fun checkIfUserIsAlreadyLoggedIn() {
        if (authViewModel.currentUser != null) {
            loadUserDataAndEnterApp()
        }
    }

    private fun passwordValidation() {

        binding.editPassword.doOnTextChanged { text, _, _, _ ->
            val validity = CredentialValidation.validPassword(text)
            if (validity == null || text.toString() == "") {
                binding.editPasswordLayout.helperText = null
                handleButton()
            } else {
                if (startedTypingPassword) {
                    binding.editPasswordLayout.helperText = validity
                    handleButton()
                }
            }
        }

        binding.editPassword.setOnFocusChangeListener { _, focused ->
            val passwordText = binding.editPassword.text.toString()
            if (!focused && passwordText != "") {
                binding.editPasswordLayout.helperText =
                    CredentialValidation.validPassword(passwordText)
                startedTypingPassword = true
            }
        }
    }

    private fun emailValidation() {
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            val validity = CredentialValidation.validEmail(text.toString())
            if (validity == null || text.toString() == "") {
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
            val emailText = binding.editEmail.text.toString()
            if (!focused && emailText != "") {
                binding.editEmailLayout.helperText = CredentialValidation.validEmail(emailText)
                startedTypingEmail = true
            }
        }
    }

    private fun handleButton() {
        val emailText = binding.editEmail.text.toString()
        val passwordText = binding.editPassword.text.toString()

        if (CredentialValidation.validPassword(passwordText) == null && CredentialValidation.validEmail(
                emailText
            ) == null
        ) {
            binding.buttonLogin.isEnabled = true
            binding.buttonLogin.setBackgroundColor(resources.getColor(R.color.primary))
            binding.buttonLogin.setTextColor(resources.getColor(R.color.textOnPrimary))
        } else {
            binding.buttonLogin.isEnabled = false
            binding.buttonLogin.setBackgroundColor(resources.getColor(R.color.disabledBackground))
            binding.buttonLogin.setTextColor(resources.getColor(R.color.disabledText))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}