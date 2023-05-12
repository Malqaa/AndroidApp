package com.malka.androidappp.newPhase.data.helper

import android.widget.ImageView
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.newPhase.domain.models.cartListResp.ProductCartItem
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.User

class ConstantObjects {
    companion object {
        const val  transactionType_In="In"
        const val  transactionType_Out="Out"
        const val transactionSource_chargeWallet="ChargeWallet"

        const val isMyProduct: String = "isMyProductKey"
        const val rateObjectKey: String = "rateObjectKey"
        const val editRateKey: String = "editRateKey"
        const val  addressKey: String = "addressKey"
        const val  orderNumberKey: String = "orderNumberKey"
        const val idKey:String="idKey"
        const val objectKey:String="objectKey"
        const val orderItemKey:String="orderItemKey"
        const val orderTypeKey:String="orderTypeKey"
        const val orderMasterIdKey:String="orderMasterIdKey"
        const val shipmentOrderDataKey="shipmentOrderDataKey"
        const val  orderShippingSectionNumberKey: String = "orderShippingNumber"
        const val  orderPriceKey: String = "orderPriceKey"
        const val sellerObjectKey:String="sellerObjectKey"
        const val ENGLISH = "en"
        const val ARABIC = "ar"

        var is_watch_iv: ImageView? = null

        val defaltCountry: Int = 31
        val isSuccess: String = "isSuccess";


        var userobj: User? = null;
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
        var isBusinessUser: Boolean = false
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
        const val providerIdKey="providerIdKey"
        const val businessAccountIdKey="businessAccountIdKey"
        const val questionListKey = "QuestionListKey"
        const val productFavStatusKey = "productFavStatusKey"
        const val categoryIdKey: String = "categoryid"
        const val categoryName: String = "categoryName"
        const val isEditKey: String = "isEdit"


        const val orderStatus_provider_new=1
        const val orderStatus_provider_inProgress=8
        const val orderStatus_provider_inDelivery=3
        const val orderStatus_provider_finished=5
        const val orderStatus_provider_cancel=9
        const val orderStatus_client_cancel=6
    }
}