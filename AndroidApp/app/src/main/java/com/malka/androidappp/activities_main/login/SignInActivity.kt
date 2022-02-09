package com.malka.androidappp.activities_main.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.activities_main.Bottmmm
import com.malka.androidappp.botmnav_fragments.forgot_password.ForgotPasswordActivty
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.SHARED_PREFS
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.TEXT
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.TEXT2
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hideLoader
import com.malka.androidappp.helper.showLoader
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_sign_in.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader
import java.io.Reader
import java.util.regex.Pattern


open class SignInActivity : BaseActivity() {
    var datafound: Boolean = false
    var calledfromsigninactivity = false
    var loginsuccessful = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calledfromsigninactivity = true
        loadData()
        if (datafound) {
            loginApiCallwithSaveData()
        }

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
            val intent = Intent(this@SignInActivity, SignupOptionsActivity::class.java)
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
    fun keepmesignedcheck() {
        saveData()
    }


    open fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TEXT, email_tv.getText().toString())
        editor.putString(TEXT2, passwword_tv.getText().toString())
        editor.apply()
    }

    fun clearData() {
        email_tv.setText("")
        passwword_tv.setText("")
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TEXT, "")
        editor.putString(TEXT2, "")
        editor.apply()
    }

    open fun loadData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        text = sharedPreferences.getString(TEXT, "")
        text2 = sharedPreferences.getString(TEXT2, "")

        if ((text != null && text.toString().trim().length > 0) &&
            (text2 != null && text2.toString().trim().length > 0)
        ) {
            datafound = true
        }
    }


    open fun updateViews() {
        email_tv.setText(text)
        passwword_tv.setText(text2)

    }

    ////////////////////////////////SignIn to homepage with parse data///////////////////////////////////////
    //fun signtohome()
    fun signtohome(context: Context, activity: Activity) {
        val intentt = Intent(context, Bottmmm::class.java)
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

    companion object {
        val PASSWORD_PATTERN = Pattern.compile(
            "^" + "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +  //any letter
                    // "(?=.*[@#$%^&+=])" +  //at least 1 special character
                    "(?=\\S+$)" +  //no white spaces
                    ".{4,}" +  //at least 4 characters
                    "$"
        )
    }

    fun MakeLoginAPICall(email: String, password: String, context: Context, activity: Activity) {
        loader.showLoader()
        val malqa: MalqaApiService = RetrofitBuilder.createAccountsInstance()
        val emaill = email.toString().trim()
        val passwordd = password.toString().trim()
        val login = LoginClass(emaill, passwordd)

        //val call: Call<LoginClass?>? = malqa.loginUser(LoginClass(emaill, passwordd))
        val call: Call<ResponseBody?>? = malqa.loginUser(login)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                loader.hideLoader()
                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show()
            }

            var isBusinessUser: Boolean = false
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {

                    val reader: Reader = InputStreamReader(response.body()?.byteStream(), "UTF-8")
                    val data = Gson().fromJson(reader, LoginResponseBack::class.java)
                    //Zack
                    //Date: 11/04/2020
                    ConstantObjects.logged_userid = data.data.id
                    ConstantObjects.isBusinessUser = false
                    if (data.data.isBusinessUser > 0) {
                        isBusinessUser = true
                        ConstantObjects.isBusinessUser = true
                    } else {
                        isBusinessUser = false
                        ConstantObjects.isBusinessUser = false
                    }
                    // To check if user is approved user or not
                    if (data.data.isBusinessUser < 1 || data.data.isBusinessUser > 1) {
                        loginsuccessful = true
                        val userId: String = data.data.id
                        ConstantObjects.logged_userid = userId
                        if (calledfromsigninactivity != null && calledfromsigninactivity) {
//                            Toast.makeText(
//                                this@SignInActivity,
//                                "Login Successfully",
//                                Toast.LENGTH_LONG
//                            ).show()
                            HelpFunctions.ShowLongToast(
                                getString(R.string.LoginSuccessfully),
                                context
                            )
                            keepmesignedcheck()
                            GoToHome()
                        }
                    } else {
                        loginsuccessful = false
//                        Toast.makeText(
//                            context,
//                            "Your account is not approved",
//                            Toast.LENGTH_LONG
//                        ).show()
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Youraccountisnotapproved),
                            context
                        )
                    }
                } else {
//                    Toast.makeText(
//                        context,
//                        "Invalid Username or Password",
//                        Toast.LENGTH_LONG
//                    ).show()
                    HelpFunctions.ShowLongToast(
                        getString(R.string.InvalidUsernameorPassword),
                        context
                    )
                }
                loader.hideLoader()
            }
        })
    }

    fun GoToHome() {
        signtohome(this@SignInActivity, this)
    }

    fun loginApiCall() {

        MakeLoginAPICall(
            email_tv.text.toString().trim(),
            passwword_tv.text.toString().trim(),
            this@SignInActivity,
            this
        )
    }

    fun loginApiCallwithSaveData() {
        MakeLoginAPICall(text.toString().trim(), text2.toString().trim(), this@SignInActivity, this)
    }


}
