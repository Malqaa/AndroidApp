package com.malka.androidappp.newPhase.presentation.addressUser

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass.Companion.saveAddressTitle
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AddressViewModel : BaseViewModel() {
    var addUserAddressesListObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var userAddressesListObserver: MutableLiveData<UserAddressesResp> = MutableLiveData()
    var deleteUserAddressesObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
   var isLoadingDeleteAddress:MutableLiveData<Boolean> = MutableLiveData()
    fun getUserAddress() {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getListAddressesForUser()
            .enqueue(object : Callback<UserAddressesResp> {
                override fun onFailure(call: Call<UserAddressesResp>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<UserAddressesResp>,
                    response: Response<UserAddressesResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        userAddressesListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun deleteUSerAddress(addressId:Int) {
        isLoadingDeleteAddress.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .deleteUserAddress(addressId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoadingDeleteAddress.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoadingDeleteAddress.value = false
                    if (response.isSuccessful) {
                        deleteUserAddressesObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
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
        phone: String
    ) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["name"]= title.requestBody()
        map["title"]= title.requestBody()
        map["location"] = location.requestBody()
        map["street"] = street.requestBody()
        map["appartment"] = appartment.requestBody()
        map["floor"] = floor.requestBody()
        map["building"] = building.requestBody()
        map["lat"] = lat.requestBody()
        map["lng"] = lng.requestBody()
        map["phone"] = phone.requestBody()
        map["defaultAddress"] = "false".requestBody()
        println("hhhh " + map)
        println("hhhh "+ title+" "+location+" s "+street+" a "+appartment+" f "+floor+" b"+building+" "+lat+" "+lng+" "+phone)
        RetrofitBuilder.GetRetrofitBuilder()
            .addAddressForUser(map)
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
                        saveAddressTitle(title)
                        addUserAddressesListObserver.value = response.body()
                    } else {
                        println("hhhh "+Gson().toJson(getErrorResponse(response.code(),response.errorBody())))
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun editUserAddress(
        id:Int,
        title: String,
        location: String,
        street: String,
        appartment: String,
        floor: String,
        building: String,
        lat: String,
        lng: String,
        phone: String
    ) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["id"]= id.toString().requestBody()
        map["name"]= title.requestBody()
        map["title"]= title.requestBody()
        map["location"] = location.requestBody()
        map["street"] = street.requestBody()
        map["appartment"] = appartment.requestBody()
        map["floor"] = floor.requestBody()
        map["building"] = building.requestBody()
        map["lat"] = lat.requestBody()
        map["lng"] = lng.requestBody()
        map["phone"] = phone.requestBody()
        map["defaultAddress"] = "false".requestBody()
//        println("hhhh " + map)
        println("hhhh ud $id"+ title+" "+location+" s "+street+" a "+appartment+" f "+floor+" b"+building+" "+lat+" "+lng+" "+phone)
        RetrofitBuilder.GetRetrofitBuilder()
            .editAddressForUser(map)
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
                        addUserAddressesListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }
}