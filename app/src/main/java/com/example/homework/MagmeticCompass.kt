package com.example.homework

import android.content.Context
import android.hardware.Sensor

class MagneticCompass(context: Context) : Compass(context, Sensor.TYPE_MAGNETIC_FIELD) {

    override val isSensor: Boolean
        get() = sensor != null
}