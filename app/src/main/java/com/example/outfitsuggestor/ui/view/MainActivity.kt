package com.example.outfitsuggestor.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.databinding.ActivityMainBinding
import com.example.outfitsuggestor.ui.presenter.MainPresenter

class MainActivity : AppCompatActivity(), MainViewInterface {
    private lateinit var binding: ActivityMainBinding
    private val presenter by lazy {
        MainPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWeatherData()
    }

    private fun getWeatherData() {
        presenter.getWeatherData()
    }

    override fun onWeatherSuccessResponse(response: WeatherResponse) {
        Log.i("ACTIVITY_MAIN",response.toString())
        runOnUiThread {
            binding.textTemperature.text = response.main.temp.toInt().toString()
            binding.textLocation.text = response.name + ", " + response.sys.country
            binding.textWeatherCondition.text = response.weather[0].main
        }
    }

    override fun onWeatherFailureResponse(error: Throwable) {
        Log.i("ACTIVITY_MAIN","failed")
    }


}