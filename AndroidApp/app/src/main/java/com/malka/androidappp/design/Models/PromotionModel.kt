package com.malka.androidappp.design.Models

class PromotionModel(
    val packagename: String,
    val packageprice: String,
    val packageservice: List<String>,
    var is_select: Boolean = false,
    var is_common: Boolean = false
)