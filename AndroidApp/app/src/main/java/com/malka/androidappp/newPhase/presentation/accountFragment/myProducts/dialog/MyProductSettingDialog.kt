package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.dialog

import android.content.Context
import com.malka.androidappp.databinding.DialogMyProductSttingBinding
import com.malka.androidappp.newPhase.core.BaseBottomSheetDialog

class MyProductSettingDialog(context: Context,var setOnSelectedListeners : SetOnSelectedListeners) : BaseBottomSheetDialog(context) {
    lateinit var viewBinding: DialogMyProductSttingBinding
    override fun setViewBinding() {
        viewBinding = DialogMyProductSttingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun initialization() {
        viewBinding.btnAddDiscount.setOnClickListener {
            setOnSelectedListeners.onAddDiscount()
            dismiss()
        }
        viewBinding.btnModifyProduct.setOnClickListener {
            setOnSelectedListeners.onModifyProduct()
            dismiss()
        }
        viewBinding.btnDeleteProduct.setOnClickListener {
            setOnSelectedListeners.onDeleteProduct()
            dismiss()
        }
    }

    interface SetOnSelectedListeners{
        fun onAddDiscount()
        fun onModifyProduct()
        fun onDeleteProduct()
    }

}