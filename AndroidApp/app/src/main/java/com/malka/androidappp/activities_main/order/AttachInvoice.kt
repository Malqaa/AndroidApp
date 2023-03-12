package com.malka.androidappp.activities_main.order

import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.helper.hide
import kotlinx.android.synthetic.main.toolbar_main.*

class AttachInvoice : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_confirm)


        toolbar_title.hide()
        back_btn.setOnClickListener {
            finish()

        }

    }
}