package com.malka.androidappp.botmnav_fragments.create_ads

data class Tags(
    val categoryid: Int,
    val keyword: String,
    val lang: String,
    val name: String,
    val path: String,
    val searchcategory_id: Int,
    val tags: String,
    var isSelect: Boolean=false
)