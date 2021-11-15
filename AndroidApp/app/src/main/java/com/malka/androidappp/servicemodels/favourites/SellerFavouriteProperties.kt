package com.malka.androidappp.servicemodels.favourites

import org.joda.time.DateTime

data class SellerFavouriteProperties(
    val id: String,
    val username: String,
    val totallistings: String,
    val userid: String,
    val createddate: String,
    val isActive: Boolean
)