package com.malka.androidappp.activities_main.signup_account.signup_pg3

import com.malka.androidappp.helper.HelpFunctions

data class User(
    val address: String? = null,
    val area: String? = null,
    val city: String? = null,
    val country: String? = null,
    val createdAt: String? = null,
    val dateOfBirth: String? = null,
    val descriptions: String? = null,
    val distric: String? = null,
    val email: String? = null,
    val facebook: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val idGender: String? = null,
    val idStatus: String? = null,
    val image: String? = null,
    val instagram: String? = null,
    val ipAddress: String? = null,
    val ipArea: String? = null,
    val ipCity: String? = null,
    val ipCountry: String? = null,
    val isApproved: Int = 0,
    val isConfirm: Boolean = false,
    val isFavorite: Boolean = false,
    val isForgot: Boolean = false,
    val lastActive: String? = null,
    val firstName: String? = null,
    val lastname: String? = null,
    val linkedin: String? = null,
    val notify: Boolean = false,
    val notify_cat: String? = null,
    val online: Boolean = false,
    val password: String? = null,
    val phone: String? = null,
    val postCode: String? = null,
    val refreshTokens: List<RefreshTokenX>? = null,
    val region: String? = null,
    val state: String? = null,
    val tagline: String? = null,
    val twitter: String? = null,
    val updatedAt: String? = null,
    val username: String? = null,
    val views: Int = 0,
    val website: String? = null,
    val youtube: String? = null,
    val zipcode: String? = null,
    val cPassword: String? = null,
    val termsAndConditions: Boolean? = null,

    ) {
    val createdatFormated: String
        get() {
            createdAt?.let {
                val result: String = it.substring(0, createdAt.indexOf("."))

                return HelpFunctions.FormatDateTime(
                    result,
                    HelpFunctions.datetimeformat_24hrs,
                    HelpFunctions.datetimeformat_mmddyyyy
                )
            } ?: kotlin.run {
                return ""
            }

        }
    val fullName: String
        get() {
            return "$firstName $lastname"
        }

}