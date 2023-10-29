package com.malka.androidappp.newPhase.domain.models.addOrderResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.addProductToCartResp.AddProductToCartData

data class AddOrderResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val addOrderObject: AddOrderObject? = null
)

data class AddOrderObject(
    @SerializedName("item1")
    val cartAdded: Boolean,
    @SerializedName("item2")
    val orderNumber: Int,
    @SerializedName("item3")
    val item3: Int,
//"item3": 690
)