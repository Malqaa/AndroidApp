package com.malqaa.androidappp.newPhase.domain.models.productResp

import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category


data class Product(
    val acceptQuestion: Boolean,
    val isFixedPriceEnabled: Boolean,
    val isAuctionEnabled: Boolean,
    val isNegotiationEnabled: Boolean,
    val price: Float,
    var priceDisc: Float,
    var priceDiscount: Float,
    var paymentOptionId: Int,
    val isCashEnabled: Boolean,
    val auctionStartPrice: Float,
    val disccountEndDate: String? = null,
    val sendOfferForAuction: Boolean,
    val isAuctionClosingTimeFixed : Boolean,
    val auctionMinimumPrice: Float,
    val auctionNegotiateForWhom: String?=null,
    val auctionNegotiatePrice: Float,
    val auctionClosingTime: String? = null,
    val id: Int,
    var productPosition: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val name: String? = null,
    val nameAr: String? = null,
    val nameEn: String? = null,
    val subTitle: String? = null,
    val subTitleAr: String? = null,
    val subTitleEn: String? = null,
    val description: String? = null,
    val descriptionAr: String? = null,
    val descriptionEn: String? = null,
    val qty: Int,
    val productImage: String? = null,
    val isAuctionPaied: Boolean,
    val status: Int,
    val countView: Int,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val categoryId: Int,
    val categoryDto: Category? = null,
    val category: String? = null,
    val countryId: Int,
    val country: String? = null,
    val regionId: Int,
    val regionName: String? = null,
    val neighborhoodId: Int,
    val neighborhood: String? = null,
    val district: String? = null,
    val street: String? = null,
    val governmentCode: String? = null,
    val pakatId: Int,
    var isFavourite: Boolean,
    val pickUpDeliveryOption: String? = null,
    val productShippingOptions: List<PorductShippingOptionItem>? = null,
    val isFreeDelivery: Boolean,
    val isMerchant: Boolean,
    val negotiationOffersCount: Int,
    val purchasedQuantity: Int,
    val restQuantity: Int,
    val addedToFavoritsCount: Int,
    val viewsCount: Int,
    val lat: String? = null,
    val lon: String? = null,
    val listMedia: ArrayList<ImageSelectModel>? = null,
    val listProductSep: List<DynamicSpecificationSentObject>? = null,
    val productMazadNegotiate: ProductMazadNegotiateObject? = null,
    var highestBidPrice:Float,
    var bankAccountId:Int,
    // val sellerInformation: SellerInformation?=null,

    //*******
    var image: String? = null,
    var myBid:Float,




    var productPublishPrice:Float,
    var selectedPacket: PakatDetails? = null,



//    val isPaied: Boolean,
//    val isNegotiationOffers: Boolean,
//    val withFixedPrice: Boolean,
//    val isMazad: Boolean,
//    val isSendOfferForMazad: Boolean,
//    val startPriceMazad: Int,
//    val lessPriceMazad: Int,
//
//    val codeRegoin: String,
//
//    val publishDate: String,
//    val streetName: String,
//    val stutes: Int,
//    val updateDate: String? = null,


) {

//    val createdOnFormated: String
//        get() {
//            updatedAt?.let {
//                // val result: String = it.substring(0, updateDate.indexOf("."))
//
//                return HelpFunctions.FormatDateTime(
//                    it,
//                    HelpFunctions.datetimeformat_24hrs,
//                    HelpFunctions.datetimeformat_mmddyyyy
//                )
//            } ?: kotlin.run {
//                return ""
//            }
//
//        }
}


//"sellerInformation":{
//    "providerId":"string",
//    "businessAccountId":0,
//    "image":"string",
//    "name":"string",
//    "city":"string",
//    "phone":"string",
//    "instagram":"string",
//    "youTube":"string",
//    "webSite":"string",
//    "faceBook":"string",
//    "twitter":"string",
//    "linkedIn":"string",
//    "snapchat":"string",
//    "tikTok":"string",
//    "createdAt":"2023-06-24T11:17:21.421Z",
//    "lat":"string",
//    "lon":"string",
//    "isFollowed":true,
//    "rate":0,
//    "showUserInformation":"EveryOne",
//    "branches":[
//    {
//        "id":0,
//        "location":"string",
//        "lat":"string",
//        "lng":"string",
//        "streetName":"string",
//        "regionCode":"string",
//        "userName":"string",
//        "name":"string",
//        "isActive":true,
//        "countryName":"string",
//        "regionName":"string",
//        "neighborhoodName":"string",
//        "country":{
//        "id":0,
//        "name":"string"
//    },
//        "neighborhood":{
//        "id":0,
//        "name":"string"
//    },
//        "region":{
//        "id":0,
//        "name":"string"
//    }
//    }
//    ]
//},
//"totalRecords":0
