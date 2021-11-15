package com.malka.androidappp.botmnav_fragments.create_ads

import com.google.gson.annotations.SerializedName

data class ModelDataCreatePropertyAd (

    val images: List<String>? = null,
    val id:String? = null,
    val floorarea:String? = null,
    val bedrooms:String? = null,
    val bathrooms:String? = null,
    val landarea:String? =null,
    val floorsnumber:String? = null,
    val featureexpirydate:String? = null,
    val urgentexpirydate:String? = null,
    val highlightexpirydate:String? = null,
    val producttitle:String? = null,
    val template:String? = null,
    val iscontactphone:Boolean? = false,
    val selectasmain:String? = null,
    val isfeatured:Boolean? = false,
    val iscontactemail:Boolean? = false,
    val iscontactchat:Boolean? = false,
    val isnogotiable:Boolean? = false,
    val isphonehidden:Boolean? = null,
    val title:String? = null,
    val user:String? = null,
    val subtitle:String? = null,
    val quantity:String? = null,
    val phone:String? = null,
    val country:String? = null,
    val region:String? = null,
    val city:String? = null,
    val address:String? = null,
    val description:String? = null,
    @SerializedName("brand-new-item") var brand_new_item:Any? = 0,
    val price:String? = null,
    val cash_pm:String? = null,
    val sa_bank_pm:String? = null,
    val fixLength:String? = null,
    val timepicker:String? = null,
    @SerializedName("pickup-option") var pickup_option:String? = null,
    @SerializedName("shipping-option") var shipping_option:String? = null,
    @SerializedName("cost-amount") var cost_amount:String? = null,
    @SerializedName("cost-desc") var cost_desc:String? = null,
    val pack4:String? = null

)