package com.malka.androidappp.newPhase.presentation.addProduct

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_continue.*


class SuccessProductActivity : BaseActivity() {

    var AdvId: String = ""
    var template: String = ""

    override fun onBackPressed() {
        back_to_main.performClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_continue)

        AdvId = intent?.getStringExtra(ConstantObjects.productIdKey).toString()
        //template = intent?.getStringExtra("Template").toString()

        button6.setOnClickListener() {
            startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, AdvId)
                putExtra(ConstantObjects.isSuccess, true)
            })
            finish()
        }
        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()
        }

    }


}