package com.example.pictureme.utils

import android.util.Patterns

object CredentialValidation {

    fun validPassword(passwordText: CharSequence?): CharSequence? {
        if (passwordText == null || passwordText.length < 6) {
            return "Password must have at least 6 characters"
        }
        return null
    }

    fun validEmail(emailText: String?): String? {
        if (emailText == null || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    fun validUsername(usernameText: String?): String? {
        val regex = Regex("^[a-z0-9]*$", RegexOption.IGNORE_CASE)
        if(usernameText == null || !regex.matches(usernameText)) {
            return "Username must only contain letters and numbers"
        }
        return null
    }

    fun validFullName(nameText: String?): String? {
        val regex = Regex("^[a-zàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçšž '-]+$", RegexOption.IGNORE_CASE)
        if(nameText == null || !regex.matches(nameText)) {
            return "Name must only contain letters and spaces"
        }
        return null
    }

    fun matchingPasswords(passwordText1: String?, passwordText2: String?) : String? {
        if(passwordText1 != passwordText2) {
            return "The passwords do not match"
        }
        return null
    }

}