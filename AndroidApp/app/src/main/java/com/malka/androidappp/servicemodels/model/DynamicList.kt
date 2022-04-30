package com.malka.androidappp.servicemodels.model

import com.malka.androidappp.servicemodels.AdDetailModel

class DynamicList (
    val category_name:String,
    val category_icon:Int,
    val product:List<AdDetailModel>,
    val typeName:String="category",
    val detail:String="",
    val category_id: String
)