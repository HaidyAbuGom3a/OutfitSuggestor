package com.example.outfitsuggestor.data.source

import com.example.outfitsuggestor.BuildConfig
import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.data.source.LocalDataSource.outfits
import com.example.outfitsuggestor.enums.OutfitType
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.executeWithCallbacks
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class DataSource() : IDataSource {
    override fun getWeatherData(
        latitude: Double?,
        longitude: Double?,
        location:String?,
        units: String,
        onSuccessCallback: (WeatherResponse) -> Unit,
        onFailureCallback: (error: Throwable) -> Unit
    ) {
        val url = HttpUrl.Builder().scheme(Constants.SCHEME).host(Constants.BASE_URL)
            .addPathSegment("data")
            .addPathSegment("2.5")
            .addPathSegment("weather")
            .apply {
                addQueryParameter("lat", latitude.toString())
                addQueryParameter("lon", longitude.toString())
                addQueryParameter("q",location)
                addQueryParameter("appid", BuildConfig.API_KEY)
                addQueryParameter("units", units)
            }.build()

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.executeWithCallbacks(
            request, onSuccessCallback, onFailureCallback
        )
    }

    override fun getAllOutfits(): List<Outfit> = outfits

    override fun getOutfitsSuitableForRain(): List<Outfit> =
        outfits.filter { it.suitableForRain }

    override fun getLightWeightOutfits(): List<Outfit> =
        outfits.filter { it.type == OutfitType.LIGHTWEIGHT_CLOTHES }

    override fun getHeavyOutfits(): List<Outfit> =
        outfits.filter { it.type == OutfitType.HEAVY_CLOTHES }

    override fun getNeutralOutfits(): List<Outfit> =
        outfits.filter { it.type == OutfitType.NEUTRAL_CLOTHES }


}