package com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp

data class PointsTransactionsItem(
    val businessAccountId: String?=null,
    val id: Int,
    val invitationcode: String?=null,
    val totalPointsBalance: Int,
    val transactionAmount: Int,
    val transactionDate: String?=null,
    val transactionSource: String?=null,
    val transactionType: String?=null,
    val userId: String?=null
)