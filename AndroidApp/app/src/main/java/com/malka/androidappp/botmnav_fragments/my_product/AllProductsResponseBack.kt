package com.malka.androidappp.botmnav_fragments.my_product

data class AllProductsResponseBack(
    val data: List<ModelMyProduct>,
    val message: String,
    val status_code: Int
)
