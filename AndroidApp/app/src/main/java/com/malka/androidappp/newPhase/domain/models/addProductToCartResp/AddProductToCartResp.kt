package com.malka.androidappp.newPhase.domain.models.addProductToCartResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateItem

data class AddProductToCartResp(

    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val addProductToCartData: AddProductToCartData? = null
)

data class AddProductToCartData(
    val cartMasterId: String,
    val quantity: Int,
    val productId: Int
)