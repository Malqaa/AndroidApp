package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.paymentMethod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.BankTransfersListResponse
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.utils.SingleLiveEvent
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

class PaymentMethodViewModel : BaseViewModel() {

    var addBackAccountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var isLoadingBackAccountList: MutableLiveData<Boolean> = MutableLiveData()

    var _listBackAccountObserver = SingleLiveEvent<BankTransfersListResponse>()
    val listBackAccountObserver: LiveData<BankTransfersListResponse>
        get() = _listBackAccountObserver

    fun addBankAccountData(
        accountNumber: String,
        bankName: String? = null,
        bankHolderName: String,
        ibanNumber: String? = null,
        swiftCode: String? = null,
        expiryDate: String,
        ibanCertificate: String? = null,
        ibanCertificateFile: File? = null,
        saveForLaterUse: Boolean,
        paymentAccountType: String
    ) {
        isLoading.value = true

        // Helper to create plain text request body
        fun String.toPlainRequestBody(): RequestBody =
            toRequestBody("text/plain".toMediaTypeOrNull())

        // Prepare file part only if file is not null
        val filePart = ibanCertificateFile?.let { file ->
            val mediaType = "application/pdf".toMediaTypeOrNull()
            val fileRequestBody = file.asRequestBody(mediaType)
            MultipartBody.Part.createFormData(
                name = "IbanCertificateFile",
                filename = file.name,
                body = fileRequestBody
            )
        }

        // Enqueue network call
        getRetrofitBuilder().addBankAccount(
            accountNumber = accountNumber.toPlainRequestBody(),
            bankName = (bankName ?: "").toPlainRequestBody(), // handle null
            bankHolderName = bankHolderName.toPlainRequestBody(),
            ibanNumber = (ibanNumber ?: "").toPlainRequestBody(), // handle null
            swiftCode = (swiftCode ?: "").toPlainRequestBody(), // handle null
            expiryDate = expiryDate.toPlainRequestBody(),
            ibanCertificate = (ibanCertificate ?: "").toPlainRequestBody(),
            file = filePart,
            saveForLaterUse = saveForLaterUse.toString().toPlainRequestBody(),
            paymentAccountType = paymentAccountType.toPlainRequestBody()
        ).enqueue(object : Callback<GeneralResponse> {
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
                    addBackAccountObserver.value = response.body()
                } else {
                    errorResponseObserver.value =
                        getErrorResponse(response.code(), response.errorBody())
                }
            }
        })
    }


    var editBankAccountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()


    fun editBankTransferData(
        id: String,
        accountNumber: String,
        bankName: String,
        bankHolderName: String,
        ibanNumber: String,
        swiftCode: String,
        expiryDate: String,
        ibanCertificate: String? = null,
        ibanCertificateFile: File? = null,
        saveForLaterUse: Boolean,
        paymentAccountType: String
    ) {
        isLoading.value = true

        fun String.toPlainRequestBody(): RequestBody =
            toRequestBody("text/plain".toMediaTypeOrNull())

        val filePart = ibanCertificateFile?.let { file ->
            val mediaType = "application/pdf".toMediaTypeOrNull()
            val fileRequestBody = file.asRequestBody(mediaType)
            MultipartBody.Part.createFormData(
                name = "IbanCertificateFile",
                filename = file.name,
                body = fileRequestBody
            )
        }

        getRetrofitBuilder().editBankTransfer(
            id = id.toPlainRequestBody(),
            accountNumber = accountNumber.toPlainRequestBody(),
            bankName = bankName.toPlainRequestBody(),
            bankHolderName = bankHolderName.toPlainRequestBody(),
            ibanNumber = ibanNumber.toPlainRequestBody(),
            swiftCode = swiftCode.toPlainRequestBody(),
            expiryDate = expiryDate.toPlainRequestBody(),
            ibanCertificate = (ibanCertificate ?: "").toPlainRequestBody(),
            file = filePart,
            saveForLaterUse = saveForLaterUse.toString().toPlainRequestBody(),
            paymentAccountType = paymentAccountType.toPlainRequestBody()
        ).enqueue(object : Callback<GeneralResponse> {
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
                    editBankAccountObserver.value = response.body()
                } else {
                    errorResponseObserver.value =
                        getErrorResponse(response.code(), response.errorBody())
                }
            }
        })
    }

    fun getBankAccountsList(
        paymentAccountType: Int? = null
    ) {
        isLoadingBackAccountList.value = true
        getRetrofitBuilder()
            .bankTransfersList(paymentAccountType = paymentAccountType)
            .enqueue(object : Callback<BankTransfersListResponse> {
                override fun onFailure(call: Call<BankTransfersListResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoadingBackAccountList.value = false
                }

                override fun onResponse(
                    call: Call<BankTransfersListResponse>,
                    response: Response<BankTransfersListResponse>
                ) {
                    isLoadingBackAccountList.value = false
                    if (response.isSuccessful) {
                        _listBackAccountObserver.postValue(response.body())
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    var deleteBankAccountObserver: MutableLiveData<GeneralResponse> = MutableLiveData()

    fun deleteBankTransfer(id: Int) {
        isLoading.value = true

        getRetrofitBuilder().removeBankTransfer(id)
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
                        deleteBankAccountObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun closeAllCall() {

    }
}