package com.malka.androidappp.network.service

import com.google.gson.JsonObject
import com.malka.androidappp.activities_main.login.LoginClass
import com.malka.androidappp.activities_main.login.LoginResponseBack
import com.malka.androidappp.activities_main.signup_account.signup_pg2.PostReqVerifyCode
import com.malka.androidappp.activities_main.signup_account.signup_pg3.User
import com.malka.androidappp.botmnav_fragments.UserImageResponseBack
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.ModelBusinessRegistration
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.botmnav_fragments.cardetail_page.ModelAddSellerFav
import com.malka.androidappp.botmnav_fragments.cardetail_page.ModelSellerDetails
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.getbidModel.ModelBidingResponse
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.post_bidprice.ModelPostBidPrice
import com.malka.androidappp.botmnav_fragments.create_product.CreateProductResponseBack
import com.malka.androidappp.botmnav_fragments.create_product.ModelCreateProduct
import com.malka.androidappp.botmnav_fragments.create_product.ProductResponseBack
import com.malka.androidappp.botmnav_fragments.feedback_frag.insert_feedback.GiveFeedbackResponseBack
import com.malka.androidappp.botmnav_fragments.feedback_frag.insert_feedback.ModelGiveFeedBack
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.botmnav_fragments.home_view_allcategories.ModelAddCatFav
import com.malka.androidappp.botmnav_fragments.my_product.AllProductsResponseBack
import com.malka.androidappp.botmnav_fragments.my_product.edit_product.EditProductResponseBack
import com.malka.androidappp.botmnav_fragments.my_product.edit_product.ModelEditProduct
import com.malka.androidappp.botmnav_fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.botmnav_fragments.won_n_loss.model_wonloss.ModelWonLost
import com.malka.androidappp.design.Models.BankListRespone
import com.malka.androidappp.design.Models.BusinessUserModel
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.design.Models.getBusinessRegisterFile
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.constants.ApiConstants.GET_ALL_CATEGORIES_BY_ID
import com.malka.androidappp.network.constants.ApiConstants.GET_CATEGORY_TAGS_ENDPOINT
import com.malka.androidappp.servicemodels.*
import com.malka.androidappp.servicemodels.addtocart.AddToCartResponseModel
import com.malka.androidappp.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.servicemodels.categorylistings.CategoryResponse
import com.malka.androidappp.servicemodels.categorylistings.SearchRequestModel
import com.malka.androidappp.servicemodels.categorylistings.SearchRespone
import com.malka.androidappp.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardRequestModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardResponse
import com.malka.androidappp.servicemodels.favourites.FavouriteObject
import com.malka.androidappp.servicemodels.favourites.favouriteadd
import com.malka.androidappp.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.servicemodels.home.GetAllAds
import com.malka.androidappp.servicemodels.home.visitcount.visit_count_object
import com.malka.androidappp.servicemodels.questionModel.ModelAskQues
import com.malka.androidappp.servicemodels.questionModel.ModelPostAns
import com.malka.androidappp.servicemodels.questionModel.ModelQuesAnswr
import com.malka.androidappp.servicemodels.total_members.ModelGetTotalMembers
import com.malka.androidappp.servicemodels.total_online_users.ModelGetTotalOnlineUsers
import com.malka.androidappp.servicemodels.user.UserObject
import com.malka.androidappp.servicemodels.watchlist.watchlistadd
import com.malka.androidappp.servicemodels.watchlist.watchlistobject
import retrofit2.Call
import retrofit2.http.*


interface MalqaApiService {

    @POST("Accounts/signUp")
    fun createuser(@Body registeruser: User): Call<GeneralRespone>

    @POST("Accounts/verify")
    fun verifycode(@Body verifyusercode: PostReqVerifyCode): Call<BasicResponse>

    @POST("Accounts/updatePersonalInfo")
    fun updateUserSiginup(@Body updateusersignupp: User): Call<User>

    @POST("Accounts/ResendCode")
    fun resendcode(@Body resendotpcode: User): Call<BasicResponse>


