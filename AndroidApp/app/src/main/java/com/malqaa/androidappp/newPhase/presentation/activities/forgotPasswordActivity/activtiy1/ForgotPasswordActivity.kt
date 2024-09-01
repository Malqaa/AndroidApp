package com.malqaa.androidappp.newPhase.presentation.activities.forgotPasswordActivity.activtiy1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.presentation.activities.forgotPasswordActivity.activity2.ActivityForgotPassOtpcode
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.LoginViewModel
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_sign_in.language_toggle


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
        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
                loginViewModel.setLanguageChange(
                    if (Lingver.getInstance()
                            .getLanguage() == ConstantObjects.ARABIC
                    ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
                )
            else
                setLocale()
        }

        loginViewModel.languageObserver.observe(this, Observer {
            HelpFunctions.ShowLongToast(it.message, this)
            setLocale()
        })

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
