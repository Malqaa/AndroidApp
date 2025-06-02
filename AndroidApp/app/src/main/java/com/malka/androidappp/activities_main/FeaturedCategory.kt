package com.malka.androidappp.activities_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.featuredcategory.FeaturedAdapter
import com.malka.androidappp.featuredcategory.FeaturedModel
import com.malka.androidappp.featuredcategory.FeaturedXLAdapter
import kotlinx.android.synthetic.main.activity_featured_category.*

class FeaturedCategory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_featured_category)


        val toolbarr: Toolbar = findViewById(R.id.toolbar111)
        setSupportActionBar(toolbarr)
        toolbarr.setNavigationOnClickListener(){
            super.onBackPressed()
            finish()}


        val sellposts: ArrayList<FeaturedModel> = ArrayList()
        //for (i in 1..100){posts.add(Model("Kashan $i",1))}
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        sellposts.add(FeaturedModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now",
            R.drawable.car4
        ))




        recyclerView22.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView22.adapter = FeaturedXLAdapter(sellposts, this)


        var count=0
        button261.setOnClickListener(){
            if(count==0)
            {
                recyclerView22.adapter = FeaturedAdapter(sellposts, this)
                count++
            }

            else
            {
                recyclerView22.adapter = FeaturedXLAdapter(sellposts, this)
                count--
            }
        }

    }
}
