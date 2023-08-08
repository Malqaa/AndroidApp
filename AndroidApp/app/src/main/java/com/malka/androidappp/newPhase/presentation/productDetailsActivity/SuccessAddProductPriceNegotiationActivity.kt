package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import kotlinx.android.synthetic.main.activity_success_add_product_price_negotiation.*

class SuccessAddProductPriceNegotiationActivity : BaseActivity() {

    var price: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_add_product_price_negotiation)

        price = intent?.getStringExtra("price")
        tvNegotiationPrice.text=price?:""
        //template = intent?.getStringExtra("Template").toString()

        btnFollowNegotiationsOffers.setOnClickListener() {
            //finish()
        }
        back_to_main.setOnClickListener {
            finish()
        }

    }


}