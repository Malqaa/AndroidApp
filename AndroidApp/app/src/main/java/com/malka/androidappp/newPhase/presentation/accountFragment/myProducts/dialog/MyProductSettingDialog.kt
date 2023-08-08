package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.dialog

import android.content.Context
import com.malka.androidappp.databinding.DialogMyProductSttingBinding
import com.malka.androidappp.newPhase.core.BaseBottomSheetDialog
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show

class MyProductSettingDialog(context: Context, var productsForSale:Boolean,var setOnSelectedListeners : SetOnSelectedListeners) : BaseBottomSheetDialog(context) {
    lateinit var viewBinding: DialogMyProductSttingBinding
    override fun setViewBinding() {
        viewBinding = DialogMyProductSttingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun initialization() {
        if(productsForSale){
            viewBinding.btnAddDiscount.show()
            viewBinding.btnModifyProduct.show()
            viewBinding.btnSendOffer.hide()
            viewBinding.btnRepostTheProduct.hide()
        }else{
            viewBinding.btnAddDiscount.hide()
            viewBinding.btnModifyProduct.hide()
            viewBinding.btnSendOffer.show()
            viewBinding.btnRepostTheProduct.show()
        }
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
        viewBinding.btnSendOffer.setOnClickListener {
            setOnSelectedListeners.onSendOfferProductToBidPersons()
            dismiss()
        }
        viewBinding.btnRepostTheProduct.setOnClickListener {
            setOnSelectedListeners.onRepostProduct()
            dismiss()
        }
    }

    interface SetOnSelectedListeners{
        fun onAddDiscount()
        fun onModifyProduct()
        fun onDeleteProduct()
        fun onSendOfferProductToBidPersons()
        fun onRepostProduct()



    }

}