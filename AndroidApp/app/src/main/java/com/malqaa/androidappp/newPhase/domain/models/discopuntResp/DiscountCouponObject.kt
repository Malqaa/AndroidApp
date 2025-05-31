package com.malqaa.androidappp.newPhase.domain.models.discopuntResp

data class DiscountCouponObject(
    val adminCouponFor: String?=null,
    val adminCouponForBussAccProductOrPakat: String?=null,
    val couponCode: String?=null,
    val createdAt: String?=null,
    val createdBy: String?=null,
    val createdByBusinessAccountId: String?=null,
    val createdByProviderId: String?=null,
    val descriptionAr: String?=null,
    val descriptionEn: String?=null,
    val discountTypeID: String?=null,
    val discountValue: Float,
    val excludeDiscountedProducts: Boolean,
    val expiryDate: String?=null,
    val id: Int,
    val image: String?=null,
    val isActive: Boolean,
    val isAdminCoupon: Boolean,
    val isDeleted: Boolean,
    val isFreeDelivery: Boolean,
    val maxUseLimit: Int,
    val maxUsePerClient: Int,
    val maximumDiscount: Float,
    val titleAr: String?=null,
    val titleEn: String?=null,
    val totalUsedCount: Int
)