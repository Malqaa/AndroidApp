package com.malqaa.androidappp.newPhase.presentation.activities.loginScreen

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.presentation.activities.forgotPasswordActivity.activtiy1.ForgotPasswordActivity
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.presentation.activities.signup.activity1.SignupConfirmNewUserActivity
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : BaseActivity() {


    private var loginViewModel: LoginViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        setupLoginViewModel()
        setClickListeners()


        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }

        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
                loginViewModel?.setLanguageChange(
                    if (Lingver.getInstance()
                            .getLanguage() == ConstantObjects.ARABIC
                    ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
                )
            else
                setLocate()
        }

        loginViewModel?.languageObserver?.observe(this, Observer {
            HelpFunctions.ShowLongToast(it.message,this)
            setLocate()

        })

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
        loginViewModel!!.userLoginObserver.observe(this, Observer { userLoginResp ->
            if (userLoginResp.userObject != null) {
                saveUserData(userLoginResp.userObject)
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }
        })
        loginViewModel!!.errorResponseObserver.observe(this, Observer {
            if(it.status!=null && it.status=="409"){
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            }else {
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
        })
    }
    /**event clicks*/
    private fun setClickListeners() {
        Forgot_your_password.setOnClickListener {
            startActivity(Intent(this@SignInActivity, ForgotPasswordActivity::class.java))
        }
        new_registration.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignupConfirmNewUserActivity::class.java)
            startActivity(intent)
        }

    }
    fun confirmInput(v: View) {
        if (!validateEmail() or !validatePassword()) {
            return
        } else {
            loginViewModel!!.signInUser(
                email_tv.text.toString().trim(),
                passwword_tv.text.toString().trim(),
                SharedPreferencesStaticClass.getFcmToken())
        }
    }

    private fun saveUserData(userObject: LoginUser) {
        ConstantObjects.logged_userid = userObject.id
       // ConstantObjects.businessAccountUser = userObject.businessAccounts
        val userId: String = userObject.id
        ConstantObjects.logged_userid = userId
        HelpFunctions.ShowLongToast(getString(R.string.LoginSuccessfully), this)
        Paper.book().write(SharedPreferencesStaticClass.islogin, true)
        Paper.book().write<LoginUser>(SharedPreferencesStaticClass.user_object, userObject)
        setResult(RESULT_OK, Intent())
        finish()
    }

    /**validation */
    private fun validateEmail(): Boolean {
        val emailInput = email_tv!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            email_tv!!.error = getString(R.string.Emailisrequired)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email_tv!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            email_tv!!.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput = passwword_tv!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            passwword_tv!!.error = getString(R.string.Passwordisrequired)
            false
        } else {
            passwword_tv!!.error = null
            true
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel?.closeAllCall()
        loginViewModel=null
    }

}
