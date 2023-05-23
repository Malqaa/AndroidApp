package com.malka.androidappp.newPhase.presentation.loginScreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralRespone
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.yariksoffice.lingver.Lingver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModel : BaseViewModel(){
    var userLoginObserver: MutableLiveData<LoginResp> = MutableLiveData()
   var forgetPasswordObserver:MutableLiveData<GeneralResponse> = MutableLiveData()
    fun signInUser(email:String,pass:String,context:Context) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .loginUser(email, pass, Lingver.getInstance().getLanguage())
            .enqueue(object : Callback<LoginResp> {
                override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<LoginResp>,
                    response: Response<LoginResp>
                ) {
                    if (response.isSuccessful) {
                        userLoginObserver.value=response.body()
                    }else{
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                    isLoading.value = false
                }
            })


    }
    fun forgetPassword(email:String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .forgetPassword(email)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        forgetPasswordObserver.value=response.body()
                    }else{
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                    isLoading.value = false
                }
            })


    }
    fun changePasswordAfterForget(email:String,otpCOde:String,password:String){
        isLoading.value = true
        var hashMap:HashMap<String,Any> = HashMap()
        hashMap["email"]=email
        hashMap["resetPasswordCode"]=otpCOde
        hashMap["newPassword"]=password
        RetrofitBuilder.GetRetrofitBuilder()
            .changePasswordAfterForget(hashMap)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        forgetPasswordObserver.value=response.body()
                    }else{
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                    isLoading.value = false
                }
            })

    }

}