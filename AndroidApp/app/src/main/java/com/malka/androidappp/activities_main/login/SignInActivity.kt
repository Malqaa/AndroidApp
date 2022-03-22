package com.malka.androidappp.activities_main.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.activities_main.forgot.forgot_password.ForgotPasswordActivty
import com.malka.androidappp.activities_main.signup_account.signup_pg1.SignupPg1
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.islogin
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


open class SignInActivity : BaseActivity() {
    var calledfromsigninactivity = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calledfromsigninactivity = true

        // To set language


        ConstantObjects.currentLanguage = getLanguage()

        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()

        updateViews()


        Forgot_your_password.setOnClickListener {
            val intent = Intent(this@SignInActivity, ForgotPasswordActivty::class.java)
            startActivity(intent)
        }



        new_registration.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignupPg1::class.java)
            startActivity(intent)
        }



        if (ConstantObjects.currentLanguage == "en") {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }

        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            when (position) {
                0 -> {
                    setLocate("en")
                }
                1 -> {
                    setLocate("ar")
                }

            }
            recreate()
        }

    }

    ///////////////////////////////////save id/password on checkbox all functions //////////calling on top and down///////////////////////////
    private var text: String? = null
    private var text2: String? = null





    open fun updateViews() {
        email_tv.setText(text)
        passwword_tv.setText(text2)

    }

    ////////////////////////////////SignIn to homepage with parse data///////////////////////////////////////
    //fun signtohome()
    fun signtohome(context: Context, activity: Activity) {
        val intentt = Intent(context, MainActivity::class.java)
        context.startActivity(intentt)
        finish()
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }

    ////////////////////////////////Data Validation///////////////////////////////////


    private fun validateEmail(): Boolean {
        val emailInput = email_tv!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            email_tv!!.error = getString(R.string.Emailisrequired)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email_tv!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            email_tv!!.error = null
            true
        }
    }


    private fun validatePassword(): Boolean {
        val passwordInput = passwword_tv!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            passwword_tv!!.error = getString(R.string.Passwordisrequired)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwword_tv!!.error = getString(R.string.Passwordtooweak)
            false
        } else {
            passwword_tv!!.error = null
            true
        }
    }

    fun confirmInput(v: View) {
        if (!validateEmail() or !validatePassword()) {
            return
        } else {
            loginApiCall()
        }


    }



    fun MakeLoginAPICall(email: String, password: String, context: Context) {
        HelpFunctions.startProgressBar(this)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val login = LoginClass(email, password)

        val call: Call<LoginResponseBack?>? = malqa.loginUser(login)
        call?.enqueue(object : Callback<LoginResponseBack?> {
            override fun onFailure(call: Call<LoginResponseBack?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<LoginResponseBack?>, response: Response<LoginResponseBack?>) {
                if (response.isSuccessful) {

                    //Zack
                    //Date: 11/04/2020
                    ConstantObjects.logged_userid = response.body()!!.data.id
                    ConstantObjects.isBusinessUser = response.body()!!.data.isBusinessUser > 0
                    // To check if user is approved user or not
                    if (response.body()!!.data.isBusinessUser < 1 || response.body()!!.data.isBusinessUser > 1) {
                        val userId: String = response.body()!!.data.id
                        ConstantObjects.logged_userid = userId
                        if (calledfromsigninactivity != null && calledfromsigninactivity) {
                            HelpFunctions.ShowLongToast(
                                getString(R.string.LoginSuccessfully),
                                context
                            )
                            Paper.book().write(islogin,true)
                            Paper.book().write(SharedPreferencesStaticClass.userData,response.body()!!.data)
                           finish()
                        }
                    } else {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Youraccountisnotapproved),
                            context
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.InvalidUsernameorPassword),
                        context
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })
    }


    fun loginApiCall() {

        MakeLoginAPICall(
            email_tv.text.toString().trim(),
            passwword_tv.text.toString().trim(),
            this@SignInActivity
        )
    }
}
