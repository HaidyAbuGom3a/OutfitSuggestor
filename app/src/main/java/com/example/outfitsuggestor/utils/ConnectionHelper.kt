package com.example.outfitsuggestor.utils

import com.example.outfitsuggestor.data.model.WeatherResponse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

fun OkHttpClient.executeWithCallbacks(
    request: Request,
    onSuccessCallback: (response: WeatherResponse) -> Unit,
    onFailureCallback: (error: Throwable) -> Unit
): Call {
    val call = newCall(request)
    val callback = object : Callback {

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val gson = Gson()
                val result = gson.fromJson(responseBody, WeatherResponse::class.java)
                onSuccessCallback(result)
            } else {
                onFailureCallback(Throwable("$response"))
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            onFailureCallback(e)
        }

    }
    call.enqueue(callback)
    return call
}