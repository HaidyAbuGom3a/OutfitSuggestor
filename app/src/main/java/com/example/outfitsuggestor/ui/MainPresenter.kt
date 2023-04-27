package com.example.outfitsuggestor.ui

import android.app.Activity
import android.content.Context
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.DataSourceImplementation
import com.example.outfitsuggestor.ui.permission.LocationPermissionHandler
import com.example.outfitsuggestor.utils.SharedPrefsUtil

class MainPresenter(
    activity: Activity,
    context: Context,
    private val mainView: MainView,
) {
    private val dataSourceImplementation = DataSourceImplementation()
    private val locationHandler = LocationPermissionHandler(activity, context)

    fun getWeatherData(location: String) {
        dataSourceImplementation.getWeatherData(
            null,
            null,
            location,
            "metric",
            ::onGetWeatherDataSuccess,
            ::onGetWeatherDataFailure
        )
    }

    fun getCityName(latitude: Double, longitude: Double) {
        dataSourceImplementation.getWeatherData(
            latitude,
            longitude,
            null,
            "metric",
            ::onGetWeatherDataSuccess,
            ::onGetWeatherDataFailure
        )
    }

    private fun onGetWeatherDataSuccess(response: WeatherResponse) {
        mainView.handleWeatherDataOnUi(response)
    }

    private fun onGetWeatherDataFailure(error: Throwable) {
        mainView.showRequestError(error)
    }

    fun handleLocationPermissions() {
        locationHandler.getCurrentLocation(
            ::handleLatitudeAndLongitude,
            ::showLocationIsNull,
            ::makeUserTurnOnLocation
        )
    }

    private fun handleLatitudeAndLongitude(latitude: Double, longitude: Double) {
        mainView.handleLatitudeAndLongitude(latitude, longitude)
    }

    private fun showLocationIsNull() {
        mainView.showLocationIsNull()
    }

    private fun makeUserTurnOnLocation() {
        mainView.makeUserTurnOnLocation()
    }

    fun getOutfitSuitableForRain(excludedOutfits: Set<String>?): Int {
        val outfitList =
            dataSourceImplementation.getOutfitsSuitableForRain().map { it.image.toString() }
                .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSourceImplementation.getOutfitsSuitableForRain().map { it.image.toString() }
                .random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getNeutralOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = dataSourceImplementation.getNeutralOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSourceImplementation.getNeutralOutfits().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getHeavyOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = dataSourceImplementation.getHeavyOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSourceImplementation.getHeavyOutfits().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getLightWeightOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList =
            dataSourceImplementation.getLightWeightOutfits().map { it.image.toString() }
                .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSourceImplementation.getLightWeightOutfits().map { it.image.toString() }
                .random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    private fun resetSavedOutfits(outfitList: Set<String>) {
        if (outfitList.isEmpty()) {
            SharedPrefsUtil.outfitList = null
        }
    }
}