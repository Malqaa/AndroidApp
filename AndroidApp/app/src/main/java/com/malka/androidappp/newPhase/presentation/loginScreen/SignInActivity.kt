package com.malka.androidappp.newPhase.presentation.loginScreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.forgot.ForgotPasswordActivty
import com.malka.androidappp.newPhase.presentation.signup.SignupPg1
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.newPhase.models.loginResp.LoginResp
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_sign_in)

        email_tv.text="muhammadumersheraz@gmail.com"
        passwword_tv.text="Test@123"

        Forgot_your_password.setOnClickListener {
            startActivity(Intent(this@SignInActivity, ForgotPasswordActivty::class.java))
        }



        new_registration.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignupPg1::class.java)
            startActivity(intent)
        }



        if (ConstantObjects.currentLanguage ==  ConstantObjects.ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }

        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            setLocate()
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

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
        } else {
            passwword_tv!!.error = null
            true
        }
    }

    fun confirmInput(v: View) {
        if (!validateEmail() or !validatePassword()) {
            return
        } else {
            MakeLoginAPICall(
                email_tv.text.toString().trim(),
                passwword_tv.text.toString().trim(),
                this@SignInActivity
            )
        }
    }


    fun MakeLoginAPICall(email: String, password: String, context: Context) {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<LoginResp?>? = malqa.loginUser(email,password,password)
        call?.enqueue(object : Callback<LoginResp?> {
            override fun onFailure(call: Call<LoginResp?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<LoginResp?>,
                response: Response<LoginResp?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.run {
                        if(this.userObject!=null){
                            ConstantObjects.logged_userid =this.userObject.id
                            ConstantObjects.isBusinessUser = this.userObject.isBusinessAccount
                            val userId: String =this.userObject.id
                            ConstantObjects.logged_userid = userId
                            HelpFunctions.ShowLongToast(
                                getString(R.string.LoginSuccessfully),
                                context
                            )
                            Paper.book().write(SharedPreferencesStaticClass.islogin, true)
                            Paper.book().write(SharedPreferencesStaticClass.user_object, Gson().toJson(this.userObject))
                            setResult(RESULT_OK, Intent())
                            finish()
                        }


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

}
