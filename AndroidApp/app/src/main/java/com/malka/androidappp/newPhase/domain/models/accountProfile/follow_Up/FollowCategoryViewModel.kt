package com.malka.androidappp.fragments.follow_Up

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.GeneralResponses
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowResp
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.FavoriteSeller
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.SavedSearch
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class FollowCategoryViewModel : BaseViewModel() {
    var savedSearchObserve: MutableLiveData<List<SavedSearch>> = MutableLiveData()
    var favoriteSellerRespObserver: MutableLiveData<List<FavoriteSeller>> = MutableLiveData()
    var categoryFollowRespObserver: MutableLiveData<CategoryFollowResp> = MutableLiveData()

    var removeSellerToFavObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var sellerLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getListFavoriteSeller() {
        isLoading.value = true
        getRetrofitBuilder().getListFavoriteSeller()
            .enqueue(object : Callback<GeneralResponses<List<FavoriteSeller>>> {
                override fun onFailure(
                    call: Call<GeneralResponses<List<FavoriteSeller>>>,
                    t: Throwable
                ) {

                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponses<List<FavoriteSeller>>>,
                    response: Response<GeneralResponses<List<FavoriteSeller>>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        favoriteSellerRespObserver.value = response.body()?.data
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun removeSellerToFav(sellerProviderID: String?, businessAccountId: String?) {
        isLoading.value = true
        getRetrofitBuilder()
            .removeFavoriteSeller(sellerProviderID, businessAccountId)
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
                        removeSellerToFavObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun getCategoryFollow() {
        isLoading.value = true
         getRetrofitBuilder()
            .getListCategoryFollow()
            .enqueue(object : Callback<CategoryFollowResp> {
                override fun onFailure(call: Call<CategoryFollowResp>, t: Throwable) {

                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<CategoryFollowResp>,
                    response: Response<CategoryFollowResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        categoryFollowRespObserver.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun removeCategoryFollow(categoryID: Int) {
        isLoading.value = true
         getRetrofitBuilder().removeFollow(categoryID)
            .enqueue(object : Callback<GeneralResponse?> {
                override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponse?>,
                    response: Response<GeneralResponse?>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        getCategoryFollow()
                    }
                }
            })
    }


    fun getListSaveSearch() {
        isLoading.value = true
         getRetrofitBuilder()
            .getListSaveSearch()
            .enqueue(object : Callback<GeneralResponses<List<SavedSearch>>> {
                override fun onFailure(
                    call: Call<GeneralResponses<List<SavedSearch>>>,
                    t: Throwable
                ) {
                    isLoading.value = false
                }

                override fun onResponse(
                    call: Call<GeneralResponses<List<SavedSearch>>>,
                    response: Response<GeneralResponses<List<SavedSearch>>>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        savedSearchObserve.value = response.body()?.data
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun removeSearchFollow(searchID: Int) {
         getRetrofitBuilder().removeSavedSearch(searchID)
            .enqueue(object : Callback<GeneralResponse?> {
                override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<GeneralResponse?>,
                    response: Response<GeneralResponse?>
                ) {
                    if (response.isSuccessful) {
                        getListSaveSearch()
                    }
                }
            })
    }
}