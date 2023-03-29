package com.malka.androidappp.newPhase.domain.models.dynamicSpecification

import com.google.gson.annotations.SerializedName

data class DynamicSpecificationResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val dynamicList: List<DynamicSpecificationItem>? = null
)

//type 1 spinner , 2 edit text
data class DynamicSpecificationItem(
    var id: Int,
    var name: String? = null,
    var isRequired: Boolean,
    var description: String? = null,
    var placeHolder: String? = null,
    var type: Int,
    var createdAt: String,
    var isActive: Boolean,
    var subSpecifications: List<SubSpecificationItem>? = null,
    var valueArText: String = "",
    var valueEnText: String = "",
    var subSpecificationsValue:SubSpecificationItem?=null
) {

}

data class SubSpecificationItem(
    var id: Int,
    var nameAr: String = "",
    var nameEn: String = "",
    var createdAt: String="",
    var isActive: Boolean=false
)