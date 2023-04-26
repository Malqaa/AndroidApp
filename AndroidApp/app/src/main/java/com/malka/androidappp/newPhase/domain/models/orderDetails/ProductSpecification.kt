package com.malka.androidappp.newPhase.domain.models.orderDetails

data class ProductSpecification(
    val headerSpeAr: String?=null,
    val headerSpeEn: String?=null,
    val id: Int,
    val product: String?=null,
    val productId: Int,
    val type: Int,
    val valueSpeAr: String?=null,
    val valueSpeEn: String?=null,
)