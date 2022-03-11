package com.malka.androidappp.design

import android.content.Intent
import com.malka.androidappp.activities_main.BaseActivity
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetails : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        hideSystemUI(mainContainer)

        back_button.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java).apply {
        })
        finish()
    }
}