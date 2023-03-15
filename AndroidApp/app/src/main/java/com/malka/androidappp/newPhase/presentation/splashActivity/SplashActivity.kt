package com.malka.androidappp.newPhase.presentation.splashActivity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.network.CommonAPI
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideSystemUI(mainContainer)
       // CommonAPI().getCountry()
        GoToHomeScreen()

    }

    fun GoToHomeScreen() {
        try {
            splash_view.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splash)
            splash_view.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                if (isFinishing) return@OnCompletionListener
                else {
                    val intentt = Intent(this@SplashActivity, MainActivity::class.java)
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



}

