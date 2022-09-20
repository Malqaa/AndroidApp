package com.malka.androidappp.servicemodels.model

data class HomeProducts(
    val `data`: List<HomeCategory>,
    val message: String,
    val status_code: Int
) {
    data class HomeCategory(
        val catId: Int,
        val image: String,
        val listProducts: List<Products>,
        val name: String
    ) {

    }
}