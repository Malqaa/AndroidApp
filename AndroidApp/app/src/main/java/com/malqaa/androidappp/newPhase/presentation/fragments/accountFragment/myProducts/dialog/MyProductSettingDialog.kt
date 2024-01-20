package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog

import android.content.Context
import com.malqaa.androidappp.databinding.DialogMyProductSttingBinding
import com.malqaa.androidappp.newPhase.core.BaseBottomSheetDialog
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MyProductSettingDialog(
    private val isAuctionEnabled: Boolean,
    private val auctionClosingTime: String,
    context: Context,
    var productsForSale: Boolean,
    var setOnSelectedListeners: SetOnSelectedListeners
) : BaseBottomSheetDialog(context) {
    lateinit var viewBinding: DialogMyProductSttingBinding
    var auctionTime: Date? = null
    override fun setViewBinding() {
        viewBinding = DialogMyProductSttingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    override fun initialization() {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        if (isAuctionEnabled) {
            auctionTime = df.parse(auctionClosingTime)
        }
        val c: Calendar = Calendar.getInstance()
        df.format(c.time)

        if (productsForSale) {
            if (isAuctionEnabled)
                viewBinding.btnAddDiscount.hide()
            else
                viewBinding.btnAddDiscount.show()
            viewBinding.btnModifyProduct.show()
            viewBinding.btnSendOffer.hide()
            viewBinding.btnRepostTheProduct.hide()
        } else {
            viewBinding.btnAddDiscount.hide()
            viewBinding.btnModifyProduct.hide()
            if (!isAuctionEnabled) {
                viewBinding.btnSendOffer.hide()
            } else {

                if (auctionTime?.before(c.time)!!)
                    viewBinding.btnSendOffer.show()
            }
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

    interface SetOnSelectedListeners {
        fun onAddDiscount()
        fun onModifyProduct()
        fun onDeleteProduct()
        fun onSendOfferProductToBidPersons()
        fun onRepostProduct()


    }

}