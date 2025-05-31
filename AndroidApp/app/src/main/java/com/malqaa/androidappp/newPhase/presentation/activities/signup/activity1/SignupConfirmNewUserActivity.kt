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
import com.malqaa.androidappp.databinding.ActivitySignupPg1Binding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.activities.signup.activity2.SignupOTPVerificationActivity
import com.malqaa.androidappp.newPhase.presentation.activities.signup.signupViewModel.SignupViewModel
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper

class SignupConfirmNewUserActivity : BaseActivity<ActivitySignupPg1Binding>(),
    CountryDialog.GetSelectedCountry,
    TermsDialog.AcceptTermsListener {

    var isPhoneNumberValid: Boolean = false
    var isBusinessAccount = false
    lateinit var countryDialog: CountryDialog
    private var signupViewModel: SignupViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySignupPg1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        countryDialog = CountryDialog(this, this)
        if (ConstantObjects.currentLanguage == ConstantObjects.ENGLISH) {
            binding.languageToggle.checkedTogglePosition = 0
        } else {
            binding.languageToggle.checkedTogglePosition = 1
        }
        setupRegisterViewModel()
        setClickListeners()
        setupCountryCodePiker()

        binding.confirmPass!!._addTextChangedListener(object : TextWatcher {
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
                        binding.userNamee.error = getString(R.string.userNameExists)
                    }

                    "PhoneNumberExists" -> {
                        binding.textEmaill.error = getString(R.string.userPhoneExists)
                    }

                    "EmailExists", "EmailExisting" -> {
                        binding.textEmaill.error = getString(R.string.userEmailExists)
                    }

                    "OTP generetd successfully" -> {
                        validateUserAndGenerateOTP.otpData!!.userName =
                            binding.userNamee.text.toString().trim()
                        validateUserAndGenerateOTP.otpData!!.userPass =
                            binding.textPass.text.toString().trim()
                        validateUserAndGenerateOTP.otpData!!.userEmail =
                            binding.textEmaill.text.toString().trim()
                        validateUserAndGenerateOTP.otpData!!.isBusinessAccount = isBusinessAccount
                        validateUserAndGenerateOTP.otpData!!.invitationCode =
                            binding.invitationCode.text.toString().trim()
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
                            binding.userNamee.error = getString(R.string.userNameExists)
                        }

                        "PhoneNumberExists" -> {
                            binding.etPhoneNumber.error = getString(R.string.userPhoneExists)
                        }

                        "EmailExists", "EmailExisting" -> {
                            binding.textEmaill.error = getString(R.string.userEmailExists)
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
            setLocale()
        })
    }

    /**click events**/
    private fun setClickListeners() {
        binding.languageToggle.setOnToggleSwitchChangeListener { _, _ ->
            if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
                signupViewModel!!.setLanguageChange(
                    if (Lingver.getInstance()
                            .getLanguage() == ConstantObjects.ARABIC
                    ) ConstantObjects.ENGLISH else ConstantObjects.ARABIC
                )
            else
                setLocale()

        }
        binding.btnLogin.setOnClickListener {
            onBackPressed()
        }
        binding.btnOpenCountry.setOnClickListener {
            countryDialog.show()
        }

        binding.switchTermCondition.setOnClickListener {
            TermsDialog(this@SignupConfirmNewUserActivity, this).show()

        }

    }

    private fun setupCountryCodePiker() {
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            binding.countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC)
        } else {
            binding.countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH)
        }
        binding.countryCodePicker.registerCarrierNumberEditText(binding.etPhoneNumber)
        binding.countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
            isPhoneNumberValid = isValidNumber
        }
        binding.countryCodePicker.setOnCountryChangeListener {
            binding.etPhoneNumber.text = Editable.Factory.getInstance().newEditable("")
        }
    }

    fun signUpg1confirmInput(v: View) {
        if (!validateSignupEmail() or !validateSignupPassword()
            or !validateNumber() or !validateSignupConfrmPassword() or
            !validateSignupUser()
        ) {
            return
        } else {
            if (binding.switchTermCondition._getChecked()) {
                signupViewModel!!.validateUserAndGenerateOTP(
                    binding.userNamee.text.toString().trim(),
                    binding.textEmaill.text.toString().trim(),
                    "${binding.etPhoneNumber.text.toString().trim()}",
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
        finish()
    }

    /**Validation**/
    //Email Validation
    private fun validateSignupEmail(): Boolean {
        val emailInput =
            binding.textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.textEmaill!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.textEmaill!!.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            binding.textEmaill!!.error = null
            true
        }
    }

    //PassswordValidation
    private fun validateSignupPassword(): Boolean {
        val passwordInput = binding.textPass!!.text.toString().trim { it <= ' ' }
        return if (passwordInput.isEmpty()) {
            binding.textPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.textPass!!.error = getString(R.string.regexPassword)
            false
        } else {
            binding.textPass!!.error = null
            true
        }
    }

    //phone no validation///
    private fun validateNumber(): Boolean {
        val numberInput =
            binding.etPhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            binding.tvPhoneError.visibility = View.VISIBLE
            binding.tvPhoneError.text = getString(R.string.Fieldcantbeempty)
            false
        } else {
            binding.tvPhoneError.text = null
            binding.tvPhoneError.visibility = View.GONE
            true
        }

    }

    //confirmpass validation
    private fun validateSignupConfrmPassword(): Boolean {
        val passwordInput = binding.textPass!!.text.toString().trim { it <= ' ' }
        val confrmpassInput = binding.confirmPass!!.text.toString().trim { it <= ' ' }
        return if (confrmpassInput.isEmpty()) {
            binding.confirmPass!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (confrmpassInput != passwordInput) {
            binding.confirmPass!!.error = getString(R.string.Passworddonotmatch)
            false
        } else {
            binding.confirmPass!!.error = null
            true
        }
    }

    //User Validation
    private fun validateSignupUser(): Boolean {
        val Input =
            binding.userNamee!!.text.toString().trim { it <= ' ' }
        return if (Input.isEmpty()) {
            binding.userNamee!!.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (Input.length < 2) {
            binding.userNamee!!.error = getString(R.string.Usernamemusthaveatleast2characters)
            false
        } else {
            binding.userNamee!!.error = null
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
        binding.tvCode.text = countryCode
        binding.ivFlag.setImageDrawable(null)
        getPicassoInstance()
            .load(countryFlag)
            .into(binding.ivFlag)
    }

    override fun onAccept() {
        if (ConstantObjects.acceptTerms) {
            binding.switchTermCondition._setChecked(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        signupViewModel?.closeAllCall()
        signupViewModel = null
    }
}
