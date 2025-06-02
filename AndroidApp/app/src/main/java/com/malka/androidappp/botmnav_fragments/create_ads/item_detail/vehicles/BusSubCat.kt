package com.malka.androidappp.botmnav_fragments.create_ads.item_details.vehicle

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.ListingDetailsFragment
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_bus_sub_cat.*
import kotlinx.android.synthetic.main.fragment_car_subcat.*
import kotlinx.android.synthetic.main.fragment_other_details.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.toolbar_otherdetailsautombile
import java.util.*


class BusSubCat : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_sub_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_busitemdetail.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_busitemdetail.setTitle("Item Details")
        toolbar_busitemdetail.setTitleTextColor(Color.WHITE)
        toolbar_busitemdetail.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_busitemdetail.setNavigationOnClickListener({
            requireActivity().onBackPressed()
            //   super.onBackPressed()
            //  finish()
        })

        toolbar_busitemdetail.setOnMenuItemClickListener(object :
            Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.action_close) {
                    findNavController().navigate(R.id.close_otherdetailsautomobile)
                    //closefrag()
                } else {
                    // do something
                }
                return false
            }
        })
        btnbus.setOnClickListener() {
            val yearr: String = yearbus.getText().toString()
            StaticClassAdCreate.year = yearr
            val cyclinder: String = cyclinderbus.getText().toString()
            StaticClassAdCreate.cylinders = cyclinder
            val kilometer: String = kilometersbus.getText().toString()
            StaticClassAdCreate.kilometers = kilometer
            val motorInspection: String = Periodic_Inspection_bus.getText().toString()
            StaticClassAdCreate.motorvehiclesperiodicinspection = motorInspection
            val model: String = model_bus.getText().toString()
            StaticClassAdCreate.model = model
            val fuelType: String = fueltype_bus.getText().toString()
            StaticClassAdCreate.fuel = fuelType
            val transmission: String = transmission_bus.getText().toString()
            StaticClassAdCreate.transmission = transmission
            val make: String = makebus.getText().toString()
            StaticClassAdCreate.make = make
            val numberOfpassengers: String = numberOfpassengersbus.getText().toString()
            StaticClassAdCreate.numberOfPassengers = numberOfpassengers

            //



            findNavController().navigate(R.id.busitemdetails_listingdetail)

        }
    }
}