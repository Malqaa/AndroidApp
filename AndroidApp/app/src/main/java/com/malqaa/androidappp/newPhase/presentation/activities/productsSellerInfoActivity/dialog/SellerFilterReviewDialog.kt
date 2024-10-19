package com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.dialog

import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogSellerFilterRateBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class SellerFilterReviewDialog(
    context: Context,
    var applySellerReviewFilter: ApplySellerReviewFilter
) : BaseDialog<DialogSellerFilterRateBinding>(context) {
    var selectionTap: Int = sellerOrBayerFilterTap;
    var getReviewAsAsSellerOrBuyer = sellerAsASeller
    var getReviewType = 8

    companion object { // 1 for region ,2 for sub category  ,3 for specification
        const val sellerOrBayerFilterTap = 1
        const val reviewTypeTap = 2
        const val sellerAsASeller = 3
        const val sellerAsABuyer = 4
        const val sellerAsAll = 5
        const val happyReview = 1
        const val satisfiedReview = 2
        const val unsatisfiedReview = 3
        const val allReview = 8
    }

    override fun inflateViewBinding(): DialogSellerFilterRateBinding {
        return DialogSellerFilterRateBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false

    override fun initialization() {
        binding.reviewType2.setOnClickListener {
            binding.ContainerReviewAsAsAsSellerOrBuyer.show()
            binding.containerReviewTypes.hide()
        }
        binding.reviewType1.setOnClickListener {
            binding.ContainerReviewAsAsAsSellerOrBuyer.hide()
            binding.containerReviewTypes.show()
        }
        binding.cbReviewsAsAll.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewAsAsSellerOrBuyer = sellerAsAll
                binding.reviewType2.text = context.getString(R.string.all)
                binding.cbReviewsAsSeller.isChecked = false
                binding.cbReviewsAsBuyer.isChecked = false
            }
        }
        binding.cbReviewsAsSeller.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewAsAsSellerOrBuyer = sellerAsASeller
                binding.reviewType2.text = context.getString(R.string.reviews_as_a_seller)
                binding.cbReviewsAsAll.isChecked = false
                binding.cbReviewsAsBuyer.isChecked = false
            }
        }
        binding.cbReviewsAsBuyer.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewAsAsSellerOrBuyer = sellerAsABuyer
                binding.cbReviewsAsAll.isChecked = false
                binding.cbReviewsAsSeller.isChecked = false
                binding.reviewType2.text = context.getString(R.string.reviews_as_a_buyer)
            }
        }
        binding.cbReviewsHappy.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewType = happyReview
                binding.cbReviewsSatisfied.isChecked = false
                binding.cbReviewsNotSatisfied.isChecked = false
            }
        }
        binding.cbReviewsSatisfied.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewType = satisfiedReview
                binding.cbReviewsHappy.isChecked = false
                binding.cbReviewsNotSatisfied.isChecked = false
            }
        }
        binding.cbReviewsNotSatisfied.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                getReviewType = unsatisfiedReview
                binding.cbReviewsHappy.isChecked = false
                binding.cbReviewsSatisfied.isChecked = false
            }
        }
        binding.btnApplyFilter.setOnClickListener {
            applySellerReviewFilter.onApplyFilter(getReviewType, getReviewAsAsSellerOrBuyer)
        }
        binding.btnResetFilter.setOnClickListener {
            selectionTap = sellerOrBayerFilterTap;
            getReviewAsAsSellerOrBuyer = sellerAsASeller
            getReviewType = 8
            applySellerReviewFilter.onResetFilter()
        }
    }

    fun setSelectedTap(selectedOne: Int, getReviewAsAsSellerOrBuyerOption: Int, reviewType: Int) {
        getReviewAsAsSellerOrBuyer = getReviewAsAsSellerOrBuyerOption
        selectionTap = selectedOne
        getReviewType = reviewType
        when (selectionTap) {
            sellerOrBayerFilterTap -> {
                binding.reviewType2.performClick()
            }

            reviewTypeTap -> {
                binding.reviewType1.performClick()
            }
        }
        when (getReviewAsAsSellerOrBuyer) {
            sellerAsAll -> {
                binding.cbReviewsAsAll.isChecked = true
                binding.cbReviewsAsSeller.isChecked = false
                binding.cbReviewsAsBuyer.isChecked = false
                binding.reviewType2.text = context.getString(R.string.all)
            }

            sellerAsASeller -> {
                binding.cbReviewsAsAll.isChecked = false
                binding.cbReviewsAsSeller.isChecked = true
                binding.cbReviewsAsBuyer.isChecked = false
                binding.reviewType2.text = context.getString(R.string.reviews_as_a_seller)
            }

            sellerAsABuyer -> {
                binding.cbReviewsAsAll.isChecked = false
                binding.cbReviewsAsSeller.isChecked = false
                binding.cbReviewsAsBuyer.isChecked = true
                binding.reviewType2.text = context.getString(R.string.reviews_as_a_buyer)
            }
        }
        when (getReviewType) {
            allReview -> {
                binding.cbReviewsHappy.isChecked = false
                binding.cbReviewsSatisfied.isChecked = false
                binding.cbReviewsNotSatisfied.isChecked = false
            }

            happyReview -> {
                binding.cbReviewsHappy.isChecked = true
                binding.cbReviewsSatisfied.isChecked = false
                binding.cbReviewsNotSatisfied.isChecked = false
            }

            satisfiedReview -> {
                binding.cbReviewsHappy.isChecked = false
                binding.cbReviewsSatisfied.isChecked = true
                binding.cbReviewsNotSatisfied.isChecked = false
            }

            unsatisfiedReview -> {
                binding.cbReviewsHappy.isChecked = false
                binding.cbReviewsSatisfied.isChecked = false
                binding.cbReviewsNotSatisfied.isChecked = true
            }
        }
    }

    interface ApplySellerReviewFilter {
        fun onApplyFilter(reviewType: Int, rateAsSellerOrBuyer: Int)
        fun onResetFilter()
    }
}