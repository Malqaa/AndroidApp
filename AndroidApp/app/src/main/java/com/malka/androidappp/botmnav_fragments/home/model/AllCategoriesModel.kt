package com.malka.androidappp.botmnav_fragments.home.model

data class AllCategoriesModel(
    val id: String?,
    val categoryid: Int?,
    val categoryName: String?,
    val categoryKey: String?,
    val categoryParentId: Int?,
    val isCategory: Boolean,
    val isActive: Boolean,
    val createdBy: String?,
    val createdOn: Any?,
    val template: String?
)
