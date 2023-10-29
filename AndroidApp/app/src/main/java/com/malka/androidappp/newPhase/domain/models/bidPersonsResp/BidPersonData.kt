package com.malka.androidappp.newPhase.domain.models.bidPersonsResp

data class BidPersonData(
    val bidId: Int=77,
    val bidPrice: Int=0,
    val createdAt: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val userImage:String?=null,
    var isSelected: Boolean=false
)