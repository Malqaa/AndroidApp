package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.fragment_listing_duration.*
import kotlinx.android.synthetic.main.fragment_pricing_payment.*
import kotlinx.android.synthetic.main.fragment_shipping_pickups.*
import java.lang.NumberFormatException


class PricingPayment : Fragment() {
    lateinit var cashCheckBox: CheckBox
    lateinit var saPaymentCheckBox: CheckBox
    lateinit var paymentError: TextView
    lateinit var switchBuynow: Switch
    lateinit var radioAuction: Switch
    lateinit var radioBoth: Switch
    lateinit var editTextBuyNow: TextInputEditText
    lateinit var editTextStartPrice: TextInputEditText
    lateinit var editTextReservePrice: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pricing_payment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cashCheckBox = requireActivity().findViewById(R.id.cash)
        saPaymentCheckBox = requireActivity().findViewById(R.id.bank)
        paymentError = requireActivity().findViewById(R.id.error_payment_radionbtn)
        switchBuynow = requireActivity().findViewById(R.id.listingtyp_rb1)
        radioAuction = requireActivity().findViewById(R.id.listingtyp_rb2)
        radioBoth = requireActivity().findViewById(R.id.listingtyp_rb3)
        editTextBuyNow = requireActivity().findViewById(R.id.buynowprice)
        editTextStartPrice = requireActivity().findViewById(R.id.startprice)
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
        val textName = requireActivity().findViewById(R.id.buynowprice) as TextInputEditText
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
        val textStartPrice = requireActivity().findViewById(R.id.startprice) as TextInputEditText
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
            requireActivity().findViewById(R.id.reserveprice) as TextInputEditText
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
        return if (cashCheckBox.isChecked or saPaymentCheckBox.isChecked) {
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
            if (saPaymentCheckBox.isChecked) {
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
            radioAuction.isChecked -> {
                if (!validateStartPriceBox() or !validateReservePriceBox() or !validAmount() or !limitReservePrice() or !limitStartPrice())
                    return false
            }
            radioBoth.isChecked -> {
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
        var buyNowView: TextInputLayout = requireActivity().findViewById(R.id.pricepayment_inputlay)
        var startPriceView: TextInputLayout =
            requireActivity().findViewById(R.id.startprice_inputlay)
        var reservePriceView: TextInputLayout =
            requireActivity().findViewById(R.id.reserveprice_inputlay)

        var chooseOptionText: TextView = requireActivity().findViewById(R.id.chooseOption)

        switchBuynow.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                editTextStartPrice.isEnabled = false
                editTextStartPrice.isFocusableInTouchMode = false
                editTextStartPrice.setText("")

                editTextReservePrice.isEnabled = false
                editTextReservePrice.isFocusableInTouchMode = false
                editTextReservePrice.setText("")

                editTextBuyNow.isEnabled = true
                editTextBuyNow.isFocusableInTouchMode = true

                buyNowView.visibility = View.VISIBLE
                startPriceView.visibility = View.GONE
                reservePriceView.visibility = View.GONE
                chooseOptionText.visibility = View.GONE

            }
        })

        radioAuction.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                editTextStartPrice.isEnabled = true
                editTextStartPrice.isFocusableInTouchMode = true

                editTextReservePrice.isEnabled = true
                editTextReservePrice.isFocusableInTouchMode = true

                editTextBuyNow.isEnabled = false
                editTextBuyNow.isFocusableInTouchMode = false
                editTextBuyNow.setText("")

                buyNowView.visibility = View.GONE
                startPriceView.visibility = View.VISIBLE
                reservePriceView.visibility = View.VISIBLE
                chooseOptionText.visibility = View.GONE

            }
        })
        radioBoth.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
            if (b) {
                editTextStartPrice.isEnabled = true
                editTextStartPrice.isFocusableInTouchMode = true

                editTextReservePrice.isEnabled = true
                editTextReservePrice.isFocusableInTouchMode = true

                editTextBuyNow.isEnabled = true
                editTextBuyNow.isFocusableInTouchMode = true

                buyNowView.visibility = View.VISIBLE
                startPriceView.visibility = View.VISIBLE
                reservePriceView.visibility = View.VISIBLE
                chooseOptionText.visibility = View.GONE

            }
        })
    }

    private fun validAmount(): Boolean {
        return if (editTextReservePrice.text.toString().trim()
                .isNotEmpty() && editTextStartPrice.text.toString().trim().isNotEmpty()
        ) {
            if (editTextStartPrice.text.toString()
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
        return if (editTextStartPrice.text.toString().trim().isNotEmpty()) {
            if (editTextStartPrice.text.toString()
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