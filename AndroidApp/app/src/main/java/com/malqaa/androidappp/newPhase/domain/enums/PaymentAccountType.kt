package com.malqaa.androidappp.newPhase.domain.enums

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

enum class PaymentMethod(
    val paymentType: String,
    val value: Int,
    val paymentCardType: Int? = null
) {
    Cash(paymentType = "Cash", value = 1),
    BankTransfer(paymentType = "BankTransfer", value = 2, paymentCardType = 3),
    CreditCard(paymentType = "CreditCard", value = 3, paymentCardType = 1),
    Mada(paymentType = "Mada", value = 4, paymentCardType = 2),
    Wallet(paymentType = "Wallet", value = 5),
    Points(paymentType = "Points", value = 6);

    companion object {
        fun fromValue(value: Int): PaymentMethod? = values().find { it.value == value }
    }
}
