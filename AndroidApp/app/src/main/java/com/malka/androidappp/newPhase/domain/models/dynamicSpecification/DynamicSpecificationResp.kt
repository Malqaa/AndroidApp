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
    var nameAr: String? = null,
    var nameEn: String? = null,
    var isRequired: Boolean,
    var description: String? = null,
    var placeHolder: String? = null,
    var type: Int,
    var createdAt: String,
    var isActive: Boolean,
    var subSpecifications: List<SubSpecificationItem>? = null,
    var valueArText: String = "",
    var valueEnText: String = "",
    var subSpecificationsValue: SubSpecificationItem? = null,
    var filterValue: String=""
)

data class DynamicSpecificationSentObject(
    var HeaderSpeAr: String = "",
    var HeaderSpeEn: String = "",
    var ValueSpeAr: String = "",
    var ValueSpeEn: String = "",
    var Type: Int = 1
)



data class SubSpecificationItem(
    var id: Int,
    var nameAr: String = "",
    var nameEn: String = "",
    var createdAt: String = "",
    var isActive: Boolean = false,
 var isSelected:Boolean=false
)