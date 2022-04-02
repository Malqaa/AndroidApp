package com.malka.androidappp.servicemodels.checkout

data class CheckoutRequestModel(
    var cartId: MutableList<String>,
    var addressId: String,
    var tax: String,
    var totalamount: String,
    var creditCardNo: String,
    var loginId: String,
    var shippingmethod: String,
    var paymentType: MutableList<String>,
    var bankAccountId: MutableList<Int>,

    )