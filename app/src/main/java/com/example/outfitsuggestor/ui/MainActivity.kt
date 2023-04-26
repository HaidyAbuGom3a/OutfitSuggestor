package com.example.outfitsuggestor.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.databinding.ActivityMainBinding
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.SharedPrefsUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.sql.Time
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherResponse: WeatherResponse
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
        addCallbacks()
        handleLocationPermissions()
        //getWeatherData()
        getWeatherDataWithTwoRequests(48.8566,2.3522)
        SharedPrefsUtil.initPrefsUtil(applicationContext)
        getUsedOutfits()
        eraseOldestOutfitAfterGivenDays(3)
    }

    private fun addCallbacks() {
        onClickSearchInput()
    }

    @SuppressLint("CheckResult")
    private fun onClickSearchInput() {
        val observable = Observable.create<String> { emitter ->
            binding.textInput.doOnTextChanged { text, start, before, count ->
                emitter.onNext(text.toString())
            }
        }
            .debounce(1,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(
            ::makeRequest,
            ::onError
        )
    }

    private fun makeRequest(location: String) {
        Log.i(TAG,location)
        presenter.getWeatherDataWithSearch(location)
    }

    private fun onError(e: Throwable) {
        Log.i(TAG,"something went wrong :(")
    }

    private fun getUsedOutfits() {
        usedOutfits = SharedPrefsUtil.outfitList
    }

    private fun getWeatherData() {
        presenter.getWeatherData()
    }

    @SuppressLint("CheckResult")
    private fun getWeatherDataWithTwoRequests(latitude:Double, longitude:Double){
        val observable = Observable.just(latitude,longitude)
            .flatMap {
                Observable.create<String> { emitter ->
                    presenter.getCityName(latitude,longitude)
                    emitter.onNext(weatherResponse.name)
                }
            }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(
            ::makeRequest,
            ::onError
        )
    }

    private fun handleLocationPermissions() {
        presenter.handleLocationPermissions()
    }

    override fun handleWeatherDataOnUi(response: WeatherResponse) {
        weatherResponse = response
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

    override fun showSomethingWentWrongInNetwork(error: Throwable) {
        Log.i(TAG, "failed")
    }

    override fun showLocationIsNull() {
        Toast.makeText(
            this,
            "Location is null",
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
        if (requestCode == Constants.PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                presenter.handleLocationPermissions()
            }
        }
    }

    companion object {
        private const val TAG = "ACTIVITY_MAIN"
    }

}