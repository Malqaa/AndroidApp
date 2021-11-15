package com.malka.androidappp.botmnav_fragments.shopping_cart2

data class ModelShoppingcart2(
    val productImgCaart: String,
    val cartproductDescripp: String? = "",
    val listingPartyy: String? = "",
    val pricebyQuantityy: String? = "",
    val totalPrice: String? = "",
    val advid: String = "",
    var ItemInWatchlist: Boolean = false,
    var CartId: String = "",
)