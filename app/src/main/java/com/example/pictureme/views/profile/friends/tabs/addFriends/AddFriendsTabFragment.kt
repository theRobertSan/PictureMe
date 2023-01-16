package com.example.pictureme.views.profile.friends.tabs.addFriends

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentAddFriendsTabBinding
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

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

            userViewModel.sendFriendRequest(username)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}