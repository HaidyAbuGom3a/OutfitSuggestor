package com.example.outfitsuggestor.ui

import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.DataSource
import com.example.outfitsuggestor.utils.SharedPrefsUtil

class MainPresenter(
    private val mainViewInterface: MainViewInterface
) {
    private val dataSource = DataSource()
    fun getWeatherData() {
        dataSource.getWeatherData(
            90.0000,
            135.0000,
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