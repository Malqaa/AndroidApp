package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_continue.*


class ContinueFragment : BaseActivity() {

    var AdvId: String = ""
    var template: String = ""
    var sellerID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_continue)

        AdvId = intent?.getStringExtra("AdvId").toString()
        template = intent?.getStringExtra("Template").toString()

        textView49.text = AdvId + template
        button6.setOnClickListener(){
            val args = Bundle()
            args.putString("AdvId", AdvId)
            args.putString("Template",template)

            SharedPreferencesStaticClass.ad_userid = ConstantObjects.logged_userid


        }

    }




}