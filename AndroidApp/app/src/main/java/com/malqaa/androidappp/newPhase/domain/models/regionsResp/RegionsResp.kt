package com.malqaa.androidappp.newPhase.domain.models.regionsResp

import com.google.gson.annotations.SerializedName

class RegionsResp (
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val regionsList:List<Region> ?=null
)
