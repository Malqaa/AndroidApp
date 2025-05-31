package com.malqaa.androidappp.newPhase.domain.models.categoryResp

import com.google.gson.annotations.SerializedName

import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category

data class CategoriesResp(
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val categoryList:List<Category> ?=null
)