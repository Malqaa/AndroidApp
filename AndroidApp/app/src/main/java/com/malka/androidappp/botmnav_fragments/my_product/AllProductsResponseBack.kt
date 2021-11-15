package com.malka.androidappp.botmnav_fragments.my_product

import com.malka.androidappp.botmnav_fragments.create_product.ProductDetailModel

data class AllProductsResponseBack(
    val data: List<ModelMyProduct>,
    val message: String,
    val status_code: Int
)
