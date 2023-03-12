package com.malka.androidappp.activities_main.add_product

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.activities_main.product_detail.ProductDetails
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_continue.*


class SuccessProduct : BaseActivity() {

    var AdvId: String = ""
    var template: String = ""

    override fun onBackPressed() {
        back_to_main.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_continue)

        AdvId = intent?.getStringExtra("AdvId").toString()
        template = intent?.getStringExtra("Template").toString()

        button6.setOnClickListener() {
            startActivity(Intent(this, ProductDetails::class.java).apply {
                putExtra("AdvId", AdvId)
                putExtra("Template", template)
                putExtra(ConstantObjects.isSuccess, true)
            })
            finish()
        }
        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
            })
            finish()
        }

    }


}