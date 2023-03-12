package com.malka.androidappp.fragments.sellerdetails

import com.malka.androidappp.newPhase.domain.models.servicemodels.User
import com.malka.androidappp.newPhase.domain.models.servicemodels.AdDetailModel

data class Data(
    val advertisements: List<AdDetailModel>,
    val detailOfUser: User
)