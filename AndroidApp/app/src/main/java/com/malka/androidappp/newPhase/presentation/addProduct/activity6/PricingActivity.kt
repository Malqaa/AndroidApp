package com.malka.androidappp.newPhase.presentation.addProduct.activity6

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Filter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.servicemodels.BankListRespone
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralRespone
import com.malka.androidappp.newPhase.presentation.addProduct.ConfirmationAddProductActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity7.ListingDurationActivity
import kotlinx.android.synthetic.main.add_account_layout.*
import kotlinx.android.synthetic.main.add_bank_layout.view.*
import kotlinx.android.synthetic.main.activity_pricing_payment.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call


class PricingActivity : BaseActivity() {
    var bankList: List<BankListRespone.BankDetail> = ArrayList()
    var isEdit: Boolean = false
    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java))
            finish()
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing_payment)
        toolbar_title.text = getString(R.string.sale_details)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setViewClickListeners()
        if (isEdit) {
            setData()
        }
        //  getBankAccount()
//        if(!AddProductObjectData.listingType.isEmpty()){
//            isEdit=true
//        }

        disableTextFields()

    }

    private fun setData() {
        when (AddProductObjectData.buyingType) {
            "1" -> {
                fixedPriceType_rb1.isChecked = true
                fixed_price_layout.isVisible = true
                buynowprice.setText(AddProductObjectData.price)
                fix_Price_l.setBackgroundResource(R.drawable.field_selection_border_enable)
                fixed_price_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))
            }
            "2" -> {

            }
            "12" -> {

            }
        }
        if (priceNegotiable_rb3.isChecked) {
            priceNegotiable_rb3.isChecked = true
            Tender_pickUp_l.setBackgroundResource(R.drawable.field_selection_border_enable)
            Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))
        } else {
            priceNegotiable_rb3.isChecked = false
            Tender_pickUp_l.setBackgroundResource(R.drawable.edittext_bg)
            Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color))

        }

        if (AddProductObjectData.isvisapaid) {
            swicth_visa_mastercard.isChecked = true
            layout_visa_mastercard.background =
                ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
            visa_mastercard.setTextColor(ContextCompat.getColor(this, R.color.bg))

        } else {
            swicth_visa_mastercard.isChecked = false
            layout_visa_mastercard.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_bg)
            visa_mastercard.setTextColor(ContextCompat.getColor(this, R.color.text_color))

        }


    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        btnnn.setOnClickListener {
            confirmPricePaymentFrag()
        }

        add_a_new_account.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun disableTextFields() {
        fixedPriceType_rb1.setOnCheckedChangeListener { _, b ->
            fixed_price_layout.isVisible = b
            if (b) {
                fix_Price_l.setBackgroundResource(R.drawable.field_selection_border_enable)
                fixed_price_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                fix_Price_l.setBackgroundResource(R.drawable.edittext_bg)
                fixed_price_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
        auctionType_rb2.setOnCheckedChangeListener { _, b ->
            Auction_layout.isVisible = b

            if (b) {
                auction_option.setBackgroundResource(R.drawable.field_selection_border_enable)
                Auction_price_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                auction_option.setBackgroundResource(R.drawable.edittext_bg)
                Auction_price_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color))


            }
        }
        priceNegotiable_rb3.setOnCheckedChangeListener { _, b ->
            if (b) {
                Tender_pickUp_l.setBackgroundResource(R.drawable.field_selection_border_enable)
                Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                Tender_pickUp_l.setBackgroundResource(R.drawable.edittext_bg)
                Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
        swicth_visa_mastercard.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                saudi_bank_option.visibility = View.GONE
                swicth_saudi_bank_deposit.isChecked = false

                layout_visa_mastercard.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                visa_mastercard.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {

                layout_visa_mastercard.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                visa_mastercard.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
        swicth_saudi_bank_deposit.setOnCheckedChangeListener { _, b ->
            if (b) {
                swicth_visa_mastercard.isChecked = false

                //  saudi_bank_auction.visibility = View.GONE
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

                // saudi_bank_auction.visibility = View.VISIBLE
            }
        }
        if (isEdit) {
            swicth_visa_mastercard.isChecked = AddProductObjectData.isvisapaid
            swicth_saudi_bank_deposit.isChecked = AddProductObjectData.isbankpaid
            priceNegotiable_rb3.isChecked = AddProductObjectData.isnegotiable
            buynowprice.setText(AddProductObjectData.price)
            startprice.setText(AddProductObjectData.startingPrice)
            reserveprice.setText(AddProductObjectData.reservedPrice)

            if (AddProductObjectData.buyingType.equals("1")) {
                fixedPriceType_rb1.isChecked = true
            } else {
                auctionType_rb2.isChecked = true
            }



            btnnn.setOnClickListener {
                confirmPricePaymentFrag()
            }
        }
    }


    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.add_account_layout)
        bottomSheetDialog.add_account_btn.setOnClickListener {
            addBankAccount(bottomSheetDialog)
        }
        bottomSheetDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()
    }

    private fun validaterSaleTypeRadiobutton(): Boolean {
//        return if (fixedPriceType_rb1.isChecked or auctionType_rb2.isChecked or priceNegotiable_rb3.isChecked) {
//            true
//        } else {
//            showError(getString(R.string.SelectSaleType))
//            false
//        }
        return if (fixedPriceType_rb1.isChecked or auctionType_rb2.isChecked) {
            true
        } else {
            showError(getString(R.string.SelectSaleType))
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
        return if (swicth_visa_mastercard.isChecked || AddProductObjectData.isbankpaid) {
            true
        } else {
            showError(getString(R.string.Selectanyonepaymentmethod))
            false
        }
    }

    fun confirmPricePaymentFrag() {
        if (validaterSaleTypeRadiobutton()) {
            if (checkValidation()) {
                if (validateradiobutton()) {
                    ////////to get edittext data and save to static class////////
                    val priceText: String = buynowprice.text.toString()
                    val startPrice: String = startprice.text.toString()
                    val reservedPrice: String = reserveprice.text.toString()
                    AddProductObjectData.price = priceText
                    AddProductObjectData.reservedPrice = reservedPrice
                    AddProductObjectData.startingPrice = startPrice
                    if (swicth_visa_mastercard.isChecked) {
                        AddProductObjectData.isvisapaid = true
                    }
                    AddProductObjectData.buyingType = ""

                    if (fixedPriceType_rb1.isChecked) {
                        AddProductObjectData.buyingType += "1"
                    }
                    if (auctionType_rb2.isChecked) {
                        AddProductObjectData.buyingType += "2"
                    }
                    AddProductObjectData.isnegotiable = priceNegotiable_rb3.isChecked


                    if (isEdit) {
                        startActivity(Intent(this, ConfirmationAddProductActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, ListingDurationActivity::class.java))
                    }

                }
            }
        }

    }

    private fun checkValidation(): Boolean {
        when {
            fixedPriceType_rb1.isChecked -> {
                if (!validateCheckPriceBox() or !limitBuyNowPrice()) {
                    return false
                }
            }
            auctionType_rb2.isChecked -> {
                if (!validateStartPriceBox() or !validateReservePriceBox() or !validAmount() or !limitReservePrice() or !limitStartPrice())
                    return false
            }
//            priceNegotiable_rb3.isChecked -> {
//                if (!validateCheckPriceBox() or !validateStartPriceBox() or !validateReservePriceBox()
//                    or !validAmount() or !limitBuyNowPrice() or !limitReservePrice()
//                    or !limitStartPrice()
//                )
//                    return false
//            }
        }
        return true
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


    fun addBankAccount(bottomSheetDialog: BottomSheetDialog) {

        HelpFunctions.startProgressBar(this)

        val malqa = RetrofitBuilder.GetRetrofitBuilder()

        val holder_name = bottomSheetDialog.account_holder_name.text.toString()
        val nameBank = bottomSheetDialog.bank_name.text.toString()
        val account_no = bottomSheetDialog.account_number.text.toString()
        val iban_no = bottomSheetDialog.iban_number.text.toString()

        val addBankAccount = BankListRespone.BankDetail(

            userBank_Name = holder_name,
            userBankAccount_Title = nameBank,
            userBankAccount_No = account_no,
            userBankAccount_IBN_Number = iban_no,
            userID = ConstantObjects.logged_userid

        )
        val call: Call<GeneralRespone> = malqa.addbankaccount(addBankAccount)

        call.enqueue(object : retrofit2.Callback<GeneralRespone?> {
            override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                Toast.makeText(this@PricingActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<GeneralRespone?>,
                response: retrofit2.Response<GeneralRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val respone: GeneralRespone = response.body()!!
                        if (respone.status_code == 200) {
                            bottomSheetDialog.dismiss()
                            getBankAccount()
                            Toast.makeText(
                                this@PricingActivity,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()

                        } else {

                            Toast.makeText(
                                this@PricingActivity,
                                respone.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }

    fun getBankAccount() {

        HelpFunctions.startProgressBar(this)

        val malqa = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getBankDetail(ConstantObjects.logged_userid)

        call.enqueue(object : retrofit2.Callback<BankListRespone?> {
            override fun onFailure(call: Call<BankListRespone?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

                Toast.makeText(this@PricingActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<BankListRespone?>,
                response: retrofit2.Response<BankListRespone?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: BankListRespone = response.body()!!
                        if (respone.status_code == 200) {
                            bankList = respone.data
                            addBankAdaptor(respone.data)
                        } else {

                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })


    }
    @SuppressLint("ResourceType")
    private fun addBankAdaptor(list: List<BankListRespone.BankDetail>) {
        addbank_rcv.adapter =
        object : GenericListAdapter<BankListRespone.BankDetail>(
            R.layout.add_bank_layout,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        bank_name.text = userBankAccount_Title
                        account_number.text = userBankAccount_No
                        user_name.text = userBank_Name
                        iban_number.text = userBankAccount_IBN_Number
                        bank.isChecked = isSelect

                        bank.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                AddProductObjectData.isbankpaid = true
                                list.forEach {
                                    it.isSelect = false
                                }
                                list.get(position).isSelect = true
                                addbank_rcv.post {
                                    addbank_rcv.adapter!!.notifyDataSetChanged()
                                }


                            }
                        }

                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }

}