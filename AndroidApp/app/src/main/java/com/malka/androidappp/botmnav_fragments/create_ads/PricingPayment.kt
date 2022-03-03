package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.activity_add_item_details3.*
import kotlinx.android.synthetic.main.all_categories_cardview.*
import kotlinx.android.synthetic.main.fragment_about_the_seller.*
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.fragment_listing_duration.*
import kotlinx.android.synthetic.main.fragment_other_details.view.*
import kotlinx.android.synthetic.main.fragment_pie_chart_frag1.*
import kotlinx.android.synthetic.main.fragment_pricing_payment.*
import kotlinx.android.synthetic.main.fragment_shipping_pickups.*


class PricingPayment : Fragment() {
    lateinit var cashCheckBox: CheckBox
    lateinit var paymentError: TextView
    lateinit var switchBuynow: SwitchCompat
    lateinit var SwitchAuction: SwitchCompat
    lateinit var switchResrvepri: SwitchCompat
    lateinit var buynowlayout: LinearLayout
    lateinit var auctionlayout: LinearLayout
    lateinit var auctionPriceTV: LinearLayout
    lateinit var auctionOption: TextView
    lateinit var minimumPriceTV: LinearLayout
    lateinit var minimumlayout: LinearLayout
    lateinit var minimumEdittext: EditText
    lateinit var textviewbuynow: LinearLayout
    lateinit var editTextBuyNow: EditText
    lateinit var editTextAuctionPri: EditText
    lateinit var editTextReservePrice: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pricing_payment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        buynowlayout= requireActivity().findViewById(R.id.Buy_now_l)
        auctionlayout=  requireActivity().findViewById(R.id.Buy_Auctino_l)
        auctionPriceTV= requireActivity().findViewById(R.id.Auction_price)
        auctionOption = requireActivity().findViewById(R.id.auction_opt)
        minimumlayout= requireActivity().findViewById(R.id.minimum_price_l)
        minimumPriceTV= requireActivity().findViewById(R.id.textview_minimum_p)
        minimumEdittext= requireActivity().findViewById(R.id.reserveprice)
        textviewbuynow = requireActivity().findViewById(R.id.textview_price)
        switchBuynow = requireActivity().findViewById(R.id.listingtyp_rb1)
        SwitchAuction = requireActivity().findViewById(R.id.listingtyp_rb2)
        switchResrvepri = requireActivity().findViewById(R.id.listingtyp_rb3)
        editTextBuyNow = requireActivity().findViewById(R.id.buynowprice)
        editTextAuctionPri = requireActivity().findViewById(R.id.startprice)
        editTextReservePrice = requireActivity().findViewById(R.id.reserveprice)



        //////////////////////////////////////////
        btnnn.setOnClickListener() {
            confirmPricePaymentFrag(view)
        }

