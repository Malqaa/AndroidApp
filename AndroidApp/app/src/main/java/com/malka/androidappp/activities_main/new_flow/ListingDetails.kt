package com.malka.androidappp.activities_main.new_flow

import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CountryRespone
import kotlinx.android.synthetic.main.fragment_list_details.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ListingDetails : BaseActivity() {
    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list_details)


        toolbar_title.text = getString(R.string.item_details)
        back_btn.setOnClickListener {
            finish()
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
            it.key==ConstantObjects.defaltCountry
        }.let {
            if(it.size>0){
                phone_number_edittext._setEndText(it.get(0).countryCode)
                select_country._setStartIconImage(it.get(0).flagimglink)
            }
        }
        select_country._setOnClickListener {
            val list: ArrayList<SearchListItem> = ArrayList()
            ConstantObjects.countryList.forEachIndexed { index, country ->
                list.add(SearchListItem(country.key, country.name))
            }
            select_country.showSpinner(
                this,
                list,
                getString(R.string.Select, getString(R.string.Country))
            ) {
                select_country.text = it.title
                selectedCountry = it
                ConstantObjects.countryList.filter {
                    it.key==selectedCountry!!.key
                }.let {
                    if(it.size>0){
                        phone_number_edittext._setEndText(it.get(0).countryCode)
                        select_country._setStartIconImage(it.get(0).flagimglink)
                    }
                }
                select_region.text = ""
                selectedRegion = null
            }

        }
        select_region._setOnClickListener {
            if (select_country.text.toString().isEmpty()) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                getRegion(selectedCountry!!.key, culture())
            }

        }
        select_city._setOnClickListener {

            if (select_region.text.toString().isEmpty()) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                getCity(selectedRegion!!.key)
            }
        }
        title_tv.setText(StaticClassAdCreate.producttitle)
        btnotherr.setOnClickListener { ListDetailsconfirmInput() }

    }

    fun getCity(key: String) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getCity(key, culture())
        call.enqueue(object : retrofit2.Callback<CountryRespone?> {
            override fun onFailure(call: retrofit2.Call<CountryRespone?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: retrofit2.Call<CountryRespone?>,
                response: retrofit2.Response<CountryRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: CountryRespone = response.body()!!
                        if (respone.status_code == 200) {
                            val list: ArrayList<SearchListItem> = ArrayList()
                            respone.data.forEachIndexed { index, country ->
                                list.add(SearchListItem(country.key, country.name))
                            }
                            select_city.showSpinner(
                                this@ListingDetails,
                                list,
                                getString(R.string.Select, getString(R.string.district))
                            ) {
                                select_city.text = it.title
                                selectedCity = it
                            }
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()

            }
        })


    }

    fun getRegion(key: String, culture: String) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getRegion(key, culture)
        call.enqueue(object : retrofit2.Callback<CountryRespone?> {
            override fun onFailure(call: retrofit2.Call<CountryRespone?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: retrofit2.Call<CountryRespone?>,
                response: retrofit2.Response<CountryRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: CountryRespone = response.body()!!
                        if (respone.status_code == 200) {
                            val list: ArrayList<SearchListItem> = ArrayList()
                            respone.data.forEachIndexed { index, country ->
                                list.add(SearchListItem(country.key, country.name))
                            }
                            select_region.showSpinner(
                                this@ListingDetails,
                                list,
                                getString(R.string.Select, getString(R.string.Region))
                            ) {
                                select_region.text = it.title
                                selectedRegion = it
                                select_city.text = ""
                                selectedCity = null

                            }
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()

            }
        })


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
        }else if (StaticClassAdCreate.quantity.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        }else if (StaticClassAdCreate.quantity.toInt()<=0) {
            showError(getString(R.string.Please_select, getString(R.string.QuantityAvailable)))
        } else if (select_city.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.district)))
        } else if (phone_number_edittext.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.PhoneNumber)))
        } else {

            StaticClassAdCreate.producttitle = title_tv.getText()
            StaticClassAdCreate.subtitle = subtitle.getText()
            StaticClassAdCreate.item_description = item_description.getText().toString()
            StaticClassAdCreate.phone = phone_number_edittext._getEndText()+phone_number_edittext.text.toString()
            StaticClassAdCreate.country = selectedCountry!!.title
            StaticClassAdCreate.region = selectedRegion!!.title
            StaticClassAdCreate.city = selectedCity!!.title

            startActivity(Intent(this, PricingActivity::class.java).apply {
            })
        }


    }

}