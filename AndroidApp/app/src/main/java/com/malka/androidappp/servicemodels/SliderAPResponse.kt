package com.malka.androidappp.servicemodels

data class SliderAPResponse(
    val `data`: List<Data>,
    val message: String,
    val status_code: Int
) {
    data class Data(
        val id: Int,
        val img: String,
        val isActive: Boolean,
        val type: Int,
        val url: String
    )
}