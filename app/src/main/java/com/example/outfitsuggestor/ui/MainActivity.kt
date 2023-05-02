package com.example.outfitsuggestor.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.databinding.ActivityMainBinding
import com.example.outfitsuggestor.ui.permission.LocationHandlerImp
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.SharedPrefsUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private val locationHandler by lazy { LocationHandlerImp(this,applicationContext) }
    private val TAG = this::class.java.simpleName.toString()

    private var usedOutfits: Set<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    private fun setUp() {
        handleLocationPermissions()
        updateUi()
        handleErrorResponse()
        addCallbacks()
        SharedPrefsUtil.initPrefsUtil(applicationContext)
        getUsedOutfits()
        eraseOldestOutfitAfterGivenDays(2)
    }

    private fun handleLocationPermissions() {
        locationHandler.getCurrentLocation(
            ::handleLatitudeAndLongitude,
            ::showLocationIsNull,
            ::makeUserTurnOnLocation
        )
    }

    private fun handleLatitudeAndLongitude(latitude: Double, longitude: Double) {
        getWeatherDataWithTwoRequests(latitude, longitude)
    }

    @SuppressLint("CheckResult")
    private fun getWeatherDataWithTwoRequests(latitude: Double, longitude: Double) {
        val observable = Observable.just(latitude, longitude)
            .flatMap {
                makeCityNameRequest(latitude, longitude)
            }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(
            ::getWeatherData,
            ::showError
        )
    }

    private fun makeCityNameRequest(latitude: Double, longitude: Double) =
        Observable.create { emitter ->
            viewModel.getCityName(latitude, longitude)
            emitter.onNext(viewModel.response.value!!.name)
        }

    private fun getWeatherData(location: String){
        viewModel.getWeatherData(location)
    }

    private fun updateUi(){
        viewModel.response.observe(this,::handleWeatherDataOnUi)
    }

    private fun handleErrorResponse(){
        viewModel.error.observe(this,::showRequestError)
    }

    private fun handleWeatherDataOnUi(response: WeatherResponse) {
        runOnUiThread {
            val weatherCondition = response.weather[0].main
            val temperature = response.main.temp.toInt()
            val location = response.name + ", " + response.sys.country
            showBackgroundBasedOnWeatherCondition(weatherCondition)
            showWeatherData(weatherCondition, temperature, location)
            suggestOutfit(weatherCondition, temperature)
        }
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

    private fun showWeatherData(weatherCondition: String, temperature: Int, location: String) {
        with(binding){
            textWeatherCondition.text = weatherCondition
            textTemperature.text = temperature.toString()
            textLocation.text = location
        }
    }

    private fun suggestOutfit(weatherCondition: String, temperature: Int) {
        val outfitImage: Int = if (weatherCondition == "Rain") {
            viewModel.getOutfitSuitableForRain(usedOutfits)
        } else {
            if (temperature > 25) {
                viewModel.getLightWeightOutfit(usedOutfits)
            } else if (temperature in 10..25) {
                viewModel.getNeutralOutfit(usedOutfits)
            } else {
                viewModel.getHeavyOutfit(usedOutfits)
            }
        }
        showOutfit(outfitImage)
        saveOutfit(outfitImage)
    }

    private fun showOutfit(outfit: Int) {
        binding.outfit.background = ContextCompat.getDrawable(this, outfit)
    }

    private fun saveOutfit(outfit: Int) {
        usedOutfits = (usedOutfits ?: emptySet()).plus(setOf(outfit.toString()))
        SharedPrefsUtil.outfitList = usedOutfits
    }

    private fun showRequestError(error: Throwable) {
        Log.i(
            TAG,
            "Request failed, please try another city or check your internet connection"
        )
    }

    private fun showLocationIsNull() {
        Toast.makeText(
            this,
            "Location is null",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun makeUserTurnOnLocation() {
        Toast.makeText(this, "Turn on location ", Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun addCallbacks() {
        onClickSearchInput()
    }

    private fun onClickSearchInput() {
        handleSearch()
    }

    @SuppressLint("CheckResult")
    private fun handleSearch() {
        val cityObservable = emitCityName()
        cityObservable.subscribe(
            ::getWeatherData,
            ::showError
        )
    }

    private fun emitCityName(): Observable<String> {
        return Observable.create { emitter ->
            binding.textInput.doOnTextChanged { text, start, before, count ->
                emitter.onNext(text.toString())
            }
        }
            .debounce(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }

    private fun showError(e: Throwable) {
        Log.i(TAG, "something went wrong :(")
    }


    private fun getUsedOutfits() {
        usedOutfits = SharedPrefsUtil.outfitList
    }


    private fun eraseOldestOutfitAfterGivenDays(days: Int) {
        if ((usedOutfits?.size ?: 0) > days) {
            val outfitList = SharedPrefsUtil.outfitList
            SharedPrefsUtil.outfitList = outfitList?.drop(1)?.toSet()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_ID) {
            if (checkPermissions(grantResults)) {
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                handleLocationPermissions()
            }
        }
    }

    private fun checkPermissions(grantResults: IntArray) =
        grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

}