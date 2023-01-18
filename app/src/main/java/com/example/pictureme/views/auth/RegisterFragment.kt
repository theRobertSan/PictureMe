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
import com.example.pictureme.databinding.FragmentRegisterBinding
import com.example.pictureme.utils.CredentialValidation
import com.example.pictureme.viewmodels.AuthViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()

    private var startConstantEmailValidation = false
    private var startConstantPasswordValidation = false
    private var startConstantUsernameValidation = false
    private var startConstantFullNameValidation = false
    private var startConstantPasswordRepeatValidation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // OnClickListeners
        binding.textLogin.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.buttonSignUp.setOnClickListener {
            authViewModel.signup(
                binding.editUsername.text.toString(),
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            )
        }

        // Start all listeners for validation
        usernameValidation()
        fullNameValidation()
        emailValidation()
        passwordValidation()
        passwordRepeatValidation()

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
                    Toast.makeText(activity, "Username or email has already been taken!", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }

        }

        return (binding.root)
    }

    private fun usernameValidation() {
        binding.editUsername.doOnTextChanged { text, _, _, _ ->
            val validity = CredentialValidation.validUsername(text.toString())
            if(validity == null  || text.toString() == "") {
                binding.editUsernameLayout.helperText = null
                handleButton()
            } else {
                if(startConstantUsernameValidation) {
                    binding.editUsernameLayout.helperText = validity
                    handleButton()
                }
            }
        }

        binding.editUsername.setOnFocusChangeListener { _, focused ->
            val usernameText = binding.editUsername.text.toString()
            if (!focused && usernameText != "") {
                binding.editUsernameLayout.helperText = CredentialValidation.validUsername(usernameText)
                startConstantUsernameValidation = true
            }
        }
    }

    private fun fullNameValidation() {
        binding.editName.doOnTextChanged { text, _, _, _ ->
            val validity = CredentialValidation.validFullName(text.toString())
            if(validity == null || text.toString() == "") {
                binding.editNameLayout.helperText = null
                handleButton()
            } else {
                if(startConstantFullNameValidation) {
                    binding.editNameLayout.helperText = validity
                    handleButton()
                }
            }
        }

        binding.editName.setOnFocusChangeListener { _, focused ->
            val nameText = binding.editName.text.toString()
            if (!focused && nameText != "") {
                binding.editNameLayout.helperText = CredentialValidation.validFullName(nameText)
                startConstantFullNameValidation = true
            }
        }
    }

    private fun emailValidation() {
        binding.editEmail.doOnTextChanged { text, _, _, _ ->
            val validity = CredentialValidation.validEmail(text.toString())
            if (validity == null  || text.toString() == "") {
                binding.editEmailLayout.helperText = null
                handleButton()
            } else {
                if (startConstantEmailValidation) {
                    binding.editEmailLayout.helperText = validity
                    handleButton()
                }
            }
        }
        binding.editEmail.setOnFocusChangeListener { _, focused ->
            val emailText = binding.editEmail.text.toString()
            if (!focused && emailText != "") {
                binding.editEmailLayout.helperText = CredentialValidation.validEmail(emailText)
                startConstantEmailValidation = true
            }
        }
    }

    private fun passwordValidation() {

        binding.editPassword.doOnTextChanged { text, _, _, _ ->
            val passwordRepeatText = binding.editPasswordRepeat.text.toString()
            val validitySize = CredentialValidation.validPassword(text.toString())
            val validityMatch = CredentialValidation.matchingPasswords(text.toString(), passwordRepeatText)
            if (validitySize == null  || text.toString() == "") {
                // If there are no input errors, remove warnings
                if(validityMatch == null || text.toString() == "") {
                    binding.editPasswordRepeatLayout.helperText = null

                // If there are input errors and repeat password began, put warnings warnings
                } else if (startConstantPasswordRepeatValidation && passwordRepeatText != ""){
                    binding.editPasswordRepeatLayout.helperText = validityMatch
                }

                binding.editPasswordLayout.helperText = null
                handleButton()
            } else {
                if (startConstantPasswordValidation) {
                    binding.editPasswordLayout.helperText = validitySize
                    handleButton()
                }
            }
        }

        binding.editPassword.setOnFocusChangeListener { _, focused ->
            val passwordText = binding.editPassword.text.toString()
            if (!focused && passwordText != "") {
                binding.editPasswordLayout.helperText = CredentialValidation.validPassword(passwordText)
                startConstantPasswordValidation = true
            }
        }
    }

    private fun passwordRepeatValidation() {

        binding.editPasswordRepeat.doOnTextChanged { text, _, _, _ ->
            val validity = CredentialValidation.matchingPasswords(text.toString(), binding.editPassword.text.toString())
            if (validity == null || text.toString() == "") {
                binding.editPasswordRepeatLayout.helperText = null
                handleButton()
            } else {
                if (startConstantPasswordRepeatValidation) {
                    binding.editPasswordRepeatLayout.helperText = validity
                    handleButton()
                }
            }
        }
        binding.editPasswordRepeat.setOnFocusChangeListener { _, focused ->
            val passwordRepeatText = binding.editPasswordRepeat.text.toString()
            val passwordText = binding.editPassword.text.toString()
            if (!focused && passwordRepeatText != "") {
                binding.editPasswordRepeatLayout.helperText = CredentialValidation.matchingPasswords(passwordText, passwordRepeatText)
                startConstantPasswordRepeatValidation = true
            }
        }
    }

    private fun handleButton() {
        val usernameText = binding.editUsername.text.toString()
        val fullNameText = binding.editName.text.toString()
        val emailText = binding.editEmail.text.toString()
        val passwordText = binding.editPassword.text.toString()
        val passwordRepeatText = binding.editPasswordRepeat.text.toString()

        if (CredentialValidation.validPassword(passwordText) == null && CredentialValidation.validEmail(emailText) == null
            && CredentialValidation.validUsername(usernameText) == null && CredentialValidation.validFullName(fullNameText) == null
            && passwordRepeatText == passwordText) {
            binding.buttonSignUp.isEnabled = true
            binding.buttonSignUp.setBackgroundColor(resources.getColor(R.color.primary))
            binding.buttonSignUp.setTextColor(resources.getColor(R.color.textOnPrimary))
        } else {
            binding.buttonSignUp.isEnabled = false
            binding.buttonSignUp.setBackgroundColor(resources.getColor(R.color.disabledBackground))
            binding.buttonSignUp.setTextColor(resources.getColor(R.color.disabledText))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}