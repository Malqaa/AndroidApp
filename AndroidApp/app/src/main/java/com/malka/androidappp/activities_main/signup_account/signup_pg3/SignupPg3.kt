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
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_signup_pg4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignupPg3 : AppCompatActivity() {
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

        select_region._setOnClickListener {
            val items = List(3) { i ->
                SearchListItem(1, "Dubai")
                SearchListItem(2, "Abu Dhabi")
                SearchListItem(3, "Sharjah")
            }
            select_region.showSpinner(this,items, "Select Region") {
                select_region.text = it.title
            }

        }
        select_city._setOnClickListener {
            val items = List(3) { i ->
                SearchListItem(1, "Al Bahah")
                SearchListItem(2, "Al Aqiq")
            }
            select_region.showSpinner(this,items, "Select District") {
                select_city.text = it.title
            }

        }
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
            streetNUmber!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            streetNUmber!!.error = null
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


    fun SignuuPg4confirmInput(v: View) {

        if (!!validateSign4Streetnum() or !validateSign4Area() or
            !validateSign4PoBox() or !validateRegion() or !validateCity() or !validateSign3FullName() or !validateSign3Day() or !validateRadio()
        ) {
            return
        } else {

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
        val country = editText10.selectedCountryName
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
