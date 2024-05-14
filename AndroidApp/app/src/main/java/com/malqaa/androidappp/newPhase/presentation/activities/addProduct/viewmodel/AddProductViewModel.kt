package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.getAuctionClosingTimeForApi
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountBankListResp
import com.malqaa.androidappp.newPhase.domain.models.cartPriceSummery.CartPriceSummeryResp
import com.malqaa.androidappp.newPhase.domain.models.discopuntResp.DiscountCouponResp
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.presentation.YourProductData
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddProductViewModel : BaseViewModel() {

    var getDynamicSpecificationObserver: MutableLiveData<DynamicSpecificationResp> =
        MutableLiveData()
    var getPakatRespObserver: MutableLiveData<PakatResp> = MutableLiveData()
    var confirmAddPorductRespObserver: MutableLiveData<AddProductResponse> = MutableLiveData()
    var addBackAccountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var listBackAccountObserver: MutableLiveData<AccountBankListResp> = MutableLiveData()
    var isLoadingBackAccountList: MutableLiveData<Boolean> = MutableLiveData()
    var cartPriceSummeryObserver: MutableLiveData<CartPriceSummeryResp> = MutableLiveData()
    var couponByCodeObserver: MutableLiveData<DiscountCouponResp> = MutableLiveData()
    var productDetailsObservable: MutableLiveData<ProductResp> = MutableLiveData()
    var shippingOptionObserver: MutableLiveData<ShippingOptionResp> = MutableLiveData()
    var bankOptionObserver: MutableLiveData<AccountBankListResp> = MutableLiveData()
    var paymentOptionObserver: MutableLiveData<ShippingOptionResp> = MutableLiveData()


    private var callSellerListProductOp: Call<ShippingOptionResp>? = null
    private var callProductBankAccounts: Call<AccountBankListResp>? = null
    private var callProductDetailsById: Call<ProductResp>? = null
    private var callProductPaymentOp: Call<ShippingOptionResp>? = null
    private var callProduct: Call<AddProductResponse>? = null
    fun convertToJson(data: Any): String {
        val gson = Gson()
        return gson.toJson(data)
    }

    fun closeAllCall() {
        if (callSellerListProductOp != null) {
            callSellerListProductOp?.cancel()
        }
        if (callProductDetailsById != null) {
            callProductDetailsById?.cancel()
        }
        if (callProductPaymentOp != null)
            callProductPaymentOp?.cancel()

        if (callProductBankAccounts != null)
            callProductBankAccounts?.cancel()

        if (callProduct != null)
            callProduct?.cancel()
    }

    fun callAddProduct( listImageFile: List<File>) {
        val yourData = YourProductData(
            nameAr = "Product Name AR",
            nameEn = "Product Name EN",
            subTitleAr = "Sub Title AR",
            subTitleEn = "Sub Title EN",
            descriptionAr = "Description AR",
            descriptionEn = "Description EN",
            qty = 10,
            status = 1,
            categoryId = 1,
            countryId = 1,
            regionId = 1,
            neighborhoodId = 1,
            district = "District",
            street = "Street",
            governmentCode = "123456",
            productSep = "Product Sep",
            mainImageIndex = 0,
            lat = 123.456,
            lon = 456.789,
            acceptQuestion = true,
            isFixedPriceEnabled = true,
            isAuctionEnabled = true,
            isNegotiationEnabled = true,
            price = 100.0,
            priceDisc = 90.0,
            isCashEnabled = true,
            auctionStartPrice = 50.0,
            discountEndDate = "2024-05-12",
            sendOfferForAuction = true,
            auctionMinimumPrice = 40.0,
            auctionNegotiateForWhom = 1,
            auctionNegotiatePrice = 30.0,
            auctionClosingTime = "2024-05-15",
            sendYourAccountInfoToAuctionWinner = true,
            almostSoldOutQuantity = 5,
            pakatId = 1,
            additionalPakatId = 2,
            productPublishPrice = 80.0,
            enableFixedPriceSaleFee = true,
            enableAuctionFee = true,
            enableNegotiationFee = true,
            extraProductImageFee = 5.0,
            extraProductVideoFee = 10.0,
            subTitleFee = 2.0,
            fixedPriceSaleFee = 3.0,
            auctionFee = 4.0,
            negotiationFee = 5.0,
            productPaymentDetailsCategoryId = 1,
            productPaymentDetailsCouponId = 1,
            productPaymentDetailsCouponDiscountValue = 10.0,
            productPaymentDetailsTotalAmountBeforeCoupon = 90.0,
            productPaymentDetailsTotalAmountAfterCoupon = 80.0,
            productPaymentDetailsPaymentType = 1
        )

        // Convert data to JSON
        val jsonData = convertToJson(yourData)

        // Create request body with JSON data
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonData)
        val gsonPart = MultipartBody.Part.createFormData("data", null, requestBody)

        // Create other parts (empty for demonstration)
        val listImageFiles = emptyList<MultipartBody.Part>()
        val videoUrlList = emptyList<MultipartBody.Part>()
        val shippingOptions = emptyList<MultipartBody.Part>()
        val paymentOptions = emptyList<MultipartBody.Part>()
        val productBankAccounts = emptyList<MultipartBody.Part>()


        val imageFiles = ArrayList<MultipartBody.Part>()

        for (file in listImageFile) {
            val multipartBody: MultipartBody.Part = if (file != null) {
                ConstantsHelper.getMultiPart(file, "image/*", "listImageFile")!!
            } else {
                MultipartBody.Part.createFormData("listImageFile", "null", "null".toRequestBody())
            }
            imageFiles.add(multipartBody)
        }
        // Make the API call
        val call = getRetrofitBuilder().addProductTest(gsonPart, imageFiles, videoUrlList, shippingOptions, paymentOptions, productBankAccounts)

        callApi(call!!,
            onSuccess = {
                ConstantObjects.isRepost = false
                ConstantObjects.isModify = false
                isLoading.value = false
                confirmAddPorductRespObserver.value = it
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


    fun getProductShippingOptions(productId: Int) {
        //isLoading.value = true
        callSellerListProductOp = getRetrofitBuilder().getProductShippingOptions(productId)
        callApi(callSellerListProductOp!!,
            onSuccess = {
//                isLoading.value = false
                shippingOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->

            },
            goLogin = {
//                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getProductBankAccounts(productId: Int) {
        //isLoading.value = true
        callProductBankAccounts = getRetrofitBuilder().getProductBankAccounts(productId)
        callApi(callProductBankAccounts!!,
            onSuccess = {
//                isLoading.value = false
                bankOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->

            },
            goLogin = {
//                isLoading.value = false
                needToLogin.value = true
            })
    }


    fun getProductPaymentOptions(productId: Int) {
        //isLoading.value = true
        callProductPaymentOp = getRetrofitBuilder().getProductPaymentOptions(productId)
        callApi(callProductPaymentOp!!,
            onSuccess = {
//                isLoading.value = false
                paymentOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
//                isLoading.value = false
//
            },
            goLogin = {
                isLoading.value = false
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
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getCouponByCode(couponCode: String,couponScreen :String) {
        isLoading.value = true
        getRetrofitBuilder()
            .getCouponByCode(couponScreen ,couponCode)
            .enqueue(object : Callback<DiscountCouponResp> {
                override fun onFailure(call: Call<DiscountCouponResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<DiscountCouponResp>,
                    response: Response<DiscountCouponResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        couponByCodeObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun getDynamicSpecification(categoryId: Int) {
        isLoading.value = true
        println("hhhh $categoryId")
        getRetrofitBuilder()
            .getDynamicSpecificationForCategory(categoryId.toString())
            .enqueue(object : Callback<DynamicSpecificationResp> {
                override fun onFailure(call: Call<DynamicSpecificationResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<DynamicSpecificationResp>,
                    response: Response<DynamicSpecificationResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getDynamicSpecificationObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getPakatList(categoryId: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .getAllPakatList(categoryId.toString())
            .enqueue(object : Callback<PakatResp> {
                override fun onFailure(call: Call<PakatResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<PakatResp>,
                    response: Response<PakatResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getPakatRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun addBackAccountData(
        accountNumber: String,
        bankName: String,
        bankHolderName: String,
        ibanNumber: String,
        swiftCode: String,
        expiaryDate: String,
        SaveForLaterUse: String
    ) {
        isLoading.value = true
        getRetrofitBuilder()
            .addBankAccount(
                accountNumber.requestBody(),
                bankName.requestBody(),
                bankHolderName.requestBody(),
                ibanNumber.requestBody(),
                swiftCode.requestBody(),
                expiaryDate.requestBody(),
                SaveForLaterUse.requestBody()
            )
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
                        addBackAccountObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getBankAccountsList() {
        isLoadingBackAccountList.value = true
        getRetrofitBuilder()
            .getAllBacksAccount()
            .enqueue(object : Callback<AccountBankListResp> {
                override fun onFailure(call: Call<AccountBankListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoadingBackAccountList.value = false
                }

                override fun onResponse(
                    call: Call<AccountBankListResp>,
                    response: Response<AccountBankListResp>
                ) {
                    isLoadingBackAccountList.value = false
                    if (response.isSuccessful) {
                        listBackAccountObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun uploadDataToApi(
        isEdit: Boolean,
        productId: Int,
        context: Context,
        nameAr: String,
        nameEn: String,
        subTitleAr: String,
        subTitleEn: String,
        descriptionAr: String,
        descriptionEn: String,
        qty: String,
        productCondition: String,
        categoryId: String,
        countryId: String,
        regionId: String,
        neighborhoodId: String,
        Street: String,
        GovernmentCode: String,
        pakatId: String,
        productSep: List<DynamicSpecificationSentObject>?,
        listImageFile: List<File>,//listImageFile
        MainImageIndex: String,
        videoUrl: List<String>?,
        PickUpDelivery: String,
        DeliveryOption: List<Int>,
        isFixedPriceEnabled: Boolean,
        isAuctionEnabled: Boolean,
        isNegotiationEnabled: Boolean,
        price: String,
        priceDisc: String,
        paymentOptionIdList: List<Int>?,
        isCashEnabled: String,
        disccountEndDate: String,
        auctionStartPrice: String,
        auctionMinimumPrice: String,
        auctionClosingTime: String,
        productBankAccounts: List<Int>?,
        ProductPaymentDetailsDto_AdditionalPakatId: String,
        ProductPaymentDetailsDto_ProductPublishPrice: Float,
        ProductPaymentDetailsDto_EnableAuctionFee: Float,
        ProductPaymentDetailsDto_EnableNegotiationFee: Float,
        ProductPaymentDetailsDto_ExtraProductImageFee: Float,
        ProductPaymentDetailsDto_ExtraProductVidoeFee: Float,
        ProductPaymentDetailsDto_SubTitleFee: Float,
        ProductPaymentDetailsDto_CouponId: Int,
        ProductPaymentDetailsDto_CouponDiscountValue: Float,
        ProductPaymentDetailsDto_TotalAmountAfterCoupon: Float,
        ProductPaymentDetailsDto_TotalAmountBeforeCoupon: Float,
    ) {
        var validTime: String? = null
        if (auctionClosingTime.isNotEmpty()) {
            validTime = if (!auctionClosingTime.contains(":")) {
                val currentTime: String =
                    SimpleDateFormat("hh:mm", Locale.ENGLISH).format(Date())
                ("$auctionClosingTime $currentTime")
            } else {
                auctionClosingTime
            }
        }
        val imageFiles = ArrayList<MultipartBody.Part>()

        for (file in listImageFile) {
            val multipartBody: MultipartBody.Part = if (file != null) {
                ConstantsHelper.getMultiPart(file, "image/*", "listImageFile")!!
            } else {
                MultipartBody.Part.createFormData("listImageFile", "null", "null".toRequestBody())
            }
            imageFiles.add(multipartBody)
        }

        val videoUrlList: ArrayList<MultipartBody.Part> = ArrayList()
        videoUrl?.let {
            for (item in videoUrl) {
                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("videoUrl", item)
                videoUrlList.add(multipartBody)
            }

        }
        val shippingOptionsList: ArrayList<MultipartBody.Part> = ArrayList()
        /**DeliveryOption**/
        for (item in DeliveryOption) {
            val multipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("ShippingOptions", item.toString())
            shippingOptionsList.add(multipartBody)
        }

        val sendPaymentOptionList: ArrayList<MultipartBody.Part> = ArrayList()
        paymentOptionIdList?.let {
            for (item in paymentOptionIdList) {
                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("PaymentOptions", item.toString())
                sendPaymentOptionList.add(multipartBody)
            }

        }

        val sendBankList: ArrayList<MultipartBody.Part> = ArrayList()
        productBankAccounts?.let {
            for (item in productBankAccounts) {
                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("ProductBankAccounts", item.toString())
                sendBankList.add(multipartBody)
            }

        }

        // Prepare other data as RequestBody
        val isCashEnabledBody = isCashEnabled.toRequestBody("text/plain".toMediaTypeOrNull())
        val nameArRequestBody = nameAr.toRequestBody("text/plain".toMediaTypeOrNull())
        val nameEnRequestBody = nameEn.toRequestBody("text/plain".toMediaTypeOrNull())
        val subTitleArRequestBody = subTitleAr.toRequestBody("text/plain".toMediaTypeOrNull())
        val subTitleEnRequestBody = subTitleEn.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionArRequestBody = descriptionAr.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionEnRequestBody = descriptionEn.toRequestBody("text/plain".toMediaTypeOrNull())
        val qtyRequestBody = qty.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val statusRequestBody =
            productCondition.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryIdRequestBody =
            categoryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val countryIdRequestBody =
            countryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val regionIdRequestBody =
            regionId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val neighborhoodIdRequestBody =
            neighborhoodId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val districtRequestBody = Street.toRequestBody("text/plain".toMediaTypeOrNull())
        val streetRequestBody = Street.toRequestBody("text/plain".toMediaTypeOrNull())
        val governmentCodeRequestBody =
            GovernmentCode.toRequestBody("text/plain".toMediaTypeOrNull())


        var productSepRequestBody = if (productSep != null)
            productSep.let {
                Gson().toJson(it).toString().toRequestBody("text/plain".toMediaTypeOrNull())
            }
        else {
            "[]".toRequestBody("text/plain".toMediaTypeOrNull())
        }
        val mainImageIndexRequestBody = if (MainImageIndex == "")
            "0".toRequestBody("text/plain".toMediaTypeOrNull())
        else {
            MainImageIndex.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        val latRequestBody = "0.0".toRequestBody("text/plain".toMediaTypeOrNull())
        val lonRequestBody = "0.0".toRequestBody("text/plain".toMediaTypeOrNull())
        val acceptQuestionRequestBody =
            false.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val isFixedPriceEnabledRequestBody =
            isFixedPriceEnabled.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val isAuctionEnabledRequestBody =
            isAuctionEnabled.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val isNegotiationEnabledRequestBody =
            isNegotiationEnabled.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val priceRequestBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val priceDiscRequestBody =
            priceDisc.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val auctionStartPriceRequestBody =
            auctionStartPrice.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val discountEndDateRequestBody =
            disccountEndDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val sendOfferForAuctionRequestBody =
            false.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val auctionMinimumPriceRequestBody =
            auctionMinimumPrice.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val auctionNegotiateForWhomRequestBody = "".toRequestBody("text/plain".toMediaTypeOrNull())
        val auctionNegotiatePriceRequestBody =
            price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val auctionClosingTimeRequestBody = if (validTime != null) {
            (validTime).toRequestBody("text/plain".toMediaTypeOrNull())
        } else
            "null".toRequestBody("text/plain".toMediaTypeOrNull())


        val sendYourAccountInfoToAuctionWinnerRequestBody =
            false.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val almostSoldOutQuantityRequestBody =
            "".toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val pakatIdRequestBody = pakatId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val additionalPakatIdRequestBody =
            pakatId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val productPublishPriceRequestBody =
            ProductPaymentDetailsDto_ProductPublishPrice.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val enableFixedPriceSaleFeeRequestBody =
            AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val enableAuctionFeeRequestBody =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val enableNegotiationFeeRequestBody =
            AddProductObjectData.selectedCategory?.enableNegotiationFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val extraProductImageFeeRequestBody =
            AddProductObjectData.selectedCategory?.extraProductImageFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val extraProductVideoFeeRequestBody =
            AddProductObjectData.selectedCategory?.extraProductVidoeFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val subTitleFeeRequestBody =
            AddProductObjectData.selectedCategory?.subTitleFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val fixedPriceSaleFeeRequestBody =
            AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val auctionFeeRequestBody =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val negotiationFeeRequestBody =
            AddProductObjectData.selectedCategory?.enableNegotiationFee.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val productPaymentDetailsCategoryIdRequestBody = categoryId.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val productPaymentDetailsCouponIdRequestBody = if (ProductPaymentDetailsDto_CouponId != 0) {
            ProductPaymentDetailsDto_CouponId.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
        } else {
            "null".toRequestBody("text/plain".toMediaTypeOrNull())
        }

        val productPaymentDetailsCouponDiscountValueRequestBody =
            if (ProductPaymentDetailsDto_CouponId != 0) {
                ProductPaymentDetailsDto_CouponDiscountValue.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            } else {
                "null".toRequestBody("text/plain".toMediaTypeOrNull())
            }

        val productPaymentDetailsTotalAmountBeforeCouponRequestBody =
            if (ProductPaymentDetailsDto_TotalAmountBeforeCoupon != 0f) {
                ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            } else
                "null".toRequestBody("text/plain".toMediaTypeOrNull())
        val productPaymentDetailsTotalAmountAfterCouponRequestBody =
            if (ProductPaymentDetailsDto_TotalAmountBeforeCoupon != 0f) {
                ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString()
                    .toRequestBody("text/plain".toMediaTypeOrNull())
            } else
                "null".toRequestBody("text/plain".toMediaTypeOrNull())

        val productPaymentDetailsPaymentTypeRequestBody =
            "Cash".toRequestBody("text/plain".toMediaTypeOrNull())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = getRetrofitBuilder().uploadData(
                    nameArRequestBody,
                    nameEnRequestBody,
                    subTitleArRequestBody,
                    subTitleEnRequestBody,
                    descriptionArRequestBody,
                    descriptionEnRequestBody,
                    qtyRequestBody,
                    statusRequestBody,
                    categoryIdRequestBody,
                    countryIdRequestBody,
                    regionIdRequestBody,
                    neighborhoodIdRequestBody,
                    districtRequestBody,
                    streetRequestBody,
                    governmentCodeRequestBody,
                    productSepRequestBody,
                    imageFiles,
                    mainImageIndexRequestBody,
                    videoUrlList,
                    shippingOptionsList,
                    latRequestBody,
                    lonRequestBody,
                    acceptQuestionRequestBody,
                    isFixedPriceEnabledRequestBody,
                    isAuctionEnabledRequestBody,
                    isNegotiationEnabledRequestBody,
                    priceRequestBody,
                    priceDiscRequestBody,
                    sendPaymentOptionList,
                    sendBankList,
                    isCashEnabledBody,
                    auctionStartPriceRequestBody,
                    discountEndDateRequestBody,
                    sendOfferForAuctionRequestBody,
                    auctionMinimumPriceRequestBody,
                    auctionNegotiateForWhomRequestBody,
                    auctionNegotiatePriceRequestBody,
                    auctionClosingTimeRequestBody!!,
                    sendYourAccountInfoToAuctionWinnerRequestBody,
                    almostSoldOutQuantityRequestBody,
                    pakatIdRequestBody,
                    additionalPakatIdRequestBody,
                    productPublishPriceRequestBody,
                    enableFixedPriceSaleFeeRequestBody,
                    enableAuctionFeeRequestBody,
                    enableNegotiationFeeRequestBody,
                    extraProductImageFeeRequestBody,
                    extraProductVideoFeeRequestBody,
                    subTitleFeeRequestBody,
                    fixedPriceSaleFeeRequestBody,
                    auctionFeeRequestBody,
                    negotiationFeeRequestBody,
                    productPaymentDetailsCategoryIdRequestBody,
                    productPaymentDetailsCouponIdRequestBody!!,
                    productPaymentDetailsCouponDiscountValueRequestBody!!,
                    productPaymentDetailsTotalAmountBeforeCouponRequestBody!!,
                    productPaymentDetailsTotalAmountAfterCouponRequestBody!!,
                    productPaymentDetailsPaymentTypeRequestBody,
                    "multipart/form-data;boundry=<calculated when request is sent>"
                )
                isLoading.value = true
                callApi(response,
                    onSuccess = {
                        ConstantObjects.isRepost = false
                        ConstantObjects.isModify = false
                        isLoading.value = false
                        confirmAddPorductRespObserver.value = it
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
                // Handle response
            } catch (e: Exception) {
//                isLoading.value = false
                Log.i("", e.message.toString())
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getAddProduct3(
        isEdit: Boolean,
        productId: Int,
        context: Context,
        nameAr: String,
        nameEn: String,
        subTitleAr: String,
        subTitleEn: String,
        descriptionAr: String,
        descriptionEn: String,
        qty: String,
        productCondition: String,
        categoryId: String,
        countryId: String,
        regionId: String,
        neighborhoodId: String,
        Street: String,
        GovernmentCode: String,
        pakatId: String,
        productSep: List<DynamicSpecificationSentObject>?,
        listImageFile: List<File>,//listImageFile
        MainImageIndex: String,
        videoUrl: List<String>?,
        PickUpDelivery: String,
        DeliveryOption: List<Int>,
        isFixedPriceEnabled: Boolean,
        isAuctionEnabled: Boolean,
        isNegotiationEnabled: Boolean,
        price: String,
        priceDisc: String,
        paymentOptionIdList: List<Int>?,
        isCashEnabled: String,
        disccountEndDate: String,
        auctionStartPrice: String,
        auctionMinimumPrice: String,
        auctionClosingTime: String,
        productBankAccounts: List<Int>?,
        ProductPaymentDetailsDto_AdditionalPakatId: String,
        ProductPaymentDetailsDto_ProductPublishPrice: Float,
        ProductPaymentDetailsDto_EnableAuctionFee: Float,
        ProductPaymentDetailsDto_EnableNegotiationFee: Float,
        ProductPaymentDetailsDto_ExtraProductImageFee: Float,
        ProductPaymentDetailsDto_ExtraProductVidoeFee: Float,
        ProductPaymentDetailsDto_SubTitleFee: Float,
        ProductPaymentDetailsDto_CouponId: Int,
        ProductPaymentDetailsDto_CouponDiscountValue: Float,
        ProductPaymentDetailsDto_TotalAmountAfterCoupon: Float,
        ProductPaymentDetailsDto_TotalAmountBeforeCoupon: Float,
    ) {
        isLoading.value = true
        var validTime: String? = null
        if (auctionClosingTime.isNotEmpty()) {
            validTime = if (!auctionClosingTime.contains(":")) {
                val currentTime: String =
                    SimpleDateFormat("hh:mm", Locale.ENGLISH).format(Date())
                ("$auctionClosingTime $currentTime")
            } else {
                auctionClosingTime
            }
        }


        /**Image**/
        val imageListTOSend: ArrayList<MultipartBody.Part> = ArrayList()
        for (file in listImageFile) {
            val multipartBody: MultipartBody.Part = if (file != null) {
                ConstantsHelper.getMultiPart(file, "image/*", "listImageFile")!!
            } else {
                MultipartBody.Part.createFormData("listImageFile", "null", "null".toRequestBody())
            }
            imageListTOSend.add(multipartBody)
        }
        val shippingOptionsList: ArrayList<MultipartBody.Part> = ArrayList()
        /**DeliveryOption**/
        for (item in DeliveryOption) {
            val multipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("ShippingOptions", item.toString())
            shippingOptionsList.add(multipartBody)
        }

        /**DeletedMedias**/
        var deletedMediasList: List<MultipartBody.Part> = ArrayList()
        if (!AddProductObjectData.imagesListRemoved.isNullOrEmpty()) {
            val deletedMediaList = AddProductObjectData.imagesListRemoved // Your array of integers
            deletedMediasList = deletedMediaList?.map {
                MultipartBody.Part.createFormData("DeletedMedias", it.toString())
            }!!
        }


        /**Video**/
        val videoUrlList: ArrayList<MultipartBody.Part> = ArrayList()
        videoUrl?.let {
            for (item in videoUrl) {
                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("videoUrl", item)
                videoUrlList.add(multipartBody)
            }

        }
        /**PaymentOption**/
        val sendPaymentOptionList: ArrayList<MultipartBody.Part> = ArrayList()
        paymentOptionIdList?.let {
            for (item in paymentOptionIdList) {
                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("PaymentOptions", item.toString())
                sendPaymentOptionList.add(multipartBody)
            }

        }

        val sendBankList: ArrayList<MultipartBody.Part> = ArrayList()
        productBankAccounts?.let {
            for (item in productBankAccounts) {
                val multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("ProductBankAccounts", item.toString())
                sendBankList.add(multipartBody)
            }

        }

        /**data**/
        val map: HashMap<String, RequestBody> = HashMap()
        map["nameAr"] = nameAr.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["nameEn"] = nameEn.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (subTitleAr != "")
        map["subTitleAr"] = subTitleAr.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (subTitleEn != "")
        map["subTitleEn"] = subTitleEn.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (descriptionAr != "")
        map["descriptionAr"] =
            descriptionAr.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (descriptionEn != "")
        map["descriptionEn"] =
            descriptionEn.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (qty != "")
        map["qty"] = qty.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (productCondition != "0" && productCondition != "" && productCondition != "null")
        map["status"] = productCondition.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (categoryId != "0" && categoryId != "")
        map["categoryId"] = categoryId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (countryId != "0" && countryId != "" && countryId != "null")
        map["countryId"] = countryId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (regionId != "0" && regionId != "" && regionId != "null")
        map["regionId"] = regionId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (neighborhoodId != "0" && neighborhoodId != "" && neighborhoodId != "null")
        map["neighborhoodId"] =
            neighborhoodId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        map["Street"] = Street.requestBody()
        if (productSep != null)
            productSep.let {
                map["productSep"] = Gson().toJson(it).toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
        else {
            map["productSep"] = "[]".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        if (MainImageIndex == "")
            map["MainImageIndex"] = "0".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        else
            map["MainImageIndex"] =
                MainImageIndex.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["AcceptQuestion"] = "false".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["IsFixedPriceEnabled"] =
            isFixedPriceEnabled.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["IsAuctionEnabled"] =
            isAuctionEnabled.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["IsNegotiationEnabled"] =
            isNegotiationEnabled.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (price != "") {
            map["price"] = price.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            map["priceDisc"] = priceDisc.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        map["IsCashEnabled"] =
            isCashEnabled.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["AuctionStartPrice"] =
            auctionStartPrice.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["DisccountEndDate"] =
            disccountEndDate.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["AuctionMinimumPrice"] =
            auctionMinimumPrice.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        map["SendYourAccountInfoToAuctionWinner"] =
            "false".toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        if (ProductPaymentDetailsDto_AdditionalPakatId != "")
        map["ProductPaymentDetailsDto.AdditionalPakatId"] =
            pakatId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

//        if (ProductPaymentDetailsDto_ProductPublishPrice != 0f)
        map["ProductPaymentDetailsDto.ProductPublishPrice"] =
            ProductPaymentDetailsDto_ProductPublishPrice.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        map["ProductPaymentDetailsDto.EnableFixedPriceSaleFee"] =
            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        map["ProductPaymentDetailsDto.EnableAuctionFee"] =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

//        if (ProductPaymentDetailsDto_EnableNegotiationFee != 0f) {
        map["ProductPaymentDetailsDto.EnableNegotiationFee"] =
            "10.5".toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        }
//        if (ProductPaymentDetailsDto_ExtraProductImageFee != 0f) {
        map["ProductPaymentDetailsDto.ExtraProductImageFee"] =
            "1.5".toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        }
//        if (ProductPaymentDetailsDto_ExtraProductVidoeFee != 0f) {
        map["ProductPaymentDetailsDto.ExtraProductVidoeFee"] =
            "1.5".toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        }
//        if (ProductPaymentDetailsDto_SubTitleFee != 0f) {
        map["ProductPaymentDetailsDto.SubTitleFee"] =
            "1.4".toRequestBody("multipart/form-data".toMediaTypeOrNull())
//        }
        if (ProductPaymentDetailsDto_CouponId != 0) {
            map["ProductPaymentDetailsDto.CouponId"] =
                ProductPaymentDetailsDto_CouponId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            map["ProductPaymentDetailsDto.CouponDiscountValue"] =
                ProductPaymentDetailsDto_CouponDiscountValue.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        map["ProductPaymentDetailsDto.TotalAmountAfterCoupon"] =
            ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (ProductPaymentDetailsDto_TotalAmountBeforeCoupon != 0f) {
            map["ProductPaymentDetailsDto.TotalAmountBeforeCoupon"] =
                ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        if (validTime != null) {
            map["AuctionClosingTime"] =
                (validTime).toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        map["ProductPaymentDetailsDto.PaymentType"] =
            "Cash".toRequestBody("multipart/form-data".toMediaTypeOrNull())

//        if (isFixedPriceEnabled) {
////            map["isMazad"] = "false".requestBody()
////            map["startPriceMazad"] = "0".requestBody()
////            map["lessPriceMazad"] = "0".requestBody()
//            map["mazadNegotiatePrice"] = price.requestBody()
//        } else {
//            map["startPriceMazad"] = auctionStartPrice.requestBody()
//            map["lessPriceMazad"] = auctionMinimumPrice.requestBody()
//            map["isMazad"] = "true".requestBody()
//        }
//        map["isSendOfferForMazad"] = "false".requestBody()
//        map["appointment"] = "".requestBody()
//        map["PaymentOptionId"] = "1".requestBody()
//        map["mazadNegotiateForWhom"] = "0".requestBody()
//        map["AuctionNegotiatePrice"] = price.requestBody()


//        if (pakatId != "")
//            map["pakatId"] = pakatId.requestBody()
//        map["HighestBidPrice"] = price.toRequestBody()

//    /***PaymentObject*/
//        map["PickUpDelivery"] = PickUpDelivery.requestBody()
//        println("hhhh d " + DeliveryOption.toString())
        // map["videoUrl"] = videoUrl.toString().requestBody()
        // map["ShippingOptions"] = DeliveryOption.toString().requestBody()
//    map["Lat"] = "".requestBody()
//    map["Lon"] = "".requestBody()
        //  map["District"] = Street.requestBody()
        //map["GovernmentCode"] = GovernmentCode.requestBody()
        //map["AuctionNegotiateForWhom"]="".requestBody()
//    //map["ProductPaymentDetailsDto.PakatId"] = "".toRequestBody()
////    map["IsAuctionPaied"]="".requestBody()
//    map["SendOfferForAuction"]="false".requestBody()
//        map["ProductPaymentDetailsDto.typePay"] = "1".toRequestBody()

//        map["withFixedPrice"] = "false".requestBody()

//        map["isNegotiationOffers"] =
//            AddProductObjectData.selectedCategory?.enableNegotiation.toString().toRequestBody()
//        map["ProductPaymentDetailsDto.FixedPriceSaleFee"] =
//            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()
//                .requestBody()
//        map["ProductPaymentDetailsDto.EnableNegotiationFee"] = "1.5".toRequestBody()
//        if (ProductPaymentDetailsDto_AdditionalPakatId != "")
//            map["ProductPaymentDetailsDto.AdditionalPakatId"] = pakatId.toRequestBody()
//        if (ProductPaymentDetailsDto_EnableAuctionFee != 0f) {
//            map["ProductPaymentDetailsDto.EnableAuctionFee"] =
//                "1.5".toString().toRequestBody()
//        }


        if (isEdit) {
            map["id"] = AddProductObjectData.productId.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            if (ConstantObjects.isRepost) {
                map["EditOrRepost"] = "2".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            } else {
                map["EditOrRepost"] = "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
        }

        callProduct = if (isEdit) {
            getRetrofitBuilder()
                .editProduct(
                    map,
                    imageListTOSend,
                    shippingOptionsList,
                    deletedMediasList,
                    videoUrlList,
                    sendPaymentOptionList,
                    sendBankList
                )
        } else {
            getRetrofitBuilder()
                .addProduct3(
                    map,
                    imageListTOSend,
                    shippingOptionsList,
                    videoUrlList,
                    sendPaymentOptionList,
                    sendBankList
                )
        }
        callApi(callProduct!!,
            onSuccess = {
                ConstantObjects.isRepost = false
                ConstantObjects.isModify = false
                isLoading.value = false
                confirmAddPorductRespObserver.value = it
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

    fun c(doubleAsString: String): MultipartBody {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("double_value", doubleAsString)
            .build()
        return requestBody
    }
}