package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Midea(
    val createdAt: String?=null,
    val id: Int,
    val isMainMadia: Boolean,
    val product: String ?=null,
    val productId: Int,
    val type: Int,
    val url: String?=null,
):Parcelable