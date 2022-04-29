package com.malka.androidappp.servicemodels

data class Country(
    val countryId: String,
    val createdBy: String,
    val createdOn: String,
    val id: String,
    val isActive: Boolean,
    val isDetail: Boolean,
    val key: String,
    val name: String,
    val flagimglink: String?=null,
    val countryCode: String?=null,
)