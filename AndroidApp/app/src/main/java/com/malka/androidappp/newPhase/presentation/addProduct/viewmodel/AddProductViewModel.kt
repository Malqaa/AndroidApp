package com.malka.androidappp.newPhase.presentation.addProduct.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import com.malka.androidappp.newPhase.domain.models.categoryResp.CategoriesResp
import com.malka.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malka.androidappp.newPhase.domain.models.pakatResp.PakatResp
import com.malka.androidappp.newPhase.domain.models.productTags.CategoryTagsResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import okhttp3.MediaType
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
    var configurationRespObserver: MutableLiveData<ConfigurationResp> = MutableLiveData()

    var confirmAddPorductRespObserver: MutableLiveData<GeneralResponse> = MutableLiveData()

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

    fun getConfigurationResp(configKey: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getConfigurationData(configKey)
            .enqueue(object : Callback<ConfigurationResp> {
                override fun onFailure(call: Call<ConfigurationResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ConfigurationResp>,
                    response: Response<ConfigurationResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        configurationRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getAddProduct(
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
        productSep: String,
        listImageFile: List<File>,//listImageFile
        MainImageIndex: String,
        videoUrl: String,
        PickUpDelivery: String,
        DeliveryOption: String,
    ) {
        isLoading.value = true
        val listOfImages = ArrayList<MultipartBody.Part>()
        for (i in listImageFile.indices) {
            listOfImages.add(prepareFilePart("listImageFile", listImageFile[i]))
        }
        println("hhh " + nameAr.requestBody())
//        println("hhh images "+listOfImages.size)
        RetrofitBuilder.GetRetrofitBuilder()
            .addProduct(
                nameAr.requestBody(),
                nameEn.requestBody(),
                subTitleAr.requestBody(),
                subTitleEn.requestBody(),
                descriptionAr.requestBody(),
                descriptionEn.requestBody(),
                qty.requestBody(),
                price.requestBody(),
                priceDisc.requestBody(),
                acceptQuestion.requestBody(),
                isNegotiationOffers.requestBody(),
                withFixedPrice.requestBody(),
                isMazad.requestBody(),
                isSendOfferForMazad.requestBody(),
                startPriceMazad.requestBody(),
                lessPriceMazad.requestBody(),
                mazadNegotiatePrice.requestBody(),
                mazadNegotiateForWhom.requestBody(),
                appointment.requestBody(),
                productCondition.requestBody(),
                categoryId.requestBody(),
                countryId.requestBody(),
                regionId.requestBody(),
                neighborhoodId.requestBody(),
                Street.requestBody(),
                GovernmentCode.requestBody(),
                pakatId.requestBody(),
                productSep.requestBody(),
                listOfImages,//listImageFile
                MainImageIndex.requestBody(),
                videoUrl.requestBody(),
                PickUpDelivery.requestBody(),
                DeliveryOption.requestBody(),
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
        productSep: String,
        listImageFile: List<Uri>,//listImageFile
        MainImageIndex: String,
        videoUrl: String,
        PickUpDelivery: String,
        DeliveryOption: String,
    ) {

        val map: HashMap<String, RequestBody> = HashMap()
        map["nameAr"] = nameAr.requestBody()
        map["nameEn"] = nameEn.requestBody()
        map["subTitleAr"] = subTitleAr.requestBody()
        map["subTitleEn"] = subTitleEn.requestBody()
        map["descriptionAr"] = descriptionAr.requestBody()
        map["descriptionEn"] = descriptionEn.requestBody()
        map["qty"] = qty.requestBody()
        map["price"] = price.requestBody()
       // map["priceDisc"] = priceDisc.requestBody()
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
        map["District"] = Street.requestBody()
        map["Street"] = Street.requestBody()
        //map["GovernmentCode"] = GovernmentCode.requestBody()
        map["pakatId"] = pakatId.requestBody()
        map["productSep"] = productSep.requestBody()
        map["MainImageIndex"] = MainImageIndex.requestBody()
        map["PickUpDelivery"] = PickUpDelivery.requestBody()
        map["DeliveryOption"] = DeliveryOption.requestBody()

        val listOfImages = ArrayList<MultipartBody.Part>()
        for (i in listImageFile.indices) {
            listOfImages.add(prepareFilePart("listImageFile", listImageFile[i], context))
            // listOfImages.add(prepareFilePart("listImageFile[$i]", listImageFile[i]))
        }
        println("hhhh " + map)
        println("hhhh catId [${categoryId}] countryId [${countryId}] region [${regionId}]  neighborhoodId [${neighborhoodId} pakaId [${pakatId}")
        RetrofitBuilder.GetRetrofitBuilder()
            .addProduct2(map)
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


}