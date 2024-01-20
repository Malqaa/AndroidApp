package com.malqaa.androidappp.newPhase.domain.models.servicemodels.favourites

data class CategoryFavouriteProperties(
    val categoryName: String,
    val isFavorite: Boolean,
    val reminder: String,
    val createdDatetime: String
)