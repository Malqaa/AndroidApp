package com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel

import com.malqaa.androidappp.newPhase.utils.HelpFunctions

data class Answer(
    var description: String,
    var updatedDateTime: String
){

    val dateTimeFormated: String
        get() {

            val dateTime: String = updatedDateTime.substring(0, updatedDateTime.indexOf("."))
            return  HelpFunctions.FormatDateTime(
                dateTime,
                HelpFunctions.datetimeformat_24hrs,
                HelpFunctions.datetimeformat_mmddyyyy
            )

        }
}