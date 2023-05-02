package com.example.outfitsuggestor.ui.permission

interface LocationHandler {
    fun getCurrentLocation(
        handleLatitudeAndLongitude: (latitude: Double, longitude: Double) -> Unit,
        showLocationIsNull: () -> Unit,
        makeUserTurnOnLocation: () -> Unit
    )
}
