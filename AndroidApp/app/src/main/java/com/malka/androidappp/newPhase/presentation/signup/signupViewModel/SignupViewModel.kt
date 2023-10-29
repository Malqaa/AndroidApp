package com.malka.androidappp.newPhase.presentation.signup.signupViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.resgisterResp.RegisterResp
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.UserVerifiedResp
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Query
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class SignupViewModel : BaseViewModel() {


    var validateAndGenerateOTPObserver: MutableLiveData<ValidateAndGenerateOTPResp> =
        MutableLiveData()
    var userVerifiedObserver: MutableLiveData<UserVerifiedResp> = MutableLiveData()
    var registerRespObserver: MutableLiveData<RegisterResp> = MutableLiveData()

    fun validateUserAndGenerateOTP(
        userName: String,
        email: String,
        fullNumberWithPlus: String,
        language: String
    ) {
        isLoading.value = true
        println("hhh $userName $email $fullNumberWithPlus $language" )
        RetrofitBuilder.GetRetrofitBuilder()
            .validateUserAndGenerateOtp(userName, email, fullNumberWithPlus)
            .enqueue(object : Callback<ValidateAndGenerateOTPResp> {
                override fun onResponse(
                    call: Call<ValidateAndGenerateOTPResp>,
                    response: Response<ValidateAndGenerateOTPResp>
                ) {

                    if (response.isSuccessful) {
                        val data: ValidateAndGenerateOTPResp? = response.body()
                        validateAndGenerateOTPObserver.value = response.body()
                    } else {

                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<ValidateAndGenerateOTPResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })

    }


    fun resendOtp(userPhone: String, language: String,otpType: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .resendOtp(userPhone, otpType, language).enqueue(object : Callback<ValidateAndGenerateOTPResp> {
                override fun onResponse(
                    call: Call<ValidateAndGenerateOTPResp>,
                    response: Response<ValidateAndGenerateOTPResp>
                ) {
                    if (response.isSuccessful) {
                        validateAndGenerateOTPObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<ValidateAndGenerateOTPResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })
    }

    fun verifyOtp(userPhone: String, otpCode: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .verifyOtp(userPhone, otpCode).enqueue(object : Callback<UserVerifiedResp> {
                override fun onResponse(
                    call: Call<UserVerifiedResp>, response: Response<UserVerifiedResp>
                ) {
                    if (response.isSuccessful) {
                        userVerifiedObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<UserVerifiedResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })
    }

    fun createUser(
        userName: String,
        phoneNumber: String,
        userEmail: String,
        userPass: String,
        invitationCode: String,
        firstName: String,
        editTextlastname: String,
        date: String,
        gender_: String,
        selectedCountryId: String,
        selectedRegionId: String,
        cityId: String,
        area: String,
        streetNUmber: String,
        county_code: String,
        isBusinessAccount: String,
        lang: String,
        projectName: String,
        deviceType: String,
        deviceId: String,
        file: File?,
        context: Context
    ) {
        isLoading.value = true
         var multipartBody :MultipartBody.Part?=null
        file?.let {file->
            var requestbody:RequestBody=file.asRequestBody("image/*".toMediaTypeOrNull())
            multipartBody=MultipartBody.Part.createFormData("imgProfile",file.name,requestbody)
        }

//        imageUri?.let {
//            val file = File(imageUri.path)
//            //===="application/octet-stream"
//            requestFile = file.asRequestBody("application/binary".toMediaTypeOrNull())
//            body = MultipartBody.Part.createFormData("image", file.name, requestFile!!)
//        }
//        imageUri?.let {
//            try {
//                val file = File(imageUri.path)
//                val `in`: InputStream = FileInputStream(file)
//                val buf: ByteArray
//                buf = ByteArray(`in`.available())
//                while (`in`.read(buf) !== -1);
//                requestFile = buf.toRequestBody("application/octet-stream".toMediaTypeOrNull())
//            } catch (e: java.lang.Exception) {
//            }
//        }


        RetrofitBuilder.GetRetrofitBuilder()
            .createuser2(
                userName.requestBody(),
                phoneNumber.requestBody(),
                userEmail.requestBody(),
                userPass.requestBody(),
                invitationCode.requestBody(),
                firstName.requestBody(),
                editTextlastname.requestBody(),
                date.requestBody(),
                gender_.requestBody(),
                selectedCountryId.requestBody(),
                selectedRegionId.requestBody(),
                cityId.requestBody(),
                area.requestBody(),
                streetNUmber.requestBody(),
                county_code.requestBody(),
                isBusinessAccount.requestBody(),
                lang.requestBody(),
                projectName.requestBody(),
                deviceType.requestBody(),
                deviceId.requestBody(),
                multipartBody
            ).enqueue(object : Callback<RegisterResp> {
                override fun onResponse(
                    call: Call<RegisterResp>, response: Response<RegisterResp>
                ) {
                    if (response.isSuccessful) {
                        registerRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<RegisterResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }
            })
    }

}