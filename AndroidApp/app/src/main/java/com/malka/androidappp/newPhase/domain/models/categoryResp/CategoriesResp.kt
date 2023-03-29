package com.malka.androidappp.newPhase.domain.models.categoryResp

import com.google.gson.annotations.SerializedName

import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category

data class CategoriesResp(
    val code: String?=null,
    val message: String?=null,
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val categoryList:List<Category> ?=null
)