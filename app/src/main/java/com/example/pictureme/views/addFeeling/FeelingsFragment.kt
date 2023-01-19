package com.example.pictureme.views.addFeeling

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.data.models.Feeling
import com.example.pictureme.databinding.FragmentFeelingsBinding
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.PreviewPicmeViewModel
import com.example.pictureme.views.addFeeling.adapters.FeelingsAdapter
import com.example.pictureme.views.addFeeling.adapters.SelectedPosition

class FeelingsFragment(private val selectFoodFeelings: Boolean) : Fragment() {

    private var _binding: FragmentFeelingsBinding? = null
    private val binding get() = _binding!!

    private val previewViewModel by activityViewModels<PreviewPicmeViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()

    private lateinit var adapter: FeelingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeelingsBinding.inflate(inflater, container, false)

        var feelings: List<Feeling> = emptyList()
        adapter = FeelingsAdapter(feelings, previewViewModel, viewLifecycleOwner)

        previewViewModel.previewLiveData.observe(viewLifecycleOwner) { preview ->
            adapter.setPreview(preview)
        }

        binding.rvFoodFeelings.adapter = adapter
        binding.rvFoodFeelings.layoutManager = LinearLayoutManager(activity)

        if (selectFoodFeelings) {
            picmeViewModel.foodFeelingsLiveData.observe(viewLifecycleOwner) { response ->
                feelings = response
                adapter.setList(feelings)
            }
        } else {
            picmeViewModel.nonFoodFeelingsLiveData.observe(viewLifecycleOwner) { response ->
                feelings = response
                adapter.setList(feelings)

            }
        }

        return (binding.root)
    }

    override fun onResume() {
        super.onResume()
        SelectedPosition.changePage(selectFoodFeelings)
        adapter.notifyDataSetChanged()
    }
}