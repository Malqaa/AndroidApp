package com.malqaa.androidappp.newPhase.presentation.dialogsShared.currentPriceDialog

import android.content.Context
import com.malqaa.androidappp.databinding.DialogBuyCurrentPriceBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog

class BuyCurrentPriceDialog(
    context: Context,
    var totalPrice: String,
    var nameProduct: String,
    var onAttachedImageMethodSelected: OnAttachedCartMethodSelected
) : BaseDialog<DialogBuyCurrentPriceBinding>(context) {

    override fun inflateViewBinding(): DialogBuyCurrentPriceBinding {
        return DialogBuyCurrentPriceBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        binding.txtName.text = nameProduct
        binding.numTotal.text = totalPrice
        binding.txtGoCart.setOnClickListener {
            onAttachedImageMethodSelected.setOnGoCart()
            dismiss()
        }
        binding.txtFollowing.setOnClickListener {
            onAttachedImageMethodSelected.setOnFollowShopping()
            dismiss()
        }
    }

    interface OnAttachedCartMethodSelected {
        fun setOnGoCart()
        fun setOnFollowShopping()
    }
}