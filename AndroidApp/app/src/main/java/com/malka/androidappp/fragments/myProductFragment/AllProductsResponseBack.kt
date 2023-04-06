package com.malka.androidappp.fragments.myProductFragment

data class AllProductsResponseBack(
    val data: List<ModelMyProduct>,
    val message: String,
    val status_code: Int
)
