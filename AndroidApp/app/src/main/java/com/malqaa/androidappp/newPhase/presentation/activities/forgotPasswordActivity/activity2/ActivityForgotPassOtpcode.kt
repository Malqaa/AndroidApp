package com.malqaa.androidappp.newPhase.presentation.activities.forgotPasswordActivity.activity2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityForgotPassOtpcodeBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.LoginViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class ActivityForgotPassOtpcode : BaseActivity<ActivityForgotPassOtpcodeBinding>() {

    private var START_TIME_IN_MILLIS: Long = 60000
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var loginViewModel: LoginViewModel? = null
    var email: String = ""
    private var codeOtp: String = ""
    private var expireMinutes: Int = 1
    var expireReceiveMinutes = 1
    private var typeClick = 1
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityForgotPassOtpcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra(ConstantObjects.emailKey) ?: ""
        codeOtp = intent.getStringExtra("codeOtp").toString().split(".0")[0]
        binding.pinview.value = codeOtp
        setupLoginViewModel()
        binding.resendBtn.hide()
        binding.countdownTimer.hide()
        setClickListeners()

        loginViewModel!!.getConfigurationResp(ConstantObjects.configration_otpExpiryTime)
        loginViewModel!!.getConfigurationResp(ConstantObjects.Configuration_DidNotReceiveCodeTime)
    }

    private fun setupLoginViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel!!.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })

        loginViewModel!!.isNetworkFail.observe(this, Observer {
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

        loginViewModel!!.errorResponseObserver.observe(this, Observer {
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

        loginViewModel!!.changePasswordAfterForgetObserver.observe(this, Observer {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(
                    it.message,
                    this
                )
                signInAfterSignUp()
            }

        })

        loginViewModel!!.changePasswordAfterForgetObserver.observe(this, Observer {
            if (it.status_code == 200) {
                startTimeCounter((expireMinutes * 60 * 1000).toLong())
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(
                        it.message,
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

        loginViewModel!!.configurationRespObserver.observe(this) {
            if (it.configurationData != null) {
                binding.countdownTimer.show()
                try {
                    expireMinutes = it.configurationData.configValue.toInt()
                } catch (_: Exception) {
                }
                startTimeCounter((expireMinutes * 60 * 1000).toLong())
            }
        }

        loginViewModel!!.configurationRespDidNotReceive.observe(this) {
            if (it.configurationData != null) {
                try {
                    expireReceiveMinutes = (it.configurationData.configValue).toInt()
                } catch (e: Exception) {
                }
            }
        }

        loginViewModel!!.forgetPasswordObserver.observe(this, Observer {
            if (it.status_code == 200) {
                binding.pinview.value = it.data.toString().split(".0")[0]
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(
                        it.message,
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

    }

    private fun signInAfterSignUp() {
        val intentSignIn = Intent(this, SignInActivity::class.java)
        intentSignIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentSignIn)
    }

    /**clickEvents*/
    private fun startTimeCounter(expireMinutes: Long) {
        countDownTimer = object : CountDownTimer(expireMinutes.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.resendBtn.hide()

                mTimeLeftInMillis = millisUntilFinished
                var seconds = (mTimeLeftInMillis / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                val timeText = (String.format("%02d", minutes) + ":" + String.format(
                    "%02d",
                    seconds
                )).toString()

                val timeReceive = "${String.format("%02d", expireReceiveMinutes)}:00"
                if (timeText == timeReceive) {
                    binding.resendBtn.hide()
                    binding.txtDontReceive.show()
                }
                binding.countdownTimer.text = "${getString(R.string.Seconds2)}: $timeText"
            }

            override fun onFinish() {
                binding.countdownTimer.text = getString(R.string.CodeExpired)
                binding.resendBtn.show()
                binding.countdownTimer.hide()
            }

        }.start()
    }


    private fun setClickListeners() {
        binding.confirmPass!!._addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                validateSignupConfrmPassword()
            }

        })

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.resendBtn.setOnClickListener {
            binding.txtDontReceive.hide()
            binding.resendBtn.hide()
            binding.countdownTimer.show()
            typeClick = 1
            startTimeCounter((expireMinutes * 60 * 1000).toLong())
            loginViewModel!!.forgetPassword(email)
        }

        binding.txtDontReceive.setOnClickListener {
            binding.resendBtn.hide()
            binding.countdownTimer.show()
            typeClick = 2
            loginViewModel!!.forgetPassword(email)
        }
        binding.button3.setOnClickListener {
            var readyToGo = true
            if (!validatePin() or !validateSignupPassword() or !validateSignupConfrmPassword()) {
                readyToGo = false
            } else {
                loginViewModel!!.changePasswordAfterForget(
                    email,
                    binding.pinview.value,
                    binding.textPass.text.toString().trim()
                )
            }
        }
    }

    private fun validateSignupPassword(): Boolean {
        val passwordInput = binding.textPass!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            binding.textPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!HelpFunctions.PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.textPass!!.error = getString(R.string.regexPassword)
            false
        } else {
            binding.textPass!!.error = null
            true
        }
    }

    //confirmpass validation
    private fun validateSignupConfrmPassword(): Boolean {
        val passwordInput = binding.textPass!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = binding.confirmPass!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            binding.confirmPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            binding.confirmPass!!.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            binding.confirmPass!!.error = null
            true
        }
    }

    /***/
    private fun validatePin(): Boolean {
        val inputtt = binding.pinview.value
        return if (inputtt.length != 4) {
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
            val otpCode: String? = binding.pinview.value
            loginViewModel!!.forgetPassword(email)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer = null
        loginViewModel!!.closeAllCall()
        loginViewModel!!.baseCancel()
        loginViewModel = null

    }
}