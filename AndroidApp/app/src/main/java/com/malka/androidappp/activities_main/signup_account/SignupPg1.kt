package com.malka.androidappp.activities_main.signup_account

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.Extension.getDeviceId
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malka.androidappp.helper.HelpFunctions.Companion.deviceType
import com.malka.androidappp.helper.HelpFunctions.Companion.projectName
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.GeneralRespone
import com.malka.androidappp.servicemodels.User
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupPg1 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg1)


        if (ConstantObjects.currentLanguage == ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }

        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            setLocate()
        }

    }



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
            textPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textPass!!.error = getString(R.string.Passwordtooweak)
            false
        } else {
            textPass!!.error = null
            true
        }
    }


    //phone no validation///
    private fun validateNumber(): Boolean {
        val numberInput =
            PhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            PhoneNumber!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            PhoneNumber!!.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else if (numberInput.length < 9) {
            PhoneNumber!!.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else {
            PhoneNumber!!.error = null
            true
        }
    }

    //confirmpass validation
    private fun validateSignupConfrmPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = confirmPass!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            confirmPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            confirmPass!!.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            confirmPass!!.error = null
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

            if(switch_term_condition._getChecked()){
                apicallcreateuser()
            }else{
                showError(getString(R.string.Please_select,getString(R.string.term_condition)))
            }

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
        HelpFunctions.startProgressBar(this)

        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val emailId = textEmaill.text.toString().trim()
        val mobilenum = PhoneNumber.text.toString().trim()
        val passcode = textPass.text.toString().trim()
        val usernaam = userNamee.text.toString().trim()
        val createUser = User(
            email = emailId,
            password = passcode,
            phone = mobilenum,
            cPassword = passcode,
            userName = usernaam,
            info = "abc",
            lang = language(),
            imgProfile = "abcsd",
            projectName = projectName,
            deviceType = deviceType,
            deviceId = getDeviceId(),
            termsAndConditions = switch_term_condition._getChecked()
        )
        val jsonString = Gson().toJson(createUser)
        val retMap: HashMap<String, Any> = Gson().fromJson(
            jsonString, object : TypeToken<HashMap<String?, Any?>?>() {}.getType()
        )
        val call: Call<GeneralRespone> = malqaa.createuser(retMap)

        call.enqueue(object : Callback<GeneralRespone> {

            override fun onResponse(
                call: Call<GeneralRespone>, response: Response<GeneralRespone>
            ) {
                if (response.isSuccessful) {
                    val data=response.body()
                    if (data!!.status_code == 200) {
                        NextAcivityparsedata(data)
                    } else {
                        showError(data.message)
                    }
                } else {
                    HelpFunctions.ShowLongToast(response.message(), this@SignupPg1)
                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<GeneralRespone>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg1) }
            }
        })

    }




    fun NextAcivityparsedata(data: GeneralRespone) {
        val datacode = data.code
        val dataUserId = data.id
        val dataemail = textEmaill.text.toString().trim()
        val datapassword = textPass.text.toString().trim()

        val datamobnum = PhoneNumber.text.toString().trim()
        val countryCode = PhoneNumber._getEndText()
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
