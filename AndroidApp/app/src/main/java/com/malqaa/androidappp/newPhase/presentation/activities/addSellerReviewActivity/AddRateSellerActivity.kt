package com.malqaa.androidappp.newPhase.presentation.activities.addSellerReviewActivity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityAddRateSellerBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions

class AddRateSellerActivity : BaseActivity<ActivityAddRateSellerBinding>() {

    var providerId: String = ""
    var businessAccountId: String = ""
    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityAddRateSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.add_Review)
        if (intent.getStringExtra(ConstantObjects.providerIdKey) != null) {
            providerId = intent.getStringExtra(ConstantObjects.providerIdKey)!!
        }
        if (intent.getStringExtra(ConstantObjects.businessAccountIdKey) != null) {
            businessAccountId = intent.getStringExtra(ConstantObjects.businessAccountIdKey)!!
        }
        println("hhhh $providerId $businessAccountId")
        setClickListeners()
        setProductDetailsViewModel()
    }

    private fun setProductDetailsViewModel() {
        productDetialsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetialsViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
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
            productDetialsViewModel.addSellerRate(
                providerId,
                businessAccountId,
                binding.ratingBar.rating,
                binding.etCommnet.text.trim().toString()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetialsViewModel.closeAllCall()
    }
}