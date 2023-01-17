package com.example.pictureme.views.explore

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentMapBinding
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.explore.adapters.ClusterPicmeAdapter
import com.example.pictureme.views.picmeDetails.adapters.PicmeFriendsAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager


class MapFragment : Fragment(), OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    // Google Maps
    private lateinit var mMap: GoogleMap

    // Location Tracking
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var currentLocation: Location
    private var requestingLocationUpdates: Boolean = true

    // Cluster
    private lateinit var clusterManager: ClusterManager<PicmeClusterItem>

    // Card Views
    private lateinit var cardViewMarker: MaterialCardView
    private lateinit var cardViewCluster: MaterialCardView

    // PicMes
    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        cardViewMarker = binding.fragmentMapCardViewMarker
        cardViewCluster =  binding.fragmentMapCardViewCluster

        // Setup SupportMapFragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map_googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Setup Location Request
        createLocationRequest()

        // Location Updates
        setupLocationUpdates()

        // Hide card view for marker clicking
        hideCardViews()

        return (binding.root)
    }

    // ---------------------------- LOCATION ----------------------------

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun setupLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d("mapdora", location.toString())
                    currentLocation = location
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                        (LatLng(currentLocation.latitude, currentLocation.longitude), 12f))
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // ---------------------------- MAP ----------------------------

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupMap()
        setupPicmeMarkers()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Map stuff
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener(this)

        // Cluster Manager Stuff
        clusterManager = ClusterManager(context, mMap)
        mMap.setOnCameraIdleListener(clusterManager)
        clusterManager.markerCollection.setOnMarkerClickListener { marker: Marker ->
            val ret = onMarkerClick(marker)
            ret
        }

        clusterManager.setOnClusterClickListener { cluster ->
            val ret = onClusterClick(cluster)
            ret
        }
    }

    private fun setupPicmeMarkers() {
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            println("DATE CHANGED ------------" + response.size)
            for (picme in response) {
                val geoPoint = picme.location
                if (geoPoint is GeoPoint) {
                    val lat = geoPoint.latitude
                    val lng = geoPoint.longitude

                    val item = picme.id?.let { PicmeClusterItem(lat, lng, it, "s") }
                    clusterManager.addItem(item)

                    //val latLng = LatLng(lat, lng)
                    //placeMarker(picme, latLng)

                }
            }
        }
    }

    private fun sortPerDate(list: List<Picme>): List<Picme> {
        val result = mutableListOf<Picme>()
        for (item in list) {
            result.add(item)
        }
        result.sortBy { it.createdAt }
        result.reverse()
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMarkerClick(marker: Marker): Boolean {
        hideCardView(cardViewCluster)
        showCardView(cardViewMarker)

        val id = marker.title
        val picme = id?.let { picmeViewModel.getPicme(it) }

        if (picme != null) {
            Pictures.loadPicme(picme.imagePath, binding.fragmentMapImageView, binding.fragmentMapLoadingBar)
            binding.fragmentMapTextView.text = Details.getRelativeDate(picme.createdAt!!)
            binding.fragmentMapImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        return false
    }

    private fun onClusterClick(cluster: Cluster<PicmeClusterItem>): Boolean{
        hideCardView(cardViewMarker)
        showCardView(cardViewCluster)
        val picmes = mutableListOf<Picme>()

        for(item in cluster.items) {
            val id = item.title
            val picme = id?.let { picmeViewModel.getPicme(it) }
            if (picme != null) {
                picmes.add(picme)
            }
        }

        val sortedPicmes = sortPerDate(picmes)

        val clusterPicmeAdapter = ClusterPicmeAdapter(sortedPicmes)
        binding.fragmentMapRvCluster.adapter = clusterPicmeAdapter
        binding.fragmentMapRvCluster.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        return false
    }

    override fun onMapClick(latLng: LatLng) {
        hideCardViews()
    }

    private fun hideCardViews() {
        cardViewMarker.visibility = View.GONE
        cardViewCluster.visibility = View.GONE
    }

    private fun hideCardView(cardView: MaterialCardView) {
        cardView.visibility = View.GONE
    }

    private fun showCardView(cardView: MaterialCardView) {
        cardView.visibility = View.VISIBLE
    }

    private fun placeMarker(picme: Picme, currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong").visible(false)
        val marker = mMap.addMarker(markerOptions)
        marker!!.tag = picme
    }

    // Inner class for cluster items
    inner class PicmeClusterItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
        }
    }



}
