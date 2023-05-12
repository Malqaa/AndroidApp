package com.malka.androidappp.newPhase.domain.models.loginResp

data class LoginUser(
    val activeCode: Boolean,
    val closeNotify: Boolean,
    val code: Int,
    val countryId: Any,
    val dateOfBirth: String?=null,
    val districtName: String?=null,
    val email: String?=null,
    val firstName: String?=null,
    val gender: Int,
    val id: String,
    val img: String?=null,
    val invitationCode: String?=null,
    val isBusinessAccount: Boolean,
    val lang: Any,
    val lastName: String?=null,
    val membershipNumber: Int,
    val neighborhoodId: Any,
    val password: String?=null,
    val phone: String?=null,
    val rate: Int,
    val regoinId: Any,
    val streetNumber: String?=null,
    val token: String?=null,
    val typeUser: Int,
    val userName: String?=null,
    val zipCode: String?=null,
    val createdAt:String?=null,
)
{
    val fullName:String
        get() {
            return "$firstName $lastName"
        }
}