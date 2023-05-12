package com.malka.androidappp.newPhase.presentation.account_fragment

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addWaletTransactionResp.AddWalletTranactionResp
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.ContactUsMessageResp
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.ConvertMoneyToPointResp
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.UserPointDataResp
import com.malka.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetailsResp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AccountViewModel: BaseViewModel()  {

    var walletDetailsObserver:MutableLiveData<WalletDetailsResp> = MutableLiveData()
    var userPointsDetailsObserver:MutableLiveData<UserPointDataResp> = MutableLiveData()
    var addWalletTransactionObserver:MutableLiveData<AddWalletTranactionResp> = MutableLiveData()
    var convertMoneyToPointObserver:MutableLiveData<ConvertMoneyToPointResp> = MutableLiveData()
    var productListObserver:MutableLiveData<ProductListResp> = MutableLiveData()
    var contactsMessageObserver:MutableLiveData<ContactUsMessageResp> = MutableLiveData()
    var technicalSupportMessageListObserver:MutableLiveData<TechnicalSupportMessageListResp> = MutableLiveData()
    fun getWalletDetailsInAccountTap(){
      //  isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getWalletDetails()
            .enqueue(object : Callback<WalletDetailsResp> {
                override fun onFailure(call: Call<WalletDetailsResp>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<WalletDetailsResp>,
                    response: Response<WalletDetailsResp>
                ) {
                    if (response.isSuccessful) {
                        walletDetailsObserver.value = response.body()
                    }
                }
            })
    }
    fun getWalletDetailsInWallet(){
          isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getWalletDetails()
            .enqueue(object : Callback<WalletDetailsResp> {
                override fun onFailure(call: Call<WalletDetailsResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<WalletDetailsResp>,
                    response: Response<WalletDetailsResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        walletDetailsObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun addWalletTransaction( transactionType:String, amount:String){
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["TransactionSource"] = ConstantObjects.transactionSource_chargeWallet.requestBody()
        map["TransactionType"] = transactionType.requestBody()
        map["TransactionAmount"] = amount.requestBody()
        RetrofitBuilder.GetRetrofitBuilder()
            .addWalletTransaction(map)
            .enqueue(object : Callback<AddWalletTranactionResp> {
                override fun onFailure(call: Call<AddWalletTranactionResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<AddWalletTranactionResp>,
                    response: Response<AddWalletTranactionResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addWalletTransactionObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getUserPointDetailsInAccountTap(){
        //  isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getUserPointsTransactions()
            .enqueue(object : Callback<UserPointDataResp> {
                override fun onFailure(call: Call<UserPointDataResp>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<UserPointDataResp>,
                    response: Response<UserPointDataResp>
                ) {
                    if (response.isSuccessful) {
                        userPointsDetailsObserver.value = response.body()
                    }
                }
            })
    }
    fun getUserPointDetailsInWallet(){
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getUserPointsTransactions()
            .enqueue(object : Callback<UserPointDataResp> {
                override fun onFailure(call: Call<UserPointDataResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<UserPointDataResp>,
                    response: Response<UserPointDataResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        userPointsDetailsObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun convertMountToPoints(amount: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .transferPointsToMoney(amount)
            .enqueue(object : Callback<ConvertMoneyToPointResp> {
                override fun onFailure(call: Call<ConvertMoneyToPointResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ConvertMoneyToPointResp>,
                    response: Response<ConvertMoneyToPointResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        convertMoneyToPointObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun grtLostProducts() {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getLostProducts()
            .enqueue(object : Callback<ProductListResp> {
                override fun onFailure(call: Call<ProductListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ProductListResp>,
                    response: Response<ProductListResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        productListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun addContactUsMessage(typeCommunication:Int,phoneNumber:String,email:String,title:String,message:String,id:Int?){
        isLoading.value = true
        val map: HashMap<String, Any> = HashMap()
        map["typeOfCommunication"] = typeCommunication
        map["problemTitle"] = title
        map["mobileNumber"] = phoneNumber
        map["email"] = email
        map["meassageDetails"] = message
        if(id!=null){
            map["id"] = id
        }

        RetrofitBuilder.GetRetrofitBuilder()
            .addEditContactUs(map)
            .enqueue(object : Callback<ContactUsMessageResp> {
                override fun onFailure(call: Call<ContactUsMessageResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ContactUsMessageResp>,
                    response: Response<ContactUsMessageResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        contactsMessageObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun getListContactUs() {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getListContactUs()
            .enqueue(object : Callback<TechnicalSupportMessageListResp> {
                override fun onFailure(call: Call<TechnicalSupportMessageListResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<TechnicalSupportMessageListResp>,
                    response: Response<TechnicalSupportMessageListResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        technicalSupportMessageListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.errorBody())
                    }
                }
            })
    }
}