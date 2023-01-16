package com.example.pictureme.views.explore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentGalleryBinding
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.explore.adapters.ImageAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        // PicMe Cards Setup
        setPicMeCards()

        return (binding.root)
    }

    // Setup PicMe Cards
    private fun setPicMeCards() {
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            println("DATE CHANGED ------------" + response.size)
            val gridView = binding.fragmentGalleryGridView;
            gridView.adapter = ImageAdapter(response, requireContext())
            gridView.setOnItemClickListener { _, _, i, _ ->
                // Navigate to details
                picmeDetailsViewModel.selectPicme(response[i])
                Navigation.findNavController(requireView().parent.parent.parent.parent.parent.parent as View)
                    .navigate(R.id.action_navFragment_to_picmeDetailsFragment)
            }
        }
    }

}