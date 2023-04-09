package com.malka.androidappp.newPhase.domain.models.productResp

data class SellerInformation(
    val providerId: String="",
    val rate: Float,
    val businessAccountId: String="",
    val image: String? = null,
    val name: String? = null,
    val city: String? = null,
    val phone: String? = null,
    val instagram: String? = null,
    val youTube: String? = null,
    val skype: String? = null,
//    "webSite": null,
    val faceBook: String? = null,
    val twitter: String? = null,
    val linkedIn: String? = null,
    val snapchat: String? = null,
    val tikTok: String? = null,

    val createdAt: String? = null,
)