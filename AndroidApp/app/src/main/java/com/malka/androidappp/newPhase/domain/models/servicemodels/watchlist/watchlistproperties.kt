package com.malka.androidappp.newPhase.domain.models.servicemodels.watchlist

import com.malka.androidappp.newPhase.domain.models.servicemodels.AdDetailModel

data class watchlistproperties(
    val id: String,
    val userid: String,
    val advertisement: AdDetailModel?,
    val createddate: String,
    val isActive: Boolean
)