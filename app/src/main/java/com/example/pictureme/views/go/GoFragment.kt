package com.example.pictureme.views.go

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.pictureme.R
import com.example.pictureme.databinding.FragmentAddFeelingBinding
import com.example.pictureme.databinding.FragmentGoImageBinding
import com.example.pictureme.databinding.FragmentLetsgoBinding
import com.example.pictureme.viewmodels.DistanceViewModel
import com.example.pictureme.viewmodels.PicmeDetailsViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.views.addFeeling.adapters.AddFeelingViewPagerAdapter
import com.example.pictureme.views.go.adapters.GoViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Math.sqrt
import java.util.*
import kotlin.random.Random.Default.nextInt

class GoFragment : Fragment() {

    private var _binding: FragmentLetsgoBinding? = null
    private val binding get() = _binding!!

    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val picmeDetailsViewModel by activityViewModels<PicmeDetailsViewModel>()
    private val distanceViewModel by activityViewModels<DistanceViewModel>()


    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var shakeDetected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Getting the Sensor Manager instance
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!
            .registerListener(
                sensorListener, sensorManager!!
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
            )
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = kotlin.math.sqrt((x * x + y * y + z * z).toDouble())
                .toFloat() / SensorManager.GRAVITY_EARTH
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            // acceleration value is over 12
            if (acceleration > 0.4f && !shakeDetected) {
                shakeDetected = true
                phoneShake()

            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLetsgoBinding.inflate(inflater, container, false)
        shakeDetected = false
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

    private fun phoneShake() {
//        picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { picmes ->
//            distanceViewModel.getDistanceOrderedPicmes(picmes, false, )
////            picmeDetailsViewModel.selectPicme(picmes.random())
////            Navigation.findNavController(requireView().parent.parent as View)
////                .navigate(R.id.action_navFragment_to_picmeDetailsFragment)
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}