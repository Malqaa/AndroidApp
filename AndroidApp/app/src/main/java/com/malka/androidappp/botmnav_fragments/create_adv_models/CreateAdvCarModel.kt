package com.malka.androidappp.botmnav_fragments.create_ads

import org.joda.time.DateTime

data class CreateAdvCarModel(

    val platenumber: String? = null,
    val squencenumber: String? = null,
    val make: String? = null,
    val model: String? = null,
    val body: String? = null,
    val year: String? = null,
    val kilometers: String? = null,
    val fuel: String? = null,
    val cylinders: String? = null,
    val noofpreviousowner: String? = null,
    val transmission: String? = null,
    val vehiclesperiodicinspection: String? = null,
    val isimported: Boolean? = false,
    val isnogotiable: Boolean? = false,
    val sellertype: String? = null,

    //
    //
    val Id: String? = null,
    val City: String? = null,
    val Country: String? = null,
    val Name: String? = null,
    val Slug: String? = null,
    val Template: String? = null,
    val Region: String? = null,
    val Urgentexpirydate: String? = null,
    val Title: String? = null,
    val Price: String? = null,
    val user: String? = null,
    val StartingPrice: String? = null,
    val ReservePrice: String? = null,
    //public string CreatedDate { get; set; }
    val Duration: String? = null,
    val EndTime: String? = null,
    val FixLength: String? = null,
    val Timepicker: String? = null,
    val DateTime: String? = null,
    val createdBy: String? = null,
    val updatedOn: String? = null,
    val updatedBy: String? = null,
    val isActive: Boolean? = false,
    val Images: List<String>? = null,
    val referenceId: String? = null,
    val isWatching: Boolean? = false,
    val Isuserfavorite: Boolean? = false,
    val listingType: String? = null,
    val quantity: String? = null,
    val featureexpirydate: String? = null,
    val highlightexpirydate: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val pickupOption: String? = null,
    val shippingOption: String? = null,
    val pack4: String? = null,
    val description: String? = null,
    val subtitle: String? = null,
    val producttitle: String? = null,
    val brand_new_item: String? = null,
    val enddate: String? = null,
    val createdOn: DateTime? = null,
    val platform: String? = null,
    val isbankpaid: String? = null,
    val iscashpaid: String? = null
//
//

)
