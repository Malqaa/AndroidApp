package com.malka.androidappp.newPhase.domain.models.productTags

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category

data class CategoryTagsResp(
    val count: Int,
    @SerializedName("data")
//    val tagsList: List<Tags>,
    val tagsList: List<Category>?=null,
    val status_code: Int
)