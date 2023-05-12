package com.malka.androidappp.newPhase.presentation.addressUser.addAddressActivity

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
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GetAddressResponse
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malka.androidappp.newPhase.presentation.addressUser.AddressViewModel
import com.malka.androidappp.newPhase.presentation.dialogsShared.LocationPermissionDialog
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
            btnAddAddress.text=getString(R.string.Edit)
            toolbar_title.text = getString(R.string.edit_address)
        }else{
            btnAddAddress.text=getString(R.string.Add)
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
//        setListenser()

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
                var currntlat: Double = 0.0
                var currentlng: Double = 0.0
                if (it.lat != null) {
                    currntlat = it.lat!!.toDouble()
                }
                if (it.lng != null) {
                    currentlng = it.lng!!.toDouble()
                }
                println("hhhh "+currntlat+" "+currentlng)
                latLngLocation = LatLng(currntlat, currentlng)
                loadLocation(latLngLocation!!)
            } catch (e: java.lang.Exception) {
                println("hhhh "+e.message)
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
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
        addressViewModel.addUserAddressesListObserver.observe(this) { addAddressResp ->
            if (addAddressResp.status_code == 200) {
               // HelpFunctions.ShowLongToast(addAddressResp.message, this)
                var intent = Intent()
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
        if (latLngLocation == null) {
            readyToSave = false
            HelpFunctions.ShowLongToast(getString(R.string.plaseSelectYourLocation), this)
        }
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

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnMapClickListener { latLng ->
            latLngLocation = LatLng(latLng.latitude, latLng.longitude)
            loadLocation(latLngLocation!!)
        }
        addressObject?.let {
            try {
                var currntlat: Double = 0.0
                var currentlng: Double = 0.0
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

    private fun loadLocation(latLngLocation: LatLng) {
        mMap.clear()
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngLocation, 16f)
        mMap.animateCamera(cameraUpdate)
        val marker = MarkerOptions().position(latLngLocation)
        mMap.addMarker(marker)
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

    fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    /*****************/
    /*****************/
    /*****************/
    /*****************/
    /*****************/
    /*****************/
    /*****************/
    /*****************/

//    private fun setPreValue() {
//        ConstantObjects.userobj?.run {
//            firstname_tv.text = fullName
//            lastname_tv.text = lastname
//            PhoneNumber_tv.text = phone?.substring(4)
//            PhoneNumber_tv._setEndText(phone?.substring(0,4))
//        }
//    }

    private fun initView() {


    }

    fun addressDetailValidation() {


//        if (SelectCity() && validateArea() && validateStreetNumber()
//        ) {
//
//            insertAddress()
//        }


    }


    private fun setListenser() {


//
//        countryContainer._setOnClickListener {
//            val list: ArrayList<SearchListItem> = ArrayList()
//            ConstantObjects.countryList.forEachIndexed { index, country ->
//                list.add(SearchListItem(country.id, country.name))
//            }
//            countryContainer.showSpinner(
//                this,
//                list,
//                getString(R.string.Select, getString(R.string.Country))
//            ) {
//                countryContainer.text = it.title
//                selectedCountry = it
//                regionContainer.text = ""
//                selectedRegion = null
//
//                neighborhoodContainer.text = ""
//                selectedCity = null
//
//
//                ConstantObjects.countryList.filter {
//                    it.id == selectedCountry!!.id
//                }.let {
//                    if (it.size > 0) {
//                        countryContainer._setStartIconImage(it.get(0).flagimglink)
//                    }
//                }
//            }
//
//        }
//        regionContainer._setOnClickListener {
//            if (countryContainer.text.toString().isEmpty()) {
//                (this as BaseActivity).showError(
//                    getString(
//                        R.string.Please_select,
//                        getString(R.string.Country)
//                    )
//                )
//            } else {
//                CommonAPI().getRegion(selectedCountry!!.id, this) {
//                    val list: ArrayList<SearchListItem> = ArrayList()
//                    it.forEachIndexed { index, country ->
//                        list.add(SearchListItem(country.id, country.name))
//                    }
//                    regionContainer.showSpinner(
//                        this,
//                        list,
//                        getString(R.string.Select, getString(R.string.Region))
//                    ) {
//                        regionContainer.text = it.title
//                        selectedRegion = it
//
//
//                        neighborhoodContainer.text = ""
//                        selectedCity = null
//
//                    }
//                }
//            }
//
//        }
//        neighborhoodContainer._setOnClickListener {
//
//            if (regionContainer.text.toString().isEmpty()) {
//                (this as BaseActivity).showError(
//                    getString(
//                        R.string.Please_select,
//                        getString(R.string.Region)
//                    )
//                )
//
//            } else {
//                CommonAPI().getCity(selectedRegion!!.id,this) {
//                    val list: ArrayList<SearchListItem> = ArrayList()
//                    it.forEachIndexed { index, country ->
//                        list.add(SearchListItem(country.id, country.name))
//                    }
//                    neighborhoodContainer.showSpinner(
//                        this,
//                        list,
//                        getString(R.string.Select, getString(R.string.district))
//                    ) {
//                        neighborhoodContainer.text = it.title
//                        selectedCity = it
//                    }
//                }
//            }
//        }

    }


//    private fun SelectCity(): Boolean {
//        val Inputname = neighborhoodContainer!!.text.toString().trim { it <= ' ' }
//        return if (Inputname.isEmpty()) {
//            (this as BaseActivity).showError(
//                getString(
//                    R.string.Please_select,
//                    getString(R.string.City)
//                )
//            )
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun validateArea(): Boolean {
//        val Inputname = area_address!!.text.toString().trim { it <= ' ' }
//        return if (Inputname.isEmpty()) {
//            (this as BaseActivity).showError(
//                getString(
//                    R.string.Please_enter,
//                    getString(R.string.Area)
//                )
//            )
//            false
//        } else {
//            true
//        }
//    }
//
//    private fun validateStreetNumber(): Boolean {
//        val Inputname = streetnumber!!.text.toString().trim { it <= ' ' }
//        return if (Inputname.isEmpty()) {
//            (this as BaseActivity).showError(
//                getString(
//                    R.string.Please_enter,
//                    getString(R.string.StreetNumber)
//                )
//            )
//            false
//        } else {
//            true
//        }
//    }
//
//
//    fun insertAddress() {
//
//        HelpFunctions.startProgressBar(this)
//
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//
//        val ftname = firstname_tv.text.toString()
//        val ltname = lastname_tv.text.toString()
//        val phonenumber = PhoneNumber_tv._getEndText() + PhoneNumber_tv.text.toString()
//
//        val address = "${area_address.text.toString()} - ${streetnumber.text.toString()}"
//        val selectCountry = countryContainer.text.toString()
//        val selectRegion = regionContainer.text.toString()
//        val selectCity = neighborhoodContainer.text.toString()
//
//
//        val addAddress = GetAddressResponse.AddressModel(
//            firstName = ftname,
//            lastName = ltname,
//            mobileNo = phonenumber,
//            address = address,
//            userId = ConstantObjects.logged_userid,
//            country = selectCountry,
//            region = selectRegion,
//            city = selectCity,
//            id = "",
//            createdBy = "",
//            createdOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
//            updatedBy = "",
//            updatedOn = HelpFunctions.GetCurrentDateTime(HelpFunctions.datetimeformat_24hrs_milliseconds),
//            isActive = true,
//
//            )
//
//        if (isEdit) {
//            addAddress.id = oldAddress.id
//
//            val call: Call<GeneralRespone> = malqa.updateAddress(addAddress)
//            call.enqueue(object : Callback<GeneralRespone?> {
//                override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
//                    HelpFunctions.dismissProgressBar()
//                }
//
//                override fun onResponse(
//                    call: Call<GeneralRespone?>,
//                    response: retrofit2.Response<GeneralRespone?>
//                ) {
//                    if (response.isSuccessful) {
//
//                        if (response.body() != null) {
//
//                            val respone: GeneralRespone = response.body()!!
//                            if (respone.status_code==200) {
//                                setResult(Activity.RESULT_OK, Intent())
//                                finish()
//                            } else {
//
//                                HelpFunctions.ShowLongToast(
//                                    getString(R.string.ErrorOccur),
//                                    this@AddAddressActivity
//                                )
//
//                            }
//                        }
//
//                    }
//                    HelpFunctions.dismissProgressBar()
//                }
//            })
//        } else {
//            val call: Call<GeneralRespone> = malqa.insertAddress(addAddress)
//
//            call.enqueue(object : Callback<GeneralRespone?> {
//                override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
//                    HelpFunctions.dismissProgressBar()
//                }
//
//                override fun onResponse(
//                    call: Call<GeneralRespone?>,
//                    response: retrofit2.Response<GeneralRespone?>
//                ) {
//                    if (response.isSuccessful) {
//
//                        if (response.body() != null) {
//
//                            val respone: GeneralRespone = response.body()!!
//                            if (respone.status_code==200) {
//                                setResult(Activity.RESULT_OK, Intent())
//                                finish()
//                            } else {
//
//                                HelpFunctions.ShowLongToast(
//                                    getString(R.string.ErrorOccur),
//                                    this@AddAddressActivity
//                                )
//
//                            }
//                        }
//
//                    }
//                    HelpFunctions.dismissProgressBar()
//                }
//            })
//        }
//
//
//
//    }

}