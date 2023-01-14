package com.example.pictureme.views.picmeDetails

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentHomeBinding
import com.example.pictureme.databinding.FragmentPicmeDetailsBinding
import com.example.pictureme.utils.Details
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PicmeDetailsFragment : Fragment() {
    private var _binding: FragmentPicmeDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var picme: Picme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicmeDetailsBinding.inflate(inflater, container, false)
        // Get picme passed as argument
        picme = arguments?.getSerializable("picme") as Picme

//        binding.textExactTime.text = Details.getExactDate(picme.createdAt!!)
        binding.textExactLocation.text =
            Details.getExactLocation(picme.location!!, requireContext())
        // Inflate the layout for this fragment
        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}