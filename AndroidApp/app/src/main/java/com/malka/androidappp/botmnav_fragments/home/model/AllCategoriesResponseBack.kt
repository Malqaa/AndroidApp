package com.malka.androidappp.botmnav_fragments.home.model

data class AllCategoriesResponseBack(
    val status_code: String,
    val count: Int,
    val message: String,
    val data: List<AllCategoriesModel>
)
