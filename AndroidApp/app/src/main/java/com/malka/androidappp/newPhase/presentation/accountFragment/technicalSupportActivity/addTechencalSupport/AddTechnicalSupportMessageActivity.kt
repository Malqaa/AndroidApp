package com.malka.androidappp.newPhase.presentation.accountFragment.technicalSupportActivity.addTechencalSupport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.View
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageDetails
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_technical_support_add_message.*


import kotlinx.android.synthetic.main.toolbar_main.*


class AddTechnicalSupportMessageActivity : BaseActivity() {
    //1-> suggestion , 2_complaint
    private var communicationType = 0
    private var isPhoneNumberValid: Boolean = false
    private lateinit var accountViewModel: AccountViewModel
    private var isEdit = false
    private var editId = 0
    private var technicalSupportMessageDetails: TechnicalSupportMessageDetails?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_technical_support_add_message)
        toolbar_title.text = getString(R.string.technical_support)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        editId = intent.getIntExtra(ConstantObjects.idKey, 0)
        technicalSupportMessageDetails=intent.getParcelableExtra(ConstantObjects.objectKey)
        if(isEdit&&technicalSupportMessageDetails!=null){
            setData(technicalSupportMessageDetails!!)
        }
       // println("ttt "+editId)
        setClickListeners()
        setupCountryCodePiker()
        setUpViewModel()
    }
     fun setData(technicalSupportMessageDetails: TechnicalSupportMessageDetails){
         if(technicalSupportMessageDetails.typeOfCommunication==1){
             tv_error_contact_us.hide()
             communicationType = 1
             tvContactUsTypeTitle.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
             tvContactUsTypeTitle.textSize = 11f
             tvContactUsType.text = getString(R.string.suggestions)
             tvContactUsType.show()
         } else if (technicalSupportMessageDetails.typeOfCommunication == 2) {
             tv_error_contact_us.hide()
             communicationType = 2
             tvContactUsTypeTitle.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
             tvContactUsTypeTitle.textSize = 11f
             tvContactUsType.text = getString(R.string.complaint)
             tvContactUsType.show()
         }
         etPhoneNumber.setText(technicalSupportMessageDetails.mobileNumber?:"")
         etEmail.setText(technicalSupportMessageDetails.email)
         problem_title.setText(technicalSupportMessageDetails.problemTitle)
         tvDescriptionAr.setText(technicalSupportMessageDetails.meassageDetails)
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
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
        accountViewModel.contactsMessageObserver.observe(this) { contactsMessageResp ->
            if (contactsMessageResp.status_code == 200) {
                val returnIntent = Intent()
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
                contactsMessageResp.message?.let { HelpFunctions.ShowLongToast(it, this) }
            } else {
                if (contactsMessageResp.message != null) {
                    HelpFunctions.ShowLongToast(contactsMessageResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }

    }

    private fun setupCountryCodePiker() {
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC)
        } else {
            countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH)
            //  etPhoneNumber.textAlignment=View.TEXT_ALIGNMENT_VIEW_START
        }
        countryCodePicker.registerCarrierNumberEditText(etPhoneNumber)
        countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
            isPhoneNumberValid = isValidNumber
        }
        countryCodePicker.setOnCountryChangeListener {
            etPhoneNumber.text = Editable.Factory.getInstance().newEditable("")
        }
    }

    private fun setClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }


        containerContactUsType.setOnClickListener {
            openCommunicationDialog()
        }
        btnSend.setOnClickListener {
            prepareDataToSave()
        }

    }

    private fun prepareDataToSave() {
        var readyToAdd = true
        tv_phone_error.visibility = View.GONE
        problem_title.error = null
        if (communicationType == 0) {
            tv_error_contact_us.show()
            readyToAdd = false
        }
        if (!isPhoneNumberValid) {
            tv_phone_error.visibility = View.VISIBLE
            tv_phone_error.text = getString(R.string.PleaseenteravalidPhoneNumber)
            readyToAdd = false

        }
        if (!validateSignupEmail()) {
            readyToAdd = false
        }
        if (problem_title.text.toString().trim() == "") {
            readyToAdd = false
            problem_title.error = getString(R.string.enter_problem_title)
        }
        if (tvDescriptionAr.text.toString().trim() == "") {
            readyToAdd = false
            tvDescriptionAr.error = getString(R.string.message_details)
        }
        if (readyToAdd) {
            if (isEdit) {
                accountViewModel.addContactUsMessage(
                    communicationType,
                    countryCodePicker.fullNumberWithPlus,
                    etEmail.text.toString().trim(),
                    problem_title.text.toString().trim(),
                    tvDescriptionAr.text.toString().trim(),
                    editId
                )
            } else {
                accountViewModel.addContactUsMessage(
                    communicationType,
                    countryCodePicker.fullNumberWithPlus,
                    etEmail.text.toString().trim(),
                    problem_title.text.toString().trim(),
                    tvDescriptionAr.text.toString().trim(),
                    null
                )
            }
        }

    }

    private fun validateSignupEmail(): Boolean {
        val emailInput =
            etEmail.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            etEmail.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            etEmail.error = null
            true
        }
    }

    private fun openCommunicationDialog() {
        val contactUsTypeDialog =
            ContactUsTypeDialog(this, object : ContactUsTypeDialog.SetOnSelectCommunicationType {
                override fun onSelectCommunicationType(position: Int) {
                    if (position == 1) {
                        tv_error_contact_us.hide()
                        communicationType = position
                        tvContactUsTypeTitle.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
                        tvContactUsTypeTitle.textSize = 11f
                        tvContactUsType.text = getString(R.string.suggestions)
                        tvContactUsType.show()
                    } else if (position == 2) {
                        tv_error_contact_us.hide()
                        communicationType = position
                        tvContactUsTypeTitle.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
                        tvContactUsTypeTitle.textSize = 11f
                        tvContactUsType.text = getString(R.string.complaint)
                        tvContactUsType.show()
                    }
                }

            })
        contactUsTypeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }
}