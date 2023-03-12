package com.malka.androidappp.newPhase.domain.models.servicemodels.categorylistings

data class SearchRequestModel(
    val category: String = "",
    val country: String = "",
    val city: String = "",
    val region: String = "",
    val pricesort: String = "",
    val listingtype: String = "",
    val listingTitle: String = "",
    val brandNewItem: String = "",
    val query: String = ""
)