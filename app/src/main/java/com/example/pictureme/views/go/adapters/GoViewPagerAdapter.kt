package com.example.pictureme.views.go.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pictureme.views.go.GoImageFragment
import com.example.pictureme.views.profile.friends.tabs.addFriends.AddFriendsTabFragment
import com.example.pictureme.views.profile.friends.tabs.friendRequests.FriendRequestsFragment
import com.example.pictureme.views.profile.friends.tabs.friendsList.FriendsListFragment

class GoViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GoImageFragment(false)
            1 -> GoImageFragment(true)
            else -> throw RuntimeException("Invalid position: $position")
        }
    }
}