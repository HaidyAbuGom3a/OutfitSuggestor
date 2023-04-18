package com.example.outfitsuggestor.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefsUtil {
    private var sharedPrefs: SharedPreferences? = null
    private const val SHARED_PREFS_NAME = "OUTFIT_SHARED_PREFERENCES"
    private const val KEY_OUTFITS_NAME = "OUTFITS_LIST"

    fun initPrefsUtil(context: Context){
        sharedPrefs = context.getSharedPreferences(
            SHARED_PREFS_NAME, Context.MODE_PRIVATE
        )
    }
    var outfitList: Set<String>?
        get() = sharedPrefs?.getStringSet(KEY_OUTFITS_NAME,null)
        set(value){
            sharedPrefs?.apply{
                edit().putStringSet(KEY_OUTFITS_NAME,value).apply()
            }
        }
}