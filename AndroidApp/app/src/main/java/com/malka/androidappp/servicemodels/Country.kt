package com.malka.androidappp.servicemodels

import com.yariksoffice.lingver.Lingver

data class Country(
    val id: Int,
    val nameAr: String,
    val nameEn: String,
    val flagimglink: String?=null,
    val countryCode: String?=null,
){
    val name: String


        get() {
            return if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC && !nameAr.isBlank()) nameAr else nameEn
                ?: ""
        }
}