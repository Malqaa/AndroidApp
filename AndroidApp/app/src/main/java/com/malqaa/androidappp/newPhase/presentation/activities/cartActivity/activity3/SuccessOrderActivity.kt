package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity3

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        if (intent.getStringExtra(ConstantObjects.orderShippingSectionNumberKey) != null
            || !intent.getStringExtra(ConstantObjects.orderShippingSectionNumberKey).equals("null"))
            shipments_tv.text=intent.getStringExtra(ConstantObjects.orderShippingSectionNumberKey)?:"0"
        else
            shipments_tv.visibility=View.GONE

        if(intent.getStringExtra("RequestType")?.lowercase().equals("FixedPrice".lowercase())){
            txt_request.text=getString(R.string.fixed_price)
        }else if(intent.getStringExtra("RequestType")?.lowercase().equals("Negotiation".lowercase())){
            txt_request.text=getString(R.string.Negotiation)
        }else if(intent.getStringExtra("RequestType")?.lowercase().equals("Auction".lowercase())){
            txt_request.text=getString(R.string.auction)
        }


        total_order_tv.text = "${intent.getStringExtra(ConstantObjects.orderPriceKey)} ${getString(R.string.rial)}"
    }
}