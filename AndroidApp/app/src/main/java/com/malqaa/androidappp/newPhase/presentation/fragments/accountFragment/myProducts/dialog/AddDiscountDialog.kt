package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogAddDiscountBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.malqaa.androidappp.newPhase.utils.helper.widgets.TimePickerFragment


class AddDiscountDialog(
    context: Context,
    var currentPrice: Float,
    var fm: FragmentManager,
    var setonClickListeners: SetonClickListeners
) : BaseDialog<DialogAddDiscountBinding>(context) {

    var selectdate = ""
    var selectTime = ""
    var dateString = ""

    interface SetonClickListeners {
        fun onAddDiscount(finaldate: String, newPrice: Float)
    }

    override fun isLoadingDialog(): Boolean = false

    override fun inflateViewBinding(): DialogAddDiscountBinding {
        return DialogAddDiscountBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun initialization() {
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            binding.addBtn.setBackgroundResource(R.drawable.add_left_corner)
            binding.subtractBtn.setBackgroundResource(R.drawable.add_rigt_corner)
        } else {
            binding.subtractBtn.setBackgroundResource(R.drawable.add_left_corner)
            binding.addBtn.setBackgroundResource(R.drawable.add_rigt_corner)
        }
        binding.etPriceDiscound.setText(currentPrice.toString())
        binding.tvCurrnetPrice.text = currentPrice.toString()
        setViewClickListeners()
    }

    private fun setViewClickListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvClosingAuctionCustomDataOption2.setOnClickListener {
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                selectdate = selectdate_
                val timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    binding.tvClosingAuctionCustomDataOption2.text = "$selectdate $selectTime"
                    dateString = "$selectdate $selectTime"
                }
                timeDialog.show(fm, "")

            }
            dateDialog.show(fm, "")
        }
        binding.addBtn.setOnClickListener {
            var price = binding.etPriceDiscound.text.trim().toString().toFloat()
            price += 1
            binding.etPriceDiscound.setText(price.toString())
        }
        binding.subtractBtn.setOnClickListener {
            var price = binding.etPriceDiscound.text.trim().toString().toFloat()
            price -= 1
            binding.etPriceDiscound.setText(price.toString())
        }
        binding.completeOrderBtn.setOnClickListener {
            if (binding.etPriceDiscound.text.trim().toString() == "") {
                HelpFunctions.ShowLongToast(context.getString(R.string.addDiscount), context)
            } else if (binding.etPriceDiscound.text.trim().toString().toFloat() > currentPrice) {
                HelpFunctions.ShowLongToast(
                    context.getString(R.string.discountPriceShouldBeLower),
                    context
                )
            } else if (selectdate == "" || selectTime == "" || dateString == "") {
                HelpFunctions.ShowLongToast(
                    context.getString(R.string.addDiscountEndTime),
                    context
                )
            } else {
                val finalDate = HelpFunctions.getFormattedDate2(
                    dateString,
                    HelpFunctions.datetimeformat_hrs,
                    HelpFunctions.datetimeformat_hrs
                )
                val newPrice = binding.etPriceDiscound.text.trim().toString().toFloat()
                setonClickListeners.onAddDiscount(dateString, newPrice)
                dismiss()
            }
        }
    }


}