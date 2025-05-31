package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.dialogs

import android.content.Context
import com.malqaa.androidappp.databinding.DialogOrderStatusBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show


class OrderStatusDialog(
    context: Context,
    val status: Int,
    var setOnSelectOrderStatus: SetOnSelectOrderStatus
) :
    BaseDialog<DialogOrderStatusBinding>(context) {


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
        if (status >= 3) {
            binding.tvCanceled.hide()
        }
        when (status) {
            ConstantObjects.WaitingForPayment -> {
                binding.tvWaitingForPayment.hide()
                binding.tvWaitingForReview.show()
                binding.tvInProgress.show()
                binding.tvReadyForDelivery.show()
                binding.tvDeliveryInProgress.show()
                binding.tvDelivered.show()
                binding.tvRetrieved.hide()
            }

            ConstantObjects.WaitingForReview -> {
                binding.tvWaitingForReview.hide()
                binding.tvWaitingForPayment.hide()
                binding.tvInProgress.show()
                binding.tvReadyForDelivery.show()
                binding.tvDeliveryInProgress.show()
                binding.tvDelivered.show()
                binding.tvRetrieved.hide()
            }

            ConstantObjects.InProgress -> {
                binding.tvInProgress.hide()
                binding.tvWaitingForReview.hide()
                binding.tvWaitingForPayment.hide()
                binding.tvReadyForDelivery.show()
                binding.tvDeliveryInProgress.show()
                binding.tvDelivered.show()
                binding.tvRetrieved.hide()
            }

            ConstantObjects.ReadyForDelivery -> {
                binding.tvReadyForDelivery.hide()
                binding.tvInProgress.hide()
                binding.tvWaitingForReview.hide()
                binding.tvWaitingForPayment.hide()
                binding.tvDeliveryInProgress.show()
                binding.tvDelivered.show()
                binding.tvRetrieved.hide()
            }

            ConstantObjects.DeliveryInProgress -> {
                binding.tvDeliveryInProgress.hide()
                binding.tvReadyForDelivery.hide()
                binding.tvInProgress.hide()
                binding.tvWaitingForReview.hide()
                binding.tvWaitingForPayment.hide()
                binding.tvDelivered.show()
                binding.tvRetrieved.hide()
            }

            ConstantObjects.Delivered -> {
                binding.tvDelivered.hide()
                binding.tvDeliveryInProgress.hide()
                binding.tvReadyForDelivery.hide()
                binding.tvInProgress.hide()
                binding.tvWaitingForReview.hide()
                binding.tvWaitingForPayment.hide()
                binding.tvRetrieved.show()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvWaitingForPayment.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.WaitingForPayment)
            dismiss()
        }
        binding.tvWaitingForReview.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.WaitingForReview)
            dismiss()
        }
        binding.tvInProgress.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.InProgress)
            dismiss()
        }
        binding.tvReadyForDelivery.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.ReadyForDelivery)
            dismiss()
        }
        binding.tvDeliveryInProgress.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.DeliveryInProgress)
            dismiss()
        }
        binding.tvDelivered.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.Delivered)
            dismiss()
        }
        binding.tvCanceled.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.Canceled)
            dismiss()
        }
    }
}