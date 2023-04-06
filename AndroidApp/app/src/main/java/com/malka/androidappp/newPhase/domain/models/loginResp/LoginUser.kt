package com.malka.androidappp.newPhase.domain.models.loginResp

data class LoginUser(
    val activeCode: Boolean,
    val closeNotify: Boolean,
    val code: Int,
    val countryId: Any,
    val dateOfBirth: String,
    val districtName: String,
    val email: String,
    val firstName: String,
    val gender: Int,
    val id: String,
    val img: String,
    val invitationCode: String,
    val isBusinessAccount: Boolean,
    val lang: Any,
    val lastName: String,
    val membershipNumber: Int,
    val neighborhoodId: Any,
    val password: String,
    val phone: String,
    val rate: Int,
    val regoinId: Any,
    val streetNumber: String,
    val token: String,
    val typeUser: Int,
    val userName: String,
    val zipCode: String,
    val createdAt:String?=null,
)
{
    val fullName:String
        get() {
            return "$firstName $lastName"
        }
}