package com.malqaa.androidappp.newPhase.domain.models.cartListResp

import com.google.gson.annotations.SerializedName

data class CartListResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val cartDataObject: CartDataObject? = null
)

data class CartDataObject(
    var totalPriceForCartBeforeDiscount: Float=0f,
    var totalPriceForCartFinal: Float=0f,
    var buyType: Int=0,

    var adminCouponcodeApplied: String="",
    var adminCouponcodeDiscount: Int=0,
    var shippingFee: Int=0,
    var listCartProducts: List<CartProductDetails> = ArrayList()
)

data class CartProductDetails(
    var providerId: String? = null,
    var businessAccountId: String? = null,
    var providerName: String? = null,
    var businessAccountName: String? = null,
    var totalPriceForProvider: String? = null,
    var cartMsterId: String? = null,
    var shipmentCouponId:String ?=null,
    var couponAppliedBussinessAccountDto: CouponAppliedBussinessAccountDto? = null,
    var listProduct: ArrayList<ProductCartItem>? = null
)

data class CouponAppliedBussinessAccountDto(
    var businessAccountId: String? = null,
    var businessAccountAmountBeforeCoupon: Float,
    var businessAccountAmountAfterCoupon: Float,
    var finalTotalPriceForBusinessAccount: Float,
    var totalPriceBeforeCouponForCart: Float,
    var totalPriceForCart: Float,
    var adminCouponcodeApplied: String,
    var adminCouponcodeDiscount: Int,
)

data class ProductCartItem(
    var cartproductId: Int,
    var id: Int,
    var img: String? = null,
    var name: String? = null,
    var qty: Int?=null,
    var cartProductQuantity:Int?=null,
    var price: Float,
    var priceDiscount: Float,
    var categoryName: String? = null,
    var country: String? = null,
    var region: String? = null,
    var shippingOptions: List<ShippingOptions>,
    var paymentOptions: List<PaymentOptions>,
    var msgError: String,
)
data class ShippingOptions(
    var shippingOptionName: String,
    var shippingOptionId: Int,
)

data class PaymentOptions(
    var id: Int,
    var paymentOptionId: Int,
)

