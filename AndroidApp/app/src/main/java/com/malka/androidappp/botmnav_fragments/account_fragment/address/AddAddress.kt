package com.malka.androidappp.botmnav_fragments.account_fragment.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.servicemodels.GetAddressResponse
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.Country
import com.malka.androidappp.servicemodels.CountryRespone
import com.malka.androidappp.servicemodels.GeneralRespone
import kotlinx.android.synthetic.main.add_address_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback

class AddAddress : BaseActivity() {

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null
    var isEdit: Boolean = false
    lateinit var oldAddress: GetAddressResponse.AddressModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_address_fragment)
        isEdit = intent.getBooleanExtra("isEdit", false)

        if (isEdit) {

            val addressObject = intent.getStringExtra("addressObject")
            oldAddress = Gson().fromJson(addressObject, GetAddressResponse.AddressModel::class.java)


            oldAddress.run {
                select_country.text = country
                select_region.text = region
                select_city.text = city
                area_address.text = address
                PhoneNumber_tv.text = mobileNo
                firstname_tv.text = firstName
                lastname_tv.text = lastName
            }

            add_button.text = getString(R.string.Confirm)
            toolbar_title.text = getString(R.string.update_address)
            ConstantObjects.countryList.filter {
                it.name==select_country.text.toString()
            }.let {
                if(it.isNotEmpty()){
                    it.get(0).run {
                        selectedCountry = SearchListItem(key, name)
                        getRegion(selectedCountry!!.key, culture()) {
                            it.filter {
                                it.name==select_region.text.toString()
                            }.let {
                                if (it.isNotEmpty()) {
                                    it.get(0).run {
                                        selectedRegion = SearchListItem(key, name)
                                    }
                                }
                            }

                        }
                    }
                }
            }


        } else {

            toolbar_title.text = getString(R.string.add_a_new_address)
            ConstantObjects.userobj?.let {
                setPreValue()
            } ?: kotlin.run {
                CommonAPI().GetUserInfo(this, ConstantObjects.logged_userid) {
                    setPreValue()
                }
            }

        }

        add_button.setOnClickListener {


            addressDetailValidation()

        }




        initView()
        setListenser()

    }

    private fun setPreValue() {
        ConstantObjects.userobj?.run {
            firstname_tv.text = fullName
            lastname_tv.text = lastname
            PhoneNumber_tv.text = phone
        }
    }

    private fun initView() {


    }

    fun addressDetailValidation() {


        if (SelectCity() && validateArea() && validateStreetNumber()
        ) {

            insertAddress()
        }


    }


    private fun setListenser() {


        back_btn.setOnClickListener {
            finish()
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
                select_region.text = ""
                selectedRegion = null

                select_city.text = ""
                selectedCity = null


                ConstantObjects.countryList.filter {
                    it.key == selectedCountry!!.key
                }.let {
                    if (it.size > 0) {
                        select_country._setStartIconImage(it.get(0).flagimglink)
                    }
                }
            }

        }
        select_region._setOnClickListener {
            if (select_country.text.toString().isEmpty()) {
                (this as BaseActivity).showError(
                    getString(
                        R.string.Please_select,
                        getString(R.string.Country)
                    )
                )
            } else {
                getRegion(selectedCountry!!.key, culture()) {
                    val list: ArrayList<SearchListItem> = ArrayList()
                    it.forEachIndexed { index, country ->
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
        select_city._setOnClickListener {

            if (select_region.text.toString().isEmpty()) {
                (this as BaseActivity).showError(
                    getString(
                        R.string.Please_select,
                        getString(R.string.Region)
                    )
                )

            } else {
                getCity(selectedRegion!!.key)
            }
        }

    }


    fun getRegion(key: String, culture: String,onSuccess: (data: List<Country>) -> Unit) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getRegion(key, culture)
        call.enqueue(object : Callback<CountryRespone?> {
            override fun onFailure(call: Call<CountryRespone?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<CountryRespone?>,
                response: retrofit2.Response<CountryRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: CountryRespone = response.body()!!
                        if (respone.status_code == 200) {
                            onSuccess.invoke(respone.data)
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()

            }
        })


    }


    fun getCity(key: String) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getCity(key,culture())
        call.enqueue(object : Callback<CountryRespone?> {
            override fun onFailure(call: Call<CountryRespone?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<CountryRespone?>,
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
            (this as BaseActivity).showError(
                getString(
                    R.string.Please_select,
                    getString(R.string.City)
                )
            )
            false
        } else {
            true
        }
    }

    private fun validateArea(): Boolean {
        val Inputname = area_address!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (this as BaseActivity).showError(
                getString(
                    R.string.Please_enter,
                    getString(R.string.Area)
                )
            )
            false
        } else {
            true
        }
    }

    private fun validateStreetNumber(): Boolean {
        val Inputname = streetnumber!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (this as BaseActivity).showError(
                getString(
                    R.string.Please_enter,
                    getString(R.string.StreetNumber)
                )
            )
            false
        } else {
            true
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
            id = "",
            createdBy = "",
            createdOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
            updatedBy = "",
            updatedOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
            isActive = true,

            )

        if (isEdit) {
            addAddress.id = oldAddress.id

            val call: Call<GeneralRespone> = malqa.updateAddress(addAddress)
            call.enqueue(object : Callback<GeneralRespone?> {
                override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                }

                override fun onResponse(
                    call: Call<GeneralRespone?>,
                    response: retrofit2.Response<GeneralRespone?>
                ) {
                    if (response.isSuccessful) {

                        if (response.body() != null) {

                            val respone: GeneralRespone = response.body()!!
                            if (respone.status_code==200) {
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
        } else {
            val call: Call<GeneralRespone> = malqa.insertAddress(addAddress)

            call.enqueue(object : Callback<GeneralRespone?> {
                override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                }

                override fun onResponse(
                    call: Call<GeneralRespone?>,
                    response: retrofit2.Response<GeneralRespone?>
                ) {
                    if (response.isSuccessful) {

                        if (response.body() != null) {

                            val respone: GeneralRespone = response.body()!!
                            if (respone.status_code==200) {
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

}