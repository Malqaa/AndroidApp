package com.malqaa.androidappp.newPhase.domain.model.loginWebsite

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody

data class LoginResponse(
    val code: String? = null,
    val message: String? = null,
    @SerializedName("status_code")
    val statusCode: Int,
    val status: String? = null,
    @SerializedName("data")
    val loginUser: LoginUser? = null
)

data class LoginUser(
    val activeCode: Boolean,
    val businessAccounts: List<BusinessAccountsDetails>? = null,
    val closeNotify: Boolean,
    val code: Int,
    val countryId: Int,
    val createdAt: String? = null,
    var dateOfBirth: String? = null,
    val districtName: String? = null,
    var email: String? = null,
    val firstName: String? = null,
    var gender: Int,
    val id: String,
    val img: String? = null,
    val invitationCode: String? = null,
    val lang: String? = null,
    var lastName: String? = null,
    val membershipNumber: Int,
    val neighborhoodId: Int,
    var password: String? = null,
    var phone: String? = null,
    val rate: Float,
    val regionId: Int,
    val streetNumber: String? = null,
    var token: String? = null,
    val typeUser: Int,
    var userName: String? = null,
    val zipCode: String? = null,
    var showUserInformation: String? = null
)

data class BusinessAccountsDetails(
    var businessAccountId: Int,
    var businessAccountName: String? = null,
    var providerId: String? = null,
)

fun String.toRequestBody(mediaType: String = "multipart/form-data"): RequestBody {
    return this.toRequestBody(mediaType.toMediaType().toString())
}


data class LoginRequest(
    val email: String,
    val password: String,
    val lang: String,
    val deviceId: String,
    val deviceType: String
) {
    fun toPartMap(): Map<String, @JvmSuppressWildcards RequestBody> {
        return mapOf(
            "email" to email.toRequestBody(),
            "password" to password.toRequestBody(),
            "lang" to lang.toRequestBody(),
            "deviceId" to deviceId.toRequestBody(),
            "deviceType" to deviceType.toRequestBody()
        )
    }
}

