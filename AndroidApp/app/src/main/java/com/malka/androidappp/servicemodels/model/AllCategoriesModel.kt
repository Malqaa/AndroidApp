package com.malka.androidappp.servicemodels.model

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
    val template: String?,
    val imagePath: String?,
    val jsonFilePath: String?,
    var is_select: Boolean=false,
)
