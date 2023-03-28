package com.malka.androidappp.fragments.activities_main.business_signup

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.FragmentManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.place_picker.LocationPickerActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.ImageSelectModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.zfdang.multiple_images_selector.ImagesSelectorActivity
import com.zfdang.multiple_images_selector.SelectorSettings
import kotlinx.android.synthetic.main.activity_business_signup.*


class BusinessAccountCreate : BaseActivity() {
    var youtube = false
    var twitter = false
    var whatsApp = false
    var snapShot = false
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

    val chooseLocationLuncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                locationPicker =
                    Gson().fromJson(result.data!!.getStringExtra(ConstantObjects.data), LocationPickerModel::class.java)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_signup)

        business_signup2_button.setOnClickListener() {
            if(profileImageBase64.isEmpty()){
                showError(getString(R.string.Please_enter, getString(R.string.profile_picture)))
            }else{
                if (validateUserName() && validateCompanyName() && validateSignupEmail() && validateNumber() && validateCompanyURL() && validateProfilePicture() && validateEmployementType() && validateCommercialRegisterNo()
                    && validateExpiryDate() && validateTaxNumber() && validateCommercialRegisterDoc()
                ) {

                    if (selectedImagesURI.size == 0) {
                        showError(
                            getString(
                                R.string.Please_select,
                                getString(R.string.download_the_commercial_registry_file)
                            )
                        )
                    } else {

                        if(locationPicker==null){
                            showError(
                                getString(
                                    R.string.Please_select,
                                    getString(R.string.locating_the_company)
                                )
                            )
                        }else{
                            addBusinessUser()
                        }

                    }
                }
            }


        }

        employment_type.setOnClickListener {

            val list: ArrayList<SearchListItem> = ArrayList()

            list.add(SearchListItem(1, getString(R.string.Commercial_Record)))
            list.add(SearchListItem(2, getString(R.string.Free_work)))
            list.add(SearchListItem(3, getString(R.string.A_favour)))

            employment_type.showSpinner(
                this,
                list,
                getString(R.string.Select, getString(R.string.type_of_employment))
            ) {
                employment_type.text = it.title

            }
        }


        download_the_commercial_registry_file.setOnClickListener {
            isProfileImage = false
            checkPermissions()

        }



        date.setOnClickListener {
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                date.text = "$selectdate_ "
                selectdate = selectdate_

            }
            dateDialog.show(fm!!, "")
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
        ic_whatsapp_select.setOnClickListener {
            whatsApp = !whatsApp
            if (whatsApp) {
                ic_whatsapp_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_enable)
                ic_whatsapp.show()
            } else {
                ic_whatsapp_select.background =
                    ContextCompat.getDrawable(this, R.drawable.circle_bg_disable)
                ic_whatsapp.hide()
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

        locating_the_company.setOnClickListener {
            checkLocationPermission()
        }


        busi_signup2_btn.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        upload_photo.setOnClickListener {
            isProfileImage = true
            checkPermissions()

        }


    }

    private fun checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionChecker.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
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
                this@BusinessAccountCreate,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@BusinessAccountCreate,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )

        }else{
            checkGPS()
        }
    }

    private fun openPlacePicker() {
        val intent = Intent(this@BusinessAccountCreate, LocationPickerActivity::class.java)
        chooseLocationLuncher.launch(intent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_CODE) {
                mResults = data!!.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS)!!
                selectedImagesURI.clear()
                if (isProfileImage){

                    mResults.forEach {
                        try {
                            profileImageBase64 = HelpFunctions.encodeImage(it)!!
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }

                }else{

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
                    builder.setTitle(getString(R.string.alert))
                        .setCancelable(false)
                        .setPositiveButton(
                            getString(R.string.ok)
                        ) { dialog, id ->
                            checkGPS()
                        }
                        .setNegativeButton(
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

        val malqa = RetrofitBuilder.GetRetrofitBuilder()

        val UserNamee = userNamee.text.toString()
        val CompanyName = company_name.text.toString()
        val EmailAddress = textEmaill.text.toString()
        val WebsiteName = busi_signup3_edittext2.text.toString()
        val FacebookLink = Facebook.text.toString()
        val InstagramLink = Instagram.text.toString()
        val youtubeLink = ic_youtube.text.toString()
        val TwitterLink = ic_twitter.text.toString()
        val WhatsappNumber = ic_whatsapp.text.toString()
        val SnapchatLInk = ic_snapshot.text.toString()
        val SelectEmployemntType = employment_type.text.toString()
        val CommercialRegisterNo = commercial_registration_no.text.toString()
        val SelectDate = date.text.toString()
        val profile_photo = upload_photo.text.toString()
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
            businessType = SelectEmployemntType,
            businessRegistrationNumber = CommercialRegisterNo,
            businessRegistrationExpiry = "",
            approvedBy = "",
            approvedOn = "",
            businessGoogleLatLng = "${locationPicker!!.lat},${locationPicker!!.lng}",
            businessLogoPath = profileImageBase64,
            businessPhone = PhoneNumber.text.toString(),
            id = 0,
            isApproved = true,
            userId = ConstantObjects.logged_userid

        )

        val businessRegistrationExpiry = HelpFunctions.getFormattedDate(
            SelectDate,
            HelpFunctions.datetimeformat_ddmyyyy,
            HelpFunctions.datetimeformat_24hrs
        )


        addBusinessUser.businessRegistrationExpiry = businessRegistrationExpiry
        val call= malqa.addBusinesUser(addBusinessUser)

        call.enqueue(object : retrofit2.Callback<GeneralRespone?> {
            override fun onFailure(
                call: retrofit2.Call<GeneralRespone?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@BusinessAccountCreate, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<GeneralRespone?>,
                response: retrofit2.Response<GeneralRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: GeneralRespone = response.body()!!

                        if (!respone.isError) {
                            addBusinessRegisterFile(respone.id)

                        } else {
                            HelpFunctions.dismissProgressBar()

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
        })

    }


    private fun validateNumber(): Boolean {
        val numberInput =
            PhoneNumber!!.text.toString().trim { it <= ' ' }
        return if (numberInput.isEmpty()) {
            showError(getString(R.string.PleaseenteravalidPhoneNumber))
            false
        } else if (!Patterns.PHONE.matcher(numberInput).matches()) {
            showError(getString(R.string.PleaseenteravalidPhoneNumber))
            false
        }  else {
            true
        }
    }


    //Email Validation
    private fun validateSignupEmail(): Boolean {
        val emailInput =
            textEmaill!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {

            showError(getString(R.string.Please_enter, getString(R.string.Email)))
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            showError(getString(R.string.Pleaseenteravalidemailaddress))
            false
        } else {

            true
        }
    }

    //CompanyName Validation
    private fun validateUserName(): Boolean {
        val emailInput =
            userNamee!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Username)))
            false
        } else {
            true
        }
    }


    //CompanyName Validation
    private fun validateCompanyName(): Boolean {
        val emailInput =
            company_name!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.the_company_s_name)))
            false
        }else if (emailInput.length<5) {
            showError(getString(R.string.please_enter_valid, getString(R.string.the_company_s_name)))
            false
        } else {
            true
        }
    }


    //WebsiteURL Validation
    private fun validateCompanyURL(): Boolean {
        val emailInput =
            busi_signup3_edittext2!!.text.toString()
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.website)))
            false
        } else if (!URLUtil.isValidUrl(emailInput)) {
            showError(getString(R.string.please_enter_valid, getString(R.string.website)))
            false
        } else {
            true
        }
    }


    //EmployementType Validation
    private fun validateEmployementType(): Boolean {
        val emailInput =
            employment_type!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.type_of_employment)))
            false
        } else {
            true
        }
    }


    //CommercialRegisterNo Validation
    private fun validateCommercialRegisterNo(): Boolean {
        val emailInput =
            commercial_registration_no!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(
                getString(
                    R.string.Please_enter,
                    getString(R.string.commercial_registration_no)
                )
            )
            false
        } else {
            true
        }
    }

    //ProfilePicture Validation
    private fun validateProfilePicture(): Boolean {
        val emailInput =
            upload_photo!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(
                getString(
                    R.string.Please_select,
                    getString(R.string.profile_picture)
                )
            )
            false
        } else {
            true
        }
    }


    //ExpiryDate Validation
    private fun validateExpiryDate(): Boolean {
        val emailInput =
            date!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.expiry_date)))
            false
        } else {
            true
        }
    }


    //TaxNumber Validation
    private fun validateTaxNumber(): Boolean {
        val emailInput =
            TaxNumber!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(getString(R.string.Please_select, getString(R.string.TaxNumber)))
            false
        } else {
            true
        }
    }

    //CommercialRegisterDoc Validation
    private fun validateCommercialRegisterDoc(): Boolean {
        val emailInput =
            download_the_commercial_registry_file!!.text.toString().trim { it <= ' ' }
        return if (emailInput.isEmpty()) {
            showError(
                getString(
                    R.string.Please,
                    getString(R.string.download_the_commercial_registry_file)
                )
            )
            false
        } else {
            true
        }
    }



    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    HelpFunctions.ShowLongToast("Permission denied", this)
                }
            }
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    checkGPS()

                } else {
                    HelpFunctions.ShowLongToast("Permission denied", this)
                }
            }

        }
    }

    private fun checkGPS() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task =
            client.checkLocationSettings(builder.build())
        task.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                openPlacePicker()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable =
                            exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this@BusinessAccountCreate,
                            REQUEST_GPS_SETTINGS
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


        val malqa = RetrofitBuilder.GetRetrofitBuilder()

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
                call: retrofit2.Call<GeneralRespone?>?,
                t: Throwable
            ) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@BusinessAccountCreate, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: retrofit2.Call<GeneralRespone?>,
                response: retrofit2.Response<GeneralRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: GeneralRespone = response.body()!!

                        if (!respone.isError) {
                            HelpFunctions.dismissProgressBar()
                            setResult(RESULT_OK, Intent())
                            finish()

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG

                            ).show()

                        } else {
                            HelpFunctions.dismissProgressBar()

                            Toast.makeText(
                                this@BusinessAccountCreate,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }

                }
            }
        })

    }


}