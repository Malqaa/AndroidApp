package com.malka.androidappp.newPhase.domain.models.cartListResp

import com.google.gson.annotations.SerializedName
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateItem
import com.malka.androidappp.newPhase.domain.models.productResp.ProductListResp

data class CartListResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val cartDataObject: CartDataObject? = null
)

data class CartDataObject(
    var totalPriceForCartBeforeDiscount: Float,
    var totalPriceForCartFinal: Float,
    var buyType: Int,
    var listCartProducts: List<CartProductDetails>
)

data class CartProductDetails(
    var providerId: String? = null,
    var businessAccountId: String? = null,
    var providerName: String? = null,
    var businessAccountName: String? = null,
    var totalPriceForProvider: String? = null,
    var cartMsterId: String? = null,
    var couponAppliedBussinessAccountDto: CouponAppliedBussinessAccountDto? = null,
    var listProduct: ArrayList<ProductCartItem>? = null
)

data class CouponAppliedBussinessAccountDto(
    var businessAccountId: String? = null,
    var businessAccountAmountBeforeCoupon: Float,
    var businessAccountAmountAfterCoupon: Float,
    var finalTotalPriceForBusinessAccount: Float,
    var totalPriceBeforeCouponForCart: Float,
    var totalPriceForCart: Float
)

data class ProductCartItem(
    var cartproductId: Int,
    var id: Int,
    var img: String? = null,
    var name: String? = null,
    var qty: Int,
    var price: Float,
    var priceDiscount: Float,
    var categoryName: String? = null,
    var country: String? = null,
    var region: String? = null
)

