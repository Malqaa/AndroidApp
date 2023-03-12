package com.malka.androidappp.newPhase.domain.models.servicemodels.watchlist

data class watchlistadd(
    val userid: String,
    val advertisementId: String,
    val remindertype: Int = 0
)