package com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount.addBusinessAccount

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.webkit.URLUtil
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.utils.activitiesMain.placePicker.LocationPickerActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ImageSelectModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.presentation.accountFragment.businessAccount.BusinessAccountViewModel
import com.malka.androidappp.newPhase.presentation.accountFragment.mapActivity.MapActivity
import com.malka.androidappp.newPhase.presentation.dialogsShared.PickImageMethodsDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malka.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.malka.androidappp.newPhase.presentation.utils.CameraHelper
import com.squareup.picasso.Picasso
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import kotlinx.android.synthetic.main.activity_business_signup.*
import kotlinx.android.synthetic.main.activity_business_signup.btnOpenCountry
import kotlinx.android.synthetic.main.activity_business_signup.countryContainer
import kotlinx.android.synthetic.main.activity_business_signup.etPhoneNumber
import kotlinx.android.synthetic.main.activity_business_signup.ivFlag
import kotlinx.android.synthetic.main.activity_business_signup.ivPickUserImage
import kotlinx.android.synthetic.main.activity_business_signup.ivUserImageBusiness
import kotlinx.android.synthetic.main.activity_business_signup.neighborhoodContainer
import kotlinx.android.synthetic.main.activity_business_signup.regionContainer
import kotlinx.android.synthetic.main.activity_business_signup.textEmaill
import kotlinx.android.synthetic.main.activity_business_signup.tvCode
import kotlinx.android.synthetic.main.activity_business_signup.userNamee
import okhttp3.MultipartBody

import kotlin.math.roundToInt


