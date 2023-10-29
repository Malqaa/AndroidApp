package com.malka.androidappp.newPhase.presentation.myOrderDetails.dialogs

import android.content.Context
import android.view.View
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import kotlinx.android.synthetic.main.dialog_order_status.*


class OrderStatusDialog(
    context: Context,
    val status:Int,
    var setOnSelectOrderStatus: OrderStatusDialog.SetOnSelectOrderStatus
) :
    BaseDialog(context) {


    interface SetOnSelectOrderStatus {
        fun onSelectOrderStatus(orderStatus: Int)
    }

    override fun getViewId(): Int = R.layout.dialog_order_status

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        if(status>=3){
            tvCanceled.hide()
        }
        when(status){
            ConstantObjects.WaitingForPayment->{
                tvWaitingForPayment.hide()
                tvWaitingForReview.show()
                tvInProgress.show()
                tvReadyForDelivery.show()
                tvDeliveryInProgress.show()
                tvDelivered.show()
                tvRetrieved.hide()
            }
            ConstantObjects.WaitingForReview->{
                tvWaitingForReview.hide()
                tvWaitingForPayment.hide()
                tvInProgress.show()
                tvReadyForDelivery.show()
                tvDeliveryInProgress.show()
                tvDelivered.show()
                tvRetrieved.hide()
            }
            ConstantObjects.InProgress->{
                tvInProgress.hide()
                tvWaitingForReview.hide()
                tvWaitingForPayment.hide()
                tvReadyForDelivery.show()
                tvDeliveryInProgress.show()
                tvDelivered.show()
                tvRetrieved.hide()

            }
            ConstantObjects.ReadyForDelivery->{
                tvReadyForDelivery.hide()
                tvInProgress.hide()
                tvWaitingForReview.hide()
                tvWaitingForPayment.hide()
                tvDeliveryInProgress.show()
                tvDelivered.show()
                tvRetrieved.hide()

            }
            ConstantObjects.DeliveryInProgress->{
                tvDeliveryInProgress.hide()
                tvReadyForDelivery.hide()
                tvInProgress.hide()
                tvWaitingForReview.hide()
                tvWaitingForPayment.hide()
                tvDelivered.show()
                tvRetrieved.hide()

            }
            ConstantObjects.Delivered->{
                tvDelivered.hide()
                tvDeliveryInProgress.hide()
                tvReadyForDelivery.hide()
                tvInProgress.hide()
                tvWaitingForReview.hide()
                tvWaitingForPayment.hide()
                tvRetrieved.show()

            }
        }


        btnCancel.setOnClickListener {
            dismiss()
        }
        ivClose.setOnClickListener {
            dismiss()
        }

        tvWaitingForPayment.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.WaitingForPayment)
            dismiss()
        }
        tvWaitingForReview.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.WaitingForReview)
            dismiss()
        }
        tvInProgress.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.InProgress)
            dismiss()
        }
        tvReadyForDelivery.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.ReadyForDelivery)
            dismiss()
        }
        tvDeliveryInProgress.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.DeliveryInProgress)
            dismiss()
        }
        tvDelivered.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.Delivered)
            dismiss()
        }

        tvCanceled.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.Canceled)
            dismiss()
        }

    }


}