package com.malka.androidappp.newPhase.presentation.addProduct.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.accountBackListResp.AccountBankListResp
import com.malka.androidappp.newPhase.domain.models.cartPriceSummery.CartPriceSummeryResp
import com.malka.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malka.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
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


class AddProductViewModel : BaseViewModel() {

    var getListCategoriesByProductNameObserver: MutableLiveData<CategoryTagsResp> =
        MutableLiveData()
    var getDynamicSpecificationObserver: MutableLiveData<DynamicSpecificationResp> =
        MutableLiveData()
    var getPakatRespObserver: MutableLiveData<PakatResp> = MutableLiveData()
    var categoriesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var categoriesErrorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()
    var isLoadingAllCategory: MutableLiveData<Boolean> = MutableLiveData()
    var categoryListObserver: MutableLiveData<CategoriesResp> = MutableLiveData()


    var confirmAddPorductRespObserver: MutableLiveData<AddProductResponse> = MutableLiveData()

    var addBackAccountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var listBackAccountObserver: MutableLiveData<AccountBankListResp> = MutableLiveData()
    var isLoadingBackAccountList: MutableLiveData<Boolean> = MutableLiveData()

    var cartPriceSummeryObserver: MutableLiveData<CartPriceSummeryResp> = MutableLiveData()
    fun checkOutAdditionalPakat(
        pakatId: Int, categoryId: Int,
        extraProductImageFee: Float,
        extraProductVidoeFee: Float,
        subTitleFee: Float
    ) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .checkOutAdditionalPakat(pakatId, categoryId,extraProductImageFee,extraProductVidoeFee,subTitleFee)
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
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getListCategoriesByProductName(productName: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getListCategoriesByProductName(productName)
            .enqueue(object : Callback<CategoryTagsResp> {
                override fun onFailure(call: Call<CategoryTagsResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<CategoryTagsResp>,
                    response: Response<CategoryTagsResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getListCategoriesByProductNameObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getSubCategoriesByCategoryID(categoryId: Int) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getSubCategoryByMainCategory2(categoryId.toString())
            .enqueue(object : Callback<CategoriesResp> {
                override fun onFailure(call: Call<CategoriesResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<CategoriesResp>,
                    response: Response<CategoriesResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        categoryListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getDynamicSpecification(categoryId: Int) {
        isLoading.value = true
        println("hhhh $categoryId")
        RetrofitBuilder.GetRetrofitBuilder()
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
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getPakatList(categoryId: Int) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getAllCategories() {
        isLoadingAllCategory.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                            getErrorResponse(response.errorBody())
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
        RetrofitBuilder.GetRetrofitBuilder()
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
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getBankAccountsList() {
        isLoadingBackAccountList.value = true
        RetrofitBuilder.GetRetrofitBuilder()
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
                            getErrorResponse(response.errorBody())
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
//        RetrofitBuilder.GetRetrofitBuilder()
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
//            .enqueue(object : Callback<GeneralResponse> {
//                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
//                    isNetworkFail.value = t !is HttpException
//                    isLoading.value = false
//                }
//
//                override fun onResponse(
//                    call: Call<GeneralResponse>,
//                    response: Response<GeneralResponse>
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
//                            getErrorResponse(response.errorBody())
//                    }
//                }
//            })
//    }
fun getAddProduct3(
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
    DeliveryOption: List<String>,
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
    backAccountId: Int,

    ) {
    isLoading.value = true
    var imageListTOSend: ArrayList<MultipartBody.Part> = ArrayList()
    for (file in listImageFile) {
        var multipartBody: MultipartBody.Part = if (file != null) {
            var requestbody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("listImageFile", file.name, requestbody)
        } else {
            MultipartBody.Part.createFormData("listImageFile", "null", "null".toRequestBody())
        }
        imageListTOSend.add(multipartBody)
    }
    var shippingOptionsList: ArrayList<MultipartBody.Part> = ArrayList()

    for(item in DeliveryOption){
       // var requestbody: RequestBody = item.requestBody()
        var multipartBody: MultipartBody.Part =   MultipartBody.Part.createFormData("ShippingOptions",item)
            shippingOptionsList.add(multipartBody)
    }
    var videoUrlList: ArrayList<MultipartBody.Part> = ArrayList()
    videoUrl?.let {
        for (item in videoUrl) {
            // var requestbody: RequestBody = item.requestBody()
            var multipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("videoUrl", item)
            videoUrlList.add(multipartBody)
        }

    }
    var sendPaymentOptionList: ArrayList<MultipartBody.Part> = ArrayList()
    paymentOptionIdList?.let {
        for (item in paymentOptionIdList) {
            // var requestbody: RequestBody = item.requestBody()
            var multipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("PaymentOptions", item.toString())
            sendPaymentOptionList.add(multipartBody)
        }

    }


    val map: HashMap<String, RequestBody> = HashMap()
    map["nameAr"] = nameAr.requestBody()
    map["nameEn"] = nameEn.requestBody()
    map["subTitleAr"] = subTitleAr.requestBody()
    map["subTitleEn"] = subTitleEn.requestBody()
    map["descriptionAr"] = descriptionAr.requestBody()
    map["descriptionEn"] = descriptionEn.requestBody()
    map["qty"] = qty.requestBody()
    // map["appointment"]="".requestBody()
    map["status"] = productCondition.requestBody()
    map["categoryId"] = categoryId.requestBody()
    map["countryId"] = countryId.requestBody()
    map["regionId"] = regionId.requestBody()
    map["neighborhoodId"] = neighborhoodId.requestBody()
    //  map["District"] = Street.requestBody()
    //  map["Street"] = Street.requestBody()
    //map["GovernmentCode"] = GovernmentCode.requestBody()
    if (pakatId != "")
        map["pakatId"] = pakatId.requestBody()
    productSep.let {
        map["productSep"] = Gson().toJson(it).toString().requestBody()
    }
    map["MainImageIndex"] = MainImageIndex.requestBody()
   // map["videoUrl"] = videoUrl.toString().requestBody()
    map["PickUpDelivery"] = PickUpDelivery.requestBody()
   // map["ShippingOptions"] = DeliveryOption.toString().requestBody()
    println("hhhh d "+DeliveryOption.toString())
//    map["Lat"] = "".requestBody()
//    map["Lon"] = "".requestBody()
    // map["AcceptQuestion"]="".requestBody()
    map["IsFixedPriceEnabled"] = isFixedPriceEnabled.toString().requestBody()
    map["IsAuctionEnabled"] = isAuctionEnabled.toString().requestBody()
    map["IsNegotiationEnabled"] = isNegotiationEnabled.toString().requestBody()
    map["price"] = price.requestBody()
    map["priceDisc"] = price.requestBody()

    //map["IsCashEnabled"] = isCashEnabled.toRequestBody()
    map["AuctionStartPrice"] = auctionStartPrice.toRequestBody()
    // map["DisccountEndDate"] = disccountEndDate.toRequestBody()
    //map["IsAuctionPaied"]="".requestBody()
    //map["SendOfferForAuction"]="".requestBody()
    map["AuctionMinimumPrice"] = auctionMinimumPrice.toRequestBody()
    //map["AuctionNegotiateForWhom"]="".requestBody()
    //map["AuctionNegotiatePrice]="".requestBody()
    map["AuctionClosingTime"] = auctionClosingTime.toRequestBody()
    // map["HighestBidPrice"]="".toRequestBody()
    if (backAccountId != 0) {
        map["BankAccountId"] = backAccountId.toString().toRequestBody()
    }
    RetrofitBuilder.GetRetrofitBuilder()
        .addProduct3(map, imageListTOSend, shippingOptionsList, videoUrlList, sendPaymentOptionList)
        .enqueue(object : Callback<AddProductResponse> {
            override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                println(
                    "hhhh " + t.message
                )
                isNetworkFail.value = t !is HttpException
                isLoading.value = false
            }

            override fun onResponse(
                call: Call<AddProductResponse>,
                response: Response<AddProductResponse>
            ) {
                isLoading.value = false
                if (response.isSuccessful) {
                    confirmAddPorductRespObserver.value = response.body()
                } else {
                    println(
                        "hhhh " + response.code() + " " + Gson().toJson(response.errorBody())
                            .toString()+ " "+Gson().toJson(getErrorResponse(response.errorBody()))
                    )
                    errorResponseObserver.value =
                        getErrorResponse(response.errorBody())
                }
            }
        })
}

    fun getAddProduct2(
        context: Context,
        nameAr: String,
        nameEn: String,
        subTitleAr: String,
        subTitleEn: String,
        descriptionAr: String,
        descriptionEn: String,
        qty: String,
        price: String,
        priceDisc: String,
        acceptQuestion: String,
        isNegotiationOffers: String,
        withFixedPrice: String,
        isMazad: String,
        isSendOfferForMazad: String,
        startPriceMazad: String,
        lessPriceMazad: String,
        mazadNegotiatePrice: String,
        mazadNegotiateForWhom: String,
        appointment: String,
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
        DeliveryOption: String,
    ) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["nameAr"] = nameAr.requestBody()
        map["nameEn"] = nameEn.requestBody()
        map["subTitleAr"] = subTitleAr.requestBody()
        map["subTitleEn"] = subTitleEn.requestBody()
        map["descriptionAr"] = descriptionAr.requestBody()
        map["descriptionEn"] = descriptionEn.requestBody()
        map["qty"] = qty.requestBody()
        map["price"] = price.requestBody()
        map["priceDisc"] = "0".requestBody()
        //  map["acceptQuestion"]=acceptQuestion.requestBody()
        map["isNegotiationOffers"] = isNegotiationOffers.requestBody()
        map["withFixedPrice"] = withFixedPrice.requestBody()
        map["isMazad"] = isMazad.requestBody()
        map["isSendOfferForMazad"] = isSendOfferForMazad.requestBody()
        map["startPriceMazad"] = "0".requestBody()
        map["lessPriceMazad"] = "0".requestBody()
        map["mazadNegotiatePrice"] = mazadNegotiatePrice.requestBody()
        //  map["mazadNegotiateForWhom"]=mazadNegotiateForWhom.requestBody()
        //  map["appointment"]=appointment.requestBody()
        map["status"] = productCondition.requestBody()
        map["categoryId"] = categoryId.requestBody()
        map["countryId"] = countryId.requestBody()
        map["regionId"] = regionId.requestBody()
        map["neighborhoodId"] = neighborhoodId.requestBody()
        //  map["District"] = Street.requestBody()
        //  map["Street"] = Street.requestBody()
        //map["GovernmentCode"] = GovernmentCode.requestBody()
        if (pakatId != "")
            map["pakatId"] = pakatId.requestBody()
        productSep.let {
            map["productSep"] = Gson().toJson(it).toString().requestBody()
        }

        // map["productSep"] =
        //          "[{HeaderSpeAr:\"colorAr\",HeaderSpeEn:\"colorEn\", ValueSpeAr:\"redAr\", ValueSpeEn:\"redEn\", Type:1},{HeaderSpeAr:\"colorAr\",HeaderSpeEn:\"colorEn\", ValueSpeAr:\"redAr\", ValueSpeEn:\"redEn\", Type:1}]".requestBody()
        map["MainImageIndex"] = MainImageIndex.requestBody()
        map["PickUpDelivery"] = PickUpDelivery.requestBody()
        map["DeliveryOption"] = DeliveryOption.requestBody()


//        val listOfImages = ArrayList<MultipartBody.Part>()
//        for (i in listImageFile.indices) {
//            listOfImages.add(prepareFilePart("listImageFile", listImageFile[i], context))
//            // map["listImageFile"] = HelpFunctions.getFileImage(listImageFile[i], context).asRequestBody()
//            //  listOfImages.add(prepareFilePart2("listImageFile", listImageFile[i], context))
//            //listOfImages.add(prepareFilePart("listImageFile[$i]", listImageFile[i]))
//        }
//
////        println("hhhh " + map)
////        println(
////            "hhhh catId [${categoryId}] countryId [${countryId}] region [${regionId}]  neighborhoodId [${neighborhoodId} pakaId [${pakatId}  productSep $" +
////                    "$productSep"
////        )
        RetrofitBuilder.GetRetrofitBuilder()
            .addProduct2(map)
            .enqueue(object : Callback<AddProductResponse> {
                override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddProductResponse>,
                    response: Response<AddProductResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        confirmAddPorductRespObserver.value = response.body()
                    } else {
                        println(
                            "hhhh " + response.code() + " " + Gson().toJson(response.errorBody())
                                .toString()
                        )
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }


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


}