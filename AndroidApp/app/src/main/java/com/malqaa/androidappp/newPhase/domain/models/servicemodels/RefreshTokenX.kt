package com.malqaa.androidappp.newPhase.domain.models.servicemodels

data class RefreshTokenX(
    val created: String,
    val createdByIp: String,
    val expires: String,
    val id: String,
    val jwtToken: String,
    val refhToken: String,
    val replacedByToken: String,
    val revoked: String,
    val revokedByIp: String
)