package com.malka.androidappp.activities_main.product_detail

import com.malka.androidappp.helper.HelpFunctions

data class RateResponse(
    val `data`: List<RateReview>,
    val message: String,
    val status_code: Int
) {
    data class RateReview(
        val comment: String,
        val createdAt: String,
        val id: Int,
        val productNameAr: String,
        val productNameEn: String,
        val rate: Double,
        val userName: String,
        val userImage: String
    ){
        val dateTimeFormated: String
            get() {

                val dateTime: String = createdAt.substring(0, createdAt.indexOf("."))
                return  HelpFunctions.FormatDateTime(
                    dateTime,
                    HelpFunctions.datetimeformat_24hrs,
                    HelpFunctions.datetimeformat_mmddyyyy
                )

            }
    }
}