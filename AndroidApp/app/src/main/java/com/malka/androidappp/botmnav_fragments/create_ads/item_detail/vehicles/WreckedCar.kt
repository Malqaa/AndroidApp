package com.malka.androidappp.botmnav_fragments.create_ads.item_details.vehicle

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.toolbar_otherdetailsautombile
import kotlinx.android.synthetic.main.fragment_trucks.*
import kotlinx.android.synthetic.main.fragment_wrecked_car.*

class WreckedCar : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wrecked_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_wreckedcar.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_wreckedcar.setTitle("Item Details")
        toolbar_wreckedcar.setTitleTextColor(Color.WHITE)
        toolbar_wreckedcar.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_wreckedcar.setNavigationOnClickListener({
            requireActivity().onBackPressed()
        })

        toolbar_wreckedcar.setOnMenuItemClickListener(object :
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

        btnwreckedcar.setOnClickListener() {
            val yearr: String = year1_wreckedcar.getText().toString()
            StaticClassAdCreate.year = yearr

            val model: String = model_wreckedcar1.getText().toString()
            StaticClassAdCreate.model = model

            val make: String = make1_wreckedcar.getText().toString()
            StaticClassAdCreate.make = make

            findNavController().navigate(R.id.wreckedcaritemdetail_Listingdetail)
        }
    }
}