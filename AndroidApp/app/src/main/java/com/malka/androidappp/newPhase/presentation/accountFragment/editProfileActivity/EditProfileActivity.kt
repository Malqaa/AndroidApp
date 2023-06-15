package com.malka.androidappp.newPhase.presentation.accountFragment.editProfileActivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.btnDate
import kotlinx.android.synthetic.main.activity_edit_profile.etPhoneNumber
import kotlinx.android.synthetic.main.activity_edit_profile.lastName
import kotlinx.android.synthetic.main.activity_edit_profile.radiofemale
import kotlinx.android.synthetic.main.activity_edit_profile.radiomale
import kotlinx.android.synthetic.main.activity_edit_profile.userNamee
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.activity_signup_pg4.*
import kotlinx.android.synthetic.main.toolbar_main.*

class EditProfileActivity : AppCompatActivity() {
    var gender_: Int = 0
    private lateinit var accountViewModel: AccountViewModel

    private var showMYInfo: Int = 0
    val chooseLocationLuncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ConstantObjects.userobj?.let { setUserData(it) }
            }
        }
    val changePhoneLuncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ConstantObjects.userobj?.let { setUserData(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        toolbar_title.text = getString(R.string.edit_profile)
        password.isEnabled = false
        etPasswordLayout.isEnabled = false
        userNamee.isEnabled = false
        //etPhoneNumber.isEnabled = false

        if (ConstantObjects.currentLanguage == "ar") {
            btn_reset_phone_number.setBackgroundResource(R.drawable.background_corener_from_start)
            activate_email.setBackgroundResource(R.drawable.background_corener_from_start)
            reset_password_btn.setBackgroundResource(R.drawable.background_corener_from_start)
        } else {
            btn_reset_phone_number.setBackgroundResource(R.drawable.background_corener_from_end)
            activate_email.setBackgroundResource(R.drawable.background_corener_from_end)
            reset_password_btn.setBackgroundResource(R.drawable.background_corener_from_end)
        }
        setViewClickListeners()
        setUpViewModel()
        accountViewModel.getUserData()
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
                when (it.status) {
                    "EmailExists", "EmailExisting" -> {
                        textEmaill.error = getString(R.string.userEmailExists)
                    }
                    else -> {
                        HelpFunctions.ShowLongToast(it.status!!, this)
                    }
                }

            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
        accountViewModel.getUserDataObserver.observe(this) { getUserDataObserver ->
            if (getUserDataObserver.status == "Success") {
                if (getUserDataObserver.userObject != null) {
                    var userData =
                        Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                    var tempUserData = getUserDataObserver.userObject
                    tempUserData.token = userData?.token ?: ""
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, tempUserData)
                    tempUserData?.let {
                        ConstantObjects.userobj = it
                    }
                    setUserData(tempUserData)
                }
            }
        }
        accountViewModel.changeEmailObserver.observe(this) {
            if (it.status == "Success") {

                chooseLocationLuncher.launch(
                    Intent(
                        this,
                        ConfirmChangeEmailActivity::class.java
                    ).apply {
                        putExtra(ConstantObjects.emailKey, textEmail.text.toString().trim())
                    })
            }
        }
        accountViewModel.validateAndGenerateOTPObserver.observe(this) { validateUserAndGenerateOTP ->
            if (validateUserAndGenerateOTP.otpData != null) {
                goToOTPVerificationScreen(validateUserAndGenerateOTP.otpData!!)
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }
        }
        accountViewModel.updateProfileDataObserver.observe(this) {
            if (it.status == "Success") {
                var userObjects = ConstantObjects.userobj
                userObjects?.userName = fristName.text.toString().trim()
                userObjects?.lastName = lastName.text.toString().trim()
                userObjects?.dateOfBirth = btnDate.text.toString().trim()
                userObjects?.gender = gender_
                userObjects?.showUserInformation = "EveryOne"
                userObjects?.let {
                    ConstantObjects.userobj = userObjects
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, userObjects)
                }
                HelpFunctions.ShowLongToast(getString(R.string.profileUpdatedSuccessfully), this)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
    }

    private fun goToOTPVerificationScreen(otpData: OtpData) {
        val intent = Intent(this, ConfirmChangeNumberActivity::class.java).apply {
            putExtra(Constants.otpDataKey, otpData)
        }
        changePhoneLuncher.launch(intent)
//        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setUserData(tempUserData: LoginUser) {
        password.setText(tempUserData.password ?: "")
        etPhoneNumber.setHint(tempUserData.phone ?: "")
        userNamee.setText(tempUserData.userName ?: "")
        textEmail.setHint(tempUserData.email ?: "")
        fristName.setText(tempUserData.firstName ?: "")
        lastName.setText(tempUserData.lastName ?: "")
        btnDate.setText(tempUserData.dateOfBirth ?: "")
        gender_ = tempUserData.gender
        when (gender_) {
            Constants.male -> {
                radiomale._setCheck(true)
            }
            Constants.female -> {
                radiofemale._setCheck(true)
            }
        }
    }

    private fun setViewClickListeners() {
        ivShowMyInfoToAll.setOnClickListener {
            showMYInfo = 1
            ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_checked)
            ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_unchecked)
            ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_unchecked)
        }
        ivShowMyInfoToMembers.setOnClickListener {
            showMYInfo = 2
            ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_unchecked)
            ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_checked)
            ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_unchecked)
        }
        ivShowMyInfoToNoOne.setOnClickListener {
            showMYInfo = 3
            ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_unchecked)
            ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_unchecked)
            ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_checked)
        }


        btnDate.setOnClickListener {
            DatePickerFragment(true, false) { selectdate_ ->
                btnDate.text = "$selectdate_ "

            }.show(supportFragmentManager, "")
        }
        back_btn.setOnClickListener {
            onBackPressed()
        }
        radiomale._setOnClickListener() {
            radiomale._setCheck(!radiomale.getCheck())
            radiofemale._setCheck(false)
            gender_ = Constants.male
        }
        radiofemale._setOnClickListener {
            radiofemale._setCheck(!radiofemale.getCheck())
            radiomale._setCheck(false)
            gender_ = Constants.female

        }
        reset_password_btn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        activate_email.setOnClickListener {
            if (textEmail.text.toString().trim() == "") {
                textEmail.error = getString(R.string.Fieldcantbeempty)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail.text).matches()) {
                textEmail.error = getString(R.string.Pleaseenteravalidemailaddress)
            } else if (textEmail.text.toString().trim() == ConstantObjects.userobj?.email) {
                textEmail.error = getString(R.string.updateYourEmail)
            } else {
                accountViewModel.changeUserEmail(textEmail.text.toString().trim())
            }
        }
        btn_reset_phone_number.setOnClickListener {
            if (validateNumber()) {
                accountViewModel.resendOtp(
                    etPhoneNumber.text.toString().trim(),
                    ConstantObjects.currentLanguage,
                    "3"
                )
            }
        }
        button.setOnClickListener {
            prepareDataToUpdate()
        }
    }

    private fun prepareDataToUpdate() {
        var readyToSave = true
        if (fristName.text.toString().trim() == "") {
            readyToSave = false
            fristName.error = getString(R.string.Please_enter, getString(R.string.First_Name))
        }
        if (lastName.text.toString().trim() == "") {
            readyToSave = false
            lastName.error = getString(R.string.Please_enter, getString(R.string.Last_Name))
        }
        if (btnDate.text.toString().trim() == "") {
            readyToSave = false
            btnDate.error = getString(R.string.Please_select, getString(R.string.Date_of_Birth))
        }
        if(showMYInfo==0){
            readyToSave = false
            HelpFunctions.ShowLongToast(getString(R.string.statusShowingYourData),this)
        }
        if (readyToSave) {
            accountViewModel.updateMobileNumber(
                ConstantObjects.logged_userid,
                fristName.text.toString().trim(),
                lastName.text.toString().trim(),
                btnDate.text.toString().trim(),
                gender_,
                showMYInfo.toString()
            )
        }
    }

    private fun validateNumber(): Boolean {
        val numberInput =
            etPhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            etPhoneNumber.visibility = View.VISIBLE
            etPhoneNumber.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else {
            etPhoneNumber.error = null
            true
        }

    }
}