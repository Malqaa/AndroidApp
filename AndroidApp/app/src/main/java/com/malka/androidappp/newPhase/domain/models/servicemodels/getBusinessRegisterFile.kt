package com.malka.androidappp.newPhase.domain.models.servicemodels

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
        val businessId: String,
        val documentName: String,
        val isActive: Boolean,
        val uploadedOn: String,
        val createdBy: String,
    )

}
