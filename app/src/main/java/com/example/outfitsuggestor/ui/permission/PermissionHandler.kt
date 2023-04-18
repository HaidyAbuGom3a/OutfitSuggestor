package com.example.outfitsuggestor.ui.permission

interface PermissionHandler {
    fun getCurrentLocation(
        onLocationUpdated: (latitude: Double, longitude: Double) -> Unit,
        showLocationIsNull: () -> Unit,
        makeUserTurnOnLocation: () -> Unit
    )
}
