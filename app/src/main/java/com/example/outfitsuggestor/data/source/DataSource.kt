package com.example.outfitsuggestor.data.source

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.outfitsuggestor.BuildConfig
import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.data.model.WeatherResponse
import com.example.outfitsuggestor.enums.OutfitType
import com.example.outfitsuggestor.utils.Constants
import com.example.outfitsuggestor.utils.executeWithCallbacks
import com.google.android.gms.location.LocationServices
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class DataSource(private val context: Context, private val activity: Activity) : IDataSource {
    private val fusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(activity)
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
        latitude: Double?,
        longitude: Double?,
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

    override fun getCurrentLocation(
        onLocationUpdated: (latitude: Double, longitude: Double) -> Unit,
        showLocationIsNull: () -> Unit,
        makeUserTurnOnLocation: () -> Unit
    ) {
        if (checkPermission()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }

                fusedLocationProviderClient.lastLocation.addOnCompleteListener(activity) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        showLocationIsNull()
                        Log.i("LOCATION_CHECK_CASES", "Location is null")
                    } else {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        onLocationUpdated(latitude, longitude)
                        Log.i("LOCATION_CHECK_CASES",
                            "latitude $latitude, longitude $longitude")
                    }
                }
            }
            else {
                makeUserTurnOnLocation()
                Log.i("LOCATION_CHECK_CASES", "Hey! turn on location")
            }
        }
        else {
            requestPermissions()
            Log.i("LOCATION_CHECK_CASES", "I will make a request now!")
        }
    }

    private fun checkPermission(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager
            .isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Constants.PERMISSION_ID
        )
    }



}