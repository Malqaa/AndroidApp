package com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityRateProductBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions

class AddRateProductActivity : BaseActivity<ActivityRateProductBinding>() {

    private var productId: Int = 0
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private var rateReviewItemEdit: RateReviewItem? = null
    private var editRate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityRateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.add_Review)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, 0)
        setClickListeners()
        setProductDetailsViewModel()
        productDetailsViewModel.getCurrentUserRate(productId)
    }

    private fun setProductDetailsViewModel() {
        productDetailsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetailsViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        productDetailsViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        productDetailsViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, this)
                } else if (it.message2 != null) {
                    HelpFunctions.ShowLongToast(it.message2!!, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        productDetailsViewModel.addRateRespObservable.observe(this) { addRateResp ->
            if (addRateResp.status_code == 200) {
                val intent = Intent()
                addRateResp.rateObject?.let {
                    intent.putExtra(ConstantObjects.rateObjectKey, it)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                if (addRateResp.message != null) {
                    HelpFunctions.ShowLongToast(addRateResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }
        productDetailsViewModel.editRateRespObservable.observe(this) { addRateResp ->
            if (addRateResp.status_code == 200) {
                val intent = Intent()
                addRateResp.rateObject?.let {
                    intent.putExtra(ConstantObjects.rateObjectKey, it)
                    intent.putExtra(ConstantObjects.editRateKey, true)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                if (addRateResp.message != null) {
                    HelpFunctions.ShowLongToast(addRateResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }
        productDetailsViewModel.getCurrentUserRateObservable.observe(this) { currentUserRate ->
            if (currentUserRate.status_code == 200) {
                currentUserRate.data?.let { data ->
                    setPerviosUserRate(data)
                }

            }

        }

    }

    private fun setPerviosUserRate(data: RateReviewItem) {
        editRate = true
        rateReviewItemEdit = data
        try {
            binding.ratingBar.rating = data.rate
            binding.etCommnet.setText(data.comment)
        } catch (e: Exception) {
        }
    }

    private fun setClickListeners() {
        binding.btnSend.setOnClickListener {
            checkValidations()
        }
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    private fun checkValidations() {
        var readyToSave = true
        if (binding.etCommnet.text.trim().toString() == "") {
            binding.etCommnet.error = getString(R.string.enterComment)
            readyToSave = false
        }
        if (binding.ratingBar.rating == 0f) {
            readyToSave = false
            HelpFunctions.ShowLongToast(getString(R.string.add_Review), this)
        }
        if (readyToSave) {
            if (editRate && rateReviewItemEdit != null) {
                productDetailsViewModel.editRateProduct(
                    rateReviewItemEdit!!.id,
                    binding.ratingBar.rating,
                    binding.etCommnet.text.trim().toString()
                )
            } else {
                productDetailsViewModel.addRateProduct(
                    productId,
                    binding.ratingBar.rating,
                    binding.etCommnet.text.trim().toString()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetailsViewModel.closeAllCall()
    }
}