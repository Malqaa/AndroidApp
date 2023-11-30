package com.malka.androidappp.newPhase.data.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> callApi(call: Call<T>, onResponse: (T) -> Unit, onFailure: (Throwable) -> Unit, goLogin: () -> Unit){
    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onResponse(response.body()!!)
            } else {
                if(response.code()==401){
                    goLogin()
                }else
                onFailure(Throwable("Unsuccessful response: ${response.code()}"))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onFailure(t)
        }
    })
}

