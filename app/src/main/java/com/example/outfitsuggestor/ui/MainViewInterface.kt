package com.example.outfitsuggestor.ui

import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.data.model.WeatherResponse

interface MainViewInterface {
    fun onWeatherSuccessResponse(response: WeatherResponse)
    fun onWeatherFailureResponse(error: Throwable)
    fun showLocationIsNull()
    fun makeUserTurnOnLocation()
}