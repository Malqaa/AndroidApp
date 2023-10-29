package com.malka.androidappp.newPhase.presentation.cartActivity

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class InvoiceViewModel : BaseViewModel() {
    var confirmImageObserver: MutableLiveData<GeneralResponse> = MutableLiveData()



    fun confirmBankTransferPayment(orderId:Int,file: File?) {
        isLoading.value = true
        var multipartBody: MultipartBody.Part = if (file != null) {
            var requestbody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("orderInvoice", file.name, requestbody)
        } else {
            MultipartBody.Part.createFormData("orderInvoice", "null", "null".toRequestBody())
        }

        val map: HashMap<String, RequestBody> = HashMap()
        map["orderId"] = orderId.toString().requestBody()
        RetrofitBuilder.GetRetrofitBuilder()
            .confirmBankTransferPayment(map ,multipartBody)
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
                        confirmImageObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

}