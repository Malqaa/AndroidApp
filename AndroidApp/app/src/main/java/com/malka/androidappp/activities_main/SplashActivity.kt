package com.malka.androidappp.activities_main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.onboarding_intro_slider.OnBoardingIntroSlider
import com.malka.androidappp.botmnav_fragments.home.view.HomeFragment
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Exception
import java.lang.reflect.Executable

class SplashActivity : BaseActivity() {

    var datafound: Boolean = false;
    var email: String = "";
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideSystemUI(mainContainer)

        GoToHomeScreen()
    }

    fun LogIntoHomeScreen() {
        try {
            loadData()
            var singobj: SignInActivity = SignInActivity()
            if (datafound) {
                singobj.MakeLoginAPICall(email, password, this@SplashActivity, this)
            }
            splash_view.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splash)
            splash_view.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                if (isFinishing) return@OnCompletionListener
                else {
                    if (!datafound || !singobj.loginsuccessful) {
                        startActivity(Intent(this, SignInActivity::class.java))
                        finish()
                    } else if (singobj.loginsuccessful) {
                        val intentt = Intent(this@SplashActivity, OnBoardingIntroSlider::class.java)
                        this@SplashActivity.startActivity(intentt)
                        finish()
                        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    }
                }
            })
            splash_view!!.start()
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex);
        }
    }

    fun GoToHomeScreen() {
        try {
            splash_view.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splash)
            splash_view.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                if (isFinishing) return@OnCompletionListener
                else {
                    val intentt = Intent(this@SplashActivity, SignInActivity::class.java)
                    this@SplashActivity.startActivity(intentt)
                    finish()
                    this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            })
            splash_view!!.start()
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex);
        }
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

