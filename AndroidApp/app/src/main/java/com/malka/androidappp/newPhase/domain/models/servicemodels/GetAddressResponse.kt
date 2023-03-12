package com.malka.androidappp.newPhase.domain.models.servicemodels

data class GetAddressResponse(
    val `data`: List<AddressModel>,
    val message: String,
    val status_code: Int
) {
    data class AddressModel(
        val address: String,
        val city: String,
        val country: String,
        val createdBy: String,
        val createdOn: String,
        val firstName: String,
        var id: String,
        val isActive: Boolean,
        val lastName: String,
        val mobileNo: String,
        val region: String,
        val updatedBy: Any,
        val updatedOn: String,
        val userId: String,
        var is_select: Boolean=false
    )
}