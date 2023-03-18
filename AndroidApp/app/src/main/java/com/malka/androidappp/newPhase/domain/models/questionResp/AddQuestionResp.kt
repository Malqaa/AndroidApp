package com.malka.androidappp.newPhase.domain.models.questionResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.productResp.Product

data class AddQuestionResp(
    val code: String?=null,
    val message: String="",
    val status_code: Int,
    val status:String?=null,
    @SerializedName("data")
    val productDetails: QuestionItem?=null
)