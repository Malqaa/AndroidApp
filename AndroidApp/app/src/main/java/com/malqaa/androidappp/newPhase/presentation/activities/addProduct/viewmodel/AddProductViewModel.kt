package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountBankListResp
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.BankAccountRequest
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.cartPriceSummery.CartPriceSummeryResp
import com.malqaa.androidappp.newPhase.domain.models.discopuntResp.DiscountCouponResp
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malqaa.androidappp.newPhase.domain.models.pointsBalance.GetPointsBalanceResponse
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.PaymentAccountType
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.getMonth
import com.malqaa.androidappp.newPhase.utils.getYear
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    var pointsBalance: MutableLiveData<GetPointsBalanceResponse> = MutableLiveData()
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

    fun getProductShippingOptions(productId: Int) {
        callSellerListProductOp = getRetrofitBuilder().getProductShippingOptions(productId)
        callApi(
            callSellerListProductOp!!,
            onSuccess = {
                shippingOptionObserver.value = it
            },
            onFailure = { _, _, _ ->

            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getProductBankAccounts(productId: Int) {
        callProductBankAccounts = getRetrofitBuilder().getProductBankAccounts(productId)
        callApi(
            callProductBankAccounts!!,
            onSuccess = {
                bankOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->

            },
            goLogin = {
                needToLogin.value = true
            })
    }

    fun getProductPaymentOptions(productId: Int) {
        callProductPaymentOp = getRetrofitBuilder().getProductPaymentOptions(productId)
        callApi(
            callProductPaymentOp!!,
            onSuccess = {
                paymentOptionObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
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
        callApi(
            callProductDetailsById!!,
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

    fun getCouponByCode(couponCode: String, couponScreen: String) {
        isLoading.value = true
        getRetrofitBuilder()
            .getCouponByCode(couponScreen, couponCode)
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
        bankName: String? = null,
        bankHolderName: String,
        ibanNumber: String? = null,
        swiftCode: String? = null,
        expiryDate: String,
        saveForLaterUse: Boolean,
        paymentAccountType: String? = null
    ) {
        isLoading.value = true

        val bankAccountRequest = BankAccountRequest(
            accountNumber = accountNumber,
            bankName = bankName,
            bankHolderName = bankHolderName,
            ibanNumber = ibanNumber,
            swiftCode = swiftCode,
            expiryDate = expiryDate,
            saveForLaterUse = saveForLaterUse,
            paymentAccountType = paymentAccountType
        )

        getRetrofitBuilder().addBankAccount(bankAccountRequest = bankAccountRequest)
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

    fun getPointsBalance() {
        isLoading.value = true
        getRetrofitBuilder().getPointsBalance()
            .enqueue(object : Callback<GetPointsBalanceResponse> {
                override fun onFailure(call: Call<GetPointsBalanceResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GetPointsBalanceResponse>,
                    response: Response<GetPointsBalanceResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        pointsBalance.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getBankAccountsList(
        paymentAccountType: Int? = null
    ) {
        isLoadingBackAccountList.value = true
        getRetrofitBuilder()
            .getAllBanksAccount(paymentAccountType = paymentAccountType)
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
        accountDetails: AccountDetails? = null,
        totalAmount: Float,
        pointsNumber: Double? = null,
        selectedPaymentType: PaymentAccountType,
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
        if (subTitleAr != "")
            map["subTitleAr"] = subTitleAr.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (subTitleEn != "")
            map["subTitleEn"] = subTitleEn.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (descriptionAr != "")
            map["descriptionAr"] =
                descriptionAr.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (descriptionEn != "")
            map["descriptionEn"] =
                descriptionEn.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (qty != "")
            map["qty"] = qty.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (productCondition != "0" && productCondition != "" && productCondition != "null")
            map["status"] =
                productCondition.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (categoryId != "0" && categoryId != "")
            map["categoryId"] = categoryId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (countryId != "0" && countryId != "" && countryId != "null")
            map["countryId"] = countryId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (regionId != "0" && regionId != "" && regionId != "null")
            map["regionId"] = regionId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        if (neighborhoodId != "0" && neighborhoodId != "" && neighborhoodId != "null")
            map["neighborhoodId"] =
                neighborhoodId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        map["Street"] = Street.requestBody()
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
        if (ProductPaymentDetailsDto_AdditionalPakatId != "")
            map["ProductPaymentDetailsDto.AdditionalPakatId"] =
                pakatId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        if (ProductPaymentDetailsDto_ProductPublishPrice != 0f)
            map["ProductPaymentDetailsDto.ProductPublishPrice"] =
                ProductPaymentDetailsDto_ProductPublishPrice.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        map["ProductPaymentDetailsDto.EnableFixedPriceSaleFee"] =
            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        map["ProductPaymentDetailsDto.EnableAuctionFee"] =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())

        if (ProductPaymentDetailsDto_EnableNegotiationFee != 0f) {
            map["ProductPaymentDetailsDto.EnableNegotiationFee"] =
                ProductPaymentDetailsDto_EnableNegotiationFee.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        if (ProductPaymentDetailsDto_ExtraProductImageFee != 0f) {
            map["ProductPaymentDetailsDto.ExtraProductImageFee"] =
                ProductPaymentDetailsDto_ExtraProductImageFee.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        if (ProductPaymentDetailsDto_ExtraProductVidoeFee != 0f) {
            map["ProductPaymentDetailsDto.ExtraProductVidoeFee"] =
                ProductPaymentDetailsDto_ExtraProductVidoeFee.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        if (ProductPaymentDetailsDto_SubTitleFee != 0f) {
            map["ProductPaymentDetailsDto.SubTitleFee"] =
                ProductPaymentDetailsDto_SubTitleFee.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
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

        val paymentType = accountDetails?.paymentAccountType?.paymentType
        if (!paymentType.isNullOrEmpty()) {
            map["ProductPaymentDetailsDto.PaymentType"] =
                "Cash".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

        // =========================================================================================
        // execute payment
        // =========================================================================================
        // account number
        val accountNumber = accountDetails?.accountNumber
        if (!accountNumber.isNullOrEmpty()) {
            map["ExecutePaymentDto.PaymentCard.Number"] =
                accountNumber.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // expiry month
        val expiryMonth = accountDetails?.expiaryDate?.getMonth()
        if (!expiryMonth.isNullOrEmpty()) {
            map["ExecutePaymentDto.PaymentCard.ExpiryMonth"] =
                expiryMonth.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // expiry year
        val expiryYear = accountDetails?.expiaryDate?.getYear()
        if (!expiryYear.isNullOrEmpty()) {
            map["ExecutePaymentDto.PaymentCard.ExpiryYear"] =
                expiryYear.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // security code
        val securityCode = accountDetails?.cvv.toString()
        if (securityCode.isNotEmpty()) {
            map["ExecutePaymentDto.PaymentCard.SecurityCode"] =
                securityCode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // holder name
        val holderName = accountDetails?.bankHolderName
        if (!holderName.isNullOrEmpty()) {
            map["ExecutePaymentDto.PaymentCard.HolderName"] =
                holderName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // payment method id
        val paymentMethodId = selectedPaymentType.value.toString()
        Log.d("test #1", "PaymentMethodId: $paymentMethodId")
        if (paymentMethodId.isNotEmpty()) {
            map["ExecutePaymentDto.PaymentMethodId"] =
                paymentMethodId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // holder name
        if (totalAmount > 0) {
            map["ExecutePaymentDto.TotalAmount"] =
                totalAmount.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // points number
        Log.d("test #1", "pointsNumber: $pointsNumber")
        if (pointsNumber != null) {
            map["ExecutePaymentDto.PointsNumber"] =
                pointsNumber.toInt().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }
        // =========================================================================================

        if (isEdit) {
            map["id"] = AddProductObjectData.productId.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            if (ConstantObjects.isRepost) {
                map["EditOrRepost"] = "2".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            } else {
                map["EditOrRepost"] = "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            }
        }

        callProduct?.request().toString()
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
        callApi(
            callProduct!!,
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
}