package com.malka.androidappp.activities_main.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.onboarding_intro_slider.OnBoardingIntroSlider
import com.malka.androidappp.botmnav_fragments.forgot_password.ForgotPasswordActivty
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.SHARED_PREFS
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.TEXT
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.TEXT2
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_promotional.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader
import java.io.Reader
import java.util.*
import java.util.regex.Pattern


open class SignInActivity : AppCompatActivity() {
    var datafound: Boolean = false
    var calledfromsigninactivity = false
    var loginsuccessful = false

    lateinit var email: TextInputLayout
    lateinit var password: TextInputLayout

    var currentLanguage: String = Locale.getDefault().language

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calledfromsigninactivity = true
        loadData()
        if (datafound) {
            loginApiCallwithSaveData()
        }

        // To set language
        loadLocate()

        // Storing language for further use
        ConstantObjects.currentLanguage = currentLanguage

        setContentView(R.layout.activity_sign_in)
        supportActionBar?.hide()
        textEmail = findViewById(R.id.editText3)
        textPassword = findViewById(R.id.editText4)

        ///////////////save password part 2//////////////////
        updateViews()
        keepmesigneduncheck()

        imageButton33.setOnClickListener() {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        val clickspan = object : ClickableSpan() {
            //It removes underline from clickablespan
            override fun updateDrawState(ds: TextPaint) { // override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }

            override fun onClick(view: View) {
                //Zack
                //Date: 03/17/2021
                //val intent = Intent(this@SignInActivity, SignupPg1::class.java)
                val intent = Intent(this@SignInActivity, SignupOptionsActivity::class.java)
                startActivity(intent)
            }
        }

        val clickrecover = object : ClickableSpan() {

            //It removes underline from clickablespan
            override fun updateDrawState(ds: TextPaint) { // override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }

            override fun onClick(view: View) {
                val intent = Intent(this@SignInActivity, ForgotPasswordActivty::class.java)
                startActivity(intent)
            }
        }

        email = findViewById(R.id.textinput_edittext3)
        password = findViewById(R.id.textinput_editText4)

        email.hint = getString(R.string.type_your_email_here)
        password.hint = getString(R.string.type_your_password_here)


        changeLanguage.setOnClickListener() {

            if (currentLanguage == "en") {

                setLocate("ar")
                email.hint = getString(R.string.type_your_email_here)
                password.hint = getString(R.string.type_your_password_here)
                recreate()

            } else {
                setLocate("en")
                email.hint = getString(R.string.type_your_email_here)
                password.hint = getString(R.string.type_your_password_here)
                recreate()
            }
        }


        /////////////////////////ClickableSpan for Signup////////////////////////////////
        val myId = getString(R.string.signuptext)
        val mSpannableString = SpannableString(myId)

        // To get the current language and set span accordingly
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")

        if (language == "en") {
            mSpannableString.setSpan(clickspan, 22, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            mSpannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.greenspan)),
                22,
                29,
                0
            )
            // make text bold
            mSpannableString.setSpan(StyleSpan(Typeface.BOLD), 22, 29, 0)
            textView33.movementMethod = LinkMovementMethod.getInstance()
            textView33.text = mSpannableString
        } else {
            mSpannableString.setSpan(clickspan, 14, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            mSpannableString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.greenspan)),
                14,
                20,
                0
            )
            // make text bold
            mSpannableString.setSpan(StyleSpan(Typeface.BOLD), 14, 20, 0)
            textView33.movementMethod = LinkMovementMethod.getInstance()
            textView33.text = mSpannableString
        }


        /////////////////////////ClickableSpan for RecoverPass////////////////////////////////
        val mSpannablerecover = SpannableString(getString(R.string.recoverpass))

        if (language == "en") {
            mSpannablerecover.setSpan(clickrecover, 17, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            mSpannablerecover.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.greenspan)),
                17,
                29,
                0
            )
            // make text bold
            mSpannablerecover.setSpan(StyleSpan(Typeface.BOLD), 17, 29, 0)
            textView12.movementMethod = LinkMovementMethod.getInstance()
            textView12.text = mSpannablerecover
        } else {
            mSpannablerecover.setSpan(clickrecover, 18, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            mSpannablerecover.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.greenspan)),
                18,
                27,
                0
            )
            // make text bold
            mSpannablerecover.setSpan(StyleSpan(Typeface.BOLD), 18, 27, 0)
            textView12.movementMethod = LinkMovementMethod.getInstance()
            textView12.text = mSpannablerecover
        }

    }

    ///////////////////////////////////save id/password on checkbox all functions //////////calling on top and down///////////////////////////
    private var text: String? = null
    private var text2: String? = null
    fun keepmesignedcheck() {
        if (signin_checkbox.isChecked == true) {
            saveData()
        }
    }

    fun keepmesigneduncheck() {
        signin_checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                //saveData()
            } else {
                //clearData()
            }
        })
    }

    open fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(TEXT, editText3.getText().toString())
        editor.putString(TEXT2, editText4.getText().toString())
        editor.apply()
    }

    fun clearData() {
        editText3.setText("")
        editText4.setText("")
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
        editText3.setText(text)
        editText4.setText(text2)
        if (text!!.isEmpty() or text2!!.isEmpty()) {
            signin_checkbox.setChecked(false)
        } else {
            signin_checkbox.setChecked(true)
        }
    }

    ////////////////////////////////SignIn to homepage with parse data///////////////////////////////////////
    //fun signtohome()
    fun signtohome(context: Context, activity: Activity) {
        val intentt = Intent(context, OnBoardingIntroSlider::class.java)
        context.startActivity(intentt)
        finish()
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }

    ////////////////////////////////Data Validation///////////////////////////////////
    private var textEmail: EditText? = null
    private var textPassword: EditText? = null


    private fun validateEmail(): Boolean {
        val emailInput = textEmail!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            textinput_edittext3!!.error = getString(R.string.Emailisrequired)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textinput_edittext3!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            textinput_edittext3!!.error = null
            true
        }
    }


    private fun validatePassword(): Boolean {
        val passwordInput = textPassword!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            textinput_editText4!!.error = getString(R.string.Passwordisrequired)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textinput_editText4!!.error = getString(R.string.Passwordtooweak)
            false
        } else {
            textinput_editText4!!.error = null
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
        val malqa: MalqaApiService = RetrofitBuilder.createAccountsInstance()
        val emaill = email.toString().trim()
        val passwordd = password.toString().trim()
        val login = LoginClass(emaill, passwordd)

        //val call: Call<LoginClass?>? = malqa.loginUser(LoginClass(emaill, passwordd))
        val call: Call<ResponseBody?>? = malqa.loginUser(login)
        call?.enqueue(object : Callback<ResponseBody?> {
            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
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
            }
        })
    }

    fun GoToHome() {
        signtohome(this@SignInActivity, this)
    }

    fun loginApiCall() {
        MakeLoginAPICall(
            editText3.text.toString().trim(),
            editText4.text.toString().trim(),
            this@SignInActivity,
            this
        )
    }

    fun loginApiCallwithSaveData() {
        MakeLoginAPICall(text.toString().trim(), text2.toString().trim(), this@SignInActivity, this)
    }


    //Methods For language
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
    }


}
