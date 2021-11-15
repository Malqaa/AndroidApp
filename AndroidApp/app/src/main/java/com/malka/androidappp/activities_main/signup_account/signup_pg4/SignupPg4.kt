package com.malka.androidappp.activities_main.signup_account.signup_pg4

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.activity_signup_pg3.*
import kotlinx.android.synthetic.main.activity_signup_pg4.*
import kotlinx.android.synthetic.main.activity_signup_pg4.editText10
import kotlinx.android.synthetic.main.activity_signup_pg4.editText1033
import kotlinx.android.synthetic.main.activity_signup_pg4.editText11
import kotlinx.android.synthetic.main.activity_signup_pg4.editText12
import kotlinx.android.synthetic.main.activity_signup_pg4.editTextlastname
import kotlinx.android.synthetic.main.activity_signup_pg4.radioGroup
import kotlinx.android.synthetic.main.activity_signup_pg4.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SignupPg4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg4)
        supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        //Data Validation

        country = findViewById(R.id.editText10)
        districtName = findViewById(R.id.editTextdistrict)
        streetNUmber = findViewById(R.id.editText14)
        Area = findViewById(R.id.editText114)
        poBox = findViewById(R.id.editText145)


        //Data Validation
        fullNamee = findViewById(R.id.editTextFirstName)
        date = findViewById(R.id.editText1033)

        /////////////////For Region Dropdown/Spinner/////////////////////
        val spinner2: Spinner = findViewById(R.id.spinner21)
        val adapter2 = ArrayAdapter.createFromResource(
            this, R.array.regionlist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner2.adapter = adapter2


        /////////////////For City Dropdown/Spinner/////////////////////
        val spinner3: Spinner = findViewById(R.id.spinner22)
        val adapter3 = ArrayAdapter.createFromResource(
            this, R.array.citylist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner3.adapter = adapter3

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        ///////////////////////////////Hightlight Expiry Date////////////////////////////////////////////////////////
        date!!.setOnClickListener() {
            hidekeyboard()
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    val monthplus: Int = mMonth + 1
                    date!!.setText("" + mYear + "-" + monthplus + "-" + mDay)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

    }

    private var country: CountryCodePicker? = null
    private var districtName: EditText? = null
    private var streetNUmber: EditText? = null
    private var Area: EditText? = null
    private var poBox: EditText? = null

    //Data Validation
    private var fullNamee: EditText? = null
    private var date: EditText? = null


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupPg4, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    //Data Validation
    //Data Validation
    //Data Validation
    private fun validateRegion(): Boolean {

        val regions4Spinner: Spinner = findViewById(R.id.spinner21)
        val errorregion_s4text = findViewById<TextView>(R.id.errorregion_s4)
        return if (regions4Spinner.getSelectedItem().toString().trim() == "- - Select Region - -") {
            errorregion_s4text.visibility = View.VISIBLE
            false
        } else {
            errorregion_s4text.visibility = View.GONE
            true
        }
    }

    private fun validateCity(): Boolean {

        val citys4Spinner: Spinner = findViewById(R.id.spinner22)
        val errorregion_s4text = findViewById<TextView>(R.id.errorcity_s4)
        return if (citys4Spinner.getSelectedItem().toString().trim() == "- - Select City - -") {
            errorregion_s4text.visibility = View.VISIBLE
            false
        } else {
            errorregion_s4text.visibility = View.GONE
            true
        }
    }

    //Data Validation
    private fun validateSign4Districtname(): Boolean {
        val InputDistrict = districtName!!.text.toString().trim { it <= ' ' }

        return if (InputDistrict.isEmpty()) {
            districtName!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            districtName!!.error = null
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
        val InputPoBox = poBox!!.text.toString().trim { it <= ' ' }

        return if (InputPoBox.isEmpty()) {
            poBox!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            poBox!!.error = null
            true
        }
    }


    fun SignuuPg4confirmInput(v: View) {

        if (!validateSign4Districtname() or !validateSign4Streetnum() or !validateSign4Area() or
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

        val selectedId: Int = radioGroup.checkedRadioButtonId
        val radioSexButton = findViewById<View>(selectedId) as RadioButton
        val str = radioSexButton.text.toString()

        val malqaa: MalqaApiService = RetrofitBuilder.updaateuserSignup()
        val userId4 = intent.getStringExtra("useridupdate")
        val gender4 = str
        val fullnaam4 = editTextFirstName.text.toString().trim()
        val lastName = editTextlastname.text.toString().trim()
        val date4 = editText1033.text.toString().trim()
        val country = editText10.selectedCountryName
        //
        val regionn: Spinner = findViewById(R.id.spinner21)
        val region = regionn.selectedItem.toString().trim()
        val cityy: Spinner = findViewById(R.id.spinner22)
        val city = cityy.selectedItem.toString().trim()
        //
        val distrtcname = editTextdistrict.text.toString().trim()
        val areaa = editText114.text.toString().trim()
        val poBox = editText145.text.toString().trim()

        val call: Call<UpdateuserSignup> = malqaa.updateUserSiginup(
            UpdateuserSignup(
                id = userId4,
                gender = gender4,
                fullName = fullnaam4,
                dateOfBirth = date4,
                country = country,
                region = region,
                city = city,
                distric = distrtcname,
                area = areaa,
                zipcode = poBox,
                lastname = lastName
            )
        )

        call.enqueue(object : Callback<UpdateuserSignup> {

            override fun onResponse(
                call: Call<UpdateuserSignup>, response: Response<UpdateuserSignup>
            ) {
                if (response.isSuccessful) {
                    HelpFunctions.ShowLongToast(getString(R.string.Accounthasbeencreated),this@SignupPg4)
                    signInAfterSignUp()
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.NoResponse),this@SignupPg4)

                }
            }

            override fun onFailure(call: Call<UpdateuserSignup>, t: Throwable) {
                Toast.makeText(this@SignupPg4, t.message, Toast.LENGTH_LONG).show()
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

        return if (radioGroup.checkedRadioButtonId == -1) {
            HelpFunctions.ShowLongToast(getString(R.string.Pleaseselectagender),applicationContext)
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
