package com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp

import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatDetails
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.TimeAuctionSelection
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.UserPointData
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetails

class AddProductObjectData {

    companion object {
        /**activity 1*/
        // List to store path of categories
        var subCategoryPath: ArrayList<String> = ArrayList()
        var selectedCategory: Category? = null
        var selectedCategoryId: Int = 0
        var selectedCategoryName: String = ""
        /**image and videos**/
        var videoList: ArrayList<String>? = null
        var images: ArrayList<ImageSelectModel>?= null
        /**Specification**/
        var productSpecificationList: List<DynamicSpecificationSentObject>? = null
        /**productDetails*/
        var itemTitleAr: String = ""
        var itemTitleEn: String = ""
        var subtitleAr: String = ""
        var subtitleEn: String = ""
        var itemDescriptionAr: String = ""
        var itemDescriptionEn: String = ""
        //**used =1 , new =2*/
        var productCondition: Int = 0
        var brand_new_item = ""
        var quantity: String = ""
        var country: SearchListItem? = null
        var region: SearchListItem? = null
        var city: SearchListItem? = null
        var phone = ""


        /**sale and pricing data*/
        var priceFixed: String = "0"
        var priceFixedOption: Boolean = false
        var auctionOption: Boolean = false
        var auctionStartPrice: String = "0"
        var auctionMinPrice: String = ""
        var isNegotiablePrice = false
        // 1 bank , 2 cash
        var PAYMENT_OPTION_CASH=1
        var PAYMENT_OPTION_BANk=2
        var PAYMENT_OPTION_MasterCard=3
        var PAYMENT_OPTION_Mada=4

        var imagesListRemoved :ArrayList<Int> ?=null
        var paymentOptionList: List<Int> ?=null
        var selectedAccountDetails: ArrayList<AccountDetails>? = null
        var selectTimeAuction: TimeAuctionSelection?= null
/**       MustPickUp = 1, NoPickUp = 2, PickUpAvailable=3*/
        var pickUpOption: Int = 0
        var shippingOption: Int = 0
        var shippingOptionSelection: List<Selection>? = null
        var shippingOptionSelections: ArrayList<Int>? = arrayListOf()
//        var pickOptionSelections: ArrayList<Int>? = arrayListOf()
        var selectedPakat: PakatDetails? = null

        var productId: Int?=null

        /*****/


//        //var phoneCountryCode = ""
//        var price: String = ""





//
//
//        var id = ""
//        var name = ""
//        var slug = ""
//        var tag = ""
//        var featureexpirydate = ""
//        var urgentexpirydate = ""
//        var highlightexpirydate = ""
//        var template = ""
//        val user = ""
//        var address = ""
//        var iscashpaid = false
//        var fixLength = ""
//        var timepicker = ""
//        var duration = ""
//        var endtime = ""
//        var fixlenghtselected: TimeAuctionSelection? =
//            null//        var selectPromotiion: PromotionModel? = null
//        var weekSelection: TimeAuctionSelection? = null
//
//
//        // To store path for model
//        var subcatone = ""
//        var subcattwo = ""
//        var subcatthree = ""
//        var subcatfour = ""
//        var subcatfive = ""
//        var subcatsix = ""
//        var subcatonekey = ""
//        var subcattwokey = ""
//        var subcatthreekey = ""
//        var subcatfourkey = ""
//        var subcatfivekey = ""
//        var subcatsixkey = ""
//        var category = ""

    }
}

class AccountObject {
    companion object {
        var walletDetails: WalletDetails? = null;
        var userPointData: UserPointData? = null
    }
}
