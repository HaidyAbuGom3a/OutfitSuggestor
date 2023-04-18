package com.example.outfitsuggestor.data.model

import com.example.outfitsuggestor.enums.OutfitType

data class Outfit(
    val type:OutfitType,
    val image:Int,
    val suitableForRain : Boolean
)
