package com.example.pictureme.views.profile.friends.tabs.friendsList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.databinding.FragmentFriendsListBinding
import com.example.pictureme.viewmodels.UserViewModel
import com.example.pictureme.views.addfriends.adapters.AddFriendsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsListFragment : Fragment() {

    private var _binding: FragmentFriendsListBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()

    private var friendships: List<Friendship> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsListBinding.inflate(inflater, container, false)

        val adapter = setFriendListAdapter()
        setSearchBar(adapter)

        return (binding.root)
    }

    private fun setFriendListAdapter(): FriendsListAdapter {
        val adapter = FriendsListAdapter(friendships)
        binding.friendsRecyclerView.adapter = adapter
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(activity)

        return adapter
    }

    private fun setSearchBar(adapter: FriendsListAdapter) {
        binding.searchView.clearFocus()

        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            val friendships = user.friendships!!
            adapter.setFriendList(friendships)

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    binding.searchView.clearFocus()
                    return false;
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0 == null) return true

                    val filteredList = ArrayList<Friendship>()
                    for (friendship in friendships) {

                        if (friendship.friend!!.username!!.lowercase().contains(p0.lowercase())) {
                            filteredList.add(friendship)
                        }
                    }

                    adapter.setFriendList(filteredList)
                    return true
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}