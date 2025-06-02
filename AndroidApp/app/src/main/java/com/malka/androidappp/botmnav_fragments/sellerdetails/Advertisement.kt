package com.malka.androidappp.botmnav_fragments.sellerdetails

data class Advertisement(
    val description: String?,
    val homepageImage: String?,
    val price: String?,
    val reservePrice: String?,
    val title: String?,
    val template: String? = null,
    val referenceId: String? = null
)