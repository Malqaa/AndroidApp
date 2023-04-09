package com.malka.androidappp.newPhase.domain.models.categoryFollowResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateItem

data class CategoryFollowResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val CategoryFollowList: List<CategoryFollowItem>? = null
)

data class CategoryFollowItem(
    var id: Int,
    var name: String? = null,
    var description: String? = null,
    var postion: Int,
    var isShowInHome: Boolean,
    var image: String? = null,
    var parentId: Int,
    var isActive: Boolean,
    var productPriceType: Int,
    var productPublishPrice: Int,
    var productFeeDuetTime: Int,

    )
