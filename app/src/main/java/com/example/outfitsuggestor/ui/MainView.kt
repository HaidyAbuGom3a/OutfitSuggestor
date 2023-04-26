package com.example.outfitsuggestor.ui

import com.example.outfitsuggestor.data.model.WeatherResponse

interface MainView {
    fun handleWeatherDataOnUi(response: WeatherResponse)
    fun showSomethingWentWrongInNetwork(error: Throwable)
    fun showLocationIsNull()
    fun makeUserTurnOnLocation()
    fun onLocationUpdated(latitude: Double, longitude: Double)
}