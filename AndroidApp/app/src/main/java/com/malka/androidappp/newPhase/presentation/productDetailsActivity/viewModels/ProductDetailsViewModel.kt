package com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addProductToCartResp.AddProductToCartResp
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malka.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malka.androidappp.newPhase.domain.models.questionsResp.QuestionsResp
import com.malka.androidappp.newPhase.domain.models.ratingResp.CurrentUserRateResp
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateResponse
import com.malka.androidappp.newPhase.domain.models.sellerInfoResp.SellerInfoResp
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralRespone
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
    var getRateResponseObservable: MutableLiveData<RateResponse> = MutableLiveData()
    var addRateRespObservable: MutableLiveData<AddRateResp> = MutableLiveData()
    var editRateRespObservable: MutableLiveData<AddRateResp> = MutableLiveData()
    var getCurrentUserRateObservable: MutableLiveData<CurrentUserRateResp> = MutableLiveData()
    var sellerRateListObservable: MutableLiveData<SellerRateListResp> = MutableLiveData()
    var addSellerRateObservable: MutableLiveData<GeneralRespone> = MutableLiveData()
    var addProductToCartObservable: MutableLiveData<AddProductToCartResp> = MutableLiveData()
    var sellerInfoObservable: MutableLiveData<SellerInfoResp> = MutableLiveData()
    var sellerInfoLoadingObservable: MutableLiveData<Boolean> = MutableLiveData()
    fun getProductDetailsById(productId: Int) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getSellerInfo(productId: Int) {
        println("hhhh tt " + productId)
        sellerInfoLoadingObservable.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
        RetrofitBuilder.GetRetrofitBuilder()
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

                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getSimilarProduct(productId: Int, page: Int) {
        RetrofitBuilder.GetRetrofitBuilder()
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
        RetrofitBuilder.GetRetrofitBuilder()
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
        RetrofitBuilder.GetRetrofitBuilder()
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
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getProductRatesForActivity(productID: Int) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getRates(productID)
            .enqueue(object : Callback<RateResponse> {
                override fun onFailure(call: Call<RateResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<RateResponse>,
                    response: Response<RateResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getRateResponseObservable.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getProductRatesForProductDetails(productID: Int) {
        RetrofitBuilder.GetRetrofitBuilder()
            .getRates(productID)
            .enqueue(object : Callback<RateResponse> {
                override fun onFailure(call: Call<RateResponse>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<RateResponse>,
                    response: Response<RateResponse>
                ) {
                    if (response.isSuccessful) {
                        getRateResponseObservable.value = response.body()
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
        RetrofitBuilder.GetRetrofitBuilder()
            .AddRateProduct(map)
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
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

        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getCurrentUserRate(productId: Int) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getSellerRates(providerId: String, businessAccountId: String?) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getSellerRates2AsSeller(providerId: String, businessAccountId: String?, page: Int, sendRate: Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore .value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getSellerRates2AsAbuyer(providerId: String, businessAccountId: String?, page: Int, sendRate: Int?) {
        if (page == 1)
            isLoading.value = true
        else
            isloadingMore .value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
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
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
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
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }


    var sellerProductsRespObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var sellerLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getSellerListProduct(sellerProviderID: String, businessAccountId: String) {
        sellerLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

}