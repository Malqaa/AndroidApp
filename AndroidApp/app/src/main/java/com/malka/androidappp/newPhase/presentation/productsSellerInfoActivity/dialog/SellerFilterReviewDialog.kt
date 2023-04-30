package com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.dialog

import android.content.Context
import android.widget.CompoundButton
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show

import kotlinx.android.synthetic.main.dialog_seller_filter_rate.*

class SellerFilterReviewDialog(context: Context,var applySellerReviewFilter:ApplySellerReviewFilter) : BaseDialog(context) {
    var selectionTap: Int = sellerOrBayerFilterTap;
   var getReviewAsAsSellerOrBuyer=sellerAsASeller
    var getReviewType=8

    companion object { // 1 for region ,2 for sub category  ,3 for specification
        const val sellerOrBayerFilterTap = 1
        const val reviewTypeTap = 2
        const val sellerAsASeller = 3
        const val sellerAsABuyer = 4
        const val happyReview = 1
        const val satisfiedReview = 2
        const val unsatisfiedReview = 3
        const val allReview = 8
    }

    override fun getViewId(): Int {
        return R.layout.dialog_seller_filter_rate
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean =false

    override fun initialization() {
        review_type2.setOnClickListener {
            ContainerReviewAsAsAsSellerOrBuyer.show()
            containerReviewTypes.hide()
        }
        review_type1.setOnClickListener {
            ContainerReviewAsAsAsSellerOrBuyer.hide()
            containerReviewTypes.show()
        }
        cbReviewsAsSeller.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewAsAsSellerOrBuyer = sellerAsASeller
                review_type2.text=context.getString(R.string.reviews_as_a_seller)
                cbReviewsAsBuyer.isChecked = false
            }
        }
        cbReviewsAsBuyer.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewAsAsSellerOrBuyer = sellerAsABuyer
                cbReviewsAsSeller.isChecked = false
                review_type2.text=context.getString(R.string.reviews_as_a_buyer)
            }
        }
        cbReviewsHappy.setOnCheckedChangeListener { p0, p1 ->
          if(p1){
              getReviewType = happyReview
              cbReviewsSatisfied.isChecked = false
              cbReviewsNotSatisfied.isChecked = false
          }
        }
        cbReviewsSatisfied.setOnCheckedChangeListener { p0, p1 ->
            if(p1){
                getReviewType = satisfiedReview
                cbReviewsHappy.isChecked = false
                cbReviewsNotSatisfied.isChecked = false
            }
        }
        cbReviewsNotSatisfied.setOnCheckedChangeListener { p0, p1 ->
            if(p1){
                getReviewType = unsatisfiedReview
                cbReviewsHappy.isChecked = false
                cbReviewsSatisfied.isChecked = false
            }
        }
        btnApplyFilter.setOnClickListener {
            applySellerReviewFilter.onApplyFilter(getReviewType,getReviewAsAsSellerOrBuyer)
        }
    }

    fun setSelectedTap(selectedOne: Int,getReviewAsAsSellerOrBuyerOption:Int,reviewType:Int) {
        getReviewAsAsSellerOrBuyer=getReviewAsAsSellerOrBuyerOption
        selectionTap = selectedOne
        getReviewType=reviewType
        when (selectionTap) {
            sellerOrBayerFilterTap -> {
                review_type2.performClick()
            }
            reviewTypeTap -> {
                review_type1.performClick()
            }
        }
        when(getReviewAsAsSellerOrBuyer){
            sellerAsASeller->{
                cbReviewsAsSeller.isChecked=true
                cbReviewsAsBuyer.isChecked=false
                review_type2.text=context.getString(R.string.reviews_as_a_seller)
            }
            sellerAsABuyer->{
                cbReviewsAsSeller.isChecked=false
                cbReviewsAsBuyer.isChecked=true
                review_type2.text=context.getString(R.string.reviews_as_a_buyer)
            }
        }
        when(getReviewType){
            allReview->{
                cbReviewsHappy.isChecked=false
                cbReviewsSatisfied.isChecked=false
                cbReviewsNotSatisfied.isChecked=false
            }
            happyReview->{
                cbReviewsHappy.isChecked=true
                cbReviewsSatisfied.isChecked=false
                cbReviewsNotSatisfied.isChecked=false
            }
            satisfiedReview->{
                cbReviewsHappy.isChecked=false
                cbReviewsSatisfied.isChecked=true
                cbReviewsNotSatisfied.isChecked=false
            }
            unsatisfiedReview->{
                cbReviewsHappy.isChecked=false
                cbReviewsSatisfied.isChecked=false
                cbReviewsNotSatisfied.isChecked=true
            }
        }
    }

    interface ApplySellerReviewFilter{
        fun onApplyFilter(reviewType:Int,rateAsSellerOrBuyer:Int)
    }


}