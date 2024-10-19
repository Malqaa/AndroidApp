package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.addBusinessAccount

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityBusinessSignupBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.LocationPickerModel
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.PickImageMethodsDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.BusinessAccountViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.mapActivity.MapActivity
import com.malqaa.androidappp.newPhase.utils.BetterActivityResult
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.ImagePicker
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.SetOnImagePickedListeners
import com.malqaa.androidappp.newPhase.utils.activitiesMain.placePicker.LocationPickerActivity
import com.malqaa.androidappp.newPhase.utils.helper.CameraHelper
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.squareup.picasso.Picasso
import com.zfdang.multiple_images_selector.SelectorSettings
import okhttp3.MultipartBody
import kotlin.math.roundToInt

class BusinessAccountCreateActivity : BaseActivity<ActivityBusinessSignupBinding>(),
    CountryDialog.GetSelectedCountry,
    AdapterCommercialImage.IRemoveImg,
    PickImageMethodsDialog.OnAttachedImageMethodSelected {

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
    var listBitMap: ArrayList<Uri> = arrayListOf()
    private val selectedImagesURI: ArrayList<ImageSelectModel> = ArrayList()
    private val IMAGE_PICK_CODE = 1000

    private val PERMISSION_CODE = 1001
    private var mResults: ArrayList<String> = ArrayList()
    var locationPicker: LocationPickerModel? = null
    private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    private val REQUEST_GPS_SETTINGS = 107
    lateinit var countryDialog: CountryDialog
    private val chooseLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                locationPicker = Gson().fromJson(
                    result.data!!.getStringExtra(ConstantObjects.data),
                    LocationPickerModel::class.java
                )
            }
        }

    lateinit var economicalRegistrationTypeAdapter: EconomicalRegistrationTypeAdapter
    var selectedCountryId: Int = 0
    var selectedRegionId: Int = 0
    var selectedNeighborhoodId: Int = 0

    private val getLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                lat = result.data?.getDoubleExtra("lat", 0.0) ?: 0.0
                longitude = result.data?.getDoubleExtra("longitude", 0.0) ?: 0.0
                binding.locatingTheCompany.text = "$lat,$longitude"
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
    private lateinit var adapter: AdapterCommercialImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityBusinessSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdapterCommercialImage(listBitMap, this)
        countryDialog = CountryDialog(this, this)
        commercialRegistryFileList = ArrayList()
        setupCommircalRegisteration()
        setViewClickListeners()
        setupViewModel()


        binding.commercialRegistrationNo._setMaxLength(15)
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
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
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
        val commricaalRegisterationTyesList: ArrayList<String> = ArrayList()
        commricaalRegisterationTyesList.add(getString(R.string.commercialRegister))
        commricaalRegisterationTyesList.add(getString(R.string.freelanceCertificate))
        economicalRegistrationTypeAdapter =
            EconomicalRegistrationTypeAdapter(this, commricaalRegisterationTyesList)
        binding.spinnerCommirecalRegisterationType.adapter = economicalRegistrationTypeAdapter
        binding.spinnerCommirecalRegisterationType.onItemSelectedListener =
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
        binding.ivPickUserImage.setOnClickListener {
            imageType = 1
            openCameraChooser()
        }

        binding.downloadTheCommercialRegistryFile.setOnClickListener {
            imageType = 2
            openCameraChooser()

        }
        binding.btnOpenCountry.setOnClickListener {
            countryDialog.show()
        }
        binding.icTwitterSelect.setOnClickListener {
            twitter = !twitter
            if (twitter) {
                binding.icTwitterSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                binding.icTwitter.show()
            } else {
                binding.icTwitterSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                binding.icTwitter.hide()
            }

        }
        binding.icYoutubeSelect.setOnClickListener {
            youtube = !youtube
            if (youtube) {
                binding.icYoutubeSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                binding.icYoutube.show()
            } else {
                binding.icYoutubeSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                binding.icYoutube.hide()
            }

        }
        binding.icLinkedin.setOnClickListener {
            linkedin = !linkedin
            if (linkedin) {
                binding.icLinkedIn.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                binding.icLinkedIn.show()
            } else {
                binding.icLinkedIn.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                binding.icLinkedIn.hide()
            }

        }
        binding.icSnapshotSelect.setOnClickListener {
            snapShot = !snapShot
            if (snapShot) {
                binding.icSnapshotSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                binding.icSnapshot.show()
            } else {
                binding.icSnapshotSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                binding.icSnapshot.hide()
            }

        }
        binding.icTicktokSelect.setOnClickListener {
            itickTok = !itickTok
            if (itickTok) {
                binding.icTicktokSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                binding.icTickTok.show()
            } else {
                binding.icTicktokSelect.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                binding.icTickTok.hide()
            }

        }
        binding.tvDate.setOnClickListener {
            binding.tvDate.error = null
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectDate ->
                binding.tvDate.text = "$selectDate "
                selectdate = selectDate

            }
            dateDialog.show(fm!!, "")
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
        binding.locatingTheCompany.setOnClickListener {
            getLocationLauncher.launch(Intent(this, MapActivity::class.java))
        }

        binding.btnSaveBussinessAccount.setOnClickListener() {
            checkAccountData()
        }

        binding.busiSignup2Btn.setOnClickListener {
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
                binding.countryContainer.text = countryName
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
                    /**setRegionData*/
                    selectedRegionId = id
                    binding.regionContainer.text = regionName
                    /**resetNeighborhood*/
                    /**resetNeighborhood*/
                    selectedNeighborhoodId = 0
                    binding.neighborhoodContainer.text = null
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
                    binding.neighborhoodContainer.text = neighborhoodName
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
                    if (activityResult.resultCode == RESULT_OK) {
                        imagePicker.handleActivityResult(
                            activityResult.resultCode, requestCode, activityResult.data
                        )
                    }
                }
            }
        })
        if (attachedMethod == ConstantObjects.CAMERA) {
            imagePicker.choosePicture(ImagePicker.CAMERA)
        } else {
            openGallery(startForResult)
        }
    }


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setImage(it.data?.data!!)
            }
        }

    fun openGallery(startForResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    override fun onDeleteImage() {
    }

    private fun setImage(imageUri: Uri) {
        listBitMap.add(imageUri)
        try {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
            val scaleBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.4f).roundToInt(),
                (bitmap.height * 0.4f).roundToInt(),
                true
            )
            adapter.updateAdapter(listBitMap)
            binding.commercialImgRecycle.visibility = View.VISIBLE
            binding.commercialImgRecycle.adapter = adapter
            if (imageType == 1) {
                getPicassoInstance().load(imageUri).into(binding.ivUserImageBusiness)
                userImageUri = bitmap
            } else if (imageType == 2) {
                val file =
                    CameraHelper.getMultiPartFrom(bitmap, "BusinessAccountCertificates", this)
                file.let {
                    commercialRegistryFileList.add(file)
                }

            }

        } catch (e: Exception) {
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
                addBusinessAccount()
            }
        }

    }

    private fun addBusinessAccount() {
        var fileUserImage: MultipartBody.Part? = null
        if (userImageUri != null) {
            val file = CameraHelper.getMultiPartFrom(userImageUri!!, "BusinessAccountImage", this)
            fileUserImage = (file)
        }


        businessAccountViewModel.addBusinessAccount(
            0.toString(),
            binding.userNamee.text.toString().trim(),
            ConstantObjects.logged_userid,
            binding.companyNameAr.text.toString().trim(),
            binding.companyNameEn.text.toString().trim(),
            binding.textEmaill.text.toString().trim(),
            binding.etPhoneNumber!!.text.toString().trim(),

            fileUserImage,
            binding.etWebSite.text.toString().trim(),
            binding.Facebook.text.toString().trim(),
            binding.Instagram.text.toString().trim(),
            binding.icTwitter.text.toString().trim(),
            binding.icYoutube.text.toString().trim(),
            binding.icLinkedIn.text.toString().trim(),
            binding.icSnapshot.text.toString().trim(),
            binding.icTickTok.text.toString().trim(),
            selectedCommericalRegisterType,
            binding.commercialRegistrationNo.text.toString().trim(),
            binding.tvDate.text.toString().trim(),
            binding.TaxNumber.text.toString().trim(),
            binding.etMaroof.text.toString().trim(),
            commercialRegistryFileList,
            selectedCountryId,
            selectedRegionId,
            selectedNeighborhoodId,
            binding.area.text.toString().trim(),
            binding.streetNUmber.text.toString().trim(),
            binding.countyCode.text.toString().trim(),
            lat,
            longitude,
            binding.switchTrade._getChecked()
        )
    }


    /**validations*/
    //CompanyName Validation
    private fun validateUserName(): Boolean {
        val emailInput = binding.userNamee!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.userNamee.error =
                (getString(R.string.Please_enter, getString(R.string.Username)))
            false
        } else {
            binding.userNamee.error = null
            true
        }
    }

    private fun validateCompanyNameAr(): Boolean {
        val emailInput = binding.companyNameAr!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.companyNameAr.error =
                (getString(R.string.Please_enter, getString(R.string.the_company_s_name)))
            false
        } else if (emailInput.length < 5) {
            binding.companyNameAr.error = (getString(
                R.string.please_enter_valid, getString(R.string.the_company_s_name)
            ))
            false
        } else {
            binding.companyNameAr.error = null
            true
        }
    }

    private fun validateCompanyNameEn(): Boolean {
        val emailInput = binding.companyNameEn!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.companyNameEn.error =
                (getString(R.string.Please_enter, getString(R.string.the_company_s_name)))
            false
        } else if (emailInput.length < 5) {
            binding.companyNameEn.error = (getString(
                R.string.please_enter_valid, getString(R.string.the_company_s_name)
            ))
            false
        } else {
            binding.companyNameEn.error = null
            true
        }
    }

    private fun validateCommercialRegisterNo(): Boolean {
        val emailInput = binding.commercialRegistrationNo!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.commercialRegistrationNo.error = (
                    getString(
                        R.string.Please_enter, getString(R.string.commercial_registration_no)
                    )
                    )
            false
        } else {
            binding.commercialRegistrationNo.error = null
            true
        }
    }

    private fun validateExpiryDate(): Boolean {
        val emailInput = binding.tvDate!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            binding.tvDate.error =
                (getString(R.string.Please_select, getString(R.string.expiry_date)))
            false
        } else {
            binding.tvDate.error = null
            true
        }
    }

    private fun openPlacePicker() {
        val intent = Intent(this@BusinessAccountCreateActivity, LocationPickerActivity::class.java)
        chooseLocationLauncher.launch(intent)
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
                    } catch (_: IntentSender.SendIntentException) {
                    } catch (_: ClassCastException) {
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }


    }

    override fun onSelectedCountry(
        id: Int, countryName: String, countryFlag: String?, countryCode: String?
    ) {
        binding.tvCode.text = countryCode
        binding.ivFlag.setImageDrawable(null)
        Picasso.get()
            .load(countryFlag)
            .into(binding.ivFlag)
    }

    override fun onDestroy() {
        super.onDestroy()
        businessAccountViewModel.closeAllCall()
    }

    override fun onClickClose(pos: Int) {
        listBitMap.removeAt(pos)
        adapter.updateAdapter(listBitMap)
        commercialRegistryFileList.removeAt(pos)
    }

}