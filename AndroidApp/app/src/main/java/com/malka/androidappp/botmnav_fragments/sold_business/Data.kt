package com.malka.androidappp.botmnav_fragments.sold_business

data class Data(
    val solditems: List<Solditem>,
    val solditemscount: Int,
    val unsolditems: List<Unsolditem>,
    val unsolditemscount: Int
)