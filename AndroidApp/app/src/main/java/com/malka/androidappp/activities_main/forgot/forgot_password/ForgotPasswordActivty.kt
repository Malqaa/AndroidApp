package com.malka.androidappp.activities_main.forgot.forgot_password

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.ForgotPassword.PostForgotpassModel
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.forgot.forgotpass_otpactivity.ActivityForgotPassOtpcode
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.edittext.TextFieldComponent
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPasswordActivty : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot_password)

        signin.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivty, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }



        /////////////////////////ClickableSpan for Alreadyaccount////////////////////////////////

        // To get the current language and set span accordingly
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")

        val mSpannablerecover = SpannableString(getString(R.string.alreadyaccount))


        //data Validation
        emailtextt = findViewById(R.id.editText4)
    }

    private var emailtextt: TextFieldComponent? = null

    //Data Validation
    private fun validateforgot(): Boolean {
        val Inputemail = emailtextt!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            emailtextt!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Inputemail).matches()) {
            emailtextt!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            emailtextt!!.error = null
            true
        }
    }

    fun forgotpassconfrm(v: View) {
        if (!validateforgot()) {
            return
        } else {
            forgotemail()
        }

    }


    fun forgotemail() {

        val emailfogotpass: String = editText4.text.toString().trim()
        val modeldataitem = PostForgotpassModel(emailfogotpass, "String")
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ForgotPassResponseModel> = malqaa.forgotpassemail(modeldataitem)


        call.enqueue(object : Callback<ForgotPassResponseModel> {
            override fun onFailure(call: Call<ForgotPassResponseModel>, t: Throwable) {
                HelpFunctions.ShowLongToast(getString(R.string.Somethingwentwrong),applicationContext)
//                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ForgotPassResponseModel>,
                response: Response<ForgotPassResponseModel>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.data.length != 47) {
                        emailtextt!!.error = null
                        val responseMsg: String? = response.body()!!.message
                        val responsedata: String? = response.body()!!.data
                        val asubstring1: String = responsedata!!.substring(41, 77)
                        val asubstring2: String = responsedata.substring(83, 87)
                        Toast.makeText(applicationContext, responseMsg, Toast.LENGTH_LONG).show()
                        val intentd = Intent(
                            this@ForgotPasswordActivty,
                            ActivityForgotPassOtpcode::class.java
                        )
                        intentd.putExtra("getid", asubstring1)
                        intentd.putExtra("getcode", asubstring2)
                        startActivity(intentd)
                    } else if (response.body()!!.data.length == 47) {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.ThisEmailAddressDoesnotExist),
                            applicationContext
                        )
                        emailtextt!!.error = getString(R.string.ThisEmailAddressDoesnotExist)
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.InternetIssue),
                        applicationContext
                    )
                }
            }
        })


    }


}
