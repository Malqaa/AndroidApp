package com.malqaa.androidappp.newPhase.presentation.activities.signup.activity3

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySignupPg4Binding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.constants.Constants
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.validateAndGenerateOTPResp.OtpData
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.signup.signupViewModel.SignupViewModel
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.PickImageMethodsDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.malqaa.androidappp.newPhase.utils.BetterActivityResult
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ImagePicker
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.SetOnImagePickedListeners
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import java.io.File
import kotlin.math.roundToInt

class SignupCreateNewUser : BaseActivity<ActivitySignupPg4Binding>(),
    PickImageMethodsDialog.OnAttachedImageMethodSelected {

    var selectedCountryId: Int = 0
    var selectedRegionId: Int = 0
    var selectedNeighborhoodId: Int = 0
    var gender_ = -1
    private var otpData: OtpData? = null
    var REQUEST_CHECK_PERMISSION_READ_STORAGE: Int = 1000
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var imageMethodsPickerDialog: PickImageMethodsDialog
    private lateinit var imagePicker: ImagePicker
    private var userImageUri: Uri? = null
    val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySignupPg4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        otpData = intent.getParcelableExtra(Constants.otpDataKey)
        setupRegisterViewModel()
        setupClickListeners()
    }

    private fun setupRegisterViewModel() {
        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        signupViewModel.isNetworkFail.observe(this) {
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

        }
        signupViewModel.errorResponseObserver.observe(this, Observer {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                when (it.message) {
                    "UsernameExists" -> {
                        HelpFunctions.ShowLongToast(getString(R.string.userNameExists), this)
                    }

                    "PhoneNumberExists" -> {
                        HelpFunctions.ShowLongToast(getString(R.string.userPhoneExists), this)
                    }

                    "EmailExists" -> {
                        HelpFunctions.ShowLongToast(getString(R.string.userEmailExists), this)
                    }

                    else -> {
                        if (it.message2 != null) {
                            HelpFunctions.ShowLongToast(
                                it.message2,
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
            }
        })
        signupViewModel.registerRespObserver.observe(this) { registerResp ->
            when (registerResp.status) {
                "UsernameExists" -> {
                    HelpFunctions.ShowLongToast(getString(R.string.userNameExists), this)
                }

                "PhoneNumberExists" -> {
                    HelpFunctions.ShowLongToast(getString(R.string.userPhoneExists), this)
                }

                "EmailExists" -> {
                    HelpFunctions.ShowLongToast(getString(R.string.userEmailExists), this)
                }

                "Success" -> {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.Accounthasbeencreated),
                        this@SignupCreateNewUser
                    )
                    saveUserData(registerResp.userObject)

                }

                else -> {
                    if (registerResp.message != null) {
                        HelpFunctions.ShowLongToast(
                            registerResp.message,
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

        }
    }

    /***clickEvents*/
    private fun setupClickListeners() {
        binding.btnDate._setOnClickListener {
            DatePickerFragment(maxdayToday = true, minDateToday = false) { selectDate_ ->
                binding.btnDate.text = "$selectDate_ "

            }.show(supportFragmentManager, "")

        }
        binding.countryContainer._setOnClickListener {
            openCountryDialog()
        }
        binding.regionContainer._setOnClickListener {
            if (selectedCountryId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                openRegionDialog()
            }

        }
        binding.neighborhoodContainer._setOnClickListener {
            if (selectedRegionId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                openNeighborhoodDialog()
            }
        }
        binding.radiomale._setOnClickListener {
            binding.radiomale._setCheck(!binding.radiomale.getCheck())
            binding.radiofemale._setCheck(false)
            gender_ = Constants.male
        }
        binding.radiofemale._setOnClickListener {
            binding.radiofemale._setCheck(!binding.radiofemale.getCheck())
            binding.radiomale._setCheck(false)
            gender_ = Constants.female

        }
        binding.confirmButton.setOnClickListener {
            if (isValid()) {
                callCreateUser()
            }
        }
        binding.ivPickUserImage.setOnClickListener {
            openCameraChooser()
        }
    }

    /*****/
    private fun openCameraChooser() {
        imageMethodsPickerDialog = PickImageMethodsDialog(this, false, this)
        imageMethodsPickerDialog.show()
    }

    //====camera chooser
    override fun setOnAttachedImageMethodSelected(attachedMethod: Int) {
        imagePicker = ImagePicker(this, null, object : SetOnImagePickedListeners {
            override fun onImagePicked(imageUri: Uri) {
                setImage(imageUri)
            }

            override fun launchImageActivityResult(
                imageIntent: Intent,
                requestCode: Int,
            ) {
                activityLauncher.launch(imageIntent) { activityResult ->
                    if (activityResult.resultCode == RESULT_OK) {
                        imagePicker.handleActivityResult(
                            activityResult.resultCode,
                            requestCode,
                            activityResult.data
                        )
                    }
                }
            }
        })
        if (attachedMethod == ConstantObjects.CAMERA) {
            imagePicker.choosePicture(ImagePicker.CAMERA)
        } else {
            imagePicker.choosePicture(ImagePicker.GALLERY)
        }
    }

    override fun onDeleteImage() {

    }

    private fun setImage(imageUri: Uri) {
        try {
            val bitmap =
                BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
            val scaleBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.4f).roundToInt(),
                (bitmap.height * 0.4f).roundToInt(),
                true
            )
            println("hhhh loaded")
            getPicassoInstance()
                .load(imageUri)
                .into(binding.ivUserImage)
            userImageUri = imageUri


        } catch (e: Exception) {
            println("hhhh " + e.message)
            HelpFunctions.ShowLongToast(getString(R.string.pickRightImage), this)
        }
    }


    //=======Permissions and data handling
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handelPermissionsResult(requestCode, grantResults)
    }

    /**countries , region and Neighborhood Dialogs**/
    private fun openCountryDialog() {
        val countryDialog = CountryDialog(this, object : CountryDialog.GetSelectedCountry {
            override fun onSelectedCountry(
                id: Int,
                countryName: String,
                countryFlag: String?,
                countryCode: String?
            ) {
                /**setCountryData*/
                selectedCountryId = id
                binding.countryContainer.text = countryName.toString()
                binding.countryContainer._setStartIconImage(countryFlag)
                /**resetRegion*/
                selectedRegionId = 0
                binding.regionContainer.text = null
                /**resetRegion*/
                selectedNeighborhoodId = 0
                binding.neighborhoodContainer.text = null
            }
        })
        countryDialog.show()
    }


    private fun openRegionDialog() {
        val regionDialog =
            RegionDialog(this, selectedCountryId, object : RegionDialog.GetSelectedRegion {
                override fun onSelectedRegion(id: Int, regionName: String) {
                    /**setRegionData*/
                    selectedRegionId = id
                    binding.regionContainer.text = regionName
                    /**resetNeighborhood*/
                    selectedNeighborhoodId = 0
                    binding.neighborhoodContainer.text = null
                }
            })
        regionDialog.show()
    }

    private fun openNeighborhoodDialog() {
        val neighborhoodDialog = NeighborhoodDialog(
            this,
            selectedRegionId,
            object : NeighborhoodDialog.GetSelectedNeighborhood {
                override fun onSelectedNeighborhood(id: Int, neighborhoodName: String) {
                    /**setNeighborhoodData*/
                    selectedNeighborhoodId = id
                    binding.neighborhoodContainer.text = neighborhoodName.toString()
                }
            })
        neighborhoodDialog.show()
    }

    private fun isValid(): Boolean {
        if (binding.firstName!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.First_Name)))
            return false
        }
        if (binding.firstName!!.text.toString().length < 2) {
            showError("${getString(R.string.typingTwoLetter)} ${getString(R.string.First_Name)}")
            return false
        }

        if (binding.lastName!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Last_Name)))
            return false
        }
        if (binding.lastName!!.text.toString().length < 2) {
            showError("${getString(R.string.typingTwoLetter)} ${getString(R.string.Last_Name)}")
            return false
        }
        if (binding.btnDate!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.Date_of_Birth)))
            return false
        }


        if (gender_ == -1) {
            showError(getString(R.string.Please_select, getString(R.string.Gender)))
            return false
        }

        if (selectedCountryId == 0) {
            showError(getString(R.string.Please_select, getString(R.string.Country)))
            return false
        }

        if (selectedRegionId == 0) {
            showError(getString(R.string.Please_select, getString(R.string.Region)))
            return false
        }
