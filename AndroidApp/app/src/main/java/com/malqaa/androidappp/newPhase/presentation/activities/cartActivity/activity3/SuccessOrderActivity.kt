package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity3

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySuccessOrderBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects

class SuccessOrderActivity : BaseActivity<ActivitySuccessOrderBinding>() {

    override fun onBackPressed() {
        binding.backToMain.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySuccessOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trackYourOrderBtn.setOnClickListener {
            startActivity(Intent(this@SuccessOrderActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(ConstantObjects.isMyOrder, true)
            })
            finish()
        }

        binding.backToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        binding.orderNumberTv.text = intent.getStringExtra(ConstantObjects.orderNumberKey)

        val orderShippingSectionNumber =
            intent.getStringExtra(ConstantObjects.orderShippingSectionNumberKey)
        if (orderShippingSectionNumber.isNullOrEmpty()
                .not() && orderShippingSectionNumber.equals("null").not()
        ) {
            binding.shipmentsTv.text = orderShippingSectionNumber ?: "0"
        } else {
            binding.linearShipments.visibility = View.GONE
        }

        if (intent.getStringExtra("RequestType")?.lowercase().equals("FixedPrice".lowercase())) {
            binding.txtRequest.text = getString(R.string.fixed_price)
        } else if (intent.getStringExtra("RequestType")?.lowercase()
                .equals("Negotiation".lowercase())
        ) {
            binding.txtRequest.text = getString(R.string.Negotiation)
        } else if (intent.getStringExtra("RequestType")?.lowercase()
                .equals("Auction".lowercase())
        ) {
            binding.txtRequest.text = getString(R.string.auction)
        }

        binding.totalOrderTv.text =
            "${intent.getStringExtra(ConstantObjects.orderPriceKey)} ${getString(R.string.rial)}"
    }
}