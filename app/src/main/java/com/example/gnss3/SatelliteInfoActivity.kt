package com.example.gnss3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SatelliteInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satellite_info)

        val nmeaInfo = intent.getStringExtra("nmeaInfo")

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = nmeaInfo
    }
}
