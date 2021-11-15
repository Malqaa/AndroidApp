package com.malka.androidappp.botmnav_fragments.shoppingcart4_googlemap

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.fragment_blacklist_menbers.*
import kotlinx.android.synthetic.main.fragment_shop_cart_map.*

class ShopCartMap : Fragment() {


    var lat: Double = -1.0;
    var long: Double = -1.0;
    lateinit var map: GoogleMap;


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        getLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_cart_map, container, false)
    }

    fun getLocation() {
        var locationManager =
            this@ShopCartMap.requireContext().getSystemService(LOCATION_SERVICE) as LocationManager?
        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (this@ShopCartMap::map != null && this@ShopCartMap::map.isInitialized) {
                    lat = location!!.latitude
                    long = location!!.longitude
                    val currentloc = LatLng(lat, long)
                    map.addMarker(MarkerOptions().position(currentloc).title("Current Location"))
                    map.moveCamera(CameraUpdateFactory.newLatLng(currentloc))
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
                this@ShopCartMap.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ShopCartMap.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        locationManager!!.requestLocationUpdates(
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
                    this@ShopCartMap.requireContext(),
                    "Information",
                    "Location Permission Needed"
                )
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        toolbar_shopcartmap.inflateMenu(R.menu.map_cancelbutton_menu)
        toolbar_shopcartmap.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.cancel_btn) {
                    // do something
                    activity!!.onBackPressed()
                }
                return false
            }
        })
        //////////////////////////////////////////////////////////////////////////////////

        btn_shopcartmap.setOnClickListener() {
            findNavController().navigate(R.id.mapfrag_locationfrag)
        }
    }
}