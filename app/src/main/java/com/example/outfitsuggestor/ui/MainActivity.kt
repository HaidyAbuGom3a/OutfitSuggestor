package com.example.outfitsuggestor.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.databinding.ActivityMainBinding
import com.example.outfitsuggestor.utils.SharedPrefsUtil

class MainActivity : AppCompatActivity(), MainViewInterface {
    private lateinit var binding: ActivityMainBinding
    private val presenter by lazy {
        MainPresenter(this)
    }
    private var usedOutfits: Set<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()

    }

    private fun setUp() {
        getWeatherData()
        SharedPrefsUtil.initPrefsUtil(applicationContext)
        getUsedOutfits()
        eraseOldestOutfitAfterGivenDays(3)
    }

    private fun getUsedOutfits() {
        usedOutfits = SharedPrefsUtil.outfitList
    }

    private fun getWeatherData() {
        presenter.getWeatherData()
    }

    override fun onWeatherSuccessResponse(response: WeatherResponse) {
        runOnUiThread {
            val weatherCondition = response.weather[0].main
            val temperature = response.main.temp.toInt()
            val location = response.name + ", " + response.sys.country
            showWeatherData(weatherCondition, temperature, location)
            showBackgroundBasedOnWeatherCondition(weatherCondition)
            suggestOutfit(weatherCondition, temperature)
        }
    }

    private fun showWeatherData(weatherCondition: String, temperature: Int, location: String) {
        binding.textWeatherCondition.text = weatherCondition
        binding.textTemperature.text = temperature.toString()
        binding.textLocation.text = location
    }

    private fun suggestOutfit(weatherCondition: String, temperature: Int) {
        val outfitImage: Int = if (weatherCondition == "Rain") {
            presenter.getOutfitSuitableForRain(usedOutfits)
        } else {
            if (temperature > 25) {
                presenter.getLightWeightOutfit(usedOutfits)
            } else if (temperature in 10..25) {
                presenter.getNeutralOutfit(usedOutfits)
            } else {
                presenter.getHeavyOutfit(usedOutfits)
            }
        }
        showOutfit(outfitImage)
        saveOutfit(outfitImage)
    }

    private fun saveOutfit(outfit: Int) {
        usedOutfits = (usedOutfits ?: emptySet()).plus(setOf(outfit.toString()))
        SharedPrefsUtil.outfitList = usedOutfits
    }

    private fun eraseOldestOutfitAfterGivenDays(days: Int) {
        if ((usedOutfits?.size ?: 0) > days) {
            val outfitList = SharedPrefsUtil.outfitList
            SharedPrefsUtil.outfitList = outfitList?.drop(1)?.toSet()
        }
    }

    private fun showOutfit(outfit: Int) {
        binding.outfit.background = ContextCompat.getDrawable(this, outfit)
    }

    override fun onWeatherFailureResponse(error: Throwable) {
        Log.i("ACTIVITY_MAIN", "failed")
    }


    private fun showBackgroundBasedOnWeatherCondition(weatherCondition: String) {
        when (weatherCondition) {
            "Clear" -> setBackground(R.drawable.bg_clear)
            "Clouds" -> setBackground(R.drawable.bg_scattered_clouds)
            "Drizzle" -> setBackground(R.drawable.bg_shower_rain)
            "Rain" -> setBackground(R.drawable.bg_rain)
            "Thunderstorm" -> setBackground(R.drawable.bg_thunderstorm)
            "Snow" -> setBackground(R.drawable.bg_snow)
            "Atmosphere" -> setBackground(R.drawable.bg_mist)
        }
    }

    private fun setBackground(drawable: Int) {
        binding.root.background = ContextCompat.getDrawable(this, drawable)
    }

}