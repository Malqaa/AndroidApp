package com.malka.androidappp.botmnav_fragments.home.model

import com.malka.androidappp.servicemodels.AdDetailModel

class DynamicList (
    val category_name:String,
    val category_icon:Int,
    val product:List<AdDetailModel>,
    val typeName:String="category",
    val detail:String=""
)