package com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountsListResp
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.ChangeBussinesAccountResp
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

class BusinessAccountViewModel : BaseViewModel() {
    var businessAccountListObserver: MutableLiveData<BusinessAccountsListResp> =
        MutableLiveData<BusinessAccountsListResp>()
    var changeBusinessAccountObserver: MutableLiveData<ChangeBussinesAccountResp> =
        MutableLiveData<ChangeBussinesAccountResp>()
    var addbusinessAccountListObserver: MutableLiveData<GeneralResponse> =
        MutableLiveData<GeneralResponse>()

    fun getBusinessAccount() {
        isLoading.value = true
        getRetrofitBuilder()
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
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun changeBusinessAccount(id: Int) {
        isLoading.value = true
        getRetrofitBuilder()
            .changeBusinessAccount(id)
            .enqueue(object : Callback<ChangeBussinesAccountResp> {
                override fun onFailure(call: Call<ChangeBussinesAccountResp>, t: Throwable) {
                    isNetworkFail.value = t !is HttpException
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<ChangeBussinesAccountResp>,
                    response: Response<ChangeBussinesAccountResp>
                ) {

                    isLoading.value = false
                    if (response.isSuccessful) {
                        changeBusinessAccountObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

    fun addBusinessAcoount(
        id: String,
        userNamee: String,
        providerId: String,
        company_name_ar: String,
        company_name_en: String,
        textEmaill: String,
        etPhoneNumber: String,
        userImageFile: MultipartBody.Part?,
        et_web_site: String,
        Facebook: String,
        Instagram: String,
        ic_twitter: String,
        ic_youtube: String,
        ic_linkedIn: String,
        ic_snapshot: String,
        ic_tickTok: String,
        selectedCommericalRegisterType: Int,
        commercial_registration_no: String,
        tvDate: String,
        TaxNumber: String,
        etMaroof: String,
        commercialRegistryFileList: ArrayList<MultipartBody.Part>,
        selectedCountryId: Int,
        selectedRegionId: Int,
        selectedNeighborhoodId: Int,
        area: String,
        streetNUmber: String,
        county_code: String,
        lat: Double,
        longitude: Double,
        _getChecked: Boolean
    ) {
        isLoading.value = true
//        val multipartBodyCommercialRegistryFileList: ArrayList<MultipartBody.Part> =
//            ArrayList<MultipartBody.Part>()
//
//        commercialRegistryFileList.let {
//            for (item in commercialRegistryFileList) {
//                // var requestbody: RequestBody = item.requestBody()
//                val multipartBody: MultipartBody.Part =
//                    MultipartBody.Part.createFormData( "BusinessAccountCertificates",item.toString())
//                multipartBodyCommercialRegistryFileList.add(multipartBody)
//            }
//
//        }

        getRetrofitBuilder()
            .addEditBusinessAccount(
                id.requestBody(),

                businessAccountUserName = userNamee.requestBody(),
                providerId = providerId.requestBody(),
                businessAccountNameAr = company_name_ar.requestBody(),
                BusinessAccountNameEn = company_name_en.requestBody(),
                BusinessAccountEmail = textEmaill.requestBody(),
                BusinessAccountPhoneNumber = etPhoneNumber.requestBody(),
                userImageFile,
                BusinessAccountWebsite = et_web_site.requestBody(),
                BusinessAccountFaceBook = Facebook.requestBody(),
                BusinessAccountInstagram = Instagram.requestBody(),
                BusinessAccountTwitter = ic_twitter.toRequestBody(),
                BusinessAccountYouTube = ic_youtube.requestBody(),
                BusinessAccountLinkedIn = ic_linkedIn.requestBody(),
//                BusinessAccountSnapchat = ic_snapshot.requestBody(),
                BusinessAccountTikTok = ic_tickTok.requestBody(),
                RegistrationDocumentType = selectedCommericalRegisterType.toString().requestBody(),
                DetailRegistrationNumber = commercial_registration_no.requestBody(),
                RegistrationNumberExpiryDate = tvDate.requestBody(),
                VatNumber = TaxNumber.requestBody(),
                Maroof = etMaroof.requestBody(),
                if (commercialRegistryFileList.isEmpty())
                    null
                else commercialRegistryFileList,
                CountryId = selectedCountryId.toString().requestBody(),
                RegionId = selectedRegionId.toString().requestBody(),
                NeighborhoodId = selectedNeighborhoodId.toString().requestBody(),
                District = area.requestBody(),
                Street = streetNUmber.requestBody(),
                ZipCode = county_code.requestBody(),
                Trade15Years = _getChecked.toString().requestBody(),
                Lat = lat.toString().requestBody(),
                Lon = longitude.toString().requestBody(),
                "true".requestBody(),
                "false".requestBody()
            )
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
                        addbusinessAccountListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

}