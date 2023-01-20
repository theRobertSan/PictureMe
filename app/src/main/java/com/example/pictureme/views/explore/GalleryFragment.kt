package com.example.pictureme.views.explore

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentGalleryBinding
import com.example.pictureme.utils.FilterPicmes
import com.example.pictureme.viewmodels.FilteredPicmesViewModel
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.explore.adapters.ImageAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()
    private val filteredPicmesViewModel by activityViewModels<FilteredPicmesViewModel>()

    // Device
    private var gridItemDim: Int = 0

    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        getGridItemSize()

        setupPicmeCards()

        return (binding.root)
    }

    private fun setupPicmeCards() {
        val gridView = binding.fragmentGalleryGridView
        adapter = ImageAdapter(emptyList(), requireContext(), gridItemDim)
        gridView.adapter = adapter

        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            filteredPicmesViewModel.addPicmeList(FilterPicmes.filterNewestPicmes(response))

            if (response.isEmpty()) {
                binding.textNoPicmes.visibility = View.VISIBLE
                binding.fragmentHomeIv.visibility = View.VISIBLE
            } else {
                binding.textNoPicmes.visibility = View.GONE
                binding.fragmentHomeIv.visibility = View.GONE
            }
        }

        filteredPicmesViewModel.filteredPicmesLiveData.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                adapter = ImageAdapter(emptyList(), requireContext(), gridItemDim)
                gridView.adapter = adapter
                adapter.setData(response)

                gridView.setOnItemClickListener { _, _, i, _ ->
                    // Navigate to details
                    picmeDetailsViewModel.selectPicme(response[i])
                    Navigation.findNavController(requireView().parent.parent.parent.parent.parent.parent as View)
                        .navigate(R.id.action_navFragment_to_picmeDetailsFragment)
                }

            }
        }

        binding.buttonFilters.setOnClickListener {
            Navigation.findNavController(requireView().parent.parent.parent.parent.parent.parent as View)
                .navigate(R.id.action_navFragment_to_filtersFragment)
        }
    }

    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }

    private fun getGridItemSize() {
        val displayMetrics = DisplayMetrics()

        requireActivity().windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        gridItemDim = (width / 3 - 20).toInt()
    }

}