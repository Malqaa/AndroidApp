package com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel

import com.malqaa.androidappp.newPhase.utils.HelpFunctions

data class Question(
    val _id: String,
    val advId: String,
    val answer: Answer,
    val buyerId: String,
    val dateTime: String,
    var isAnswered: Boolean,
    val question: String,
    val sellerId: String
){

    val dateTimeFormated: String
        get() {

            val dateTime: String = dateTime.substring(0, dateTime.indexOf("."))
            return  HelpFunctions.FormatDateTime(
                dateTime,
                HelpFunctions.datetimeformat_24hrs,
                HelpFunctions.datetimeformat_mmddyyyy
            )

        }
}