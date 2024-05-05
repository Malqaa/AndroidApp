package com.malqaa.androidappp.newPhase.presentation.fragments.nofication_fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.NotificationResp
import retrofit2.Call
import retrofit2.HttpException

class NotificationViewModel : BaseViewModel() {
    var notifyListRespObserver: MutableLiveData<NotificationResp> = MutableLiveData()
    private var callNotifyList: Call<NotificationResp>? = null

    fun getAllNotificationList(pageIndex:Int,rowCount:Int){
        isLoading.value=true

        callNotifyList = RetrofitBuilder.getRetrofitBuilder().getListNotifications(pageIndex,rowCount)
        callApi(callNotifyList!!,
            onSuccess = {
                Log.i("Notifications loaded","true")
                isLoading.value = false
                notifyListRespObserver.value = it
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
    fun closeAllCall() {
        if (callNotifyList != null) {
            callNotifyList?.cancel()
        }
    }
}