class BusinessAccountCreateActivity : BaseActivity(), CountryDialog.GetSelectedCountry,
    PickImageMethodsDialog.OnAttachedImageMethodSelected {

    //==== userImage= 1 ,commericalimage=2
    private var imageType: Int = 1
    private var selectedCommericalRegisterType: Int = 1
    var youtube = false
    var twitter = false
    var itickTok = false
    var snapShot = false
    var linkedin = false
    var businessDocument = ""
    var selectdate = ""
    var fm: FragmentManager? = null
    var isProfileImage = false
    var profileImageBase64 = ""

    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()
    private val IMAGE_PICK_CODE = 1000

    private val PERMISSION_CODE = 1001
    private var mResults: ArrayList<String> = ArrayList()
    var locationPicker: LocationPickerModel? = null
    val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    val REQUEST_GPS_SETTINGS = 107
    lateinit var countryDialog: CountryDialog
    val chooseLocationLuncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                locationPicker = Gson().fromJson(
                    result.data!!.getStringExtra(ConstantObjects.data),
                    LocationPickerModel::class.java
                )
            }
        }

    lateinit var commirecalRegisterationTypeAdapter: CommirecalRegisterationTypeAdapter
    var selectedCountryId: Int = 0
    var selectedRegionId: Int = 0
    var selectedNeighborhoodId: Int = 0

    val getLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lat = result.data?.getDoubleExtra("lat", 0.0) ?: 0.0
                longitude = result.data?.getDoubleExtra("longitude", 0.0) ?: 0.0
                locating_the_company.text = "$lat,$longitude"
            }
        }
    private lateinit var imageMethodsPickerDialog: PickImageMethodsDialog
    private lateinit var imagePicker: ImagePicker
    private var userImageUri: Bitmap? = null
    private lateinit var commercialRegistryFileList: ArrayList<MultipartBody.Part>
    val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)
    private lateinit var businessAccountViewModel: BusinessAccountViewModel
    private var lat: Double = 0.0
    private var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup)
        countryDialog = CountryDialog(this, this)
        commercialRegistryFileList = ArrayList()
        setupCommircalRegisteration()
        setViewClickListeners()
        setupViewModel()


        commercial_registration_no._setMaxLength(15)

    }

    private fun setupViewModel() {
        businessAccountViewModel = ViewModelProvider(this).get(BusinessAccountViewModel::class.java)
        businessAccountViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()

        }


        businessAccountViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        businessAccountViewModel.errorResponseObserver.observe(this) {
            if (it.message2 != null) {
                HelpFunctions.ShowLongToast(it.message2!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
//        businessAccountViewModel.errorResponseObserver.observe(this) {
//            if (it.message != null) {
//                HelpFunctions.ShowLongToast(it.message!!, this)
//            } else {
//                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
//            }
//        }
        businessAccountViewModel.addbusinessAccountListObserver.observe(this) {
            if (it.status_code == 200) {
                setResult(Activity.RESULT_OK)
                finish()
                HelpFunctions.ShowLongToast("done", this)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
    }

    private fun setupCommircalRegisteration() {
        var commricaalRegisterationTyesList: ArrayList<String> = ArrayList()
        commricaalRegisterationTyesList.add(getString(R.string.commercialRegister))
        commricaalRegisterationTyesList.add(getString(R.string.freelanceCertificate))
        commirecalRegisterationTypeAdapter =
            CommirecalRegisterationTypeAdapter(this, commricaalRegisterationTyesList)
        spinnerCommirecalRegisterationType.adapter = commirecalRegisterationTypeAdapter
        spinnerCommirecalRegisterationType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View, position: Int, l: Long
                ) {
                    selectedCommericalRegisterType = position + 1
                    println("hhhh " + selectedCommericalRegisterType)

                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
    }


    private fun setViewClickListeners() {
        ivPickUserImage.setOnClickListener {
            imageType = 1
            openCameraChooser()
        }

        download_the_commercial_registry_file.setOnClickListener {
            imageType = 2
            openCameraChooser()

        }
        btnOpenCountry.setOnClickListener {
            countryDialog.show()
        }
        ic_twitter_select.setOnClickListener {
            twitter = !twitter
            if (twitter) {
                ic_twitter_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_twitter.show()
            } else {
                ic_twitter_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_twitter.hide()
            }

        }
        ic_youtube_select.setOnClickListener {
            youtube = !youtube
            if (youtube) {
                ic_youtube_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_youtube.show()
            } else {
                ic_youtube_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_youtube.hide()
            }

        }
        ic_linkedin.setOnClickListener {
            linkedin = !linkedin
            if (linkedin) {
                ic_linkedIn.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_linkedIn.show()
            } else {
                ic_linkedIn.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_linkedIn.hide()
            }

        }
        ic_snapshot_select.setOnClickListener {
            snapShot = !snapShot
            if (snapShot) {
                ic_snapshot_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_snapshot.show()
            } else {
                ic_snapshot_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_snapshot.hide()
            }

        }
        ic_ticktok_select.setOnClickListener {
            itickTok = !itickTok
            if (itickTok) {
                ic_ticktok_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_tickTok.show()
            } else {
                ic_ticktok_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_tickTok.hide()
            }

        }
        tvDate.setOnClickListener {
            tvDate.error = null
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                tvDate.text = "$selectdate_ "
                selectdate = selectdate_

            }
            dateDialog.show(fm!!, "")
        }
        countryContainer._setOnClickListener {
            openCountryDialog()
        }
        regionContainer._setOnClickListener {
            if (selectedCountryId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                openRegionDialog()
            }

        }
        neighborhoodContainer._setOnClickListener {
            if (selectedRegionId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                openNeighborhoodDialog()
            }
        }
        locating_the_company.setOnClickListener {
            getLocationLauncher.launch(Intent(this, MapActivity::class.java))
        }

//        locating_the_company.setOnClickListener {
//            checkLocationPermission()
//        }

        btnSaveBussinessAccount.setOnClickListener() {
            checkAccountData()
//            if (profileImageBase64.isEmpty()) {
//                showError(getString(R.string.Please_enter, getString(R.string.profile_picture)))
//            } else {
//                if (validateUserName() && validateCompanyNameAr() && validateSignupEmail() && validateNumber() && validateCompanyURL() && validateProfilePicture() && validateEmployementType() && validateCommercialRegisterNo()
//                    && validateExpiryDate() && validateTaxNumber() && validateCommercialRegisterDoc()
//                ) {
//
//                    if (selectedImagesURI.size == 0) {
//                        showError(
//                            getString(
//                                R.string.Please_select,
//                                getString(R.string.download_the_commercial_registry_file)
//                            )
//                        )
//                    } else {
//
//                        if (locationPicker == null) {
//                            showError(
//                                getString(
//                                    R.string.Please_select,
//                                    getString(R.string.locating_the_company)
//                                )
//                            )
//                        } else {
//                            addBusinessUser()
//                        }
//
//                    }
//                }
//            }


        }
        //        employment_type.setOnClickListener {
//
//            val list: ArrayList<SearchListItem> = ArrayList()
//
//            list.add(SearchListItem(1, getString(R.string.Commercial_Record)))
//            list.add(SearchListItem(2, getString(R.string.Free_work)))
//            list.add(SearchListItem(3, getString(R.string.A_favour)))
//
//            employment_type.showSpinner(
//                this,
//                list,
//                getString(R.string.Select, getString(R.string.type_of_employment))
//            ) {
//                employment_type.text = it.title
//
//            }
//        }
//        upload_photo.setOnClickListener {
//            isProfileImage = true
//            checkPermissions()
//
//        }

        busi_signup2_btn.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    /***/
    private fun openCountryDialog() {
        val countryDialog = CountryDialog(this, object : CountryDialog.GetSelectedCountry {
            override fun onSelectedCountry(
                id: Int, countryName: String, countryFlag: String?, countryCode: String?
            ) {
                /**setCountryData*/
                selectedCountryId = id
                countryContainer.text = countryName.toString()
                countryContainer._setStartIconImage(countryFlag)
                /**resetRegion*/
                selectedRegionId = 0
                regionContainer.text = null
                /**resetRegion*/
                selectedNeighborhoodId = 0
                neighborhoodContainer.text = null
            }
        })
        countryDialog.show()
    }

    private fun openRegionDialog() {
        val regionDialog =
            RegionDialog(this, selectedCountryId, object : RegionDialog.GetSelectedRegion {
                override fun onSelectedRegion(id: Int, regionName: String) {
                    /**setRegionData*/
                    /**setRegionData*/
                    selectedRegionId = id
                    regionContainer.text = regionName.toString()
                    /**resetNeighborhood*/
                    /**resetNeighborhood*/
                    selectedNeighborhoodId = 0
                    neighborhoodContainer.text = null
                }
            })
        regionDialog.show()
    }

    private fun openNeighborhoodDialog() {
        val neighborhoodDialog = NeighborhoodDialog(this,
            selectedRegionId,
            object : NeighborhoodDialog.GetSelectedNeighborhood {
                override fun onSelectedNeighborhood(id: Int, neighborhoodName: String) {
                    /**setNeighborhoodData*/
                    /**setNeighborhoodData*/
                    selectedNeighborhoodId = id
                    neighborhoodContainer.text = neighborhoodName.toString()
                }
            })
        neighborhoodDialog.show()
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
                    if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
                        imagePicker.handleActivityResult(
                            activityResult.resultCode, requestCode, activityResult.data)
                    }
                }
            }
        })
        if (attachedMethod == ConstantObjects.CAMERA) {
            imagePicker.choosePicture(ImagePicker.CAMERA)
        } else {
            openGallery(startForResult)
//            openGallery()
//            imagePicker.choosePicture(ImagePicker.GALLERY)
        }
    }


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap = CameraHelper.handleResult(it?.data?.data!!, this)
                setImage(it?.data?.data!!)
