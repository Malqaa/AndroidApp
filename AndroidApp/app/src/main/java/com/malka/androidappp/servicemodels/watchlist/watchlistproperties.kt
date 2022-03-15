package com.malka.androidappp.servicemodels.watchlist

import com.malka.androidappp.servicemodels.advertisment.advertismentproperties
import com.malka.androidappp.servicemodels.home.GeneralProduct

data class watchlistproperties(
    val id: String,
    val userid: String,
    val advertisement: GeneralProduct,
    val createddate: String,
    val isActive: Boolean
)