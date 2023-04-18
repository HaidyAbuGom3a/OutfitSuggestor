package com.example.outfitsuggestor.ui

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.DataSource
import com.example.outfitsuggestor.ui.permission.LocationPermissionHandler
import com.example.outfitsuggestor.utils.SharedPrefsUtil

class MainPresenter(
    activity: AppCompatActivity,
    context: Context,
    private val mainViewInterface: MainViewInterface,
) {
    private val dataSource = DataSource()
    private val locationHandler = LocationPermissionHandler(activity,context)
    private var mlatitude:Double? = null
    private var mlongitude:Double? = null
    fun getWeatherData() {
        Log.i("INSIDE_WEATHER","$mlatitude")
        dataSource.getWeatherData(
            mlatitude,
            mlongitude,
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

    fun handleLocationPermissions() {
        locationHandler.getCurrentLocation(
            ::onLocationUpdated,
            ::showLocationIsNull,
            ::makeUserTurnOnLocation
        )
    }

    private fun onLocationUpdated(latitude: Double, longitude: Double){
        mlatitude = latitude
        mlongitude = longitude
        Log.i("NOT_NULLLL","$mlatitude")
    }

    private fun showLocationIsNull() {
        mainViewInterface.showLocationIsNull()
    }

    private fun makeUserTurnOnLocation() {
        mainViewInterface.makeUserTurnOnLocation()
    }

    fun getOutfitSuitableForRain(excludedOutfits: Set<String>?): Int {
        val outfitList = dataSource.getOutfitsSuitableForRain().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSource.getOutfitsSuitableForRain().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getNeutralOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = dataSource.getNeutralOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSource.getNeutralOutfits().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getHeavyOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = dataSource.getHeavyOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSource.getHeavyOutfits().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getLightWeightOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = dataSource.getLightWeightOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return dataSource.getLightWeightOutfits().map { it.image.toString() }.random()
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