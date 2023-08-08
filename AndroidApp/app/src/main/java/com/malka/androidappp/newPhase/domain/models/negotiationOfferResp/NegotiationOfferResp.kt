package com.malka.androidappp.newPhase.domain.models.negotiationOfferResp

import com.google.gson.annotations.SerializedName

data class NegotiationOfferResp(
    @SerializedName("data")
    val negotiationOfferDetailsList: List<NegotiationOfferDetails>?=null,
    val message: String?=null,
    val status: String?=null,
    val status_code: Int
)