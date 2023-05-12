package com.malka.androidappp.newPhase.domain.models.servicemodels

import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.yariksoffice.lingver.Lingver

data class SubSpecification(
    val createdAt: String,
    val id: Int,
    val isActive: Boolean,
    val nameAr: String,
    val nameEn: String
){
    val name: String


        get() {
            return if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC && !nameAr.isBlank()) nameAr else nameEn
                ?: ""
        }
}