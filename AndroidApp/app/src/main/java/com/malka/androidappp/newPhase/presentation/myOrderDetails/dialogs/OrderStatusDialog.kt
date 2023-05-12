package com.malka.androidappp.newPhase.presentation.myOrderDetails.dialogs

import android.content.Context
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.dialog_order_status.*


class OrderStatusDialog(
    context: Context,
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
        btnCancel.setOnClickListener {
            dismiss()
        }
        ivClose.setOnClickListener {
            dismiss()
        }
        tvProductsProcessing.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.orderStatus_provider_inProgress)
        }
        tvDeliveryPhase.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.orderStatus_provider_inDelivery)
        }
        tvDeliveryConfirmation.setOnClickListener {
            setOnSelectOrderStatus.onSelectOrderStatus(ConstantObjects.orderStatus_provider_finished)
        }

    }


}