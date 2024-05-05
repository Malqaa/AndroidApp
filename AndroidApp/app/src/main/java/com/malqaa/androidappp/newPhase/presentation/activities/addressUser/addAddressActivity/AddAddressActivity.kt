package com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addAddressActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hbb20.CountryCodePicker
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GetAddressResponse
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.AddressViewModel
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.LocationPermissionDialog
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_list_details_add_product.*
import kotlinx.android.synthetic.main.add_address_activity.*
import kotlinx.android.synthetic.main.add_address_activity.countryCodePicker
import kotlinx.android.synthetic.main.add_address_activity.etPhoneNumber
import kotlinx.android.synthetic.main.toolbar_main.*


class AddAddressActivity : BaseActivity(), OnMapReadyCallback {

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null
    var isEdit: Boolean = false
    lateinit var oldAddress: GetAddressResponse.AddressModel
    var addressObject: AddressItem? = null

    /***/
    private var isPhoneNumberValid = false


    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var latLngLocation: LatLng? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var addressViewModel: AddressViewModel

    private var locationPermissionDialog: LocationPermissionDialog? = null
    var PERMISSIONS: Array<String> =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    val REQUEST_CHECK_PERMISSION_LOCATION = 2000
    val REQUEST_CHECK_SETTINGS_LOCATIONS = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_address_activity)

        setViewClickListeners()
        setupCountryCodePiker()
        setupAddressViewModel()
        setupCuurentLocation()
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationPermissionDialog = LocationPermissionDialog(this)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        addressObject = intent.getParcelableExtra(ConstantObjects.addressKey)

        if (isEdit) {
            setAddressData()
            btnAddAddress.text = getString(R.string.Edit)
            toolbar_title.text = getString(R.string.edit_address)
        } else {
            btnAddAddress.text = getString(R.string.Add)
            toolbar_title.text = getString(R.string.add_a_new_address)
        }

