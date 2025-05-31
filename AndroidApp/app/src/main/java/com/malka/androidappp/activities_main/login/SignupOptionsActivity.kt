package com.malka.androidappp.activities_main.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.Bottmmm
import com.malka.androidappp.activities_main.signup_account.signup_pg1.SignupPg1
import com.malka.androidappp.activities_main.onboarding_intro_slider.OnBoardingIntroSlider
import com.malka.androidappp.botmnav_fragments.activities_main.business_signup.BusinessSignupPg1
import com.malka.androidappp.botmnav_fragments.forgot_password.ForgotPasswordActivty
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.SHARED_PREFS
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.TEXT
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass.Companion.TEXT2
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.user.UserObject
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up_options.*
import kotlinx.android.synthetic.main.fragment_promotional.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader
import java.io.Reader
import java.util.regex.Pattern
import kotlin.error


open class SignupOptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_options)
        supportActionBar?.hide()

        btn_signup_normal.setOnClickListener() {
            val intent = Intent(this@SignupOptionsActivity, SignupPg1::class.java)
            startActivity(intent)
        }

        btn_signup_business.setOnClickListener() {
            val intent = Intent(this@SignupOptionsActivity, BusinessSignupPg1::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
