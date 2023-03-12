package com.malka.androidappp.newPhase.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import com.malka.androidappp.BuildConfig
import com.malka.androidappp.R
import com.malka.androidappp.helper.Extension.requestBody
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.Constants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.models.validateAndGenerateOTPResp.OtpData
import com.malka.androidappp.newPhase.models.validateAndGenerateOTPResp.UserVerifiedResp
import com.malka.androidappp.newPhase.models.validateAndGenerateOTPResp.ValidateAndGenerateOTPResp
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.servicemodels.BasicResponse
import com.malka.androidappp.servicemodels.ConstantObjects.Companion.data
import com.malka.androidappp.servicemodels.PostReqVerifyCode
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_signup_pg2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupPg2 : BaseActivity() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpData: OtpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg2)
        countdownTimer = findViewById(R.id.countdownTimer)
        otpData = intent.getParcelableExtra(Constants.otpDataKey)

        var datacode: String? = otpData?.otpCode
        // println("hhh $datacode")
        /***thisForTest*/
        if (BuildConfig.DEBUG) {
            pinview.value = datacode!!
        }


        resend_btn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                resendOTPApi()
            }
        }
        startTimeCounter()
    }

    /**resend OTP*/
    fun resendOTPApi() {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val userPhone: String? = otpData?.phoneNumber
        val call = malqa.resendOtp(userPhone.toString(), Lingver.getInstance().getLanguage())
        call.enqueue(object : Callback<ValidateAndGenerateOTPResp> {

            override fun onFailure(call: Call<ValidateAndGenerateOTPResp>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }

            }

            override fun onResponse(
                call: Call<ValidateAndGenerateOTPResp>,
                response: Response<ValidateAndGenerateOTPResp>
            ) {
                if (response.isSuccessful) {
                    val otppcode = response.body()?.otpData?.otpCode
                    if (BuildConfig.DEBUG) {
                        pinview.value = otppcode
                    }
                    startTimeCounter()
                    button3.isEnabled = true
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationFailed),
                        this@SignupPg2
                    )
                }
            }
        })
    }

    fun startTimeCounter() {
        object : CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
                countdownTimer.text = getString(R.string.Seconds, seconds)

            }

            override fun onFinish() {
                countdownTimer.text = getString(R.string.CodeExpired)
                button3.isEnabled = false
            }

        }.start()
    }

    /***/
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
        val otpcode: String? = pinview.value
       val call: Call<UserVerifiedResp> = malqa.verifyOtp(otpData?.phoneNumber.toString(),otpcode.toString())

        call.enqueue(object : Callback<UserVerifiedResp> {

            override fun onFailure(call: Call<UserVerifiedResp>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }

            }
            override fun onResponse(
                call: Call<UserVerifiedResp>,
                response: Response<UserVerifiedResp>
            ) {

                if (response.isSuccessful) {
                    val data = response.body()
                    if (data!!.status_code == 200) {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.VerificationSuccessful),
                            this@SignupPg2
                        )
                        signup2next()
                    } else {
                        if(data?.message!=null){
                            showError(data.message!!)
                        }else{
                            showError(getString(R.string.serverError))
                        }
                    }

                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationFailed)+" "+response.code(),
                        this@SignupPg2
                    )
                }
            }
        })
    }
//    fun apicallSignup2() {
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
////        val userId: String? = intent.getStringExtra("userid")
//        var userId: Int = 0
//        otpData?.let {
//            userId = it.id
//        }
//        // val otpcode: String? = intent.getStringExtra("datacode")
//        val otpcode: String? = pinview.value
//        val call: Call<BasicResponse> =
//            malqa.verifycode(PostReqVerifyCode(code = otpcode!!, userId = userId.toString()))
//        call.enqueue(object : Callback<BasicResponse> {
//
//            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }
//
//            }
//
//            override fun onResponse(
//                call: Call<BasicResponse>,
//                response: Response<BasicResponse>
//            ) {
//
//                if (response.isSuccessful) {
//                    val data = response.body()
//                    if (data!!.status_code == 200) {
//                        HelpFunctions.ShowLongToast(
//                            getString(R.string.VerificationSuccessful),
//                            this@SignupPg2
//                        )
//                        signup2next()
//                    } else {
//                        showError(data.message)
//                    }
//
//                } else {
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.VerificationFailed),
//                        this@SignupPg2
//                    )
//                }
//            }
//        })
//    }


    ////////////////////////////////Switch Activities/////////////////////////////////////////
    fun signup2next() {
        val userIdupdate: String? = intent.getStringExtra("userid")
        val intent2 = Intent(this@SignupPg2, SignupPg3::class.java)
        intent2.putExtra(Constants.otpDataKey, otpData)
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

/*    fun resendCodeApi() {
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val email: String? = intent.getStringExtra("dataemail")
//        val passcode: String? = intent.getStringExtra("datapassword")
//        val resendmodel = User(email=email!!, password = passcode!!)
//        val call = malqa.resendcode(resendmodel)
//        call.enqueue(object : Callback<BasicResponse> {
//
//            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }
//
//            }
//
//            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
//
//                if (response.isSuccessful) {
//                    val otppcode = response.body()?.data.toString()
//                    if(BuildConfig.DEBUG){
//                        pinview.value = otppcode
//                    }
//                    startTimeCounter()
//                    button3.isEnabled = true
//                } else {
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.VerificationFailed),
//                        this@SignupPg2
//                    )
//                }
//            }
//        })
//}
*/


}