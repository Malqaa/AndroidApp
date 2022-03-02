package com.malka.androidappp.network.Retrofit

import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder {
    companion object {
        private val httpClient = OkHttpClient.Builder()
        var builder: Retrofit.Builder? = null

        private fun GetRetrofitBuilder(Apiurl: String): Retrofit.Builder {
          //  if (builder == null) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val authToken: String = ConstantObjects.logged_authtoken
                val authenticationInterceptor = AuthenticationInterceptor(authToken);
                if (!httpClient.interceptors().contains(httpLoggingInterceptor)) {
                    httpClient.addInterceptor(httpLoggingInterceptor);
                }
                if (!httpClient.interceptors().contains(authenticationInterceptor)) {
                    httpClient.addInterceptor(authenticationInterceptor);
                }
                builder = Retrofit.Builder()
                builder!!.baseUrl(Apiurl).addConverterFactory(GsonConverterFactory.create())
                builder!!.client(httpClient.build());
         //   }
            return builder!!

        }

        fun createInstance(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetClosingSoonAds(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_CLOSING_SOON_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetFeaturedMotorsAds(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_FEATURED_MOTORS_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetFeaturedPropertyAds(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_FEATURED_PROPERTY_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetRecentAds(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_RECENT_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetGeneralAds(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_GENERAL_ADS_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetAllAds(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_ALL_ADS_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetTotalVisitCount(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_TOTAL_VISIT_COUNT).build()
                .create(MalqaApiService::class.java)
        }

        fun createAccountsInstance(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.ACCOUNT_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun createUserInstance(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_USER_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun verifyotpcodeInstance(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.VERIFY_API_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun resendcodeInstance(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.RESEND_OTPCODE_API_BASEURL).build()
                .create(MalqaApiService::class.java)
        }


        fun updaateuserSignup(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.UPDATEUSER_SIGNUP_BASEURL).build()
                .create(MalqaApiService::class.java)
        }


        fun createAd(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_GENERAL_ADVERTISEMENT_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getcategory(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_CATEGORY_LISTING).build()
                .create(MalqaApiService::class.java)
        }

        fun getUser(userid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_USER).build()
                .create(MalqaApiService::class.java)
        }

        fun getUserWatchlist(userid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_WATCHLIST_LISTING).build()
                .create(MalqaApiService::class.java)
        }

        fun getuserfeedback(userid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.USERFEEDBACK_ENDPOINT).build()
                .create(MalqaApiService::class.java)
        }

        fun getuserfavourites(userid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.USERFAVOURITE_ENDPOINT).build()
                .create(MalqaApiService::class.java)
        }

        fun getAdDetailById(adid: String, template: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.ADVBYID_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun getAdSeller(id: String, loggedin: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_SELLER_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun getAdSellerByID(id: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_SELLER_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun forgotPass(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.FORGOTPASS_EMAIL_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun changePass(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CHANGEPASS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun InsertAdtoUserWatchlist(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.INSERT_AD_WATCHLIST).build()
                .create(MalqaApiService::class.java)
        }

        fun DeleteAdFromUserWatchlist(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.DELETE_AD_WATCHLIST).build()
                .create(MalqaApiService::class.java)
        }

        fun InsertToUserFavouritelist(apiurl: String): MalqaApiService {
            return GetRetrofitBuilder(apiurl).build().create(MalqaApiService::class.java)
        }

        fun DeleteFromUserFavouritelist(apiurl: String): MalqaApiService {
            return GetRetrofitBuilder(apiurl).build().create(MalqaApiService::class.java)
        }

        fun askQuestion(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.ASK_QUES_AD_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getQuesAnsComnt(qaadid: String, qaloginid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_QUES_ANS_COMMENT_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getCategoryTags(category: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_CATEGORY_TAGS_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun getAllProducts(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_BUSINESS_PRODUCT_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getAllCategories(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_CATEGORY_TAGS_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun getAllCategoriesByTemplateID(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_CATEGORY_TAGS_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun addSellerToFav(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.USERFAVOURITE_ENDPOINT).build()
                .create(MalqaApiService::class.java)
        }

        fun addCatToFav(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.USERFAVOURITE_ENDPOINT).build()
                .create(MalqaApiService::class.java)
        }

        fun addSearchToFav(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.USERFAVOURITE_ENDPOINT).build()
                .create(MalqaApiService::class.java)
        }


        fun postAns(ansqid: String, ansans: String, ansLoginId: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.POST_ANS_ONADD_QUESID_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun postComment(comntqid: String, comcom: String, comntLoginId: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.POST_COMMENT_ONADD_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getunsolditembyId(userid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_UNSOLDITEMS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getSolditembyId(userid: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_SOLDITEMS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun busiReg(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.POST_BUSINESS_USER_REGISTRATION_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getWonLost(logginId: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_WONLOST_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getTotalOnlineUser(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_TOTAL_ONLINEVISITS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getTotalMembers(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.HOME_TOTAL_NUMBERS_OF_MEMBERS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun createpropertyAdwaqar(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_ADV_BYWAQAR_TEST_SERVER_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getBidingbyAdId(advId: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GETPOST_AD_DETAIL_BIDING_PRICE_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun postBidPrice(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GETPOST_AD_DETAIL_BIDING_PRICE_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getaddress(loginId: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_BUYNOW_SHIPPINGADDRESS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun searchcategorylist(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.SEARCH_CATEGORY_LISTING).build()
                .create(MalqaApiService::class.java)
        }

        fun createProduct(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_BUSINESS_PRODUCT_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun giveFeedback(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.USERFEEDBACK_ENDPOINT).build()
                .create(MalqaApiService::class.java)
        }

        fun editProduct(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_BUSINESS_PRODUCT_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun getProductDetailById(id: String, loginUserId: String): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CREATE_BUSINESS_PRODUCT_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun GetUserCreditCards(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_CREDIT_CARD).build()
                .create(MalqaApiService::class.java)
        }

        fun InsertUserCreditCard(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.INSERT_CREDIT_CARD).build()
                .create(MalqaApiService::class.java)
        }

        fun DeleteUserCreditCard(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.DELETE_CREDIT_CARD).build()
                .create(MalqaApiService::class.java)
        }

        fun GetUsersCartList(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.ADD_TO_CART_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun AddToUserCart(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.ADD_TO_CART_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun DeleteFromUserCart(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.ADD_TO_CART_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun PostUserCheckOut(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.CHECKOUT_BASE_URL).build()
                .create(MalqaApiService::class.java)
        }

        fun AddNewShippingAddress(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.GET_BUYNOW_SHIPPINGADDRESS_BASEURL).build()
                .create(MalqaApiService::class.java)
        }

        fun userimageupload(): MalqaApiService {
            return GetRetrofitBuilder(ApiConstants.POST_USER_IMAGE).build()
                .create(MalqaApiService::class.java)
        }

    }
}