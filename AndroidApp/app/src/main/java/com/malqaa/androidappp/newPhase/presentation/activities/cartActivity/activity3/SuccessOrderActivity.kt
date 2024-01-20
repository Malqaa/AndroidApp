package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity3

import android.content.Intent
import android.os.Bundle
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
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