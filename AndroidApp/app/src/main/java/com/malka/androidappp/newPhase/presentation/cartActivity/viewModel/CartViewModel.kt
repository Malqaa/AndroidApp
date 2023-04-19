package com.malka.androidappp.newPhase.presentation.cartActivity.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addOrderResp.AddOrderResp
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartListResp
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CartViewModel : BaseViewModel() {

    var userAddressesListObserver: MutableLiveData<UserAddressesResp> = MutableLiveData()
    var isLoadingAddresses: MutableLiveData<Boolean> = MutableLiveData()
    var cartListRespObserver: MutableLiveData<CartListResp> = MutableLiveData()
    var increaseCartProductQuantityObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var decreaseCartProductQuantityObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var removeProductFromCartProductsObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var isLoadingQuntity: MutableLiveData<Boolean> = MutableLiveData()
    var isLoadingAssignCartToUser: MutableLiveData<Boolean> = MutableLiveData()
    var assignCartToUserObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var applyCouponOnCartObserver: MutableLiveData<GeneralResponse> = MutableLiveData()
    var addOrderObserver: MutableLiveData<AddOrderResp> = MutableLiveData()
    fun getUserAddress() {
        isLoadingAddresses.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getListAddressesForUser()
            .enqueue(object : Callback<UserAddressesResp> {
                override fun onFailure(call: Call<UserAddressesResp>, t: Throwable) {
                    isLoadingAddresses.value = false
                }

                override fun onResponse(
                    call: Call<UserAddressesResp>,
                    response: Response<UserAddressesResp>
                ) {
                    isLoadingAddresses.value = false
                    if (response.isSuccessful) {
                        userAddressesListObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun getCartList(cartMasterId: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .getListCartProductsForClient(cartMasterId)
            .enqueue(object : Callback<CartListResp> {
                override fun onFailure(call: Call<CartListResp>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<CartListResp>,
                    response: Response<CartListResp>
                ) {
                    isLoading.value = false

                    if (response.isSuccessful) {
                        cartListRespObserver.value = response.body()
                    }
//                    else {
//                        println("hhhh "+ Gson().toJson(response.errorBody()))
//                        errorResponseObserver.value = getErrorResponse(response.errorBody())
//                    }
                }
            })
    }

    fun applyCouponOnCart(cartMasterId: String,couponCode:String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .applyCouponOnCart(cartMasterId,couponCode,"FixedPrice")
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        applyCouponOnCartObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun applyCouponOnCart(cartMasterId: String,couponCode:String,couponForbusinessAccountId:String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .applyCouponOnCart(cartMasterId,couponCode,"1",couponForbusinessAccountId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false

                    if (response.isSuccessful) {
                        applyCouponOnCartObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }
    fun assignCardToUser(masterCartId: String){
        isLoadingAssignCartToUser.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .assignCartMastetToUser(masterCartId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoadingAssignCartToUser.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoadingAssignCartToUser.value = false

                    if (response.isSuccessful) {
                        assignCartToUserObserver.value = response.body()
                    }
//                    else {
//                        println("hhhh "+ Gson().toJson(response.errorBody()))
//                        errorResponseObserver.value = getErrorResponse(response.errorBody())
//                    }
                }
            })
    }


    fun increaseCartProductQuantity(productCartId: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .increaseCartProductQuantity(productCartId, "1")
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        increaseCartProductQuantityObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun decreaseCartProductQuantity(productCartId: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .decreaseCartProductQuantity(productCartId, "1")
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        decreaseCartProductQuantityObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

    fun removeProductFromCartProducts(productCartId: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .removeProductFromCartProducts(productCartId)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        removeProductFromCartProductsObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }


    fun addOrder(masterCartId: String, addressId: Int) {
        println("hhhh " + masterCartId + " " + addressId)
        val map: HashMap<String, RequestBody> = HashMap()
        map["CartMasterId"] = masterCartId.requestBody()
        map["pay"] = "Cash".requestBody()
        map["ShippingAddressId"] = addressId.toString().requestBody()
        map["BuyWithFixedRpriceOrNegotiation"]="FixedPrice".requestBody()
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .addOrder(map)
            .enqueue(object : Callback<AddOrderResp> {
                override fun onFailure(call: Call<AddOrderResp>, t: Throwable) {
                    isLoading.value = false
                    isNetworkFail.value = t !is HttpException
                }

                override fun onResponse(
                    call: Call<AddOrderResp>,
                    response: Response<AddOrderResp>
                ) {
                    isLoading.value = false
                    if (response.isSuccessful) {
                        addOrderObserver.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.errorBody())
                    }
                }
            })
    }

}