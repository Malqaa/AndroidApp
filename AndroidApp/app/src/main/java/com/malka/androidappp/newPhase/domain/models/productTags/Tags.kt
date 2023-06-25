package com.malka.androidappp.newPhase.domain.models.productTags

data class Tags(
    val categoryid: Int,
    val keyword: String,
    val lang: String,
    val name: String,
    val path: String,
    val searchcategory_id: Int,
    val tags: String,

    var productTitle: String?=null,
    var productCategoryId: Int,
    var categories:List<String>?=null,
    var isSelect: Boolean=false,
    var category:String?=null,
)