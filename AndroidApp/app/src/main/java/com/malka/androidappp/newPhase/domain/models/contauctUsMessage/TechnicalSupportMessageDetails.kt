package com.malka.androidappp.newPhase.domain.models.contauctUsMessage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TechnicalSupportMessageDetails(
    val createdAt: String?=null,
    val createdBy: String?=null,
    val email: String?=null,
    val id: Int,
    val meassageDetails: String?=null,
    val mobileNumber: String?=null,
    val problemTitle: String?=null,
    val repliedAt: String?=null,
    val repliedBy: String?=null,
    val replyDetails: String?=null,
    val typeOfCommunication: Int,
    val userName: String?=null
):Parcelable