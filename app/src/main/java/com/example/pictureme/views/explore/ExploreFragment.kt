package com.example.pictureme.views.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pictureme.databinding.FragmentExploreBinding
import com.example.pictureme.views.explore.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        val pager = binding.fragmentExploreViewPager
        val tl = binding.fragmentExploreTabs
        pager.adapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager,
            requireActivity().lifecycle,
        )
        pager.isUserInputEnabled = false

        TabLayoutMediator(tl, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Gallery"
                1 -> tab.text = "Map"
            }
        }.attach()

        return (binding.root)
    }
}