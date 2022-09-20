package com.malka.androidappp.servicemodels.model

class DynamicList (
    val category_name:String,
    val category_icon:String,
    val product:List<Products>,
    val category_id: Int,
    val typeName:String="category",
    val detail:String="",

)