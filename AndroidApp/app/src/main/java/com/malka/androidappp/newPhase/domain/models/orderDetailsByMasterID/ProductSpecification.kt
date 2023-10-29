package com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductSpecification(
    val headerSpeAr: String? = null,
    val headerSpeEn: String? = null,
    val id: Int,
    val product: String? = null,
    val productId: Int,
    val type: Int,
    val valueSpeAr: String? = null,
    val valueSpeEn: String? = null,
) : Parcelable
