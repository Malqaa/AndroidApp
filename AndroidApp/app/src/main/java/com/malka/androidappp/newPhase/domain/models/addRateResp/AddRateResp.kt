package com.malka.androidappp.newPhase.domain.models.addRateResp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class AddRateResp(

val code: String?=null,
val message: String?=null,
val status_code: Int,
val status:String?=null,
@SerializedName("data")
val rateObject:AddRateItem?=null
)
@Parcelize
data class AddRateItem(
    var rate: Float,
    var comment: String,
    //var productId: String,
    var id:Int,
) : Parcelable