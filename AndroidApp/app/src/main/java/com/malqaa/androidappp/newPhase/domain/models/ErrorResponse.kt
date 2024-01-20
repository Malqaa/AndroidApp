package com.malqaa.androidappp.newPhase.domain.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    var status: String? = null,
    var message: String? = null,
    var error:String?=null,
    var result:String?=null,
    @SerializedName("Message")
    var message2: String? = null,
)
