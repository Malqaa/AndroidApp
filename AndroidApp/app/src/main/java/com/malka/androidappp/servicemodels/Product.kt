package com.malka.androidappp.servicemodels


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
    val qty: Int,
    val regoinId: Any,
    val regoinName: String,
    val startPriceMazad: Int,
    val streetName: String,
    val stutes: Int,
    val subTitle: String,
    val updatedAt: String?=null,
    val createdAt: String?=null,
    val withFixedPrice: Boolean,
    val image: String?=null
){

    val createdOnFormated: String
        get() {
            updatedAt?.let {
//                return  HelpFunctions.FormatDateTime(
//                    it,
//                    HelpFunctions.datetimeformat_24hrs,
//                    HelpFunctions.datetimeformat_mmddyyyy
//                )
                return  it
            }?:kotlin.run {
                createdAt?.let {
//                    return  HelpFunctions.FormatDateTime(
//                        it,
//                        HelpFunctions.datetimeformat_24hrs,
//                        HelpFunctions.datetimeformat_mmddyyyy
//                    )
                    return  it
                }?:kotlin.run {
                    return ""
                }
            }


        }
}