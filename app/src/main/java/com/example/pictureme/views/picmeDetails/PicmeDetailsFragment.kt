package com.example.pictureme.views.picmeDetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentPicmeDetailsBinding
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.DistanceViewModel
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.views.picmeDetails.adapters.PicmeFriendsAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PicmeDetailsFragment : Fragment() {

    private var _binding: FragmentPicmeDetailsBinding? = null
    private val binding get() = _binding!!

    private val picmeDetailsViewModelViewModel by activityViewModels<PicmeDetailsViewModel>()
    private val distanceViewModel by activityViewModels<DistanceViewModel>()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var picme: Picme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

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

        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return (binding.root)
    }

    private fun loadPicmeFriends() {
        // If no friends, hide rv
        if (picme.friends.isEmpty()) {
            binding.cvFriends.visibility = View.GONE
        } else {
            val picmeFriendsAdapter = PicmeFriendsAdapter(picme.friends)
            binding.rcPicmeFriends.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rcPicmeFriends.adapter = picmeFriendsAdapter
        }

    }

    private fun loadImages() {
        // Load picme image
        Pictures.loadPicme(
            picme.imagePath,
            binding.imagePicme,
            binding.picmeLoadingBar
        )

        // Load feeling
        binding.imageEmotion.setImageResource(Details.getFeelingImage(picme.feeling!!.feeling)!!)

        // Check whether the user has a profile picture or not
        Pictures.loadProfilePicture(
            picme.creator!!.profilePicturePath,
            binding.imageCreator,
            binding.imageLoadingBar
        )

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

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDetails() {
        binding.textExactTime.text = Details.getExactDate(picme.createdAt!!)
        binding.textExactLocation.text =
            Details.getExactLocation(picme.location!!, requireContext())
        binding.textRelativeTime.text = Details.getRelativeDate(picme.createdAt!!)

        binding.textFeeling.text = picme.feeling!!.feeling
        binding.textCreator.text = Details.getPrettyNameFormat(picme.creator!!.fullName!!)

        // Get current location
        mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                println("HELEELELELELE")
                val currentLocation = "${location.latitude},${location.longitude}"
                val destinationLocation =
                    "${picme.location!!.latitude},${picme.location!!.longitude}"
                val apiKey = requireContext().getString(R.string.MAPS_API_KEY)
                distanceViewModel.getDistanceTo(currentLocation, destinationLocation, apiKey)
            } else {
                println("NO LOCATION")
                throw Exception("No location")
            }
        }

        distanceViewModel.distanceLiveData.observe(viewLifecycleOwner) { distance ->
            binding.textRelativeLocation.text = distance
        }

//        val client = ApiClient.apiService.getDistance()
//        GlobalScope.launch(Dispatchers.IO) {
//            picmeDetailsViewModelViewModel.getRelativeLocation(picme.location!!, requireContext())
//        }
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