package com.malka.androidappp.servicemodels

import com.malka.androidappp.network.constants.Constants

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