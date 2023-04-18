package com.example.outfitsuggestor.data.source

import android.location.Location
import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.data.model.WeatherResponse

interface IDataSource {
    fun getWeatherData(
        latitude: Double?,
        longitude: Double?,
        units: String,
        onSuccessCallback: (WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    )

    fun getCurrentLocation(
        onLocationUpdated: (latitude: Double, longitude: Double) -> Unit,
        showLocationIsNull: () -> Unit,
        makeUserTurnOnLocation: () -> Unit
    )
    
    fun addOutfit(outfit: Outfit)

    fun getAllOutfits(): List<Outfit>

    fun getOutfitsSuitableForRain(): List<Outfit>

    fun getLightWeightOutfits(): List<Outfit>

    fun getHeavyOutfits(): List<Outfit>

    fun getNeutralOutfits(): List<Outfit>


}