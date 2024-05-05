package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.editProfileActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.enums.ShowUserInfo
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper.readJson
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.btnDate
import kotlinx.android.synthetic.main.activity_edit_profile.etPhoneNumber
import kotlinx.android.synthetic.main.activity_edit_profile.lastName
import kotlinx.android.synthetic.main.activity_edit_profile.radiofemale
import kotlinx.android.synthetic.main.activity_edit_profile.radiomale
import kotlinx.android.synthetic.main.activity_edit_profile.userNamee
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.toolbar_main.*


class EditProfileActivity : AppCompatActivity() {
    private var gender: Int = 0
    var phoneCode = ""
    private lateinit var accountViewModel: AccountViewModel

    private var showMYInfo: Int = 0
    private val chooseLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                ConstantObjects.userobj?.let { setUserData(it) }
            }
        }
    private val changePhoneLauncher =
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
        userNamee.isClickable=false
        etPasswordLayout.isEnabled = false
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


        val adapter = CustomSpinnerAdapter(this, readJson(this)!!)
        spCode.adapter = adapter
        spCode.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                spinnerPosition: Int,
                l: Long
            ) {
                phoneCode =   readJson(this@EditProfileActivity)?.get(spinnerPosition)?.dialCode?:""
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

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
                    val userData =
                        Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
                    val tempUserData = getUserDataObserver.userObject
                    tempUserData.token = userData?.token ?: ""
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, tempUserData)
                    tempUserData.let {
                        ConstantObjects.userobj = it
                    }
                    setUserData(tempUserData)
                }
            }
        }
        accountViewModel.changeEmailObserver.observe(this) {
            if (it.status == "Success") {

                chooseLocationLauncher.launch(
                    Intent(
                        this,
                        ConfirmChangeEmailActivity::class.java
                    ).apply {
                        putExtra(ConstantObjects.emailKey, textEmail.text.toString().trim())
                        putExtra(Constants.otpDataKey, it.data.toString().split(".0")[0])
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
                val userObjects = ConstantObjects.userobj
                userObjects?.userName = fristName.text.toString().trim()
                userObjects?.lastName = lastName.text.toString().trim()
                userObjects?.dateOfBirth = btnDate.text.toString().trim()
                userObjects?.gender = gender

                userObjects?.showUserInformation =  it.profileData.showUserInformation
                userObjects?.let {
                    ConstantObjects.userobj = userObjects
                    Paper.book()
                        .write<LoginUser>(SharedPreferencesStaticClass.user_object, userObjects)
                }
                HelpFunctions.ShowLongToast(getString(R.string.profileUpdatedSuccessfully), this)

//                if (userObjects?.showUserInformation.toString().lowercase() == ShowUserInfo.EveryOne.name.lowercase())
//                    SharedPreferencesStaticClass.saveShowUserInformation(1)
//                else if(userObjects?.showUserInformation.toString().lowercase() == ShowUserInfo.MembersOnly.name.lowercase()){
//                    SharedPreferencesStaticClass.saveShowUserInformation(2)
//                }else{
//                    SharedPreferencesStaticClass.saveShowUserInformation(3)
//                }
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
        changePhoneLauncher.launch(intent)
    }

    private fun setUserData(tempUserData: LoginUser) {
        password.setText(tempUserData.password ?: "")
        etPhoneNumber.hint = tempUserData.phone ?: ""
        userNamee.text = tempUserData.userName ?: ""
        textEmail.hint = tempUserData.email ?: ""
        fristName.setText(tempUserData.firstName ?: "")
        lastName.setText(tempUserData.lastName ?: "")
        btnDate.setText(tempUserData.dateOfBirth ?: "")
        gender = tempUserData.gender
        when (gender) {
            Constants.male -> {
                radiomale._setCheck(true)
            }

            Constants.female -> {
                radiofemale._setCheck(true)
            }
        }
        setInfoUser((tempUserData.showUserInformation ?: "0").toInt())
        showMYInfo = (tempUserData.showUserInformation ?: "0").toInt()
    }

    private fun setInfoUser(showMYInfo: Int) {
        when (showMYInfo) {
            1 -> {
                ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_checked)
                ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_unchecked)
                ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_unchecked)
            }

            2 -> {
                ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_unchecked)
                ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_checked)
                ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_unchecked)
            }

            3 -> {
                ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_unchecked)
                ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_unchecked)
                ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_checked)
            }
        }
    }

    private fun setViewClickListeners() {
        ivShowMyInfoToAll.setOnClickListener {
            showMYInfo = 1
            setInfoUser(1)
        }
        ivShowMyInfoToMembers.setOnClickListener {
            showMYInfo = 2
            setInfoUser(2)
        }
        ivShowMyInfoToNoOne.setOnClickListener {
            showMYInfo = 3
            setInfoUser(3)
        }


        btnDate.setOnClickListener {
            DatePickerFragment(true, false) { selectDate ->
                btnDate.text = "$selectDate "

            }.show(supportFragmentManager, "")
        }
        back_btn.setOnClickListener {
            onBackPressed()
        }
        radiomale._setOnClickListener {
            radiomale._setCheck(!radiomale.getCheck())
            radiofemale._setCheck(false)
            if (radiomale.getCheck()) {
                gender = Constants.male
            } else {
                gender = -1
            }
        }
        radiofemale._setOnClickListener {
            radiofemale._setCheck(!radiofemale.getCheck())
            if (radiofemale.getCheck()) {
                gender = Constants.female
            } else {
                gender = -1
            }
            radiomale._setCheck(false)

        }
        reset_password_btn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        activate_email.setOnClickListener {
            if (textEmail.text.toString().trim() == "") {
                textEmail.error = getString(R.string.enterNewEmail)
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
                    phoneCode + etPhoneNumber.text.toString().trim(),
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
        var readyToSave = false
        if (fristName.text.toString().trim() == "") {
            readyToSave = false
            fristName.error = getString(R.string.Please_enter, getString(R.string.First_Name))
        } else if (lastName.text.toString().trim() == "") {
            readyToSave = false
            lastName.error = getString(R.string.Please_enter, getString(R.string.Last_Name))
        } else if (btnDate.text.toString().trim() == "") {
            readyToSave = false
            btnDate.error = getString(R.string.Please_select, getString(R.string.Date_of_Birth))
        } else if (gender == -1) {
            HelpFunctions.ShowLongToast(getString(R.string.select_gender), this)
            readyToSave = false
        } else if (showMYInfo == 0) {
            readyToSave = false
            HelpFunctions.ShowLongToast(getString(R.string.statusShowingYourData), this)
        } else {
            readyToSave = true
            if (readyToSave) {
                accountViewModel.updateMobileNumber(
                    ConstantObjects.logged_userid,
                    fristName.text.toString().trim(),
                    lastName.text.toString().trim(),
                    btnDate.text.toString().trim(),
                    gender,
                    showMYInfo.toString()
                )
            }
        }
    }

    private fun validateNumber(): Boolean {
        val numberInput =
            etPhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            etPhoneNumber.visibility = View.VISIBLE
            etPhoneNumber.error = getString(R.string.enterNewPhone)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else if (phoneCode.isEmpty()) {
            etPhoneNumber.error = getString(R.string.selectCountry)
            false
        } else {
            etPhoneNumber.error = null
            true
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }

}