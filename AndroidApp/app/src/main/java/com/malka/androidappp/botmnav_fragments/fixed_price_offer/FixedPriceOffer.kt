package com.malka.androidappp.botmnav_fragments.fixed_price_offer

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_fixed_price_offer.*
import kotlinx.android.synthetic.main.fragment_lost.*


class FixedPriceOffer : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fixed_price_offer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_fixedprice.title = getString(R.string.Fixedpriceoffer)
        toolbar_fixedprice.setTitleTextColor(Color.WHITE)
        toolbar_fixedprice.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_fixedprice.navigationIcon?.isAutoMirrored = true
        toolbar_fixedprice.setNavigationOnClickListener(){
            requireActivity().onBackPressed()
        }
    }


}