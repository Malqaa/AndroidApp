package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.editProfileActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityChangePasswordBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import io.paperdb.Paper

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = ""
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
                    binding.tvCurrentPassword.error = getString(R.string.wrongPassword)
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
                userData?.password = binding.tvNewPassword.text.toString().trim()
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
                binding.tvCurrentPassword.error = getString(R.string.wrongPassword)
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
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.btnChangePassword.setOnClickListener {
            if (validateOldPassword() && validateNewPassword()
                && validateConfirmPassword()
            ) {
                accountViewModel.changeUserPassword(
                    ConstantObjects.logged_userid,
                    binding.tvCurrentPassword.text.toString().trim(),
                    binding.tvNewPassword.text.toString().trim()
                )
            }
        }
    }

    private fun validateOldPassword(): Boolean {
        val passwordInput = binding.tvCurrentPassword.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            binding.tvCurrentPassword.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.tvCurrentPassword.error = getString(R.string.regexPassword)
            false
        } else {
            binding.tvCurrentPassword.error = null
            true
        }
    }

    private fun validateNewPassword(): Boolean {
        val passwordInput = binding.tvNewPassword.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            binding.tvNewPassword.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.tvNewPassword.error = getString(R.string.regexPassword)
            false
        } else {
            binding.tvNewPassword.error = null
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val passwordInput = binding.tvNewPassword.text.toString().trim { it <= ' ' }
        val confrmpassInput = binding.tvConfirmNewPassword.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            binding.tvConfirmNewPassword.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            binding.tvConfirmNewPassword.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            binding.tvConfirmNewPassword.error = null
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }
}