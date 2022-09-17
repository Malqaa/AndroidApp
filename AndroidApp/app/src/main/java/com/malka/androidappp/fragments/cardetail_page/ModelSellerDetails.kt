package com.malka.androidappp.fragments.cardetail_page

import com.malka.androidappp.servicemodels.User

data class ModelSellerDetails(
    val `data`: User?,
    val message: String,
    val status_code: Int
)