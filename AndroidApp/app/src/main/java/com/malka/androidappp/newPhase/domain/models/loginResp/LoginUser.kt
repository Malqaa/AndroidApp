package com.malka.androidappp.newPhase.domain.models.loginResp

data class LoginUser(
    val activeCode: Boolean,
    val businessAccounts: List<BusinessAccountsDetails>?=null,
    val closeNotify: Boolean,
    val code: Int,
    val countryId: Int,
    val createdAt: String?=null,
    var dateOfBirth: String?=null,
    val districtName: String?=null,
    var email: String?=null,
    val firstName: String?=null,
    var gender: Int,
    val id: String,
    val img: String?=null,
    val invitationCode: String?=null,
    val lang:  String?=null,
    var lastName: String?=null,
    val membershipNumber: Int,
    val neighborhoodId: Int,
    var password: String?=null,
    var phone:String?=null,
    val rate: Int,
    val regionId: Int,
    val streetNumber: String?=null,
    var token: String?=null,
    val typeUser: Int,
    var userName: String?=null,
    val zipCode: String?=null,
    var showUserInformation: String?=null
)

data class BusinessAccountsDetails(
    var businessAccountId: Int,
    var businessAccountName: String? = null,
    var providerId: String? = null,
)