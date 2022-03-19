package com.malka.androidappp.servicemodels.home

import com.malka.androidappp.servicemodels.AdDetailModel

data class Data(
    val closingsoon: List<AdDetailModel>,
    val generaladvetisement: List<AdDetailModel>,
    val propertyadvetisement: List<AdDetailModel>,
    val recentadvetisement: List<AdDetailModel>,
    val caradvetisement: List<AdDetailModel>
)