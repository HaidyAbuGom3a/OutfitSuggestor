package com.example.outfitsuggestor.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.databinding.ActivityMainBinding
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.SharedPrefsUtil

class MainActivity : AppCompatActivity(), MainViewInterface {
    private lateinit var binding: ActivityMainBinding
    private val presenter by lazy {
        MainPresenter(this, applicationContext, this)
    }
    private var usedOutfits: Set<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()

    }

    private fun setUp() {
        handleLocationPermissions()
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

    private fun handleLocationPermissions() {
        presenter.handleLocationPermissions()
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
        Toast.makeText(
            this,
            "Something went wrong",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showLocationIsNull() {
        Toast.makeText(
            this,
            "Location is null please try another one",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun makeUserTurnOnLocation() {
        Toast.makeText(this, "Turn on location ", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Granted",Toast.LENGTH_SHORT).show()
                presenter.handleLocationPermissions()
            }
        }
    }

}