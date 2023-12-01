package com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addProductToCartResp.AddProductToCartResp
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateResp
import com.malka.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonsResp
import com.malka.androidappp.newPhase.domain.models.orderRateResp.BuyerRateResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malka.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malka.androidappp.newPhase.domain.models.questionsResp.QuestionsResp
import com.malka.androidappp.newPhase.domain.models.ratingResp.CurrentUserRateResp
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateProductResponse
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateResponse
import com.malka.androidappp.newPhase.domain.models.sellerInfoResp.SellerInfoResp
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralRespone
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Query

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

    fun getProductShippingOptions(productId: Int) {
        //isLoading.value = true
        getRetrofitBuilder()
            .getProductShippingOptions(productId)
            .enqueue(object : Callback<ShippingOptionResp> {
                override fun onFailure(call: Call<ShippingOptionResp>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ShippingOptionResp>,
                    response: Response<ShippingOptionResp>
                ) {
                    // isLoading.value = false
                    if (response.isSuccessful) {
                        shippingOptionObserver.value = response.body()
                    }
//                    else {
//                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
//                    }
                }
            })
    }

    fun getProductPaymentOptions(productId: Int) {
        //isLoading.value = true
        getRetrofitBuilder()
            .getProductPaymentOptions(productId)
            .enqueue(object : Callback<ShippingOptionResp> {
                override fun onFailure(call: Call<ShippingOptionResp>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ShippingOptionResp>,
                    response: Response<ShippingOptionResp>
                ) {
                    //isLoading.value = false
                    if (response.isSuccessful) {
                        paymentOptionObserver.value = response.body()
                    }
//                    else {
//                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
//                    }
                }
            })
    }

    fun getCartTotalPrice() {
        getRetrofitBuilder()
            .getCartTotalPrice(cartMasterId =SharedPreferencesStaticClass.getMasterCartId())
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        getCartPrice.value = response.body()
                    }else{
//                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getProductDetailsById(productId: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getProductDetailById2(productId)
            .enqueue(object : Callback<ProductResp> {
                override fun onFailure(call: Call<ProductResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ProductResp>,
                    response: Response<ProductResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        productDetailsObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun callBuyNow(productId: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getBuyNow(productId)
            .enqueue(object : Callback<GeneralRespone> {
                override fun onFailure(call: Call<GeneralRespone>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralRespone>,
                    response: Response<GeneralRespone>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getMasterFromBuyNow.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getBidsPersons(productId: Int) {
        getRetrofitBuilder().getBidsPersons(productId)
            .enqueue(object : Callback<BidPersonsResp> {
                override fun onFailure(call: Call<BidPersonsResp>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BidPersonsResp>,
                    response: Response<BidPersonsResp>
                ) {
                    if (response.isSuccessful) {
                        bidsPersonsObserver.value = response.body()
                    }
                }
            })
    }
    fun getSellerInfo(productId: Int) {
        println("hhhh tt " + productId)
        sellerInfoLoadingObservable.value = true
        getRetrofitBuilder()
            .getSellerInformation(productId)
            .enqueue(object : Callback<SellerInfoResp> {
                override fun onFailure(call: Call<SellerInfoResp>, t: Throwable) {
                    sellerInfoLoadingObservable.value = false
                }

                override fun onResponse(
                    call: Call<SellerInfoResp>,
                    response: Response<SellerInfoResp>
                ) {
                    sellerInfoLoadingObservable.value = false
                    if (response.isSuccessful) {
                        sellerInfoObservable.value = response.body()
                    }
                }
            })
    }

    fun addQuestion(productId: Int, question: String) {
        val data: HashMap<String, Any> = HashMap()
        data["Question"] = question
        data["ProductId"] = productId
        data["lang"] = ConstantObjects.currentLanguage
        isLoading.value = true
        getRetrofitBuilder()
            .addQuestion(
                question.requestBody(),
                productId.toString().requestBody(),
                ConstantObjects.currentLanguage.requestBody()
            )
            //?.addQuestion(data)
            .enqueue(object : Callback<AddQuestionResp> {
                override fun onFailure(call: Call<AddQuestionResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddQuestionResp>,
                    response: Response<AddQuestionResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addQuestionObservable.value = response.body()
                    } else {

                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getSimilarProduct(productId: Int, page: Int) {
        getRetrofitBuilder()
            .getSimilarProductForOtherProduct(page, productId)

            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {

                    if (response.isSuccessful) {
                        getSimilarProductObservable.value = response.body()
                    }
                }
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
        getRetrofitBuilder()
            .getQuestionList(productId)

            .enqueue(object : Callback<QuestionsResp> {
                override fun onFailure(call: Call<QuestionsResp>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<QuestionsResp>,
                    response: Response<QuestionsResp>
                ) {

                    if (response.isSuccessful) {
                        getListOfQuestionsObservable.value = response.body()
                    }
                }
            })
    }

    fun getListOfQuestionsForActivity(productId: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getQuestionList(productId)

            .enqueue(object : Callback<QuestionsResp> {
                override fun onFailure(call: Call<QuestionsResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<QuestionsResp>,
                    response: Response<QuestionsResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getListOfQuestionsObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getProductRatesForActivity(productID: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getRates(productID)
            .enqueue(object : Callback<RateProductResponse> {
                override fun onFailure(call: Call<RateProductResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<RateProductResponse>,
                    response: Response<RateProductResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getRateResponseObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getProductRatesForProductDetails(productID: Int) {
        getRetrofitBuilder()
            .getRates(productID)
            .enqueue(object : Callback<RateProductResponse> {
                override fun onFailure(call: Call<RateProductResponse>, t: Throwable) {
                    t.message.toString()
                }

                override fun onResponse(
                    call: Call<RateProductResponse>,
                    response: Response<RateProductResponse>
                ) {
                    if (response.isSuccessful) {
                        getRateResponseObservable.value = response.body()
                    }
                }
            })
    }

    fun addRateBuyer(orderId:Int ,buyerRateId:Int ,buyerId: String, rate: Int, comment: String) {
        isLoading.value = true
        val data: HashMap<String, Any> = HashMap()
        data["orderId"] =orderId
        data["buyerRateId"] = buyerRateId
        data["buyerId"] = buyerId
        data["rate"] = rate
        data["comment"] = comment

        getRetrofitBuilder()
            .addRateBuyer(data)
            .enqueue(object : Callback<GeneralRespone> {
                override fun onFailure(call: Call<GeneralRespone>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralRespone>,
                    response: Response<GeneralRespone>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addBuyerRateObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
    fun getRateBuyer(orderId:Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getRateBuyer(orderId)
            .enqueue(object : Callback<BuyerRateResp> {
                override fun onFailure(call: Call<BuyerRateResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<BuyerRateResp>,
                    response: Response<BuyerRateResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getBuyerRateObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun addRateProduct(productID: Int, rate: Float, comment: String) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["productId"] = productID.toString().requestBody()
        map["comment"] = comment.toString().requestBody()
        map["rate"] = rate.toString().requestBody()
        getRetrofitBuilder()
            .addRateProduct(map)
            .enqueue(object : Callback<AddRateResp> {
                override fun onFailure(call: Call<AddRateResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddRateResp>,
                    response: Response<AddRateResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addRateRespObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun editRateProduct(rateId: Int, rate: Float, comment: String) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["id"] = rateId.toString().requestBody()
        map["comment"] = comment.toString().requestBody()
        map["rate"] = rate.toString().requestBody()

        getRetrofitBuilder()
            .editRateProduct(map)
            .enqueue(object : Callback<AddRateResp> {
                override fun onFailure(call: Call<AddRateResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddRateResp>,
                    response: Response<AddRateResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        editRateRespObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getCurrentUserRate(productId: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getCurrenUserRateForProdust(productId)
            .enqueue(object : Callback<CurrentUserRateResp> {
                override fun onFailure(call: Call<CurrentUserRateResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<CurrentUserRateResp>,
                    response: Response<CurrentUserRateResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getCurrentUserRateObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun getSellerRates(providerId: String, businessAccountId: String?) {
        isLoading.value = true
        getRetrofitBuilder()
            .getSellerRates(providerId, businessAccountId)
            .enqueue(object : Callback<SellerRateListResp> {
                override fun onFailure(call: Call<SellerRateListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<SellerRateListResp>,
                    response: Response<SellerRateListResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        sellerRateListObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun addDiscount(productId: Int, discountPrice: Float, finaldate: String) {
        isLoading.value = true
        getRetrofitBuilder()
            .addDiscount(productId, discountPrice, finaldate)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addDiscountObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun getSellerRates2AsSeller(providerId: String, businessAccountId: String?, page: Int, sendRate: Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore .value = true
        getRetrofitBuilder()
            .getSellerRates2AsAsseller(providerId, businessAccountId, page,sendRate)
            .enqueue(object : Callback<SellerRateListResp> {
                override fun onFailure(call: Call<SellerRateListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value=false
                }

                override fun onResponse(
                    call: Call<SellerRateListResp>,
                    response: Response<SellerRateListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value=false
                    if (response.isSuccessful) {
                        sellerRateListObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
    fun getSellerRates2AsAbuyer(providerId: String, businessAccountId: String?, page: Int, sendRate: Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore .value = true
        getRetrofitBuilder()
            .getSellerRates2AsAsABuyer(providerId, businessAccountId, page,sendRate)
            .enqueue(object : Callback<SellerRateListResp> {
                override fun onFailure(call: Call<SellerRateListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                    isloadingMore.value=false
                }

                override fun onResponse(
                    call: Call<SellerRateListResp>,
                    response: Response<SellerRateListResp>
                ) {
                    isLoading.value = false
                    isloadingMore.value=false
                    if (response.isSuccessful) {
                        sellerRateListObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
    fun addSellerRate(
        providerId: String,
        businessAccountId: String,
        rating: Float,
        comment: String
    ) {
        isLoading.value = true
        var data: HashMap<String, Any> = HashMap()
        data["providerId"] = providerId
        data["businessAccountId"] = businessAccountId
        data["rate"] = rating
        data["comment"] = comment
        getRetrofitBuilder()
            .addRateSeller2(data)
            .enqueue(object : Callback<AddRateResp> {
                override fun onFailure(call: Call<AddRateResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddRateResp>,
                    response: Response<AddRateResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addRateRespObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun addProductToCart(masterCartId: String, productId: Int) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["productId"] = productId.toString().requestBody()
        map["CartMasterId"] = masterCartId.requestBody()
        map["quantity"] = "1".requestBody()
        println("hhhh tt " + masterCartId + " " + productId)
        getRetrofitBuilder()
            .addProductToCartProducts(map)
            .enqueue(object : Callback<AddProductToCartResp> {
                override fun onFailure(call: Call<AddProductToCartResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddProductToCartResp>,
                    response: Response<AddProductToCartResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addProductToCartObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }


    var sellerProductsRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var sellerLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getSellerListProduct(sellerProviderID: String, businessAccountId: String) {
        sellerLoading.value = true
        getRetrofitBuilder()
            .getListSellerProducts(1, sellerProviderID, businessAccountId)
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    sellerLoading.value = false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    sellerLoading.value = false
                    if (response.isSuccessful) {
                        sellerProductsRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    var addSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var removeSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    fun addSellerToFav(sellerProviderID:String?,businessAccountId:String?){
        sellerLoading.value = true
        getRetrofitBuilder()
            .addFavoriteSeller(sellerProviderID, businessAccountId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    sellerLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    sellerLoading.value = false
                    if (response.isSuccessful) {
                        addSellerToFavObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
    fun removeSellerToFav(sellerProviderID:String?,businessAccountId:String?){
        sellerLoading.value = true
        getRetrofitBuilder()
            .removeFavoriteSeller(sellerProviderID, businessAccountId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    sellerLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    sellerLoading.value = false
                    if (response.isSuccessful) {
                        removeSellerToFavObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun removeProduct(productId: Int){
        sellerLoading.value = true
        getRetrofitBuilder()
            .removeProduct(productId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    sellerLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    sellerLoading.value = false
                    if (response.isSuccessful) {
                        removeProductObserver.value = response.body()

                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
}