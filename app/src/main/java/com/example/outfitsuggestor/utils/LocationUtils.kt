package com.example.outfitsuggestor.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

object LocationUtils {

    private const val PERMISSION_ID = 42

    fun checkLocationPermission(context: Context): Boolean {
        return (
                ActivityCompat.checkSelfPermission
                (
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission
                (
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }


    fun isLocationEnabled(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(activity: Activity, onLocationRetrieved: (Location?) -> Unit) {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
            val location: Location? = task.result
            onLocationRetrieved(location)
        }
    }
}
