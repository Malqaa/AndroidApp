package com.malka.androidappp.newPhase.domain.models

data class ErrorResponse (
    var status: String? = null,
    var message: String? = null,
    var error:String?=null,
    var result:String?=null,
)
