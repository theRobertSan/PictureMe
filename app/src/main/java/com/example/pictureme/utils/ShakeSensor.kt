package com.example.pictureme.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.*

object ShakeSensor {

    private var i = 0
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var shakeDetected = false
    private lateinit var toRunFunc: () -> Unit?
    private var sensorListener: SensorEventListener? = null

    fun setShake(context: Context, phoneShake: () -> Unit) {
        sensorListener = object : SensorEventListener {
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
                if (acceleration > 0.5f && !shakeDetected) {
                    shakeDetected = true
                    // Invoke desired function
                    toRunFunc.invoke()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        i++
        shakeDetected = false
        toRunFunc = phoneShake
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!
            .registerListener(
                sensorListener, sensorManager!!
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
            )
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    fun stopShakeDetection() {
        sensorManager?.unregisterListener(sensorListener)
        sensorManager = null
    }

}