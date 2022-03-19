package com.malka.androidappp.activities_main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_image.*

class FullImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)
        back_button.setOnClickListener {
            finish()
        }

        val filepath = intent.getStringExtra("imageUri")
        Picasso.get()
            .load(filepath)
            .into(imageviewFullimg)

    }
}