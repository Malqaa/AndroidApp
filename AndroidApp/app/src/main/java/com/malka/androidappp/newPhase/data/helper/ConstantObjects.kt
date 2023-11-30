package com.malka.androidappp.newPhase.data.helper

import android.widget.ImageView
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malka.androidappp.newPhase.domain.models.loginResp.BusinessAccountsDetails
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category

class ConstantObjects {
    companion object {
        const val transactionType_In = "In"
        const val transactionType_Out = "Out"
        const val transactionSource_chargeWallet = "ChargeWallet"
        const val emailKey: String = "emailKey"
        const val isMyProduct: String = "isMyProductKey"
        const val rateObjectKey: String = "rateObjectKey"
        const val editRateKey: String = "editRateKey"
        const val addressKey: String = "addressKey"
        const val orderNumberKey: String = "orderNumberKey"
        const val idKey: String = "idKey"
        const val objectKey: String = "objectKey"
        const val orderItemKey: String = "orderItemKey"
        const val orderTypeKey: String = "orderTypeKey"
        const val orderMasterIdKey: String = "orderMasterIdKey"
        const val shipmentOrderDataKey = "shipmentOrderDataKey"
        const val orderShippingSectionNumberKey: String = "orderShippingNumber"
        const val orderPriceKey: String = "orderPriceKey"
        const val sellerObjectKey: String = "sellerObjectKey"
        const val ENGLISH = "en"
        const val ARABIC = "ar"
        const val CAMERA = 0
        const val FILES = 1
        const val DELETEImage = 2
        var is_watch_iv: ImageView? = null

        val defaltCountry: Int = 31
        val isSuccess: String = "isSuccess";


        var userobj: LoginUser? = null;
        var userfeedback: FeedbackObject? = null;
        var userfavourite: FavouriteObject? = null;
        var userwatchlist = java.util.ArrayList<Product>()
        var usercreditcard: List<CreditCardModel>? = null;
        var usercart: List<CartItemModel> = ArrayList()
        var newUserCart: List<ProductCartItem> = ArrayList()
        var useraddresses: List<ShippingAddressessData>? = null
        var selected_address_index: Int = -1
        var selected_credit_card_index: Int = -1
        var logged_userid: String = ""
        var logged_authtoken: String = ""
        var businessAccountUser: BusinessAccountsDetails? = null
        var dynamic_json_dictionary: HashMap<String, String> = HashMap<String, String>()

        var currentLanguage: String = ""
        var categoryList: List<Category> = ArrayList()
        var categoryProductHomeList: ArrayList<CategoryProductItem> = java.util.ArrayList()
        var countryList: List<Country> = ArrayList()

        var isBid = "isBid"
        var isMyOrder = "isMyOrder"

        @JvmStatic
        var data = "data"

        var View = "View"
        var Select = "Select"
        const val searchQueryKey = "searchQueryKey"
        const val productIdKey = "ProductIdKey"
        const val providerIdKey = "providerIdKey"
        const val businessAccountIdKey = "businessAccountIdKey"
        const val questionListKey = "QuestionListKey"
        const val productFavStatusKey = "productFavStatusKey"
        const val categoryIdKey: String = "categoryid"
        const val categoryName: String = "categoryName"
        const val isEditKey: String = "isEdit"
         var acceptTerms: Boolean = false


//        const val orderStatus_provider_new = 1
//        const val orderStatus_provider_inProgress = 8
//        const val orderStatus_provider_inDelivery = 3
//        const val orderStatus_provider_finished = 5
//        const val orderStatus_provider_cancel = 9
//        const val orderStatus_client_cancel = 6

        const val WaitingForPayment = 1
        const val WaitingForReview = 2
        const val InProgress = 3
        const val ReadyForDelivery = 4
        const val DeliveryInProgress = 5
        const val Delivered = 6
        const val Canceled = 7
        const val Retrieved = 8


        const val Fixed_Price ="FixedPrice"
        const val Auction ="Auction"
        const val Negotiation ="Negotiation"

        /***configration**/
        const val configration_otpExpiryTime = "OTPExpiryTime"
        const val Configuration_DidNotReceiveCodeTime = "DidNotReceiveCodeTime"

        const val configration_resetPasswordExpiryTime = "ResetPasswordExpiryTime"
        const val configration_resetPasswordWrongTrials = "ResetPasswordWrongTrials"
        const val otpWrongTrials = "OTPWrongTrials"
        const val configration_OfferExpiredHours = "OfferExpiredHours"

        /**auctionClosingPeriodsUnit**/
        var auctionClosingPeriodsUnit_day = 1
        var auctionClosingPeriodsUnit_week = 2
        var auctionClosingPeriodsUnit_month = 3

        /**PickUpOption**/
        const val pickUp_Must = 1
        const val pickUp_No = 2
        const val pickUp_Available = 3

        /**shipping options*/
        const val shippingOption_integratedShippingCompanyOptions = 4
        const val shippingOption_freeShippingWithinSaudiArabia = 5
        const val shippingOption_arrangementWillBeMadeWithTheBuyer = 6

        /**SearchCategories**/
        const val search_categoriesDetails = 1
        const val search_seller = 2
        const val search_product = 3


    }
}