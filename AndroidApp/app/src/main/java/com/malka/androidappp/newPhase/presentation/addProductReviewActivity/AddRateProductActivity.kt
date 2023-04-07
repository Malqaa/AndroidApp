package com.malka.androidappp.newPhase.presentation.addProductReviewActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_rate_product.*
import kotlinx.android.synthetic.main.activity_rate_product.rating_bar
import kotlinx.android.synthetic.main.toolbar_main.*

class AddRateProductActivity : BaseActivity() {


    var productId: Int = 0
    private lateinit var productDetialsViewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_product)
        toolbar_title.text = getString(R.string.add_Review)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, 0)
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
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        productDetialsViewModel.addRateRespObservable.observe(this) { addRateResp ->
            if (addRateResp.status_code == 200) {
                var intent = Intent()
                addRateResp.rateObject?.let {
                    intent.putExtra(ConstantObjects.rateObjectKey, it);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                if (addRateResp.message != null) {
                    HelpFunctions.ShowLongToast(addRateResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }

    }

    private fun setClickListeners() {
        btnSend.setOnClickListener {
            checkValidations()
        }
        back_btn.setOnClickListener {
            onBackPressed()
        }

    }

    private fun checkValidations() {
        var readyToSave = true
        if (etCommnet.text.trim().toString() == "") {
            etCommnet.error = getString(R.string.enterComment)
            readyToSave = false
        }
        if (rating_bar.rating == 0f) {
            readyToSave = false
            HelpFunctions.ShowLongToast(getString(R.string.add_Review), this)
        }
        if (readyToSave) {
          productDetialsViewModel.addRateProduct(productId,rating_bar.rating,etCommnet.text.trim().toString())
        }
    }

}