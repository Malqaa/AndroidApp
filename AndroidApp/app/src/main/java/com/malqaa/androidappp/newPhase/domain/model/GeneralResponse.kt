package com.casecode.domain.model

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    val `data`: Any? = null,
    val message: String = "",
    @SerializedName("status_code")
    val statusCode: Int = 0,
    val status: String = "",
    var addressTitle: String = ""
)