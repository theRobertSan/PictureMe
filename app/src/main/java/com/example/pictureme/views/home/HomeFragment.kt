package com.example.pictureme.views.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureme.views.home.adapters.HomeAdapter
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentHomeBinding
import org.w3c.dom.Text
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    // TODO Needs to get user's name from user
    private fun setWelcomingMessage() {
        val rightNow = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY) // 24 hour format

        val tvWelcome: TextView = binding.tvWelcome
        if(currentHour < 12)
            tvWelcome.text = "Good Morning, John"
        else if(currentHour < 18)
            tvWelcome.text = "Good Afternoon, John"
        else
            tvWelcome.text = "Good Evening, John"
    }

    // Setup PicMe Cards
    // TODO Needs to get data from user
    private fun setPicMeCards() {

        val picMeList1 = mutableListOf(
            PicMe("sim"),
            PicMe("burro")
        )

        val picMeList2 = mutableListOf(
            PicMe("tou"),
            PicMe("aqui")
        )

        val rvHome = binding.rvHome
        val rvsCategory = arrayListOf<ParentModelClass>()

        val rv1 = ParentModelClass("Your PicMe's with friends", picMeList1)
        val rv2 = ParentModelClass("Food PicMes", picMeList2)

        rvsCategory.add(rv1)
        rvsCategory.add(rv2)

        val adapter = HomeAdapter(rvsCategory)
        rvHome.adapter = adapter
        rvHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        //adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}