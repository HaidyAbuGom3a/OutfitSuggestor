package com.example.outfitsuggestor.data.source.repository

import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.RemoteDataSourceImp

class Repository {
    private val dataSource = RemoteDataSourceImp()

    fun getWeatherData(
        latitude: Double?,
        longitude: Double?,
        location: String?,
        units: String,
        onSuccess: (WeatherResponse) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) = dataSource.getWeatherData(latitude, longitude, location, units, onSuccess, onFailure)

    fun getAllOutfits() = dataSource.getAllOutfits()

    fun getOutfitsSuitableForRain() = dataSource.getOutfitsSuitableForRain()

    fun getLightWeightOutfits() = dataSource.getLightWeightOutfits()

    fun getHeavyOutfits() = dataSource.getHeavyOutfits()

    fun getNeutralOutfits() = dataSource.getNeutralOutfits()

}