package com.example.pictureme.views.profile.friends.tabs.addFriends

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentAddFriendsTabBinding
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Thread.sleep

@AndroidEntryPoint
class AddFriendsTabFragment : Fragment() {
    private var _binding: FragmentAddFriendsTabBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFriendsTabBinding.inflate(inflater, container, false)

        addButtonListener()

        return (binding.root)
    }

    private fun addButtonListener() {
        binding.buttonSend.setOnClickListener {
            val username = binding.inputUsername.text.toString()
            binding.inputUsername.text = null
            binding.inputUsername.clearFocus()

            // Hide keyboard
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if(view != null) {
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
            }

            userViewModel.sendFriendRequest(username)

            Toast.makeText(this.context, "Friend Request Sent to " + username, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}