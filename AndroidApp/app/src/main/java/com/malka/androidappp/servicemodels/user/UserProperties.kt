package com.malka.androidappp.servicemodels.user

import org.joda.time.DateTime

data class UserProperties(
    val id: String,
    val username: String,
    val isFavorite: Boolean,
    val password: String,
    val isForgot: Boolean,
    val isConfirm: Boolean,
    val email: String,
    val idStatus: String,
    val views: Int,
    val createdAt: String,
    val updatedAt: DateTime,
    val fullName: String,
    val tagline: String,
    val descriptions: String,
    val website: String,
    val idGender: String,
    val phone: String,
    val postCode: String,
    val area: String,
    val ipArea: String,
    val address: String,
    val ipAddress: String,
    val country: String,
    val ipCountry: String,
    val state: String,
    val city: String,
    val ipCity: String,
    val image: String,
    val lastActive: String,
    val facebook: String,
    val twitter: String,
    val instagram: String,
    val linkedin: String,
    val youtube: String,
    val online: Boolean,
    val notify: Boolean,
    val notify_cat: String,
    val gender: String,
    val region: String,
    val distric: String,
    val lastname: String,
    val dateOfBirth: String,
    val zipcode: String,
    val refreshTokens: String
)