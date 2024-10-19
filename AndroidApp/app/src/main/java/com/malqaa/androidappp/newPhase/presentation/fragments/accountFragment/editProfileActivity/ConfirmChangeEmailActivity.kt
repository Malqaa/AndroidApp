package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.editProfileActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityConfirmChangeEmailBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import io.paperdb.Paper

class ConfirmChangeEmailActivity : BaseActivity<ActivityConfirmChangeEmailBinding>() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpCode: String? = null
    private var countDownTimer: CountDownTimer? = null
    private var accountViewModel: AccountViewModel? = null
    var expireMinutes: Int = 1
    var expireReceiveMinutes = 1
    var typeClick = 1
    var newEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityConfirmChangeEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = ""
        countdownTimer = findViewById(R.id.countdownTimer)
        newEmail = intent.getStringExtra(ConstantObjects.emailKey)
        otpCode = intent.getStringExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setClickListeners()
        binding.resendBtn.hide()
        binding.pinViewEmail.value = otpCode!!
        accountViewModel?.getConfigurationResp(ConstantObjects.Configuration_DidNotReceiveCodeTime)
        accountViewModel?.getConfigurationResp(ConstantObjects.configration_otpExpiryTime)
    }

    private fun setupRegisterViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel?.isLoading?.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        accountViewModel?.isNetworkFail?.observe(this, Observer {
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
        accountViewModel?.errorResponseObserver?.observe(this, Observer {
            when (it.message) {
                "OTPExpired" -> {
                    showError(getString(R.string.CodeExpired))
                }

                "EmailExists" -> {
                    showError(getString(R.string.userEmailExists))
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

        accountViewModel?.configurationRespObserver?.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }

                val expireMilliSeconds = expireMinutes * 60 * 1000
                startTimeCounter(expireMilliSeconds.toLong())
            }
        }
        accountViewModel?.configurationRespDidNotReceive?.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireReceiveMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
            }
        }


        accountViewModel?.changeEmailObserver?.observe(this) {
            if (it.status == "Success") {
                binding.pinViewEmail.value = it.data.toString().split(".0")[0]
                if (typeClick == 1)
                    startTimeCounter((expireMinutes * 60 * 1000).toLong())
                binding.button3.isEnabled = true
            }
        }

        accountViewModel?.confirmChangeEmailOtpObserver?.observe(this) {
            if (it.status == "Success") {
                val userData =
                    Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                userData?.email = newEmail
                userData?.let {
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, it)
                }
                ConstantObjects.userobj = userData
                HelpFunctions.ShowLongToast(
                    getString(R.string.emailUpdatedSuccessfully),
                    this
                )
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    /**clickEvents*/
    private fun startTimeCounter(expireMinutes: Long) {
        countDownTimer = object : CountDownTimer(expireMinutes, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.resendBtn.hide()

                mTimeLeftInMillis = millisUntilFinished
                var seconds = (mTimeLeftInMillis / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                val timeText = (String.format("%02d", minutes) + ":" + String.format(
                    "%02d",
                    seconds
                ))

                val timeReceive = "${String.format("%02d", expireReceiveMinutes)}:00"
                if (timeText == timeReceive) {
                    binding.resendBtn.hide()
                    binding.txtReceive.show()
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
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.resendBtn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                binding.txtReceive.hide()
                typeClick = 1
                accountViewModel?.changeUserEmail(newEmail ?: "")
            }
        }

        binding.txtReceive.setOnClickListener {
            typeClick = 2
            accountViewModel?.changeUserEmail(newEmail ?: "")
        }
    }

    /***/
    private fun validatePin(): Boolean {
        val inputtt = binding.pinViewEmail.value
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
            val otpCode: String? = binding.pinViewEmail.value
            accountViewModel?.changeUserEmail(newEmail ?: "", otpCode.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer = null
        accountViewModel?.closeAllCall()
        accountViewModel?.baseCancel()
        accountViewModel = null
    }

}