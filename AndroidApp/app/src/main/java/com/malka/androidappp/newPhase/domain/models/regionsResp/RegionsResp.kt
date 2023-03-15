package com.malka.androidappp.newPhase.domain.models.regionsResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.countryResp.Country

class RegionsResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val regionsList:List<Region> ?=null
)
