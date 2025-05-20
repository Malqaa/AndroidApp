package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBack
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductToCartResp
import com.malqaa.androidappp.newPhase.domain.models.addRateResp.AddRateResp
import com.malqaa.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonsResp
import com.malqaa.androidappp.newPhase.domain.models.bussinessAccountsListResp.ChangeBusinessAccountResp
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.BuyerRateResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malqaa.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malqaa.androidappp.newPhase.domain.models.questionsResp.QuestionsResp
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.CurrentUserRateResp
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateProductResponse
import com.malqaa.androidappp.newPhase.domain.models.sellerInfoResp.SellerInfoResp
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralRespone
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ProductDetailsViewModel : BaseViewModel() {

    var productDetailsObservable: MutableLiveData<ProductResp> = MutableLiveData()
    var addQuestionObservable: MutableLiveData<AddQuestionResp> = MutableLiveData()
    var getSimilarProductObservable: MutableLiveData<ProductListResp> = MutableLiveData()
    var getListOfQuestionsObservable: MutableLiveData<QuestionsResp> = MutableLiveData()
    var getRateResponseObservable: MutableLiveData<RateProductResponse> = MutableLiveData()
    var addRateRespObservable: MutableLiveData<AddRateResp> = MutableLiveData()
    var editRateRespObservable: MutableLiveData<AddRateResp> = MutableLiveData()
    var getCurrentUserRateObservable: MutableLiveData<CurrentUserRateResp> = MutableLiveData()
    var sellerRateListObservable: MutableLiveData<SellerRateListResp> = MutableLiveData()
    var addBuyerRateObservable: MutableLiveData<GeneralRespone> = MutableLiveData()
    var addDiscountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var getBuyerRateObservable: MutableLiveData<BuyerRateResp> = MutableLiveData()
    var addProductToCartObservable: MutableLiveData<AddProductToCartResp> = MutableLiveData()
    var sellerInfoObservable: MutableLiveData<SellerInfoResp> = MutableLiveData()
    var sellerInfoLoadingObservable: MutableLiveData<Boolean> = MutableLiveData()
    var shippingOptionObserver: MutableLiveData<ShippingOptionResp> = MutableLiveData()
    var paymentOptionObserver: MutableLiveData<ShippingOptionResp> = MutableLiveData()
    var bidsPersonsObserver: MutableLiveData<BidPersonsResp> = MutableLiveData()
    var getCartPrice: MutableLiveData<GeneralResponse> = MutableLiveData()
    var getMasterFromBuyNow: MutableLiveData<GeneralRespone> = MutableLiveData()
    var removeProductObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var sellerProductsRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var sellerLoading: MutableLiveData<Boolean> = MutableLiveData()
    var addSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var removeSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var changeBusinessAccountObserver: MutableLiveData<ChangeBusinessAccountResp> =
        MutableLiveData<ChangeBusinessAccountResp>()

    private var callChangeBusinessAccount: Call<ChangeBusinessAccountResp>? = null
    private var callRemoveProduct: Call<GeneralResponse>? = null
    private var callRemoveSellerToFav: Call<GeneralResponse>? = null
    private var callSellerListProduct: Call<ProductListResp>? = null
    private var callSellerListProductOp: Call<ShippingOptionResp>? = null
    private var callAddSellerToFav: Call<GeneralResponse>? = null
    private var callProductPaymentOp: Call<ShippingOptionResp>? = null
    private var callCartTotalPrice: Call<GeneralResponse>? = null
    private var callProductDetailsById: Call<ProductResp>? = null
    private var callBuyNowR: Call<GeneralRespone>? = null
    private var callBidsPersons: Call<BidPersonsResp>? = null
    private var callSellerInfo: Call<SellerInfoResp>? = null
    private var callAddQuestion: Call<AddQuestionResp>? = null
    private var callSimilarProduct: Call<ProductListResp>? = null
    private var callListOfQuestions: Call<QuestionsResp>? = null
    private var callListOfQuestionsActivity: Call<QuestionsResp>? = null
    private var callProductRatesActivity: Call<RateProductResponse>? = null
    private var callProductRates: Call<RateProductResponse>? = null
    private var callAddRateBuyer: Call<GeneralRespone>? = null
    private var callRateBuyer: Call<BuyerRateResp>? = null
    private var callAddRateProduct: Call<AddRateResp>? = null
    private var callEditRateProduct: Call<AddRateResp>? = null
    private var callCurrentUserRate: Call<CurrentUserRateResp>? = null
    private var callSellerRates: Call<SellerRateListResp>? = null
    private var callAddDiscount: Call<GeneralResponse>? = null
    private var callSellerRatesAsBuyer: Call<SellerRateListResp>? = null
    private var callSellerRatesAsSeller: Call<SellerRateListResp>? = null
    private var callAddSellerRate: Call<AddRateResp>? = null
    private var callAddProductToCart: Call<AddProductToCartResp>? = null

    private var answerQuestinoCallback: Call<AddQuestionResp>? = null
    fun closeAllCall() {
        if (callSellerListProduct != null) {
            callSellerListProduct?.cancel()
        }
        if (callRemoveProduct != null) {
            callRemoveProduct?.cancel()
        }
        if (callRemoveSellerToFav != null) {
            callRemoveSellerToFav?.cancel()
        }
        if (callSellerListProductOp != null) {
            callSellerListProductOp?.cancel()
        }
        if (callAddSellerToFav != null) {
            callAddSellerToFav?.cancel()
        }
        if (callProductPaymentOp != null) {
            callProductPaymentOp?.cancel()
        }
        if (callCartTotalPrice != null) {
            callCartTotalPrice?.cancel()
        }
        if (callProductDetailsById != null) {
            callProductDetailsById?.cancel()
        }
        if (callBuyNowR != null) {
            callBuyNowR?.cancel()
        }
        if (callBidsPersons != null) {
            callBidsPersons?.cancel()
        }
        if (callSellerInfo != null) {
            callSellerInfo?.cancel()
        }
        if (callAddQuestion != null) {
            callAddQuestion?.cancel()
        }
        if (callSimilarProduct != null) {
            callSimilarProduct?.cancel()
        }
        if (callListOfQuestions != null) {
            callListOfQuestions?.cancel()
        }
        if (callListOfQuestionsActivity != null) {
            callListOfQuestionsActivity?.cancel()
        }
        if (callProductRatesActivity != null) {
            callProductRatesActivity?.cancel()
        }
        if (callProductRates != null) {
            callProductRates?.cancel()
        }
        if (callAddRateBuyer != null) {
            callAddRateBuyer?.cancel()
        }
        if (callRateBuyer != null) {
            callRateBuyer?.cancel()
        }
        if (callAddRateProduct != null) {
            callAddRateProduct?.cancel()
        }
        if (callEditRateProduct != null) {
            callEditRateProduct?.cancel()
        }
        if (callCurrentUserRate != null) {
            callCurrentUserRate?.cancel()
        }
        if (callSellerRates != null) {
            callSellerRates?.cancel()
        }
        if (callAddDiscount != null) {
            callAddDiscount?.cancel()
        }
        if (callSellerRatesAsBuyer != null) {
            callSellerRatesAsBuyer?.cancel()
        }
        if (callSellerRatesAsSeller != null) {
            callSellerRatesAsSeller?.cancel()
        }
        if (callAddSellerRate != null) {
            callAddSellerRate?.cancel()
        }
        if (callAddProductToCart != null) {
            callAddProductToCart?.cancel()
        }
        if (answerQuestinoCallback != null) {
            answerQuestinoCallback?.cancel()
        }
    }


    fun addReplay(
        writeQuestion: RequestBody,
        id: RequestBody,
        context: Context,
        iReplay: ListenerCallBack
    ) {

        answerQuestinoCallback = getRetrofitBuilder().replayQuestion(
            writeQuestion,
            id
        )
        answerQuestinoCallback?.enqueue(object : Callback<AddQuestionResp> {
            override fun onFailure(call: Call<AddQuestionResp>, t: Throwable) {
                iReplay.callBackListener(true, null)
                if (t is HttpException) {
                    HelpFunctions.ShowLongToast(context.getString(R.string.serverError), context)

                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.connectionError),
                        context
                    )
                }
            }

            override fun onResponse(
                call: Call<AddQuestionResp>,
                response: Response<AddQuestionResp>
            ) {
                if (response.isSuccessful) {
                    iReplay.callBackListener(false, response.body())
                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.serverError),
                        context
                    )
                }
            }

        })
    }

    fun getProductShippingOptions(productId: Int) {
        //isLoading.value = true
        callSellerListProductOp = getRetrofitBuilder().getProductShippingOptions(productId)
        callApi(callSellerListProductOp!!,
            onSuccess = {
//                isLoading.value = false
//                isloadingMore.value = false
                shippingOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun changeBusinessAccount(id: Int) {
        isLoading.value = true
        callChangeBusinessAccount = getRetrofitBuilder().changeBusinessAccount(id)
        callApi(callChangeBusinessAccount!!,
            onSuccess = {
                isLoading.value = false
                changeBusinessAccountObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getProductPaymentOptions(productId: Int) {
        //isLoading.value = true
        callProductPaymentOp = getRetrofitBuilder().getProductPaymentOptions(productId)
        callApi(callProductPaymentOp!!,
            onSuccess = {
                paymentOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
            },
            goLogin = {
                sellerLoading.value = false
                needToLogin.value = true
            })

    }

    fun getCartTotalPrice() {
        callCartTotalPrice =
            getRetrofitBuilder().getCartTotalPrice(cartMasterId = SharedPreferencesStaticClass.getMasterCartId())
        callApi(callCartTotalPrice!!,
            onSuccess = {
//                isLoading.value = false
//                isloadingMore.value = false
                getCartPrice.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
            },
            goLogin = {
                sellerLoading.value = false
                needToLogin.value = true
            })
    }

    fun getProductDetailsById(productId: Int) {
        isLoading.value = true
        callProductDetailsById = getRetrofitBuilder().getProductDetailById2(productId)
        callApi(callProductDetailsById!!,
            onSuccess = {
                isLoading.value = false
                productDetailsObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                sellerLoading.value = false
                needToLogin.value = true
            })
    }

    fun callBuyNow(productId: Int) {
        isLoading.value = true
        callBuyNowR = getRetrofitBuilder().getBuyNow(productId)
        callApi(callBuyNowR!!,
            onSuccess = {
                isLoading.value = false
                getMasterFromBuyNow.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                sellerLoading.value = false
                needToLogin.value = true
            })
    }

    fun getBidsPersons(productId: Int) {
        callBidsPersons = getRetrofitBuilder().getBidsPersons(productId)
        callApi(callBidsPersons!!,
            onSuccess = {
                bidsPersonsObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getSellerInfo(productId: Int) {
        sellerInfoLoadingObservable.value = true
        callSellerInfo = getRetrofitBuilder().getSellerInformation(productId)
        callApi(callSellerInfo!!,
            onSuccess = {
                sellerInfoLoadingObservable.value = false
                sellerInfoObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerInfoLoadingObservable.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun addQuestion(productId: Int, question: String) {
        val data: HashMap<String, Any> = HashMap()
        data["Question"] = question
        data["ProductId"] = productId
        data["lang"] = ConstantObjects.currentLanguage
        isLoading.value = true

        callAddQuestion = getRetrofitBuilder().addQuestion(
            question.requestBody(),
            productId.toString().requestBody(),
            ConstantObjects.currentLanguage.requestBody()
        )
        callApi(callAddQuestion!!,
            onSuccess = {
                isLoading.value = false
                addQuestionObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getSimilarProduct(productId: Int, page: Int) {
        callSimilarProduct = getRetrofitBuilder().getSimilarProductForOtherProduct(page, productId)
        callApi(callSimilarProduct!!,
            onSuccess = {
                isLoading.value = false
                getSimilarProductObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun addLastViewedProduct(productId: Int) {
        getRetrofitBuilder()
            .addLastViewProduct(productId)
            .enqueue(object : Callback<GeneralRespone> {
                override fun onFailure(call: Call<GeneralRespone>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<GeneralRespone>,
                    response: Response<GeneralRespone>
                ) {
                    if (response.isSuccessful) {
                        //added product to last viewed
                    }
                }
            })
    }

    fun getListOfQuestions(productId: Int) {
        callListOfQuestions = getRetrofitBuilder().getQuestionList(productId)
        callApi(callListOfQuestions!!,
            onSuccess = {
                isLoading.value = false
                getListOfQuestionsObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun getListOfQuestionsForActivity(productId: Int) {
        isLoading.value = true
        callListOfQuestions = getRetrofitBuilder().getQuestionList(productId)
        callApi(callListOfQuestions!!,
            onSuccess = {
                isLoading.value = false
                getListOfQuestionsObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getProductRatesForActivity(productID: Int) {
        isLoading.value = true
        callProductRatesActivity = getRetrofitBuilder().getRates(productID)
        callApi(callProductRatesActivity!!,
            onSuccess = {
                isLoading.value = false
                getRateResponseObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getProductRatesForProductDetails(productID: Int) {
        callProductRates = getRetrofitBuilder().getRates(productID)
        callApi(callProductRates!!,
            onSuccess = {
//                isLoading.value = false
//                isloadingMore.value = false
                getRateResponseObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun addRateBuyer(orderId: Int, buyerRateId: Int, buyerId: String, rate: Int, comment: String) {
        isLoading.value = true
        val data: HashMap<String, Any> = HashMap()
        data["orderId"] = orderId
        data["buyerRateId"] = buyerRateId
        data["buyerId"] = buyerId
        data["rate"] = rate
        data["comment"] = comment

        callAddRateBuyer = getRetrofitBuilder().addRateBuyer(data)
        callApi(callAddRateBuyer!!,
            onSuccess = {
                isLoading.value = false
                addBuyerRateObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun getRateBuyer(orderId: Int) {
        isLoading.value = true

        callRateBuyer = getRetrofitBuilder().getRateBuyer(orderId)
        callApi(callRateBuyer!!,
            onSuccess = {
                isLoading.value = false
                getBuyerRateObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun addRateProduct(productID: Int, rate: Float, comment: String) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["productId"] = productID.toString().requestBody()
        map["comment"] = comment.requestBody()
        map["rate"] = rate.toString().requestBody()

        callAddRateProduct = getRetrofitBuilder().addRateProduct(map)
        callApi(callAddRateProduct!!,
            onSuccess = {
                isLoading.value = false
                addRateRespObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun editRateProduct(rateId: Int, rate: Float, comment: String) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["id"] = rateId.toString().requestBody()
        map["comment"] = comment.requestBody()
        map["rate"] = rate.toString().requestBody()
        callEditRateProduct = getRetrofitBuilder().editRateProduct(map)
        callApi(callEditRateProduct!!,
            onSuccess = {
                isLoading.value = false
                editRateRespObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun getCurrentUserRate(productId: Int) {
        isLoading.value = true

        callCurrentUserRate = getRetrofitBuilder().getCurrenUserRateForProdust(productId)
        callApi(callCurrentUserRate!!,
            onSuccess = {
                isLoading.value = false
                getCurrentUserRateObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun getSellerRates(providerId: String, businessAccountId: String?) {
        isLoading.value = true
        callSellerRates = getRetrofitBuilder().getSellerRates(providerId, businessAccountId)
        callApi(callSellerRates!!,
            onSuccess = {
                isLoading.value = false
                sellerRateListObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun addDiscount(productId: Int, discountPrice: Float, finaldate: String) {
        isLoading.value = true

        callAddDiscount = getRetrofitBuilder().addDiscount(productId, discountPrice, finaldate)
        callApi(callAddDiscount!!,
            onSuccess = {
                isLoading.value = false
                addDiscountObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }


    fun getSellerRates2AsSeller(
        providerId: String,
        businessAccountId: String?,
        page: Int,
        sendRate: Int?
    ) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore.value = true

        callSellerRatesAsSeller = getRetrofitBuilder().getSellerRates2AsAsseller(
            providerId,
            businessAccountId,
            page,
            sendRate
        )
        callApi(callSellerRatesAsSeller!!,
            onSuccess = {
                isLoading.value = false
                isloadingMore.value = false
                sellerRateListObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                isloadingMore.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun getSellerRates2AsAbuyer(
        providerId: String,
        businessAccountId: String?,
        page: Int,
        sendRate: Int?
    ) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore.value = true
        callSellerRatesAsBuyer = getRetrofitBuilder().getSellerRates2AsAsABuyer(
            providerId,
            businessAccountId,
            page,
            sendRate
        )
        callApi(callSellerRatesAsBuyer!!,
            onSuccess = {
                isLoading.value = false
                isloadingMore.value = false
                sellerRateListObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                isloadingMore.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun addSellerRate(
        providerId: String,
        businessAccountId: String,
        rating: Float,
        comment: String
    ) {
        isLoading.value = true
        val data: HashMap<String, Any> = HashMap()
        data["providerId"] = providerId
        data["businessAccountId"] = businessAccountId
        data["rate"] = rating
        data["comment"] = comment

        callAddSellerRate = getRetrofitBuilder().addRateSeller2(data)
        callApi(callAddSellerRate!!,
            onSuccess = {
                isLoading.value = false
                addRateRespObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun addProductToCart(masterCartId: String, productId: Int) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["productId"] = productId.toString().requestBody()
        map["CartMasterId"] = masterCartId.requestBody()
        map["quantity"] = "1".requestBody()

        callAddProductToCart = getRetrofitBuilder().addProductToCartProducts(map)
        callApi(callAddProductToCart!!,
            onSuccess = {
                isLoading.value = false
                addProductToCartObservable.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun getSellerListProduct(sellerProviderID: String, businessAccountId: String) {
        sellerLoading.value = true

        callSellerListProduct =
            getRetrofitBuilder().getListSellerProducts(1, sellerProviderID, businessAccountId)
        callApi(callSellerListProduct!!,
            onSuccess = {
                sellerLoading.value = false
                sellerProductsRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun addSellerToFav(sellerProviderID: String?, businessAccountId: String?) {
        sellerLoading.value = true

        callAddSellerToFav =
            getRetrofitBuilder().addFavoriteSeller(sellerProviderID, businessAccountId)
        callApi(callAddSellerToFav!!,
            onSuccess = {
                sellerLoading.value = false
                addSellerToFavObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })

    }

    fun removeSellerToFav(sellerProviderID: String?, businessAccountId: String?) {
        sellerLoading.value = true
        callRemoveSellerToFav =
            getRetrofitBuilder().addFavoriteSeller(sellerProviderID, businessAccountId)
        callApi(callRemoveSellerToFav!!,
            onSuccess = {
                sellerLoading.value = false
                removeSellerToFavObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun removeProduct(productId: Int) {
        sellerLoading.value = true
        callRemoveProduct = getRetrofitBuilder().removeProduct(productId)
        callApi(callRemoveProduct!!,
            onSuccess = {
                sellerLoading.value = false
                removeProductObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                sellerLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                needToLogin.value = true
            })
    }
}