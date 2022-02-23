package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.bottom_sheet1.*
import kotlinx.android.synthetic.main.bottom_sheet1.bottom_sheet_btn1
import kotlinx.android.synthetic.main.bottom_sheet2.*

class bottom_sheet2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_sheet2)

        bottom_sheet_btn2.setOnClickListener(){

            val  intent = Intent(this@bottom_sheet2, order_done::class.java)
            startActivity(intent)
        }
    }
}