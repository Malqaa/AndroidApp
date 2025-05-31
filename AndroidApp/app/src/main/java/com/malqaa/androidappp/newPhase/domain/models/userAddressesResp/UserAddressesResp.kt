package com.malqaa.androidappp.newPhase.domain.models.userAddressesResp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
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
    @SerializedName("country") var country: Country,
    @SerializedName("region") var region: Region,
    @SerializedName("neighborhood") var neighborhood: Region,
    var isSelected: Boolean = false
) : Parcelable

@Parcelize
data class Country(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class Region(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class Neighborhood(
    val id: Int,
    val name: String
) : Parcelable

// Mapping from Country to SearchListItem
fun Country.toSearchListItem(): SearchListItem {
    return SearchListItem(id, name)
}

// Mapping from Region to SearchListItem
fun Region.toSearchListItem(): SearchListItem {
    return SearchListItem(id, name)
}

// Mapping from Neighborhood to SearchListItem
fun Neighborhood.toSearchListItem(): SearchListItem {
    return SearchListItem(id, name)
}
