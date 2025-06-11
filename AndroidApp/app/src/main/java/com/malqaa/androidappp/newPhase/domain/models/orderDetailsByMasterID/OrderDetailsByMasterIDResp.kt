package com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class OrderDetailsByMasterIDResp(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("data") val orderDetailsByMasterIDData: OrderDetailsByMasterIDData? = null
)

data class OrderDetailsByMasterIDData(
    @SerializedName("orderMasterId") val orderMasterId: Int,
    @SerializedName("requestType") val requestType: String? = null,
    @SerializedName("paymentType") val paymentType: String,
    @SerializedName("orderStatus") val orderStatus: Int,
    @SerializedName("status") val status: String,
    @SerializedName("totalOrderMasterAmountBeforDiscount") val totalOrderMasterAmountBeforDiscount: Float? = null,
    @SerializedName("totalOrderMasterAmountAfterDiscount") val totalOrderMasterAmountAfterDiscount: Float? = null,
    @SerializedName("clientName") val clientName: String? = null,
    @SerializedName("providersCount") val providersCount: Int,
    @SerializedName("shippingFee") val shippingFee: Int,
    @SerializedName("orderFullInfoDto") val orderFullInfoDtoList: List<OrderFullInfoDto>? = null,
    @SerializedName("paymentTypeId") val paymentTypeId: Int
)

@Parcelize
data class OrderFullInfoDto(
    @SerializedName("orderId") val orderId: Int,
    @SerializedName("providerId") val providerId: String? = null,
    @SerializedName("providerName") val providerName: String? = null,
    @SerializedName("businessAcountId") val businessAcountId: Int? = null,
    @SerializedName("businessAcountName") val businessAcountName: String? = null,
    @SerializedName("totalOrderPrice") val totalOrderPrice: Float,
    @SerializedName("shippingFee") val shippingFee: Int,
    @SerializedName("orderProductFullInfoDto") val orderProductFullInfoDto: List<OrderProductFullInfoDto>? = null,
    @SerializedName("orderStatus") val orderStatus: Int,
    @SerializedName("status") val status: String? = null,
    @SerializedName("clientId") val clientId: String? = null,
    @SerializedName("clientName") val clientName: String? = null,
    @SerializedName("clientEmail") val clientEmail: String? = null,
    @SerializedName("clientImage") val clientImage: String? = null,
    @SerializedName("branchId") val branchId: Int,
    @SerializedName("orderSaleType") val orderSaleType: String,
    @SerializedName("orderSaleTypeId") val orderSaleTypeId: Int,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("shippingCount") val shippingCount: Int,
    @SerializedName("shippingAddress") val shippingAddress: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("requestType") val requestType: String? = null,
    @SerializedName("paymentType") val paymentType: String? = null,
    @SerializedName("paymentTypeId") val paymentTypeId: Int = 0,
    @SerializedName("totalOrderAmountBeforDiscount") val totalOrderAmountBeforDiscount: Float? = null,
    @SerializedName("totalOrderAmountAfterDiscount") val totalOrderAmountAfterDiscount: Float? = null,
    @SerializedName("orderInvoice") val orderInvoice: String,
    @SerializedName("bankTransferPaymentStatus") val bankTransferPaymentStatus: String,
    @SerializedName("confirmationCode") val confirmationCode: String,
    @SerializedName("productBankAccountsDto") val productBankAccountsDto: List<ProductBankAccountsDto>? = null
) : Parcelable


@Parcelize
data class OrderProductFullInfoDto(
    @SerializedName("productId") val productId: Int,
    @SerializedName("providerId") val providerId: String? = "",
    @SerializedName("providerName") val providerName: String? = null,
    @SerializedName("businessAcountId") val businessAcountId: String? = "",
    @SerializedName("businessAcountName") val businessAcountName: String? = null,
    @SerializedName("productName") val productName: String? = null,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Float,
    @SerializedName("priceDiscount") val priceDiscount: Float,
    @SerializedName("iamge") val iamge: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("region") val region: String? = null,
    @SerializedName("productSpecifications") val productSpecifications: List<ProductSpecification>? = null,
    @SerializedName("midea") val midea: List<Midea>,
    @SerializedName("paymentOption") val paymentOption: String? = null,
    @SerializedName("shippingOption") val shippingOption: String? = null,
    @SerializedName("shippingOptions") val shippingOptions: List<Int>? = null,
    @SerializedName("paymentOptions") val paymentOptions: List<Int>? = null,
    @SerializedName("productBankAccountsDto") val productBankAccountsDto: List<ProductBankAccountsDto>? = null,
    @SerializedName("orderStatus") val orderStatus: Int = 0,
    @SerializedName("status") val status: String? = null,
    @SerializedName("clientId") val clientId: String? = null,
    @SerializedName("clientName") val clientName: String? = null,
    @SerializedName("clientEmail") val clientEmail: String? = null,
    @SerializedName("clientImage") val clientImage: String? = null,
    @SerializedName("branchId") val branchId: String? = null,
    @SerializedName("orderSaleType") val orderSaleType: String? = null,
    @SerializedName("orderSaleTypeId") val orderSaleTypeId: Int? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("shippingCount") val shippingCount: Int? = null,
    @SerializedName("shippingAddress") val shippingAddress: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("requestType") val requestType: String? = null,
    @SerializedName("paymentType") val paymentType: String? = null,
    @SerializedName("paymentTypeId") val paymentTypeId: Int? = null,
    @SerializedName("totalOrderAmountBeforDiscount") val totalOrderAmountBeforDiscount: Double? = null,
    @SerializedName("totalOrderAmountAfterDiscount") val totalOrderAmountAfterDiscount: Double? = null,
    @SerializedName("orderInvoice") val orderInvoice: String? = null,
    @SerializedName("bankTransferPaymentStatus") val bankTransferPaymentStatus: String? = null,
    @SerializedName("confirmationCode") val confirmationCode: Int? = null,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("rate") var rate: Int = 0,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("productRateId") var productRateId: Int = 0
) : Parcelable

@Parcelize
data class ProductBankAccountsDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("accountNumber") val accountNumber: String = "",
    @SerializedName("bankName") val bankName: String = "",
    @SerializedName("bankHolderName") val bankHolderName: String = "",
    @SerializedName("ibanNumber") val ibanNumber: String = "",
    @SerializedName("swiftCode") val swiftCode: String = ""
) : Parcelable

@Parcelize
data class ProductSpecification(
    @SerializedName("headerSpeAr") val headerSpeAr: String? = null,
    @SerializedName("headerSpeEn") val headerSpeEn: String? = null,
    @SerializedName("id") val id: Int,
    @SerializedName("product") val product: String? = null,
    @SerializedName("productId") val productId: Int,
    @SerializedName("type") val type: Int,
    @SerializedName("valueSpeAr") val valueSpeAr: String? = null,
    @SerializedName("valueSpeEn") val valueSpeEn: String? = null,
) : Parcelable

@Parcelize
data class Midea(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String? = null,
    @SerializedName("type") val type: Int,
    @SerializedName("isMainMadia") val isMainMadia: Boolean,
    @SerializedName("product") val product: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("productId") val productId: Int,
) : Parcelable