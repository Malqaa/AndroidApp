package com.malka.androidappp.newPhase.domain.models.negotiationOfferResp

data class NegotiationOfferDetails(
    var  buyerName: String? = null,
    var expireDate: String? = null,
    var offerExpireDate: String? = null,
    var offerExpireHours: String? = null,
    var offerFromModule: Int,
    var offerId: Int,
    var offerPrice: Double,
    var offerStatus: String? = null,
    var productCategory: String? = null,
    var productId: Int,
    var productImage: String? = null,
    var productName: String? = null,
    var productPrice: Double,
    var receiverImage: String? = null,
    var region: String? = null,
    var sellerName: String? = null,
    var senderImage: String? = null
)