package com.malka.androidappp.servicemodels

data class ModelSoldUnsold(
    val `data`: Data,
    val message: String,
    val status_code: Int
){
    data class Data(
        val solditems: List<AdDetailModel>,
        val solditemscount: Int,
        val unsolditems: List<AdDetailModel>,
        val sellingitems: List<AdDetailModel>,
        val unsolditemscount: Int,
        val sellingitemscount: Int
    )
}