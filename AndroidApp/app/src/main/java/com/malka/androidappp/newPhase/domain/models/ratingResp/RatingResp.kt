package com.malka.androidappp.newPhase.domain.models.ratingResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser

data class RatingResp(
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val ratingData: RatingData?=null
)

data class RatingData(
    val rat:Int,
    var comment : String?=null,
    val productId: Int
)


