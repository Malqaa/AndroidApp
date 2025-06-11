package com.malqaa.androidappp.newPhase.data.network.model

import androidx.annotation.StringRes
import com.malqaa.androidappp.R

enum class OrderStatus(val value: Int, @StringRes val labelRes: Int) {
    WaitingForPayment(value = 1, labelRes = R.string.waiting_for_payment),
    WaitingForReview(value = 2, labelRes = R.string.waiting_for_review),
    InProgress(value = 3, labelRes = R.string.in_progress),
    ReadyForDelivery(value = 4, labelRes = R.string.ready_for_delivery),
    DeliveryInProgress(value = 5, labelRes = R.string.delivery_in_progress),
    Delivered(value = 6, labelRes = R.string.delivered),
    Canceled(value = 7, labelRes = R.string.canceled),
    Retrieved(value = 8, labelRes = R.string.retrieved);

    companion object {
        fun fromInt(value: Int): OrderStatus? = values().find { it.value == value }
    }
}

enum class BankTransferPaymentStatus {
    Pending,
    Rejected,
    Accepted,
    UploadPeriodExpired,
    Uploaded,
    Confirmed
}

