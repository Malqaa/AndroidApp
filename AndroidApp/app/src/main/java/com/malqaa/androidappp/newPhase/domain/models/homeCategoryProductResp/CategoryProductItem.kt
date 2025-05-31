package com.malqaa.androidappp.newPhase.domain.models.homeCategoryProductResp

import com.malqaa.androidappp.newPhase.domain.models.productResp.Product

class CategoryProductItem(
    val name: String,
    val image: String,
    var listProducts: List<Product>?=null,
    val catId: Int,
    )