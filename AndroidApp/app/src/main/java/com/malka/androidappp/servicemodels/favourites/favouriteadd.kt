package com.malka.androidappp.servicemodels.favourites

data class favouriteadd(
    val sellerId: String,
    val categoryName: String,
    val loggedInUserId: String,
    val remindertype: Int = 0,
    val searchQuery: String,
    val query: String,
    val userid: String,
    val category: String,
    val sellerid: String

)