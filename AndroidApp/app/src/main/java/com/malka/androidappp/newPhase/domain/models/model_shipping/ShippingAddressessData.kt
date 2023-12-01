package com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping

data class ShippingAddressessData(
    val id: String,
    val country: String,
    val region: String,
    val city: String,
    val address: String,
    val mobileNo: String,
    val firstName: String,
    val lastName: String,
    val createdBy: String,
    val createdOn: String,
    val updatedBy: String,
    val updatedOn: String,
    val isActive: Boolean,
    val userId: String,
    var selected: Boolean = false
)