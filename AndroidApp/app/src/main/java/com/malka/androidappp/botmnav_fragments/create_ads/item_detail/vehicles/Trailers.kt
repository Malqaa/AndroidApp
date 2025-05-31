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
import kotlinx.android.synthetic.main.fragment_trailers.*

class Trailers : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trailers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_trailers.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_trailers.setTitle("Item Details")
        toolbar_trailers.setTitleTextColor(Color.WHITE)
        toolbar_trailers.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_trailers.setNavigationOnClickListener({
            requireActivity().onBackPressed()
        })

        toolbar_trailers.setOnMenuItemClickListener(object :
            Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.action_close) {
                    //findNavController().navigate(R.id.close_otherdetailsautomobile)
                    //closefrag()
                } else {
                    // do something
                }
                return false
            }
        })


        btntrailers.setOnClickListener() {
            val yearr: String = year1_trailers.getText().toString()
            StaticClassAdCreate.year = yearr

            val model: String = model_trailers1.getText().toString()
            StaticClassAdCreate.model = model

            val make: String = make1_trailers.getText().toString()
            StaticClassAdCreate.make = make

            findNavController().navigate(R.id.traileritemdetail_listingdetail)

        }

    }
}