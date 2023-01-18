package com.example.pictureme.views.explore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.squareup.picasso.Picasso
import java.net.URL
import kotlin.math.ceil


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
            ),
            "To take a PicMe, you have to enable those permissions."
        ) {
            cardViewMarker = binding.fragmentMapCardViewMarker
            cardViewCluster = binding.fragmentMapCardViewCluster

            binding.imageView5.setImageResource(R.drawable.avatar)

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
                    Log.d("mapdora", location.toString())
                    currentLocation = location
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom
                            (LatLng(currentLocation!!.latitude, currentLocation!!.longitude), 12f)
                    )
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
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            Looper.getMainLooper()
        )
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
        clusterManager!!.renderer = OwnIconRendered(context, mMap, clusterManager)

        mMap.setOnCameraIdleListener(clusterManager)
        clusterManager!!.markerCollection.setOnMarkerClickListener { marker: Marker ->
            val ret = onMarkerClick(marker)
            ret
        }

        clusterManager!!.setOnClusterClickListener { cluster ->
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

                    val item = PicmeClusterItem(lat, lng, picme.id!!, "s", picme.imagePath!!)
                    clusterManager!!.addItem(item)

                    //val latLng = LatLng(lat, lng)
                    //placeMarker(picme, latLng)

                }
            }
        }
    }

    inner class OwnIconRendered(
        context: Context?, map: GoogleMap?,
        clusterManager: ClusterManager<PicmeClusterItem>?
    ) :
        DefaultClusterRenderer<PicmeClusterItem>(context, map, clusterManager) {
        override fun onBeforeClusterItemRendered(item: PicmeClusterItem, markerOptions: MarkerOptions) {

                val imagePath = item.getImagePath()
                println("image path $imagePath")

                val imageView = binding.imageView5

              /*  imageView.load(imagePath) {
                    crossfade(true)
                    crossfade(1000)
                }*/

                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.launcher)
                val markerBitmap = createUserBitmap(bitmap)
                val icon = BitmapDescriptorFactory.fromBitmap(markerBitmap!!)
                markerOptions.icon(icon)

                /*Picasso.get().load(imagePath).into(imageView);
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                val bitmap2 = createUserBitmap(bitmap)
                val icon = BitmapDescriptorFactory.fromBitmap(bitmap2!!)
                markerOptions.icon(icon)*/

                /*Glide.with(requireActivity())
                    .asBitmap()
                    .load(imagePath)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val bitmap = createUserBitmap(null)

                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // do nothing
                        }
                    })*/



        }
    }

    fun createUserBitmap(bitmap: Bitmap): Bitmap? {
        var result: Bitmap? = null
        try {
            result = Bitmap.createBitmap(dp(62.toFloat()), dp(72.toFloat()), Bitmap.Config.ARGB_8888)
            result.eraseColor(Color.TRANSPARENT)
            val canvas = Canvas(result)
            val drawable = resources.getDrawable(R.drawable.livepin)
            drawable.setBounds(0, 0, dp(62.toFloat()), dp(76.toFloat()))
            drawable.draw(canvas)
            val roundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val bitmapRect = RectF()
            canvas.save()


            val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val matrix = Matrix()
            val scale = dp(52.toFloat()) / bitmap.width.toFloat()
            matrix.postTranslate(dp(5.toFloat()).toFloat(), dp(5.toFloat()).toFloat())
            matrix.postScale(scale, scale)
            roundPaint.shader = shader
            shader.setLocalMatrix(matrix)
            bitmapRect[dp(5.toFloat()).toFloat(), dp(5.toFloat()).toFloat(), dp(52 + 5.toFloat()).toFloat()] =
                dp(52 + 5.toFloat()).toFloat()
            canvas.drawRoundRect(bitmapRect, dp(26.toFloat()).toFloat(), dp(26.toFloat()).toFloat(), roundPaint)
            canvas.restore()
            try {
                canvas.setBitmap(null)
            } catch (e: Exception) {
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return result
    }

    fun dp(value: Float): Int {
        return if (value == 0f) {
            0
        } else ceil((resources.displayMetrics.density * value).toDouble()).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMarkerClick(marker: Marker): Boolean {
        hideCardView(cardViewCluster!!)
        showCardView(cardViewMarker!!)

        val id = marker.title
        val picme = id?.let { picmeViewModel.getPicme(it) }

        if (picme != null) {
            Pictures.loadPicme(
                picme.imagePath,
                binding.fragmentMapImageView,
                binding.fragmentMapLoadingBar
            )
            binding.fragmentMapTextView.text = Details.getRelativeDate(picme.createdAt!!)
            binding.fragmentMapImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        return false
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
