package com.malka.androidappp.newPhase.domain.models.questionResp

data class QuestionItem(
    val id: Int,
    val clientId: String,
    val client: Any? = null,
    val productId: Int,
    val product: Any?,
    val question: String? = null,
    val answer: String? = null,
    val answeredById: String? = null,
    val answeredBy: Any?,
    val createdAt: String? = null,
    val answeredAt :String? = null,
)