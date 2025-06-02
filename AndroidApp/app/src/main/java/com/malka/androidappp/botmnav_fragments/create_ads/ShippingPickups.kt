package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_shipping_pickups.*


class ShippingPickups : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipping_pickups, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_ship_pick.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_ship_pick.title = getString(R.string.Shippingpickupoptions)
        toolbar_ship_pick.setTitleTextColor(Color.WHITE)
        toolbar_ship_pick.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_ship_pick.setNavigationOnClickListener {
            requireActivity().onBackPressed()

        }

        toolbar_ship_pick.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.action_close) {
                    findNavController().navigate(R.id.close_shippick)
                    //closefrag()
                } else {
                    // do something
                }
                return false
            }
        })
        //////////////////////////
        btn_shippickup.setOnClickListener(){
            confirmShipPickup(view)
        }
    }

    //////////////////////////////////////////////////////////
    private  fun validaterShippingRadiobutton(): Boolean {
        return if (rb1.isChecked or rb2.isChecked) {
            rbtn_1.visibility = View.GONE
            true
        }

        else{
            rbtn_1.visibility = View.VISIBLE
            rbtn_1.text = getString(R.string.Selectanyoneoption)
            false
        }
    }

    //////////////////////////////////////////////////////////
    private  fun validaterShippingRadiobutton2(): Boolean {
        return if (rb2_1.isChecked or rb2_2.isChecked or rb2_3.isChecked or rb2_4.isChecked) {
            rbtn_2.visibility = View.GONE
            true
        }

        else{
            rbtn_2.visibility = View.VISIBLE
            rbtn_2.text = getString(R.string.Selectanyoneoption)
            false
        }
    }
/////////////////////////////////////////////////////////////
    fun confirmShipPickup(v: View) {
        if (!validaterShippingRadiobutton() or !validaterShippingRadiobutton2()) {
            return
        } else {

            ///////get value of radiobtn pickup opt1 to static class/////////////
            val selectedId: Int = ship_pickup_radioGroup.checkedRadioButtonId
            val pickupopt1 : RadioButton = requireActivity().findViewById(selectedId)
            val pickupoptRadiobtnnn1 : String = pickupopt1.text.toString()
            StaticClassAdCreate.pickup_option = pickupoptRadiobtnnn1

            ///////get value of radiobtn pickup opt2 to static class/////////////
            val selectedId2 : Int = pricepay_radioGroupp.checkedRadioButtonId
            val pickupopt2 : RadioButton = requireActivity().findViewById(selectedId2)
            val pickupoptRadiobtnnn2 : String = pickupopt2.text.toString()
            StaticClassAdCreate.shipping_option = pickupoptRadiobtnnn2


            findNavController().navigate(R.id.shippick_promotional)

        }

    }

}