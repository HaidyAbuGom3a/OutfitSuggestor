package com.example.outfitsuggestor.data.source

import com.example.outfitsuggestor.BuildConfig
import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.enums.OutfitType
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.executeWithCallbacks
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class DataSource() : IDataSource {
    private val outfits = mutableListOf(
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit1_heavy_rain, true),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit2_heavy_rain, true),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit3_heavy_rain, true),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit4_heavy_no_rain, false),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit5_heavy_no_rain, false),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit6_heavy_no_rain, false),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit7_heavy_no_rain, false),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit8_heavy_no_rain, false),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit9_heavy_rain, true),
        Outfit(OutfitType.HEAVY_CLOTHES, R.drawable.outfit10_heavy_no_rain, false),
        Outfit(OutfitType.NEUTRAL_CLOTHES, R.drawable.outfit1_netural_no_rain, false),
        Outfit(OutfitType.NEUTRAL_CLOTHES, R.drawable.outfit2_neutral_no_rain, false),
        Outfit(OutfitType.NEUTRAL_CLOTHES, R.drawable.outfit4_neutra_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit1_light_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit2_light_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit3_light_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit4_light_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit5_light_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit6_light_no_rain, false),
        Outfit(OutfitType.LIGHTWEIGHT_CLOTHES, R.drawable.outfit7_light_no_rain, false)
    )

    override fun getWeatherData(
        latitude: Double,
        longitude: Double,
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
                addQueryParameter("appid", BuildConfig.API_KEY)
                addQueryParameter("units", units)
            }.build()

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.executeWithCallbacks(
            request, onSuccessCallback, onFailureCallback
        )
    }

    override fun addOutfit(outfit: Outfit) {
        outfits.add(outfit)
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