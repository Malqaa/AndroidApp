package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

data class ModelDataCreateGeneralAd(


    val images: List<String>? = null,
    val id:String? = null,
    val name:String? = null,
    val slug:String? = null,
    val tag:String? = null,
    val featureexpirydate:String? = null,
    val urgentexpirydate:String? = null,
    val highlightexpirydate:String? = null,
    val producttitle:String? = null,
    val Template:String? = null,
    val iscontactphone:Boolean? = false,
    val iscontactemail:Boolean? = false,
    val iscontactchat:Boolean? = false,
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
    val pack4:String? = null,
    val iswatching:Boolean? = false,
    val platform:String? = null
)