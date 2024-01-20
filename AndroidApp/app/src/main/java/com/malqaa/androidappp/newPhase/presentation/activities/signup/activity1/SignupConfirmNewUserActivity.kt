package com.malqaa.androidappp.newPhase.presentation.activities.signup.activity1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malqaa.androidappp.newPhase.presentation.activities.signup.activity2.SignupOTPVerificationActivity
import com.malqaa.androidappp.newPhase.presentation.activities.signup.signupViewModel.SignupViewModel
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance

import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.activity_signup_pg1.userNamee

class SignupConfirmNewUserActivity : BaseActivity(), CountryDialog.GetSelectedCountry,
    TermsDialog.AcceptTermsListener {

    var isPhoneNumberValid: Boolean = false
    var isBusinessAccount = false
    lateinit var countryDialog: CountryDialog
    private var signupViewModel: SignupViewModel?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg1)
        countryDialog = CountryDialog(this, this)
        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }
        setupRegisterViewModel()
        setClickListeners()
        setupCountryCodePiker()

        confirmPass!!._addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                validateSignupConfrmPassword()
            }

        })

    }

    private fun setupRegisterViewModel() {
        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel?.isLoading?.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        signupViewModel!!.isNetworkFail.observe(this, Observer {
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
        signupViewModel!!.validateAndGenerateOTPObserver.observe(
            this,
            Observer { validateUserAndGenerateOTP ->
                println("hhh " + validateUserAndGenerateOTP.message)
                when (validateUserAndGenerateOTP.message) {
                    "UsernameExists" -> {
                        userNamee.error = getString(R.string.userNameExists)
                    }

                    "PhoneNumberExists" -> {
                        textEmaill.error = getString(R.string.userPhoneExists)
                    }

                    "EmailExists", "EmailExisting" -> {
                        textEmaill.error = getString(R.string.userEmailExists)
                    }

                    "OTP generetd successfully" -> {
                        validateUserAndGenerateOTP.otpData!!.userName =
                            userNamee.text.toString().trim()
                        validateUserAndGenerateOTP.otpData!!.userPass =
                            textPass.text.toString().trim()
                        validateUserAndGenerateOTP.otpData!!.userEmail =
                            textEmaill.text.toString().trim()
                        validateUserAndGenerateOTP.otpData!!.isBusinessAccount = isBusinessAccount
                        validateUserAndGenerateOTP.otpData!!.invitationCode =
                            invitation_code.text.toString().trim()
                        goToOTPVerificationScreen(validateUserAndGenerateOTP.otpData!!)
                    }

                    else -> {
                        if (validateUserAndGenerateOTP.message != null) {
                            HelpFunctions.ShowLongToast(
                                validateUserAndGenerateOTP.message!!,
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
        signupViewModel!!.errorResponseObserver.observe(this, Observer {
            println("hhhh yy" + it.message)
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    when (it.message) {
                        "UsernameExists" -> {
                            userNamee.error = getString(R.string.userNameExists)
                        }

                        "PhoneNumberExists" -> {
                            etPhoneNumber.error = getString(R.string.userPhoneExists)
                        }

                        "EmailExists", "EmailExisting" -> {
                            textEmaill.error = getString(R.string.userEmailExists)
                        }

                        else -> {
                            HelpFunctions.ShowLongToast(
                                it.message!!,
                                this
                            )
                        }
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this
                    )
                }
            }
        })

        signupViewModel?.languageObserver?.observe(this, Observer {
            HelpFunctions.ShowLongToast(it.message, this)
            setLocate()

        })
    }

    /**click events**/
    private fun setClickListeners() {
        language_toggle.setOnToggleSwitchChangeListener { _, _ ->
            if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
                signupViewModel!!.setLanguageChange(
                    if (Lingver.getInstance()
                            .getLanguage() == ConstantObjects.ARABIC
                    ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
                )
            else
                setLocate()

        }
        btnLogin.setOnClickListener {
            onBackPressed()
        }
        btnOpenCountry.setOnClickListener {
            countryDialog.show()
        }

        switch_term_condition.setOnClickListener {
            TermsDialog(this@SignupConfirmNewUserActivity, this).show()

        }

    }

    private fun setupCountryCodePiker() {
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC)
        } else {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH)
        }
        countryCodePicker.registerCarrierNumberEditText(etPhoneNumber)
        countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
            isPhoneNumberValid = isValidNumber
        }
        countryCodePicker.setOnCountryChangeListener {
            etPhoneNumber.text = Editable.Factory.getInstance().newEditable("")
        }
    }

    fun signUpg1confirmInput(v: View) {
        if (!validateSignupEmail() or !validateSignupPassword()
            or !validateNumber() or !validateSignupConfrmPassword() or
            !validateSignupUser()
        ) {
            return
        } else {
            if (switch_term_condition._getChecked()) {
                signupViewModel!!.validateUserAndGenerateOTP(
                    userNamee.text.toString().trim(),
                    textEmaill.text.toString().trim(),
                    "${etPhoneNumber.text.toString().trim()}",
                    Lingver.getInstance().getLanguage()
                )
            } else {
                showError(getString(R.string.Please_select, getString(R.string.term_condition)))
            }

        }

    }

    private fun goToOTPVerificationScreen(otpData: OtpData) {
        val intent = Intent(this, SignupOTPVerificationActivity::class.java)
        intent.putExtra(Constants.otpDataKey, otpData)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /**Validation**/
    //Email Validation
    private fun validateSignupEmail(): Boolean {
        val emailInput =
            textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            textEmaill!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textEmaill!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            textEmaill!!.error = null
            true
        }
    }

    //PassswordValidation
    private fun validateSignupPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            textPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textPass!!.error = getString(R.string.regexPassword)
            false
        } else {
            textPass!!.error = null
            true
        }
    }

    //phone no validation///
    private fun validateNumber(): Boolean {
        val numberInput =
            etPhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            tv_phone_error.visibility = View.VISIBLE
            tv_phone_error.text = getString(R.string.Fieldcantbeempty)
            false
        }
//        else if (!isPhoneNumberValid) {
//            tv_phone_error.visibility = View.VISIBLE
//            tv_phone_error.text = getString(R.string.PleaseenteravalidPhoneNumber)
//            false
//        }
        else {
            tv_phone_error.text = null
            tv_phone_error.visibility = View.GONE
            true
        }

    }

    //confirmpass validation
    private fun validateSignupConfrmPassword(): Boolean {
        val passwordInput = textPass!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = confirmPass!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            confirmPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            confirmPass!!.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            confirmPass!!.error = null
            true
        }
    }

    //User Validation
    private fun validateSignupUser(): Boolean {
        val Input =
            userNamee!!.text.toString().trim { it <= ' ' }
        return if (Input.isEmpty()) {
            userNamee!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (Input.length < 2) {
            userNamee!!.error = getString(R.string.Usernamemusthaveatleast2characters)
            false
        } else {
            userNamee!!.error = null
            true
        }
    }

    /***/
    fun signUpg1prev(view: View) {
        onBackPressed()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSelectedCountry(
        id: Int,
        countryName: String,
        countryFlag: String?,
        countryCode: String?
    ) {
        tvCode.text = countryCode
        ivFlag.setImageDrawable(null)
        getPicassoInstance()
            .load(countryFlag)
            .into(ivFlag)
    }

    override fun onAccept() {
        if (ConstantObjects.acceptTerms) {
            switch_term_condition._setChecked(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        signupViewModel?.closeAllCall()
        signupViewModel=null
    }


}
