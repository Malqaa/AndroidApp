package com.malka.androidappp.activities_main

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.signup_account.signup_pg3.SignupPg3
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.CountryRespone
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    var email: String = "";
    var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideSystemUI(mainContainer)
        getCountry(culture())

        GoToHomeScreen()
        //   getCountry("ar")
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

    fun getCountry(culture: String) {


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getCountry(culture)
        call.enqueue(object : retrofit2.Callback<CountryRespone?> {
            override fun onFailure(call: retrofit2.Call<CountryRespone?>?, t: Throwable) {

            }

            override fun onResponse(
                call: retrofit2.Call<CountryRespone?>,
                response: retrofit2.Response<CountryRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: CountryRespone = response.body()!!
                        if (respone.status_code == 200) {
                            ConstantObjects.countryList = respone.data
                        }
                    }

                }
            }
        })
    }

}

