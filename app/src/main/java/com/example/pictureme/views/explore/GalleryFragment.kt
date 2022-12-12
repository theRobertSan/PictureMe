package com.example.pictureme.views.explore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentGalleryBinding
import com.example.pictureme.views.explore.adapters.ImageAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val images = arrayOf(R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img3,
            R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3,
            R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3,
            R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3,
            R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3, R.drawable.img3,
            R.drawable.img3)

        val gridView = binding.fragmentGalleryGridView;
        gridView.adapter = context?.let { ImageAdapter(images, it) }
        gridView.setOnItemClickListener {
            adapter, view, i, l ->
                Toast.makeText(context, "clicked here", Toast.LENGTH_SHORT).show()

        }

        return (binding.root)
    }

}