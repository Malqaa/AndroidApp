package com.malka.androidappp.newPhase.presentation.cartActivity.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addOrderResp.AddOrderResp
import com.malka.androidappp.newPhase.domain.models.addOrderResp.ProductOrderPaymentDetailsDto
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartListResp
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.AddProductResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.http.Query

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

    var paymentTransaction: MutableLiveData<GeneralResponse> = MutableLiveData()
    var deleteShipment: MutableLiveData<GeneralResponse> = MutableLiveData()
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
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
//                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
//                    }
                }
            })
    }

    fun applyCouponOnCart(cartMasterId: String, couponCode: String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .applyCouponOnCart(cartMasterId, couponCode, "FixedPrice")
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun applyCouponOnCart(
        cartMasterId: String,
        couponCode: String,
        providerId: String,
        couponForbusinessAccountId: String
    ) {
        isLoading.value = true
        val xCouponAccountId: String?
        if (couponForbusinessAccountId == "null") {
            xCouponAccountId = null
        } else {
            xCouponAccountId = couponForbusinessAccountId
        }
        RetrofitBuilder.GetRetrofitBuilder()
            .applyCouponOnCart(cartMasterId, couponCode, providerId, "1", xCouponAccountId)
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun unApplyCouponOnCart(
        cartMasterId: String,
        couponCode: String,
        providerId: String,
        couponForbusinessAccountId: String
    ) {
        isLoading.value = true
        val xCouponAccountId: String?
        if (couponForbusinessAccountId == "null") {
            xCouponAccountId = null
        } else {
            xCouponAccountId = couponForbusinessAccountId
        }
        RetrofitBuilder.GetRetrofitBuilder()
            .unApplyCouponOnCart(cartMasterId, couponCode, providerId, "1", xCouponAccountId)
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun assignCardToUser(masterCartId: String) {
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
//                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }


    fun addOrder(
        masterCartId: String,
        addressId: Int,
        paymentOption: Int,
        delivery: String,
        productOrderPaymentList: List<ProductOrderPaymentDetailsDto>
    ) {
        val paymentList: List<MultipartBody.Part> = productOrderPaymentList.map { item ->
            MultipartBody.Part.createFormData("", item.toString())
        }

        println("hhhh $masterCartId $addressId")
        val map: HashMap<String, RequestBody> = HashMap()
        map["CartMasterId"] = masterCartId.requestBody()
        map["PaymentType"] = "Cash".requestBody()
        map["PaymentTypeId"] = "1".requestBody()
        map["ShippingAddressId"] = addressId.toString().requestBody()
        map["BuyWithFixedRpriceOrNegotiation"] = "FixedPrice".requestBody()
//        map["ProductOrderPaymentDetails"]= ProductOrderPaymentDetailsDto(547,1,1)


        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .addOrder(map, paymentList)
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
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }

    fun addPaymentTransaction(
        totalPriceForCartFinal: Float,
        totalPriceForCartBeforeDiscount: Float,
        masterCartId: String,
        addressId: Int,
        paymentOption: Int,
        delivery: String,
        productOrderPaymentList: List<ProductOrderPaymentDetailsDto>
    ) {

        val data: HashMap<String, Any> = HashMap()
        data["checkOutPaymentFor"] = 1
        data["orderOrPakatId"] = 1
        data["orderMasterTotalBeforDiscount"] = totalPriceForCartBeforeDiscount
        data["orderMasterTotalAfterDiscount"] = totalPriceForCartFinal
        data["paymentType"] = "Cash"
        data["shippingAddressId"] = addressId
        data["productOrderPaymentDetailsDto"] = productOrderPaymentList

        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()
            .addPaymentTransaction(data
            )
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
                        paymentTransaction.value = response.body()
                    } else {
                        errorResponseObserver.value =
                            getErrorResponse(response.code(), response.errorBody())
                    }
                }
            })
    }
    fun deleteShipping(businessAccountId: String?,cartMasterId:String,providerId:String) {
        isLoading.value = true
        RetrofitBuilder.GetRetrofitBuilder()

            .removeShipmentProductsFromCart(businessAccountId,cartMasterId,providerId)
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
                        deleteShipment.value = response.body()
                    } else {
                        errorResponseObserver.value = getErrorResponse(response.code(),response.errorBody())
                    }
                }
            })
    }

}