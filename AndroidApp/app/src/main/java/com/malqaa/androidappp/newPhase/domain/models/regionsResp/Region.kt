package com.malqaa.androidappp.newPhase.domain.models.regionsResp

data class Region(
    val id: Int,
    val name: String,
    var isSelected:Boolean=false,
    var mainNeighborhoodList: List<Region>?=null
)