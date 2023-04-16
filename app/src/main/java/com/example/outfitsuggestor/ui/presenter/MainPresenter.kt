package com.example.outfitsuggestor.ui.presenter

import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.DataSource
import com.example.outfitsuggestor.ui.view.MainViewInterface

class MainPresenter(
    private val mainViewInterface: MainViewInterface
) {
    fun getWeatherData() {
        DataSource().getWeatherData(
            44.3,
            10.99,
            "metric",
            ::onGetWeatherDataSuccess,
            ::onGetWeatherDataFailure
        )
    }

    private fun onGetWeatherDataSuccess(response: WeatherResponse) {
        mainViewInterface.onWeatherSuccessResponse(response)
    }

    private fun onGetWeatherDataFailure(error: Throwable) {
        mainViewInterface.onWeatherFailureResponse(error)
    }

}