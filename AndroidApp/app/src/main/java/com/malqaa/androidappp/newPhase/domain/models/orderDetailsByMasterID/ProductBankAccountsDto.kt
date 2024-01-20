package com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ProductBankAccountsDto(
    val id: Int=0,
    val accountNumber: String="",
    val bankName: String="",
    val bankHolderName: String="",
    val ibanNumber: String="",
    val swiftCode: String=""
) : Parcelable