package com.example.pictureme.views.go

import android.Manifest
import android.annotation.SuppressLint
import android.hardware.Sensor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentLetsgoBinding
import com.example.pictureme.utils.Permissions
import com.example.pictureme.utils.ShakeSensor
import com.example.pictureme.viewmodels.DistanceViewModel
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.go.adapters.GoViewPagerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class GoFragment : Fragment() {

    private var _binding: FragmentLetsgoBinding? = null
    private val binding get() = _binding!!

    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()
    private val distanceViewModel by activityViewModels<DistanceViewModel>()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var lastList: List<Picme>? = null
    private var lastListIgnored = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("--------------------------------------------_!!!")
        _binding = FragmentLetsgoBinding.inflate(inflater, container, false)

        ShakeSensor.stopShakeDetection()
        ShakeSensor.setShake(requireContext()) { phoneShake(binding.fragmentExploreTabs.selectedTabPosition) }
        setTabs()

        return (binding.root)
    }

    private fun setTabs() {
        val pager = binding.exploreViewPager
        val tl = binding.fragmentExploreTabs
        pager.adapter =
            GoViewPagerAdapter(
                requireActivity().supportFragmentManager,
                requireActivity().lifecycle
            )
        pager.isUserInputEnabled = false

        TabLayoutMediator(tl, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Explore"
                1 -> tab.text = "Eat"
            }
        }.attach()
    }

    @SuppressLint("MissingPermission")
    private fun phoneShake(selectedTabPosition: Int) {

        // If required permissions enabled, do something
        Permissions.checkPermissions(
            requireContext(),
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            "To explore your PicMes, you have to enable this permission."
        ) {
            picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { picmes ->
                // Get current location
                mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLocation = "${location.latitude},${location.longitude}"
                        val apiKey = requireContext().getString(R.string.MAPS_API_KEY)
                        distanceViewModel.getDistanceOrderedPicmes(
                            currentLocation,
                            picmes,
                            selectedTabPosition == 1,
                            apiKey
                        )
                    } else {
                        println("NO LOCATION")
                        throw Exception("No location")
                    }
                }
            }

            distanceViewModel.orderedPicmesLiveData
                .observe(
                    viewLifecycleOwner
                ) { newValue ->
                    if (newValue != lastList || lastListIgnored) {
                        lastListIgnored = false
                        val navController =
                            Navigation.findNavController(requireView().parent.parent as View)
                        val bundle = Bundle()
                        bundle.putString("picmeIndex", "0")
                        println("BRUUUU: ${newValue!![0]}")
                        picmeDetailsViewModel.selectPicme(newValue[0])
                        lastList = newValue
                        navController.navigate(
                            R.id.action_navFragment_to_picmeDetailsFragment,
                            bundle
                        )
                    } else {
                        lastListIgnored = true
                    }

                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        ShakeSensor.stopShakeDetection()
        _binding = null
    }


}

//if (oldValue == null) {
//    val navController =
//        Navigation.findNavController(requireView().parent.parent as View)
//    val bundle = Bundle()
//    bundle.putString("picmeIndex", "0")
//    println("BRUUUU: ${newValue!![0]}")
//    picmeDetailsViewModel.selectPicme(newValue[0])
//
//    navController.navigate(R.id.action_navFragment_to_picmeDetailsFragment, bundle)
//}