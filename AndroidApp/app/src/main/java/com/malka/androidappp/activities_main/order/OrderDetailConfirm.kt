package com.malka.androidappp.activities_main.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import kotlinx.android.synthetic.main.toolbar_main.*

class OrderDetailConfirm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_confirm)


        toolbar_title.hide()
        back_btn.setOnClickListener {
            finish()

        }

    }
}