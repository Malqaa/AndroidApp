package com.malka.androidappp.fragments.memberlisting

data class MemberListingModel(
    val mlprodname: String = "",
    val mlproddes: String = "",
    val mlresrvepricetext: String = "",
    val mlresrveprice: String = "",
    val mlbuynowprice: String = "",
    val mlbuynowtext: String = "",
    val mlprodimg: Int? = null,
    val mlwatchlist: Int? = null
)