//                val  file = CameraHelper.getMultiPartFromBitmap(bitmap, "imgProfile", this)
//                accountViewModel.editProfileImage(file)
//                ivUserImage.setImageBitmap(bitmap)
            }
        }

    fun openGallery(startForResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    override fun onDeleteImage() {

    }

    private fun setImage(imageUri: Uri) {
        try {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
            val scaleBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.4f).roundToInt(),
                (bitmap.height * 0.4f).roundToInt(),
                true
            )
            println("hhhh loaded")
            if (imageType == 1) {
                Picasso.get().load(imageUri).into(ivUserImageBusiness)
                userImageUri = bitmap
            } else if (imageType == 2) {
//                var file: File? = null
//                imageUri.let { imageUri ->
//                    file = File(imageUri.path)
                val file = CameraHelper.getMultiPartFrom(bitmap, "BusinessAccountCertificates", this)
                file.let {
                    commercialRegistryFileList.add(file)
                }

            }

        } catch (e: Exception) {
//            println("hhhh " + e.message)
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


    private fun checkAccountData() {
        if (validateUserName() && validateCompanyNameAr() && validateCompanyNameEn()
            && validateCommercialRegisterNo() && validateExpiryDate()
        ) {
            if (selectedCountryId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else if (selectedRegionId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else if (selectedNeighborhoodId == 0) {
                showError(getString(R.string.Please_select, getString(R.string.district)))
            } else if (commercialRegistryFileList.isEmpty()) {
                showError(getString(R.string.you_must_download_the_commercial_registry_file))
            } else {
                addBussinessAccount()
            }
        }

    }

    private fun addBussinessAccount() {
        var fileUserImage: MultipartBody.Part? = null
//        userImageUri?.let {
//            fileUserImage = File(it.path)
//        }

        val file = CameraHelper.getMultiPartFrom(userImageUri!!, "BusinessAccountImage", this)
        fileUserImage = (file)

        businessAccountViewModel.addBusinessAcoount(
            0.toString(),
            userNamee.text.toString().trim(),
            ConstantObjects.logged_userid,
            company_name_ar.text.toString().trim(),
            company_name_en.text.toString().trim(),
            textEmaill.text.toString().trim(),
            etPhoneNumber!!.text.toString().trim(),

            fileUserImage,
            et_web_site.text.toString().trim(),
            Facebook.text.toString().trim(),
            Instagram.text.toString().trim(),
            ic_twitter.text.toString().trim(),
            ic_youtube.text.toString().trim(),
            ic_linkedIn.text.toString().trim(),
            ic_snapshot.text.toString().trim(),
            ic_tickTok.text.toString().trim(),
            selectedCommericalRegisterType,
            commercial_registration_no.text.toString().trim(),
            tvDate.text.toString().trim(),
            TaxNumber.text.toString().trim(),
            etMaroof.text.toString().trim(),
            commercialRegistryFileList,
            selectedCountryId,
            selectedRegionId,
            selectedNeighborhoodId,
            area.text.toString().trim(),
            streetNUmber.text.toString().trim(),
            county_code.text.toString().trim(),
            lat,
            longitude,
            switch_trade._getChecked()
        )
    }


    /**validations*/
    //CompanyName Validation
    private fun validateUserName(): Boolean {
        val emailInput = userNamee!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            userNamee.error = (getString(R.string.Please_enter, getString(R.string.Username)))
            false
        } else {
            userNamee.error = null
            true
        }
    }

    private fun validateCompanyNameAr(): Boolean {
        val emailInput = company_name_ar!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            company_name_ar.error =
                (getString(R.string.Please_enter, getString(R.string.the_company_s_name)))
            false
        } else if (emailInput.length < 5) {
            company_name_ar.error = (getString(
                R.string.please_enter_valid, getString(R.string.the_company_s_name)
            ))
            false
        } else {
            company_name_ar.error = null
            true
        }
    }

    private fun validateCompanyNameEn(): Boolean {
        val emailInput = company_name_en!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            company_name_en.error =
                (getString(R.string.Please_enter, getString(R.string.the_company_s_name)))
            false
        } else if (emailInput.length < 5) {
            company_name_en.error = (getString(
                R.string.please_enter_valid, getString(R.string.the_company_s_name)
            ))
            false
        } else {
            company_name_en.error = null
            true
        }
    }

    private fun validateSignupEmail(): Boolean {
        val emailInput = textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            textEmaill!!.error = (getString(R.string.Please_enter, getString(R.string.Email)))
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textEmaill!!.error = (getString(R.string.Pleaseenteravalidemailaddress))
            false
        } else {

            true
        }
    }

    private fun validateNumber(): Boolean {
        val numberInput = etPhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
            false
        } else {
            true
        }
    }

    private fun validateCommercialRegisterNo(): Boolean {
        val emailInput = commercial_registration_no!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            commercial_registration_no.error = (
                    getString(
                        R.string.Please_enter, getString(R.string.commercial_registration_no)
                    )
                    )
            false
        } else {
            commercial_registration_no.error = null
            true
        }
    }

    private fun validateExpiryDate(): Boolean {
        val emailInput = tvDate!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            tvDate.error = (getString(R.string.Please_select, getString(R.string.expiry_date)))
            false
        } else {
            tvDate.error = null
            true
        }
    }

    // not user validation
    private fun validateCompanyURL(): Boolean {
        val emailInput = et_web_site!!.text.toString()
        return if (emailInput.isEmpty()) {
            et_web_site.error = getString(R.string.Please_enter, getString(R.string.website))
            false
        } else if (!URLUtil.isValidUrl(emailInput)) {
            et_web_site.error = getString(R.string.please_enter_valid, getString(R.string.website))
            false
        } else {
            true
        }
    }

    private fun validateProfilePicture(): Boolean {
//        val emailInput =
//            upload_photo!!.text.toString().trim { it <= ' ' }
//        return if (emailInput.isEmpty()) {
//            showError(
//                getString(
//                    R.string.Please_select,
//                    getString(R.string.profile_picture)
//                )
//            )
//            false
//        } else {
//            true
//        }
        return true
    }


    /***********/
    /***********/
    /***********/
    /***********/
    /***********/
    /***********/
    private fun checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionChecker.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PermissionChecker.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }

    }


    fun pickImageFromGallery() {
        mResults.clear()
        val intent = Intent(this, ImagesSelectorActivity::class.java)
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 1)
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, false)
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults)

        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this@BusinessAccountCreateActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@BusinessAccountCreateActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )

        } else {
            checkGPS()
        }
    }

    private fun openPlacePicker() {
        val intent = Intent(this@BusinessAccountCreateActivity, LocationPickerActivity::class.java)
        chooseLocationLuncher.launch(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_CODE) {
                mResults = data!!.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS)!!
                selectedImagesURI.clear()
                if (isProfileImage) {

                    mResults.forEach {
                        try {
                            profileImageBase64 = HelpFunctions.encodeImage(it)!!
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                } else {

                    mResults.forEach {
                        try {
                            val uri: Uri = Uri.parse(it)
                            businessDocument = HelpFunctions.encodeImage(it)!!
                            selectedImagesURI.add(ImageSelectModel(uri, businessDocument))
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                }


            }
        }

        if (requestCode == REQUEST_GPS_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    openPlacePicker()
                }

                Activity.RESULT_CANCELED -> {

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage(getString(R.string.gps_check))
                    builder.setTitle(getString(R.string.alert)).setCancelable(false)
                        .setPositiveButton(
                            getString(R.string.ok)
                        ) { dialog, id ->
                            checkGPS()
                        }.setNegativeButton(
                            "Cancel"
                        ) { dialog: DialogInterface?, id: Int ->
                            finish()
                        }
                    val alert: AlertDialog = builder.create()
                    alert.show()
                }
            }
        }
    }


    fun addBusinessUser() {

        HelpFunctions.startProgressBar(this)

        val malqa = getRetrofitBuilder()

        val UserNamee = userNamee.text.toString()
        val CompanyName = company_name_ar.text.toString()
        val EmailAddress = textEmaill.text.toString()
        val WebsiteName = et_web_site.text.toString()
        val FacebookLink = Facebook.text.toString()
        val InstagramLink = Instagram.text.toString()
        val youtubeLink = ic_youtube.text.toString()
        val TwitterLink = ic_twitter.text.toString()
        val WhatsappNumber = ic_tickTok.text.toString()
        val SnapchatLInk = ic_snapshot.text.toString()
        // val SelectEmployemntType = employment_type.text.toString()
        val CommercialRegisterNo = commercial_registration_no.text.toString()
        val SelectDate = tvDate.text.toString()
//        val profile_photo = upload_photo.text.toString()
        val TaxNumber = TaxNumber.text.toString()

        val addBusinessUser = BusinessUserRespone.BusinessUser(
            businessName = CompanyName,
            businessEmail = EmailAddress,
            businessURL = WebsiteName,
            businessFacebookURI = FacebookLink,
            businessInstagramURI = InstagramLink,
            businessYoutubeURI = youtubeLink,
            businessTwitterURI = TwitterLink,
            businessWatsappNumber = WhatsappNumber,
            businessSnapchatURI = SnapchatLInk,
            businessType = "",//SelectEmployemntType
            businessRegistrationNumber = CommercialRegisterNo,
            businessRegistrationExpiry = "",
            approvedBy = "",
            approvedOn = "",
            businessGoogleLatLng = "${locationPicker!!.lat},${locationPicker!!.lng}",
            businessLogoPath = profileImageBase64,
            businessPhone = etPhoneNumber.text.toString(),
            id = 0,
            isApproved = true,
            userId = ConstantObjects.logged_userid

        )

        val businessRegistrationExpiry = HelpFunctions.getFormattedDate(
            SelectDate, HelpFunctions.datetimeformat_ddmyyyy, HelpFunctions.datetimeformat_24hrs
        )


        addBusinessUser.businessRegistrationExpiry = businessRegistrationExpiry
        val call = malqa.addBusinesUser(addBusinessUser)

        call.enqueue(object : retrofit2.Callback<GeneralRespone?> {
            override fun onFailure(
                call: retrofit2.Call<GeneralRespone?>?, t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(
                    this@BusinessAccountCreateActivity, "${t.message}", Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: retrofit2.Call<GeneralRespone?>, response: retrofit2.Response<GeneralRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: GeneralRespone = response.body()!!

                        if (!respone.isError) {
                            addBusinessRegisterFile(respone.id)

                        } else {
                            HelpFunctions.dismissProgressBar()

                            Toast.makeText(
                                this@BusinessAccountCreateActivity,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
        })

    }


    //Email Validation


    //CompanyName Validation


    //WebsiteURL Validation


    //EmployementType Validation
    private fun validateEmployementType(): Boolean {
//        val emailInput =
//            employment_type!!.text.toString().trim { it <= ' ' }
//        return if (emailInput.isEmpty()) {
//            showError(getString(R.string.Please_select, getString(R.string.type_of_employment)))
//            false
//        } else {
//            true
//        }
        return true
    }


    //CommercialRegisterNo Validation


    //ProfilePicture Validation


    //ExpiryDate Validation


    //TaxNumber Validation
    private fun validateTaxNumber(): Boolean {
        val emailInput = TaxNumber!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.TaxNumber)))
            false
        } else {
            true
        }
    }

    //CommercialRegisterDoc Validation
    private fun validateCommercialRegisterDoc(): Boolean {
        val emailInput = download_the_commercial_registry_file!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(
                getString(
                    R.string.Please, getString(R.string.download_the_commercial_registry_file)
                )
            )
            false
        } else {
            true
        }
    }


    //handle requested permission result
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//
//            PERMISSION_CODE -> {
//                if (grantResults.size > 0 && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED
//                ) {
//                    pickImageFromGallery()
//                } else {
//                    HelpFunctions.ShowLongToast("Permission denied", this)
//                }
//            }
//            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
//                if (grantResults.size > 0 && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED
//                ) {
//                    checkGPS()
//
//                } else {
//                    HelpFunctions.ShowLongToast("Permission denied", this)
//                }
//            }
//
//        }
//    }

    private fun checkGPS() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                openPlacePicker()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this@BusinessAccountCreateActivity, REQUEST_GPS_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }


    }


    fun addBusinessRegisterFile(businessId: String) {


        val malqa = getRetrofitBuilder()

        val addBusinessDocumentFile = getBusinessRegisterFile.GetDocuments(

            isActive = true,
            documentName = businessDocument,
            uploadedOn = "",
            createdBy = "",
            businessId = businessId,

            )
        val call: retrofit2.Call<GeneralRespone> =
            malqa.addBusinessRegisterFile(addBusinessDocumentFile)

        call.enqueue(object : retrofit2.Callback<GeneralRespone?> {
            override fun onFailure(
                call: retrofit2.Call<GeneralRespone?>?, t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(
                    this@BusinessAccountCreateActivity, "${t.message}", Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: retrofit2.Call<GeneralRespone?>, response: retrofit2.Response<GeneralRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: GeneralRespone = response.body()!!

                        if (!respone.isError) {
                            HelpFunctions.dismissProgressBar()
                            setResult(RESULT_OK, Intent())
                            finish()

                            Toast.makeText(
                                this@BusinessAccountCreateActivity,
                                respone.message,
                                Toast.LENGTH_LONG

                            ).show()

                        } else {
                            HelpFunctions.dismissProgressBar()

                            Toast.makeText(
                                this@BusinessAccountCreateActivity,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }

                }
            }
        })

    }

    override fun onSelectedCountry(
        id: Int, countryName: String, countryFlag: String?, countryCode: String?
    ) {
        tvCode.text = countryCode
        ivFlag.setImageDrawable(null);
        Picasso.get().load(countryFlag).into(ivFlag)
    }


}