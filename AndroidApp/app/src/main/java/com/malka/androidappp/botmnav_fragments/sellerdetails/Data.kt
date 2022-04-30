package com.malka.androidappp.botmnav_fragments.sellerdetails

import com.malka.androidappp.servicemodels.User
import com.malka.androidappp.servicemodels.AdDetailModel

data class Data(
    val advertisements: List<AdDetailModel>,
    val detailOfUser: User
)