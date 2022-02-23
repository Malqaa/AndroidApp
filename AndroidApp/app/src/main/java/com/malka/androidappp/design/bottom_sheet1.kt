package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.add_item_checkout.*
import kotlinx.android.synthetic.main.add_item_checkout.order_complete1
import kotlinx.android.synthetic.main.bottom_sheet1.*

class bottom_sheet1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet1)

        bottom_sheet_btn1.setOnClickListener(){

            val  intent = Intent(this@bottom_sheet1, bottom_sheet2::class.java)
            startActivity(intent)
        }
    }

}
