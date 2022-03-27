package com.malka.androidappp.servicemodels
import com.malka.androidappp.helper.HelpFunctions
data class AdDetailModel(
    val createdOn: String? = null,
    val address: String? = null,
    val brandNewItem: String? = null,
    val cashPm: String? = null,
    val city: String? = null,
    val costAmount: String? = null,
    val costDesc: String? = null,
    val country: String? = null,
    val description: String? = null,
    val duration: String? = null,
    val endTime: String? = null,
    val enddate: String? = null,
    val expiredate: String? = null,
    val featureexpirydate: String? = null,
    val fixLength: String? = null,
    val highlightexpirydate: String? = null,
    val id: String? = null,
    val images: List<String>? = null,
    var isWatching: Boolean? = null,
    val iscontactchat: String? = null,
    val iscontactemail: String? = null,
    val iscontactphone: String? = null,
    val isuserfavorite: Boolean? = null,
    val listingType: String? = null,
    val name: String? = null,
    val pack4: String? = null,
    val phone: String? = null,
    val pickupOption: String? = null,
    val price: String? = null,
    val producttitle: String? = null,
    val quantity: String? = null,
    val region: String? = null,
    val reservePrice: String? = null,
    val saBankPm: String? = null,
    val shippingOption: String? = null,
    val slug: String? = null,
    val startingPrice: String? = null,
    val subtitle: String? = null,
    val tag: String? = null,
    val template: String? = null,
    val timepicker: String? = null,
    val title: String? = null,
    val urgentexpirydate: String? = null,
    val user: String? = null,
    val itemviews: String? = null,
    val video: String? = null,
    val subcatonekey: String? = null,
    val subcattwokey: String? = null,
    val brand_new_item: String? = null,
    val referenceId: String? = null,
    val homepageImage: String? = null,


){

    val createdOnFormated: String
        get() {
            createdOn?.let {
                val result: String = it.substring(0, createdOn.indexOf("."))

                return  HelpFunctions.FormatDateTime(
                    result,
                    HelpFunctions.datetimeformat_24hrs,
                    HelpFunctions.datetimeformat_mmddyyyy
                )
            }?:kotlin.run {
                return ""
            }

        }

}