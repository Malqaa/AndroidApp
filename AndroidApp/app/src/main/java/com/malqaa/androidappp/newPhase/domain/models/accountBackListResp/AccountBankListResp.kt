package com.malqaa.androidappp.newPhase.domain.models.accountBackListResp

import com.google.gson.annotations.SerializedName
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.enums.PaymentMethod
import java.io.File


data class BankTransfersListResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("message") val message: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("data") val accountsList: ArrayList<BankTransfers>? = null
)

data class BankTransfers(
    @SerializedName("id") var id: Int,
    @SerializedName("accountNumber") var accountNumber: String? = null,
    @SerializedName("bankName") var bankName: String? = null,
    @SerializedName("bankHolderName") var bankHolderName: String? = null,
    @SerializedName("ibanNumber") var ibanNumber: String? = null,
    @SerializedName("expiaryDate") var expiaryDate: String? = null,
    @SerializedName("swiftCode") var swiftCode: String? = null,
    @SerializedName("providerId") var providerId: String? = null,
    @SerializedName("isActive") var isActive: Boolean? = null,
    @SerializedName("saveForLaterUse") var saveForLaterUse: Boolean? = null,
    @SerializedName("paymentAccountType") var paymentAccountType: String? = null,
    @SerializedName("ibanCertificate") var ibanCertificate: String? = null,
)


data class AccountBankListResp(
    val code: String? = null,
    val message: String? = null,
    val status_code: Int,
    val status: String? = null,
    @SerializedName("data")
    val accountsList: ArrayList<AccountDetails>? = null
)

data class AccountDetails(
    var id: Int,
    var accountNumber: String? = null,
    var bankName: String? = null,
    var bankHolderName: String? = null,
    var ibanNumber: String? = null,
    var expiaryDate: String? = null,
    var swiftCode: String? = null,
    var providerId: String? = null,
    var isActive: String? = null,
    var saveForLaterUse: String? = null,
    var ibanCertificate: String? = null,
    var bankAccountId: Int,
    var ibanCertificateFile: String? = null,
    var isSelected: Boolean,
    var cvv: Int,
    var paymentAccountType: PaymentAccountType? = null,
    var paymentMethod: PaymentMethod? = null,
)

data class BankAccountRequest(
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("bankName") val bankName: String? = null,
    @SerializedName("bankHolderName") val bankHolderName: String,
    @SerializedName("ibanNumber") val ibanNumber: String? = null,
    @SerializedName("swiftCode") val swiftCode: String? = null,
    @SerializedName("expiaryDate") val expiryDate: String,
    @SerializedName("SaveForLaterUse") val saveForLaterUse: Boolean,
    @SerializedName("PaymentAccountType") val paymentAccountType: String? = null
)

data class EditBankTransferRequest(
    @SerializedName("id") val id: Int,
    @SerializedName("AccountNumber") val accountNumber: String,
    @SerializedName("BankName") val bankName: String,
    @SerializedName("BankHolderName") val bankHolderName: String,
    @SerializedName("IbanNumber") val ibanNumber: String,
    @SerializedName("SwiftCode") val swiftCode: String,
    @SerializedName("IbanCertificate") val ibanCertificate: String,
    @SerializedName("IbanCertificateFile") val ibanCertificateFile: String,
    @SerializedName("ExpiaryDate") val expiaryDate: String,
    @SerializedName("SaveForLaterUse") val saveForLaterUse: Boolean,
    @SerializedName("PaymentAccountType") val paymentAccountType: String,
)

data class EditBankTransfer(
    val id: Int,
    val accountNumber: String,
    val bankName: String,
    val bankHolderName: String,
    val ibanNumber: String,
    val swiftCode: String,
    val expiryDate: String,
    val ibanCertificate: String,
    val ibanCertificateFile: File? = null,
    val saveForLaterUse: Boolean,
    val paymentAccountType: String,
)

// =================================================================================================
// ✅ From AccountDetails to EditBankTransferRequest
// =================================================================================================
fun AccountDetails.toEditBankTransferRequest(
    ibanCertificate: String,
    ibanCertificateFile: String
): EditBankTransferRequest {
    return EditBankTransferRequest(
        id = this.id,
        accountNumber = this.accountNumber.orEmpty(),
        bankName = this.bankName.orEmpty(),
        bankHolderName = this.bankHolderName.orEmpty(),
        ibanNumber = this.ibanNumber.orEmpty(),
        swiftCode = this.swiftCode.orEmpty(),
        ibanCertificate = ibanCertificate,
        ibanCertificateFile = ibanCertificateFile,
        expiaryDate = this.expiaryDate.orEmpty(),
        saveForLaterUse = this.saveForLaterUse == "true",
        paymentAccountType = this.paymentAccountType?.value.toString()
    )
}
// =================================================================================================

// =================================================================================================
// ✅ From EditBankTransferRequest to AccountDetails
// =================================================================================================
fun EditBankTransferRequest.toAccountDetails(): AccountDetails {
    return AccountDetails(
        id = this.id,
        accountNumber = this.accountNumber,
        bankAccountId = this.id, // or 0 if different logic needed
        bankName = this.bankName,
        bankHolderName = this.bankHolderName,
        ibanNumber = this.ibanNumber,
        expiaryDate = this.expiaryDate,
        swiftCode = this.swiftCode,
        providerId = null,
        isActive = null,
        saveForLaterUse = this.saveForLaterUse.toString(),
        isSelected = false,
        cvv = 0,
        paymentAccountType = PaymentAccountType.values()
            .find { it.paymentType == this.paymentAccountType },
    )
}
// =================================================================================================
