package com.example.homework

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.math.atan2

abstract class Compass(context: Context, private val sensorType: Int) : SensorEventListener {

    var sensorValues: MutableState<List<Float>> = mutableStateOf(emptyList())
    var azimuth: MutableState<Float> = mutableStateOf(0f) // Azimuth angle in degrees

    abstract val isSensor: Boolean

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val sensor: Sensor? = sensorManager.getDefaultSensor(sensorType)

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            sensorValues.value = it.values.toList()
            calculateAzimuth(it.values)
        }
    }

    private fun calculateAzimuth(values: FloatArray) {
        val x = values[0]
        val y = values[1]
        azimuth.value = Math.toDegrees(atan2(y, x).toDouble()).toFloat() // Convert radians to degrees
    }

    fun start() {
        if (isSensor) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }
}