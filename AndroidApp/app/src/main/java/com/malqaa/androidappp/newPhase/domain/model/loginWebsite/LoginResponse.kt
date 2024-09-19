package com.malqaa.androidappp.newPhase.domain.model.loginWebsite

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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

data class LoginRequest(
    val email: String,
    val password: String,
    val lang: String,
    val deviceId: String,
    val deviceType: String
)

// Extension function to convert String to RequestBody
fun String.toRequestBody(mediaType: String = "multipart/form-data"): RequestBody {
    return this.toRequestBody(mediaType.toMediaType())
}


data class DomainLoginResponse(
    val code: String?,
    val message: String?,
    val statusCode: Int,
    val status: String?,
    val loginUser: DomainLoginUser?
)

data class DomainLoginUser(
    val activeCode: Boolean,
    val businessAccounts: List<DomainBusinessAccount>? = null,
    val closeNotify: Boolean,
    val code: Int,
    val countryId: Int,
    val createdAt: String?,
    val dateOfBirth: String?,
    val districtName: String?,
    val email: String?,
    val firstName: String?,
    val gender: Int,
    val id: String,
    val img: String?,
    val invitationCode: String?,
    val lang: String?,
    val lastName: String?,
    val membershipNumber: Int,
    val neighborhoodId: Int,
    val password: String?,
    val phone: String?,
    val rate: Float,
    val regionId: Int,
    val streetNumber: String?,
    val token: String?,
    val typeUser: Int,
    val userName: String?,
    val zipCode: String?,
    val showUserInformation: String?
)
data class DomainBusinessAccount(
    val businessAccountId: Int,
    val businessAccountName: String?,
    val providerId: String?
)


