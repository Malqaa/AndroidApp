package com.malka.androidappp.newPhase.presentation.accountFragment.editProfileActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_confirm_change_email.button3
import kotlinx.android.synthetic.main.activity_confirm_change_email.pinViewEmail
import kotlinx.android.synthetic.main.activity_confirm_change_email.redmessage
import kotlinx.android.synthetic.main.activity_confirm_change_email.resend_btn
import kotlinx.android.synthetic.main.activity_confirm_change_email.txtReceive
import kotlinx.android.synthetic.main.toolbar_main.back_btn
import kotlinx.android.synthetic.main.toolbar_main.toolbar_title
import java.text.ParseException
import java.text.SimpleDateFormat

class ConfirmChangeEmailActivity : BaseActivity() {

    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpData: OtpData? = null
    private var otpCode: String? = null
    private lateinit var accountViewModel: AccountViewModel
    var expireMinutes: Int = 1
    var expireReceiveMinutes = 1
    var typeClick = 1
    var newEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_change_email)
        toolbar_title.text = ""
        countdownTimer = findViewById(R.id.countdownTimer)
        newEmail = intent.getStringExtra(ConstantObjects.emailKey)
        otpCode = intent.getStringExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setClickListeners()
        resend_btn.hide()
        pinViewEmail.value = otpCode!!
        accountViewModel.getConfigurationResp(ConstantObjects.Configuration_DidNotReceiveCodeTime)
        accountViewModel.getConfigurationResp(ConstantObjects.configration_otpExpiryTime)

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

        accountViewModel.configurationRespObserver.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }

                var expireMilliSeconds = expireMinutes * 60 * 1000
                startTimeCounter(expireMilliSeconds.toLong())
            }
        }
        accountViewModel.configurationRespDidNotReceive.observe(this) { configratinoResp ->
            if (configratinoResp.configurationData != null) {
                try {
                    expireReceiveMinutes = configratinoResp.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
            }
        }


        accountViewModel.changeEmailObserver.observe(this) {
            if (it.status == "Success") {
                pinViewEmail.value = it.data.toString().split(".0")[0]
                if (typeClick == 1)
                    startTimeCounter((expireMinutes * 60 * 1000).toLong())
                button3.isEnabled = true
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
                    getString(R.string.emailUpdatedSuccessfully),
                    this
                )
                var intent = Intent()
                setResult(Activity.RESULT_OK, intent);
                finish()
            }
        }
    }

    /**clickEvents*/
    fun startTimeCounter(expireMinutes: Long) {
        object : CountDownTimer(expireMinutes.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                resend_btn.hide()

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
                    resend_btn.hide()
                    txtReceive.show()
                }
                countdownTimer.text = "${getString(R.string.Seconds2)}: $timeText"

            }

            override fun onFinish() {
                resend_btn.show()
                countdownTimer.text = getString(R.string.CodeExpired)
                button3.isEnabled = false
            }

        }.start()
    }


    private fun convertTimeToLong(timeString: String): Long {
        return try {
            val sdf = SimpleDateFormat("HH:mm:ss")
            val date = sdf.parse(timeString)


            date.time
        } catch (e: ParseException) {
            e.printStackTrace()
            -1 // Handle the exception according to your requirements
        }
    }

    private fun setClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        resend_btn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires),
                    applicationContext
                )
            } else {
                txtReceive.hide()
                typeClick=1
                accountViewModel.changeUserEmail(newEmail ?: "")
            }
        }

        txtReceive.setOnClickListener {
            typeClick=2
            accountViewModel.changeUserEmail(newEmail ?: "")
        }

    }

    /***/
    private fun validatePin(): Boolean {
        val inputtt = pinViewEmail.value
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
            val otpcode: String? = pinViewEmail.value
            accountViewModel.changeUserEmail(newEmail ?: "", otpcode.toString())
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

}