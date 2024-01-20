package com.malqaa.androidappp.newPhase.utils.activitiesMain

import android.os.Bundle
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.Extension
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