package com.example.outfitsuggestor.data.source

import com.example.outfitsuggestor.data.model.WeatherResponse

interface IDataSource {
    fun getWeatherData(
        latitude:Double,
        longitude:Double,
        units:String,
        onSuccessCallback: (WeatherResponse) -> Unit,
        onFailureCallback:  (error: Throwable) -> Unit
    )

}