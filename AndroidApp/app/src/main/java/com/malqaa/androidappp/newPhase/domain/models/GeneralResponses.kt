package com.malqaa.androidappp.newPhase.domain.models

import com.google.gson.annotations.SerializedName

data class GeneralResponses<T>(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val data: T

)