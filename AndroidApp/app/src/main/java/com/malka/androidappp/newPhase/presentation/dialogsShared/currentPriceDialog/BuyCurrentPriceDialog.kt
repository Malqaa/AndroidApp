package com.malka.androidappp.newPhase.presentation.dialogsShared.currentPriceDialog

import android.content.Context
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import kotlinx.android.synthetic.main.dialog_buy_current_price.*

class BuyCurrentPriceDialog(context: Context,var totalPrice:String ,var nameProduct:String ,var onAttachedImageMethodSelected:OnAttachedCartMethodSelected
) : BaseDialog(context) {

    override fun getViewId(): Int = R.layout.dialog_buy_current_price


    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true


    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        txt_name.text = nameProduct
        num_total.text = totalPrice
        txt_go_cart.setOnClickListener {
            onAttachedImageMethodSelected.setOnGoCart()
            dismiss()
        }
        txt_following.setOnClickListener {
            onAttachedImageMethodSelected.setOnFollowShopping()
            dismiss()
        }
    }
    interface OnAttachedCartMethodSelected {
        fun setOnGoCart()
        fun setOnFollowShopping()
    }
}