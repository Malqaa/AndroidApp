package com.malka.androidappp.servicemodels.watchlist

import com.malka.androidappp.servicemodels.advertisment.advertismentproperties

data class watchlistproperties(
    val id: String,
    val userid: String,
    val advertisement: advertismentproperties,
    val createddate: String,
    val isActive: Boolean
)