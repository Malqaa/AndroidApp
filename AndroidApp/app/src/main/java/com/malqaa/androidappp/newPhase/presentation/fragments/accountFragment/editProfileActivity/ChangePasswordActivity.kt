package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.editProfileActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ChangePasswordActivity : BaseActivity() {
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        toolbar_title.text = ""
        setViewClickListeners()
        setUpViewModel()

    }

    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        accountViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        accountViewModel.errorResponseObserver.observe(this) {
            if (it.status != null) {
                if (it.status == "WrongPassword") {
                    tvCurrentPassword.error = getString(R.string.wrongPassword)
                } else {
                    HelpFunctions.ShowLongToast(it.status!!, this)
                }
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }

        accountViewModel.changePasswordObserver.observe(this) { it ->
            if (it.status == "Success") {
                val userData =
                    Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                userData?.password = tvNewPassword.text.toString().trim()
                userData?.let { it ->
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, it)
                }
                ConstantObjects.userobj = userData
                HelpFunctions.ShowLongToast(
                    getString(R.string.emailUpdatedSuccessfully),
                    this
                )
                finish()
            } else if (it.status == "WrongPassword") {
                tvCurrentPassword.error = getString(R.string.wrongPassword)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }

        accountViewModel.changeEmailObserver.observe(this) { changeEmailObserver ->
            if (changeEmailObserver.message == "Success") {
                startActivity(Intent(this, ConfirmChangeEmailActivity::class.java))
            }
        }

    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        btnChangePassword.setOnClickListener {
            if (validateOldPassword() && validateNewPassword()
                && validateConfirmPassword()
            ) {
                accountViewModel.changeUserPassword(
                    ConstantObjects.logged_userid,
                    tvCurrentPassword.text.toString().trim(),
                    tvNewPassword.text.toString().trim()
                )
            }
        }
    }

    private fun validateOldPassword(): Boolean {
        val passwordInput = tvCurrentPassword.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            tvCurrentPassword.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            tvCurrentPassword.error = getString(R.string.regexPassword)
            false
        } else {
            tvCurrentPassword.error = null
            true
        }
    }

    private fun validateNewPassword(): Boolean {
        val passwordInput = tvNewPassword.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            tvNewPassword.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            tvNewPassword.error = getString(R.string.regexPassword)
            false
        } else {
            tvNewPassword.error = null
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val passwordInput = tvNewPassword.text.toString().trim { it <= ' ' }
        val confrmpassInput = tvConfirmNewPassword.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            tvConfirmNewPassword.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            tvConfirmNewPassword.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            tvConfirmNewPassword.error = null
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }
}