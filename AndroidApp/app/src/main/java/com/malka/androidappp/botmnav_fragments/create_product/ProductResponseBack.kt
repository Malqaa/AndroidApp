package com.malka.androidappp.botmnav_fragments.create_product

import com.malka.androidappp.activities_main.login.Data

data class ProductResponseBack(
    val data: ProductDetailModel,
    val message: String,
    val status_code: Int
)
