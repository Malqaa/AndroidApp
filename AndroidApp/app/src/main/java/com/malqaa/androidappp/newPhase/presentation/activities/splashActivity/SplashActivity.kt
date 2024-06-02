package com.malqaa.androidappp.newPhase.presentation.activities.splashActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    var productId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideSystemUI(mainContainer)
        // CommonAPI().getCountry()
        // GoToHomeScreen()
        lifecycleScope.launch(Dispatchers.IO) {
            /**get Product id from share product link*/
            val mainIntent = intent
            if (mainIntent != null && mainIntent.data != null) {
                if (mainIntent.data?.scheme == "http") {
                    val data = mainIntent.data
                    if (data?.query != null) {
                       // println("hhhh "+ getLinkFromLocalPassedData(data))
                        productId=getLinkFromLocalPassedData(data)
                    }
                }
            }
            delay(5300)
            val intentt = Intent(this@SplashActivity, MainActivity::class.java).apply {
                putExtra("productId",productId)
            }
            startActivity(intentt)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


    }

    private fun getLinkFromLocalPassedData(data: Uri): Int {
        //System.out.println("hhhh q not null")
        val linkQuery: String = data.query.toString()
        var porductId = 0
        return try {
            productId =
                linkQuery.substring(linkQuery.indexOf('=') + 1, linkQuery.length)
                    .toInt()
            productId
            //  System.out.println("hhhh " + data.path + linkQuery + " " + prefix + " " + jobID)
        } catch (e: Exception) {
            //System.out.println("hhhh e" + e.message)
            productId
        }
    }
//    fun GoToHomeScreen() {
//        try {
//            splash_view.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.splash)
//            splash_view.setOnCompletionListener(MediaPlayer.OnCompletionListener {
//                if (isFinishing) return@OnCompletionListener
//                else {
//                    val intentt = Intent(this@SplashActivity, MainActivity::class.java)
//                    this@SplashActivity.startActivity(intentt)
//                    finish()
//                    this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                }
//            })
//            splash_view!!.start()
//        } catch (ex: Exception) {
//            HelpFunctions.ReportError(ex);
//        }
//    }


}

