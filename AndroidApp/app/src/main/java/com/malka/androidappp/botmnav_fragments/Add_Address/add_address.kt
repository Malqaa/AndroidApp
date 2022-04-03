package com.malka.androidappp.botmnav_fragments.Add_Address

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.activities_main.login.LoginData
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.network.service.insertAddressResponseBack
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CountryRespone
import io.paperdb.Paper
import kotlinx.android.synthetic.main.add_address_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*

class add_address : Fragment(R.layout.add_address_fragment)  {

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()

    }

    private fun initView() {

        toolbar_title.text = getString(R.string.save_addresses)
    }


    private fun setListenser() {


        add_button.setOnClickListener {

            SignupApi()
        }

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }


        select_country._setOnClickListener {
            val list: ArrayList<SearchListItem> = ArrayList()
            ConstantObjects.countryList.forEachIndexed { index, country ->
                list.add(SearchListItem(country.key, country.name))
            }
            select_country.showSpinner(requireActivity(),
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
                (requireActivity() as MainActivity).showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                getRegion(selectedCountry!!.key,(requireActivity() as MainActivity). culture())
            }

        }
        select_city._setOnClickListener {

            if (select_region.text.toString().isEmpty()) {
                (requireActivity() as MainActivity).showError(getString(R.string.Please_select, getString(R.string.Region)))

            } else {
                getCity(selectedRegion!!.key)
            }
        }

    }


    fun getRegion(key: String, culture: String) {
        HelpFunctions.startProgressBar(requireActivity())


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
                                requireActivity(),
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
        HelpFunctions.startProgressBar(requireActivity())


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getCity(key, (requireActivity() as MainActivity). culture())
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
                                requireActivity(),
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
            (requireActivity() as MainActivity).showError(getString(R.string.Please_select, getString(R.string.City)))
            false
        } else {
            select_city!!.error = null
            true
        }
    }

    private fun validateArea(): Boolean {
        val Inputname = area_address!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (requireActivity() as MainActivity).showError(getString(R.string.Please_enter, getString(R.string.Area)))
            false
        } else {
            area_address!!.error = null
            true
        }
    }

    private fun validateStreetNumber(): Boolean {
        val Inputname = streetnumber!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            (requireActivity() as MainActivity).showError(getString(R.string.Please_enter, getString(R.string.StreetNumber)))
            false
        } else {
            streetnumber!!.error = null
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

        HelpFunctions.startProgressBar(requireActivity())

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

        val ftname = firstname.text.toString()
        val ltname = lastname.text.toString()
        val phonenumber = PhoneNumber.text.toString()
        val address = area_address.text.toString()
        val streetno = streetnumber.text.toString()
        val selectCountry = select_country.text.toString()
        val selectRegion = select_region.text.toString()
        val selectCity = select_city.text.toString()
        val `data` = Paper.book().read<LoginData>(SharedPreferencesStaticClass.userData)!!


        val addAddress = GetAddressResponse.AddressModel(
            firstName = ftname,
            lastName = ltname,
            mobileNo = phonenumber,
            address = address,
            userId = ConstantObjects.logged_userid,
            country = selectCountry,
            region = selectRegion,
            city = selectCity,
            createdBy = "",
            createdOn = "",
            isActive = true,
            id = "0",
            updatedBy = "",
            updatedOn = "",


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


                            requireActivity().onBackPressed()

                        } else {

                            HelpFunctions.ShowLongToast(
                                getString(R.string.ErrorOccur),
                                requireContext()
                            )

                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }




}