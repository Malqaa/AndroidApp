package com.malka.androidappp.newPhase.presentation.forgotPasswordActivity.activity2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.presentation.loginScreen.LoginViewModel
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.*
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.button3
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.confirmPass
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.ibBack
import kotlinx.android.synthetic.main.activity_forgot_pass_otpcode.textPass

class ActivityForgotPassOtpcode : BaseActivity() {
    private var START_TIME_IN_MILLIS: Long = 60000

    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private lateinit var loginViewModel: LoginViewModel
    var email: String = ""
    var expireMinutes: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_otpcode)
        email = intent.getStringExtra(ConstantObjects.emailKey) ?: ""
//        val getcodee:String? = intent.getStringExtra("getcode")
//        if(BuildConfig.DEBUG){
//            pinview.setValue(getcodee?:"")
//        }
        setupLoginViewModel()
        ContianerResend.hide()
        countdownTimer.hide()
        setClickListeners()

        loginViewModel.getConfigurationResp(ConstantObjects.configration_otpExpiryTime)
    }

    private fun setupLoginViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        loginViewModel.isNetworkFail.observe(this, Observer {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
        loginViewModel.errorResponseObserver.observe(this, Observer {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
        loginViewModel.changePasswordAfterForgetObserver.observe(this, Observer {
            if (it.status_code == 200) {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        this
                    )
                }
                signInAfterSignUp()
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this
                    )
                }
            }

        })

        loginViewModel.changePasswordAfterForgetObserver.observe(this, Observer {
            if (it.status_code == 200) {
                startTimeCounter(expireMinutes)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this
                    )
                }
            }

        })
        loginViewModel.configurationRespObserver.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                countdownTimer.show()
                try {
                    expireMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
                startTimeCounter(expireMinutes)
            }
        }
    }

    fun signInAfterSignUp() {
        val intentsignin = Intent(this, SignInActivity::class.java)
        intentsignin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentsignin)
    }

    /**clickEvents*/
    fun startTimeCounter(expireMinutes: Int) {
        object : CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                ContianerResend.hide()
                countdownTimer.show()
                mTimeLeftInMillis = millisUntilFinished
                var seconds = (mTimeLeftInMillis / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                var timeText = (String.format("%02d", minutes) + ":" + String.format(
                    "%02d",
                    seconds
                )).toString()

                countdownTimer.text = "${getString(R.string.Seconds2)}: $timeText"
            }

            override fun onFinish() {
                countdownTimer.text = getString(R.string.CodeExpired)
                ContianerResend.show()
                countdownTimer.hide()
            }

        }.start()
    }

    private fun setClickListeners() {
        ibBack.setOnClickListener {
            onBackPressed()
        }
        resend_btn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                ContianerResend.hide()
                countdownTimer.show()
                startTimeCounter(expireMinutes)
                // val userPhone: String? = otpData?.phoneNumber
                loginViewModel.forgetPassword(email)
                //resendOTPApi()
            }
        }
        button3.setOnClickListener {
            var readyToGo = true
            if (!validatePin() or !validateSignupPassword() or !validateSignupConfrmPassword()) {
                readyToGo = false
            } else {
                loginViewModel.changePasswordAfterForget(
                    email,
                    pinview.value,
                    textPass.text.toString().trim()
                )
            }
        }
    }

    private fun validateSignupPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            textPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!HelpFunctions.PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textPass!!.error = getString(R.string.Passwordtooweak)
            false
        } else {
            textPass!!.error = null
            true
        }
    }

    //confirmpass validation
    private fun validateSignupConfrmPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = confirmPass!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            confirmPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            confirmPass!!.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            confirmPass!!.error = null
            true
        }
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
            //apicallSignup2()
            val otpcode: String? = pinview.value
            loginViewModel.forgetPassword(email)
        }
    }
    //////////////////////////////////////Api Post Verify//////////////////////////////////////////////////
//    fun verifyotpcode() {
//        HelpFunctions.startProgressBar(this)
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val userId: String? = intent.getStringExtra("getid")
//        val otpcode: String = pinview.getValue().toString().trim()
//        val call= malqa.verifycode(PostReqVerifyCode(userId=userId.toString(),code= otpcode))
//        call.enqueue(object : Callback<BasicResponse> {
//
//            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//
//                t.message?.let {
//                    HelpFunctions.ShowLongToast(it, this@ActivityForgotPassOtpcode) }
//
//            }
//
//            override fun onResponse(
//                call: Call<BasicResponse>,
//                response: Response<BasicResponse>
//            ) {
//
//                if (response.isSuccessful) {
//                    val data=response.body()
//                    if (data!!.status_code == 200) {
//                        val getpin: String = pinview234.getValue().toString().trim()
//                        val getidd = intent.getStringExtra("getid")
//                        val intenddd = Intent(this@ActivityForgotPassOtpcode, ForgotChangepassActivity::class.java)
//                        intenddd.putExtra("getidd",getidd)
//                        intenddd.putExtra("getcodee",getpin)
//                        startActivity(intenddd)
//                        finish()
//                    } else {
//                        showError(data.message)
//                    }
//
//
//                } else {
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.VerificationFailed),
//                        this@ActivityForgotPassOtpcode
//                    )
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//        })
//    }
}