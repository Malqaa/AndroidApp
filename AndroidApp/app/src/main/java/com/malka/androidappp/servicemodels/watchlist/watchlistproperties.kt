package com.malka.androidappp.servicemodels.watchlist

import com.malka.androidappp.servicemodels.AdDetailModel

data class watchlistproperties(
    val id: String,
    val userid: String,
    val advertisement: AdDetailModel,
    val createddate: String,
    val isActive: Boolean
)