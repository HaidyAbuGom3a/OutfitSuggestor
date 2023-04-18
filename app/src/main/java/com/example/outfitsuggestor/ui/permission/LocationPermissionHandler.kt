package com.example.outfitsuggestor.ui.permission

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.outfitsuggestor.utils.LocationUtils

class LocationPermissionHandler(
    private val activity: AppCompatActivity,
    private val context: Context
) : PermissionHandler {

    override fun getCurrentLocation(
        onLocationUpdated: (latitude: Double, longitude: Double) -> Unit,
        showLocationIsNull: () -> Unit,
        makeUserTurnOnLocation: () -> Unit
    ) {
        if (LocationUtils.checkLocationPermission(context)) {
            if (LocationUtils.isLocationEnabled(context)) {
                LocationUtils.getLastKnownLocation(activity) { location ->
                    if (location == null) {
                        showLocationIsNull()
                    } else {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        onLocationUpdated(latitude, longitude)
                    }
                }
            } else {
                makeUserTurnOnLocation()
            }
        } else {
            LocationUtils.requestLocationPermission(activity)
        }
    }
}
