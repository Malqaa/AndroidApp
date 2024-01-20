package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.Branch
import kotlinx.android.synthetic.main.activity_show_branches_map.mapBranches

class ShowBranchesMapActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityShowBranchesMapBinding

    var lat: Double = -1.0
    var long: Double = -1.0
    lateinit var map: GoogleMap
    var receivedObjects = ArrayList<Branch>()
    lateinit var locationManager :LocationManager
    lateinit var  locationListener :LocationListener
    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        getLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_show_branches_map)
         receivedObjects = intent.getParcelableArrayListExtra<Branch>("customBranches")?: arrayListOf()

        val mapFragment =   mapBranches as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    fun getLocation() {
         locationManager =
             (this@ShowBranchesMapActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager?)!!
         locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (this@ShowBranchesMapActivity::map != null &&this@ShowBranchesMapActivity::map.isInitialized) {
                    lat = location.latitude
                    long = location.longitude
                    val currentloc = LatLng(lat, long)
                    for(i in receivedObjects){
                        val loc = LatLng(i.lat?.toDouble()!!, i.lng?.toDouble()!!)

                        map.addMarker(MarkerOptions().position(loc).title(""))

                    }
                    map.addMarker(MarkerOptions().position(currentloc).title("Current Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentloc, 15.0f))

                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }

        }

        if (ContextCompat.checkSelfPermission(
                this@ShowBranchesMapActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ShowBranchesMapActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> HelpFunctions.ShowAlert(
                    this@ShowBranchesMapActivity,
                    "Information",
                    "Location Permission Needed"
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)

    }
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

}