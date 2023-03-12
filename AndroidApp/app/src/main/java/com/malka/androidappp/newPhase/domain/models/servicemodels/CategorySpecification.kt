package com.malka.androidappp.newPhase.domain.models.servicemodels

data class CategorySpecification(
    val createdAt: String,
    val description: String,
    val id: Int,
    val isActive: Boolean,
    val isRequired: Boolean,
    val name: String,
    val placeHolder: String,
    val subSpecifications: List<SubSpecification>,
    val type: Int
){

}