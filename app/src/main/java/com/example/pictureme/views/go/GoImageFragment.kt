package com.example.pictureme.views.go

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentGoImageBinding

class GoImageFragment(private val isFoodTab: Boolean) : Fragment() {

    private var _binding: FragmentGoImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoImageBinding.inflate(inflater, container, false)

        if (isFoodTab) {
            binding.image.setImageResource(R.drawable.ic_shake_drink)
            binding.text.text = "Shake for a food PicMe"
        } else {
            binding.image.setImageResource(R.drawable.ic_shake_phone)
            binding.text.text = "Shake for a PicMe to go explore"
        }

        return (binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}