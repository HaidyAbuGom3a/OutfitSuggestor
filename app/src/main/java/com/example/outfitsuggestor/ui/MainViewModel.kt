package com.example.outfitsuggestor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.repository.Repository
import com.example.outfitsuggestor.utils.SharedPrefsUtil

class MainViewModel : ViewModel() {
    private val repository = Repository()

    private val _response = MutableLiveData<WeatherResponse>()
    val response: LiveData<WeatherResponse>
        get() = _response
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error


    fun getWeatherData(location: String) {
        repository.getWeatherData(
            null,
            null,
            location,
            "metric",
            ::onGetWeatherDataSuccess,
            ::onGetWeatherDataFailure
        )
    }

    fun getCityName(latitude: Double, longitude: Double) {
        repository.getWeatherData(
            latitude,
            longitude,
            null,
            "metric",
            ::onGetWeatherDataSuccess,
            ::onGetWeatherDataFailure
        )
    }

    private fun onGetWeatherDataSuccess(response: WeatherResponse) {
        _response.postValue(response)
    }

    private fun onGetWeatherDataFailure(error: Throwable) {
        _error.postValue(error)
    }

    fun getOutfitSuitableForRain(excludedOutfits: Set<String>?): Int {
        val outfitList =
            repository.getOutfitsSuitableForRain().map { it.image.toString() }
                .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return repository.getOutfitsSuitableForRain().map { it.image.toString() }
                .random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getNeutralOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = repository.getNeutralOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return repository.getNeutralOutfits().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getHeavyOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList = repository.getHeavyOutfits().map { it.image.toString() }
            .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return repository.getHeavyOutfits().map { it.image.toString() }.random()
                .toInt()
        }
        return outfitList.random().toInt()
    }

    fun getLightWeightOutfit(excludedOutfits: Set<String>?): Int {
        val outfitList =
            repository.getLightWeightOutfits().map { it.image.toString() }
                .subtract((excludedOutfits ?: emptySet()).toSet())
        if (outfitList.isEmpty()) {
            resetSavedOutfits(outfitList)
            return repository.getLightWeightOutfits().map { it.image.toString() }
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