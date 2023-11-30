package com.malka.androidappp.newPhase.presentation.accountFragment

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addWaletTransactionResp.AddWalletTranactionResp
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.ContactUsMessageResp
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageListResp
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.accountProfile.AccountInfo
import com.malka.androidappp.newPhase.domain.models.editProfileResp.EditProfileResp
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.ConvertMoneyToPointResp
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.UserPointDataResp
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import com.malka.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetailsResp
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
import java.lang.Exception

class AccountViewModel : BaseViewModel() {

    var walletDetailsObserver: MutableLiveData<WalletDetailsResp> = MutableLiveData()
    var userPointsDetailsObserver: MutableLiveData<UserPointDataResp> = MutableLiveData()
    var addWalletTransactionObserver: MutableLiveData<AddWalletTranactionResp> = MutableLiveData()
    var convertMoneyToPointObserver: MutableLiveData<ConvertMoneyToPointResp> = MutableLiveData()
    var productListObserver: MutableLiveData<ProductListResp> = MutableLiveData()
    var contactsMessageObserver: MutableLiveData<ContactUsMessageResp> = MutableLiveData()
    var technicalSupportMessageListObserver: MutableLiveData<TechnicalSupportMessageListResp> =
        MutableLiveData()
    var editProfileImageObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var getUserDataObserver: MutableLiveData<LoginResp> = MutableLiveData()
    var changePasswordObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var changeEmailObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var confirmChangeEmailOtpObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var accountInfoObserver: MutableLiveData<AccountInfo> = MutableLiveData()
    fun editProfileImage( multipartBody: MultipartBody.Part?) {
        isLoading.value = true


//        val path = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        val file =  File(path, "DemoPicture.jpg");
//
//        try {
//            // Make sure the Pictures directory exists.
//            path.mkdirs();
//        }catch (e:Exception){
//
//        }
//        var multipartBody: MultipartBody.Part = if (file != null) {
//            var requestbody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//            MultipartBody.Part.createFormData("imgProfile", file.name, requestbody)
//        } else {
//            MultipartBody.Part.createFormData("imgProfile", "null", "null".toRequestBody())
//        }

        RetrofitBuilder.GetRetrofitBuilder()
            .editProfileImage(multipartBody)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    println("hhhh " + t.message)
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        editProfileImageObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getAccountInfo() {
        RetrofitBuilder.GetRetrofitBuilder()
            .getMyAccountInfo()
            .enqueue(object : Callback<AccountInfo> {
                override fun onFailure(call: Call<AccountInfo>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<AccountInfo>,
                    response: Response<AccountInfo>
                ) {
                    if (response.isSuccessful) {
                        accountInfoObserver.value = response.body()
                    }
                }
            })
    }

    fun getWalletDetailsInAccountTap() {
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

    fun getWalletDetailsInWallet() {
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
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun addWalletTransaction(transactionSource:String,transactionType: String, amount: String) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["TransactionSource"] = transactionSource.requestBody()
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
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getUserPointDetailsInAccountTap() {
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

    fun getUserPointDetailsInWallet() {
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
                            getErrorResponse(response.code(), response.errorBody())
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
                            getErrorResponse(response.code(), response.errorBody())
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
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun addContactUsMessage(
        typeCommunication: Int,
        phoneNumber: String,
        email: String,
        title: String,
        message: String,
        id: Int?
    ) {
        isLoading.value = true
        val map: HashMap<String, Any> = HashMap()
        map["typeOfCommunication"] = typeCommunication
        map["problemTitle"] = title
        map["mobileNumber"] = phoneNumber
        map["email"] = email
        map["meassageDetails"] = message
        if (id != null) {
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
                            getErrorResponse(response.code(), response.errorBody())
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
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getUserData() {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getUserData()
            .enqueue(object : Callback<LoginResp> {
                override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<LoginResp>,
                    response: Response<LoginResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getUserDataObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun getUserDataForAccountTap() {
        RetrofitBuilder.GetRetrofitBuilder()
            .getUserData()
            .enqueue(object : Callback<LoginResp> {
                override fun onFailure(call: Call<LoginResp>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<LoginResp>,
                    response: Response<LoginResp>
                ) {
                    if (response.isSuccessful) {
                        getUserDataObserver.value = response.body()
                    }
                }
            })
    }

    fun changeUserPassword(loggedUserid: String, currentPassword: String, newPassword: String) {
        isLoading.value = true
        var data: HashMap<String, Any> = HashMap()
        data["userId"] = loggedUserid
        data["oldPassword"] = currentPassword
        data["newPassword"] = newPassword
        RetrofitBuilder.GetRetrofitBuilder()
            .editProfileChangePassword(data)
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
                        changePasswordObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun changeUserEmail(newEmail: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .editProfileChangeEmail(newEmail)
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
                        changeEmailObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun changeUserEmail(newEmail: String, otp: String) {

        isLoading.value = true
        var data: HashMap<String, Any> = HashMap()
        data["resetPasswordCode"] = otp
        data["newEmail"] = newEmail
        RetrofitBuilder.GetRetrofitBuilder()
            .confirmChangeEmail(data)
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
                        confirmChangeEmailOtpObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    /****/
    var validateAndGenerateOTPObserver: MutableLiveData<ValidateAndGenerateOTPResp> =
        MutableLiveData()
    var updateUserMobielNumberObserver: MutableLiveData<GeneralResponse> =
        MutableLiveData()

    fun resendOtp(userPhone: String, language: String, otpType: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .resendOtp(userPhone, otpType, language)
            .enqueue(object : Callback<ValidateAndGenerateOTPResp> {
                override fun onResponse(
                    call: Call<ValidateAndGenerateOTPResp>,
                    response: Response<ValidateAndGenerateOTPResp>
                ) {
                    if (response.isSuccessful) {
                        validateAndGenerateOTPObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<ValidateAndGenerateOTPResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })
    }

    fun upddateMobileNumber(userPhone: String, userId: String, otpCode: String) {
        isLoading.value = true
        var data: HashMap<String, Any> = HashMap()
        data["id"] = userId
        data["mobileNumber"] = userPhone
        data["otpCode"] = otpCode
        RetrofitBuilder.GetRetrofitBuilder()
            .updateUserMobileNumber(data).enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        updateUserMobielNumberObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })
    }

    /****/
    var updateProfileDataObserver: MutableLiveData<EditProfileResp> =
        MutableLiveData()

    fun updateMobileNumber(
        userId: String,
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        gender: Int,
        showUserInformation: String
    ) {
        isLoading.value = true
        var data: HashMap<String, Any> = HashMap()
        data["id"] = userId
        data["firstName"] = firstName
        data["lastName"] = lastName
        data["dateOfBirth"] = dateOfBirth
        data["gender"] = gender
        data["showUserInformation"] = showUserInformation
        RetrofitBuilder.GetRetrofitBuilder()
            .updateAccountProfile(data).enqueue(object : Callback<EditProfileResp> {
                override fun onResponse(
                    call: Call<EditProfileResp>,
                    response: Response<EditProfileResp>
                ) {
                    if (response.isSuccessful) {
                        updateProfileDataObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<EditProfileResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })
    }
}