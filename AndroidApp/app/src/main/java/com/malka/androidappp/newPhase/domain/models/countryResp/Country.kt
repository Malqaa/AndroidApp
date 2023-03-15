package com.malka.androidappp.newPhase.domain.models.countryResp

import com.malka.androidappp.newPhase.data.network.constants.Constants

data class Country(
    val id: Int,
    val name: String,
    val countryFlag: String?=null,
    val countryCode: String?=null,
){

    val flagimglink: String
        get() {
            return Constants.IMAGE_URL2 + countryFlag
        }

}