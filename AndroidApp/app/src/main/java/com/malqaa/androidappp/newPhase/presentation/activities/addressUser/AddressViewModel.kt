package com.malqaa.androidappp.newPhase.presentation.activities.addressUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.HttpException

class AddressViewModel : BaseViewModel() {

    private var _userMessage: MutableLiveData<String?> = MutableLiveData(null)
    val userMessage: MutableLiveData<String?> = _userMessage

    var addUserAddressesListObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var userAddressesListObserver: MutableLiveData<UserAddressesResp> = MutableLiveData()
    var deleteUserAddressesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var isLoadingDeleteAddress: MutableLiveData<Boolean> = MutableLiveData()

    private var callUserAddress: Call<UserAddressesResp>? = null
    private var callDeleteAddress: Call<GeneralResponse>? = null
    private var callAddUserAddress: Call<GeneralResponse>? = null
    private var callEditUserAddress: Call<GeneralResponse>? = null

    fun closeAllCall() {
        if (callUserAddress != null) {
            callUserAddress?.cancel()
        }
        if (callDeleteAddress != null) {
            callDeleteAddress?.cancel()
        }
        if (callAddUserAddress != null) {
            callAddUserAddress?.cancel()
        }
        if (callEditUserAddress != null) {
            callEditUserAddress?.cancel()
        }
    }

    fun getUserAddress() {
        isLoading.value = true
        callUserAddress = getRetrofitBuilder().getListAddressesForUser()
        callApi(callUserAddress!!,
            onSuccess = {
                isLoading.value = false
                userAddressesListObserver.value = it
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

    fun deleteUSerAddress(addressId: Int) {
        isLoadingDeleteAddress.value = true
        callDeleteAddress = getRetrofitBuilder().deleteUserAddress(addressId)
        callApi(callDeleteAddress!!,
            onSuccess = {
                isLoadingDeleteAddress.value = false
                deleteUserAddressesObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoadingDeleteAddress.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoadingDeleteAddress.value = false
                needToLogin.value = true
            })

    }

    fun addUserAddress(
        title: String,
        location: String,
        street: String,
        appartment: String,
        floor: String,
        building: String,
        lat: String,
        lng: String,
        phone: String,
        defaultAddress: Boolean? = false
    ) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["name"] = title.requestBody()
        map["title"] = title.requestBody()
        map["location"] = location.requestBody()
        map["street"] = street.requestBody()
        map["appartment"] = appartment.requestBody()
        map["floor"] = floor.requestBody()
        map["building"] = building.requestBody()
        map["lat"] = lat.requestBody()
        map["lng"] = lng.requestBody()
        map["phone"] = phone.requestBody()
        map["defaultAddress"] = defaultAddress.toString().requestBody()
        getRetrofitBuilder()
            .addAddressForUser(map)

        callAddUserAddress = getRetrofitBuilder().addAddressForUser(map)
        callApi(callAddUserAddress!!,
            onSuccess = {
                isLoading.value = false
                addUserAddressesListObserver.value = it
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

    fun editUserAddress(
        id: Int,
        title: String,
        location: String,
        street: String,
        appartment: String,
        floor: String,
        building: String,
        lat: String,
        lng: String,
        phone: String,
        defaultAddress: Boolean? = false
    ) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["id"] = id.toString().requestBody()
        map["name"] = title.requestBody()
        map["title"] = title.requestBody()
        map["location"] = location.requestBody()
        map["street"] = street.requestBody()
        map["appartment"] = appartment.requestBody()
        map["floor"] = floor.requestBody()
        map["building"] = building.requestBody()
        map["lat"] = lat.requestBody()
        map["lng"] = lng.requestBody()
        map["phone"] = phone.requestBody()
        map["defaultAddress"] = defaultAddress.toString().requestBody()
        getRetrofitBuilder()
            .editAddressForUser(map)

        callEditUserAddress = getRetrofitBuilder().editAddressForUser(map)
        callApi(callEditUserAddress!!,
            onSuccess = {
                isLoading.value = false
                addUserAddressesListObserver.value = it
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

    fun setDefaultAddress(addressId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val defaultAddress = getRetrofitBuilder().setDefaultAddress(addressId = addressId)
                callApi(
                    defaultAddress,
                    onSuccess = {
                        isLoading.value = false
                        _userMessage.value = it.message
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
    }
}