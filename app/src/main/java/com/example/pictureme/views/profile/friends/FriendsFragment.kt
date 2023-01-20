package com.example.pictureme.views.profile.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentFriendsBinding
import com.example.pictureme.views.explore.adapters.ViewPagerAdapter
import com.example.pictureme.views.profile.friends.adapter.FriendsViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        setFriendsTabs()
        setGoBackButton()

        return (binding.root)
    }

    private fun setGoBackButton() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setFriendsTabs() {

        val pager = binding.friendsViewPager
        val tl = binding.friendsTabs
        pager.adapter = FriendsViewPagerAdapter(
            requireActivity().supportFragmentManager,
            requireActivity().lifecycle
        )
        pager.isUserInputEnabled = false

        TabLayoutMediator(tl, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Friends"
                1 -> tab.text = "Requests"
                2 -> tab.text = "Add"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}