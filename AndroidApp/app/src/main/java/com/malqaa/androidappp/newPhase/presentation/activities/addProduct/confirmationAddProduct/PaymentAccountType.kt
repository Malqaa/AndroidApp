package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct

enum class PaymentAccountType(
    val paymentType: String,
    val value: Int,
    val paymentMethod: Int? = null
) {
    VisaMasterCard(paymentType = "VisaMasterCard", value = 1),
    Mada(paymentType = "Mada", value = 2, paymentMethod = 4),
    BankAccount(paymentType = "BankAccount", value = 3),
    Points(paymentType = "Points", value = 6);

    companion object {
        fun fromValue(value: Int): PaymentAccountType? = values().find { it.value == value }
    }
}
