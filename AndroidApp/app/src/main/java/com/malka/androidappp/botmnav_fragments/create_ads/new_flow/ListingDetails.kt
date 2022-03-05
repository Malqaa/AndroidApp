package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_list_details.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ListingDetails : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_details)


        toolbar_title.text = getString(R.string.item_details)
        back_btn.setOnClickListener {
            finish()
        }

        tv_New.setOnClickListener {
            tv_New.isEnabled = true
            tv_used.isEnabled = false
            StaticClassAdCreate.brand_new_item = "on"

        }

        tv_used.setOnClickListener {
            tv_used.isEnabled = true
            tv_New.isEnabled = false
            StaticClassAdCreate.brand_new_item = "Off"

        }
        title_tv.setText(StaticClassAdCreate.producttitle)
        btnotherr.setOnClickListener() { ListDetailsconfirmInput() }

    }

    //Data Validation
    private fun validateTitle(): Boolean {
        val InputTitle = title_tv.getText().trim { it <= ' ' }

        return if (InputTitle.isEmpty()) {
            title_tv.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            title_tv.error = null
            true
        }
    }


    fun ListDetailsconfirmInput() {
        if (validateTitle()) {
            StaticClassAdCreate.phone = phone_number_edittext.text.toString()
            startActivity(Intent(this, PricingActivity::class.java).apply {
            })
        }


    }

}