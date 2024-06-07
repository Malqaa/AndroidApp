package com.malqaa.androidappp.newPhase.domain.models.servicemodels

data class Category(
    val description: String,
    val id: Int,
    val productCategoryId:Int,
    val image: String,
    val isActive: Boolean,
    val enableFixedPrice: Boolean,
    val isShowInHome: Boolean,
    val name: String,
    val postion: Int,
    var category:String?=null,
    val productFeeDuetTime: Int,
    val productPriceType: Int,
    var productPublishPrice: Float,
    var is_select: Boolean = false,
    var isCategory: Boolean = false,
    var template: String = "",
    var categoryKey: String = "",
    var jsonFilePath: String = "",
    var isSelected: Boolean = false,
    var isFollow: Boolean = false,
    var list: List<Category>,

    var isSelect:Boolean,
    /***/
    var isRealState: Boolean,
    var freeProductImagesCount: Int,
    var freeProductVidoesCount: Int,
    var extraProductImageFee: Float,
    var extraProductVidoeFee: Float,
    var enableAuction: Boolean,
    var enableNegotiation: Boolean,
    var minimumBidValue: Float,
    var subTitleFee: Float,
    var auctionClosingPeriods: String? = null,
    var auctionClosingPeriodsUnit: Int,
    var auctionClosingTimeFee: Float,
    var enableFixedPriceSaleFee: Float,
    var enableAuctionFee:Float,
    var enableNegotiationFee:Float,



//    "isRealState": false,
//    "freeProductImagesCount": 5,
//"freeProductVidoesCount": 2,
//"extraProductImageFee": 2,
//"extraProductVidoeFee": 1,
//"enableAuction": true,
//"enableNegotiation": true,
//"minimumBidValue": 10,
//"subTitleFee": 2,
//"auctionClosingPeriods": "1,2,3,4",
//"auctionClosingPeriodsUnit": 2,
//"auctionClosingTimeFee": 25

//
//{ tags
//    "productTitle": "test 123",
//    "productCategoryId": 5,
//    "categories": [
//    "cars"
//    ],
//    "category": "cars",
//    "freeProductImagesCount": 5,
//    "freeProductVidoesCount": 2,
//    "extraProductImageFee": 2,
//    "extraProductVidoeFee": 1,
//    "enableAuction": true,
//    "enableNegotiation": true,
//    "minimumBidValue": 10,
//    "subTitleFee": 2,
//    "auctionClosingPeriods": "1,2,3,4",
//    "auctionClosingPeriodsUnit": 2,
//    "auctionClosingTimeFee": 25
//},

)
