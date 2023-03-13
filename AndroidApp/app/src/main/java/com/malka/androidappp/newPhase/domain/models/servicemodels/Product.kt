package com.malka.androidappp.newPhase.domain.models.servicemodels

import com.malka.androidappp.newPhase.data.helper.HelpFunctions


data class Product(
    var id: Int,
    var isActive: Boolean,
    var image:String?=null,
    var name: String?=null,
    var qty: Int,
    var price: Float,
    var priceDiscount: Float,
    var createdAt: String?=null,
    var updatedAt: String?=null,
    var isPaied: Boolean,
   var isFavourite: Boolean,
    val acceptQuestion: Boolean,
    val category: String,
    val categoryId: Int,
    val codeRegoin: String,
    val countView: Int,
    val country: String,
    val countryId: Any,
    val description: String,
    val productImage:String?=null,
    val regionId: Int,
    val regionName: String?=null,

    val isDelete: Boolean,
    val isMazad: Boolean,
    val isNegotiationOffers: Boolean,
    val isSendOfferForMazad: Boolean,
    val lessPriceMazad: Int,
    val listMedia: List<Any>,
    val listProductSep: List<Any>,

    val neighborhood: String,
    val neighborhoodId: Int,
    val pakatId: Int,

    val priceDisc: Int,
    val productMazadNegotiate: Any,
    val publishDate: String,


    val startPriceMazad: Int,
    val streetName: String,
    val stutes: Int,
    val subTitle: String,
    val updateDate: String?=null,
    val withFixedPrice: Boolean
){

    val createdOnFormated: String
        get() {
            updateDate?.let {
               // val result: String = it.substring(0, updateDate.indexOf("."))

                return  HelpFunctions.FormatDateTime(
                    it,
                    HelpFunctions.datetimeformat_24hrs,
                    HelpFunctions.datetimeformat_mmddyyyy
                )
            }?:kotlin.run {
                return ""
            }

        }
}