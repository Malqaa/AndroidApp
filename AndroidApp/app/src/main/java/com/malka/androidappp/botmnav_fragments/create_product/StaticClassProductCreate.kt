package com.malka.androidappp.botmnav_fragments.create_product

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.servicemodels.ConstantObjects
import org.joda.time.DateTime

class StaticClassProductCreate {
    companion object {

        var id = ""
        var userId = ConstantObjects.logged_userid
        var code = ""
        var sKU = ""
        var title = ""
        var subTitle = ""
        var brand = ""
        var barcodeGTIN = ""
        var images: MutableList<String>? = null
        var manufacturersCode = ""
        var stock = ""
        var listingDuration = ""
        var specifyEndTime = ""
        var description = ""
        var unwantedChristmasgift = ""
        var color = ""
        var size = ""
        var ddlbrand = ""
        var BuyNow = ""
        var startPrice = ""
        var specifyReserve = ""
        var fixedPriceOffer = ""
        var offerDuration = ""
        var OfferTo = ""
        var Length = ""
        var Width = ""
        var Height = ""
        var Weight = ""
        var extrasListingOffers = ""
        var activateAutoListing = ""
        var BuyNowAuction = ""
        var ShippingOptions = ""
        var PickUp = ""
        var onlyAllowBidsFromAuthenticatedMembers: Boolean? = false
        var isfixedPriceOffer: Boolean? = false
        var saudiBankDeposit: Boolean? = false
        var ping: Boolean? = false
        var cash: Boolean? = false
        var iDontknowTheShippingCostsYet: Boolean? = false
        var freeShippingWithinSaudia: Boolean? = false
        var useBookACourierShippingCosts: Boolean? = false
        var specifyShippingCosts: Boolean? = false
        var useShippingTemplate: Boolean? = false
        var UnlimitedStock: Boolean? = false
        var Paypal: Boolean? = true
        var ListingType = ""
        var CreatedDate = ""
        var updatedon = ""
        var updateby = ""
        var City = "Karachi"
        var Country = "Pakistan"
        var Slug = ""
        var Template = ""
        var Region = ""

    }
}