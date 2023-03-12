package com.malka.androidappp.newPhase.domain.models.servicemodels.model

data class AllCategoriesResponseBack(
    val status_code: String,
    val count: Int,
    val message: String,
    val data: List<Category>
)
