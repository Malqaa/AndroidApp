package com.malka.androidappp.activities_main.signup_account.signup_pg2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.signup_account.signup_pg3.SignupPg3
import com.malka.androidappp.activities_main.signup_account.signup_pg3.User
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_signup_pg2.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStreamReader
import java.io.Reader


class SignupPg2 : AppCompatActivity() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    var counter = 60
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg2)
        supportActionBar?.hide()


        // OTP expiry timer
        countdownTimer = findViewById(R.id.countdownTimer)

        //////////////////////Parse Mobile Num on textview/////////////////////////


        //////////////////////Parse code Num on pinview/////////////////////////
      //  val datacode: String? = intent.getStringExtra("datacode")
        val datacode="1234"
        pinview.value = datacode!!


        textView3501.setOnClickListener() {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                resendCodeApi()
                startTimeCounter(View(this@SignupPg2))
                button3.isEnabled = true
            }

        }

        startTimeCounter(View(this@SignupPg2))
    }


    private fun validatePin(): Boolean {
        val inputtt = pinview.value
        return if (inputtt.length != 4) {
            redmessage.visibility = View.VISIBLE
            redmessage.text = getString(R.string.Fieldcantbeempty)
            false
        } else {
            redmessage.error = null
            redmessage.visibility = View.GONE
            true
        }
    }

    fun SignuuPg2confirmInput(v: View) {
        if (!validatePin()) {
            return
        } else {

            apicallSignup2()
        }
    }


    //////////////////////////////////////Api Post Verify//////////////////////////////////////////////////
    fun apicallSignup2() {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val userId: String? = intent.getStringExtra("userid")
        val otpcode: String? = intent.getStringExtra("datacode")
        val call: Call<PostReqVerifyCode> = malqa.verifycode(PostReqVerifyCode(userId!!, otpcode!!))
        call.enqueue(object : Callback<PostReqVerifyCode> {

            override fun onFailure(call: Call<PostReqVerifyCode>, t: Throwable) {

                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }

            }

            override fun onResponse(
                call: Call<PostReqVerifyCode>,
                response: Response<PostReqVerifyCode>
            ) {

                if (response.isSuccessful) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationSuccessful),
                        this@SignupPg2
                    )
                    signup2next()
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationFailed),
                        this@SignupPg2
                    )
                }
            }
        })
    }

    ////////////////////////////////Switch Activities/////////////////////////////////////////
    fun signup2next() {
        val userIdupdate: String? = intent.getStringExtra("userid")
        val intent2 = Intent(this@SignupPg2, SignupPg3::class.java)
        intent2.putExtra("useridupdate", userIdupdate)
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent2)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupPg2, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    fun resendCodeApi() {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val email: String? = intent.getStringExtra("dataemail")
        val passcode: String? = intent.getStringExtra("datapassword")
        val resendmodel = User(email=email!!, password = passcode!!)

        val call: Call<ResponseBody> = malqa.resendcode(resendmodel)
        call.enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    val reader: Reader = InputStreamReader(response.body()?.byteStream(), "UTF-8")
                    val data = Gson().fromJson(reader, ResendCodeResponseBodyModel::class.java)
                    val otppcode = data.data
                    pinview.value = otppcode

                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationFailed),
                        this@SignupPg2
                    )
                }
            }
        })
    }

    fun startTimeCounter(view: View) {

        object : CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                var seconds = (mTimeLeftInMillis / 1000).toInt() % 60
//                countdownTimer.text = "$seconds Seconds"
                countdownTimer.text = getString(R.string.Seconds, seconds)

            }

            override fun onFinish() {
                countdownTimer.text = getString(R.string.CodeExpired)
                button3.isEnabled = false
            }

        }.start()
    }

}