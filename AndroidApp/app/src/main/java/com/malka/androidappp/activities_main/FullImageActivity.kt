package com.malka.androidappp.activities_main

import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.Extension
import kotlinx.android.synthetic.main.activity_full_image.*

class FullImageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_image)
        fbButtonBack.setOnClickListener {
            finish()
        }

        val filepath = intent.getStringExtra("imageUri")

        Extension.loadThumbnail(
            this@FullImageActivity,
            filepath,
            imageviewFullimg, null
        )



    }
}