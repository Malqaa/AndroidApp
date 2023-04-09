package com.malka.androidappp.newPhase.domain.models.servicemodels.model

data class Category(
    val description: String,
    val id: Int,
    val image: String,
    val isActive: Boolean,
    val isShowInHome: Boolean,
    val name: String,
    val postion: Int,
    val productFeeDuetTime: Int,
    val productPriceType: Int,
    val productPublishPrice: Int,
    var is_select: Boolean=false,
    var isCategory: Boolean=false,
    var template: String="",
    var categoryKey: String="",
    var jsonFilePath: String="",
    var isSelected:Boolean=false,
    var isFollow:Boolean=false,
    var list: List<Category>,

)
