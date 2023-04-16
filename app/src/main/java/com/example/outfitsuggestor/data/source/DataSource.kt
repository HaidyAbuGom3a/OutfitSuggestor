package com.example.outfitsuggestor.data.source

import com.example.outfitsuggestor.BuildConfig
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.executeWithCallbacks
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class DataSource() : IDataSource {
    override fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        onSuccessCallback: (WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    ) {
        val url = HttpUrl.Builder().scheme(Constants.SCHEME)
            .host(Constants.BASE_URL)
            .addPathSegment("data")
            .addPathSegment("2.5")
            .addPathSegment("weather")
            .apply {
                addQueryParameter("lat", latitude.toString())
                addQueryParameter("lon", longitude.toString())
                addQueryParameter("appid", BuildConfig.API_KEY)
                addQueryParameter("units", units)
            }.build()

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.executeWithCallbacks(
            request,
            onSuccessCallback,
            onFailureCallback
        )
    }

}