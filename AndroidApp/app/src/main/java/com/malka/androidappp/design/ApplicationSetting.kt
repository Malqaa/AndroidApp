package com.malka.androidappp.design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.toolbar_main.*

class ApplicationSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_setting)


        toolbar_title.text = getString(R.string.application_settings)
        back_btn.setOnClickListener {
            finish()
        }
    }
}