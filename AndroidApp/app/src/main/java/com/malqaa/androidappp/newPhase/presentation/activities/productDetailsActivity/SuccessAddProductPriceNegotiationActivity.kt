package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Intent
import android.os.Bundle
import com.malqaa.androidappp.databinding.ActivitySuccessAddProductPriceNegotiationBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.negotiationOfferPurchase.NegotiationOffersPurchaseActivity

class SuccessAddProductPriceNegotiationActivity :
    BaseActivity<ActivitySuccessAddProductPriceNegotiationBinding>() {

    var price: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySuccessAddProductPriceNegotiationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        price = intent?.getStringExtra("price")
        binding.tvNegotiationPrice.text = price ?: ""

        binding.btnFollowNegotiationsOffers.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    NegotiationOffersPurchaseActivity::class.java
                ).apply {
                    putExtra("ComeFrom", "SuccessAddNegotiation")
                })
            finish()

        }

        binding.backToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()
        }
    }
}
