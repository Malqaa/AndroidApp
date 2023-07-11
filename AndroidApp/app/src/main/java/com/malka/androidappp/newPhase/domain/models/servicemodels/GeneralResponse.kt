package com.malka.androidappp.newPhase.domain.models.servicemodels

import com.google.gson.annotations.SerializedName


data class GeneralResponse(
    val `data`: Any,
    val message: String,
    val status_code: Int,
    val status: String,
) {

}
data class AddProductResponse(
    @SerializedName("data")
    val productId: Int,
    val message: String,
    val status_code: Int
) {

}