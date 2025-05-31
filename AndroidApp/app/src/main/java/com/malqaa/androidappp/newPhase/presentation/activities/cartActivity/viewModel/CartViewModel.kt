package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.addOrderResp.AddOrderResp
import com.malqaa.androidappp.newPhase.domain.models.addOrderResp.ProductOrderPaymentDetailsDto
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartListResp
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderDetailsByMasterIDResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.UserAddressesResp
import com.malqaa.androidappp.newPhase.domain.enums.PaymentMethod
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.getMonth
import com.malqaa.androidappp.newPhase.utils.getYear
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    var currentOrderByMusterIdRespObserver: MutableLiveData<OrderDetailsByMasterIDResp> =
        MutableLiveData()


    private var callCurrentOrderDetails: Call<OrderDetailsByMasterIDResp>? = null
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
        if (callCurrentOrderDetails != null) {
            callCurrentOrderDetails?.cancel()
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

    fun getCurrentOrderDetailsByMasterID(orderMasterID: Int) {
        isLoading.value = true
        callCurrentOrderDetails =
            getRetrofitBuilder().getOrderMasterDetailsByMasterOrderId(orderMasterID)
        callApi(callCurrentOrderDetails!!,
            onSuccess = {
                isLoading.value = false
                currentOrderByMusterIdRespObserver.value = it
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
        productOrderPaymentList: List<ProductOrderPaymentDetailsDto>,
        accountDetails: AccountDetails? = null,
        totalAmount: Float,
        walletAmount: Int? = null,
        selectedPaymentMethod: PaymentMethod,
    ) {
        val map = HashMap<String, RequestBody>()

        // Required simple fields
        map["CartMasterId"] = masterCartId.requestBody()
        map["PaymentType"] = selectedPaymentMethod.value.toString().requestBody()
        map["ShippingAddressId"] = addressId.toString().requestBody()
        map["BuyWithFixedRpriceOrNegotiation"] = "FixedPrice".requestBody()

        // Payment card info (if present)
        accountDetails?.accountNumber?.let {
            map["ExecutePaymentDto.PaymentCard.Number"] = it.requestBody()
        }
        accountDetails?.expiaryDate?.getMonth()?.let {
            map["ExecutePaymentDto.PaymentCard.ExpiryMonth"] = it.requestBody()
        }
        accountDetails?.expiaryDate?.getYear()?.let {
            map["ExecutePaymentDto.PaymentCard.ExpiryYear"] = it.requestBody()
        }
        accountDetails?.cvv?.toString()?.let {
            map["ExecutePaymentDto.PaymentCard.SecurityCode"] = it.requestBody()
        }
        accountDetails?.bankHolderName?.let {
            map["ExecutePaymentDto.PaymentCard.HolderName"] = it.requestBody()
        }

        // Payment method
        map["ExecutePaymentDto.PaymentMethodId"] =
            selectedPaymentMethod.value.toString().requestBody()

        // Total amount
        map["ExecutePaymentDto.TotalAmount"] = totalAmount.toString().requestBody()

        // Wallet amount
        walletAmount?.let {
            map["ExecutePaymentDto.WalletAmount"] = it.toString().requestBody()
        }

        // Correctly formatted complex part
        val productOrderJson = Gson().toJson(productOrderPaymentList.first()) // Assuming only one item
        val productOrderPart = MultipartBody.Part.createFormData(
            "ProductOrderPaymentDetailsDto",
            null,
            productOrderJson.toRequestBody("application/json".toMediaTypeOrNull())
        )

        // Call API
        isLoading.value = true
        callAddOrder = getRetrofitBuilder().addOrder(map, listOf(productOrderPart))

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

    fun String.requestBody(): RequestBody {
        return this.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    fun addPaymentTransaction(
        totalPriceForCartFinal: Float,
        totalPriceForCartBeforeDiscount: Float,
        masterCartId: String,
        addressId: Int,
        productOrderPaymentList: List<ProductOrderPaymentDetailsDto>,
        accountDetails: AccountDetails? = null,
        totalAmount: Float,
        walletAmount: Double? = null,
        selectedPaymentMethod: PaymentMethod
    ) {
        val data: HashMap<String, Any> = HashMap()
        data["checkOutPaymentFor"] = 1
        data["orderOrPakatId"] = masterCartId
        data["orderMasterTotalBeforDiscount"] = totalPriceForCartBeforeDiscount
        data["orderMasterTotalAfterDiscount"] = totalPriceForCartFinal
        data["paymentType"] = "Cash"
        data["shippingAddressId"] = addressId
        data["productOrderPaymentDetailsDto"] = productOrderPaymentList

        // =========================================================================================
        // execute payment
        // =========================================================================================
        // account number
        val accountNumber = accountDetails?.accountNumber
        if (!accountNumber.isNullOrEmpty()) {
            data["ExecutePaymentDto.PaymentCard.Number"] = accountNumber
        }
        // expiry month
        val expiryMonth = accountDetails?.expiaryDate?.getMonth()
        if (!expiryMonth.isNullOrEmpty()) {
            data["ExecutePaymentDto.PaymentCard.ExpiryMonth"] = expiryMonth
        }
        // expiry year
        val expiryYear = accountDetails?.expiaryDate?.getYear()
        if (!expiryYear.isNullOrEmpty()) {
            data["ExecutePaymentDto.PaymentCard.ExpiryYear"] = expiryYear
        }
        // security code
        val securityCode = accountDetails?.cvv.toString()
        if (securityCode.isNotEmpty()) {
            data["ExecutePaymentDto.PaymentCard.SecurityCode"] = securityCode
        }
        // holder name
        val holderName = accountDetails?.bankHolderName
        if (!holderName.isNullOrEmpty()) {
            data["ExecutePaymentDto.PaymentCard.HolderName"] = holderName
        }
        // payment method id
        val paymentMethodId = selectedPaymentMethod.value.toString()
        if (paymentMethodId.isNotEmpty()) {
            data["ExecutePaymentDto.PaymentMethodId"] = paymentMethodId
        }
        // holder name
        if (totalAmount > 0) {
            data["ExecutePaymentDto.TotalAmount"] = totalAmount
        }
        // wallet amount
        if (walletAmount != null) {
            data["ExecutePaymentDto.WalletAmount"] = walletAmount
        }
        // =========================================================================================

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