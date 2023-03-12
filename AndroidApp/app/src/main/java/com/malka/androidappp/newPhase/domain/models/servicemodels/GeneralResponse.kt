package com.malka.androidappp.newPhase.domain.models.servicemodels


data class GeneralResponse(
    val `data`: Any,
    val message: String,
    val status_code: Int
) {

}