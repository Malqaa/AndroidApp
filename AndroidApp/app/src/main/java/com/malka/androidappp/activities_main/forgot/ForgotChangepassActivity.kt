package com.malka.androidappp.activities_main.forgot

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.signup_account.signup_pg3.User
import com.malka.androidappp.helper.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_forgotpass_reset.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotChangepassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpass_reset)

        button5.setOnClickListener() {
            changespassconfirmInput()
        }
    }

    private fun validatePasscode(): Boolean {
        val passwordInputt = editText1asdasd0!!.text.toString().trim { it <= ' ' }
        return if (passwordInputt.isEmpty()) {
            editText1asdasd0!!.error = "Field can't be empty"
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInputt).matches()) {
            editText1asdasd0!!.error = "Password too weak"
            false
        } else {
            editText1asdasd0!!.error = null
            true
        }
    }

    private fun validateConfrmPassword(): Boolean {
        val newpasswordInput1 = editText1asdasd0!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = sdsdss!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            sdsdss!!.error = "Field can't be empty"
            false
        } else if (confrmpassInput != newpasswordInput1) {
            sdsdss!!.error = "Password do not match"
            false
        } else {
            sdsdss!!.error = null
            true
        }
    }

    fun changespassconfirmInput() {
        if (!validatePasscode() or !validateConfrmPassword()) {
            return
        } else {
            changepassapi()
        }
    }

    fun changepassapi() {
        val getnewpasscode: String = editText1asdasd0.text.toString().trim()
        val getdata = intent.getStringExtra("getidd").toString()
        val getdata2 = intent.getStringExtra("getcodee").toString()
        val postdatachangepass = User(code = getdata2, id=getdata, password=getnewpasscode)
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<User> = malqaa.changepass(postdatachangepass)

        call.enqueue(object : Callback<User> {

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.password_update),
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, getString(R.string.Error), Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}