package com.malka.androidappp.servicemodels

data class watchlistResponse(
    val `data`: ArrayList<Product>,
    val message: String,
    val status: String,
    val status_code: Int
)