package com.example.pictureme.views.addfriends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.data.Resource
import com.example.pictureme.databinding.FragmentAddFriendsBinding
import com.example.pictureme.viewmodels.UserViewModel
import com.example.pictureme.views.addfriends.adapters.AddFriendsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFriendsFragment : Fragment() {

    private var _binding: FragmentAddFriendsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFriendsBinding.inflate(inflater, container, false);

        setupListeners()

        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                val adapter = AddFriendsAdapter(it.result.friendships!!)
                binding.rvFriends.adapter = adapter
                binding.rvFriends.layoutManager = LinearLayoutManager(activity)
            }

        }

        return (binding.root)
    }

    private fun setupListeners() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}