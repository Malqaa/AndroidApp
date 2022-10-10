package com.malka.androidappp.activities_main.product_detail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar_main.*

class ProductReviews : BaseActivity() {

    lateinit var productDetailHelper:ProductDetailHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_reviews1)
        productDetailHelper=ProductDetailHelper(this)

        toolbar_title.text = getString(R.string.reviews)
        back_btn.setOnClickListener {
            finish()
        }
        val productId = intent.getStringExtra("AdvId") ?: ""
        productDetailHelper.getRates(productId) {
            val reviewList = findViewById<RecyclerView>(R.id.category_rcv)
           val  adapter = RateAdapter(this@ProductReviews, it)
            reviewList.adapter = adapter
            reviewList.layoutManager = LinearLayoutManager(this@ProductReviews)
        }

    }
}


