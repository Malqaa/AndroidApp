package com.malqaa.androidappp.newPhase.domain.models.pakatResp

import com.google.gson.annotations.SerializedName

data class PakatResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val pakatList: List<PakatDetails>? = null
)

data class PakatDetails(
    val id: Int,
    val name: String? = null,
    val description: String? = null,
    val numMonth:Int,
    val countImage:Int,
    val countVideo:Int,
    val image: String? = null,
    val productPosition: String? = null,
    val popular:Boolean,
    val showSupTitle:Boolean,
    val showHighLight:Boolean,
    val pakatType: String? = null,
    val pakatFor: String? = null,
    val productSize:Int,
    val smSsCount:Int,
    val isActive:Boolean,
    val price:Float,
    val isFree:Boolean,
    val isBusinessAccountSubscriped:Boolean,
    val totalRecords:Int,
    val listCategories:List<CategoryItemBakat>,
    var isSelected:Boolean=false
)

data class CategoryItemBakat(
    val id: Int,
    val name: String? = null,
    val image: String? = null,
)

data class ItemPacket(
    val Title: String? = null,
    val value: Any? = null,
)