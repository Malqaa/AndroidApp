package com.malka.androidappp.activities_main.signup_account.signup_pg3

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CountryRespone
import kotlinx.android.synthetic.main.activity_signup_pg4.*
import kotlinx.android.synthetic.main.activity_signup_pg4.select_city
import kotlinx.android.synthetic.main.activity_signup_pg4.select_country
import kotlinx.android.synthetic.main.activity_signup_pg4.select_region
import kotlinx.android.synthetic.main.fragment_list_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignupPg3 : BaseActivity() {

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null
    var gender4 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg4)
        supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)



        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        date!!._setOnClickListener() {
            val dpd = DatePickerDialog(
                this,
                { view, mYear, mMonth, mDay ->
                    val monthplus: Int = mMonth + 1
                    date!!.setText("" + mYear + "-" + monthplus + "-" + mDay)
                },
                year,
                month,
                day
            )
            dpd.show()
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
            gender4=radiomale.getText()
        }
        radiofemale._setOnClickListener {
            radiofemale._setCheck(!radiofemale.getCheck())
            radiomale._setCheck(false)
            gender4=radiomale.getText()

        }

        confirm_button.setOnClickListener {
            SignupApi()
        }
    }


    fun getRegion(key: String, culture: String) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
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


    fun getCity(key: String, ) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
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

    //Data Validation
    //Data Validation
    //Data Validation
    private fun validateRegion(): Boolean {

        val errorregion_s4text = findViewById<TextView>(R.id.errorregion_s4)
        return if (select_region.text!!.isEmpty()) {
            errorregion_s4text.visibility = View.VISIBLE
            false
        } else {
            errorregion_s4text.visibility = View.GONE
            true
        }
    }

    private fun validateCity(): Boolean {

        val errorregion_s4text = findViewById<TextView>(R.id.errorcity_s4)
        return if (select_city.isEmpty()) {
            errorregion_s4text.visibility = View.VISIBLE
            false
        } else {
            errorregion_s4text.visibility = View.GONE
            true
        }
    }


    //Data Validation
    private fun validateSign4Streetnum(): Boolean {
        val InputStreet = streetNUmber!!.text.toString().trim { it <= ' ' }

        return if (InputStreet.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.StreetNumber)))
            false
        } else {
            true
        }
    }

    //Data Validation
    private fun validateSign4Area(): Boolean {
        val InputArea = Area!!.text.toString().trim { it <= ' ' }

        return if (InputArea.isEmpty()) {
            Area!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            Area!!.error = null
            true
        }
    }

    //Data Validation
    private fun validateSign4PoBox(): Boolean {
        val InputPoBox = county_code!!.text.toString().trim { it <= ' ' }

        return if (InputPoBox.isEmpty()) {
            county_code!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            county_code!!.error = null
            true
        }
    }


    fun SignupApi() {

        if (validateSign4Streetnum() && validateSign4Area() &&
            validateSign4PoBox() && validateCity() && validateSign3FullName() && validateSign3Day() && validateRadio()
        ) {

            updateapicall()
        }

    }

    //fun signup4prev(view: View) {
    //  val intent = Intent(this@SignupPg4, SignupPg3::class.java)
    //startActivity(intent)
    //finish()
    //}

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun updateapicall() {


        val malqaa: MalqaApiService = RetrofitBuilder.updaateuserSignup()
        //  val userId4 = intent.getStringExtra("useridupdate")
        val userId4 = "userId4"
        val fullnaam4 = fullNamee.text.toString().trim()
        val lastName = editTextlastname.text.toString().trim()
        val date4 = date.text.toString().trim()
        val country = select_country.text.toString()
        //

        //
        val areaa = Area.text.toString().trim()

        val call: Call<UpdateuserSignup> = malqaa.updateUserSiginup(
            UpdateuserSignup(
                id = userId4,
                gender = gender4,
                fullName = fullnaam4,
                dateOfBirth = date4,
                country = country,
                region = select_region.text.toString(),
                city = select_city.text.toString(),
                distric = "",
                area = areaa,
                zipcode = county_code.text.toString(),
                lastname = lastName
            )
        )

        call.enqueue(object : Callback<UpdateuserSignup> {

            override fun onResponse(
                call: Call<UpdateuserSignup>, response: Response<UpdateuserSignup>
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

            override fun onFailure(call: Call<UpdateuserSignup>, t: Throwable) {
                Toast.makeText(this@SignupPg3, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun signInAfterSignUp() {
        val intentsignin = Intent(this, SignInActivity::class.java)
        intentsignin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentsignin)
    }


    // SignUp 3 Starts from here

    //Data Validation
    private fun validateSign3FullName(): Boolean {
        val Inputname = fullNamee!!.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            fullNamee!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            fullNamee!!.error = null
            true
        }
    }

    //Data Validation
    private fun validateSign3Day(): Boolean {
        val Inputday = date!!.text.toString().trim { it <= ' ' }

        return if (Inputday.isEmpty()) {
            date!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            date!!.error = null
            true
        }
    }

    private fun validateRadio(): Boolean {
//        val radioButtonMale: RadioButton = findViewById(R.id.radiomale)
//        val radioButtonFemale: RadioButton = findViewById(R.id.radiofemale)
//        return if (radioButtonMale.isChecked or radioButtonFemale.isChecked) {
//            Toast.makeText(applicationContext, "Please select a gender", Toast.LENGTH_SHORT).show()
//            false
//        } else {
//            true
//        }

        return if (gender4.isEmpty()) {
            HelpFunctions.ShowLongToast(getString(R.string.Pleaseselectagender), applicationContext)
            return false
        } else {
            true
        }
    }

    fun hidekeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

}
