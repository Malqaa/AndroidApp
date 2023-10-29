package com.malka.androidappp.newPhase.domain.models.categoryFollowResp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Branch(
    val country: String?="",
    val countryName: String?="",
    val id: Int?=0,
    val isActive: Boolean?=false,
    val lat: String?="",
    val lng: String?="",
    val location: String?="",
    val name: String?="",
    val neighborhood: String?="",
    val neighborhoodName: String?="",
    val region: String?="",
    val regionCode: String?="",
    val regionName: String?="",
    val streetName: String?="",
    val userName: String?=""
): Parcelable
