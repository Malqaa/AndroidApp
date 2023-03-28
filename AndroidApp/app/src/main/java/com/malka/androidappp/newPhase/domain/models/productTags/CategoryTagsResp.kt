package com.malka.androidappp.newPhase.domain.models.productTags

import com.google.gson.annotations.SerializedName

data class CategoryTagsResp(
    val count: Int,
    @SerializedName("data")
    val tagsList: List<Tags>,
    val status_code: Int
)