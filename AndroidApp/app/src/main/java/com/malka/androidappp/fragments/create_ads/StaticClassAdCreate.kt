package com.malka.androidappp.fragments.create_ads

import com.malka.androidappp.newPhase.data.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ImageSelectModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.PromotionModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.domain.models.servicemodels.TimeSelection

class StaticClassAdCreate {

    companion object {
        var images: MutableList<ImageSelectModel> =  mutableListOf()
        var id = ""
        var name = ""
        var slug = ""
        var tag = ""
        var featureexpirydate = ""
        var urgentexpirydate = ""
        var highlightexpirydate = ""
        var producttitle = ""
        var template = ""

        var title = ""
        val user = ""
        var subtitle = ""
        var quantity = ""
        var phone = ""
        var item_description = ""
        var country: SearchListItem? = null
        var region: SearchListItem? = null
        var city: SearchListItem? = null
        var address = ""
        var reservedPrice = ""
        var startingPrice = ""


        var brand_new_item = ""
        var price = ""
        var iscashpaid = false
        var isvisapaid = false
        var isbankpaid = false
        var isnegotiable = false

        var fixLength = ""
        var timepicker = ""

        var duration = ""
        var endtime = ""
        var fixlenghtselected: TimeSelection? = null

        var listingType = ""

        var selectPromotiion: PromotionModel?=null
        var weekSelection: TimeSelection?=null
        var shippingOptionSelection: Selection?=null


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
