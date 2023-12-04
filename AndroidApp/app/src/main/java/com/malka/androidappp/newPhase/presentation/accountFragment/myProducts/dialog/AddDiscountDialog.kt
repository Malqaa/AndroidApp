package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.TimePickerFragment
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.dialog_add_discount.*


class AddDiscountDialog(
    context: Context,
    var currentPrice: Float,
    var fm: FragmentManager,
    var setonClickListeners: SetonClickListeners
) :
    BaseDialog(context) {

    var selectdate = ""
    var selectTime = ""
    var dateString = ""

    //  var fm: FragmentManager? = null  supportFragmentManager
    interface SetonClickListeners {
        fun onAddDiscount(finaldate: String, newPrice: Float)
    }

    override fun getViewId(): Int = R.layout.dialog_add_discount

    override fun isLoadingDialog(): Boolean = false

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true

    override fun initialization() {
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            add_btn.setBackgroundResource(R.drawable.add_left_corner)
            subtract_btn.setBackgroundResource(R.drawable.add_rigt_corner)
        } else {
            subtract_btn.setBackgroundResource(R.drawable.add_left_corner)
            add_btn.setBackgroundResource(R.drawable.add_rigt_corner)
        }
        etPriceDiscound.setText(currentPrice.toString())
        tvCurrnetPrice.text = currentPrice.toString()
        setViewClickListeners()
    }

    private fun setViewClickListeners() {
        ivClose.setOnClickListener {
            dismiss()
        }
        tvClosingAuctionCustomDataOption2.setOnClickListener {
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                selectdate = selectdate_
                val timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    tvClosingAuctionCustomDataOption2.text = "$selectdate $selectTime"
                    dateString = "$selectdate $selectTime"
                }
                timeDialog.show(fm, "")

            }
            dateDialog.show(fm, "")
        }
        add_btn.setOnClickListener {
            var price = etPriceDiscound.text.trim().toString().toFloat()
            price += 1
            etPriceDiscound.setText(price.toString())
        }
        subtract_btn.setOnClickListener {
            var price = etPriceDiscound.text.trim().toString().toFloat()
            price -= 1
            etPriceDiscound.setText(price.toString())
        }
        complete_order_btn.setOnClickListener {
            if (etPriceDiscound.text.trim().toString() == "") {
                HelpFunctions.ShowLongToast(context.getString(R.string.addDiscount), context)
            } else if (etPriceDiscound.text.trim().toString().toFloat() > currentPrice) {
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
                val newPrice = etPriceDiscound.text.trim().toString().toFloat()
                setonClickListeners.onAddDiscount(finalDate, newPrice)
                dismiss()
            }
        }
    }


}