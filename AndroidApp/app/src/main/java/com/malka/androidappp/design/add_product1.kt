package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.activity_add_product1.*
import kotlinx.android.synthetic.main.add_product.*


class add_product1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product1)



        choose_Department.setOnClickListener(){

            val  intent = Intent(this@add_product1, add_product2::class.java)
            startActivity(intent)
        }

        add_prouct_button2.setOnClickListener(){

            val  intent = Intent(this@add_product1, add_product4::class.java)
            startActivity(intent)
        }
    }
}