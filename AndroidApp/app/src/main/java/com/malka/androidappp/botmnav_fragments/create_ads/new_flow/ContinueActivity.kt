package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.design.ProductDetails
import kotlinx.android.synthetic.main.fragment_continue.*


class ContinueActivity : BaseActivity() {

    var AdvId: String = ""
    var template: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_continue)

//        AdvId = intent?.getStringExtra("AdvId").toString()
//        template = intent?.getStringExtra("Template").toString()

        button6.setOnClickListener() {
            startActivity(Intent(this, ProductDetails::class.java).apply {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            })
            finish()
        }
        back_to_main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            })
            finish()
        }

    }


}