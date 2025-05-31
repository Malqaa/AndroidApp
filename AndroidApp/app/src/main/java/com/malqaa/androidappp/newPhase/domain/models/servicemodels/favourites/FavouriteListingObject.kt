package com.malqaa.androidappp.newPhase.domain.models.servicemodels.favourites


class FavouriteListingObject(
    val id: String,
    val isFavourite: Boolean,
    val loggedInUserId: String,
    val reminder: String,
    val createdDatetime: String,
    val seller: MutableList<SellerFavouriteProperties>,
    var searchQue: MutableList<SearchQueryFavouriteProperties>,
    var category: MutableList<CategoryFavouriteProperties>
)
