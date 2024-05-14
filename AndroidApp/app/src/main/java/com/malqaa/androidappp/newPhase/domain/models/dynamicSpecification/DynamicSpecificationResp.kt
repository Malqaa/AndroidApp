package com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification

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
    var isSelected: Boolean=false,
    var subSpecifications: ArrayList<SubSpecificationItem>? = null,
    var valueArText: String = "",
    var valueEnText: String = "",
    var valueBoolean: Boolean = false,
    var subSpecificationsValue: SubSpecificationItem? = null,
    var filterValue: String = ""


) {
    fun setData(obj: DynamicSpecificationSentObject) {
        name = obj.HeaderSpeAr
        nameAr = obj.HeaderSpeAr
        nameEn = obj.HeaderSpeEn
        valueArText = obj.ValueSpeAr
        valueEnText = obj.ValueSpeEn
        type = obj.Type
        isSelected = true

    }
}

data class DynamicSpecificationSentObject(
    @SerializedName("headerSpeAr")
    var HeaderSpeAr: String = "",
    @SerializedName("headerSpeEn")
    var HeaderSpeEn: String = "",
    @SerializedName("valueSpeAr")
    var ValueSpeAr: String = "",
    @SerializedName("valueSpe")
    var valueSpe: String = "",
    @SerializedName("valueSpeEn")
    var ValueSpeEn: String = "",
    @SerializedName("type")
    var Type: Int = 1,
    @SerializedName("specificationId")
    var SpecificationId: Int = 0,

    val id: Int = 0,

)


data class SubSpecificationItem(
    var id: Int,
    var nameAr: String = "",
    var nameEn: String = "",
    var createdAt: String = "",
    var isActive: Boolean = false,
    var isSelected: Boolean = false,
    var isDataSelected: Boolean = false,
)