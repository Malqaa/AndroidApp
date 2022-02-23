package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.add_product.*

class add_product : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)

       choose_Department.setOnClickListener(){

         val  intent = Intent(this@add_product, add_product2::class.java)
           startActivity(intent)
       }

        product_search_bar.setOnClickListener(){
            val intent = Intent (this@add_product, add_product1::class.java)
            startActivity(intent)
        }

    }
}