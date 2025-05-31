package com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.getbidModel

import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.getbidModel.BidingData

data class ModelBidingResponse(
    val `data`: BidingData,
    val message: String,
    val status_code: Int
)