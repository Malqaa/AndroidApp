package com.malka.androidappp.newPhase.domain.models.servicemodels.categorylistings

import com.malka.androidappp.newPhase.domain.models.servicemodels.AdDetailModel

data class SearchRespone(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
) {
    data class Data(
        val _id: String,
        val _index: String,
        val _score: Double,
        val _source: AdDetailModel,
        val _type: String,
        val hits: Any,
        val max_score: Any,
        val sort: Any,
        val total: Any
    )
}