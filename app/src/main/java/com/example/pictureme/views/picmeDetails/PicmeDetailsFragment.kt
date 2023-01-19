package com.example.pictureme.views.picmeDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentPicmeDetailsBinding
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.example.pictureme.utils.ShakeSensor
import com.example.pictureme.viewmodels.DistanceViewModel
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.views.picmeDetails.adapters.PicmeFriendsAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class PicmeDetailsFragment() : Fragment() {

    private var _binding: FragmentPicmeDetailsBinding? = null
    private val binding get() = _binding!!

    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()
    private val distanceViewModel by activityViewModels<DistanceViewModel>()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var picme: Picme

    private var picmeIndex: Int? = null

    private var task: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("CREATED DETAILS")
        _binding = FragmentPicmeDetailsBinding.inflate(inflater, container, false)
        picmeDetailsViewModel.picmeLiveData.observe(viewLifecycleOwner) { picme ->
            this.picme = picme
            loadImages()
            loadDetails()
            loadPicmeFriends()
        }
        val picmeIndexStr = arguments?.getString("picmeIndex")
        // Show Explore text
        if (picmeIndexStr != null) {
            picmeIndex = picmeIndexStr.toInt()
            ShakeSensor.stopShakeDetection()

            task = GlobalScope.launch {
                delay(2000)
                ShakeSensor.setShake(requireContext()) { phoneShake() }
            }


        }

        // Show Explore text
        if (picmeIndex != null) {
            // Change text if end is reached
            distanceViewModel.orderedPicmesLiveData.observe(viewLifecycleOwner) { orderedPicmes ->
                if (picmeIndex == orderedPicmes!!.size - 1) {
                    binding.textGo.text = "No more PicMe's! All you can do is..."
                }
            }

            binding.textGo.visibility = View.VISIBLE

        }

        binding.buttonGoBack.setOnClickListener {
            if (task != null) {
                task!!.cancel()
            }
            if (picmeIndex != null && picmeIndex != 0) {
                distanceViewModel.orderedPicmesLiveData.observe(viewLifecycleOwner) { orderedPicmes ->
                    picmeDetailsViewModel.selectPicme(orderedPicmes!![picmeIndex!! - 1])
                }
            }
//                Navigation.findNavController(requireView())
//                    .navigate(R.id.action_picmeDetailsFragment_to_navFragment)
            findNavController().popBackStack()
            //}

        }

        binding.buttonGoThere.setOnClickListener {
            mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val originLat = location.latitude
                    val originLng = location.longitude
                    val destLat = picme.location!!.latitude
                    val destLng = picme.location!!.longitude


                    //val uri = Uri.parse("google.navigation:q=$destLat,$destLng&mode=w")
                    val uri = Uri.parse("http://maps.google.com/maps?saddr=" +
                            "$originLat,$originLng&daddr=$destLat,$destLng")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage("com.google.android.apps.maps");
                    val packageManager = requireActivity().packageManager
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Google Maps not found", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    println("NO LOCATION")
                    throw Exception("No location")
                }
            }
        }

        return (binding.root)
    }

    // Only used in Explore Mode
    private fun phoneShake() {
        distanceViewModel.orderedPicmesLiveData.observe(viewLifecycleOwner) { orderedPicmes ->
            if (picmeIndex == orderedPicmes!!.size - 1) {
                Toast.makeText(requireContext(), "No more PicMe's!", Toast.LENGTH_SHORT).show()
            } else {
                val navController =
                    Navigation.findNavController(requireView())
                val bundle = Bundle()
                picmeIndex = picmeIndex!! + 1
                bundle.putString("picmeIndex", picmeIndex.toString())
                picmeDetailsViewModel.selectPicme(orderedPicmes[picmeIndex!!])
                navController.navigate(R.id.action_picmeDetailsFragment_self, bundle)
            }


        }
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
        println("DETAILS BYE BYE")
        //ShakeSensor.stopShakeDetection()
        _binding = null
    }

//    override fun onPause() {
//        super.onPause()
//        Sensor.stopShakeDetection()
//    }
}