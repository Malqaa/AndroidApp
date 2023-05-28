package com.malka.androidappp.newPhase.presentation.accountFragment.editProfileActivity

import android.app.Activity
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
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_confirm_change_email.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ConfirmChangeEmailActivity : BaseActivity() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpData: OtpData? = null
    private lateinit var accountViewModel: AccountViewModel
    var expireMinutes: Int = 1
    var newEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_change_email)
        toolbar_title.text = ""
        countdownTimer = findViewById(R.id.countdownTimer)
        newEmail = intent.getStringExtra(ConstantObjects.emailKey)
        setupRegisterViewModel()
        setClickListeners()
        resendCodeAfterExpire.hide()

        accountViewModel.getConfigurationResp(ConstantObjects.otpExpiryTime)

    }

    private fun setupRegisterViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        accountViewModel.isNetworkFail.observe(this, Observer {
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
        accountViewModel.errorResponseObserver.observe(this, Observer {
            when (it.message) {
                "OTPExpired" -> {
                    showError(getString(R.string.CodeExpired))
                }
                "EmailExists"->{
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

        accountViewModel.configurationRespObserver.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
                startTimeCounter(expireMinutes)
            }
        }
        accountViewModel.changeEmailObserver.observe(this) {
            if (it.status == "Success") {
                startTimeCounter(expireMinutes)
            }
        }
        accountViewModel.confirmChangeEmailOtpObserver.observe(this) {
            if (it.status == "Success") {
                var userData =
                    Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                userData?.email = newEmail
                userData?.let { userData ->
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, userData)
                }
                ConstantObjects.userobj = userData
                HelpFunctions.ShowLongToast(
                    getString(R.string.passwordUpdatedSuccessfully),
                    this
                )
                var intent = Intent()
                setResult(Activity.RESULT_OK, intent);
                finish()
                finish()
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
                accountViewModel.changeUserEmail(newEmail ?: "")
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
            accountViewModel.changeUserEmail(newEmail ?: "", otpcode.toString())
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

}