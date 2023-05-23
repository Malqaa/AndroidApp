package com.malka.androidappp.newPhase.presentation.account_fragment.businessAccount

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountsListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class BusinessAccountViewModel : BaseViewModel()  {
    var businessAccountListObserver:MutableLiveData<BusinessAccountsListResp> = MutableLiveData<BusinessAccountsListResp>()

    fun getBusinessAccount(){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .gatAllBusinessAccounts()
            .enqueue(object : Callback<BusinessAccountsListResp> {
                override fun onFailure(call: Call<BusinessAccountsListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<BusinessAccountsListResp>,
                    response: Response<BusinessAccountsListResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        businessAccountListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

}