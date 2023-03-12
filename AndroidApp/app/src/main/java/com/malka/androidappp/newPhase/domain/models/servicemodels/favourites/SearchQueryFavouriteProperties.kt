package com.malka.androidappp.newPhase.domain.models.servicemodels.favourites

data class SearchQueryFavouriteProperties(
    val searchQuery: String,
    val isFavorite: Boolean,
    val reminder: String,
    val createdDatetime: String
)