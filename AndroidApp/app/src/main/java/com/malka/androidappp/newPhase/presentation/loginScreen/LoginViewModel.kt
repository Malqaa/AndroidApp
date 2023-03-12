package com.malka.androidappp.newPhase.presentation.loginScreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.yariksoffice.lingver.Lingver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModel : BaseViewModel(){
    var userLoginObserver: MutableLiveData<LoginResp> = MutableLiveData()

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

}