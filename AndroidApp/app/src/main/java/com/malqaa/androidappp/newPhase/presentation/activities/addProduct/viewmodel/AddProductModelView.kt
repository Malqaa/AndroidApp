package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.HttpException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddProductModelView : BaseViewModel() {
    var confirmAddPorductRespObserver: MutableLiveData<AddProductResponse> = MutableLiveData()


    fun convertMap(map: HashMap<String, String>): HashMap<String, RequestBody> {
        val convertedMap = HashMap<String, RequestBody>()

        for ((key, value) in map) {
            // Create a RequestBody for each entry in the original map
            val requestBody = value.toRequestBody("text/plain".toMediaType())
            convertedMap[key] = requestBody
        }

        return convertedMap
    }

    private var callProduct: Call<AddProductResponse>? = null
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
        val map: HashMap<String, String> = HashMap()
        map["nameAr"] = nameAr
        map["nameEn"] = nameEn
        map["subTitleAr"] = subTitleAr
        map["subTitleEn"] = subTitleEn
        map["descriptionAr"] = descriptionAr
        map["descriptionEn"] = descriptionEn
        map["qty"] = qty
        if (productCondition != "0" && productCondition != "" && productCondition != "null")
            map["status"] = productCondition
        map["categoryId"] = categoryId
        map["countryId"] = countryId
        map["regionId"] = regionId
        map["neighborhoodId"] =
            neighborhoodId
        map["Street"] = Street
        if (productSep != null)
            productSep.let { map["productSep"] = Gson().toJson(it).toString() }
        else {
            map["productSep"] = "[]"
        }
        if (MainImageIndex == "")
            map["MainImageIndex"] = "0"
        else
            map["MainImageIndex"] = MainImageIndex

        map["AcceptQuestion"] = "false"
        map["IsFixedPriceEnabled"] = isFixedPriceEnabled.toString()
        map["IsAuctionEnabled"] = isAuctionEnabled.toString()
        map["IsNegotiationEnabled"] = isNegotiationEnabled.toString()
        if (price != "") {
            map["price"] = price
            map["priceDisc"] = priceDisc
        }

        map["IsCashEnabled"] = isCashEnabled
        map["AuctionStartPrice"] = auctionStartPrice
        map["DisccountEndDate"] = disccountEndDate
        map["AuctionMinimumPrice"] = auctionMinimumPrice

        map["SendYourAccountInfoToAuctionWinner"] =
            "false"
        if (ProductPaymentDetailsDto_AdditionalPakatId != "")
        map["ProductPaymentDetailsDto.AdditionalPakatId"] = pakatId

        if (ProductPaymentDetailsDto_ProductPublishPrice != 0f)
        map["ProductPaymentDetailsDto.ProductPublishPrice"] =
            ProductPaymentDetailsDto_ProductPublishPrice.toString()


        map["ProductPaymentDetailsDto.EnableFixedPriceSaleFee"] =
            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()

        map["ProductPaymentDetailsDto.FixedPriceSaleFee"] =
            (AddProductObjectData.selectedCategory?.enableFixedPriceSaleFee).toString()


        map["ProductPaymentDetailsDto.AuctionFee"] =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString()

        map["ProductPaymentDetailsDto.EnableAuctionFee"] =
            AddProductObjectData.selectedCategory?.enableAuctionFee.toString()

        if (ProductPaymentDetailsDto_EnableNegotiationFee != 0f) {
            map["ProductPaymentDetailsDto.NegotiationFee"] = "10.5".toString()
        }

//        if (ProductPaymentDetailsDto_EnableNegotiationFee != 0f) {
        map["ProductPaymentDetailsDto.EnableNegotiationFee"] =
            "10.5".toString()
//        }
//        if (ProductPaymentDetailsDto_ExtraProductImageFee != 0f) {
        map["ProductPaymentDetailsDto.ExtraProductImageFee"] =
            "1.5".toString()
//        }
//        if (ProductPaymentDetailsDto_ExtraProductVidoeFee != 0f) {
        map["ProductPaymentDetailsDto.ExtraProductVidoeFee"] =
            "1.5".toString()
//        }
//        if (ProductPaymentDetailsDto_SubTitleFee != 0f) {
        map["ProductPaymentDetailsDto.SubTitleFee"] =
            "1.4"
//        }
        if (ProductPaymentDetailsDto_CouponId != 0) {
            map["ProductPaymentDetailsDto.CouponId"] =
                ProductPaymentDetailsDto_CouponId.toString()

            map["ProductPaymentDetailsDto.CouponDiscountValue"] =
                ProductPaymentDetailsDto_CouponDiscountValue.toString()

        }
        map["ProductPaymentDetailsDto.TotalAmountAfterCoupon"] =
            ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString()

        if (ProductPaymentDetailsDto_TotalAmountBeforeCoupon != 0f) {
            map["ProductPaymentDetailsDto.TotalAmountBeforeCoupon"] =
                ProductPaymentDetailsDto_TotalAmountBeforeCoupon.toString()

        }
        if (validTime != null) {
            map["AuctionClosingTime"] = (validTime)
        }
        map["ProductPaymentDetailsDto.PaymentType"] = "Cash"
        map["AuctionNegotiatePrice"] = price

        if (isEdit) {
            map["id"] = AddProductObjectData.productId.toString()

            if (ConstantObjects.isRepost) {
                map["EditOrRepost"] = "2"
            } else {
                map["EditOrRepost"] = "1"
            }
        }


        convertMap(map)
        callProduct?.request().toString()
        callProduct = if (isEdit) {
            RetrofitBuilder.getRetrofitBuilder()
                .editProduct(
                    convertMap(map),
                    imageListTOSend,
                    shippingOptionsList,
                    deletedMediasList,
                    videoUrlList,
                    sendPaymentOptionList,
                    sendBankList
                )
        } else {
            RetrofitBuilder.getRetrofitBuilder()
                .addProduct3(
                    convertMap(map),
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