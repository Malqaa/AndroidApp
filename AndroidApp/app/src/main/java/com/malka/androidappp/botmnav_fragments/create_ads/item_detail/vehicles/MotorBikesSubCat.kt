package com.malka.androidappp.botmnav_fragments.create_ads.item_details.vehicle

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_bus_sub_cat.*
import kotlinx.android.synthetic.main.fragment_motor_bikes_sub_cat.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.toolbar_otherdetailsautombile

class MotorBikesSubCat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motor_bikes_sub_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_bikeitemdetail.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_bikeitemdetail.setTitle("Item Details")
        toolbar_bikeitemdetail.setTitleTextColor(Color.WHITE)
        toolbar_bikeitemdetail.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_bikeitemdetail.setNavigationOnClickListener({
            requireActivity().onBackPressed()
            //   super.onBackPressed()
            //  finish()
        })

        toolbar_bikeitemdetail.setOnMenuItemClickListener(object :
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

        val allStyles: Array<String> = arrayOf(
            "- - Select Style - -",
            "Cruisers Motorcycle",
            "Sport Motorcycles",
            "Touring Motorcycles",
            "Dirt Bikes",
            "Dual Motorcycles",
            "Scooter Bikes",
            "Quads & ATVs Bikes"
        )

        /////////////////For Duration Dropdown/Spinner/////////////////////
        val spinner: Spinner = requireActivity().findViewById(R.id.bikeStyles)
//        val adapter = ArrayAdapter.createFromResource(
//            this.requireActivity(),
//            allWeeks,
//            R.layout.support_simple_spinner_dropdown_item
//        )
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//        spinner.adapter = adapter
        spinner.adapter = ArrayAdapter<String>(
            requireActivity(), android.R.layout.simple_list_item_1, allStyles
        )

        btnobike.setOnClickListener(){
            val style: String = bikeStyles.getSelectedItem().toString()
            StaticClassAdCreate.style = style
            val yearr: String = year1_bike.getText().toString()
            StaticClassAdCreate.year = yearr
            val kilometers: String = kilometers_bike.getText().toString()
            StaticClassAdCreate.kilometers = kilometers
            val model: String = model_bike.getText().toString()
            StaticClassAdCreate.model = model
            val make: String = make1_bike.getText().toString()
            StaticClassAdCreate.make = make


            findNavController().navigate(R.id.bikeitemdetails_listingdetail)


        }

    }
}