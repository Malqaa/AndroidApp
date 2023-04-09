package com.malka.androidappp.newPhase.domain.models.productResp

import com.malka.androidappp.newPhase.data.helper.HelpFunctions


data class Product(
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val name: String? = null,
    val subTitle: String? = null,
    val description: String? = null,
    val qty: Int,
    val productImage: String? = null,
    val isPaied: Boolean,
    val price: Float,
    val disccountEndDate: String? = null,
    val acceptQuestion: Boolean,
    val isNegotiationOffers: Boolean,
    val withFixedPrice: Boolean,
    val isMazad: Boolean,
    val isSendOfferForMazad: Boolean,
    val startPriceMazad: Int,
    val lessPriceMazad: Int,
    val status: Int,
    val countView: Int,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val categoryId: Int,
    val category: String ,
    val countryId: Int,
    val country: String? = null,
    val regionId: Int,
    val regionName: String? = null,
    val neighborhood: String? = null,
    val neighborhoodId: Int,
    val district: String? = null,
    val street: String? = null,
    val governmentCode: String? = null,
    val pakatId: Int,
    var isFavourite: Boolean,
    val listMedia: List<ProductMediaItemDetails>? = null,
    val listProductSep: List<ProductSpecialityItemDetails>? = null,
    val productMazadNegotiate:ProductMazadNegotiateObject?=null,
    val sellerInformation:SellerInformation?=null,
    var image: String? = null,
    val codeRegoin: String,
    var priceDisc: Float,
    var priceDiscount:Float,
    val publishDate: String,
    val streetName: String,
    val stutes: Int,
    val updateDate: String? = null,
    val isFreeDeleivry:Boolean,
    val isMerchant:Boolean

) {

    val createdOnFormated: String
        get() {
            updateDate?.let {
                // val result: String = it.substring(0, updateDate.indexOf("."))

                return HelpFunctions.FormatDateTime(
                    it,
                    HelpFunctions.datetimeformat_24hrs,
                    HelpFunctions.datetimeformat_mmddyyyy
                )
            } ?: kotlin.run {
                return ""
            }

        }
}