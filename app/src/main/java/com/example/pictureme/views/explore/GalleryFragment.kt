package com.example.pictureme.views.explore

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
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
        val gridView = binding.fragmentGalleryGridView;
        val adapter = ImageAdapter(emptyList(), requireContext(), gridItemDim)
        gridView.adapter = adapter
        /*gridView.setOnItemClickListener { _, _, i, _ ->
        // Navigate to details
        picmeDetailsViewModel.selectPicme(response[i])
        Navigation.findNavController(requireView().parent.parent.parent.parent.parent.parent as View)
            .navigate(R.id.action_navFragment_to_picmeDetailsFragment)
        */
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            filteredPicmesViewModel.addPicmeList(response)
        }

        filteredPicmesViewModel.filteredPicmesLiveData.observe(viewLifecycleOwner) { response ->
            println("DATE CHANGED ------------" + response.size)
            adapter.setData(response)
        }
/*
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            println("DATE CHANGED ------------" + response.size)
            //FILTER TEST
            //val filteredPicmes = FilterPicmes.filterFoodPicmes(response)
            //val filteredPicmes = FilterPicmes.filterNotFoodPicmes(response)
            //val filteredPicmes = FilterPicmes.filterOldestPicmes(response)
            //val filteredPicmes = FilterPicmes.filterNewestPicmes(response)
            val filteredPicmes = response
            val gridView = binding.fragmentGalleryGridView;
            gridView.adapter = ImageAdapter(filteredPicmes, requireContext())
            gridView.setOnItemClickListener { _, _, i, _ ->
                // Navigate to details
                picmeDetailsViewModel.selectPicme(filteredPicmes[i])
                Navigation.findNavController(requireView().parent.parent.parent.parent.parent.parent as View)
                    .navigate(R.id.action_navFragment_to_picmeDetailsFragment)
            }
        }*/

        binding.buttonFilters.setOnClickListener {
            Navigation.findNavController(requireView().parent.parent.parent.parent.parent.parent as View)
                .navigate(R.id.action_navFragment_to_filtersFragment)
        }
    }

    private fun getGridItemSize() {
        val displayMetrics = DisplayMetrics()

        requireActivity().windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        //val parms = AbsListView.LayoutParams(width, height)
        gridItemDim = (width / 3.5).toInt()
    }

}