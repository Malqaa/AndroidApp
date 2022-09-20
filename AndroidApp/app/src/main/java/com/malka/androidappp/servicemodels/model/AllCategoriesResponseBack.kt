package com.malka.androidappp.servicemodels.model

data class AllCategoriesResponseBack(
    val status_code: String,
    val count: Int,
    val message: String,
    val data: List<Category>
)
