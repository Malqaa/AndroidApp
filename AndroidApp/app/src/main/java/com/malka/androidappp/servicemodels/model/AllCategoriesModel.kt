package com.malka.androidappp.servicemodels.model

data class AllCategoriesModel(
    val id: String?,
    val categoryid: Int?,
    val nameEn: String?,
    val nameAr: String?,
    val descriptionAr: String?,
    val descriptionEn: String?,
    val postion: String?,
    val categoryKey: String?,
    val isShowInHome: Boolean = true,
    val isActive: Boolean = true,
    val image: String?,
    val categoryParentId: Int?,
    val isCategory: Boolean,
    val createdBy: String?,
    val createdOn: Any?,
    val template: String?,
    val jsonFilePath: String?,
    var is_select: Boolean=false,
)
