package com.malka.androidappp.newPhase.domain.models.accountProfile

data class BaseInfo(
    val membershipNumber: Int,
    val userName: String,
    val image: String,
    val createdAt: String,
    val walletBalance: Int,
    val pointsBalance: Int,
    val followCatergoriesCount: Int,
    val rate: Int
)
