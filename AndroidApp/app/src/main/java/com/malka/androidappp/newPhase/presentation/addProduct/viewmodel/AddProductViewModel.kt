package com.malka.androidappp.newPhase.presentation.addProduct.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.HelpFunctions.Companion.getAuctionClosingTimeForApi
import com.malka.androidappp.newPhase.data.network.callApi
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.accountBackListResp.AccountBankListResp
import com.malka.androidappp.newPhase.domain.models.cartPriceSummery.CartPriceSummeryResp
import com.malka.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malka.androidappp.newPhase.domain.models.discopuntResp.DiscountCouponResp
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductResp
import com.malka.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionResp
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.utils.ConstantsHelper
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
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var isLoadingAllCategory: MutableLiveData<Boolean> = MutableLiveData()
    var confirmAddPorductRespObserver: MutableLiveData<AddProductResponse> = MutableLiveData()
    var addBackAccountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var listBackAccountObserver: MutableLiveData<AccountBankListResp> = MutableLiveData()
    var isLoadingBackAccountList: MutableLiveData<Boolean> = MutableLiveData()
    var cartPriceSummeryObserver: MutableLiveData<CartPriceSummeryResp> = MutableLiveData()
    var couponByCodeObserver: MutableLiveData<DiscountCouponResp> = MutableLiveData()
    var productDetailsObservable: MutableLiveData<ProductResp> = MutableLiveData()
    var shippingOptionObserver: MutableLiveData<ShippingOptionResp> = MutableLiveData()
    var paymentOptionObserver: MutableLiveData<ShippingOptionResp> = MutableLiveData()


    private var callSellerListProductOp: Call<ShippingOptionResp>? = null
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
        if (callProductPaymentOp != null) {
            callProductPaymentOp?.cancel()
        }
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

    fun getAllCategories() {
        isLoadingAllCategory.value = true
        getRetrofitBuilder()
            .getAllCategories()
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoadingAllCategory.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoadingAllCategory.value = false
                    if (response.isSuccessful) {
                        categoriesObserver.value = response.body()
                    } else {
                        categoriesErrorResponseObserver.value =
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


    //    fun getAddProduct(
//        nameAr: String,
//        nameEn: String,
//        subTitleAr: String,
//        subTitleEn: String,
//        descriptionAr: String,
//        descriptionEn: String,
//        qty: String,
//        price: String,
//        priceDisc: String,
//        acceptQuestion: String,
//        isNegotiationOffers: String,
//        withFixedPrice: String,
//        isMazad: String,
//        isSendOfferForMazad: String,
//        startPriceMazad: String,
//        lessPriceMazad: String,
//        mazadNegotiatePrice: String,
//        mazadNegotiateForWhom: String,
//        appointment: String,
//        productCondition: String,
//        categoryId: String,
//        countryId: String,
//        regionId: String,
//        neighborhoodId: String,
//        Street: String,
//        GovernmentCode: String,
//        pakatId: String,
//        productSep: String,
//        listImageFile: List<File>,//listImageFile
//        MainImageIndex: String,
//        videoUrl: String,
//        PickUpDelivery: String,
//        DeliveryOption: String,
//    ) {
//        isLoading.value = true
//        val listOfImages = ArrayList<MultipartBody.Part>()
//        for (i in listImageFile.indices) {
//            listOfImages.add(prepareFilePart("listImageFile", listImageFile[i]))
//        }
//        println("hhh " + nameAr.requestBody())
////        println("hhh images "+listOfImages.size)
//        getRetrofitBuilder()
//            .addProduct(
//                nameAr.requestBody(),
//                nameEn.requestBody(),
//                subTitleAr.requestBody(),
//                subTitleEn.requestBody(),
//                descriptionAr.requestBody(),
//                descriptionEn.requestBody(),
//                qty.requestBody(),
//                price.requestBody(),
//                priceDisc.requestBody(),
//                acceptQuestion.requestBody(),
//                isNegotiationOffers.requestBody(),
//                withFixedPrice.requestBody(),
//                isMazad.requestBody(),
//                isSendOfferForMazad.requestBody(),
//                startPriceMazad.requestBody(),
//                lessPriceMazad.requestBody(),
//                mazadNegotiatePrice.requestBody(),
//                mazadNegotiateForWhom.requestBody(),
//                appointment.requestBody(),
//                productCondition.requestBody(),
//                categoryId.requestBody(),
//                countryId.requestBody(),
//                regionId.requestBody(),
//                neighborhoodId.requestBody(),
//                Street.requestBody(),
//                GovernmentCode.requestBody(),
//                pakatId.requestBody(),
//                productSep.requestBody(),
//                listOfImages,//listImageFile
//                MainImageIndex.requestBody(),
//                videoUrl.requestBody(),
//                PickUpDelivery.requestBody(),
//                DeliveryOption.requestBody(),
//            )
//            .enqueue(object : Callback<GeneralResponses> {
//                override fun onFailure(call: Call<GeneralResponses>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
//                }
//
//                override fun onResponse(
//                    call: Call<GeneralResponses>,
//                    response: Response<GeneralResponses>
//                ) {
//                    isLoading.value = false
//                    if (response.isSuccessful) {
//                        confirmAddPorductRespObserver.value = response.body()
//                    } else {
//                        println(
//                            "hhhh " + response.code() + " " + Gson().toJson(response.errorBody())
//                                .toString()
//                        )
//                        errorResponseObserver.value =
//                            getErrorResponse(response.code(),response.errorBody())
//                    }
//                }
//            })
//    }
    @SuppressLint("SuspiciousIndentation")
    fun getAddProduct3(
        isEdit: Boolean,
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
            if (!auctionClosingTime.contains(":")) {
                val currentTime: String =
                    SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())
                validTime = ("$auctionClosingTime $currentTime")
            } else {
                validTime = ("$auctionClosingTime")
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
        var shippingOptionsList: ArrayList<MultipartBody.Part> = ArrayList()
        /**DeliveryOption**/
        for (item in DeliveryOption) {
            // var requestbody: RequestBody = item.requestBody()
            var multipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("ShippingOptions", item.toString())
            shippingOptionsList.add(multipartBody)
        }
        /**Video**/
        var videoUrlList: ArrayList<MultipartBody.Part> = ArrayList()
        videoUrl?.let {
            for (item in videoUrl) {
                // var requestbody: RequestBody = item.requestBody()
                var multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("videoUrl", item)
                videoUrlList.add(multipartBody)
            }

        }
        /**PaymentOption**/
        var sendPaymentOptionList: ArrayList<MultipartBody.Part> = ArrayList()
        paymentOptionIdList?.let {
            for (item in paymentOptionIdList) {
                // var requestbody: RequestBody = item.requestBody()
                var multipartBody: MultipartBody.Part =
                    MultipartBody.Part.createFormData("PaymentOptions", item.toString())
                sendPaymentOptionList.add(multipartBody)
            }

        }

        var sendBankList: ArrayList<MultipartBody.Part> = ArrayList()
        productBankAccounts?.let {
            for (item in productBankAccounts) {
                var multipartBody: MultipartBody.Part =
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
        productSep.let {
            map["productSep"] = Gson().toJson(it).toString().requestBody()
        }
        map["MainImageIndex"] = MainImageIndex.requestBody()
//        map["PickUpDelivery"] = PickUpDelivery.requestBody()
        println("hhhh d " + DeliveryOption.toString())
        map["acceptQuestion"] = "false".requestBody()
        map["IsFixedPriceEnabled"] = isFixedPriceEnabled.toString().requestBody()
        map["IsAuctionEnabled"] = isAuctionEnabled.toString().requestBody()
        map["IsNegotiationEnabled"] = isNegotiationEnabled.toString().requestBody()
        if (price != "") {
            map["price"] = price.requestBody()
            map["priceDisc"] = priceDisc.requestBody()
        }

        // map["videoUrl"] = videoUrl.toString().requestBody()
        // map["ShippingOptions"] = DeliveryOption.toString().requestBody()
//    map["Lat"] = "".requestBody()
//    map["Lon"] = "".requestBody()
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
        //  map["District"] = Street.requestBody()
        //map["GovernmentCode"] = GovernmentCode.requestBody()
        map["IsCashEnabled"] = isCashEnabled.toRequestBody()
        map["AuctionStartPrice"] = auctionStartPrice.toRequestBody()
        map["DisccountEndDate"] = disccountEndDate.toRequestBody()
        map["AuctionMinimumPrice"] = auctionMinimumPrice.toRequestBody()
        //map["AuctionNegotiateForWhom"]="".requestBody()
        map["AuctionNegotiatePrice"] = price.requestBody()
        if (validTime != null){
            map["AuctionClosingTime"] = getAuctionClosingTimeForApi(validTime).toRequestBody()
        }
        map["HighestBidPrice"] = price.toRequestBody()

//    /***PaymentObject*/
//    //map["ProductPaymentDetailsDto.PakatId"] = "".toRequestBody()
        if (ProductPaymentDetailsDto_ProductPublishPrice != 0f)
            map["ProductPaymentDetailsDto.ProductPublishPrice"] =
                ProductPaymentDetailsDto_ProductPublishPrice.toString().toRequestBody()
////    map["IsAuctionPaied"]="".requestBody()
////    map["SendOfferForAuction"]="".requestBody()
        map["withFixedPrice"] = "false".requestBody()
        map["ProductPaymentDetailsDto.PaymentType"] = "Cash".requestBody()

        map["ProductPaymentDetailsDto.EnableAuctionFee"] = AddProductObjectData.selectedCategory?.enableAuctionFee.toString().requestBody()
        map["isNegotiationOffers"]=AddProductObjectData.selectedCategory?.enableNegotiation.toString().toRequestBody()
        map["ProductPaymentDetailsDto.FixedPriceSaleFee"] =(AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString().requestBody()
        map["ProductPaymentDetailsDto.EnableFixedPriceSaleFee"] = (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString().requestBody()
//    map["SendYourAccountInfoToAuctionWinner"]="1".requestBody()


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
//        map["ProductPaymentDetailsDto.typePay"] = "1".toRequestBody()

        if (isEdit) {
            if (ConstantObjects.isRepost)
                map["EditOrRepost"] = "2".toRequestBody()
            else {
                map["EditOrRepost"] = "1".toRequestBody()
            }
        }
        callProduct = if (isEdit) {
            ConstantObjects.isRepost = false
            ConstantObjects.isModify = false
            getRetrofitBuilder()
                .editProduct(
                    map,
                    imageListTOSend,
                    shippingOptionsList,
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
                isLoading.value = false
                confirmAddPorductRespObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoading.value = false
                ConstantObjects.isRepost = isEdit
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

//    fun getAddProduct2(
//        context: Context,
//        nameAr: String,
//        nameEn: String,
//        subTitleAr: String,
//        subTitleEn: String,
//        descriptionAr: String,
//        descriptionEn: String,
//        qty: String,
//        price: String,
//        priceDisc: String,
//        acceptQuestion: String,
//        isNegotiationOffers: String,
//        withFixedPrice: String,
//
//        isMazad: String,
//        isSendOfferForMazad: String,
//        startPriceMazad: String,
//        lessPriceMazad: String,
//        mazadNegotiatePrice: String,
//        appointment: String,
//
//        mazadNegotiateForWhom: String,
//        productCondition: String,
//        categoryId: String,
//        countryId: String,
//        regionId: String,
//        neighborhoodId: String,
//        Street: String,
//        GovernmentCode: String,
//        pakatId: String,
//        productSep: List<DynamicSpecificationSentObject>?,
//        listImageFile: List<File>,//listImageFile
//        MainImageIndex: String,
//        videoUrl: List<String>?,
//        PickUpDelivery: String,
//        DeliveryOption: String,
//    ) {
//        isLoading.value = true
//        val map: HashMap<String, RequestBody> = HashMap()
//        map["nameAr"] = nameAr.requestBody()
//        map["nameEn"] = nameEn.requestBody()
//        map["subTitleAr"] = subTitleAr.requestBody()
//        map["subTitleEn"] = subTitleEn.requestBody()
//        map["descriptionAr"] = descriptionAr.requestBody()
//        map["descriptionEn"] = descriptionEn.requestBody()
//        map["qty"] = qty.requestBody()
//        map["price"] = price.requestBody()
//        map["priceDisc"] = "0".requestBody()
//        //  map["acceptQuestion"]=acceptQuestion.requestBody()
//        map["isNegotiationOffers"] = isNegotiationOffers.requestBody()
//        map["withFixedPrice"] = withFixedPrice.requestBody()
//        map["isMazad"] = isMazad.requestBody()
//        map["isSendOfferForMazad"] = isSendOfferForMazad.requestBody()
//        map["startPriceMazad"] = "0".requestBody()
//        map["lessPriceMazad"] = "0".requestBody()
//        map["mazadNegotiatePrice"] = mazadNegotiatePrice.requestBody()
//        //  map["mazadNegotiateForWhom"]=mazadNegotiateForWhom.requestBody()
//        //  map["appointment"]=appointment.requestBody()
//        map["status"] = productCondition.requestBody()
//        map["categoryId"] = categoryId.requestBody()
//        map["countryId"] = countryId.requestBody()
//        map["regionId"] = regionId.requestBody()
//        map["neighborhoodId"] = neighborhoodId.requestBody()
//        //  map["District"] = Street.requestBody()
//        //  map["Street"] = Street.requestBody()
//        //map["GovernmentCode"] = GovernmentCode.requestBody()
//        if (pakatId != "")
//            map["pakatId"] = pakatId.requestBody()
//        productSep.let {
//            map["productSep"] = Gson().toJson(it).toString().requestBody()
//        }
//
//        // map["productSep"] =
//        //          "[{HeaderSpeAr:\"colorAr\",HeaderSpeEn:\"colorEn\", ValueSpeAr:\"redAr\", ValueSpeEn:\"redEn\", Type:1},{HeaderSpeAr:\"colorAr\",HeaderSpeEn:\"colorEn\", ValueSpeAr:\"redAr\", ValueSpeEn:\"redEn\", Type:1}]".requestBody()
//        map["MainImageIndex"] = MainImageIndex.requestBody()
//        map["PickUpDelivery"] = PickUpDelivery.requestBody()
//        map["DeliveryOption"] = DeliveryOption.requestBody()
//
//
////        val listOfImages = ArrayList<MultipartBody.Part>()
////        for (i in listImageFile.indices) {
////            listOfImages.add(prepareFilePart("listImageFile", listImageFile[i], context))
////            // map["listImageFile"] = HelpFunctions.getFileImage(listImageFile[i], context).asRequestBody()
////            //  listOfImages.add(prepareFilePart2("listImageFile", listImageFile[i], context))
////            //listOfImages.add(prepareFilePart("listImageFile[$i]", listImageFile[i]))
////        }
////
//////        println("hhhh " + map)
//////        println(
//////            "hhhh catId [${categoryId}] countryId [${countryId}] region [${regionId}]  neighborhoodId [${neighborhoodId} pakaId [${pakatId}  productSep $" +
//////                    "$productSep"
//////        )
//        getRetrofitBuilder()
//            .addProduct2(map)
//            .enqueue(object : Callback<AddProductResponse> {
//                override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
//                }
//
//                override fun onResponse(
//                    call: Call<AddProductResponse>,
//                    response: Response<AddProductResponse>
//                ) {
//                    isLoading.value = false
//                    if (response.isSuccessful) {
//                        confirmAddPorductRespObserver.value = response.body()
//                    } else {
//                        println(
//                            "hhhh " + response.code() + " " + Gson().toJson(response.errorBody())
//                                .toString()
//                        )
//                        errorResponseObserver.value =
//                            getErrorResponse(response.code(), response.errorBody())
//                    }
//                }
//            })
//    }


    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    private fun createPartFromString(descriptionString: String): RequestBody? {
        return descriptionString.toRequestBody(
            MultipartBody.FORM
        )
    }


    private fun prepareFilePart(
        partName: String,
        fileUri: Uri,
        context: Context
    ): MultipartBody.Part {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        val file: File = HelpFunctions.getFileImage(fileUri, context)

        // create RequestBody instance from file
        val requestFile: RequestBody = file
            .asRequestBody(context.contentResolver.getType(fileUri)?.let { it.toMediaTypeOrNull() })

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.name, requestFile);
    }

    private fun prepareFilePart2(
        partName: String,
        fileUri: ByteArray,
        context: Context
    ): MultipartBody.Part {
        // create RequestBody instance from file
        val requestFile: RequestBody = fileUri.toRequestBody()
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, "image", requestFile);
    }


    fun checkOutAdditionalPakat(
        pakatId: Int, categoryId: Int,
        extraProductImageFee: Float,
        extraProductVidoeFee: Float,
        subTitleFee: Float
    ) {
        isLoading.value = true
        getRetrofitBuilder()
            .checkOutAdditionalPakat(
                pakatId,
                categoryId,
                extraProductImageFee,
                extraProductVidoeFee,
                subTitleFee
            )
            .enqueue(object : Callback<CartPriceSummeryResp> {
                override fun onFailure(call: Call<CartPriceSummeryResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<CartPriceSummeryResp>,
                    response: Response<CartPriceSummeryResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        cartPriceSummeryObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

}