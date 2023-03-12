package com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtpData(
    val createdAt: String? = null,
    val expiredAt: String? = null,
    val id: Int,
    val isVerified: Boolean,
    val otpCode: String? = null,
    val phoneNumber: String? = null,
    var userName: String? = null,
    var userEmail: String? = null,
    var userPass: String? = null,
    var isBusinessAccount: Boolean = false,
    var invitationCode: String

    ):Parcelable