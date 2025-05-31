package com.malka.androidappp.botmnav_fragments.forgot_changepass_reset_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.ForgotPassword.PostForgotpassModel
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.forgot_password.ForgotPassResponseModel
import com.malka.androidappp.botmnav_fragments.forgotpass_otpactivity.ActivityForgotPassOtpcode
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgotpass_reset.*
import kotlinx.android.synthetic.main.activity_signup_pg1.*
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
        } else if (!SignInActivity.PASSWORD_PATTERN.matcher(passwordInputt).matches()) {
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
        val postdatachangepass = PostChangePassApiModel(getdata2, getdata, getnewpasscode)
        val malqaa: MalqaApiService = RetrofitBuilder.changePass()
        val call: Call<PostChangePassApiModel> = malqaa.changepass(postdatachangepass)

        call.enqueue(object : Callback<PostChangePassApiModel> {

            override fun onFailure(call: Call<PostChangePassApiModel>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<PostChangePassApiModel>,
                response: Response<PostChangePassApiModel>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Password has been successfully changed",
                        Toast.LENGTH_LONG
                    ).show()
                    val intentt = Intent(this@ForgotChangepassActivity, SignInActivity::class.java)
                    startActivity(intentt)
                } else {
                    Toast.makeText(applicationContext, "Failedddd", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}