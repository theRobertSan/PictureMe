package com.example.pictureme.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.R
import com.example.pictureme.views.home.adapters.HomeAdapter
import com.example.pictureme.data.Response
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.databinding.FragmentHomeBinding
import com.example.pictureme.utils.FilterPicmes
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
            val username = user.username
            if (currentHour < 12)
                binding.fragmentHomeTvWelcome.text = "Good Morning, $username"
            else if (currentHour < 18)
                binding.fragmentHomeTvWelcome.text = "Good Afternoon, $username"
            else
                binding.fragmentHomeTvWelcome.text = "Good Evening, $username"
        }


    }

    // Setup PicMe Cards
    private fun setPicMeCards() {
        var feelings: List<Feeling> = emptyList()
        picmeViewModel.foodFeelingsLiveData.observe(viewLifecycleOwner) { response ->
            feelings = response
        }
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            println("DATE CHANGED ------------" + response.size)
            val rvHome = binding.fragmentHomeRv
            val rvsCategory = arrayListOf<ParentModelClass>()

            val filteredPicmes = FilterPicmes().getFilteredPicmes(response, feelings)
            for (filter in filteredPicmes) {
                val rv = ParentModelClass(filter.first, filter.second)
                rvsCategory.add(rv)
            }

            val adapter = HomeAdapter(rvsCategory)
            rvHome.adapter = adapter
            rvHome.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

            adapter.notifyDataSetChanged()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}