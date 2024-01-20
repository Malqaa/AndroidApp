package com.malqaa.androidappp.newPhase.domain.models.productTags

import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category

data class CategoryTagsResp(
    val count: Int,
    @SerializedName("data")
//    val tagsList: List<Tags>,
    val tagsList: List<Category>?=null,
    val status_code: Int
)