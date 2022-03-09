package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.property.sub_cat_c

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_residential__lands.*


class ResidentialLands : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_residential__lands, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_propertyitemdetail.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_propertyitemdetail.setTitle("Item Details")
        toolbar_propertyitemdetail.setTitleTextColor(Color.WHITE)
        toolbar_propertyitemdetail.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_propertyitemdetail.setNavigationOnClickListener({
            requireActivity().onBackPressed()
        })

        toolbar_propertyitemdetail.setOnMenuItemClickListener(object :
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

        btnrlands.setOnClickListener() {

            val floorArea: String = floorArea2.getText().toString()
            StaticClassAdCreate.floorarea = floorArea

            //
            findNavController().navigate(R.id.residentialland_listingdetail)

        }
    }

}