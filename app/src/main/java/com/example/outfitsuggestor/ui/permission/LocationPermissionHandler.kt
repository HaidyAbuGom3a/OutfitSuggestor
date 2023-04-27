package com.example.outfitsuggestor.ui.permission

import android.app.Activity
import android.content.Context
import com.example.outfitsuggestor.utils.LocationUtils

class LocationPermissionHandler(
    private val activity: Activity,
    private val context: Context
) : PermissionHandler {

    override fun getCurrentLocation(
        handleLatitudeAndLongitude: (latitude: Double, longitude: Double) -> Unit,
        showLocationIsNull: () -> Unit,
        makeUserTurnOnLocation: () -> Unit
    ) {
        if (LocationUtils.checkLocationPermission(context)) {
            if (LocationUtils.isLocationEnabled(context)) {
                LocationUtils.getLastKnownLocation(context,activity) { location ->
                    if (location == null) {
                        showLocationIsNull()
                    } else {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        handleLatitudeAndLongitude(latitude, longitude)
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
