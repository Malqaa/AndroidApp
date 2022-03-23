package com.malka.androidappp.design.Models

data class BusinessUserModel(
    val code: Any,
    val `data`: List<getBusinessList>,
    val errors: Any,
    val id: String,
    val isError: Boolean,
    val message: String,
    val status_code: Int
) {
    data class getBusinessList(
        val approvedBy: String,
        val approvedOn: String,
        val businessEmail: String,
        val businessFacebookURI: String,
        val businessInstagramURI: String,
        val businessSnapchatURI: String,
        val businessGoogleLatLng: String,
        val businessLogoPath: Any,
        val businessName: String,
        val businessPhone: String,
        val businessRegistrationExpiry: String,
        val businessRegistrationNumber: String,
        val businessTwitterURI: String,
        val businessType: String,
        val businessURL: String,
        val businessWatsappNumber: String,
        val businessYoutubeURI: String,
        val id: Int,
        val isApproved: Boolean,
        val userId: String
    )
}