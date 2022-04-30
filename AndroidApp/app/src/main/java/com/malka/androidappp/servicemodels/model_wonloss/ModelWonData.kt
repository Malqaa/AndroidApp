package com.malka.androidappp.servicemodels.model_wonloss

import com.malka.androidappp.servicemodels.AdDetailModel

data class ModelWonData(
    val lostAuctions: List<AdDetailModel>,
    val wonAuctions: List<AdDetailModel>
)