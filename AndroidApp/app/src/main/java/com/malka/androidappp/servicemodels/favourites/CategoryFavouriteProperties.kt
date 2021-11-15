package com.malka.androidappp.servicemodels.favourites

import org.joda.time.DateTime

data class CategoryFavouriteProperties(
    val categoryName: String,
    val isFavorite: Boolean,
    val reminder: String,
    val createdDatetime: String
)