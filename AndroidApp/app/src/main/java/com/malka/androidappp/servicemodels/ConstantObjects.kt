package com.malka.androidappp.servicemodels

import android.widget.ImageView
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardResponseModel
import com.malka.androidappp.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.servicemodels.user.UserObject
import com.malka.androidappp.servicemodels.watchlist.watchlistobject

class ConstantObjects {
    companion object {
        var is_watch_iv: ImageView?=null

        val defaltCountry: String = "KSA-en-US";
        val isSuccess: String = "isSuccess";


        var userobj: UserObject? = null;
        var userfeedback: FeedbackObject? = null;
        var userfavourite: FavouriteObject? = null;
        var userwatchlist: watchlistobject? = null;
        var usercreditcard: List<CreditCardResponseModel>? = null;
        var usercart: List<CartItemModel>? = null;
        var useraddresses: List<ShippingAddressessData>? = null
        var selected_address_index: Int = -1
        var selected_credit_card_index: Int = -1
        var logged_userid: String = ""
        var logged_authtoken: String = ""
        var isBusinessUser: Boolean = false
        var dynamic_json_dictionary: HashMap<String, String> = HashMap<String, String>()

        var currentLanguage: String = ""
        var categoryList: List<AllCategoriesModel> = ArrayList()
        var countryList: List<CountryRespone.Country> = ArrayList()


        @JvmStatic
        var data= "data"

    }
}