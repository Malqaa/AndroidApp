package com.malka.androidappp.newPhase.domain.models.servicemodels

data class CountryRespone(
    val `data`: List<Country>,
    val message: String,
    val status_code: Int
) {

}