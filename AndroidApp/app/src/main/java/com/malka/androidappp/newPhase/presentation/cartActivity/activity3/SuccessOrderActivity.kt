package com.malka.androidappp.newPhase.presentation.cartActivity.activity3

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.activity_success_order.*

class SuccessOrderActivity : BaseActivity() {

    override fun onBackPressed() {
        back_to_main.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        track_your_order_btn.setOnClickListener {
            startActivity(Intent(this@SuccessOrderActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(ConstantObjects.isMyOrder,true)
            })
            finish()
        }

        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
        order_number_tv.text=intent.getStringExtra(ConstantObjects.orderNumberKey)
        shipments_tv.text=intent.getStringExtra(ConstantObjects.orderShippingSectionNumberKey)
        total_order_tv.text = "${intent.getStringExtra(ConstantObjects.orderPriceKey)} ${getString(R.string.rial)}"
    }
}