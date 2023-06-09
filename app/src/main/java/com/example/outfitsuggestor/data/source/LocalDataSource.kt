package com.example.outfitsuggestor.data.source

import com.example.outfitsuggestor.R
import com.example.outfitsuggestor.data.model.Outfit
import com.example.outfitsuggestor.enums.OutfitType

object LocalDataSource {
     val outfits = mutableListOf(
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit1_heavy_rain,
            true)
        ,
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit2_heavy_rain,
            true
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit3_heavy_rain,
            true
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit4_heavy_no_rain,
            false
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit5_heavy_no_rain,
            false
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit6_heavy_no_rain,
            false
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit7_heavy_no_rain,
            false
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit8_heavy_no_rain,
            false
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit9_heavy_rain,
            true
        ),
        Outfit(
            OutfitType.HEAVY_CLOTHES,
            R.drawable.outfit10_heavy_no_rain,
            false
        ),
        Outfit(
            OutfitType.NEUTRAL_CLOTHES,
            R.drawable.outfit1_netural_no_rain,
            false
        ),
        Outfit(
            OutfitType.NEUTRAL_CLOTHES,
            R.drawable.outfit4_neutra_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit1_light_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit2_light_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit3_light_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit4_light_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit5_light_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit6_light_no_rain,
            false
        ),
        Outfit(
            OutfitType.LIGHTWEIGHT_CLOTHES,
            R.drawable.outfit7_light_no_rain,
            false
        )
    )
}