//
//        if (isEdit) {
//            val addressObject = intent.getStringExtra("addressObject")
//            oldAddress = Gson().fromJson(addressObject, GetAddressResponse.AddressModel::class.java)
//
//
//            oldAddress.run {
//                countryContainer.text = country
//                regionContainer.text = region
//                neighborhoodContainer.text = city
//                area_address.text = address
//
//                ConstantObjects.countryList.forEach {
//                    if (mobileNo.startsWith(it.countryCode!!)){
//                        PhoneNumber_tv.text = mobileNo.replace(it.countryCode!!,"")
//                        PhoneNumber_tv._setEndText(it.countryCode)
//
//                    }
//                }
//                firstname_tv.text = firstName
//                lastname_tv.text = lastName
//            }
//
//            add_button.text = getString(R.string.Confirm)
//            toolbar_title.text = getString(R.string.update_address)
//            ConstantObjects.countryList.filter {
//                it.name==countryContainer.text.toString()
//            }.let {
//                if(it.isNotEmpty()){
//                    it.get(0).run {
//                        selectedCountry = SearchListItem(id, name)
//
//                        CommonAPI().getRegion(selectedCountry!!.id, this@AddAddressActivity) {
//                            it.filter {
//                                it.name==regionContainer.text.toString()
//                            }.let {
//                                if (it.isNotEmpty()) {
//                                    it.get(0).run {
//                                        selectedRegion = SearchListItem(id, name)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//
//        } else {
//
//            toolbar_title.text = getString(R.string.add_a_new_address)
//            ConstantObjects.userobj?.let {
//                setPreValue()
//            } ?: kotlin.run {
//                CommonAPI().GetUserInfo(this, ConstantObjects.logged_userid) {
//                    setPreValue()
//                }
//            }
//
//        }
//
//        add_button.setOnClickListener {
//
//            addressDetailValidation()
//
//        }
//
//        initView()
//        setListener()

    }

    private fun setAddressData() {
        addressObject?.let {
            tvAddressTitle.text = it.title ?: ""
            tvAddress.text = it.location ?: ""
            tvStreet.text = it.street ?: ""
            tvBuildingNumber.text = it.building ?: ""
            tvFloor.text = it.floor ?: ""
            tvApartmentNumber.text = it.appartment ?: ""
            try {
                countryCodePicker.fullNumber = it.phone
            } catch (e: Exception) {

            }
            try {
                var currntlat = 0.0
                var currentlng = 0.0
                if (it.lat != null) {
                    currntlat = it.lat!!.toDouble()
                }
                if (it.lng != null) {
                    currentlng = it.lng!!.toDouble()
                }
                latLngLocation = LatLng(currntlat, currentlng)
                loadLocation(latLngLocation!!)
            } catch (e: java.lang.Exception) {
                println("hhhh " + e.message)
            }


        }
    }

    private fun setupAddressViewModel() {
        addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        addressViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        addressViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        addressViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, this)
                }else if (it.message2 != null) {
                    HelpFunctions.ShowLongToast(it.message2!!, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        addressViewModel.addUserAddressesListObserver.observe(this) { addAddressResp ->
            if (addAddressResp.status_code == 200) {
                addAddressResp.addressTitle
                // HelpFunctions.ShowLongToast(addAddressResp.message, this)
                val intent = Intent()
                setResult(RESULT_OK, intent)
                finish()
            } else {
                HelpFunctions.ShowLongToast(addAddressResp.message, this)
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

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        containerLocation.setOnClickListener {
            checkLocationSetting()
        }
        btnAddAddress.setOnClickListener {
            checkDataToAddAddress()
        }
    }

    private fun checkDataToAddAddress() {
        var readyToSave = true
//        if (latLngLocation == null) {
//            readyToSave = false
//            HelpFunctions.ShowLongToast(getString(R.string.plaseSelectYourLocation), this)
//        }
        if (tvAddressTitle.text?.trim().toString() == "") {
            readyToSave = false
            tvAddressTitle.error = getString(R.string.addressTitle)
        }
        if (tvAddress.text?.trim().toString() == "") {
            readyToSave = false
            tvAddress.error = getString(R.string.address)
        }
        if (tvStreet.text?.trim().toString() == "") {
            readyToSave = false
            tvStreet.error = getString(R.string.street)
        }
        if (tvBuildingNumber.text?.trim().toString() == "") {
            readyToSave = false
            tvBuildingNumber.error = getString(R.string.buildingNumber)
        }
        if (tvFloor.text?.trim().toString() == "") {
            readyToSave = false
            tvFloor.error = getString(R.string.floorNumber)
        }
        if (tvApartmentNumber.text?.trim().toString() == "") {
            readyToSave = false
            tvApartmentNumber.error = getString(R.string.apartmentNumber)
        }
        if (!isPhoneNumberValid) {
            readyToSave = false
            etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
        }
        if (readyToSave) {
            if (isEdit) {
                addressObject?.id?.let {
                    addressViewModel.editUserAddress(
                        id = it,
                        title = tvAddressTitle.text?.trim().toString(),
                        location = tvAddress.text?.trim().toString(),
                        street = tvStreet.text?.trim().toString(),
                        appartment = tvApartmentNumber.text?.trim().toString(),
                        floor = tvFloor.text?.trim().toString(),
                        building = tvBuildingNumber.text?.trim().toString(),
                        lat = latLngLocation?.latitude.toString(),
                        lng = latLngLocation?.longitude.toString(),
                        phone = countryCodePicker.fullNumberWithPlus
                    )
                }
            } else {
                addressViewModel.addUserAddress(
                    title = tvAddressTitle.text?.trim().toString(),
                    location = tvAddress.text?.trim().toString(),
                    street = tvStreet.text?.trim().toString(),
                    appartment = tvApartmentNumber.text?.trim().toString(),
                    floor = tvFloor.text?.trim().toString(),
                    building = tvBuildingNumber.text?.trim().toString(),
                    lat = latLngLocation?.latitude.toString(),
                    lng = latLngLocation?.longitude.toString(),
                    phone = countryCodePicker.fullNumberWithPlus
                )
            }
        }
    }


    /**Map set ups**/
    private fun setupCuurentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                try {
                    latLngLocation = LatLng(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    )
                    loadLocation(latLngLocation!!)
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                    // println("hhhh " + latLngLocation.latitude + " " + latLngLocation.longitude)
                } catch (e: java.lang.Exception) {
                }
            }
        }
    }

    private fun loadLocation(latLngLocation: LatLng) {
        mMap.clear()
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngLocation, 16f)
        mMap.animateCamera(cameraUpdate)
        val marker = MarkerOptions().position(latLngLocation)
        mMap.addMarker(marker)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnMapClickListener { latLng ->
            latLngLocation = LatLng(latLng.latitude, latLng.longitude)
            loadLocation(latLngLocation!!)
        }
        addressObject?.let {
            try {
                var currntlat = 0.0
                var currentlng = 0.0
                if (it.lat != null) {
                    currntlat = it.lat!!.toDouble()
                }
                if (it.lng != null) {
                    currentlng = it.lng!!.toDouble()
                }
                latLngLocation = LatLng(currntlat, currentlng)
                loadLocation(latLngLocation!!)
            } catch (e: java.lang.Exception) {
            }
        }
    }


    //----------------------------- location setting------------
    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                checkLocationPermission()
            } else if (activityResult.resultCode == RESULT_CANCELED) {
                checkLocationSetting()
            }
        }

    private fun checkLocationSetting() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener(this) { _: LocationSettingsResponse? ->
                checkLocationPermission()
            }
            .addOnFailureListener(this) { exception: Exception? ->
                if (exception is ResolvableApiException) {
                    // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
//                        val resolvable = ex as ResolvableApiException
//                        resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS_LOCATIONS)
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.resolution).build()
                        resolutionForResult.launch(intentSenderRequest)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
    }

    private fun checkLocationPermission() {
        if (hasPermissions(this, permissions = PERMISSIONS)) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS, REQUEST_CHECK_PERMISSION_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CHECK_PERMISSION_LOCATION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                locationPermissionDialog?.let {
                    if (!it.isShowing) {
                        it.show()
                    }
                }
            } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(
                    permissions[0]
                )
            ) {
                locationPermissionDialog?.let {
                    if (!it.isShowing) {
                        it.show()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            mLocationCallback, Looper.getMainLooper()
        )
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onDestroy() {
        super.onDestroy()
        addressViewModel.closeAllCall()
    }
}