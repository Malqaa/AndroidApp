package com.malka.androidappp.botmnav_fragments.create_product

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_create_product.*
import kotlinx.android.synthetic.main.fragment_create_product.toolbar_product
import kotlinx.android.synthetic.main.fragment_create_product_pg3.*


class CreateProduct3 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_product_pg3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_createpg3.title = "My Products"
        toolbar_createpg3.setTitleTextColor(Color.WHITE)
        toolbar_createpg3.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_createpg3.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val offerDuration: Array<String> =
            arrayOf("1 day", "3 days", "7 days")

        val offerTo: Array<String> =
            arrayOf("Bidders/watchers", "Bidders only")

        /////////////////For Offer Duration Dropdown/Spinner/////////////////////
        val spinner: Spinner = requireActivity().findViewById(R.id.offerduartion_spinner)
        spinner.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            offerDuration
        )

        /////////////////For Offer To Dropdown/Spinner/////////////////////
        val spinner2: Spinner = requireActivity().findViewById(R.id.offerto_spinner)
        spinner2.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            offerTo
        )

        list_buynow.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                list_auction.isChecked = false
                start_price_textt.isEnabled = false
                start_price_textt.setText("")
                start_price_textt.inputType = InputType.TYPE_NULL
                start_price_textt.isFocusableInTouchMode = false

                specifyreserve_textt.isEnabled = false
                specifyreserve_textt.setText("")
                specifyreserve_textt.inputType = InputType.TYPE_NULL
                specifyreserve_textt.isFocusableInTouchMode = false
            } else {
                list_auction.isChecked = true
                start_price_textt.isEnabled = true
                start_price_textt.inputType = InputType.TYPE_CLASS_TEXT
                start_price_textt.isFocusableInTouchMode = true

                specifyreserve_textt.isEnabled = true
                specifyreserve_textt.inputType = InputType.TYPE_CLASS_TEXT
                specifyreserve_textt.isFocusableInTouchMode = true
            }
        })

        list_auction.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                list_buynow.isChecked = false
                start_price_textt.isEnabled = true
                start_price_textt.inputType = InputType.TYPE_CLASS_TEXT
                start_price_textt.isFocusableInTouchMode = true

                specifyreserve_textt.isEnabled = true
                specifyreserve_textt.inputType = InputType.TYPE_CLASS_TEXT
                specifyreserve_textt.isFocusableInTouchMode = true

            } else {
                list_buynow.isChecked = true

                start_price_textt.isEnabled = false
                start_price_textt.setText("")
                start_price_textt.inputType = InputType.TYPE_NULL
                start_price_textt.isFocusableInTouchMode = false

                specifyreserve_textt.isEnabled = false
                specifyreserve_textt.setText("")
                specifyreserve_textt.inputType = InputType.TYPE_NULL
                specifyreserve_textt.isFocusableInTouchMode = false
            }
        })

        btn_productinfo2.setOnClickListener() {

            radioButtonCheck(view)

            val buyNowText: String = buynow_textt.getText().toString()
            StaticClassProductCreate.BuyNow = buyNowText

            StaticClassProductCreate.onlyAllowBidsFromAuthenticatedMembers = only_bids.isChecked

            StaticClassProductCreate.isfixedPriceOffer = fixedprice_details.isChecked

            val fixedPriceText: String = fixedprice_textt.getText().toString()
            StaticClassProductCreate.fixedPriceOffer = fixedPriceText

            val offerDurationText: String = offerduartion_spinner.getSelectedItem().toString()
            StaticClassProductCreate.offerDuration = offerDurationText

            val offerTo: String = offerto_spinner.getSelectedItem().toString()
            StaticClassProductCreate.OfferTo = offerTo

            // Accepted Payment Methods
            StaticClassProductCreate.saudiBankDeposit = cash_sadeposit.isChecked
            StaticClassProductCreate.ping = bank_ping.isChecked
            StaticClassProductCreate.cash = bank_cash.isChecked
            StaticClassProductCreate.Paypal = bank_paypal.isChecked

            findNavController().navigate(R.id.product_page4)
        }
    }

    fun radioButtonCheck(v:View){
        if (list_buynow.isChecked){
            StaticClassProductCreate.startPrice = ""
            StaticClassProductCreate.specifyReserve = ""
            StaticClassProductCreate.ListingType = "1"
        }else {
            StaticClassProductCreate.startPrice = start_price_textt.getText().toString()
            StaticClassProductCreate.specifyReserve = specifyreserve_textt.getText().toString()
            StaticClassProductCreate.ListingType = "2"
        }
    }

}