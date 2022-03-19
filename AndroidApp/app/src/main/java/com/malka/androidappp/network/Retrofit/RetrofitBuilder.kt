package com.malka.androidappp.network.Retrofit

import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder {
    companion object {
        private val httpClient = OkHttpClient.Builder()
        var builder: Retrofit.Builder? = null

        fun GetRetrofitBuilder(): Retrofit.Builder {
            if (builder == null) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val authenticationInterceptor = AuthenticationInterceptor();
                httpClient.addInterceptor(httpLoggingInterceptor);

                httpClient.addInterceptor(authenticationInterceptor);

                builder = Retrofit.Builder()
                builder!!.baseUrl(ApiConstants.API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                builder!!.client(httpClient.build());
            }
            return builder!!

        }

        fun GetRetrofitBuilder2(): MalqaApiService {
            if (builder == null) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val authenticationInterceptor = AuthenticationInterceptor();
                httpClient.addInterceptor(httpLoggingInterceptor);

                httpClient.addInterceptor(authenticationInterceptor);

                builder = Retrofit.Builder()
                builder!!.baseUrl(ApiConstants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                builder!!.client(httpClient.build());
            }
            return builder!!.build().create(MalqaApiService::class.java)

        }

        fun createInstance(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetClosingSoonAds(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetFeaturedMotorsAds(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetFeaturedPropertyAds(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetRecentAds(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetGeneralAds(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetAllAds(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetTotalVisitCount(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun createAccountsInstance(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun createUserInstance(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun addBankAccountInstance(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun verifyotpcodeInstance(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun resendcodeInstance(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }


        fun updaateuserSignup(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }




        fun getcategory(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getUser(userid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getUserWatchlist(userid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getuserfeedback(userid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getuserfavourites(userid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }



        fun getAdSeller(id: String, loggedin: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getAdSellerByID(id: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }



        fun InsertAdtoUserWatchlist(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun DeleteAdFromUserWatchlist(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun InsertToUserFavouritelist(apiurl: String): MalqaApiService {
            return GetRetrofitBuilder().build().create(MalqaApiService::class.java)
        }

        fun DeleteFromUserFavouritelist(apiurl: String): MalqaApiService {
            return GetRetrofitBuilder().build().create(MalqaApiService::class.java)
        }

        fun askQuestion(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getQuesAnsComnt(qaadid: String, qaloginid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getCategoryTags(category: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getAllProducts(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getAllCategories(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getAllCategoriesByTemplateID(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun addSellerToFav(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun addCatToFav(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun addSearchToFav(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }


        fun postAns(ansqid: String, ansans: String, ansLoginId: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun postComment(comntqid: String, comcom: String, comntLoginId: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getunsolditembyId(userid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getSolditembyId(userid: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun busiReg(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getWonLost(logginId: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getTotalOnlineUser(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getTotalMembers(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun createpropertyAdwaqar(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getBidingbyAdId(advId: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun postBidPrice(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getaddress(loginId: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun searchcategorylist(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun createProduct(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun giveFeedback(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun editProduct(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun getProductDetailById(id: String, loginUserId: String): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetUserCreditCards(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun InsertUserCreditCard(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun DeleteUserCreditCard(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun GetUsersCartList(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun AddToUserCart(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun DeleteFromUserCart(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun PostUserCheckOut(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun AddNewShippingAddress(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }

        fun userimageupload(): MalqaApiService {
            return GetRetrofitBuilder().build()
                .create(MalqaApiService::class.java)
        }


    }
}