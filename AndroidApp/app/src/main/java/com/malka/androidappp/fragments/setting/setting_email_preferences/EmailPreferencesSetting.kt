package com.malka.androidappp.fragments.setting.setting_email_preferences

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_email_preferences_setting.*


class EmailPreferencesSetting : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_preferences_setting, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///////////////////////////////////////////////////////
        toolbar_emailpreferences.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_emailpreferences.title = getString(R.string.EmailPreferences)
        toolbar_emailpreferences.setTitleTextColor(Color.WHITE)
        toolbar_emailpreferences.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            //finish()
        }
        //////////////////////////////////////////////////////////////


        var count = 0


        trademeupdate_card.setOnClickListener() {


            if (count == 0) {
                trademeupdate_sub.setVisibility(View.VISIBLE)
                img_rrl1.setRotation(90F)
                count++
            } else {
                trademeupdate_sub.setVisibility(View.GONE)
                img_rrl1.setRotation(270F)
                count--
            }
        }
///////////////////////////////////////////////////////////////////
        propertyupdate_card.setOnClickListener() {


            if (count == 0) {
                propertyupdate_sub.setVisibility(View.VISIBLE)
                img_rrl2.setRotation(90F)
                count++
            } else {
                propertyupdate_sub.setVisibility(View.GONE)
                img_rrl2.setRotation(270F)
                count--
            }
        }
///////////////////////////////////////////////////////////////////////
        jobupdate_card.setOnClickListener() {


            if (count == 0) {
                jobupdate_sub.setVisibility(View.VISIBLE)
                img_propertyupdate.setRotation(90F)
                count++
            } else {
                jobupdate_sub.setVisibility(View.GONE)
                img_propertyupdate.setRotation(270F)
                count--
            }
        }

////////////////////////////////////////////////////////////////////////
        motorupdate_card.setOnClickListener() {


            if (count == 0) {
                motorupdate_sub.setVisibility(View.VISIBLE)
                img_motorupdate.setRotation(90F)
                count++
            } else {
                motorupdate_sub.setVisibility(View.GONE)
                img_motorupdate.setRotation(270F)
                count--
            }
        }

/////////////////////////////////////////////////////////////////////////
        buying_selling_card.setOnClickListener() {


            if (count == 0) {
                buysell_sub.setVisibility(View.VISIBLE)
                img_buyingsellingupdate.setRotation(90F)
                count++
            } else {
                buysell_sub.setVisibility(View.GONE)
                img_buyingsellingupdate.setRotation(270F)
                count--
            }
        }

/////////////////////////////////////////////////////////////////////////
        watchlistreminder_card.setOnClickListener() {


            if (count == 0) {
                watchlistreminder_sub.setVisibility(View.VISIBLE)
                img_watchlistreminder.setRotation(90F)
                count++
            } else {
                watchlistreminder_sub.setVisibility(View.GONE)
                img_watchlistreminder.setRotation(270F)
                count--
            }
        }

/////////////////////////////////////////////////////////////////////////
        fixedpriceoffers_card.setOnClickListener() {


            if (count == 0) {
                fixedpriceoffer_sub.setVisibility(View.VISIBLE)
                img_fixedpriceoffers.setRotation(90F)
                count++
            } else {
                fixedpriceoffer_sub.setVisibility(View.GONE)
                img_fixedpriceoffers.setRotation(270F)
                count--
            }
        }

/////////////////////////////////////////////////////////////////////////
        relisteditem_card.setOnClickListener() {


            if (count == 0) {
                relisteditem_sub.setVisibility(View.VISIBLE)
                img_relisteditem.setRotation(90F)
                count++
            } else {
                relisteditem_sub.setVisibility(View.GONE)
                img_relisteditem.setRotation(270F)
                count--
            }
        }

/////////////////////////////////////////////////////////////////////////
        emailformat_card.setOnClickListener() {


            if (count == 0) {
                emailformat_sub.setVisibility(View.VISIBLE)
                img_emailformat.setRotation(90F)
                count++
            } else {
                emailformat_sub.setVisibility(View.GONE)
                img_emailformat.setRotation(270F)
                count--
            }
        }





    }
}