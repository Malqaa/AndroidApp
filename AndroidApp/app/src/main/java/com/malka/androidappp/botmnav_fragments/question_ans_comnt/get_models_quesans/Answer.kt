package com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans

import com.malka.androidappp.helper.HelpFunctions

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