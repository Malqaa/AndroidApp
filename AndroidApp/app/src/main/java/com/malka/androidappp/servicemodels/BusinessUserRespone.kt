package com.malka.androidappp.servicemodels

data class BusinessUserRespone(
    val code: Any,
    val `data`: List<BusinessUser>,
    val errors: Any,
    val id: String,
    val isError: Boolean,
    val message: String,
    val status_code: Int
) {
    data class BusinessUser(
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
        var businessRegistrationExpiry: String,
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