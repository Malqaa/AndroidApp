package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_shipping_pickups.*
import kotlinx.android.synthetic.main.toolbar_main.*


class ShippingPickups : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_shipping_pickups)

        toolbar_title.text = getString(R.string.Shippingpickupoptions)
        back_btn.setOnClickListener {
            finish()
        }

        //////////////////////////
        btn_shippickup.setOnClickListener(){
            confirmShipPickup(it)
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
            val pickupopt1 : RadioButton = findViewById(selectedId)
            val pickupoptRadiobtnnn1 : String = pickupopt1.text.toString()
            StaticClassAdCreate.pickup_option = pickupoptRadiobtnnn1

            ///////get value of radiobtn pickup opt2 to static class/////////////
            val selectedId2 : Int = pricepay_radioGroupp.checkedRadioButtonId
            val pickupopt2 : RadioButton =findViewById(selectedId2)
            val pickupoptRadiobtnnn2 : String = pickupopt2.text.toString()
            StaticClassAdCreate.shipping_option = pickupoptRadiobtnnn2

            startActivity(Intent(this, PromotionalFragment::class.java).apply {
            })

        }

    }

}