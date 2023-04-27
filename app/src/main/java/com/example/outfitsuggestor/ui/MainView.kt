package com.example.outfitsuggestor.ui

import com.example.outfitsuggestor.data.model.WeatherResponse

interface MainView {
    fun handleWeatherDataOnUi(response: WeatherResponse)
    fun showRequestError(error: Throwable)
    fun showLocationIsNull()
    fun makeUserTurnOnLocation()
    fun handleLatitudeAndLongitude(latitude: Double, longitude: Double)
}