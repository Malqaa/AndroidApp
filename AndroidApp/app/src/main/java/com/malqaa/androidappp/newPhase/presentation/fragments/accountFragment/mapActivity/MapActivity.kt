package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.mapActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityMapBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.LocationPermissionDialog

class MapActivity : BaseActivity<ActivityMapBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var latLngLocation: LatLng? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback
    private var locationPermissionDialog: LocationPermissionDialog? = null

    private var PERMISSIONS: Array<String> =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    private val REQUEST_CHECK_PERMISSION_LOCATION = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.selectLocation)
        setViewClickListener()
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationPermissionDialog = LocationPermissionDialog(this)
    }

    private fun setViewClickListener() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.containerLocation.setOnClickListener {
            checkLocationSetting()
        }
        binding.btnSaveLocatiionData.setOnClickListener {
            if (latLngLocation != null) {
                val intent = Intent()
                intent.putExtra("lat", latLngLocation!!.latitude)
                intent.putExtra("longitude", latLngLocation!!.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
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
                    try {
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

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.setOnMapClickListener { latLng ->
            latLngLocation = LatLng(latLng.latitude, latLng.longitude)
            loadLocation(latLngLocation!!)
        }
        setupCurrentLocation()
    }

    private fun setupCurrentLocation() {
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

}