package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.add_account_layout.*
import kotlinx.android.synthetic.main.fragment_pricing_payment.*
import kotlinx.android.synthetic.main.toolbar_main.*


class PricingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pricing_payment)


        toolbar_title.text = getString(R.string.sale_details)
        back_btn.setOnClickListener {
            finish()
        }
        btnnn.setOnClickListener {
            confirmPricePaymentFrag()
        }

        add_a_new_account.setOnClickListener {
            showBottomSheetDialog()
        }

        disableTextFields()

    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.add_account_layout)
        bottomSheetDialog.add_account_btn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()
    }

    private fun validaterListingTypeRadiobutton(): Boolean {
        return if (listingtyp_rb1.isChecked or listingtyp_rb2.isChecked or listingtyp_rb3.isChecked) {
            true
        } else {
            showError(getString(R.string.Selectanyoneoption))
            false
        }
    }


    private fun validateCheckPriceBox(): Boolean {
        val Inputname = buynowprice.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            showError(getString(R.string.Fieldcantbeempty))
            false
        } else {
            true
        }
    }

    private fun validateStartPriceBox(): Boolean {
        val InputStartPrice = startprice.text.toString().trim { it <= ' ' }
        return if (InputStartPrice.isEmpty()) {
            showError(getString(R.string.Fieldcantbeempty))

            false
        } else {
            true
        }
    }

    private fun validateReservePriceBox(): Boolean {

        val InputReservePrice = reserveprice.text.toString().trim { it <= ' ' }
        return if (InputReservePrice.isEmpty()) {
            showError(getString(R.string.Fieldcantbeempty))
            false
        } else {
            true
        }
    }

    private fun validateradiobutton(): Boolean {
        return if (swicth_visa_mastercard.isChecked or bank.isChecked) {
            true
        } else {
            showError(getString(R.string.Selectanyonepaymentmethod))
            false
        }
    }

    fun confirmPricePaymentFrag() {
        if (validaterListingTypeRadiobutton()) {
            if (checkValidation()) {
                if (validateradiobutton()) {
                    ////////to get edittext data and save to static class////////
                    val priceText: String = buynowprice.text.toString()
                    val startPrice: String = startprice.text.toString()
                    val reservedPrice: String = reserveprice.text.toString()

                    StaticClassAdCreate.price = priceText
                    StaticClassAdCreate.reservedPrice = reservedPrice
                    StaticClassAdCreate.startingPrice = startPrice

                    if (swicth_visa_mastercard.isChecked) {
                        StaticClassAdCreate.iscashpaid = "Cash"
                    }
                    if (bank.isChecked) {
                        StaticClassAdCreate.isbankpaid = "SA bank deposit"
                    }

                    if (listingtyp_rb1.isChecked) {

                        StaticClassAdCreate.listingType = "1"
                    } else if (listingtyp_rb2.isChecked) {
                        StaticClassAdCreate.listingType = "2"
                    } else if (listingtyp_rb3.isChecked) {
                        StaticClassAdCreate.listingType = "3"
                    }

                    startActivity(Intent(this, ListingDuration::class.java).apply {
                    })
                }
            }
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

    private fun disableTextFields() {


        with(listingtyp_rb1) {
            listingtyp_rb1.setOnCheckedChangeListener({ _, b ->
                if (b) {
                    listingtyp_rb2.isChecked = false
                    listingtyp_rb3.isChecked = false

                    startprice.isEnabled = false
                    startprice.isFocusableInTouchMode = false
                    startprice.setText("")

                    reserveprice.isEnabled = false
                    reserveprice.isFocusableInTouchMode = false
                    reserveprice.setText("")

                    fix_Price_l.setBackgroundResource(R.drawable.product_attribute_bg_linebg)

                    buynowprice.isEnabled = true
                    buynowprice.isFocusableInTouchMode = true






                    buynowprice.visibility = View.VISIBLE
                    Buy_now_l.visibility = View.VISIBLE
                    textview_price.visibility = View.VISIBLE
                    Buy_Auctino_l.visibility = View.GONE
                    Auction_price.visibility = View.GONE
                    auction_opt.visibility = View.GONE
                    textview_minimum_p.visibility = View.GONE
                    minimum_price_l.visibility = View.GONE
                    startprice.visibility = View.GONE
                    reserveprice.visibility = View.GONE
                    chooseOption.visibility = View.VISIBLE


                } else {

                    buynowprice.visibility = View.VISIBLE
                    Buy_now_l.visibility = View.GONE
                    Buy_Auctino_l.visibility = View.GONE
                    minimum_price_l.visibility = View.GONE
                    startprice.visibility = View.GONE
                    textview_price.visibility = View.GONE
                    reserveprice.visibility = View.GONE
                    reserveprice.visibility = View.GONE
                    chooseOption.visibility = View.GONE
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
        }


        listingtyp_rb2.setOnCheckedChangeListener({ _, b ->
            if (b) {
                listingtyp_rb1.isChecked = false
                listingtyp_rb3.isChecked = false

                startprice.isEnabled = true
                startprice.isFocusableInTouchMode = true

                reserveprice.isEnabled = true
                reserveprice.isFocusableInTouchMode = true

                buynowprice.isEnabled = false
                buynowprice.isFocusableInTouchMode = false
                buynowprice.setText("")



                auction_option.setBackgroundResource(R.drawable.product_attribute_bg_linebg)








                buynowprice.visibility = View.GONE
                Buy_now_l.visibility = View.GONE
                textview_price.visibility = View.GONE
                Buy_Auctino_l.visibility = View.VISIBLE
                startprice.visibility = View.VISIBLE
                Auction_price.visibility = View.VISIBLE
                auction_opt.visibility = View.VISIBLE
                reserveprice.visibility = View.VISIBLE
                textview_minimum_p.visibility = View.VISIBLE
                minimum_price_l.visibility = View.VISIBLE
                chooseOption.visibility = View.VISIBLE


            } else {

                buynowprice.visibility = View.GONE
                startprice.visibility = View.GONE
                reserveprice.visibility = View.GONE
                chooseOption.visibility = View.GONE
                Buy_Auctino_l.visibility = View.GONE
                startprice.visibility = View.GONE
                Auction_price.visibility = View.GONE
                auction_opt.visibility = View.GONE
                textview_minimum_p.visibility = View.GONE
                reserveprice.visibility = View.GONE
                minimum_price_l.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE
                auction_option.setBackgroundResource(R.drawable.add_product_attribte)


            }
        })
        listingtyp_rb3.setOnCheckedChangeListener({ _, b ->
            if (b) {
                listingtyp_rb1.isChecked = false
                listingtyp_rb2.isChecked = false

                startprice.isEnabled = true
                startprice.isFocusableInTouchMode = true

                reserveprice.isEnabled = true
                reserveprice.isFocusableInTouchMode = true

                Tender_l.setBackgroundResource(R.drawable.product_attribute_bg_linebg)

                buynowprice.isEnabled = true
                buynowprice.isFocusableInTouchMode = true


                Buy_now_l.visibility = View.VISIBLE
                textview_price.visibility = View.VISIBLE
                Buy_Auctino_l.visibility = View.VISIBLE
                startprice.visibility = View.VISIBLE
                Auction_price.visibility = View.VISIBLE
                auction_opt.visibility = View.VISIBLE
                textview_minimum_p.visibility = View.VISIBLE
                minimum_price_l.visibility = View.VISIBLE
                reserveprice.visibility = View.VISIBLE
                chooseOption.visibility = View.VISIBLE
                buynowprice.visibility = View.VISIBLE


            } else {

                buynowprice.visibility = View.GONE
                Buy_now_l.visibility = View.GONE
                textview_price.visibility = View.GONE
                Buy_Auctino_l.visibility = View.GONE
                startprice.visibility = View.GONE
                Auction_price.visibility = View.GONE
                auction_opt.visibility = View.GONE
                Tender_l.setBackgroundResource(R.drawable.add_product_attribte)
                textview_minimum_p.visibility = View.GONE
                minimum_price_l.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE
                reserveprice.visibility = View.GONE
                chooseOption.visibility = View.VISIBLE


            }
        })



        swicth_visa_mastercard.setOnCheckedChangeListener { _, b ->
            if (b) {
                saudi_bank_auction.visibility = View.VISIBLE
                saudi_bank_option.visibility = View.GONE
                swicth_saudi_bank_deposit.isChecked = false

                layout_visa_mastercard.background =
                    ContextCompat.getDrawable(this, R.drawable.product_attribute_bg_linebg)

            } else {

                layout_visa_mastercard.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)

            }
        }

        swicth_saudi_bank_deposit.setOnCheckedChangeListener { _, b ->
            if (b) {
                swicth_visa_mastercard.isChecked = false

                saudi_bank_auction.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE
                saudi_bank_deposit_switch.isChecked = true

            }
        }

        saudi_bank_deposit_switch.setOnCheckedChangeListener { _, b ->
            if (b) {
                swicth_visa_mastercard.isChecked = false
            } else {
                saudi_bank_option.visibility = View.GONE
                swicth_saudi_bank_deposit.isChecked = false

                saudi_bank_auction.visibility = View.VISIBLE
            }
        }
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
                HelpFunctions.ShowLongToast(getString(R.string.Startpriceshouldbesmaller), this)
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
                HelpFunctions.ShowLongToast(getString(R.string.Buynowmaxlimitreached), this)
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
                HelpFunctions.ShowLongToast(getString(R.string.Reservemaxlimitreached), this)
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

                HelpFunctions.ShowLongToast(getString(R.string.StartPricemaxlimitreached), this)

//                Toast.makeText(context, "Start Price max limit reached", Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            true
        }
    }

}