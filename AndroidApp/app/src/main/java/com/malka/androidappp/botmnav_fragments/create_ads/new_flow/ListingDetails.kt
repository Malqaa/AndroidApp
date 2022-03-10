package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isEmpty
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
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
            StaticClassAdCreate.brand_new_item = "on"

        }

        tv_used.setOnClickListener {
            tv_New.setSelected(false)
            tv_used.setSelected(true)
            StaticClassAdCreate.brand_new_item = "Off"

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
            }

        }
        select_region._setOnClickListener {
            if (select_country.text.toString().isEmpty()) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                getRegion(selectedCountry!!.key,"en-US")
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
        btnotherr.setOnClickListener() { ListDetailsconfirmInput() }

    }

    fun getCity(key: String) {


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
        val call = malqa.getCity(key)
        call.enqueue(object : retrofit2.Callback<CountryRespone?> {
            override fun onFailure(call: retrofit2.Call<CountryRespone?>?, t: Throwable) {

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
                            }
                        }
                    }

                }
            }
        })


    }

    fun getRegion(key: String, culture: String) {


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
        val call = malqa.getRegion(key, culture)
        call.enqueue(object : retrofit2.Callback<CountryRespone?> {
            override fun onFailure(call: retrofit2.Call<CountryRespone?>?, t: Throwable) {

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

                            }
                        }
                    }

                }
            }
        })


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
            StaticClassAdCreate.item_description = item_description.text.toString()
            StaticClassAdCreate.subtitle = subtitle.text.toString()
            startActivity(Intent(this, PricingActivity::class.java).apply {
            })
        }


    }

}