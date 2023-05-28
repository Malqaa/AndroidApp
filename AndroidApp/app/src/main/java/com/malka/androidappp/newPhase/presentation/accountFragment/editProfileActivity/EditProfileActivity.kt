package com.malka.androidappp.newPhase.presentation.accountFragment.editProfileActivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.servicemodels.LocationPickerModel
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.date
import kotlinx.android.synthetic.main.activity_edit_profile.etPhoneNumber
import kotlinx.android.synthetic.main.activity_edit_profile.lastName
import kotlinx.android.synthetic.main.activity_edit_profile.radiofemale
import kotlinx.android.synthetic.main.activity_edit_profile.radiomale
import kotlinx.android.synthetic.main.activity_edit_profile.userNamee
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.toolbar_main.*

class EditProfileActivity : AppCompatActivity() {
    var gender_: Int = 0
    private lateinit var accountViewModel: AccountViewModel
    val chooseLocationLuncher =
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
        etPhoneNumber.isEnabled = false

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
    }

    private fun setUserData(tempUserData: LoginUser) {
        password.setText(tempUserData.password ?: "")
        etPhoneNumber.setText(tempUserData.phone ?: "")
        userNamee.setText(tempUserData.userName ?: "")
        textEmail.setHint(tempUserData.email ?: "")
        fristName.setText(tempUserData.firstName ?: "")
        lastName.setText(tempUserData.lastName ?: "")
        date.setText(tempUserData.dateOfBirth ?: "")
        gender_ = tempUserData.gender
        when (gender_) {
            Constants.male -> {
                radiomale.performClick()
            }
            Constants.female -> {
                radiofemale.performClick()
            }
        }
    }

    private fun setViewClickListeners() {
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
    }
}