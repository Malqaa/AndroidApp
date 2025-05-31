package com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage

data class ContactUsMessage(
    val email: String?=null,
    val id: Int,
    val meassageDetails: String?=null,
    val mobileNumber: String?=null,
    val problemTitle: String?=null,
    val typeOfCommunication: Int
)