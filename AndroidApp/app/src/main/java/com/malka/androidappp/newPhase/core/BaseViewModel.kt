package com.malka.androidappp.newPhase.core

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.malka.androidappp.newPhase.domain.models.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

open class BaseViewModel : ViewModel() {
    val STATUS_UNAUTHORIZED = 401
    val STATUS_INVALID_TOKEN = 403
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isloadingMore: MutableLiveData<Boolean> = MutableLiveData()
    var isNetworkFail: MutableLiveData<Boolean> = MutableLiveData()
    var errorResponseObserver: MutableLiveData<ErrorResponse> = MutableLiveData()


    fun getErrorResponse(body: ResponseBody?): ErrorResponse {
        try {
            val adapter: TypeAdapter<ErrorResponse> =
                Gson().getAdapter<ErrorResponse>(ErrorResponse::class.java)
            return adapter.fromJson(body?.string())
        } catch (e: Exception) {
            return ErrorResponse()
        }
    }

}