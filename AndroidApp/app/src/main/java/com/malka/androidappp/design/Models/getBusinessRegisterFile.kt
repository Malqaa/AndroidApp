package com.malka.androidappp.design.Models

data class getBusinessRegisterFile(
    val code: Any,
    val `data`: List<GetDocuments>,
    val errors: Any,
    val id: Any,
    val isError: Boolean,
    val message: String,
    val status_code: Int
){
    data class GetDocuments(
        val businessId: Any,
        val documentName: String,
        val isActive: Boolean,
        val uploadedOn: String,
        val createdBy: String,
    )

}
