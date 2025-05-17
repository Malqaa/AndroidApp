package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment

import androidx.lifecycle.MutableLiveData
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.accountProfile.AccountInfo
import com.malqaa.androidappp.newPhase.domain.models.addWaletTransactionResp.AddWalletTranactionResp
import com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage.ContactUsMessageResp
import com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageListResp
import com.malqaa.androidappp.newPhase.domain.models.editProfileResp.EditProfileResp
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginResp
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LogoutResp
import com.malqaa.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.ConvertMoneyToPointResp
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.UserPointDataResp
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.TransactionPointsAmountRes
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetailsResp
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

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

    var updateProfileDataObserver: MutableLiveData<EditProfileResp> =
        MutableLiveData()

    var validateAndGenerateOTPObserver: MutableLiveData<ValidateAndGenerateOTPResp> =
        MutableLiveData()
    var updateUserMobielNumberObserver: MutableLiveData<GeneralResponse> =
        MutableLiveData()
    var logoutObserver: MutableLiveData<LogoutResp> = MutableLiveData()

    var transferWalletToPointsObserver: MutableLiveData<TransactionPointsAmountRes> = MutableLiveData()


    private var callEditProfile: Call<GeneralResponse>? = null
    private var callAccountInfo: Call<AccountInfo>? = null
    private var callWalletDetails: Call<WalletDetailsResp>? = null
    private var callAddWallet: Call<AddWalletTranactionResp>? = null
    private var callUserPointDetails: Call<UserPointDataResp>? = null
    private var callConvertMount: Call<ConvertMoneyToPointResp>? = null
    private var callLostProducts: Call<ProductListResp>? = null
    private var callAddContact: Call<ContactUsMessageResp>? = null
    private var callListContactUs: Call<TechnicalSupportMessageListResp>? = null
    private var callUserData: Call<LoginResp>? = null
    private var callChangeUserPassword: Call<GeneralResponse>? = null
    private var callChangeUserEmail: Call<GeneralResponse>? = null
    private var callChangeUserEmailOtp: Call<GeneralResponse>? = null
    private var callResendOtp: Call<ValidateAndGenerateOTPResp>? = null
    private var callUpdateMobile: Call<GeneralResponse>? = null
    private var callUpdateAccountProfile: Call<EditProfileResp>? = null
    private var callLogout: Call<LogoutResp>? = null
    private var callTransferWalletToPoints: Call<TransactionPointsAmountRes>? = null


    fun closeAllCall() {
        if (callEditProfile != null) {
            callEditProfile?.cancel()
        }
        if (callAccountInfo != null) {
            callAccountInfo?.cancel()
        }
        if (callWalletDetails != null) {
            callWalletDetails?.cancel()
        }
        if (callTransferWalletToPoints != null) {
            callTransferWalletToPoints?.cancel()
        }
        if (callAddWallet != null) {
            callAddWallet?.cancel()
        }
        if (callUserPointDetails != null) {
            callUserPointDetails?.cancel()
        }
        if (callConvertMount != null) {
            callConvertMount?.cancel()
        }
        if (callLostProducts != null) {
            callLostProducts?.cancel()
        }
        if (callAddContact != null) {
            callAddContact?.cancel()
        }
        if (callListContactUs != null) {
            callListContactUs?.cancel()
        }
        if (callUserData != null) {
            callUserData?.cancel()
        }
        if (callChangeUserPassword != null) {
            callChangeUserPassword?.cancel()
        }
        if (callChangeUserEmail != null) {
            callChangeUserEmail?.cancel()
        }
        if (callChangeUserEmailOtp != null) {
            callChangeUserEmailOtp?.cancel()
        }
        if (callResendOtp != null) {
            callResendOtp?.cancel()
        }
        if (callUpdateMobile != null) {
            callUpdateMobile?.cancel()
        }
        if (callUpdateAccountProfile != null) {
            callUpdateAccountProfile?.cancel()
        }
        if (callLogout != null) {
            callLogout?.cancel()
        }
    }

    fun logoutUser(deviceId: String) {
        isLoading.value = true
        callLogout = getRetrofitBuilder().logout(deviceId)
        callApi(callLogout!!, onSuccess = {
            isLoading.value = false
            logoutObserver.value = it

        }, onFailure = { throwable, statusCode, errorBody ->
            isLoading.value = false
            if (throwable != null && errorBody == null)
                isNetworkFail.value = throwable !is HttpException
            else {
                errorResponseObserver.value =
                    getErrorResponse(statusCode, errorBody)
            }
        },
            goLogin = {})
    }

    fun editProfileImage(multipartBody: MultipartBody.Part?) {
        isLoading.value = true
        callEditProfile = getRetrofitBuilder().editProfileImage(multipartBody)
        callApi(callEditProfile!!,
            onSuccess = {
                isLoading.value = false
                editProfileImageObserver.value = it
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

    fun getAccountInfo() {
        callAccountInfo = getRetrofitBuilder().getMyAccountInfo()

        callApi(callAccountInfo!!,
            onSuccess = {
                isLoading.value = false
                accountInfoObserver.value = it
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

    fun getWalletDetailsInWallet() {
        isLoading.value = true
        callWalletDetails = getRetrofitBuilder()
            .getWalletDetails()

        callApi(callWalletDetails!!,
            onSuccess = {
                isLoading.value = false
                walletDetailsObserver.value = it
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

    fun getTransferWalletToPoints(amount:Int) {
        isLoading.value = true
        callTransferWalletToPoints = getRetrofitBuilder().transferWalletToPoints(amount)

        callApi(callTransferWalletToPoints!!,
            onSuccess = {
                isLoading.value = false
                transferWalletToPointsObserver.value = it
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

    fun addWalletTransaction(transactionSource: String, transactionType: String, amount: String) {
        isLoading.value = true
        val map: HashMap<String, RequestBody> = HashMap()
        map["TransactionSource"] = transactionSource.requestBody()
        map["TransactionType"] = transactionType.requestBody()
        map["TransactionAmount"] = amount.requestBody()

        callAddWallet = getRetrofitBuilder().addWalletTransaction(map)

        callApi(callAddWallet!!,
            onSuccess = {
                isLoading.value = false
                addWalletTransactionObserver.value = it
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


    fun getUserPointDetailsInWallet() {
        isLoading.value = true
        callUserPointDetails = getRetrofitBuilder().getUserPointsTransactions()

        callApi(callUserPointDetails!!,
            onSuccess = {
                isLoading.value = false
                userPointsDetailsObserver.value = it
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

    fun convertMountToPoints(amount: String) {
        isLoading.value = true

        callConvertMount = getRetrofitBuilder().transferPointsToMoney(amount)

        callApi(callConvertMount!!,
            onSuccess = {
                isLoading.value = false
                convertMoneyToPointObserver.value = it
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

    fun grtLostProducts() {
        isLoading.value = true
        callLostProducts = getRetrofitBuilder().getLostProducts()
        callApi(callLostProducts!!,
            onSuccess = {
                isLoading.value = false
                productListObserver.value = it
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

        callAddContact = getRetrofitBuilder().addEditContactUs(map)
        callApi(callAddContact!!,
            onSuccess = {
                isLoading.value = false
                contactsMessageObserver.value = it
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

    fun getListContactUs() {
        isLoading.value = true
        callListContactUs = getRetrofitBuilder().getListContactUs()
        callApi(callListContactUs!!,
            onSuccess = {
                isLoading.value = false
                technicalSupportMessageListObserver.value = it
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

    fun getUserData() {
        isLoading.value = true
        callUserData = getRetrofitBuilder().getUserData()

        callApi(callUserData!!,
            onSuccess = {
                isLoading.value = false
                getUserDataObserver.value = it
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

    fun getUserDataForAccountTap() {
        getRetrofitBuilder()
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
        val data: HashMap<String, Any> = HashMap()
        data["userId"] = loggedUserid
        data["oldPassword"] = currentPassword
        data["newPassword"] = newPassword


        callChangeUserPassword = getRetrofitBuilder()
            .editProfileChangePassword(data)

        callApi(callChangeUserPassword!!,
            onSuccess = {
                isLoading.value = false
                changePasswordObserver.value = it
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

    fun changeUserEmail(newEmail: String) {
        isLoading.value = true

        callChangeUserEmail = getRetrofitBuilder()
            .editProfileChangeEmail(newEmail)

        callApi(callChangeUserEmail!!,
            onSuccess = {
                isLoading.value = false
                changeEmailObserver.value = it
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

    fun changeUserEmail(newEmail: String, otp: String) {
        isLoading.value = true
        val data: HashMap<String, Any> = HashMap()
        data["resetPasswordCode"] = otp
        data["newEmail"] = newEmail
        callChangeUserEmailOtp = getRetrofitBuilder()
            .confirmChangeEmail(data)

        callApi(callChangeUserEmailOtp!!,
            onSuccess = {
                isLoading.value = false
                confirmChangeEmailOtpObserver.value = it
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


    fun resendOtp(userPhone: String, language: String, otpType: String) {
        isLoading.value = true
        callResendOtp = getRetrofitBuilder()
            .resendOtp(userPhone, otpType, language)

        callApi(callResendOtp!!,
            onSuccess = {
                isLoading.value = false
                validateAndGenerateOTPObserver.value = it
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

    fun updateMobileNumber(userPhone: String, userId: String, otpCode: String) {
        isLoading.value = true
        val data: HashMap<String, Any> = HashMap()
        data["id"] = userId
        data["mobileNumber"] = userPhone
        data["otpCode"] = otpCode

        callUpdateMobile = getRetrofitBuilder()
            .updateUserMobileNumber(data)

        callApi(callUpdateMobile!!,
            onSuccess = {
                isLoading.value = false
                updateUserMobielNumberObserver.value = it
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

    /****/
    fun updateMobileNumber(
        userId: String,
        firstName: String,
        lastName: String,
        dateOfBirth: String,
        gender: Int,
        showUserInformation: String
    ) {
        isLoading.value = true
        val data: HashMap<String, Any> = HashMap()
        data["id"] = userId
        data["firstName"] = firstName
        data["lastName"] = lastName
        data["dateOfBirth"] = dateOfBirth
        data["gender"] = gender
        data["showUserInformation"] = showUserInformation

        callUpdateAccountProfile = getRetrofitBuilder()
            .updateAccountProfile(data)

        callApi(callUpdateAccountProfile!!,
            onSuccess = {
                isLoading.value = false
                updateProfileDataObserver.value = it
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
}