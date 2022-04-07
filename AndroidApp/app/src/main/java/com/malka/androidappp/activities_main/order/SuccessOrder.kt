package com.malka.androidappp.activities_main.order

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_success_order.*

class SuccessOrder : BaseActivity() {

    override fun onBackPressed() {
        back_to_main.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        track_your_order_btn.setOnClickListener {
//            startActivity(Intent(this, ProductDetails::class.java).apply {
//                putExtra("AdvId", AdvId)
//                putExtra("Template", template)
//                putExtra(ConstantObjects.isSuccess, true)
//            })
//            finish()
        }

        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
            })
            finish()
        }

    }
}