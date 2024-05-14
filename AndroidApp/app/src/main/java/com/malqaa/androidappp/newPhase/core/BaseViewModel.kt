package com.malqaa.androidappp.newPhase.core

import android.provider.ContactsContract.Data
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.utils.ConstantObjects.Companion.configration_otpExpiryTime
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.ErrorAddOrder
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import com.malqaa.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

open class BaseViewModel : ViewModel() {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isloadingMore: MutableLiveData<Boolean> = MutableLiveData()
    var needToLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    var isNetworkFail: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()

    /***/
    var addProductToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var isNetworkFailProductToFav: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseObserverProductToFav: MutableLiveData<ErrorResponse> = MutableLiveData()
    var configurationRespObserver: MutableLiveData<ConfigurationResp> = MutableLiveData()
    var configurationRespDidNotReceive: MutableLiveData<ConfigurationResp> = MutableLiveData()

    private var callFav: Call<GeneralResponse>? = null
    private var callConfig: Call<ConfigurationResp>? = null

    fun getErrorResponse(code: Int, errorBody: ResponseBody?): ErrorResponse? {
        if (errorBody == null) return null

        return try {


            if (code == 400) {
                var productName = ""
                var quantity = ""
                val jsonString = errorBody.string()
                val jsonObject = JSONObject(jsonString)
                val gson = Gson()
                val response = gson.fromJson(jsonObject.toString(), ErrorResponse::class.java)

                println("Status Code: ${response.statusCode}")
                println("Message: ${response.message}")
                println("Status: ${response.status}")

                if (response.status == "ProductsOutOfStock") {
                    val list = response.data as List<ErrorAddOrder>
                    list.forEach { product ->
                        productName = product.productName
                        quantity = product.quantity.toString()

                        response.message =
                            response.message + " " + product.productName + " " + product.quantity
                //                        println("Product ID: ${product.produtId}, Name: ${product.productName}, Quantity: ${product.quantity}")
                    }
                }
                return ErrorResponse(
                    status = response.status,
                    message = response.message,
                    error = response.error,
                    result = response.result,
                    message2 = response.message2
                )

            } else {
                return ErrorResponse(
                    status = null,
                    message = null,
                    error = null,
                    result = null,
                    message2 = null
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            errorBody.close() // Close the response body to release its resources
        }
    }

    //    fun getErrorResponse(code: Int, errorBody: ResponseBody?): ErrorResponse? {
//        if (errorBody == null) return null
//
//        return try {
//            // Step 1: Convert the response body to a string
//            if (code == 400) {
//                val jsonString = errorBody.string()
//
//                // Step 2: Parse the JSON string to extract the error message
//                val jsonObject = JSONObject(jsonString)
//                var msg: String? = null
//                var status: String? = null
//                var result: String? = null
//                var message: String? = null
//                if (jsonObject.has("message")) {
//                    message = (jsonObject.getString("message") ?: "")
//                }
//                if (jsonObject.has("Message")) {
//                    msg = jsonObject.getString("Message")
//                }
//                if (jsonObject.has("result")) {
//                    result = jsonObject.getString("result")
//                }
//                if (jsonObject.has("status")) {
//                    status = jsonObject.getString("status")
//                }
//
//                if (status == "ProductsOutOfStock") {
//                    if (jsonObject.has("data")) {
//                        val errorAdd = jsonObject.getJSONObject("data") as ErrorAddOrder
//                        message = message + " " + errorAdd.productName + errorAdd.quantity
//                    }
//                }
//
//                if (message == null) {
//                    return ErrorResponse(
//                        status = status,
//                        message = msg,
//                        error = "error",
//                        result = result,
//                        message2 = msg
//                    )
//                } else
//                    return ErrorResponse(
//                        status = status,
//                        message = message,
//                        error = "error",
//                        result = result,
//                        message2 = msg
//                    )
//            } else {
//                return ErrorResponse(
//                    status = null,
//                    message = null,
//                    error = null,
//                    result = null,
//                    message2 = null
//                )
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        } finally {
//            errorBody.close() // Close the response body to release its resources
//        }
//    }


    fun addProductToFav(ProductID: Int) {
        isLoading.value = true
        callFav = getRetrofitBuilder().addProductToFav(ProductID)

        callApi(callFav!!,
            onSuccess = {
                isLoading.value = false
                addProductToFavObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->

                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFailProductToFav.value = throwable !is HttpException
                else {
                    errorResponseObserverProductToFav.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoading.value = false
                needToLogin.value = true
            })
    }

    fun getConfigurationResp(configKey: String) {
        isLoading.value = true
        callConfig = getRetrofitBuilder().getConfigurationData(configKey)

        callApi(callConfig!!,
            onSuccess = {
                isLoading.value = false
                if (configKey == configration_otpExpiryTime)
                    configurationRespObserver.value = it
                else
                    configurationRespDidNotReceive.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->

                isLoading.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFailProductToFav.value = throwable !is HttpException
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

    fun baseCancel() {
        if (callFav != null) {
            callFav?.cancel()
        }
        if (callConfig != null) {
            callConfig?.cancel()
        }
    }


}