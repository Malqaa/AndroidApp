package com.malka.androidappp.network.service

import com.malka.androidappp.activities_main.signup_account.signup_pg1.CreateUserDataModel
import com.malka.androidappp.activities_main.login.LoginClass
import com.malka.androidappp.activities_main.login.LoginResponseBack
import com.malka.androidappp.activities_main.signup_account.signup_pg2.PostReqVerifyCode
import com.malka.androidappp.activities_main.signup_account.signup_pg2.ResendCodeDataModel
import com.malka.androidappp.activities_main.signup_account.signup_pg3.UpdateuserSignup
import com.malka.androidappp.botmnav_fragments.ForgotPassword.PostForgotpassModel
import com.malka.androidappp.botmnav_fragments.UserImageResponseBack
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.ModelBusinessRegistration
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.botmnav_fragments.cardetail_page.AdDetailModel
import com.malka.androidappp.botmnav_fragments.cardetail_page.ModelAddSellerFav
import com.malka.androidappp.botmnav_fragments.cardetail_page.ModelSellerDetails
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.getbidModel.ModelBidingResponse
import com.malka.androidappp.botmnav_fragments.cardetail_page.bottomsheet_bidopt.post_bidprice.ModelPostBidPrice
import com.malka.androidappp.botmnav_fragments.create_ads.*
import com.malka.androidappp.botmnav_fragments.create_product.CreateProductResponseBack
import com.malka.androidappp.botmnav_fragments.create_product.ModelCreateProduct
import com.malka.androidappp.botmnav_fragments.create_product.ProductResponseBack
import com.malka.androidappp.botmnav_fragments.feedback_frag.insert_feedback.GiveFeedbackResponseBack
import com.malka.androidappp.botmnav_fragments.feedback_frag.insert_feedback.ModelGiveFeedBack
import com.malka.androidappp.activities_main.forgot.forgot_changepass_reset_activity.PostChangePassApiModel
import com.malka.androidappp.activities_main.forgot.forgot_password.ForgotPassResponseModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.botmnav_fragments.home.model.CarTemplate
import com.malka.androidappp.botmnav_fragments.home_view_allcategories.ModelAddCatFav
import com.malka.androidappp.botmnav_fragments.my_product.AllProductsResponseBack
import com.malka.androidappp.botmnav_fragments.my_product.edit_product.EditProductResponseBack
import com.malka.androidappp.botmnav_fragments.my_product.edit_product.ModelEditProduct
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_ask_ques_api_edittext.ModelAskQues
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.ModelQuesAnswr
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_answer_api.ModelPostAns
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_comment_api_model.ModelPostComment
import com.malka.androidappp.botmnav_fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ModelShipAddresses
import com.malka.androidappp.botmnav_fragments.shoppingcart3_shippingaddress.shipping_addresslist.model_shipping.ShippingAddressessData
import com.malka.androidappp.botmnav_fragments.sold_business.ModelSoldUnsold
import com.malka.androidappp.botmnav_fragments.won_n_loss.model_wonloss.ModelWonLost
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.design.Models.BankListRespone
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.constants.ApiConstants.ADDBANK_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ADDRESS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ADD_TO_CART_DELETE_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ADD_TO_CART_INSERT_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ADD_TO_CART_USER_LIST_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ADVBYID_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ALL_PRODUCTS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ALL_USER_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.ASK_QUES_AD_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.CHANGEPASS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.CHECKOUT_INSERT_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.CREATE_BUSINESS_PRODUCT_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.CREATE_GENERAL_ADVERTISEMENT_ENDPOINTT
import com.malka.androidappp.network.constants.ApiConstants.CREATE_USER_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.DELETE_AD_WATCHLIST_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.DELETE_CREDIT_CARD_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.EDIT_PRODUCTS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.FAVOURITE_PARAMETER
import com.malka.androidappp.network.constants.ApiConstants.FEEDBACK_PARAMETER
import com.malka.androidappp.network.constants.ApiConstants.FORGOTPASS_EMAIL_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GETUSER_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_ADDRESS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_ALL_CATEGORIES
import com.malka.androidappp.network.constants.ApiConstants.GET_ALL_CATEGORIES_BY_ID
import com.malka.androidappp.network.constants.ApiConstants.GET_BANK__ACCOUNT_DETAIL
import com.malka.androidappp.network.constants.ApiConstants.GET_CATEGORY_LISTING_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_CATEGORY_TAGS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_QUES_ANS_COMMENT_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_SELLER_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_SOLDITEMS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_UNSOLDITEMS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_USER_CREDIT_CARD_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_WATCHLIST_LISTING_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GET_WONLOST_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.GIVE_FEEDBACK_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.HOME_TOTAL_NUMBERS_OF_MEMBERS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.HOME_TOTAL_ONLINEVISITS_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.INSERT_AD_WATCHLIST_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.INSERT_CREDIT_CARD_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.INSERT_FAVOURITE_CATEGORY_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.INSERT_FAVOURITE_SEARCH_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.INSERT_FAVOURITE_SELLER_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.LOGIN_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.POST_ANS_ONADD_QUESID_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.POST_BUSINESS_USER_REGISTRATION_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.POST_COMMENT_ONADD_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.POST_USER_IMAGE_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.PRODUCTBYID_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.RESEND_OTPCODE_API_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.SEARCH_CATEGORY_LISTING_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.UPDATEUSER_SIGNUP_ENDPOINT
import com.malka.androidappp.network.constants.ApiConstants.VERIFY_API_ENDPOINT
import com.malka.androidappp.servicemodels.BasicResponseInt
import com.malka.androidappp.servicemodels.Basicresponse
import com.malka.androidappp.servicemodels.CountryRespone
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
import com.malka.androidappp.servicemodels.home.*
import com.malka.androidappp.servicemodels.home.visitcount.visit_count_object
import com.malka.androidappp.servicemodels.total_members.ModelGetTotalMembers
import com.malka.androidappp.servicemodels.total_online_users.ModelGetTotalOnlineUsers
import com.malka.androidappp.servicemodels.user.UserObject
import com.malka.androidappp.servicemodels.watchlist.watchlistadd
import com.malka.androidappp.servicemodels.watchlist.watchlistobject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MalqaApiService {
    @get:GET(ALL_USER_ENDPOINT)
    val users: Call<CarTemplate?>?

    @POST(CREATE_USER_ENDPOINT)
    fun createuser(@Body registeruser: CreateUserDataModel): Call<ResponseBody>

    @POST(VERIFY_API_ENDPOINT)
    fun verifycode(@Body verifyusercode: PostReqVerifyCode): Call<PostReqVerifyCode>

    @POST(UPDATEUSER_SIGNUP_ENDPOINT)
    fun updateUserSiginup(@Body updateusersignupp: UpdateuserSignup): Call<UpdateuserSignup>

    @POST(RESEND_OTPCODE_API_ENDPOINT)
    fun resendcode(@Body resendotpcode: ResendCodeDataModel): Call<ResponseBody>



    @POST(CREATE_GENERAL_ADVERTISEMENT_ENDPOINTT)
    fun createAllAd(@Body data: HashMap<String, String>): Call<CreateAdvResponseBack>




    @POST(LOGIN_ENDPOINT)
    fun loginUser(@Body info: LoginClass): Call<LoginResponseBack?>?

    @POST(ADDRESS_ENDPOINT)
    fun insertAddress(@Body info: GetAddressResponse.AddressModel): Call<insertAddressResponseBack>

    @POST(ADDBANK_ENDPOINT)
    fun addbankaccount(@Body info: BankListRespone.BankDetail): Call<addBankAccountResponseBack>

    @GET(GET_ADDRESS_ENDPOINT)
    fun getAddress(@Query("loginId") loginId: String): Call<GetAddressResponse>

    @GET(GET_BANK__ACCOUNT_DETAIL)
    fun getBankDetail(@Query("UserID") loginId: String): Call<BankListRespone>

    @GET("Country/GetAllCountryFrmSql")
    fun getCountry(@Query("culture") culture: String): Call<CountryRespone>

    @GET("Country/GetRegionFrmSqlbyKey")
    fun getRegion(@Query("key") Id: String,@Query("culture") culture: String): Call<CountryRespone>


 @GET("Country/GetCityFrmSqlbyKey")
    fun getCity(@Query("key") Id: String,@Query("culture") culture: String): Call<CountryRespone>



    @POST(GET_CATEGORY_LISTING_ENDPOINT)
    fun categorylist(@Body creategeneralad: SearchRequestModel): Call<SearchRespone>;

    @GET(GETUSER_ENDPOINT + "?")
    fun getuser(@Query("id") userid: String): Call<UserObject>;

    @GET(GET_WATCHLIST_LISTING_ENDPOINT + "?")
    fun getUserWatchlist(@Query("loggedinUserId") userid: String): Call<watchlistobject>;

    @GET(FEEDBACK_PARAMETER + "?")
    fun getuserfeedback(@Query("loggedin") userid: String): Call<FeedbackObject>;

    @GET(FAVOURITE_PARAMETER + "?")
    fun getuserfavourites(@Query("loggedIn") userid: String): Call<FavouriteObject>;

    @GET(ADVBYID_ENDPOINT + "?")
    fun getAdDetailById(
        @Query("id") id: String,
        @Query("template") template: String
    ): Call<AdDetailModel>

    @GET(GET_SELLER_ENDPOINT + "?")
    fun getAdSeller(
        @Query("id") id: String,
        @Query("loggedin") loggedin: String
    ): Call<SellerResponseBack>

    @GET(GET_SELLER_ENDPOINT)
    fun getAdSellerByID(
        @Query("id") id: String
    ): Call<ModelSellerDetails>

    @POST(CREATE_GENERAL_ADVERTISEMENT_ENDPOINTT)
    fun createAds(@Body createad: ModelDataCreateGeneralAd): Call<ModelDataCreateGeneralAd>

    ////////////////////CREATE AUTOMOBILE AD/////////////////////
    @POST(CREATE_GENERAL_ADVERTISEMENT_ENDPOINTT)
    fun createAutoMobileAd(@Body createautoad: ModelDataCreateAutoMobile): Call<ModelDataCreateAutoMobile>

    @POST(FORGOTPASS_EMAIL_ENDPOINT)
    fun forgotpassemail(@Body forgottpass: PostForgotpassModel): Call<ForgotPassResponseModel>

    @POST(CHANGEPASS_ENDPOINT)
    fun changepass(@Body changepasspost: PostChangePassApiModel): Call<PostChangePassApiModel>

    @POST(INSERT_AD_WATCHLIST_ENDPOINT)
    fun InsertAdtoUserWatchlist(@Body insertads: watchlistadd): Call<Basicresponse>

    @POST(DELETE_AD_WATCHLIST_ENDPOINT + "?")
    fun DeleteAdFromUserWatchlist(
        @Query("userid") userid: String,
        @Query("adid") adsId: String
    ): Call<Basicresponse>

    @POST(POST_USER_IMAGE_ENDPOINT + "?")
    fun userimageupload(
        @Query("userId") userid: String,
        @Body imagebase64string: String
    ): Call<UserImageResponseBack>

    @POST("?")
    fun InsertToUserFavouritelist(@Body insertfav: favouriteadd): Call<Basicresponse>

    @POST("?")
    fun DeleteFromUserFavouritelist(
        @Query("sellerid") sellerid: String,
        @Query("category") category: String,
        @Query("query") query: String,
        @Query("userid") userid: String,
    ): Call<BasicResponseInt>

    @POST(ASK_QUES_AD_ENDPOINT)
    fun askQues(@Body askques: ModelAskQues): Call<ModelAskQues>

    @GET(GET_QUES_ANS_COMMENT_ENDPOINT + "?")
    fun quesAns(
        @Query("adid") qaadid: String,
        @Query("loggedin") qalogid: String
    ): Call<ModelQuesAnswr>

    @GET("$GET_CATEGORY_TAGS_ENDPOINT?")
    fun getCategoryTags(
        @Query("name") name: String
    ): Call<CategoryTagsModel>

    @GET(ALL_PRODUCTS_ENDPOINT + "?")
    fun getAllProducts(): Call<AllProductsResponseBack>

    @GET(GET_ALL_CATEGORIES)
    fun getAllCategories(): Call<AllCategoriesResponseBack>

    @GET("$GET_ALL_CATEGORIES_BY_ID?")
    fun getAllCategoriesByTemplateID(
        @Query("categoryKey") categoryParentId: String,
        @Query("culture") culture: String
    ): Call<AllCategoriesResponseBack>

    @POST(INSERT_FAVOURITE_SELLER_ENDPOINT)
    fun addSellerFav(@Body addfav: ModelAddSellerFav): Call<ModelAddSellerFav>

    @POST(INSERT_FAVOURITE_CATEGORY_ENDPOINT)
    fun addCatFav(@Body addfav: ModelAddCatFav): Call<ModelAddCatFav>

    @POST(INSERT_FAVOURITE_SEARCH_ENDPOINT)
    fun addSearchFav(@Body addfav: ModelAddSearchFav): Call<ModelAddSearchFav>

    @POST(POST_ANS_ONADD_QUESID_ENDPOINT + "?")
    fun postAnsByQid(
        @Query("qId") ansqid: String,
        @Query("answer") ansans: String,
        @Query("Loggedin") ansLoginId: String
    ): Call<ModelPostAns>


    @POST(POST_COMMENT_ONADD_ENDPOINT + "?")
    fun postCommentByQId(
        @Query("qId") comntqid: String,
        @Query("comment") comntcomnt: String,
        @Query("Loggedin") comntLoginId: String
    ): Call<ModelPostComment>

    @GET(ApiConstants.HOME_CLOSING_SOON_URL)
    fun GetClosingSoonAds(): Call<closingsoon>;

    @GET(ApiConstants.HOME_FEATURED_MOTORS_URL)
    fun GetFeaturedMotorsAds(): Call<favouritecars>;

    @GET(ApiConstants.HOME_FEATURED_PROPERTY_URL)
    fun GetFeaturedPropertyAds(): Call<favouriteproperties>;

    @GET(ApiConstants.HOME_RECENT_URL)
    fun GetRecentAds(): Call<recentlisting>;

    @GET(ApiConstants.HOME_GENERAL_ADS_URL)
    fun GetGeneralAds(): Call<generalads>;

    @GET(ApiConstants.HOME_ALL_ADS_URL)
    fun GetAllAds(): Call<GetAllAds>;

    @GET(ApiConstants.HOME_TOTAL_VISIT_COUNT)
    fun GetTotalVisitCount(): Call<visit_count_object>;

    @GET(GET_UNSOLDITEMS_ENDPOINT + "?")
    fun getunsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>

    @GET(GET_SOLDITEMS_ENDPOINT + "?")
    fun getsolditemsbyId(@Query("userid") userid: String): Call<ModelSoldUnsold>

    @POST(POST_BUSINESS_USER_REGISTRATION_ENDPOINT)
    fun busiRegis(@Body busiReg: ModelBusinessRegistration): Call<ModelBusinessRegistration>

    @GET(GET_WONLOST_ENDPOINT + "?")
    fun getWonLost(@Query("loginId") wonLogin: String): Call<ModelWonLost>

    @GET(HOME_TOTAL_ONLINEVISITS_ENDPOINT)
    fun GetTotalOnlineUers(): Call<ModelGetTotalOnlineUsers>

    @GET(HOME_TOTAL_NUMBERS_OF_MEMBERS_ENDPOINT)
    fun getTotalMembers(): Call<ModelGetTotalMembers>

    @GET(ApiConstants.GET_AD_DETAIL_BIDING_PRICE_ENDPOINT)
    fun getbidgpricebyAdvId(@Query("advId") getbidByadvId: String): Call<ModelBidingResponse>

    @POST(ApiConstants.POST_MAX_BIDING_PRICE)
    fun postBidPrice(@Body postBidpricee: ModelPostBidPrice): Call<ModelPostBidPrice>

    @GET(ApiConstants.GET_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun getshipaddress(@Query("loginId") getaddressbyLoginId: String): Call<ModelShipAddresses>

    @POST(ApiConstants.INSERT_BUYNOW_SHIPPINGADDRESS_ENDPOINT)
    fun AddNewShippingAddress(@Body shippingaddress: ShippingAddressessData): Call<Basicresponse>

    @GET(SEARCH_CATEGORY_LISTING_ENDPOINT + "?")
    fun searchcategorylist(@Query("query") category: String): Call<CategoryResponse>;

    @POST(CREATE_BUSINESS_PRODUCT_ENDPOINT)
    fun createBusinessProduct(@Body createproduct: ModelCreateProduct): Call<CreateProductResponseBack>

    @POST(GIVE_FEEDBACK_ENDPOINT)
    fun giveFeedback(@Body giveFeedback: ModelGiveFeedBack): Call<GiveFeedbackResponseBack>

    @POST(EDIT_PRODUCTS_ENDPOINT)
    fun editBusinessProduct(@Body editproduct: ModelEditProduct): Call<EditProductResponseBack>

    @GET(PRODUCTBYID_ENDPOINT + "?")
    fun getProductDetailById(
        @Query("id") id: String,
        @Query("loginUserId") loginUserId: String
    ): Call<ProductResponseBack>

    @GET(GET_USER_CREDIT_CARD_ENDPOINT + "?")
    fun GetUserCreditCards(@Query("usid") userid: String): Call<CreditCardResponse>;

    @POST(INSERT_CREDIT_CARD_ENDPOINT)
    fun InsertUserCreditCard(@Body insertads: CreditCardRequestModel): Call<Basicresponse>

    @DELETE(DELETE_CREDIT_CARD_ENDPOINT + "?")
    fun DeleteUserCreditCard(@Query("id") userid: String): Call<Basicresponse>

    @GET(ADD_TO_CART_USER_LIST_ENDPOINT + "?")
    fun GetUsersCartList(@Query("loggedinUserId") userid: String): Call<AddToCartResponseModel>;

    @POST(ADD_TO_CART_INSERT_ENDPOINT)
    fun AddToUserCart(@Body addtocart: InsertToCartRequestModel): Call<Basicresponse>

    @DELETE(ADD_TO_CART_DELETE_ENDPOINT + "?")
    fun DeleteFromUserCart(@Query("id") userid: String): Call<Basicresponse>

    @POST(CHECKOUT_INSERT_ENDPOINT)
    fun PostUserCheckOut(@Body checkout: CheckoutRequestModel): Call<Basicresponse>
}