package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.add_product4.*
import kotlinx.android.synthetic.main.add_product5.*

class add_product5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product5)

        add_product_button6.setOnClickListener(){

            val  intent = Intent(this@add_product5, add_item_details::class.java)
            startActivity(intent)
        }
    }
}