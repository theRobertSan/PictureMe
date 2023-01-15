package com.example.pictureme.views.picmeDetails

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentHomeBinding
import com.example.pictureme.databinding.FragmentPicmeDetailsBinding
import com.example.pictureme.utils.Details
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.home.adapters.PicmeAdapter
import com.example.pictureme.views.picmeDetails.adapters.PicmeFriendsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope

@AndroidEntryPoint
class PicmeDetailsFragment : Fragment() {

    private var _binding: FragmentPicmeDetailsBinding? = null
    private val binding get() = _binding!!

    private val picmeDetailsViewModelViewModel by activityViewModels<PicmeDetailsViewModel>()

    private lateinit var picme: Picme

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPicmeDetailsBinding.inflate(inflater, container, false)
        // Get picme passed as argument
//        picme = (arguments?.getSerializable("picme")!!) as Picme
        picmeDetailsViewModelViewModel.picmeLiveData.observe(viewLifecycleOwner) { picme ->
            this.picme = picme
            loadImages()
            loadDetails()
            loadPicmeFriends()
        }

        return (binding.root)
    }

    private fun loadPicmeFriends() {
        val picmeFriendsAdapter = PicmeFriendsAdapter(picme.friends)
        binding.rcPicmeFriends.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcPicmeFriends.adapter = picmeFriendsAdapter
    }

    private fun loadImages() {
        // Load picme image
        binding.imagePicme.load(picme.imagePath) {
            crossfade(true)
            crossfade(1000)
            listener { _, _ ->
                binding.picmeLoadingBar.isGone = true
            }
        }

//        binding.imagePicme.setOnClickListener {
//            if (zoomOut) {
//                binding.imagePicme.f
//            }
//        }

        // Load creator image
//        binding.imageCreator.load(picme.crea) {
//            crossfade(true)
//            crossfade(1000)
//            listener { _, _ ->
//                binding.picmeLoadingBar.isGone = true
//            }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDetails() {
        binding.textExactTime.text = Details.getExactDate(picme.createdAt!!)
        binding.textExactLocation.text =
            Details.getExactLocation(picme.location!!, requireContext())
        binding.textRelativeTime.text = Details.getRelativeDate(picme.createdAt!!)

        binding.textFeeling.text = picme.feeling!!.feeling
        binding.textCreator.text = picme.creator!!.username

//        picmeDetailsViewModelViewModel.getRelativeLocation(picme.location!!, requireContext())
//        // When data received, update text
//        picmeDetailsViewModelViewModel.relativeLocationLiveData.observe(viewLifecycleOwner) { relativeLocation ->
//            binding.textRelativeLocation.text = relativeLocation
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}