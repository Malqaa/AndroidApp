package com.malka.androidappp.newPhase.domain.models.servicemodels.model_wonloss

import com.malka.androidappp.newPhase.domain.models.servicemodels.AdDetailModel

data class ModelWonData(
    val lostAuctions: List<AdDetailModel>,
    val wonAuctions: List<AdDetailModel>
)