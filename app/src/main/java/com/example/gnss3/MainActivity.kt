package com.example.gnss3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssMeasurementsEvent
import android.location.GnssStatus
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun getConstellationTypeName(constellationType: Int): String {
    return when (constellationType) {
        GnssStatus.CONSTELLATION_GPS -> "GPS"
        GnssStatus.CONSTELLATION_SBAS -> "SBAS"
        GnssStatus.CONSTELLATION_GLONASS -> "GLONASS"
        GnssStatus.CONSTELLATION_QZSS -> "QZSS"
        GnssStatus.CONSTELLATION_BEIDOU -> "BEIDOU"
        GnssStatus.CONSTELLATION_GALILEO -> "GALILEO"
        else -> "Unknown"
    }
}
class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_LOCATION = 1
    private val TAG = "MainActivity"
    private lateinit var satelliteInfoTextView: TextView  // Объявите TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация TextView
        satelliteInfoTextView = findViewById(R.id.satelliteInfoTextView)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_LOCATION)
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val gnssMeasurementsListener = object : GnssMeasurementsEvent.Callback() {
            override fun onGnssMeasurementsReceived(event: GnssMeasurementsEvent) {
                val satelliteInfoStringBuilder = StringBuilder()

                for (measurement in event.measurements) {
                    val svid = measurement.svid
                    val constellationType = measurement.constellationType
                    val constellationTypeName = getConstellationTypeName(constellationType)
                    val carrierFrequencyHz = measurement.carrierFrequencyHz

                    // Добавить информацию о спутнике к StringBuilder
                    satelliteInfoStringBuilder.append("SVID: $svid Constellation: $constellationTypeName Frequency: $carrierFrequencyHz\n")
                }

                // Обновить TextView в основном потоке
                runOnUiThread {
                    satelliteInfoTextView.text = satelliteInfoStringBuilder.toString()
                }
            }
        }

        locationManager.registerGnssMeasurementsCallback(gnssMeasurementsListener)
    }
}
