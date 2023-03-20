package com.malka.androidappp.newPhase.domain.models.homeSilderResp

import com.google.gson.annotations.SerializedName


data class HomeSliderResp(
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val sliderList: List<HomeSliderItem>?=null
)
data class HomeSliderItem(
    val id: Int,
    var image: String,
)