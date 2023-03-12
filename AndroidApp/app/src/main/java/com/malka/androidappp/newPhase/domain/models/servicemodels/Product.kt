package com.malka.androidappp.newPhase.domain.models.servicemodels

import com.malka.androidappp.newPhase.data.helper.HelpFunctions


data class Product(
    val acceptQuestion: Boolean,
    val category: String,
    val categoryId: Int,
    val codeRegoin: String,
    val countView: Int,
    val country: String,
    val countryId: Any,
    val description: String,
    val id: Int,
    val isActive: Boolean,
    val isDelete: Boolean,
    val isMazad: Boolean,
    val isNegotiationOffers: Boolean,
    val isPaied: Boolean,
    val isSendOfferForMazad: Boolean,
    val lessPriceMazad: Int,
    val listMedia: List<Any>,
    val listProductSep: List<Any>,
    val name: String,
    val neighborhood: String,
    val neighborhoodId: Int,
    val pakatId: Int,
    val price: Int,
    val priceDisc: Int,
    val productMazadNegotiate: Any,
    val publishDate: String,
    val qty: Int,
    val regoinId: Any,
    val regoinName: String,
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