        disableTextFields(view)
    }

    private fun validaterListingTypeRadiobutton(): Boolean {
        return if (listingtyp_rb1.isChecked or listingtyp_rb2.isChecked or listingtyp_rb3.isChecked) {
            listingtype_rbtn_1_errortxt.visibility = View.GONE
            true
        } else {
            listingtype_rbtn_1_errortxt.visibility = View.VISIBLE
            listingtype_rbtn_1_errortxt.text = getString(R.string.Selectanyoneoption)
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
        return if (cashCheckBox.isChecked or bank.isChecked) {
            paymentError.visibility = View.GONE
            true
        } else {
            paymentError.visibility = View.VISIBLE
            paymentError.text = getString(R.string.Selectanyonepaymentmethod)
            false
        }
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

            if (cashCheckBox.isChecked) {
                StaticClassAdCreate.iscashpaid = "Cash"
            }
            if (bank.isChecked) {
                StaticClassAdCreate.isbankpaid = "SA bank deposit"
            }

            // To save the listing type
            confirmListingType(v)

            findNavController().navigate(R.id.pricepay_listduration)

        }

    }

    private fun checkValidation(): Boolean {
        when {
            switchBuynow.isChecked -> {
                if (!validateCheckPriceBox() or !limitBuyNowPrice()) {
                    return false
                }
            }
            SwitchAuction.isChecked -> {
                if (!validateStartPriceBox() or !validateReservePriceBox() or !validAmount() or !limitReservePrice() or !limitStartPrice())
                    return false
            }
            switchResrvepri.isChecked -> {
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




        switchBuynow.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                editTextAuctionPri.isEnabled = false
                editTextAuctionPri.isFocusableInTouchMode = false
                editTextAuctionPri.setText("")

                editTextReservePrice.isEnabled = false
                editTextReservePrice.isFocusableInTouchMode = false
                editTextReservePrice.setText("")

                fix_Price_l.setBackgroundResource(R.drawable.product_attribute_bg_linebg)

                editTextBuyNow.isEnabled = true
                editTextBuyNow.isFocusableInTouchMode = true

                saudi_bank_auction.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE


                listingtyp_rb2.isChecked = false
                listingtyp_rb3.isChecked = false

                buyNowView.visibility = View.VISIBLE
                buynowlayout.visibility = View.VISIBLE
                textviewbuynow.visibility = View.VISIBLE
                auctionlayout.visibility = View.GONE
                auctionPriceTV.visibility = View.GONE
                auctionOption.visibility = View.GONE
                minimumPriceTV.visibility = View.GONE
                minimumlayout.visibility = View.GONE
                startPriceView.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.VISIBLE

            }else{

                buyNowView.visibility = View.VISIBLE
                buynowlayout.visibility = View.GONE
                auctionlayout.visibility = View.GONE
                minimumlayout.visibility = View.GONE
                startPriceView.visibility = View.GONE
                textviewbuynow.visibility = View.GONE
                minimumEdittext.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.GONE
                auctionlayout.visibility = View.GONE
                editTextAuctionPri.visibility = View.GONE
                auctionPriceTV.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE
                auctionOption.visibility = View.GONE
                minimumPriceTV.visibility = View.GONE
                fix_Price_l.setBackgroundResource(R.drawable.add_product_attribte)
                minimumEdittext.visibility = View.GONE
                minimumlayout.visibility = View.GONE


                editTextBuyNow.isEnabled = true
                editTextBuyNow.isFocusableInTouchMode = true


            }
        })


        SwitchAuction.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                editTextAuctionPri.isEnabled = true
                editTextAuctionPri.isFocusableInTouchMode = true

                editTextReservePrice.isEnabled = true
                editTextReservePrice.isFocusableInTouchMode = true

                editTextBuyNow.isEnabled = false
                editTextBuyNow.isFocusableInTouchMode = false
                editTextBuyNow.setText("")

                saudi_bank_option.visibility = View.GONE

                saudi_bank_auction.visibility = View.VISIBLE

                auction_option.setBackgroundResource(R.drawable.product_attribute_bg_linebg)




                listingtyp_rb1.isChecked = false
                listingtyp_rb3.isChecked = false



                buyNowView.visibility = View.GONE
                buynowlayout.visibility = View.GONE
                textviewbuynow.visibility = View.GONE
                auctionlayout.visibility = View.VISIBLE
                editTextAuctionPri.visibility = View.VISIBLE
                auctionPriceTV.visibility = View.VISIBLE
                auctionOption.visibility = View.VISIBLE
                minimumEdittext.visibility = View.VISIBLE
                minimumPriceTV.visibility = View.VISIBLE
                minimumlayout.visibility = View.VISIBLE
                chooseOptionText.visibility = View.VISIBLE

            }else{

                buyNowView.visibility = View.GONE
                startPriceView.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.GONE
                auctionlayout.visibility = View.GONE
                editTextAuctionPri.visibility = View.GONE
                auctionPriceTV.visibility = View.GONE
                auctionOption.visibility = View.GONE
                minimumPriceTV.visibility = View.GONE
                minimumEdittext.visibility = View.GONE
                minimumlayout.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE
                saudi_bank_auction.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE
                auction_option.setBackgroundResource(R.drawable.add_product_attribte)



            }
        })
        switchResrvepri.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                editTextAuctionPri.isEnabled = true
                editTextAuctionPri.isFocusableInTouchMode = true

                editTextReservePrice.isEnabled = true
                editTextReservePrice.isFocusableInTouchMode = true

                Tender_l.setBackgroundResource(R.drawable.product_attribute_bg_linebg)

                editTextBuyNow.isEnabled = true
                editTextBuyNow.isFocusableInTouchMode = true

                saudi_bank_auction.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE

                buynowlayout.visibility = View.VISIBLE
                textviewbuynow.visibility = View.VISIBLE
                auctionlayout.visibility = View.VISIBLE
                editTextAuctionPri.visibility = View.VISIBLE
                auctionPriceTV.visibility = View.VISIBLE
                auctionOption.visibility = View.VISIBLE
                minimumPriceTV.visibility = View.VISIBLE
                minimumlayout.visibility = View.VISIBLE
                minimumEdittext.visibility = View.VISIBLE
                chooseOptionText.visibility = View.VISIBLE
                editTextBuyNow.visibility = View.VISIBLE


                listingtyp_rb1.isChecked = false
                listingtyp_rb2.isChecked = false


            }else{

                buyNowView.visibility = View.GONE
                buynowlayout.visibility = View.GONE
                textviewbuynow.visibility = View.GONE
                auctionlayout.visibility = View.GONE
                editTextAuctionPri.visibility = View.GONE
                auctionPriceTV.visibility = View.GONE
                auctionOption.visibility = View.GONE
                Tender_l.setBackgroundResource(R.drawable.add_product_attribte)
                minimumPriceTV.visibility = View.GONE
                minimumlayout.visibility = View.GONE
                chooseOptionText.visibility = View.VISIBLE
                minimumEdittext.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE



            }
        })
    }

    private fun validAmount(): Boolean {
        return if (editTextReservePrice.text.toString().trim()
                .isNotEmpty() && editTextAuctionPri.text.toString().trim().isNotEmpty()
        ) {
            if (editTextAuctionPri.text.toString()
                    .toBigInteger() < editTextReservePrice.text.toString()
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
        return if (editTextBuyNow.text.toString().trim().isNotEmpty()) {
            if (editTextBuyNow.text.toString().toBigInteger() <= 99999999.toBigInteger()) {
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
        return if (editTextReservePrice.text.toString().trim().isNotEmpty()) {
            if (editTextReservePrice.text.toString().toBigInteger() <= 99999999.toBigInteger()) {
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
        return if (editTextAuctionPri.text.toString().trim().isNotEmpty()) {
            if (editTextAuctionPri.text.toString()
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