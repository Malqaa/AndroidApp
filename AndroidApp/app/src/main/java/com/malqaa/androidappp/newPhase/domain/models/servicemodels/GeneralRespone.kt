package com.malqaa.androidappp.newPhase.domain.models.servicemodels

import java.io.Serializable

data class GeneralRespone(
    val code: String,
    val id: String,
    val message: String,
    val status_code: Int,
    val isError: Boolean,
    val data: String
):Serializable
