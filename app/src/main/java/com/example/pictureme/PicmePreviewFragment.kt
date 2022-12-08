package com.example.pictureme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.pictureme.databinding.FragmentLoginBinding
import com.example.pictureme.databinding.FragmentPicmePreviewBinding

class PicmePreviewFragment : Fragment() {

    private var _binding: FragmentPicmePreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicmePreviewBinding.inflate(inflater, container, false);

        binding.test.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_picmePreviewFragment_to_navFragment)
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}