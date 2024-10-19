package com.malqaa.androidappp.newPhase.utils.activitiesMain

import android.os.Bundle
import com.malqaa.androidappp.databinding.ActivityFullImageBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.Extension

class FullImageActivity : BaseActivity<ActivityFullImageBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityFullImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fbButtonBack.setOnClickListener {
            finish()
        }

        val filepath = intent.getStringExtra("imageUri")

        Extension.loadImgGlide(
            this@FullImageActivity,
            filepath,
            binding.imageviewFullimg, null
        )
    }
}