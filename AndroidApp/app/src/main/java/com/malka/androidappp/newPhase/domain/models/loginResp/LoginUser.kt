package com.malka.androidappp.newPhase.domain.models.loginResp

data class LoginUser(
    val activeCode: Boolean,
    val businessAccounts: List<BusinessAccountsDetails>?=null,
    val closeNotify: Boolean,
    val code: Int,
    val countryId: Int,
    val createdAt: String?=null,
    val dateOfBirth: String?=null,
    val districtName: String?=null,
    val email: String?=null,
    val firstName: String?=null,
    val gender: Int,
    val id: String,
    val img: String?=null,
    val invitationCode: String?=null,
    val lang:  String?=null,
    val lastName: String?=null,
    val membershipNumber: Int,
    val neighborhoodId: Int,
    val password: String?=null,
    val phone:String?=null,
    val rate: Int,
    val regionId: Int,
    val streetNumber: String?=null,
    val token: String?=null,
    val typeUser: Int,
    val userName: String?=null,
    val zipCode: String?=null
)

data class BusinessAccountsDetails(
    var businessAccountId: Int,
    var businessAccountName: String? = null,
    var providerId: String? = null,
)