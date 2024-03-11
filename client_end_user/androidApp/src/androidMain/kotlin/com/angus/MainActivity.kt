package com.angus

import MainView
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.firebase.Firebase
import com.google.firebase.initialize
import dev.icerock.moko.geo.LocationTracker
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val locationTracker by inject<LocationTracker>()
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Firebase.initialize(this)
        super.onCreate(savedInstanceState)
        checkLocationPermission()
        locationTracker.bind(lifecycle, this, supportFragmentManager)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            this.window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            this.window.navigationBarColor =
                ContextCompat.getColor(this, android.R.color.transparent)
            MainView()
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                LOCATION_PERMISSION_REQUEST_CODE,
            )
        } else {
            // Permissions already granted - you can start using location functionality
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted - you can start using location functionality
            } else {
                // Permissions denied - handle the failure scenario
            }
        }
    }
}
