package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.add_item_details.*
import kotlinx.android.synthetic.main.add_item_details1.*

class add_item_details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_details)


        add_product_button6.setOnClickListener(){

            val  intent = Intent(this@add_item_details, add_item_details1::class.java)
            startActivity(intent)
        }
    }
}