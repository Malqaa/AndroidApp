package com.malqaa.androidappp.newPhase.data.network

import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.domain.models.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> callApi(call: Call<T>, onSuccess: (T) -> Unit, onFailure: (Throwable?,Int, ResponseBody?) -> Unit, goLogin: () -> Unit){
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {

            if (response.isSuccessful) {
                onSuccess(response.body()!!)
            } else {
                if(response.code()==401){
                    goLogin()
                }else
                onFailure(null ,response.code(), response.errorBody())
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure(t,0,null)
        }
    })
}

fun cancelApiCall(call: Call<*>) {
    call.cancel()
}

