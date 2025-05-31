package com.malka.androidappp.recycler_browsecat

data class BrowseMarketModel(
    val marketprodname: String = "",
    val marketproddes: String = "",
    val marketresrvepricetext: String = "",
    val marketresrveprice: String = "",
    val marketbuynowprice: String = "",
    val marketbuynowtext: String = "",
    val marketprodimg: String = "",
    val marketwatchlist: String = "",
    val advid: String = "",
    var ItemInWatchlist: Boolean = false
)