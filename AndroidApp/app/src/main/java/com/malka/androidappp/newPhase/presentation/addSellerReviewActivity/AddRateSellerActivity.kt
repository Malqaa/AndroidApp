package com.malka.androidappp.newPhase.presentation.addSellerReviewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_add_rate_seller.*
import kotlinx.android.synthetic.main.toolbar_main.*

class AddRateSellerActivity : BaseActivity() {

    var providerId: String = ""
    var businessAccountId: String = ""
    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rate_seller)
        toolbar_title.text = getString(R.string.add_Review)
        if (intent.getStringExtra(ConstantObjects.providerIdKey) != null) {
            providerId = intent.getStringExtra(ConstantObjects.providerIdKey)!!
        }
        if (intent.getStringExtra(ConstantObjects.businessAccountIdKey) != null) {
            businessAccountId = intent.getStringExtra(ConstantObjects.businessAccountIdKey)!!
        }
        println("hhhh $providerId $businessAccountId")
        setClickListeners()
        setProductDetailsViewModel()
        //  println("hhhh product id =$productId")
     //   productDetialsViewModel.getCurrentUserRate(productId)
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
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
//        productDetialsViewModel.addRateRespObservable.observe(this) { addRateResp ->
//            if (addRateResp.status_code == 200) {
//                var intent = Intent()
//                addRateResp.rateObject?.let {
//                    intent.putExtra(ConstantObjects.rateObjectKey, it);
//                }
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//            } else {
//                if (addRateResp.message != null) {
//                    HelpFunctions.ShowLongToast(addRateResp.message, this)
//                } else {
//                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
//                }
//            }
//
//        }
//        productDetialsViewModel.editRateRespObservable.observe(this) { addRateResp ->
//            if (addRateResp.status_code == 200) {
//                var intent = Intent()
//                addRateResp.rateObject?.let {
//                    intent.putExtra(ConstantObjects.rateObjectKey, it);
//                    intent.putExtra(ConstantObjects.editRateKey, true);
//                }
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//            } else {
//                if (addRateResp.message != null) {
//                    HelpFunctions.ShowLongToast(addRateResp.message, this)
//                } else {
//                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
//                }
//            }
//
//        }
//        productDetialsViewModel.getCurrentUserRateObservable.observe(this) { currentUserRate ->
//            if (currentUserRate.status_code == 200) {
//                currentUserRate.data?.let { data ->
//                    setPerviosUserRate(data)
//                }
//
//            }
//
//        }

    }

//    private fun setPerviosUserRate(data: RateReviewItem) {
//        editRate = true
//        rateReviewItemEdit = data
//        try {
//            rating_bar.rating = data.rate
//            etCommnet.setText(data.comment)
//        } catch (e: Exception) {
//        }
//    }

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
//            if (editRate && rateReviewItemEdit != null) {
//                productDetialsViewModel.editRateProduct(
//                    rateReviewItemEdit!!.id,
//                    rating_bar.rating,
//                    etCommnet.text.trim().toString()
//                )
//            } else {
                productDetialsViewModel.addSellerRate(
                    providerId,
                    businessAccountId,
                    rating_bar.rating,
                    etCommnet.text.trim().toString()
                )
           // }
        }
    }
}