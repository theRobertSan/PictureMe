package com.example.pictureme.views.addFeeling

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.pictureme.databinding.FragmentAddFeelingBinding
import com.example.pictureme.views.addFeeling.adapters.AddFeelingViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFeelingFragment : Fragment() {

    private var _binding: FragmentAddFeelingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFeelingBinding.inflate(inflater, container, false)

        setupListeners()

        val pager = binding.fragmentAddFeelingViewPager
        val tl = binding.fragmentExploreTabs
        pager.adapter =
            AddFeelingViewPagerAdapter(
                requireActivity().supportFragmentManager,
                requireActivity().lifecycle
            )
        pager.isUserInputEnabled = false

        TabLayoutMediator(tl, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Emotions"
                1 -> tab.text = "Food"
            }
        }.attach()

        return (binding.root)
    }

    private fun setupListeners() {

        binding.buttonGoBack.setOnClickListener {
            Navigation.findNavController(binding.root)
                .popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}