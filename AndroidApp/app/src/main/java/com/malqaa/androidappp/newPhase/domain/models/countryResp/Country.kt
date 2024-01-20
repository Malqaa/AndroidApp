package com.malqaa.androidappp.newPhase.domain.models.countryResp

import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.Region

data class Country(
    val id: Int,
    val name: String,
    val countryFlag: String? = null,
    val countryCode: String? = null,
    var regionsList: List<Region>?=null,
){

    val flagimglink: String
        get() {
            return Constants.IMAGE_URL2 + countryFlag
        }

}