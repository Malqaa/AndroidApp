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
import com.malqaa.androidappp.databinding.ActivityConfirmChangeNumberBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import io.paperdb.Paper

class ConfirmChangeNumberActivity : BaseActivity<ActivityConfirmChangeNumberBinding>() {

    private var countDownTimer: CountDownTimer? = null
    private var START_TIME_IN_MILLIS: Long = 60000
    lateinit var countdownTimer: TextView
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var otpData: OtpData? = null
    private var accountViewModel: AccountViewModel? = null
    var expireMinutes: Int = 1
    var expireReceiveMinutes = 1
    var typeClick = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityConfirmChangeNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = ""
        countdownTimer = findViewById(R.id.countdownTimer)
        otpData = intent.getParcelableExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setClickListeners()
        binding.resendBtn.hide()
        /***thisForTest*/
        val dataCode: String? = otpData?.otpCode
        binding.pinview.value = dataCode ?: ""
        accountViewModel!!.getConfigurationResp(ConstantObjects.configration_otpExpiryTime)
        accountViewModel!!.getConfigurationResp(ConstantObjects.Configuration_DidNotReceiveCodeTime)

    }

    private fun setupRegisterViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel!!.isLoading.observe(this, Observer {
            if (it) HelpFunctions.startProgressBar(this)
            else HelpFunctions.dismissProgressBar()
        })
        accountViewModel!!.isNetworkFail.observe(this, Observer {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError), this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError), this
                )
            }

        })
        accountViewModel!!.errorResponseObserver.observe(this, Observer {
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
                }

                else -> {
                    if (it.message != null) {
                        HelpFunctions.ShowLongToast(
                            it.message!!, this
                        )
                    } else {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.serverError), this
                        )
                    }
                }
            }


        })
        accountViewModel!!.validateAndGenerateOTPObserver.observe(this) { validateUserAndGenerateOTP ->
            if (validateUserAndGenerateOTP.otpData != null) {
                if (typeClick == 1) startTimeCounter(expireMinutes)
                binding.button3.isEnabled = true
                /***thisForTest*/
                val otpCode = validateUserAndGenerateOTP.otpData!!.otpCode
                binding.pinview.value = otpCode
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError), this
                )
            }
        }
        accountViewModel!!.configurationRespObserver.observe(this) {
            if (it.configurationData != null) {
                try {
                    expireMinutes = it.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
                if (typeClick == 1) startTimeCounter(expireMinutes)
            }
        }
        accountViewModel!!.configurationRespDidNotReceive.observe(this) {
            if (it.configurationData != null) {
                try {
                    expireReceiveMinutes = it.configurationData.configValue.toInt()
                } catch (e: Exception) {
                }
            }
        }

        accountViewModel!!.updateUserMobielNumberObserver.observe(this) {
            if (it.status == "Success") {
                val userData =
                    Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                userData?.phone = otpData?.phoneNumber
                userData?.let { userData ->
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, userData)
                }
                ConstantObjects.userobj = userData
                HelpFunctions.ShowLongToast(
                    getString(R.string.phoneUpdatedSuccessfully), this
                )

                val intent = Intent()
                setResult(Activity.RESULT_OK, intent);
                finish()

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
                val timeText = (String.format("%02d", minutes) + ":" + String.format(
                    "%02d", seconds
                ))
                val timeReceive = "${String.format("%02d", expireReceiveMinutes)}:00"
                if (timeText == timeReceive) {
                    binding.resendBtn.hide()
                    binding.txtDontReceive.show()
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
            onBackPressed()
        }
        binding.resendBtn.setOnClickListener {
            if (mTimeLeftInMillis >= 1000) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Pleasewaituntilthecodeexpires), applicationContext
                )
            } else {
                typeClick = 1
                binding.txtDontReceive.hide()
                val userPhone: String? = otpData?.phoneNumber
                accountViewModel!!.resendOtp(
                    userPhone.toString(), "IndividualUpdateMoileNumber", "3"
                )
                //resendOTPApi()
            }
        }
        binding.txtDontReceive.setOnClickListener {
            binding.resendBtn.hide()
            countdownTimer.show()
            typeClick = 2
            val userPhone: String? = otpData?.phoneNumber
            accountViewModel!!.resendOtp(
                userPhone.toString(), "IndividualUpdateMoileNumber", "3"
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
            //apicallSignup2()
            val otpCode: String? = binding.pinview.value
            accountViewModel!!.updateMobileNumber(
                otpData?.phoneNumber.toString(), ConstantObjects.logged_userid, otpCode.toString()
            )
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer = null
        accountViewModel?.closeAllCall()
        accountViewModel?.baseCancel()
        accountViewModel = null
    }
}