package com.malka.androidappp.activities_main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.onboarding_intro_slider.OnBoardingIntroSlider
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions

class SplashActivity : AppCompatActivity() {

    var datafound: Boolean = false;
    var email: String = "";
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        LogIntoHomeScreen()
    }

    fun LogIntoHomeScreen() {
        try {
            loadData()
            var singobj = SignInActivity()
            if (datafound) {
                singobj.MakeLoginAPICall(email, password, this@SplashActivity, this)
            }
            if (!datafound || !singobj.loginsuccessful) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            } else if (singobj.loginsuccessful) {
                val intentt = Intent(this@SplashActivity, Bottmmm::class.java)
                this@SplashActivity.startActivity(intentt)
                finish()
                this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex);
        }
    }

    fun GoToHomeScreen() {

        val handler = Handler()
        handler.postDelayed(Runnable {
            val intentt = Intent(this@SplashActivity, Bottmmm::class.java)
            this@SplashActivity.startActivity(intentt)
            finish()
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }, 2000) // you can increase or decrease the timelimit of your screen



    }

    fun loadData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SharedPreferencesStaticClass.SHARED_PREFS, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(SharedPreferencesStaticClass.TEXT, "").toString()
        password = sharedPreferences.getString(SharedPreferencesStaticClass.TEXT2, "").toString()

        if ((email != null && email.trim().length > 0) &&
            (password != null && password.trim().length > 0)
        ) {
            datafound = true
        }
    }
}

