package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.technicalSupportActivity.addTechencalSupport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.View
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityTechnicalSupportAddMessageBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.contauctUsMessage.TechnicalSupportMessageDetails
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver

class AddTechnicalSupportMessageActivity :
    BaseActivity<ActivityTechnicalSupportAddMessageBinding>() {
    //1-> suggestion , 2_complaint
    private var communicationType = 0
    private var isPhoneNumberValid: Boolean = false
    private lateinit var accountViewModel: AccountViewModel
    private var isEdit = false
    private var editId = 0
    private var technicalSupportMessageDetails: TechnicalSupportMessageDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityTechnicalSupportAddMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.technical_support)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        editId = intent.getIntExtra(ConstantObjects.idKey, 0)
        technicalSupportMessageDetails = intent.getParcelableExtra(ConstantObjects.objectKey)
        if (isEdit && technicalSupportMessageDetails != null) {
            setData(technicalSupportMessageDetails!!)
        }
        // println("ttt "+editId)
        setClickListeners()
        setupCountryCodePiker()
        setUpViewModel()
    }

    fun setData(technicalSupportMessageDetails: TechnicalSupportMessageDetails) {
        if (technicalSupportMessageDetails.typeOfCommunication == 1) {
            binding.tvErrorContactUs.hide()
            communicationType = 1
            binding.tvContactUsTypeTitle.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
            binding.tvContactUsTypeTitle.textSize = 11f
            binding.tvContactUsType.text = getString(R.string.suggestions)
            binding.tvContactUsType.show()
        } else if (technicalSupportMessageDetails.typeOfCommunication == 2) {
            binding.tvErrorContactUs.hide()
            communicationType = 2
            binding.tvContactUsTypeTitle.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
            binding.tvContactUsTypeTitle.textSize = 11f
            binding.tvContactUsType.text = getString(R.string.complaint)
            binding.tvContactUsType.show()
        }
        binding.etPhoneNumber.setText(technicalSupportMessageDetails.mobileNumber ?: "")
        binding.etEmail.setText(technicalSupportMessageDetails.email)
        binding.problemTitle.setText(technicalSupportMessageDetails.problemTitle)
        binding.tvDescriptionAr.setText(technicalSupportMessageDetails.meassageDetails)
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

    private fun setClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.containerContactUsType.setOnClickListener {
            openCommunicationDialog()
        }
        binding.btnSend.setOnClickListener {
            prepareDataToSave()
        }
    }

    private fun prepareDataToSave() {
        var readyToAdd = true
        binding.tvPhoneError.visibility = View.GONE
        binding.problemTitle.error = null
        if (communicationType == 0) {
            binding.tvErrorContactUs.show()
            readyToAdd = false
        }
        if (!isPhoneNumberValid) {
            binding.tvPhoneError.visibility = View.VISIBLE
            binding.tvPhoneError.text = getString(R.string.PleaseenteravalidPhoneNumber)
            readyToAdd = false
        }
        if (!validateSignupEmail()) {
            readyToAdd = false
        }
        if (binding.problemTitle.text.toString().trim() == "") {
            readyToAdd = false
            binding.problemTitle.error = getString(R.string.enter_problem_title)
        }
        if (binding.tvDescriptionAr.text.toString().trim() == "") {
            readyToAdd = false
            binding.tvDescriptionAr.error = getString(R.string.message_details)
        }
        if (readyToAdd) {
            if (isEdit) {
                accountViewModel.addContactUsMessage(
                    communicationType,
                    binding.countryCodePicker.fullNumberWithPlus,
                    binding.etEmail.text.toString().trim(),
                    binding.problemTitle.text.toString().trim(),
                    binding.tvDescriptionAr.text.toString().trim(),
                    editId
                )
            } else {
                accountViewModel.addContactUsMessage(
                    communicationType,
                    binding.countryCodePicker.fullNumberWithPlus,
                    binding.etEmail.text.toString().trim(),
                    binding.problemTitle.text.toString().trim(),
                    binding.tvDescriptionAr.text.toString().trim(),
                    null
                )
            }
        }

    }

    private fun validateSignupEmail(): Boolean {
        val emailInput =
            binding.etEmail.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.etEmail.error = getString(R.string.Fieldcantbeempty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.etEmail.error = getString(R.string.Pleaseenteravalidemailaddress)
            false
        } else {
            binding.etEmail.error = null
            true
        }
    }

    private fun openCommunicationDialog() {
        val contactUsTypeDialog =
            ContactUsTypeDialog(this, object : ContactUsTypeDialog.SetOnSelectCommunicationType {
                override fun onSelectCommunicationType(position: Int) {
                    if (position == 1) {
                        binding.tvErrorContactUs.hide()
                        communicationType = position
                        binding.tvContactUsTypeTitle.updatePadding(
                            top = 0,
                            bottom = 0,
                            left = 0,
                            right = 0
                        )
                        binding.tvContactUsTypeTitle.textSize = 11f
                        binding.tvContactUsType.text = getString(R.string.suggestions)
                        binding.tvContactUsType.show()
                    } else if (position == 2) {
                        binding.tvErrorContactUs.hide()
                        communicationType = position
                        binding.tvContactUsTypeTitle.updatePadding(
                            top = 0,
                            bottom = 0,
                            left = 0,
                            right = 0
                        )
                        binding.tvContactUsTypeTitle.textSize = 11f
                        binding.tvContactUsType.text = getString(R.string.complaint)
                        binding.tvContactUsType.show()
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