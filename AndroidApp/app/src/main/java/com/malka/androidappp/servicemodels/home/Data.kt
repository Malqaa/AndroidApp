package com.malka.androidappp.servicemodels.home

data class Data(
    val caradvetisement: List<Caradvetisement>,
    val generaladvetisement: List<Generaladvetisement>,
    val propertyadvetisement: List<Any>,
    val recentadvetisement: List<Recentadvetisement>
)