package com.malka.androidappp.newPhase.presentation.signup.activity2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.BuildConfig
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.signup.activity3.SignupCreateNewUser
import com.malka.androidappp.newPhase.presentation.signup.signupViewModel.SignupViewModel
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_confirm_change_number.*
import kotlinx.android.synthetic.main.activity_signup_pg2.*
import kotlinx.android.synthetic.main.activity_signup_pg2.button3
import kotlinx.android.synthetic.main.activity_signup_pg2.pinview
import kotlinx.android.synthetic.main.activity_signup_pg2.redmessage
import kotlinx.android.synthetic.main.activity_signup_pg2.resendCodeAfterExpire
import kotlinx.android.synthetic.main.activity_signup_pg2.resend_btn


class SignupOTPVerificationActivity : BaseActivity() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpData: OtpData? = null
    private lateinit var signupViewModel: SignupViewModel
    var expireMinutes: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg2)
        countdownTimer = findViewById(R.id.countdownTimer)
        otpData = intent.getParcelableExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setClickListeners()
        resendCodeAfterExpire.hide()
        /***thisForTest*/
        if (BuildConfig.DEBUG) {
            val datacode: String? = otpData?.otpCode
            // println("hhh $datacode")
            pinview.value = datacode!!
        }
        signupViewModel.getConfigurationResp(ConstantObjects.otpExpiryTime)

    }

    private fun setupRegisterViewModel() {
        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        signupViewModel.isNetworkFail.observe(this, Observer {
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
        signupViewModel.errorResponseObserver.observe(this, Observer {
            when (it.message) {
                "OTPExpired" -> {
                    showError(getString(R.string.CodeExpired))
                }
                "OTPWrongTrialsExcced" -> {
                    showError(getString(R.string.OTPWrongTrialsExcced))
                }
                "WrongTrialsLimitexceeds" -> {
                    showError(getString(R.string.OTPWrongTrialsExcced))
                }
                "ResetPasswordCodeExpired" -> {

                }
                "InvalidOTP" -> {
                    showError(getString(R.string.InvalidOTP))
                }

                "CodeNotCorrect" -> {
                    showError(getString(R.string.InvalidOTP))
                }
                "Success" -> {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationSuccessful),
                        this@SignupOTPVerificationActivity
                    )
                    signup2next()
                }
                else -> {
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
            }


        })
        signupViewModel.validateAndGenerateOTPObserver.observe(this) { validateUserAndGenerateOTP ->
            if (validateUserAndGenerateOTP.otpData != null) {
                startTimeCounter(expireMinutes)
                button3.isEnabled = true
                /***thisForTest*/
                val otppcode = validateUserAndGenerateOTP.otpData!!.otpCode
                if (BuildConfig.DEBUG) {
                    pinview.value = otppcode
                }
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }
        }
        signupViewModel.userVerifiedObserver.observe(this) { userVerified ->
//         *****      Api response status: *****
//            ResetPasswordCodeExpired
//            WrongTrialsLimitexceeds
//            CodeNotCorrect
//            Failed
//            Success
//          *****  Api resonse status  :****
//            OTPExpired
//            OTPWrongTrialsExcced
//            InvalidOTP
//            Success
            println("hhhh "+userVerified.message)
            when (userVerified.message) {
                "OTPExpired" -> {
                    showError(getString(R.string.CodeExpired))
                }
                "OTPWrongTrialsExcced" -> {
                    showError(getString(R.string.OTPWrongTrialsExcced))
                }
                "WrongTrialsLimitexceeds" -> {
                    showError(getString(R.string.OTPWrongTrialsExcced))
                }
                "ResetPasswordCodeExpired" -> {

                }
                "InvalidOTP" -> {
                    showError(getString(R.string.InvalidOTP))
                }

                "CodeNotCorrect" -> {
                    showError(getString(R.string.InvalidOTP))
                }
                "Success","Otp verified successfully" -> {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.VerificationSuccessful),
                        this@SignupOTPVerificationActivity
                    )
                    signup2next()
                }
                else -> {
                    showError(getString(R.string.serverError))
                }
            }

        }
        signupViewModel.configurationRespObserver.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
                startTimeCounter(expireMinutes)
            }
        }
    }

    /**clickEvents*/
    fun startTimeCounter(expireMinutes: Int) {
        var expireSeconds = expireMinutes * 60
        var expireMilliSeconds = expireSeconds * 1000
        object : CountDownTimer(expireMilliSeconds.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resendCodeAfterExpire.hide()
                mTimeLeftInMillis = millisUntilFinished
                var seconds = (mTimeLeftInMillis / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                var timeText = (String.format("%02d", minutes) + ":" + String.format(
                    "%02d",
                    seconds
                )).toString()

                countdownTimer.text = "${getString(R.string.Seconds2)}: $timeText"
//                val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
//                countdownTimer.text = getString(R.string.Seconds, seconds)

            }

            override fun onFinish() {
                resendCodeAfterExpire.show()
                countdownTimer.text = getString(R.string.CodeExpired)
                button3.isEnabled = false
            }

        }.start()
    }

    private fun setClickListeners() {
        resend_btn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                val userPhone: String? = otpData?.phoneNumber
                signupViewModel.resendOtp(userPhone.toString(), "1",Lingver.getInstance().getLanguage())
                //resendOTPApi()
            }
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
            signupViewModel.verifyOtp(otpData?.phoneNumber.toString(), otpcode.toString())
        }
    }

    fun signup2next() {
        val userIdupdate: String? = intent.getStringExtra("userid")
        val intent2 = Intent(this@SignupOTPVerificationActivity, SignupCreateNewUser::class.java)
        intent2.putExtra(Constants.otpDataKey, otpData)
        startActivity(intent2)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupOTPVerificationActivity, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


//    //////////////////////////////////////Api Post Verify//////////////////////////////////////////////////
//    fun apicallSignup2() {
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val otpcode: String? = pinview.value
//        val call: Call<UserVerifiedResp> =
//            malqa.verifyOtp(otpData?.phoneNumber.toString(), otpcode.toString())
//
//        call.enqueue(object : Callback<UserVerifiedResp> {
//
//            override fun onFailure(call: Call<UserVerifiedResp>, t: Throwable) {
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }
//
//            }
//
//            override fun onResponse(
//                call: Call<UserVerifiedResp>,
//                response: Response<UserVerifiedResp>
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
//                        if (data?.message != null) {
//                            showError(data.message!!)
//                        } else {
//                            showError(getString(R.string.serverError))
//                        }
//                    }
//
//                } else {
//                    HelpFunctions.ShowLongToast(
//                        getString(R.string.VerificationFailed) + " " + response.code(),
//                        this@SignupPg2
//                    )
//                }
//            }
//        })
//    }


//    /**resend OTP*/
//    fun resendOTPApi() {
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val userPhone: String? = otpData?.phoneNumber
//        val call = malqa.resendOtp(userPhone.toString(), Lingver.getInstance().getLanguage())
//        call.enqueue(object : Callback<ValidateAndGenerateOTPResp> {
//
//            override fun onFailure(call: Call<ValidateAndGenerateOTPResp>, t: Throwable) {
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg2) }
//
//            }
//
//            override fun onResponse(
//                call: Call<ValidateAndGenerateOTPResp>,
//                response: Response<ValidateAndGenerateOTPResp>
//            ) {
//                if (response.isSuccessful) {
//                    val otppcode = response.body()?.otpData?.otpCode
//                    if (BuildConfig.DEBUG) {
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
//    }


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