package com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel

import com.malka.androidappp.newPhase.data.helper.HelpFunctions

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