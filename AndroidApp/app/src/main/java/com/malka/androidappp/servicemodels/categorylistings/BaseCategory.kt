package com.malka.androidappp.servicemodels.categorylistings

import com.google.gson.annotations.SerializedName

open class BaseCategory(
    val id: String,
    val images: MutableList<String>,
    val featureexpirydate: String,
    val urgentexpirydate: String,
    val highlightexpirydate: String,
    val Producttitle: String,
    val Template: String,
    val selectasmain: String,
    val isfeatured: String,
    val iscontactemail: String,
    val iscontactchat: String,
    val isnogotiable: String,
    val Title: String,
    val isimported: String,
    val user: String,
    val Subtitle: String,
    val Quantity: String,
    val Phone: String,
    val Country: String,
    val Region: String,
    val City: String,
    val Address: String,
    val Description: String,
    @SerializedName("brand-new-item")
    val brand_new_item: String,
    val price: String,
    val cash_pm: String,
    val sa_bank_pm: String,
    val fixLength: String,
    val timepicker: String,
    @SerializedName("pickup-option")
    val pickup_option: String,
    @SerializedName("shipping-option")
    val shipping_option: String,
    @SerializedName("cost-amount")
    val cost_amount: String,
    @SerializedName("cost-desc")
    val cost_desc: String, val pack4: String
) {
}