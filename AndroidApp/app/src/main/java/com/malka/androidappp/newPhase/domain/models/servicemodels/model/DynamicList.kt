package com.malka.androidappp.newPhase.domain.models.servicemodels.model

import com.malka.androidappp.newPhase.domain.models.servicemodels.Product

class DynamicList(
    val category_name: String,
    val category_icon: String,
    val product: List<Product>,
    val category_id: Int,
    val typeName: String = "category",
    val detail: String = "",

    )