    @POST("CarTemplate/Create")
    fun createAllAd(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>


    @POST("CarTemplate/update")
    fun updateCarTemplate(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>


    @POST("Accounts/login")
    fun loginUser(@Body info: LoginClass): Call<LoginResponseBack?>?

    @POST("Accounts/insertaddress")
    fun insertAddress(@Body info: GetAddressResponse.AddressModel): Call<GeneralRespone>
    @POST("Accounts/updateAdress")
    fun updateAddress(@Body info: GetAddressResponse.AddressModel): Call<GeneralRespone>

    @POST("Accounts/CreateBusinessAccount")
    fun addBusinesUser(@Body info: BusinessUserModel.getBusinessList): Call<GeneralRespone>

    @POST("Accounts/UploadBusinessDocuments")
    fun addBusinessRegisterFile(@Body info: getBusinessRegisterFile.GetDocuments): Call<GeneralRespone>

    @POST("UserBankAccount/AddUserBankAccount")
    fun addbankaccount(@Body info: BankListRespone.BankDetail): Call<GeneralRespone>

    @GET("Accounts/getaddresses")
    fun getAddress(@Query("loginId") loginId: String): Call<GetAddressResponse>

    @GET("UserBankAccount/UserBankAccountsById")
    fun getBankDetail(@Query("UserID") loginId: String): Call<BankListRespone>

    @GET("Accounts/GetBusinessListByUserId")
    fun getBusinessUserList(@Query("UserID") loginId: String): Call<BusinessUserModel>

    @GET("Country/GetAllCountryFrmSql")
    fun getCountry(@Query("culture") culture: String): Call<CountryRespone>

    @GET("Country/GetRegionFrmSqlbyKey")
    fun getRegion(@Query("key") Id: String, @Query("culture") culture: String): Call<CountryRespone>


    @GET("Country/GetCityFrmSqlbyKey")
    fun getCity(@Query("key") Id: String, @Query("culture") culture: String): Call<CountryRespone>


    @POST("SearchFilter/generalsearchfilters")
    fun categorylist(@Body creategeneralad: SearchRequestModel): Call<SearchRespone>;

    @GET("Accounts/GetUser")
    fun getuser(@Query("id") userid: String): Call<UserObject>;

    @GET("Watchlist/getall")
    fun getUserWatchlist(@Query("loggedinUserId") userid: String): Call<watchlistobject>;

    @GET("Auction/getall")
    fun getuserfeedback(@Query("loggedin") userid: String): Call<FeedbackObject>;

    @GET("all")
    fun getuserfavourites(@Query("loggedIn") userid: String): Call<FavouriteObject>;

    @GET("CarTemplate/Details")
    fun getAdDetailById(
        @Query("id") id: String,
        @Query("template") template: String,
        @Query("loggedin") loggedin: String
    ): Call<JsonObject>

    @GET("Accounts/GetUser")
    fun getAdSeller(
        @Query("id") id: String,
        @Query("loggedin") loggedin: String
    ): Call<SellerResponseBack>

    @GET("Accounts/GetUser")
    fun getAdSellerByID(
        @Query("id") id: String
    ): Call<ModelSellerDetails>


    @POST("Accounts/ForgetEmailRequest")
    fun forgotpassemail(@Body forgottpass: User): Call<GeneralRespone>

    @POST("Accounts/ChangePassword")
    fun changepass(@Body changepasspost: User): Call<User>

    @POST("Watchlist/insert")
    fun InsertAdtoUserWatchlist(@Body insertads: watchlistadd): Call<BasicResponse>

    @POST("Watchlist/delete")
    fun DeleteAdFromUserWatchlist(
        @Query("userid") userid: String,
        @Query("adid") adsId: String
    ): Call<BasicResponse>

    @POST("Accounts/userimageupload")
    fun userimageupload(
        @Query("userId") userid: String,
        @Body imagebase64string: String
    ): Call<UserImageResponseBack>

    @POST("?")
    fun InsertToUserFavouritelist(@Body insertfav: favouriteadd): Call<BasicResponse>

    @POST("?")
    fun DeleteFromUserFavouritelist(
        @Query("sellerid") sellerid: String,
        @Query("category") category: String,
        @Query("query") query: String,
        @Query("userid") userid: String,
    ): Call<BasicResponseInt>

    @POST("AdvQueAndAns/create")
    fun askQues(@Body askques: ModelAskQues): Call<ModelAskQues>

    @GET("CarTemplate/GetQuestionByAd")
    fun quesAns(
        @Query("adid") qaadid: String,
        @Query("loggedin") qalogid: String
    ): Call<ModelQuesAnswr>

    @GET("$GET_CATEGORY_TAGS_ENDPOINT?")
    fun getCategoryTags(
        @Query("name") name: String
    ): Call<CategoryTagsModel>

    @GET("BussinessProduct/getall")
    fun getAllProducts(): Call<AllProductsResponseBack>

    @GET("Category/GetAllCategoryByCulture")
    fun getAllCategories(
        @Query("culture") culture: String,
    ): Call<AllCategoriesResponseBack>

    @GET("$GET_ALL_CATEGORIES_BY_ID?")
    fun getAllCategoriesByTemplateID(
        @Query("categoryKey") categoryParentId: String,
        @Query("culture") culture: String
    ): Call<AllCategoriesResponseBack>

    @POST("Favourite/insertseller")
    fun addSellerFav(@Body addfav: ModelAddSellerFav): Call<ModelAddSellerFav>

    @POST("Favourite/insertcategory")
    fun addCatFav(@Body addfav: ModelAddCatFav): Call<ModelAddCatFav>

    @POST("Favourite/insertsearch")
    fun addSearchFav(@Body addfav: ModelAddSearchFav): Call<ModelAddSearchFav>

    @POST("AdvQueAndAns/answer")
    fun postAnsByQid(
        @Query("qId") ansqid: String,
        @Query("answer") ansans: String,
        @Query("Loggedin") ansLoginId: String
    ): Call<ModelPostAns>


    @GET(ApiConstants.HOME_ALL_ADS_URL)
    fun GetAllAds(@Query("loginId") userid: String): Call<GetAllAds>;

    @GET(ApiConstants.HOME_TOTAL_VISIT_COUNT)
    fun GetTotalVisitCount(): Call<visit_count_object>;

    @GET("Accounts/soldunsolditems")
    fun getunsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>

    @GET(
        "Accounts/soldunsolditems"
    )
    fun getsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>

    @POST("BusinessUser/Insertbusinessuser")
    fun busiRegis(@Body busiReg: ModelBusinessRegistration): Call<ModelBusinessRegistration>

    @GET("Bid/UserWonNLostList")
    fun getWonLost(@Query("loginId") wonLogin: String): Call<ModelWonLost>

    @GET("GetAllOnlineUsers")
    fun GetTotalOnlineUers(): Call<ModelGetTotalOnlineUsers>

    @GET("Accounts/usercount")
    fun getTotalMembers(): Call<ModelGetTotalMembers>

    @GET(ApiConstants.GET_AD_DETAIL_BIDING_PRICE_ENDPOINT)
    fun getbidgpricebyAdvId(@Query("advId") getbidByadvId: String): Call<ModelBidingResponse>

    @POST(ApiConstants.POST_MAX_BIDING_PRICE)
    fun postBidPrice(@Body postBidpricee: ModelPostBidPrice): Call<ModelPostBidPrice>

    @GET(ApiConstants.GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun getshipaddress(@Query("loginId") getaddressbyLoginId: String): Call<ModelShipAddresses>

    @POST(ApiConstants.INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun AddNewShippingAddress(@Body shippingaddress: ShippingAddressessData): Call<BasicResponse>

    @GET("CarTemplate/Search")
    fun searchcategorylist(@Query("query") category: String): Call<CategoryResponse>;

    @POST("BussinessProduct/createproduct")
    fun createBusinessProduct(@Body createproduct: ModelCreateProduct): Call<CreateProductResponseBack>

    @POST("Auction/InsertFeedback")
    fun giveFeedback(@Body giveFeedback: ModelGiveFeedBack): Call<GiveFeedbackResponseBack>

    @POST("BussinessProduct/editproduct")
    fun editBusinessProduct(@Body editproduct: ModelEditProduct): Call<EditProductResponseBack>

    @GET("BussinessProduct/detailsofproduct")
    fun getProductDetailById(
        @Query("id") id: String,
        @Query("loginUserId") loginUserId: String
    ): Call<ProductResponseBack>

    @GET("CardDetail/getbyuserid")
    fun GetUserCreditCards(@Query("usid") userid: String): Call<CreditCardResponse>;

    @POST("CardDetail/insertcard")
    fun InsertUserCreditCard(@Body insertads: CreditCardRequestModel): Call<BasicResponse>

    @POST("CardDetail/updatecard")
    fun UpdateUserCreditCard(@Body insertads: CreditCardRequestModel): Call<BasicResponse>

    @DELETE("CardDetail/deletecard")
    fun DeleteUserCreditCard(@Query("id") userid: String): Call<BasicResponse>

    @GET("AddToCart/getbyloginuserid")
    fun GetUsersCartList(@Query("loggedinUserId") userid: String): Call<AddToCartResponseModel>;

    @POST("AddToCart/create")
    fun AddToUserCart(@Body addtocart: InsertToCartRequestModel): Call<BasicResponse>

    @GET("AddToCart/getall")
    fun getMyRequest(@Query("loginId") loginId: String): Call<getCartModel>

    @DELETE("AddToCart/delete")
    fun DeleteFromUserCart(@Query("id") userid: String): Call<BasicResponse>

    @POST("Checkout/checkoutiteminsert")
    fun PostUserCheckOut(@Body checkout: CheckoutRequestModel): Call<BasicResponse>

    @GET()
    fun jsonTemplates(@Url url: String): Call<JsonObject>


}