package com.malka.androidappp.newPhase.domain.models.servicemodels

import android.widget.ImageView
import com.malka.androidappp.fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem

class ConstantObjects {
    companion object {
        val ENGLISH = "en"
        val ARABIC = "ar"

        var is_watch_iv: ImageView? = null

        val defaltCountry: Int = 31
        val isSuccess: String = "isSuccess";


        var userobj: User? = null;
        var userfeedback: FeedbackObject? = null;
        var userfavourite: FavouriteObject? = null;
        var userwatchlist = java.util.ArrayList<Product>()
        var usercreditcard: List<CreditCardModel>? = null;
        var usercart: List<CartItemModel> = ArrayList()
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
        const val questionListKey="QuestionListKey"
    }
}