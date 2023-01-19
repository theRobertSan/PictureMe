package com.example.pictureme.views.explore

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.*
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.R
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentMapBinding
import com.example.pictureme.utils.Details
import com.example.pictureme.utils.Permissions
import com.example.pictureme.utils.Pictures
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.explore.adapters.ClusterPicmeAdapter
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
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
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null
    private var currentLocation: Location? = null
    private var requestingLocationUpdates: Boolean = true

    // Cluster
    private var clusterManager: ClusterManager<PicmeClusterItem>? = null

    // Card Views
    private var cardViewMarker: MaterialCardView? = null
    private var cardViewCluster: MaterialCardView? = null

    // PicMes
    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()

    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup FusedLocationProviderClient
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // If required permissions enabled, do something
        Permissions.checkPermissions(
            requireContext(),
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            "To take a PicMe, you have to enable those permissions."
        ) {
            cardViewMarker = binding.fragmentMapCardViewMarker
            cardViewCluster = binding.fragmentMapCardViewCluster

            // Setup SupportMapFragment
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.fragment_map_googleMap) as SupportMapFragment
            mapFragment.getMapAsync(this)

            // Setup Location Request
            createLocationRequest()

            // Location Updates
            setupLocationUpdates()

            // Hide card view for marker clicking
            hideCardViews()
        }

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
                    currentLocation = location
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if(fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest!!,
                locationCallback!!,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
        }
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

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupMap() {

        // Map stuff
        mMap.isMyLocationEnabled = true
        mMap.setOnMapClickListener(this)

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> mMap.setMapStyle(MapStyleOptions(resources.getString(R.string.style_json)))
        }

        // Cluster Manager Stuff
        clusterManager = ClusterManager(context, mMap)
        mMap.setOnCameraIdleListener(clusterManager)

        clusterManager!!.markerCollection.setOnMarkerClickListener { marker: Marker ->
            val ret = onMarkerClick(marker)
            this.marker = marker
            ret
        }

        clusterManager!!.setOnClusterClickListener { cluster ->
            val ret = onClusterClick(cluster)
            ret
        }

        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom
                        (LatLng(currentLocation.latitude, currentLocation.longitude), 12f)
                )
            }
        }
    }

    private fun setupPicmeMarkers() {
        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
            for (picme in response) {
                val geoPoint = picme.location
                if (geoPoint is GeoPoint) {
                    val lat = geoPoint.latitude
                    val lng = geoPoint.longitude
                    val item = PicmeClusterItem(lat, lng, picme.id!!, "s", picme.imagePath!!)
                    clusterManager!!.addItem(item)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMarkerClick(marker: Marker): Boolean {
        hideCardView(cardViewCluster!!)
        showCardView(cardViewMarker!!)

        val id = marker.title
        val picme = picmeViewModel.getPicme(id!!)

        marker.hideInfoWindow()

        Pictures.loadPicme(
            picme.imagePath,
            binding.fragmentMapImageView,
            binding.fragmentMapLoadingBar
        )

        binding.fragmentMapTextView.text = Details.getRelativeDate(picme.createdAt!!)
        if (picme.friends.isNotEmpty()) {
            binding.textFriendsNum.text = picme.friends.size.toString()
            binding.textFriendsNum.visibility = View.VISIBLE
        }

        binding.fragmentMapCardViewMarker.setOnClickListener {
            // This is a sin x2
            val navigation =
                Navigation.findNavController(it.parent.parent.parent.parent.parent.parent.parent.parent.parent.parent as View)
            picmeDetailsViewModel.selectPicme(picme)
            navigation.navigate(R.id.action_navFragment_to_picmeDetailsFragment)
        }

        return true
    }

    private fun onClusterClick(cluster: Cluster<PicmeClusterItem>): Boolean {
        hideCardView(cardViewMarker!!)
        showCardView(cardViewCluster!!)
        val picmes = mutableListOf<Picme>()

        for (item in cluster.items) {
            val id = item.title
            val picme = id?.let { picmeViewModel.getPicme(it) }
            if (picme != null) {
                picmes.add(picme)
            }
        }

        val sortedPicmes = sortPerDate(picmes)

        val clusterPicmeAdapter = ClusterPicmeAdapter(sortedPicmes, picmeDetailsViewModel)
        binding.fragmentMapRvCluster.adapter = clusterPicmeAdapter
        binding.fragmentMapRvCluster.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        return false
    }

    override fun onMapClick(latLng: LatLng) {
        hideCardViews()
    }

    private fun hideCardViews() {
        cardViewMarker!!.visibility = View.GONE
        cardViewCluster!!.visibility = View.GONE
    }

    private fun hideCardView(cardView: MaterialCardView) {
        cardView.visibility = View.GONE
    }

    private fun showCardView(cardView: MaterialCardView) {
        cardView.visibility = View.VISIBLE
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

    // Inner class for cluster items
    inner class PicmeClusterItem(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String,
        imagePath: String
    ) : ClusterItem {

        private val position: LatLng
        private val title: String
        private val snippet: String
        private val imagePath: String

        override fun getPosition(): LatLng {
            return position
        }

        override fun getTitle(): String? {
            return title
        }

        override fun getSnippet(): String? {
            return snippet
        }

        fun getImagePath(): String {
            return imagePath
        }

        init {
            position = LatLng(lat, lng)
            this.title = title
            this.snippet = snippet
            this.imagePath = imagePath
        }
    }


}
