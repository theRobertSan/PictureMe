package com.example.pictureme.views.profile.friends.tabs.friendRequests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.data.models.FriendRequest
import com.example.pictureme.databinding.FragmentFriendRequestsBinding
import com.example.pictureme.viewmodels.UserViewModel
import com.example.pictureme.views.profile.friends.tabs.friendsList.FriendsListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendRequestsFragment : Fragment() {
    private var _binding: FragmentFriendRequestsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()

    private var friendRequests: List<FriendRequest> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendRequestsBinding.inflate(inflater, container, false)

        userViewModel.loadUser()

        setFriendRequestsAdapter()

        return (binding.root)
    }

    private fun setFriendRequestsAdapter() {
        val adapter = FriendRequestsAdapter(emptyList(), userViewModel)
        binding.requestsRecyclerView.adapter = adapter
        binding.requestsRecyclerView.layoutManager = LinearLayoutManager(activity)

        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            println("CHANGING REQUEST LIST")
            friendRequests = user.friendRequests
            println(friendRequests)
            adapter.setFriendRequests(friendRequests)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}