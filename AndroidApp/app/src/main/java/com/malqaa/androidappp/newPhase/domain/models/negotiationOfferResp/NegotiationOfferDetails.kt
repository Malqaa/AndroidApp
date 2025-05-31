package com.malqaa.androidappp.newPhase.domain.models.negotiationOfferResp

data class NegotiationOfferDetails(

    var offerId: Int,
    var productCategory: String?=null,
    var productId: Int,
    var productName: String? = null,
    var  buyerName: String? = null,
    var  buyerImage: String? = null,
    var sellerName: String? = null,
    var offerStatus: String? = null,
    var expireDate: String? = null,
    var productImage: String? = null,
    var senderImage: String? = null,
    var receiverImage: String? = null,
    var offerQuantity: Int,
    var productPrice: Float,
    var region: String? = null,
    var offerFromModule: String,
    var offerExpireDate: String? = null,
    var offerExpireHours: String? = null,
    var offerPrice: Float,
    var businessAccountId: String?=null,


)