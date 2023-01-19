package com.example.pictureme.views.home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.R
import com.example.pictureme.views.home.adapters.HomeAdapter
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentHomeBinding
import com.example.pictureme.utils.FilterPicmes
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModelViewModel by activityViewModels<PicmeDetailsViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Welcoming message based on day time
        setWelcomingMessage()

        // PicMe Cards Setup
        setPicMeCards()

        return (binding.root)
    }

    // Set welcoming message based on day time
    private fun setWelcomingMessage() {
        val rightNow = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY) // 24 hour format
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            val fullName = user.fullName
            val firstName = fullName!!.split(" ")[0]
            if (currentHour < 12)
                binding.fragmentHomeTvWelcome.text = "Good Morning, $firstName"
            else if (currentHour < 18)
                binding.fragmentHomeTvWelcome.text = "Good Afternoon, $firstName"
            else
                binding.fragmentHomeTvWelcome.text = "Good Evening, $firstName"
        }

    }

    // Setup PicMe Cards
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPicMeCards() {
        var feelings: List<Feeling> = emptyList()
        var picmes: List<Picme> = emptyList()

        val rvHome = binding.fragmentHomeRv
        val rvsCategory = arrayListOf<ParentModelClass>()

        val filteredPicmes = FilterPicmes.getFilteredPicmes(picmes, feelings)
        for (filter in filteredPicmes) {
            val rv = ParentModelClass(filter.first, filter.second)
            rvsCategory.add(rv)
        }

        val adapter = HomeAdapter(rvsCategory, picmeDetailsViewModelViewModel, requireContext())
        rvHome.adapter = adapter
        rvHome.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        picmeViewModel.foodFeelingsLiveData.observe(viewLifecycleOwner) { response ->
            feelings = response
        }

        // If data changes, update
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            //println("DATA CHANGED: ${response[response.size - 1]}")
            val rvsCategory = arrayListOf<ParentModelClass>()
            val filteredPicmes = FilterPicmes.getFilteredPicmes(response, feelings)

            if (filteredPicmes.isEmpty()) {
                binding.textNoPicmes.visibility = View.VISIBLE
                binding.fragmentHomeIv.visibility = View.VISIBLE
            } else {
                binding.textNoPicmes.visibility = View.GONE
                binding.fragmentHomeIv.visibility = View.GONE
            }

            for (filter in filteredPicmes) {
                val rv = ParentModelClass(filter.first, filter.second)
                rvsCategory.add(rv)
            }
            adapter.setList(rvsCategory)

            binding.rvLoadingBar.isGone = true
            binding.fragmentHomeRv.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}