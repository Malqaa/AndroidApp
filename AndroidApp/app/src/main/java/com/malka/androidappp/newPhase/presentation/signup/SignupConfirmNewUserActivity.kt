package com.malka.androidappp.newPhase.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hbb20.CountryCodePicker
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.HelpFunctions.Companion.PASSWORD_PATTERN
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malka.androidappp.newPhase.presentation.signup.signupViewModel.SignupViewModel
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_signup_pg1.*
import kotlinx.android.synthetic.main.activity_signup_pg1.userNamee

class SignupConfirmNewUserActivity : BaseActivity() {

    var isPhoneNumberValid:Boolean=false
    var isBusinessAccount=false
    private lateinit var signupViewModel: SignupViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_pg1)

        if (ConstantObjects.currentLanguage ==ConstantObjects. ENGLISH) {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }
        setupRegisterViewModel()
        setClickListeners()
        setupCountryCodePiker()

    }

    private fun setupRegisterViewModel() {
        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        signupViewModel.isNetworkFail.observe(this, Observer {
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
        signupViewModel.validateAndGenerateOTPObserver.observe(this, Observer { validateUserAndGenerateOTP ->
            if (validateUserAndGenerateOTP.otpData != null) {
                validateUserAndGenerateOTP.otpData!!.userName=userNamee.text.toString().trim()
                validateUserAndGenerateOTP.otpData!!.userPass=textPass.text.toString().trim()
                validateUserAndGenerateOTP.otpData!!.userEmail=textEmaill.text.toString().trim()
                validateUserAndGenerateOTP.otpData!!.isBusinessAccount=isBusinessAccount
                validateUserAndGenerateOTP.otpData!!.invitationCode=invitation_code.text.toString().trim()
                goToOTPVerificationScreen(validateUserAndGenerateOTP.otpData!!)
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }
        })
        signupViewModel.errorResponseObserver.observe(this, Observer {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(
                    it.message,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        })
    }

    /**click events**/
    private fun setClickListeners() {
        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            setLocate()
        }
        btnLogin.setOnClickListener {
            onBackPressed()
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
    fun SignuuPg1confirmInput(v: View) {
        if (!validateSignupEmail() or !validateSignupPassword()
            or !validateNumber() or !validateSignupConfrmPassword() or
            !validateSignupUser() ) {
            return
        } else {
            if(switch_term_condition._getChecked()){
                //apicallcreateuser()
                signupViewModel.validateUserAndGenerateOTP(
                    userNamee.text.toString().trim(),
                    textEmaill.text.toString().trim(),
                    countryCodePicker.fullNumberWithPlus,
                    Lingver.getInstance().getLanguage()
                )

            }else{
                showError(getString(R.string.Please_select,getString(R.string.term_condition)))
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
            textPass!!.error = getString(R.string.Passwordtooweak)
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
            tv_phone_error.visibility=View.VISIBLE
            tv_phone_error.text = getString(R.string.Fieldcantbeempty)
            false
        } else if(!isPhoneNumberValid){
            tv_phone_error.visibility=View.VISIBLE
            tv_phone_error.text = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        }else{
            tv_phone_error.text = null
            tv_phone_error.visibility=View.GONE
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
        } else if (Input.length < 4) {
            userNamee!!.error = getString(R.string.Usernamemusthaveatleast4characters)
            false
        } else {
            userNamee!!.error = null
            true
        }
    }
     /***/
    fun signuppg1prev(view: View) {
        onBackPressed()
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

//    private fun persistImage(bitmap: Bitmap, name: String) :File?{
//        val filesDir: File = getFilesDir()
//        val imageFile = File(filesDir, "$name.jpg")
//        val os: OutputStream
//        try {
//            os = FileOutputStream(imageFile)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
//            os.flush()
//            os.close()
//            return imageFile
//        } catch (e: Exception) {
//           print(e.message)
//            return null
//        }
//    }

//    private fun validateUserAndGenerateOTP() {
//        HelpFunctions.startProgressBar(this)
//        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val emailId = textEmaill.text.toString().trim()
//        val mobileNumber =   countryCodePicker.fullNumberWithPlus
//        val passcode = textPass.text.toString().trim()
//        val userName = userNamee.text.toString().trim()
//        val invitationCode=invitation_code.text.toString().trim()
//        val call: Call<ValidateAndGenerateOTPResp> = malqaa.validateUserAndGenerateOtp(
//            userName=userName,
//            userPhone = mobileNumber,
//            userEmail =emailId,
//            lang=Lingver.getInstance().getLanguage()
//        )
//        call.enqueue(object : Callback<ValidateAndGenerateOTPResp> {
//            override fun onResponse(
//                call: Call<ValidateAndGenerateOTPResp>, response: Response<ValidateAndGenerateOTPResp>
//            ) {
//                if (response.isSuccessful) {
//                    val data: ValidateAndGenerateOTPResp? =response.body()
//                    if (data!!.status_code == 200) {
//                        data.otpData?.let{
//                            it.userName=userName
//                            it.userPass=passcode
//                            it.userEmail=emailId
//                            it.isBusinessAccount=isBusinessAccount
//                            it.invitationCode=invitationCode
//                            goToOTPVerificationScreen(it)
//                        }
//                    } else {
//                        if(data?.message!=null){
//                            showError(data.message!!)
//                        }else{
//                            showError(getString(R.string.serverError)+" ${response.code()}")
//                        }
//                    }
//                } else {
//                    HelpFunctions.ShowLongToast(response.toString(), this@SignupPg1)
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//
//            override fun onFailure(call: Call<ValidateAndGenerateOTPResp>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg1) }
//            }
//        })
//    }


//    fun apicallcreateuser() {
//        HelpFunctions.startProgressBar(this)
//        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val emailId = textEmaill.text.toString().trim()
//        val mobilenum =   countryCodePicker.fullNumberWithPlus
//        val passcode = textPass.text.toString().trim()
//        val usernaam = userNamee.text.toString().trim()
//
////        val icon = BitmapFactory.decodeResource(
////           resources,
////            R.drawable.accountbg
////        )
////        val file=persistImage(icon,"test.png")!!
////        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
////        val fileToUpload =
////            MultipartBody.Part.createFormData("imgProfile", file.name, requestBody)
//
//
////        val call: Call<GeneralRespone> = malqaa.createuser(
////            emailId.requestBody(),
////            passcode.requestBody(),
////            mobilenum.requestBody(),
////            passcode.requestBody(),
////            usernaam.requestBody(),
////            "abc".requestBody(),
////            culture().requestBody(),
////            projectName.requestBody(),
////            deviceType.requestBody(),
////            getDeviceId().requestBody()
////            ,fileToUpload)
//
//        val call: Call<RegisterResp> = malqaa.createuser2(
//            usernaam.requestBody(),
//            mobilenum.requestBody(),
//            emailId.requestBody(),
//            passcode.requestBody(),
//            "fridtUSrerNAme".requestBody(),
//            "fridtUSrerNAme".requestBody(),
//            isBusinessAccount.toString().requestBody(),
//            Lingver.getInstance().getLanguage().requestBody(),
//            projectName.requestBody(),
//            deviceType.requestBody(),
//            getDeviceId().requestBody(),
//            )
////        var data=HashMap<String,Any>()
////        data["userName"]=usernaam
////        data["phone"]=mobilenum
////        data["email"]=emailId
////        data["password"]=passcode
////        data["firstName"]=userFirstName
////        data["lastName"]=userLastName
////        data["isBusinessAccount"]=isBusinessAccount
////        data["lang"]= Lingver.getInstance().getLanguage()
////        data["projectName"]=projectName
////        data["deviceType"]=deviceType
////        data["deviceId"]=getDeviceId()
////        println("hhh "+ Gson().toJson(data))
//        call.enqueue(object : Callback<RegisterResp> {
//
//            override fun onResponse(
//                call: Call<RegisterResp>, response: Response<RegisterResp>
//            ) {
//
//                if (response.isSuccessful) {
//                    val data=response.body()
//                    if (data!!.status_code == 200) {
//                        HelpFunctions.ShowLongToast("register", this@SignupPg1)
//                    } else {
//                        if(data.message!=null){
//                            showError(data.message)
//                        }else{
//                            showError(response.message())
//
//                        }
//                    }
//                } else {
//                    HelpFunctions.ShowLongToast(response.toString(), this@SignupPg1)
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//
//            override fun onFailure(call: Call<RegisterResp>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                t.message?.let { HelpFunctions.ShowLongToast(it, this@SignupPg1) }
//            }
//        })
//
//    }
//
//
//
//
//    fun NextAcivityparsedata(data: GeneralRespone) {
//        val datacode = data.code
//        val dataUserId = data.id
//        val dataemail = textEmaill.text.toString().trim()
//        val datapassword = textPass.text.toString().trim()
//
////        val datamobnum = PhoneNumber.text.toString().trim()
////        val countryCode = PhoneNumber._getEndText()
//        val fullmobilenum = countryCodePicker.fullNumberWithPlus
//        val intenttt = Intent(this, SignupPg2::class.java)
//        intenttt.putExtra("datacode", datacode)
//        intenttt.putExtra("userid", dataUserId)
//        intenttt.putExtra("dataemail", dataemail)
//        intenttt.putExtra("datapassword", datapassword)
//        intenttt.putExtra("datamobnum", fullmobilenum)
//        intenttt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intenttt)
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//    }


}
