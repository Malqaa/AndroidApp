package com.malqaa.androidappp.newPhase.domain.models.userAddressesResp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class UserAddressesResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    var addressesList: List<AddressItem>? = null
)

@Parcelize
data class AddressItem(
    var id: Int,
    var name: String? = null,
    var phone: String? = null,
    var title: String? = null,
    var location: String? = null,
    var street: String? = null,
    var appartment: String? = null,
    var floor: String? = null,
    var building: String? = null,
    var defaultAddress: Boolean,
    var lat: String? = null,
    var lng: String? = null,
var isSelected:Boolean=false
):Parcelable

