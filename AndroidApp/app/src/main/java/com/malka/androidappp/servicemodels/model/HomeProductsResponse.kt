package com.malka.androidappp.servicemodels.model

import com.malka.androidappp.servicemodels.Product

data class HomeProductsResponse(
    val `data`: List<HomeCategory>,
    val message: String,
    val status_code: Int
) {
    data class HomeCategory(
        val catId: Int,
        val image: String,
        val listProducts: List<Product>,
        val name: String
    ) {

    }
}