//
        if (selectedNeighborhoodId == 0) {
            showError(getString(R.string.Please_select, getString(R.string.district)))
            return false
        }

//
        if (binding.area!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Area)))
            return false
        }
        if (binding.streetNUmber!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.StreetNumber)))
            return false
        }
        if (binding.countyCode!!.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.ZipCode)))
            return false
        }
        return true
    }

    private fun callCreateUser() {
        var invitationCode = ""
        otpData?.let {
            invitationCode = it.invitationCode ?: ""
        }
        println(
            "hhhh data:${
                binding.btnDate.text.toString().trim()
            } , gender:${gender_} ,countryId "
        )

        var file: File? = null
        userImageUri?.let {
            file = File(it.path)
        }
        Log.i("text #1", "invitationCode: $invitationCode")
        signupViewModel.createUser(
            otpData?.userName.toString(),
            otpData?.phoneNumber.toString(),
            otpData?.userEmail.toString(),
            otpData?.userPass.toString(),
            invitationCode,
            binding.firstName.text.toString().trim(),
            binding.lastName.text.toString().trim(),
            binding.btnDate.text.toString().trim(),
            gender_.toString(),
            selectedCountryId.toString(),
            selectedRegionId.toString(),
            selectedNeighborhoodId.toString(),
            binding.area.text.toString(),
            binding.streetNUmber.text.toString(),
            binding.countyCode.text.toString(),
            otpData?.isBusinessAccount.toString(),
            Lingver.getInstance().getLanguage(),
            HelpFunctions.projectName,
            HelpFunctions.deviceType,
            SharedPreferencesStaticClass.getFcmToken(),
            file
        )
    }

    private fun saveUserData(userObject: LoginUser) {
        ConstantObjects.logged_userid = userObject.id
        val userId: String = userObject.id
        ConstantObjects.logged_userid = userId
        HelpFunctions.ShowLongToast(getString(R.string.LoginSuccessfully), this)
        Paper.book().write(SharedPreferencesStaticClass.islogin, true)
        Paper.book().write<LoginUser>(SharedPreferencesStaticClass.user_object, userObject)
        setResult(RESULT_OK, Intent())
        finish()
    }

    private fun signInAfterSignUp() {
        val intentSignIn = Intent(this, SignInActivity::class.java)
        intentSignIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentSignIn)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@SignupCreateNewUser, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onDestroy() {
        super.onDestroy()
        signupViewModel.closeAllCall()
    }
}
