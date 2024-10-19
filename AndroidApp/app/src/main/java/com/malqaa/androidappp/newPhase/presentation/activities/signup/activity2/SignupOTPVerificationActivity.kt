package com.malqaa.androidappp.newPhase.presentation.activities.signup.activity2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySignupPg2Binding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.signup.activity3.SignupCreateNewUser
import com.malqaa.androidappp.newPhase.presentation.activities.signup.signupViewModel.SignupViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver


class SignupOTPVerificationActivity : BaseActivity<ActivitySignupPg2Binding>() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpData: OtpData? = null
    private var signupViewModel: SignupViewModel? = null
    private var countDownTimer: CountDownTimer? = null
    private var expireMinutes = 1
    var expireReceiveMinutes = 1
    var typeClick = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySignupPg2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        countdownTimer = findViewById(R.id.countdownTimer)
        otpData = intent.getParcelableExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setClickListeners()
        binding.resendBtn.hide()
        /***thisForTest*/
        val datacode: String? = otpData?.otpCode
        binding.pinview.value = datacode!!
        signupViewModel!!.getConfigurationResp(ConstantObjects.configration_otpExpiryTime)
        signupViewModel!!.getConfigurationResp(ConstantObjects.Configuration_DidNotReceiveCodeTime)
    }

    private fun setupRegisterViewModel() {
        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel!!.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        signupViewModel!!.isNetworkFail.observe(this, Observer {
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
        signupViewModel!!.errorResponseObserver.observe(this, Observer {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
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
            }


        })
        signupViewModel!!.validateAndGenerateOTPObserver.observe(this) { validateUserAndGenerateOTP ->
            if (validateUserAndGenerateOTP.otpData != null) {
                if (typeClick == 1)
                    startTimeCounter(expireMinutes)
                binding.button3.isEnabled = true
                /***thisForTest*/
                val otppcode = validateUserAndGenerateOTP.otpData!!.otpCode
                binding.pinview.value = otppcode
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }
        }
        signupViewModel!!.userVerifiedObserver.observe(this) { userVerified ->
            println("hhhh " + userVerified.message)
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

                "Success", "Otp verified successfully" -> {
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
        signupViewModel!!.configurationRespObserver.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
                if (typeClick == 1)
                    startTimeCounter(expireMinutes)
            }
        }
        signupViewModel!!.configurationRespDidNotReceive.observe(this) {
            if (it.configurationData != null) {
                try {
                    expireReceiveMinutes = it.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
            }
        }

    }

    /**clickEvents*/
    private fun startTimeCounter(expireMinutes: Int) {
        val expireSeconds = expireMinutes * 60
        val expireMilliSeconds = expireSeconds * 1000
        countDownTimer = object : CountDownTimer(expireMilliSeconds.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendBtn.hide()
                mTimeLeftInMillis = millisUntilFinished
                var seconds = (mTimeLeftInMillis / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                val timeText =
                    (String.format("%02d", minutes) + ":" + String.format("%02d", seconds))
                val timeReceive = "${String.format("%02d", expireReceiveMinutes)}:00"
                if (timeText == timeReceive) {
                    binding.resendBtn.hide()
                    binding.dontReceive.show()
                }
                countdownTimer.text = "${getString(R.string.Seconds2)}: $timeText"
            }

            override fun onFinish() {
                binding.resendBtn.show()
                countdownTimer.text = getString(R.string.CodeExpired)
                binding.button3.isEnabled = false
            }

        }.start()
    }

    private fun setClickListeners() {
        binding.resendBtn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                typeClick = 1
                val userPhone: String? = otpData?.phoneNumber
                signupViewModel!!.resendOtp(
                    userPhone.toString(),
                    Lingver.getInstance().getLanguage(),
                    "3"
                )
            }
        }

        binding.dontReceive.setOnClickListener {
            binding.resendBtn.hide()
            countdownTimer.show()
            typeClick = 2
            val userPhone: String? = otpData?.phoneNumber
            signupViewModel!!.resendOtp(
                userPhone.toString(),
                Lingver.getInstance().getLanguage(),
                "3"
            )
        }
    }

    /***/
    private fun validatePin(): Boolean {
        val input = binding.pinview.value
        return if (input.length != 4) {
            binding.redmessage.visibility = View.VISIBLE
            binding.redmessage.text = getString(R.string.Fieldcantbeempty)
            false
        } else {
            binding.redmessage.error = null
            binding.redmessage.visibility = View.GONE
            true
        }
    }

    fun signUpConfirmInput(v: View) {
        if (!validatePin()) {
            return
        } else {
            val otpcode: String? = binding.pinview.value
            signupViewModel!!.verifyOtp(otpData?.phoneNumber.toString(), otpcode.toString())
        }
    }

    private fun signup2next() {
        val userIdUpdate: String? = intent.getStringExtra("userid")
        val intent2 = Intent(this@SignupOTPVerificationActivity, SignupCreateNewUser::class.java)
        intent2.putExtra(Constants.otpDataKey, otpData)
        startActivity(intent2)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
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

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer = null
        signupViewModel?.closeAllCall()
        signupViewModel?.baseCancel()
        signupViewModel = null
    }

}