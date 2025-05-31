package com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans

data class Question(
    val _id: String,
    val advId: String,
    val answer: Answer,
    val buyerId: String,
    val comment: List<Comment>,
    val dateTime: String,
    val isAnswered: Boolean,
    val question: String,
    val sellerId: String
)