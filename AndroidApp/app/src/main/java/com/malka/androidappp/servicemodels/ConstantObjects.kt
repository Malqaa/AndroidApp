package com.malka.androidappp.servicemodels

import android.widget.ImageView
import com.malka.androidappp.activities_main.signup_account.signup_pg3.User
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.DynamicList
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.servicemodels.watchlist.watchlistobject

class ConstantObjects {
    companion object {
        var is_watch_iv: ImageView?=null

        val defaltCountry: String = "KSA-en-US";
        val isSuccess: String = "isSuccess";


        var userobj: User? = null;
        var userfeedback: FeedbackObject? = null;
        var userfavourite: FavouriteObject? = null;
        var userwatchlist: watchlistobject? = null;
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
        var categoryList: List<AllCategoriesModel> = ArrayList()
        val list: ArrayList<DynamicList> = java.util.ArrayList()
        var countryList: List<Country> = ArrayList()

        var isBid= "isBid"
        var isMyOrder= "isMyOrder"

        @JvmStatic
        var data= "data"

        var View= "View"
        var Select= "Select"

    }
}