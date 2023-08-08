package com.malka.androidappp.newPhase.domain.models.bidPersonsResp

data class BidPersonData(
    val bidId: Int,
    val bidPrice: Int,
    val createdAt: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val userImage:String?=null,
    var isSelected: Boolean
)