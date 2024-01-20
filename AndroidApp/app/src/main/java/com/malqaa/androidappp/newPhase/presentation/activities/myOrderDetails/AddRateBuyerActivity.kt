package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_rate_buyer.btnSend
import kotlinx.android.synthetic.main.activity_rate_buyer.edtComment
import kotlinx.android.synthetic.main.activity_rate_buyer.ivSellerHappyRate
import kotlinx.android.synthetic.main.activity_rate_buyer.ivSellerNeutralRate
import kotlinx.android.synthetic.main.activity_rate_buyer.ivSellerSadeRate
import kotlinx.android.synthetic.main.toolbar_main.*

class AddRateBuyerActivity : BaseActivity() {
    var clientId: String = ""
    var buyerRate = 0
    var orderId = 0
    var buyerRateId =0
    private lateinit var productDetialsViewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_buyer)
        toolbar_title.text = getString(R.string.add_Review)
        orderId = intent.getIntExtra("orderId", 0)
        clientId = intent.getStringExtra("clientId") ?: ""
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
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        productDetialsViewModel.addBuyerRateObservable.observe(this) { addRateResp ->
            if (addRateResp.status_code == 200) {
                HelpFunctions.ShowLongToast(getString(R.string.add_Review_success), this)
                finish()
            } else {
                if (addRateResp.message != null) {
                    HelpFunctions.ShowLongToast(addRateResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }

        productDetialsViewModel.getBuyerRateObservable.observe(this) {
            edtComment.setText(it.rateObject?.comment)
            buyerRateId= it.rateObject?.buyerRateId?:0
            when (it.rateObject?.rate) {
                3 -> {
                    buyerRate = 3
                    ivSellerHappyRate.setImageResource(R.drawable.happyface_color)
                    ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
                    ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
                }

                2 -> {
                    buyerRate = 2
                    ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
                    ivSellerNeutralRate.setImageResource(R.drawable.smileface_color)
                    ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
                }

                1 -> {
                    buyerRate = 1
                    ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
                    ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
                    ivSellerSadeRate.setImageResource(R.drawable.sadcolor_gray)

                }

            }
        }


    }


    private fun setClickListeners() {
        btnSend.setOnClickListener {
            checkRateData()
        }
        back_btn.setOnClickListener {
            onBackPressed()
        }


        /**seller rate */
        ivSellerHappyRate.setOnClickListener {
            buyerRate = 1
            ivSellerHappyRate.setImageResource(R.drawable.happyface_color)
            ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)

        }
        ivSellerNeutralRate.setOnClickListener {
            buyerRate = 2
            ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
            ivSellerNeutralRate.setImageResource(R.drawable.smileface_color)
            ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivSellerSadeRate.setOnClickListener {
            buyerRate = 3
            ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
            ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivSellerSadeRate.setImageResource(R.drawable.sadcolor_gray)
        }

    }

    private fun checkRateData() {
        if (buyerRate == 0) {
            HelpFunctions.ShowLongToast(getString(R.string.selectSellerRate), this)
        } else if (edtComment.text.toString().trim() == "") {
            edtComment.error = getString(R.string.addComment)
        } else {
            productDetialsViewModel.addRateBuyer(
                orderId,
                buyerRateId,
                clientId,
                buyerRate,
                edtComment.text.trim().toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        productDetialsViewModel.getRateBuyer(orderId)
    }
    override fun onDestroy() {
        super.onDestroy()
        productDetialsViewModel.closeAllCall()
    }
}