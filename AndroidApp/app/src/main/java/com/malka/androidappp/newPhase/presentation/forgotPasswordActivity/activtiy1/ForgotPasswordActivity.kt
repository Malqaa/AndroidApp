package com.malka.androidappp.newPhase.presentation.forgotPasswordActivity.activtiy1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.presentation.forgotPasswordActivity.activity2.ActivityForgotPassOtpcode
import com.malka.androidappp.newPhase.presentation.loginScreen.LoginViewModel
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPasswordActivity : BaseActivity() {


    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }
        setupLoginViewModel()
        setClickListeners()

    }

    private fun setClickListeners() {
        language_toggle.setOnToggleSwitchChangeListener { _, _ ->
            setLocate()
        }
        signin.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupLoginViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        loginViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }

        loginViewModel.isNetworkFail.observe(this) {
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

        }

        loginViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.errorEmail),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }

        loginViewModel.forgetPasswordObserver.observe(this) {
            if (it.status_code == 200) {
                startActivity(
                    Intent(
                        this@ForgotPasswordActivity,
                        ActivityForgotPassOtpcode::class.java
                    ).apply {
                        putExtra(ConstantObjects.emailKey, tvEmail.text.toString().trim())
                        putExtra("codeOtp", it.data.toString())
                    }
                )
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

        }
    }


    //Data Validation
    private fun validateForgot(): Boolean {
        val inputEmail = tvEmail.text.toString().trim()

        return if (inputEmail.isEmpty()) {
            tvEmail.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            tvEmail.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            tvEmail.error = null
            true
        }
    }

    fun forgotPasswordFrm(v: View) {
        if (!validateForgot()) {
            return
        } else {
            loginViewModel.forgetPassword(tvEmail.text.toString().trim())

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.closeAllCall()
    }

}
