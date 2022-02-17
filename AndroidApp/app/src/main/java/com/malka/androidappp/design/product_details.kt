package com.malka.androidappp.design

import com.malka.androidappp.activities_main.BaseActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.activity_product_details.*

class product_details : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        hideSystemUI(mainContainer)

        back_button.setOnClickListener {
            finish()
        }
    }
}