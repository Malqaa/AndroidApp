package com.malka.androidappp.botmnav_fragments.create_ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.fragment_pricing_payment.*


class PricingPayment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pricing_payment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        //////////////////////////////////////////
        btnnn.setOnClickListener() {
            confirmPricePaymentFrag(view)
        }

        disableTextFields(view)
    }

    private fun validaterListingTypeRadiobutton(): Boolean {
        return if (listingtyp_rb1.isChecked or listingtyp_rb2.isChecked or listingtyp_rb3.isChecked) {

            true
        } else {

            false
        }
    }


    // To check the type of listing duration

    fun confirmListingType(v: View) {
        if (!validaterListingTypeRadiobutton()) {
            return
        } else {

            if (listingtyp_rb1.isChecked) {

                StaticClassAdCreate.listingType = "1"
            } else if (listingtyp_rb2.isChecked) {
                StaticClassAdCreate.listingType = "2"
            } else if (listingtyp_rb3.isChecked) {
                StaticClassAdCreate.listingType = "3"
            }
        }

    }


    private fun validateCheckPriceBox(): Boolean {
        val textName = requireActivity().findViewById(R.id.buynowprice) as EditText
        val Inputname = textName.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            textName.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textName.error = null
            true
        }
    }

    private fun validateStartPriceBox(): Boolean {
        val textStartPrice = requireActivity().findViewById(R.id.startprice) as EditText
        val InputStartPrice = textStartPrice.text.toString().trim { it <= ' ' }
        return if (InputStartPrice.isEmpty()) {
            textStartPrice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textStartPrice.error = null
            true
        }
    }

    private fun validateReservePriceBox(): Boolean {
        val textReservePrice =
            requireActivity().findViewById(R.id.reserveprice) as EditText
        val InputReservePrice = textReservePrice.text.toString().trim { it <= ' ' }
        return if (InputReservePrice.isEmpty()) {
            textReservePrice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textReservePrice.error = null
            true
        }
    }

    private fun validateradiobutton(): Boolean {
//        return if (cashCheckBox.isChecked or bank.isChecked) {
//            paymentError.visibility = View.GONE
//            true
//        } else {
//            paymentError.visibility = View.VISIBLE
//            paymentError.text = getString(R.string.Selectanyonepaymentmethod)
//            false
//        }
        return false
    }

    fun confirmPricePaymentFrag(v: View) {
        if (!validateradiobutton() or !validaterListingTypeRadiobutton() or !checkValidation()) {
            return
        } else {

            ////////to get edittext data and save to static class////////
            val priceText: String = buynowprice.text.toString()
            val startPrice: String = startprice.text.toString()
            val reservedPrice: String = reserveprice.text.toString()

            StaticClassAdCreate.price = priceText
            StaticClassAdCreate.reservedPrice = reservedPrice
            StaticClassAdCreate.startingPrice = startPrice

//            if (cashCheckBox.isChecked) {
//                StaticClassAdCreate.iscashpaid = "Cash"
//            }
//            if (bank.isChecked) {
//                StaticClassAdCreate.isbankpaid = "SA bank deposit"
//            }

            // To save the listing type
            confirmListingType(v)

            findNavController().navigate(R.id.pricepay_listduration)

        }

    }

    private fun checkValidation(): Boolean {
        when {
            listingtyp_rb1.isChecked -> {
                if (!validateCheckPriceBox() or !limitBuyNowPrice()) {
                    return false
                }
            }
            listingtyp_rb2.isChecked -> {
                if (!validateStartPriceBox() or !validateReservePriceBox() or !validAmount() or !limitReservePrice() or !limitStartPrice())
                    return false
            }
            listingtyp_rb3.isChecked -> {
                if (!validateCheckPriceBox() or !validateStartPriceBox() or !validateReservePriceBox()
                    or !validAmount() or !limitBuyNowPrice() or !limitReservePrice()
                    or !limitStartPrice()
                )
                    return false
            }
        }
        return true
    }

    private fun disableTextFields(v: View) {
        var buyNowView: EditText = requireActivity().findViewById(R.id.buynowprice)
        var startPriceView: EditText = requireActivity().findViewById(R.id.startprice)
        var reservePriceView: EditText = requireActivity().findViewById(R.id.reserveprice)

        var chooseOptionText: LinearLayout = requireActivity().findViewById(R.id.chooseOption)




        listingtyp_rb1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                startprice.isEnabled = false
                startprice.isFocusableInTouchMode = false
                startprice.setText("")

                reserveprice.isEnabled = false
                reserveprice.isFocusableInTouchMode = false
                reserveprice.setText("")

                fix_Price_l.setBackgroundResource(R.drawable.product_attribute_bg_linebg)

                buynowprice.isEnabled = true
                buynowprice.isFocusableInTouchMode = true

                saudi_bank_auction.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE


                listingtyp_rb2.isChecked = false
                listingtyp_rb3.isChecked = false

                buyNowView.visibility = View.VISIBLE
                Buy_now_l.visibility = View.VISIBLE
                textview_price.visibility = View.VISIBLE
                Buy_Auctino_l.visibility = View.GONE
                Auction_price.visibility = View.GONE
                auction_opt.visibility = View.GONE
                textview_minimum_p.visibility = View.GONE
                minimum_price_l.visibility = View.GONE
                startPriceView.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.VISIBLE

            }else{

                buyNowView.visibility = View.VISIBLE
                Buy_now_l.visibility = View.GONE
                Buy_Auctino_l.visibility = View.GONE
                minimum_price_l.visibility = View.GONE
                startPriceView.visibility = View.GONE
                textview_price.visibility = View.GONE
                reserveprice.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.GONE
                Buy_Auctino_l.visibility = View.GONE
                startprice.visibility = View.GONE
                Auction_price.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE
                auction_opt.visibility = View.GONE
                textview_minimum_p.visibility = View.GONE
                fix_Price_l.setBackgroundResource(R.drawable.add_product_attribte)
                reserveprice.visibility = View.GONE
                minimum_price_l.visibility = View.GONE


                buynowprice.isEnabled = true
                buynowprice.isFocusableInTouchMode = true


            }
        })


        listingtyp_rb2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                startprice.isEnabled = true
                startprice.isFocusableInTouchMode = true

                reserveprice.isEnabled = true
                reserveprice.isFocusableInTouchMode = true

                buynowprice.isEnabled = false
                buynowprice.isFocusableInTouchMode = false
                buynowprice.setText("")

                saudi_bank_option.visibility = View.GONE

                saudi_bank_auction.visibility = View.VISIBLE

                auction_option.setBackgroundResource(R.drawable.product_attribute_bg_linebg)




                listingtyp_rb1.isChecked = false
                listingtyp_rb3.isChecked = false



                buyNowView.visibility = View.GONE
                Buy_now_l.visibility = View.GONE
                textview_price.visibility = View.GONE
                Buy_Auctino_l.visibility = View.VISIBLE
                startprice.visibility = View.VISIBLE
                Auction_price.visibility = View.VISIBLE
                auction_opt.visibility = View.VISIBLE
                reserveprice.visibility = View.VISIBLE
                textview_minimum_p.visibility = View.VISIBLE
                minimum_price_l.visibility = View.VISIBLE
                chooseOptionText.visibility = View.VISIBLE

            }else{

                buyNowView.visibility = View.GONE
                startPriceView.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.GONE
                Buy_Auctino_l.visibility = View.GONE
                startprice.visibility = View.GONE
                Auction_price.visibility = View.GONE
                auction_opt.visibility = View.GONE
                textview_minimum_p.visibility = View.GONE
                reserveprice.visibility = View.GONE
                minimum_price_l.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE
                saudi_bank_auction.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE
                auction_option.setBackgroundResource(R.drawable.add_product_attribte)



            }
        })
        listingtyp_rb3.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                startprice.isEnabled = true
                startprice.isFocusableInTouchMode = true

                reserveprice.isEnabled = true
                reserveprice.isFocusableInTouchMode = true

                Tender_l.setBackgroundResource(R.drawable.product_attribute_bg_linebg)

                buynowprice.isEnabled = true
                buynowprice.isFocusableInTouchMode = true

                saudi_bank_auction.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE

                Buy_now_l.visibility = View.VISIBLE
                textview_price.visibility = View.VISIBLE
                Buy_Auctino_l.visibility = View.VISIBLE
                startprice.visibility = View.VISIBLE
                Auction_price.visibility = View.VISIBLE
                auction_opt.visibility = View.VISIBLE
                textview_minimum_p.visibility = View.VISIBLE
                minimum_price_l.visibility = View.VISIBLE
                reserveprice.visibility = View.VISIBLE
                chooseOptionText.visibility = View.VISIBLE
                buynowprice.visibility = View.VISIBLE


                listingtyp_rb1.isChecked = false
                listingtyp_rb2.isChecked = false


            }else{

                buyNowView.visibility = View.GONE
                Buy_now_l.visibility = View.GONE
                textview_price.visibility = View.GONE
                Buy_Auctino_l.visibility = View.GONE
                startprice.visibility = View.GONE
                Auction_price.visibility = View.GONE
                auction_opt.visibility = View.GONE
                Tender_l.setBackgroundResource(R.drawable.add_product_attribte)
                textview_minimum_p.visibility = View.GONE
                minimum_price_l.visibility = View.GONE
                chooseOptionText.visibility = View.VISIBLE
                reserveprice.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE



            }
        })
    }

    private fun validAmount(): Boolean {
        return if (reserveprice.text.toString().trim()
                .isNotEmpty() && startprice.text.toString().trim().isNotEmpty()
        ) {
            if (startprice.text.toString()
                    .toBigInteger() < reserveprice.text.toString()
                    .toBigInteger()
            ) {
                true
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.Startpriceshouldbesmaller),context)
//                Toast.makeText(context, "Start price should be smaller", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            true
        }
    }

    private fun limitBuyNowPrice(): Boolean {
        return if (buynowprice.text.toString().trim().isNotEmpty()) {
            if (buynowprice.text.toString().toBigInteger() <= 99999999.toBigInteger()) {
                true
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.Buynowmaxlimitreached),context)
//                Toast.makeText(context, "Buy now max limit reached", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            true
        }

    }

    private fun limitReservePrice(): Boolean {
        return if (reserveprice.text.toString().trim().isNotEmpty()) {
            if (reserveprice.text.toString().toBigInteger() <= 99999999.toBigInteger()) {
                true
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.Reservemaxlimitreached),context)
//                Toast.makeText(context, "Reserve max limit reached", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            true
        }
    }

    private fun limitStartPrice(): Boolean {
        return if (startprice.text.toString().trim().isNotEmpty()) {
            if (startprice.text.toString()
                    .toBigInteger() <= 70000000.toBigInteger()
            ) {
                true
            } else {

                HelpFunctions.ShowLongToast(getString(R.string.StartPricemaxlimitreached),context)

//                Toast.makeText(context, "Start Price max limit reached", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            true
        }
    }
}