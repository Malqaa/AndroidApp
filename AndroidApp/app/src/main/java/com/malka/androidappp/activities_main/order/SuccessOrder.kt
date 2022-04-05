package com.malka.androidappp.activities_main.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.activities_main.product_detail.ProductDetails
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_success_order.*
import kotlinx.android.synthetic.main.activity_success_order.back_to_main
import kotlinx.android.synthetic.main.fragment_continue.*

class SuccessOrder : BaseActivity() {
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