package com.example.outfitsuggestor.ui.view

import com.example.outfitsuggestor.data.model.WeatherResponse

interface MainViewInterface {
    fun onWeatherSuccessResponse(response: WeatherResponse)
    fun onWeatherFailureResponse(error: Throwable)
}