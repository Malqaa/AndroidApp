package com.malka.androidappp.activities_main.signup_account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.DatePickerFragment
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CountryRespone
import com.malka.androidappp.servicemodels.User
import kotlinx.android.synthetic.main.activity_signup_pg4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPg3 : BaseActivity() {

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null
    var gender_ = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg4)
        date!!._setOnClickListener {

            DatePickerFragment(true, false) { selectdate_ ->
                date.text = "$selectdate_ "

            }.show(supportFragmentManager, "")

        }

        ConstantObjects.countryList.filter {
            it.key == ConstantObjects.defaltCountry
        }.let {
            if (it.size > 0) {
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
                select_region.text = ""
                selectedRegion = null

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

//
        radiomale._setOnClickListener {
            radiomale._setCheck(!radiomale.getCheck())
            radiofemale._setCheck(false)
            gender_ = radiomale.getText()
        }
        radiofemale._setOnClickListener {
            radiofemale._setCheck(!radiofemale.getCheck())
            radiomale._setCheck(false)
            gender_ = radiomale.getText()

        }

        confirm_button.setOnClickListener {
            if (isValid()) {
                updateapicall()
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
                                this@SignupPg3,
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
                                this@SignupPg3,
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


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupPg3, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun isValid(): Boolean {
        if (firstName!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.First_Name)))
            return false
        }

        if (editTextlastname!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Last_Name)))
            return false
        }

        if (date!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.Date_of_Birth)))
            return false
        }


        if (gender_.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.Gender)))
            return false
        }

        if (select_country!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.Country)))
            return false
        }

        if (select_region!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.Region)))
            return false
        }

        if (select_city!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.district)))
            return false
        }


        if (Area!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Area)))
            return false
        }
        if (streetNUmber!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.StreetNumber)))
            return false
        }
        if (county_code!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.ZipCode)))
            return false
        }
        return true
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun updateapicall() {


        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val userId = intent.getStringExtra("useridupdate")
        val firstName = firstName.text.toString().trim()
        val lastName = editTextlastname.text.toString().trim()
        val country = select_country.text.toString()
        val areaa = Area.text.toString().trim()



        val call: Call<User> = malqaa.updateUserSiginup(
            User(
                id = userId,
                gender = gender_,
                firstName = firstName,
                intDoBDay = HelpFunctions.getFormattedDate(
                    date.text.toString(),
                    HelpFunctions.datetimeformat_ddmyyyy,
              "dd"
                ).toInt(),
                intDoBMonth = HelpFunctions.getFormattedDate(
                    date.text.toString(),
                    HelpFunctions.datetimeformat_ddmyyyy,
                    "MM"
                ).toInt(),
                intDoBYear = HelpFunctions.getFormattedDate(
                    date.text.toString(),
                    HelpFunctions.datetimeformat_ddmyyyy,
                    "yyyy"
                ).toInt(),
                country = country,
                region = select_region.text.toString(),
                city = select_city.text.toString(),
                distric = select_city.text.toString(),
                area = areaa,
                zipcode = county_code.text.toString(),
                lastname = lastName,
                fullName = "$firstName $lastName",
                isApproved = 1
            )
        )

        call.enqueue(object : Callback<User> {

            override fun onResponse(
                call: Call<User>, response: Response<User>
            ) {
                if (response.isSuccessful) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.Accounthasbeencreated),
                        this@SignupPg3
                    )
                    signInAfterSignUp()
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.NoResponse), this@SignupPg3)

                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@SignupPg3, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun signInAfterSignUp() {
        val intentsignin = Intent(this, SignInActivity::class.java)
        intentsignin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentsignin)
    }



}
