package com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.callApi
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.BusinessAccountsListResp
import com.malka.androidappp.newPhase.domain.models.bussinessAccountsListResp.ChangeBussinesAccountResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.HttpException

class BusinessAccountViewModel : BaseViewModel() {
    var businessAccountListObserver: MutableLiveData<BusinessAccountsListResp> =
        MutableLiveData<BusinessAccountsListResp>()
    var changeBusinessAccountObserver: MutableLiveData<ChangeBussinesAccountResp> =
        MutableLiveData<ChangeBussinesAccountResp>()
    var addbusinessAccountListObserver: MutableLiveData<GeneralResponse> =
        MutableLiveData<GeneralResponse>()

    private var callChangeBusinessAccount: Call<ChangeBussinesAccountResp>? = null
    private var callAllBusinessAccount: Call<BusinessAccountsListResp>? = null
    private var callAddBusinessAccount: Call<GeneralResponse>? = null

    fun getBusinessAccount() {
        isLoading.value = true
        callAllBusinessAccount = getRetrofitBuilder().gatAllBusinessAccounts()
        callApi(callAllBusinessAccount!!,
            onSuccess = {
                isLoading.value = false
                businessAccountListObserver.value = it
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

    fun changeBusinessAccount(id: Int) {
        isLoading.value = true
        callChangeBusinessAccount = getRetrofitBuilder().changeBusinessAccount(id)
        callApi(callChangeBusinessAccount!!,
            onSuccess = {
                isLoading.value = false
                changeBusinessAccountObserver.value = it
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

    fun addBusinessAccount(
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

        callAddBusinessAccount=  getRetrofitBuilder()
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
        callApi(callAddBusinessAccount!!,
            onSuccess = {
                isLoading.value = false
                addbusinessAccountListObserver.value = it
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
        if (callAddBusinessAccount != null) {
            callAddBusinessAccount?.cancel()
        }
        if (callChangeBusinessAccount != null) {
            callChangeBusinessAccount?.cancel()
        }
        if (callAllBusinessAccount != null) {
            callAllBusinessAccount?.cancel()
        }
    }

}