package com.malka.androidappp.botmnav_fragments.create_ads

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.design.Models.PromotionModel
import kotlinx.android.parcel.Parcelize

class StaticClassAdCreate {

    companion object {
        var images: MutableList<String>? = null
        var id = ""
        var name = ""
        var slug = ""
        var tag = ""
        var featureexpirydate = ""
        var urgentexpirydate = ""
        var highlightexpirydate = ""
        var producttitle = ""
        var template = ""
        var iscontactphone: Boolean? = false
        var iscontactemail: Boolean? = false
        var iscontactchat: Boolean? = false
        var title = ""
        val user = ""
        var subtitle = ""
        var quantity = ""
        var phone = ""
        var item_description = ""
        var country = ""
        var region = ""
        var city = ""
        var address = ""
        var description = ""
        var reservedPrice = ""
        var startingPrice = ""


        //        var brand_new_item: Boolean? = false
        var brand_new_item = ""
        var price = ""
        var iscashpaid = ""
        var isvisaPaid = ""
        var isbankpaid = ""

        var fixLength = ""
        var timepicker = ""

        var duration = ""
        var endtime = ""

        var listingType = ""

        @SerializedName("pickup-option")
        var pickup_option = ""

        @SerializedName("shipping-option")
        var shipping_option = ""

        @SerializedName("cost-amount")
        var cost_amount = ""

        @SerializedName("cost-desc")
        var cost_desc = ""
        var pack4 = ""
        var selectPromotiion: PromotionModel?=null
        var iswatching: Boolean? = false
        var selectasmain = ""
        var isnogotiable: Boolean? = false




        /////////////////////////Property Additional Fields////////////////////
        var floorarea = ""
        var bedrooms = ""
        var bathrooms = ""
        var landarea = ""
        var floorsnumber = ""
        var isfeatured: Boolean? = false
        var isphonehidden: Boolean? = false

        // Bus
        var numberOfPassengers = ""

        //Bike
        var style = ""

        //for property selection subcategory of subcategory
        var proertySubCatA = ""
        var propertySubCatB = ""

        // To Save main category
        var mainCategory = ""

        var platform = ""

        // List to store path of categories
        var subCategoryPath: ArrayList<String> = ArrayList()

        // To store path for model
        var subcatone = ""
        var subcattwo = ""
        var subcatthree = ""
        var subcatfour = ""
        var subcatfive = ""
        var subcatsix = ""
        var subcatonekey = ""
        var subcattwokey = ""
        var subcatthreekey = ""
        var subcatfourkey = ""
        var subcatfivekey = ""
        var subcatsixkey = ""
        var category = ""
        var video = ""
    }
}
