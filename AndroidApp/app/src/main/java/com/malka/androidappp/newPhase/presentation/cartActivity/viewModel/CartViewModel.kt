package com.malka.androidappp.newPhase.presentation.cartActivity.viewModel

import androidx.lifecycle.MutableLiveData
import com.malka.androidappp.newPhase.core.BaseViewModel
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.network.callApi
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addOrderResp.AddOrderResp
import com.malka.androidappp.newPhase.domain.models.addOrderResp.ProductOrderPaymentDetailsDto
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartListResp
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.HttpException

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


    private var callUserAddress: Call<UserAddressesResp>? = null
    private var callCartList: Call<CartListResp>? = null
    private var callApplyCouponOnCartF: Call<GeneralResponse>? = null
    private var callApplyCouponOnCart: Call<GeneralResponse>? = null
    private var callUnApplyCouponOnCart: Call<GeneralResponse>? = null
    private var callAssignCardToUser: Call<GeneralResponse>? = null
    private var callDecreaseCartProductQuantity: Call<GeneralResponse>? = null
    private var callIncreaseCartProductQuantity: Call<GeneralResponse>? = null
    private var callRemoveProductFromCart: Call<GeneralResponse>? = null
    private var callAddOrder: Call<AddOrderResp>? = null
    private var callAddPaymentTransaction: Call<GeneralResponse>? = null
    private var callDeleteShipping: Call<GeneralResponse>? = null

    fun closeAllCall() {
        if (callUserAddress != null) {
            callUserAddress?.cancel()
        }
        if (callCartList != null) {
            callCartList?.cancel()
        }
        if (callApplyCouponOnCartF != null) {
            callApplyCouponOnCartF?.cancel()
        }
        if (callApplyCouponOnCart != null) {
            callApplyCouponOnCart?.cancel()
        }
        if (callUnApplyCouponOnCart != null) {
            callUnApplyCouponOnCart?.cancel()
        }
        if (callAssignCardToUser != null) {
            callAssignCardToUser?.cancel()
        }
        if (callDecreaseCartProductQuantity != null) {
            callDecreaseCartProductQuantity?.cancel()
        }
        if (callIncreaseCartProductQuantity != null) {
            callIncreaseCartProductQuantity?.cancel()
        }
        if (callRemoveProductFromCart != null) {
            callRemoveProductFromCart?.cancel()
        }
        if (callAddOrder != null) {
            callAddOrder?.cancel()
        }
        if (callRemoveProductFromCart != null) {
            callRemoveProductFromCart?.cancel()
        }
        if (callAddPaymentTransaction != null) {
            callAddPaymentTransaction?.cancel()
        }
        if (callDeleteShipping != null) {
            callDeleteShipping?.cancel()
        }

    }

    fun getUserAddress() {
        isLoadingAddresses.value = true
        getRetrofitBuilder()
            .getListAddressesForUser()

        callUserAddress = getRetrofitBuilder().getListAddressesForUser()
        callApi(callUserAddress!!,
            onSuccess = {
                isLoadingAddresses.value = false
                userAddressesListObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoadingAddresses.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoadingAddresses.value = false
                needToLogin.value = true
            })

    }

    fun getCartList(cartMasterId: String) {
        isLoading.value = true
        callCartList = getRetrofitBuilder().getListCartProductsForClient(cartMasterId)
        callApi(callCartList!!,
            onSuccess = {
                isLoading.value = false
                cartListRespObserver.value = it
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

    fun applyCouponOnCart(cartMasterId: String, couponCode: String) {
        isLoading.value = true
        callApplyCouponOnCartF =
            getRetrofitBuilder().applyCouponOnCart(cartMasterId, couponCode, "FixedPrice")
        callApi(callApplyCouponOnCartF!!,
            onSuccess = {
                isLoading.value = false
                applyCouponOnCartObserver.value = it
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

    fun applyCouponOnCart(
        cartMasterId: String,
        couponCode: String,
        providerId: String,
        couponForbusinessAccountId: String
    ) {
        isLoading.value = true
        val xCouponAccountId: String? = if (couponForbusinessAccountId == "null") {
            null
        } else {
            couponForbusinessAccountId
        }
        callApplyCouponOnCart = getRetrofitBuilder().applyCouponOnCart(
            cartMasterId,
            couponCode,
            providerId,
            "1",
            xCouponAccountId
        )
        callApi(callApplyCouponOnCart!!,
            onSuccess = {
                isLoading.value = false
                applyCouponOnCartObserver.value = it
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

    fun unApplyCouponOnCart(
        cartMasterId: String,
        couponCode: String,
        providerId: String,
        couponForbusinessAccountId: String
    ) {
        isLoading.value = true
        val xCouponAccountId: String? = if (couponForbusinessAccountId == "null") {
            null
        } else {
            couponForbusinessAccountId
        }
        callUnApplyCouponOnCart = getRetrofitBuilder().unApplyCouponOnCart(
            cartMasterId,
            couponCode,
            providerId,
            "1",
            xCouponAccountId
        )
        callApi(callUnApplyCouponOnCart!!,
            onSuccess = {
                isLoading.value = false
                applyCouponOnCartObserver.value = it
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


    fun assignCardToUser(masterCartId: String) {
        isLoadingAssignCartToUser.value = true
        callAssignCardToUser = getRetrofitBuilder().assignCartMastetToUser(masterCartId)
        callApi(callAssignCardToUser!!,
            onSuccess = {
                isLoadingAssignCartToUser.value = false
                assignCartToUserObserver.value = it
            },
            onFailure = { throwable, statusCode, errorBody ->
                isLoadingAssignCartToUser.value = false
                if (throwable != null && errorBody == null)
                    isNetworkFail.value = throwable !is HttpException
                else {
                    errorResponseObserver.value =
                        getErrorResponse(statusCode, errorBody)
                }
            },
            goLogin = {
                isLoadingAssignCartToUser.value = false
                needToLogin.value = true
            })
    }


    fun increaseCartProductQuantity(productCartId: String) {
        isLoading.value = true
        callIncreaseCartProductQuantity =
            getRetrofitBuilder().increaseCartProductQuantity(productCartId, "1")
        callApi(callIncreaseCartProductQuantity!!,
            onSuccess = {
                isLoading.value = false
                increaseCartProductQuantityObserver.value = it
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

    fun decreaseCartProductQuantity(productCartId: String) {
        isLoading.value = true
        callDecreaseCartProductQuantity =
            getRetrofitBuilder().decreaseCartProductQuantity(productCartId, "1")
        callApi(callDecreaseCartProductQuantity!!,
            onSuccess = {
                isLoading.value = false
                decreaseCartProductQuantityObserver.value = it
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

    fun removeProductFromCartProducts(productCartId: String) {
        isLoading.value = true
        callRemoveProductFromCart =
            getRetrofitBuilder().removeProductFromCartProducts(productCartId)
        callApi(callRemoveProductFromCart!!,
            onSuccess = {
                isLoading.value = false
                removeProductFromCartProductsObserver.value = it
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
        callAddOrder = getRetrofitBuilder().addOrder(map, paymentList)
        callApi(callAddOrder!!,
            onSuccess = {
                isLoading.value = false
                addOrderObserver.value = it
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
        callAddPaymentTransaction = getRetrofitBuilder().addPaymentTransaction(data)
        callApi(callAddPaymentTransaction!!,
            onSuccess = {
                isLoading.value = false
                paymentTransaction.value = it
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

    fun deleteShipping(businessAccountId: String?, cartMasterId: String, providerId: String) {
        isLoading.value = true
        callDeleteShipping = getRetrofitBuilder().removeShipmentProductsFromCart(
            businessAccountId,
            cartMasterId,
            providerId
        )
        callApi(callDeleteShipping!!,
            onSuccess = {
                isLoading.value = false
                deleteShipment.value = it
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