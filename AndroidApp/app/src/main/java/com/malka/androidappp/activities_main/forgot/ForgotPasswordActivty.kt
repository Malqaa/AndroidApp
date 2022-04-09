package com.malka.androidappp.activities_main.forgot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.signup_account.signup_pg3.User
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.edittext.TextFieldComponent
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.GeneralRespone
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPasswordActivty : BaseActivity() {




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
        HelpFunctions.startProgressBar(this)

        val emailfogotpass: String = editText4.text.toString().trim()
        val modeldataitem = User(email  =emailfogotpass, password = "String")
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call= malqaa.forgotpassemail(modeldataitem)


        call.enqueue(object : Callback<GeneralRespone> {
            override fun onFailure(call: Call<GeneralRespone>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(getString(R.string.Somethingwentwrong),applicationContext)
            }

            override fun onResponse(
                call: Call<GeneralRespone>,
                response: Response<GeneralRespone>
            ) {

                if (response.isSuccessful) {
                    val query=HelpFunctions.getQueryString(response.body()!!.data)
                    val userId=query.get("Id")?:""
                    val code=query.get("code")?:""
                    if(userId.isEmpty()||code.isEmpty()){
                        showError( getString(R.string.ThisEmailAddressDoesnotExist))
                    }else{
                        val intentd = Intent(
                            this@ForgotPasswordActivty,
                            ActivityForgotPassOtpcode::class.java
                        )
                        intentd.putExtra("getid", userId)
                        intentd.putExtra("getcode", code)
                        startActivity(intentd)
                        finish()
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.Error),
                        applicationContext
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })


    }


}
