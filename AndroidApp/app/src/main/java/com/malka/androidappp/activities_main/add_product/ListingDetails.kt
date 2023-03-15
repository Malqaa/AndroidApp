package com.malka.androidappp.activities_main.add_product

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.newPhase.data.network.CommonAPI
import com.malka.androidappp.newPhase.data.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_list_details.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ListingDetails : BaseActivity() {
    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null
    var isEdit: Boolean = false
    override fun onBackPressed() {
        intent.getBooleanExtra("isEdit",false).let {
            if (it){
                startActivity(Intent(this, Confirmation::class.java).apply {
                    finish()
                })
            }else{
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_details)
        if(!StaticClassAdCreate.brand_new_item.isEmpty()){
            isEdit=true
        }

        toolbar_title.text = getString(R.string.item_details)
        back_btn.setOnClickListener {
            onBackPressed()
        }

        tv_New.setOnClickListener {
            tv_New.setSelected(true)
            tv_used.setSelected(false)
            StaticClassAdCreate.brand_new_item = tv_New.text.toString()
        }

        tv_used.setOnClickListener {
            tv_New.setSelected(false)
            tv_used.setSelected(true)
            StaticClassAdCreate.brand_new_item = tv_used.text.toString()

        }

        ConstantObjects.countryList.filter {
            it.id == ConstantObjects.defaltCountry
        }.let {
            if (it.size > 0) {
                it.get(0).run{
                    selectedCountry = SearchListItem(id,name)
                    phone_number_edittext._setEndText(countryCode)
                    countryContainer._setStartIconImage(flagimglink)
                    countryContainer.text=it.get(0).name
                }

            }
        }
        countryContainer._setOnClickListener {
            val list: ArrayList<SearchListItem> = ArrayList()
            ConstantObjects.countryList.forEachIndexed { index, country ->
                list.add(SearchListItem(country.id, country.name))
            }
            countryContainer.showSpinner(
                this,
                list,
                getString(R.string.Select, getString(R.string.Country))
            ) {
                countryContainer.text = it.title
                selectedCountry = it
                ConstantObjects.countryList.filter {
                    it.id == selectedCountry!!.id
                }.let {
                    if (it.size > 0) {
                        phone_number_edittext._setEndText(it.get(0).countryCode)
                        countryContainer._setStartIconImage(it.get(0).flagimglink)
                    }
                }
                regionContainer.text = ""
                selectedRegion = null

                neighborhoodContainer.text = ""
                selectedCity = null
            }

        }
        regionContainer._setOnClickListener {
            if (countryContainer.text.toString().isEmpty()) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                CommonAPI().getRegion(selectedCountry!!.id, this) {
                    val list: ArrayList<SearchListItem> = ArrayList()
                    it.forEachIndexed { index, country ->
                        list.add(SearchListItem(country.id, country.name))
                    }
                    regionContainer.showSpinner(
                        this,
                        list,
                        getString(R.string.Select, getString(R.string.Region))
                    ) {
                        regionContainer.text = it.title
                        selectedRegion = it


                        neighborhoodContainer.text = ""
                        selectedCity = null

                    }
                }
            }

        }
        neighborhoodContainer._setOnClickListener {

            if (regionContainer.text.toString().isEmpty()) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                CommonAPI().getCity(selectedRegion!!.id,this) {
                    val list: ArrayList<SearchListItem> = ArrayList()
                    it.forEachIndexed { index, country ->
                        list.add(SearchListItem(country.id, country.name))
                    }
                    neighborhoodContainer.showSpinner(
                        this,
                        list,
                        getString(R.string.Select, getString(R.string.district))
                    ) {
                        neighborhoodContainer.text = it.title
                        selectedCity = it
                    }
                }
            }
        }
        title_tv.setText(StaticClassAdCreate.producttitle)
        btnotherr.setOnClickListener { ListDetailsconfirmInput() }

        if (isEdit){
            item_description.setText( StaticClassAdCreate.item_description)
            subtitle.setText(StaticClassAdCreate.subtitle)
            title_tv.setText(StaticClassAdCreate.producttitle)
            quantityavail.number = StaticClassAdCreate.quantity
            countryContainer.setText(StaticClassAdCreate.country!!.title)
            selectedCountry = StaticClassAdCreate.country
            regionContainer.setText(StaticClassAdCreate.region!!.title)
            selectedRegion = StaticClassAdCreate.region
            neighborhoodContainer.setText(StaticClassAdCreate.city!!.title)
            selectedCity = StaticClassAdCreate.city
            if (StaticClassAdCreate.brand_new_item.equals(tv_New.text.toString())){
                tv_New.performClick()
            }else{
                tv_used.performClick()
            }

            ConstantObjects.countryList.forEach {
                if (StaticClassAdCreate.phone.startsWith(it.countryCode!!)){
                    phone_number_edittext.text = StaticClassAdCreate.phone.replace(it.countryCode!!,"")
                    phone_number_edittext._setEndText(it.countryCode)
                }

            }

            btnotherr.setOnClickListener {
                ListDetailsconfirmInput() }

        }

    }


    fun ListDetailsconfirmInput() {
        StaticClassAdCreate.quantity = quantityavail.number.toString()

        if (title_tv.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_title)))
        } else if (subtitle.getText().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.sub_title)))
        } else if (item_description.getText().toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.item_details)))
        } else if (StaticClassAdCreate.brand_new_item.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.item_condition)))
        } else if (StaticClassAdCreate.quantity.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (StaticClassAdCreate.quantity.toInt() <= 0) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (neighborhoodContainer.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.district)))
        } else if (phone_number_edittext.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.PhoneNumber)))
        } else {

            StaticClassAdCreate.producttitle = title_tv.getText()
            StaticClassAdCreate.subtitle = subtitle.getText()
            StaticClassAdCreate.item_description = item_description.getText().toString()
            StaticClassAdCreate.phone = phone_number_edittext._getEndText() + phone_number_edittext.text.toString()
            StaticClassAdCreate.country = selectedCountry
            StaticClassAdCreate.region = selectedRegion
            StaticClassAdCreate.city = selectedCity
            intent.getBooleanExtra("isEdit",false).let {
                if (it){
                    startActivity(Intent(this, Confirmation::class.java).apply {
                        finish()
                    })
                }else{
                    startActivity(Intent(this, PricingActivity::class.java).apply {
                    })

                }
            }



        }


    }
}