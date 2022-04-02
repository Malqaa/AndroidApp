package com.malka.androidappp.design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import kotlinx.android.synthetic.main.activity_business_signup_pg2.*
import kotlinx.android.synthetic.main.activity_technical_support.*
import kotlinx.android.synthetic.main.toolbar_main.*

class TechnicalSupport : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technical_support)


        toolbar_title


        comunication_type.setOnClickListener {

            val list: ArrayList<SearchListItem> = ArrayList()

            list.add(SearchListItem(1, "Type 1"))
            list.add(SearchListItem(2, "Type 2"))
            list.add(SearchListItem(3, "Type 3"))


            comunication_type.showSpinner(
                this,
                list,
                getString(R.string.Select, getString(R.string.type_of_communication))
            ) {
                comunication_type.text = it.title

            }
        }


    }
}