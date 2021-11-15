package com.malka.androidappp.activities_main.signup_account.signup_pg1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.BusinessSignupPg1
import com.malka.androidappp.activities_main.signup_account.signup_pg2.SignupPg2
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader
import java.io.Reader


class SignupPg1 : SignInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg1)
        supportActionBar?.hide()

        textEmaill = findViewById(R.id.editText1011)
        textPass = findViewById(R.id.editText12)
        textMobile = findViewById(R.id.editText11)
        confirmPass = findViewById(R.id.editText13)
        userNamee = findViewById(R.id.editText14)

        textbusiness2.setOnClickListener() {
            val intenttoBsignup = Intent(this@SignupPg1, BusinessSignupPg1::class.java)
            startActivity(intenttoBsignup)
        }
    }


    //Data Validation//
    private var textEmaill: EditText? = null
    private var textPass: EditText? = null
    private var textMobile: EditText? = null
    private var confirmPass: EditText? = null
    private var userNamee: EditText? = null

    //Email Validation
    private fun validateSignupEmail(): Boolean {
        val emailInput =
            textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            textEmaill!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textEmaill!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            textEmaill!!.error = null
            true
        }
    }

    //PassswordValidation
    private fun validateSignupPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            textinput_editText12!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textinput_editText12!!.error = getString(R.string.Passwordtooweak)
            false
        } else {
            textinput_editText12!!.error = null
            true
        }
    }


    //phone no validation///
    private fun validateNumber(): Boolean {
        val numberInput =
            textMobile!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            textMobile!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            textMobile!!.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else if (numberInput.length < 9) {
            textMobile!!.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else {
            textMobile!!.error = null
            true
        }
    }

    //confirmpass validation
    private fun validateSignupConfrmPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = confirmPass!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            textinput_editText13!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            textinput_editText13!!.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            textinput_editText13!!.error = null
            true
        }
    }

    //User Validation
    private fun validateSignupUser(): Boolean {
        val Input =
            userNamee!!.text.toString().trim { it <= ' ' }
        return if (Input.isEmpty()) {
            userNamee!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (Input.length < 4) {
            userNamee!!.error = getString(R.string.Usernamemusthaveatleast4characters)
            false
        } else {
            userNamee!!.error = null
            true
        }


    }

    fun SignuuPg1confirmInput(v: View) {
        if (!validateSignupEmail() or !validateSignupPassword() or !validateNumber() or !validateSignupConfrmPassword() or !validateSignupUser()) {
            return
        } else {
            apicallcreateuser()

        }

    }

    fun signuppg1prev(view: View) {
        onBackPressed()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    fun apicallcreateuser() {
        val malqaa: MalqaApiService = RetrofitBuilder.createUserInstance()
        val emailId = editText1011.text.toString().trim()
        val mobilenum = editText11.text.toString().trim()
        val countryCode = cppfield.selectedCountryCode
        val fullmobilenum = "+" + countryCode + mobilenum
        val passcode = editText12.text.toString().trim()
        val usernaam = editText14.text.toString().trim()
//        val call: Call<ResponseBody> = malqaa.createuser(
//            CreateUserDataModel(
//                email = emailId,
//                phone = mobilenum,
//                password = passcode,
//                username = usernaam))
        val createUser = CreateUserDataModel(
            email = emailId,
            phone = fullmobilenum,
            password = passcode,
            username = usernaam
        )
        val call: Call<ResponseBody> = malqaa.createuser(createUser)

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val reader: Reader = InputStreamReader(response.body()?.byteStream(), "UTF-8")
                    val data = Gson().fromJson(reader, RegisterData::class.java)

                    if (data.status_code == 200) {
                        NextAcivityparsedata(data)
//                              signuppg1ParseNnext()
                    } else if (data.status_code == 400) {
                        HelpFunctions.ShowLongToast(data.message, this@SignupPg1)
//                        Toast.makeText(this@SignupPg1, data.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    HelpFunctions.ShowLongToast(response.message(), this@SignupPg1)
//                    Toast.makeText(this@SignupPg1, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg1) }
//                Toast.makeText(this@SignupPg1, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }


    //fun tranferdata(data: RegisterData){
    //  val dataemail = editText1011.text.toString().trim()
    //val intentd = Intent(this@SignupPg1, SignupPg2::class.java)
    //intentd.putExtra("dataemaill",dataemail)
    //startActivity(intentd)
    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    //NextScreen item//val emailId = intent.getStringExtra("dataemaill")
    //}

    fun NextAcivityparsedata(data: RegisterData) {
        val datacode = data.code
        val dataUserId = data.data
        val dataemail = editText1011.text.toString().trim()
        val datapassword = editText12.text.toString().trim()

        val datamobnum = editText11.text.toString().trim()
        val countryCode = cppfield.selectedCountryCode
        val fullmobilenum = "+" + countryCode + datamobnum
        val intenttt = Intent(this, SignupPg2::class.java)
        intenttt.putExtra("datacode", datacode)
        intenttt.putExtra("userid", dataUserId)
        intenttt.putExtra("dataemail", dataemail)
        intenttt.putExtra("datapassword", datapassword)
        intenttt.putExtra("datamobnum", fullmobilenum)
        intenttt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intenttt)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


}
