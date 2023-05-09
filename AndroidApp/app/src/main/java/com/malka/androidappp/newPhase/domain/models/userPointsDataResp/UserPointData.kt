package com.malka.androidappp.newPhase.domain.models.userPointsDataResp

data class UserPointData(
    val invitationCodePoints: Int,
    val monyOfPointsTransfered: Int,
    val newInvitationCode: String?=null,
    val pointsBalance: Int,
    val pointsCountToTransfer: Int,
    val pointsTransactionslist: List<PointsTransactionsItem>?=null
)