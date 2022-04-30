package com.malka.androidappp.servicemodels

import com.malka.androidappp.helper.HelpFunctions

data class getCartModel(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
) {
    data class Data(
        val _id: String,
        val advertisements: AdDetailModel,
        val createddate: String,
        val isActive: Boolean,
        val loggenIn: String,
        val sellerId: String,
        val orderNumber: String,
        val orderStatus: String,
        val user: User
    ){
        val createddateFormated: String
            get() {
                createddate.let {
                    val result: String = it.substring(0, createddate.indexOf("."))
                    return  HelpFunctions.FormatDateTime(
                        result,
                        HelpFunctions.datetimeformat_24hrs,
                        HelpFunctions.datetimeformat_mmddyyyy
                    )
                }

            }
    }

}