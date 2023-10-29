package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.negotiationOfferPurchase.NegotiationOffersPurchaseActivity
import kotlinx.android.synthetic.main.activity_success_add_product_price_negotiation.*

class SuccessAddProductPriceNegotiationActivity : BaseActivity() {

    var price: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_add_product_price_negotiation)

        price = intent?.getStringExtra("price")
        tvNegotiationPrice.text=price?:""
        //template = intent?.getStringExtra("Template").toString()

        btnFollowNegotiationsOffers.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NegotiationOffersPurchaseActivity::class.java
                ).apply {
                    putExtra("ComeFrom","SuccessAddNegotiation")
                })
            finish()

        }
        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()
        }

    }


}