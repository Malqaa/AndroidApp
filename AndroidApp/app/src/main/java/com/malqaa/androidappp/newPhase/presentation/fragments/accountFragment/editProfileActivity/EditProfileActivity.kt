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
import com.malqaa.androidappp.databinding.ActivityEditProfileBinding
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.ConstantsHelper.readJson
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import io.paperdb.Paper

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProfileBinding

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

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.edit_profile)
        binding.password.isEnabled = false
        binding.userNamee.isClickable = false
        binding.etPasswordLayout.isEnabled = false

        if (ConstantObjects.currentLanguage == "ar") {
            binding.btnResetPhoneNumber.setBackgroundResource(R.drawable.background_corener_from_start)
            binding.activateEmail.setBackgroundResource(R.drawable.background_corener_from_start)
            binding.resetPasswordBtn.setBackgroundResource(R.drawable.background_corener_from_start)
        } else {
            binding.btnResetPhoneNumber.setBackgroundResource(R.drawable.background_corener_from_end)
            binding.activateEmail.setBackgroundResource(R.drawable.background_corener_from_end)
            binding.resetPasswordBtn.setBackgroundResource(R.drawable.background_corener_from_end)
        }
        setViewClickListeners()
        setUpViewModel()


        val adapter = CustomSpinnerAdapter(this, readJson(this)!!)
        binding.spCode.adapter = adapter
        binding.spCode.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                spinnerPosition: Int,
                l: Long
            ) {
                phoneCode = readJson(this@EditProfileActivity)?.get(spinnerPosition)?.dialCode ?: ""
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
                        binding.textEmail.error = getString(R.string.userEmailExists)
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
                        putExtra(ConstantObjects.emailKey, binding.textEmail.text.toString().trim())
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
                userObjects?.userName = binding.fristName.text.toString().trim()
                userObjects?.lastName = binding.lastName.text.toString().trim()
                userObjects?.dateOfBirth = binding.btnDate.text.toString().trim()
                userObjects?.gender = gender

                userObjects?.showUserInformation = it.profileData.showUserInformation
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
        changePhoneLauncher.launch(intent)
    }

    private fun setUserData(tempUserData: LoginUser) {
        binding.password.setText(tempUserData.password ?: "")
        binding.etPhoneNumber.hint = tempUserData.phone ?: ""
        binding.userNamee.text = tempUserData.userName ?: ""
        binding.textEmail.hint = tempUserData.email ?: ""
        binding.fristName.setText(tempUserData.firstName ?: "")
        binding.lastName.setText(tempUserData.lastName ?: "")
        binding.btnDate.setText(tempUserData.dateOfBirth ?: "")
        gender = tempUserData.gender
        when (gender) {
            Constants.male -> {
                binding.radiomale._setCheck(true)
            }

            Constants.female -> {
                binding.radiofemale._setCheck(true)
            }
        }
        setInfoUser((tempUserData.showUserInformation ?: "0").toInt())
        showMYInfo = (tempUserData.showUserInformation ?: "0").toInt()
    }

    private fun setInfoUser(showMYInfo: Int) {
        when (showMYInfo) {
            1 -> {
                binding.ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_checked)
                binding.ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_unchecked)
                binding.ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_unchecked)
            }

            2 -> {
                binding.ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_unchecked)
                binding.ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_checked)
                binding.ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_unchecked)
            }

            3 -> {
                binding.ivShowMyInfoToAll.setImageResource(R.drawable.ic_radio_button_unchecked)
                binding.ivShowMyInfoToMembers.setImageResource(R.drawable.ic_radio_button_unchecked)
                binding.ivShowMyInfoToNoOne.setImageResource(R.drawable.ic_radio_button_checked)
            }
        }
    }

    private fun setViewClickListeners() {
        binding.ivShowMyInfoToAll.setOnClickListener {
            showMYInfo = 1
            setInfoUser(1)
        }
        binding.ivShowMyInfoToMembers.setOnClickListener {
            showMYInfo = 2
            setInfoUser(2)
        }
        binding.ivShowMyInfoToNoOne.setOnClickListener {
            showMYInfo = 3
            setInfoUser(3)
        }


        binding.btnDate.setOnClickListener {
            DatePickerFragment(true, false) { selectDate ->
                binding.btnDate.text = "$selectDate "

            }.show(supportFragmentManager, "")
        }
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.radiomale._setOnClickListener {
            binding.radiomale._setCheck(!binding.radiomale.getCheck())
            binding.radiofemale._setCheck(false)
            if (binding.radiomale.getCheck()) {
                gender = Constants.male
            } else {
                gender = -1
            }
        }
        binding.radiofemale._setOnClickListener {
            binding.radiofemale._setCheck(!binding.radiofemale.getCheck())
            if (binding.radiofemale.getCheck()) {
                gender = Constants.female
            } else {
                gender = -1
            }
            binding.radiomale._setCheck(false)

        }
        binding.resetPasswordBtn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        binding.activateEmail.setOnClickListener {
            if (binding.textEmail.text.toString().trim() == "") {
                binding.textEmail.error = getString(R.string.enterNewEmail)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.textEmail.text).matches()) {
                binding.textEmail.error = getString(R.string.Pleaseenteravalidemailaddress)
            } else if (binding.textEmail.text.toString().trim() == ConstantObjects.userobj?.email) {
                binding.textEmail.error = getString(R.string.updateYourEmail)
            } else {
                accountViewModel.changeUserEmail(binding.textEmail.text.toString().trim())
            }
        }
        binding.btnResetPhoneNumber.setOnClickListener {
            if (validateNumber()) {
                accountViewModel.resendOtp(
                    phoneCode + binding.etPhoneNumber.text.toString().trim(),
                    ConstantObjects.currentLanguage,
                    "3"
                )
            }
        }
        binding.button.setOnClickListener {
            prepareDataToUpdate()
        }
    }

    private fun prepareDataToUpdate() {
        var readyToSave = false
        if (binding.fristName.text.toString().trim() == "") {
            readyToSave = false
            binding.fristName.error =
                getString(R.string.Please_enter, getString(R.string.First_Name))
        } else if (binding.lastName.text.toString().trim() == "") {
            readyToSave = false
            binding.lastName.error = getString(R.string.Please_enter, getString(R.string.Last_Name))
        } else if (binding.btnDate.text.toString().trim() == "") {
            readyToSave = false
            binding.btnDate.error =
                getString(R.string.Please_select, getString(R.string.Date_of_Birth))
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
                    binding.fristName.text.toString().trim(),
                    binding.lastName.text.toString().trim(),
                    binding.btnDate.text.toString().trim(),
                    gender,
                    showMYInfo.toString()
                )
            }
        }
    }

    private fun validateNumber(): Boolean {
        val numberInput =
            binding.etPhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            binding.etPhoneNumber.visibility = View.VISIBLE
            binding.etPhoneNumber.error = getString(R.string.enterNewPhone)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            binding.etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else if (phoneCode.isEmpty()) {
            binding.etPhoneNumber.error = getString(R.string.selectCountry)
            false
        } else {
            binding.etPhoneNumber.error = null
            true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }

}