package com.example.outfitsuggestor.data.source

import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.data.model.WeatherResponse

interface IDataSource {
    fun getWeatherData(
        latitude: Double?,
        longitude: Double?,
        location: String?,
        units: String,
        onSuccessCallback: (WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    )

    fun getAllOutfits(): List<Outfit>

    fun getOutfitsSuitableForRain(): List<Outfit>

    fun getLightWeightOutfits(): List<Outfit>

    fun getHeavyOutfits(): List<Outfit>

    fun getNeutralOutfits(): List<Outfit>


}