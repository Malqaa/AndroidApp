package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel

import android.annotation.SuppressLint
import android.content.Context
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
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper
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

    fun getCouponByCode(couponCode: String) {
        isLoading.value = true
        getRetrofitBuilder()
            .getCouponByCode(couponCode)
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
                    SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())
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
        map["nameAr"] = nameAr.requestBody()
        map["nameEn"] = nameEn.requestBody()
//        if (subTitleAr != "")
        map["subTitleAr"] = subTitleAr.requestBody()
//        if (subTitleEn != "")
        map["subTitleEn"] = subTitleEn.requestBody()
//        if (descriptionAr != "")
        map["descriptionAr"] = descriptionAr.requestBody()
//        if (descriptionEn != "")
        map["descriptionEn"] = descriptionEn.requestBody()
        if (qty != "")
            map["qty"] = qty.requestBody()
        if (productCondition != "0" && productCondition != "" && productCondition != "null")
            map["status"] = productCondition.requestBody()
        if (categoryId != "0" && categoryId != "")
            map["categoryId"] = categoryId.requestBody()
        if (countryId != "0" && countryId != "" && countryId != "null")
            map["countryId"] = countryId.requestBody()
        if (regionId != "0" && regionId != "" && regionId != "null")
            map["regionId"] = regionId.requestBody()
        if (neighborhoodId != "0" && neighborhoodId != "" && neighborhoodId != "null")
            map["neighborhoodId"] = neighborhoodId.requestBody()
        map["Street"] = Street.requestBody()
        if (pakatId != "")
            map["pakatId"] = pakatId.requestBody()
        if (productSep != null)
            productSep.let {
                map["productSep"] = Gson().toJson(it).toString().requestBody()
            }
        else {
            map["productSep"] = "[]".requestBody()
        }
        if (MainImageIndex == "")
            map["MainImageIndex"] = "0".requestBody()
        else
            map["MainImageIndex"] = MainImageIndex.requestBody()
        map["acceptQuestion"] = "false".requestBody()
        map["IsFixedPriceEnabled"] = isFixedPriceEnabled.toString().requestBody()
        map["IsAuctionEnabled"] = isAuctionEnabled.toString().requestBody()
        map["IsNegotiationEnabled"] = isNegotiationEnabled.toString().requestBody()
        if (price != "") {
            map["price"] = price.requestBody()
            map["priceDisc"] = priceDisc.requestBody()
        }

        if (isFixedPriceEnabled) {
            map["isMazad"] = "false".requestBody()
            map["startPriceMazad"] = "0".requestBody()
            map["lessPriceMazad"] = "0".requestBody()
            map["mazadNegotiatePrice"] = price.requestBody()
        } else {
            map["startPriceMazad"] = auctionStartPrice.requestBody()
            map["lessPriceMazad"] = auctionMinimumPrice.requestBody()
            map["isMazad"] = "true".requestBody()
        }
        map["isSendOfferForMazad"] = "false".requestBody()
        map["appointment"] = "".requestBody()
        map["PaymentOptionId"] = "1".requestBody()
        map["mazadNegotiateForWhom"] = "0".requestBody()
        map["IsCashEnabled"] = isCashEnabled.toRequestBody()
        map["AuctionStartPrice"] = auctionStartPrice.toRequestBody()
        map["DisccountEndDate"] = disccountEndDate.toRequestBody()
        map["AuctionMinimumPrice"] = auctionMinimumPrice.toRequestBody()
        map["AuctionNegotiatePrice"] = price.requestBody()
        if (validTime != null) {
            map["AuctionClosingTime"] = getAuctionClosingTimeForApi(validTime).toRequestBody()
        }
        map["HighestBidPrice"] = price.toRequestBody()

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
//    map["SendYourAccountInfoToAuctionWinner"]="1".requestBody()
////    map["SendOfferForAuction"]="".requestBody()
//        map["ProductPaymentDetailsDto.typePay"] = "1".toRequestBody()
        if (ProductPaymentDetailsDto_ProductPublishPrice != 0f)
            map["ProductPaymentDetailsDto.ProductPublishPrice"] =
                ProductPaymentDetailsDto_ProductPublishPrice.toString().toRequestBody()
        map["withFixedPrice"] = "false".requestBody()
        map["ProductPaymentDetailsDto.PaymentType"] = "Cash".requestBody()

        map["ProductPaymentDetailsDto.EnableAuctionFee"] =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString().requestBody()
        map["isNegotiationOffers"] =
            AddProductObjectData.selectedCategory?.enableNegotiation.toString().toRequestBody()
        map["ProductPaymentDetailsDto.FixedPriceSaleFee"] =
            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()
                .requestBody()
        map["ProductPaymentDetailsDto.EnableFixedPriceSaleFee"] =
            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()
                .requestBody()
        map["ProductPaymentDetailsDto.NegotiationFee"] = "0.0".toRequestBody()
        if (ProductPaymentDetailsDto_AdditionalPakatId != "")
            map["ProductPaymentDetailsDto.AdditionalPakatId"] = pakatId.toRequestBody()
        if (ProductPaymentDetailsDto_EnableAuctionFee != 0f) {
            map["ProductPaymentDetailsDto.AuctionFee"] =
                ProductPaymentDetailsDto_EnableAuctionFee.toString().toRequestBody()
        }
        if (ProductPaymentDetailsDto_EnableNegotiationFee != 0f) {
            map["ProductPaymentDetailsDto.EnableNegotiationFee"] =
                ProductPaymentDetailsDto_EnableNegotiationFee.toString().toRequestBody()
        }
        if (ProductPaymentDetailsDto_ExtraProductImageFee != 0f) {
            map["ProductPaymentDetailsDto.ExtraProductImageFee"] =
                ProductPaymentDetailsDto_ExtraProductImageFee.toString().toRequestBody()
        }
        if (ProductPaymentDetailsDto_ExtraProductVidoeFee != 0f) {
            map["ProductPaymentDetailsDto.ExtraProductVidoeFee"] =
                ProductPaymentDetailsDto_ExtraProductVidoeFee.toString().toRequestBody()
        }
        if (ProductPaymentDetailsDto_SubTitleFee != 0f) {
            map["ProductPaymentDetailsDto.SubTitleFee"] =
                ProductPaymentDetailsDto_SubTitleFee.toString().toRequestBody()
        }
        if (ProductPaymentDetailsDto_CouponId != 0) {
            map["ProductPaymentDetailsDto.CouponId"] =
                ProductPaymentDetailsDto_CouponId.toString().toRequestBody()
            map["ProductPaymentDetailsDto.CouponDiscountValue"] =
                ProductPaymentDetailsDto_CouponDiscountValue.toString().toRequestBody()
            map["ProductPaymentDetailsDto.TotalAmountAfterCoupon"] =
                ProductPaymentDetailsDto_TotalAmountAfterCoupon.toString().toRequestBody()
        }
        if (ProductPaymentDetailsDto_TotalAmountBeforeCoupon != 0f) {
            map["ProductPaymentDetailsDto.TotalAmountBeforeCoupon"] =
                ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString().toRequestBody()
        }

        if (isEdit) {
            map["id"] = AddProductObjectData.productId.toString().toRequestBody()
            if (ConstantObjects.isRepost) {
                map["EditOrRepost"] = "2".toRequestBody()
            } else {
                map["EditOrRepost"] = "1".toRequestBody()
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

}