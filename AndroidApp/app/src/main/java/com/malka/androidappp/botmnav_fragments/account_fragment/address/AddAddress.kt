package com.malka.androidappp.botmnav_fragments.account_fragment.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.network.service.insertAddressResponseBack
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CountryRespone
import kotlinx.android.synthetic.main.add_address_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*

class AddAddress : BaseActivity()  {

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_address_fragment)
        ConstantObjects.userobj?.let {
            setPreValue()
        }?:kotlin.run {
            CommonAPI(). GetUserInfo(this,ConstantObjects.logged_userid) {
                setPreValue()
            }
        }



        initView()
        setListenser()

    }

    private fun setPreValue() {
        ConstantObjects.userobj?.run {
            firstname_tv.text=fullName
            lastname_tv.text=lastname
            PhoneNumber_tv.text=phone
        }
    }

    private fun initView() {

        toolbar_title.text = getString(R.string.add_a_new_address)
    }


    private fun setListenser() {


        add_button.setOnClickListener {

            SignupApi()
        }

        back_btn.setOnClickListener {
           finish()
        }


        select_country._setOnClickListener {
            val list: ArrayList<SearchListItem> = ArrayList()
            ConstantObjects.countryList.forEachIndexed { index, country ->
                list.add(SearchListItem(country.key, country.name))
            }
            select_country.showSpinner(this,
                list,
                getString(R.string.Select, getString(R.string.Country))
            ) {
                select_country.text = it.title
                selectedCountry = it
                select_region.text = ""
                selectedRegion = null

                ConstantObjects.countryList.filter {
                    it.key==selectedCountry!!.key
                }.let {
                    if(it.size>0){
                        select_country._setStartIconImage(it.get(0).flagimglink)
                    }
                }
            }

        }
        select_region._setOnClickListener {
            if (select_country.text.toString().isEmpty()) {
                (this as BaseActivity).showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                getRegion(selectedCountry!!.key,(this as BaseActivity). culture())
            }

        }
        select_city._setOnClickListener {

            if (select_region.text.toString().isEmpty()) {
                (this as BaseActivity).showError(getString(R.string.Please_select, getString(R.string.Region)))

            } else {
                getCity(selectedRegion!!.key)
            }
        }

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
                                this@AddAddress,
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


    fun getCity(key: String, ) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getCity(key, (this as BaseActivity). culture())
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
                                this@AddAddress,
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


    private fun SelectCity(): Boolean {
        val Inputname = select_city!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (this as BaseActivity).showError(getString(R.string.Please_select, getString(R.string.City)))
            false
        } else {
            true
        }
    }

    private fun validateArea(): Boolean {
        val Inputname = area_address!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (this as BaseActivity).showError(getString(R.string.Please_enter, getString(R.string.Area)))
            false
        } else {
            true
        }
    }

    private fun validateStreetNumber(): Boolean {
        val Inputname = streetnumber!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (this as BaseActivity).showError(getString(R.string.Please_enter, getString(R.string.StreetNumber)))
            false
        } else {
            true
        }
    }


    fun SignupApi() {

        if (SelectCity() && validateArea() && validateStreetNumber()
        ) {

            insertAddress()
        }

    }



    fun insertAddress() {

        HelpFunctions.startProgressBar(this)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

        val ftname = firstname_tv.text.toString()
        val ltname = lastname_tv.text.toString()
        val phonenumber = PhoneNumber_tv.text.toString()

        val address = "${area_address.text.toString()} - ${streetnumber.text.toString()}"
        val selectCountry = select_country.text.toString()
        val selectRegion = select_region.text.toString()
        val selectCity = select_city.text.toString()


        val addAddress = GetAddressResponse.AddressModel(
            firstName = ftname,
            lastName = ltname,
            mobileNo = phonenumber,
            address = address,
            userId = ConstantObjects.logged_userid,
            country = selectCountry,
            region = selectRegion,
            city = selectCity,
            id = "0",
            createdBy = "",
            createdOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
            updatedBy = "",
            updatedOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
            isActive = true,

        )
        val call: retrofit2.Call<insertAddressResponseBack> = malqa.insertAddress(addAddress)

        call?.enqueue(object : retrofit2.Callback<insertAddressResponseBack?> {
            override fun onFailure(
                call: retrofit2.Call<insertAddressResponseBack?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()


            }

            override fun onResponse(
                call: retrofit2.Call<insertAddressResponseBack?>,
                response: retrofit2.Response<insertAddressResponseBack?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: insertAddressResponseBack = response.body()!!
                        if (respone.status_code.equals("200")) {
                            setResult(Activity.RESULT_OK, Intent())
                            finish()
                        } else {

                            HelpFunctions.ShowLongToast(
                                getString(R.string.ErrorOccur),
                                this@AddAddress
                            )

                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }




}