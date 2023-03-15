package com.malka.androidappp.newPhase.domain.models.countryResp

import com.google.gson.annotations.SerializedName

data class CountriesResp(
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val countriesList:List<Country> ?=null
)
