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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hbb20.CountryCodePicker
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.AddAddressActivityBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.countryResp.Country
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.toSearchListItem
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.AddressViewModel
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.LocationPermissionDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.countryDialog.CountryDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.neighborhoodDialog.NeighborhoodDialog
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.regionDialog.RegionDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.searchdialog.SearchListItem
import com.yariksoffice.lingver.Lingver

class AddAddressActivity : BaseActivity<AddAddressActivityBinding>(), OnMapReadyCallback {

    var isEdit: Boolean = false
    var addressObject: AddressItem? = null

    private var isPhoneNumberValid = false
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var latLngLocation: LatLng? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var addressViewModel: AddressViewModel

    var selectedCountry: SearchListItem? = null
    var selectedRegion: SearchListItem? = null
    var selectedCity: SearchListItem? = null

    private var locationPermissionDialog: LocationPermissionDialog? = null
    var PERMISSIONS: Array<String> =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    val REQUEST_CHECK_PERMISSION_LOCATION = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = AddAddressActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            binding.btnAddAddress.text = getString(R.string.Edit)
            binding.toolbarMain.toolbarTitle.text = getString(R.string.edit_address)
        } else {
            binding.btnAddAddress.text = getString(R.string.Add)
            binding.toolbarMain.toolbarTitle.text = getString(R.string.add_a_new_address)
        }
    }

    private fun openCountryDialog() {
        val countryDialog = CountryDialog(this, object : CountryDialog.GetSelectedCountry {
            override fun onSelectedCountry(
                id: Int,
                countryName: String,
                countryFlag: String?,
                countryCode: String?
            ) {
                selectedCountry = SearchListItem(id, countryName)
                binding.countryContainer.text = countryName
                binding.countryContainer._setStartIconImage(countryFlag)
                selectedRegion = null
                binding.regionContainer.text = null
                selectedCity = null
                binding.neighborhoodContainer.text = null
            }
        })
        countryDialog.show()
    }

    private fun openRegionDialog() {
        val regionDialog =
            RegionDialog(this, selectedCountry!!.id, object : RegionDialog.GetSelectedRegion {
                override fun onSelectedRegion(id: Int, regionName: String) {
                    selectedRegion = SearchListItem(id, regionName)
                    binding.regionContainer.text = regionName
                    selectedCity = null
                    binding.neighborhoodContainer.text = null
                }
            })
        regionDialog.show()
    }

    private fun openNeighborhoodDialog() {
        val neighborhoodDialog = NeighborhoodDialog(
            this,
            selectedRegion!!.id,
            object : NeighborhoodDialog.GetSelectedNeighborhood {
                override fun onSelectedNeighborhood(id: Int, neighborhoodName: String) {
                    selectedCity = SearchListItem(id, neighborhoodName)
                    binding.neighborhoodContainer.text = neighborhoodName
                }
            })
        neighborhoodDialog.show()
    }

    private fun setAddressData() {
        addressObject?.let {
            binding.tvAddressTitle.text = it.title ?: ""
            binding.tvAddress.text = it.location ?: ""
            binding.tvStreet.text = it.street ?: ""
            binding.tvBuildingNumber.text = it.building ?: ""
            binding.tvFloor.text = it.floor ?: ""
            binding.tvApartmentNumber.text = it.appartment ?: ""
            binding.checkDefault.isChecked = it.defaultAddress
            try {
                binding.countryCodePicker.fullNumber = it.phone
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

            selectedCountry = it.country.toSearchListItem()
            selectedRegion = it.region.toSearchListItem()
            selectedCity = it.neighborhood.toSearchListItem()

            binding.countryContainer.text = it.country.name
            binding.regionContainer.text = it.region.name
            binding.neighborhoodContainer.text = it.neighborhood.name
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
                } else if (it.message2 != null) {
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
            binding.countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ARABIC)
        } else {
            binding.countryCodePicker.changeDefaultLanguage(CountryCodePicker.Language.ENGLISH)
            //  etPhoneNumber.textAlignment=View.TEXT_ALIGNMENT_VIEW_START
        }
        binding.countryCodePicker.registerCarrierNumberEditText(binding.etPhoneNumber)
        binding.countryCodePicker.setPhoneNumberValidityChangeListener { isValidNumber ->
            isPhoneNumberValid = isValidNumber
        }
        binding.countryCodePicker.setOnCountryChangeListener {
            binding.etPhoneNumber.text = Editable.Factory.getInstance().newEditable("")
        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.containerLocation.setOnClickListener {
            checkLocationSetting()
        }
        binding.btnAddAddress.setOnClickListener {
            checkDataToAddAddress()
        }
        binding.countryContainer._setOnClickListener {
            openCountryDialog()
        }
        binding.regionContainer._setOnClickListener {
            if (selectedCountry == null) {
                showError(getString(R.string.Please_select, getString(R.string.Country)))
            } else {
                openRegionDialog()
            }
        }
        binding.neighborhoodContainer._setOnClickListener {
            if (selectedRegion == null) {
                showError(getString(R.string.Please_select, getString(R.string.Region)))
            } else {
                openNeighborhoodDialog()
            }
        }
    }

    private fun checkDataToAddAddress() {
        var readyToSave = true
        if (binding.tvAddressTitle.text?.trim().toString() == "") {
            readyToSave = false
            binding.tvAddressTitle.error = getString(R.string.addressTitle)
        }
        if (binding.tvAddress.text?.trim().toString() == "") {
            readyToSave = false
            binding.tvAddress.error = getString(R.string.address)
        }
        if (binding.tvStreet.text?.trim().toString() == "") {
            readyToSave = false
            binding.tvStreet.error = getString(R.string.street)
        }
        if (binding.tvBuildingNumber.text?.trim().toString() == "") {
            readyToSave = false
            binding.tvBuildingNumber.error = getString(R.string.buildingNumber)
        }
        if (binding.tvFloor.text?.trim().toString() == "") {
            readyToSave = false
            binding.tvFloor.error = getString(R.string.floorNumber)
        }
        if (binding.tvApartmentNumber.text?.trim().toString() == "") {
            readyToSave = false
            binding.tvApartmentNumber.error = getString(R.string.apartmentNumber)
        }
        if (!isPhoneNumberValid) {
            readyToSave = false
            binding.etPhoneNumber.error = getString(R.string.PleaseenteravalidPhoneNumber)
        }

        if (binding.countryContainer.text.toString().isEmpty()) {
            readyToSave = false
            showError(getString(R.string.Please_select, getString(R.string.selectCountry)))
        } else if (binding.regionContainer.text.toString().isEmpty()) {
            readyToSave = false
            showError(getString(R.string.Please_select, getString(R.string.selectRegionTitle)))
        } else if (binding.neighborhoodContainer.text.toString().isEmpty()) {
            readyToSave = false
            showError(getString(R.string.Please_select, getString(R.string.City)))
        }

        if (readyToSave) {
            if (isEdit) {
                addressObject?.id?.let {
                    addressViewModel.editUserAddress(
                        id = it,
                        title = binding.tvAddressTitle.text?.trim().toString(),
                        location = binding.tvAddress.text?.trim().toString(),
                        street = binding.tvStreet.text?.trim().toString(),
                        appartment = binding.tvApartmentNumber.text?.trim().toString(),
                        floor = binding.tvFloor.text?.trim().toString(),
                        building = binding.tvBuildingNumber.text?.trim().toString(),
                        lat = latLngLocation?.latitude.toString(),
                        lng = latLngLocation?.longitude.toString(),
                        phone = binding.countryCodePicker.fullNumberWithPlus,
                        defaultAddress = binding.checkDefault.isChecked,
                        countryId = selectedCountry?.id!!,
                        regionId = selectedRegion?.id!!,
                        neighborhoodId = selectedCity?.id!!
                    )
                }
            } else {
                addressViewModel.addUserAddress(
                    title = binding.tvAddressTitle.text?.trim().toString(),
                    location = binding.tvAddress.text?.trim().toString(),
                    street = binding.tvStreet.text?.trim().toString(),
                    appartment = binding.tvApartmentNumber.text?.trim().toString(),
                    floor = binding.tvFloor.text?.trim().toString(),
                    building = binding.tvBuildingNumber.text?.trim().toString(),
                    lat = latLngLocation?.latitude.toString(),
                    lng = latLngLocation?.longitude.toString(),
                    phone = binding.countryCodePicker.fullNumberWithPlus,
                    defaultAddress = binding.checkDefault.isChecked,
                    countryId = selectedCountry?.id!!,
                    regionId = selectedRegion?.id!!,
                    neighborhoodId = selectedCity?.id!!
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