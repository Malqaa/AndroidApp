package com.malka.androidappp.servicemodels.home

data class Data(
    val closingsoon: List<GeneralProduct>,
    val generaladvetisement: List<GeneralProduct>,
    val propertyadvetisement: List<GeneralProduct>,
    val recentadvetisement: List<GeneralProduct>,
    val caradvetisement: List<GeneralProduct>
)