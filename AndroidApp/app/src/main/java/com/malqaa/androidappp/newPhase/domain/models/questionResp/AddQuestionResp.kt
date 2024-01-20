package com.malqaa.androidappp.newPhase.domain.models.questionResp

import com.google.gson.annotations.SerializedName

data class AddQuestionResp(
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val question: QuestionItem?=null
)