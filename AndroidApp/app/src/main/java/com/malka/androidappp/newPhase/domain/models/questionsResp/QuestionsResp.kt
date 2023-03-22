package com.malka.androidappp.newPhase.domain.models.questionsResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.questionResp.QuestionItem

data class QuestionsResp (
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val questionList: List<QuestionItem>?=null
        )