package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.dialogs

import android.content.Context
import com.malqaa.androidappp.databinding.DialogOrderStatusBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.model.OrderStatus
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class OrderStatusDialog(
    context: Context,
    val status: Int,
    private var setOnSelectOrderStatus: SetOnSelectOrderStatus
) : BaseDialog<DialogOrderStatusBinding>(context) {

    interface SetOnSelectOrderStatus {
        fun onSelectOrderStatus(orderStatus: Int)
    }

    override fun inflateViewBinding(): DialogOrderStatusBinding {
        return DialogOrderStatusBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        val currentStatus = OrderStatus.fromInt(status)

        if (status >= OrderStatus.InProgress.value) {
            binding.tvCanceled.hide()
        }

        // Hide current and previous statuses
        when (currentStatus) {
            OrderStatus.WaitingForPayment -> {
                binding.tvWaitingForPayment.hide()
            }

            OrderStatus.WaitingForReview -> {
                binding.tvWaitingForPayment.hide()
                binding.tvWaitingForReview.hide()
            }

            OrderStatus.InProgress -> {
                binding.tvWaitingForPayment.hide()
                binding.tvWaitingForReview.hide()
                binding.tvInProgress.hide()
            }

            OrderStatus.ReadyForDelivery -> {
                binding.tvWaitingForPayment.hide()
                binding.tvWaitingForReview.hide()
                binding.tvInProgress.hide()
                binding.tvReadyForDelivery.hide()
            }

            OrderStatus.DeliveryInProgress -> {
                binding.tvWaitingForPayment.hide()
                binding.tvWaitingForReview.hide()
                binding.tvInProgress.hide()
                binding.tvReadyForDelivery.hide()
                binding.tvDeliveryInProgress.hide()
            }

            OrderStatus.Delivered -> {
                binding.tvWaitingForPayment.hide()
                binding.tvWaitingForReview.hide()
                binding.tvInProgress.hide()
                binding.tvReadyForDelivery.hide()
                binding.tvDeliveryInProgress.hide()
                binding.tvDelivered.hide()
            }

            else -> {}
        }

        // Show retrieved only if Delivered
        if (currentStatus == OrderStatus.Delivered) {
            binding.tvRetrieved.show()
        } else {
            binding.tvRetrieved.hide()
        }

        // Click Listeners
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.ivClose.setOnClickListener { dismiss() }

        binding.tvWaitingForPayment.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.WaitingForPayment.value)
            dismiss()
        }
        binding.tvWaitingForReview.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.WaitingForReview.value)
            dismiss()
        }
        binding.tvInProgress.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.InProgress.value)
            dismiss()
        }
        binding.tvReadyForDelivery.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.ReadyForDelivery.value)
            dismiss()
        }
        binding.tvDeliveryInProgress.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.DeliveryInProgress.value)
            dismiss()
        }
        binding.tvDelivered.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.Delivered.value)
            dismiss()
        }
        binding.tvCanceled.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(OrderStatus.Canceled.value)
            dismiss()
        }
    }
}
