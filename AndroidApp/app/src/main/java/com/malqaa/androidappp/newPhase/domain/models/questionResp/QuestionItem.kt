package com.malqaa.androidappp.newPhase.domain.models.questionResp

data class QuestionItem(
    val id: Int,
    val productName:String?= null,
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
    val clientName:String?=null,
val clientImage:String?=null,
    val answeredByName:String?=null,
    var answeredByImage:String?=null,
    val noAnsweredQuetionsCount:Int
)

//"isActive": true,
//"isDeleted": false,
//"isShared": true,
