package com.example.pictureme.views.profile.friends.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pictureme.views.profile.friends.tabs.addFriends.AddFriendsTabFragment
import com.example.pictureme.views.profile.friends.tabs.friendRequests.FriendRequestsFragment
import com.example.pictureme.views.profile.friends.tabs.friendsList.FriendsListFragment

class FriendsViewPagerAdapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle)
{
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendsListFragment()
            1 -> FriendRequestsFragment()
            2 -> AddFriendsTabFragment()
            else -> throw RuntimeException("Invalid position: $position")
        }
    }
}