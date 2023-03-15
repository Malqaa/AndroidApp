package com.malka.androidappp.newPhase.domain.models.servicemodels

import com.malka.androidappp.newPhase.domain.models.countryResp.Country

data class CountryRespone(
    val `data`: List<Country>,
    val message: String,
    val status_code: Int
) {

}