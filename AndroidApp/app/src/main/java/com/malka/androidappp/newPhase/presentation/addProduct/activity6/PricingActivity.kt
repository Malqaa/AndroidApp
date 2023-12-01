package com.malka.androidappp.newPhase.presentation.addProduct.activity6

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.TimePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malka.androidappp.newPhase.domain.models.servicemodels.BankListRespone
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.addProduct.ConfirmationAddProductActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity7.ListingDurationActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.activity_pricing_payment.*
import kotlinx.android.synthetic.main.add_account_layout.*
import kotlinx.android.synthetic.main.add_bank_layout.view.*
import kotlinx.android.synthetic.main.dialog_add_discount.tvClosingAuctionCustomDataOption2
import kotlinx.android.synthetic.main.toolbar_main.*


class PricingActivity : BaseActivity() {
    var bankList: List<BankListRespone.BankDetail> = ArrayList()
    private lateinit var addProductViewModel: AddProductViewModel
    var bottomSheetDialog: BottomSheetDialog? = null

    //    var selectedAccountDetails: AccountDetails? = null
    var selectedAccountDetails: ArrayList<AccountDetails> = ArrayList()
    var isEdit: Boolean = false
    var selectdate = ""
    var selectTime = ""
    var dateString =""
    var fm: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pricing_payment)
        toolbar_title.text = getString(R.string.sale_details)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setViewClickListeners()
        disableTextFields()
        setUpViewModel()
        if (isEdit) {
            setData()
        }
        //  getBankAccount()
//        if(!AddProductObjectData.listingType.isEmpty()){
//            isEdit=true
//        }


    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        addProductViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.checkYourDataForamt),
                        this
                    )
                }
            }

        }
        addProductViewModel.isLoadingBackAccountList.observe(this) {
            if (it) {
                progressBarBankAccount.show()
            } else {
                progressBarBankAccount.hide()
            }
        }
        addProductViewModel.listBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.accountsList != null) {
                    AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
                        if (paymentOptionList.contains(AddProductObjectData.PAYMENT_OPTION_BANk) && AddProductObjectData.selectedAccountDetails != null) {
                            for (item in it.accountsList) {
//                                if (item.id == AddProductObjectData.selectedAccountDetails!!.id) {
//                                    item.isSelected = true
//                                    break
//                                }
                            }
                        }
                    }
                    addBankAdaptor(it.accountsList)
                }
            }
        }
        addProductViewModel.addBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }
                addProductViewModel.getBankAccountsList()
            }
        }
        addProductViewModel.getBankAccountsList()
    }

    private fun setData() {
        if (AddProductObjectData.priceFixedOption) {
            fixed_price_layout.isVisible = true
            fixedPriceType_rb1.isChecked = true
            fix_Price_l.setBackgroundResource(R.drawable.field_selection_border_enable)
            fixed_price_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))
            buynowprice.setText(AddProductObjectData.priceFixed)
        }
        if (AddProductObjectData.auctionOption) {
            Auction_layout.isVisible = true
            auctionType_rb2.isChecked = true
            auction_option.setBackgroundResource(R.drawable.field_selection_border_enable)
            Auction_price_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))
            startprice.setText(AddProductObjectData.auctionStartPrice)
            reserveprice.setText(AddProductObjectData.auctionMinPrice)
        }
        if (AddProductObjectData.isNegotiablePrice) {
            fixed_price_layout.isVisible = true
            priceNegotiable_rb3.isChecked = true
            switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
            tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))

        }
        AddProductObjectData.paymentOptionList?.let {
            for (item in it) {
                if (item == AddProductObjectData.PAYMENT_OPTION_CASH) {
                    saudi_bank_option.visibility = View.GONE
                    switchSaudiBankDeposit1.isChecked = false
                    switchCashPayment.isChecked = true
                    layoutCashPayment.background =
                        ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                    tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))
                } else if (item == AddProductObjectData.PAYMENT_OPTION_BANk) {
                    switchCashPayment.isChecked = false
                    switchSaudiBankDeposit1.isChecked = true
                    //  saudi_bank_auction.visibility = View.GONE
                    saudi_bank_option.visibility = View.VISIBLE
                    //switchSaudiBankDeposit2.isChecked = true
                    addProductViewModel.getBankAccountsList()
                }
            }
        }

//        if (AddProductObjectData.paymentOptionList == AddProductObjectData.PAYMENT_OPTION_CASH) {
//
//        }
//
//        if (AddProductObjectData.paymentOptionList == AddProductObjectData.PAYMENT_OPTION_BANk) {
//
//        }

    }

    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java))
            finish()
        } else {
            finish()
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        btnnn.setOnClickListener {
            confirmPricePaymentFrag()
        }
        btnAddNewAccount.setOnClickListener {
            showBottomSheetDialog()
        }
        switchSaudiBankDeposit1.setOnCheckedChangeListener { _, b ->
            if (b) {
                // switchCashPayment.isChecked = false
                // saudi_bank_auction.visibility = View.GONE
                saudi_bank_option.visibility = View.VISIBLE
                //switchSaudiBankDeposit2.isChecked = true
                addProductViewModel.getBankAccountsList()
            } else {
                saudi_bank_option.visibility = View.GONE
                //switchSaudiBankDeposit2.isChecked = false
            }
        }
//        switchSaudiBankDeposit2.setOnCheckedChangeListener { _, b ->
//            if (b) {
//                // switchCashPayment.isChecked = false
//                saudi_bank_auction.background =
//                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
//                tvBackAccount.setTextColor(ContextCompat.getColor(this, R.color.bg))
//                saudi_bank_option.visibility = View.VISIBLE
//                switchSaudiBankDeposit1.isChecked = true
//                addProductViewModel.getBankAccountsList()
//            } else {
//                saudi_bank_option.visibility = View.GONE
//                switchSaudiBankDeposit1.isChecked = false
//                saudi_bank_auction.background =
//                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
//                tvBackAccount.setTextColor(ContextCompat.getColor(this, R.color.text_color))
//                // saudi_bank_auction.visibility = View.VISIBLE
//            }
//        }
        switchCashPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                //saudi_bank_option.visibility = View.GONE
                // switchSaudiBankDeposit1.isChecked = false
                layoutCashPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                layoutCashPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
        switchMadaPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                //saudi_bank_option.visibility = View.GONE
                // switchSaudiBankDeposit1.isChecked = false
                layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
        switchCreditCard.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                //saudi_bank_option.visibility = View.GONE
                // switchSaudiBankDeposit1.isChecked = false
                layoutCreditCard.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                tvCreditCard.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                layoutCreditCard.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                tvCreditCard.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
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
                fixed_price_layout.isVisible = true
                switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
                tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
                tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }

    }

    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog?.setContentView(R.layout.add_account_layout)
        bottomSheetDialog?.add_account_btn?.setOnClickListener {
            //  addBankAccount(bottomSheetDialog)
            if (bottomSheetDialog != null)
                checkDataToAddBackAccount(bottomSheetDialog!!)
        }
        bottomSheetDialog?.etExpireDate?.setOnClickListener{
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                selectdate = selectdate_
                bottomSheetDialog?.etExpireDate?.text =selectdate
            }
            dateDialog.show(fm!!, "")
        }

        bottomSheetDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog?.show()
    }

    private fun checkDataToAddBackAccount(bottomSheetDialog: BottomSheetDialog) {
        var readyToAdd = true
        if (bottomSheetDialog.account_holder_name.text.toString().trim() == "") {
            readyToAdd = false
            bottomSheetDialog.account_holder_name.error =
                "${getString(R.string.enter)} ${getString(R.string.account_holder_s_name)}"
        }

        if (bottomSheetDialog.bank_name.text.toString().trim() == "") {
            readyToAdd = false
            bottomSheetDialog.bank_name.error = "${getString(R.string.enter)} ${getString(R.string.bank_name)}"
        }
        if (bottomSheetDialog.account_number.text.toString().trim() == "") {
            readyToAdd = false
            bottomSheetDialog.account_number.error =
                "${getString(R.string.enter)} ${getString(R.string.account_number)}"
        }
        if (bottomSheetDialog.etSwiftCode.text.toString().trim() == "") {
            readyToAdd = false
            bottomSheetDialog.etSwiftCode.error =
                "${getString(R.string.enter)} ${getString(R.string.swiftCode)}"
        }
        if (bottomSheetDialog.iban_number.text.toString().trim() == "") {
            readyToAdd = false
            bottomSheetDialog.iban_number.error = "${getString(R.string.enter)} ${getString(R.string.iban)}"
        }
        if (bottomSheetDialog.etExpireDate.text.toString().trim() == "") {
            readyToAdd = false
            bottomSheetDialog.etExpireDate.error =
                "${getString(R.string.enter)} ${getString(R.string.ExpiryDate)}"
        }
//        else if(bottomSheetDialog.etExpireDate.text.toString().trim().matches("(?:0[1-9]|1[0-2])/[0-9]{2}".toRegex())){
//            readyToAdd=false
//            bottomSheetDialog.etExpireDate.error=getString(R.string.please_enter_valid,getString(R.string.ExpiryDate))
//        }

        // "(?:0[1-9]|1[0-2])/[0-9]{2}"


        if (readyToAdd) {
            addProductViewModel.addBackAccountData(
                accountNumber = bottomSheetDialog.account_number.text.toString().trim(),
                bankName = bottomSheetDialog.bank_name.text.toString().trim(),
                bankHolderName = bottomSheetDialog.account_holder_name.text.toString().trim(),
                ibanNumber = bottomSheetDialog.iban_number.text.toString().trim(),
                swiftCode = bottomSheetDialog.etSwiftCode.text.toString().trim(),
                expiaryDate = bottomSheetDialog.etExpireDate.text.toString().trim(),
                SaveForLaterUse = bottomSheetDialog.switch_save_later.isChecked.toString()
            )
            bottomSheetDialog?.dismiss()
        }
    }


    @SuppressLint("ResourceType")
    private fun addBankAdaptor(list: List<AccountDetails>) {
        addbank_rcv.adapter = object : GenericListAdapter<AccountDetails>(
            R.layout.add_bank_layout,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        bank_name.text = bankName
                        account_number.text = accountNumber
                        user_name.text = bankHolderName
                        iban_number.text = ibanNumber
//                        bank.isChecked = isSelected
//                        bank.setOnCheckedChangeListener { buttonView, isChecked ->
////                                if(isChecked){
////                                    list[position].isSelected = false
////                                    selectedAccountDetails.remove(list[position])
////                                }else{
////                                    list[position].isSelected = true
////                                    selectedAccountDetails.add(list[position])
////
////                                }
////                                addbank_rcv.post {
////                                    addbank_rcv.adapter!!.notifyDataSetChanged()
////                                }
//                            if (isChecked) {
//                                list.forEach {
//                                    it.isSelected = false
//                                }
//                                list[position].isSelected = true
//                                selectedAccountDetails.add(list[position])
////                                    addbank_rcv.post {
////                                        addbank_rcv.adapter!!.notifyDataSetChanged()
////                                    }
//
//
//                            } else {
//
//                                list[position].isSelected = true
//                                selectedAccountDetails.add(list[position])
//                            }
//                        }

                    }
                    this.bank.setOnClickListener {
                        if (this.bank.isSelected) {
                            this.bank.isSelected=false
                            this.bank.isChecked=false
                            list[position].isSelected = false
                            selectedAccountDetails.removeAt(position)
                        } else {
                            this.bank.isSelected =true
                            this.bank.isChecked=true
                            list[position].isSelected = true
                            selectedAccountDetails.add(list[position])

                        }
//                        addbank_rcv.post {
//                            addbank_rcv.adapter!!.notifyDataSetChanged()
//                        }
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


    /****validate selected data**/
    fun confirmPricePaymentFrag() {
        if (validaterSaleTypeRadiobutton()) {
            if (checkValidation()) {
                if (validateradiobutton()) {
                    if (switchSaudiBankDeposit1.isChecked && selectedAccountDetails == null) {
                        showError(getString(R.string.selectBackAccount))
                    } else {
                        ////////to get edittext data and save to static class////////
                        val priceText: String = buynowprice.text.toString()
                        val startPrice: String = startprice.text.toString()
                        val reservedPrice: String = reserveprice.text.toString()
                        AddProductObjectData.priceFixed = priceText
                        AddProductObjectData.priceFixedOption = fixedPriceType_rb1.isChecked
                        AddProductObjectData.auctionOption = auctionType_rb2.isChecked
                        AddProductObjectData.auctionMinPrice = reservedPrice
                        AddProductObjectData.auctionStartPrice = startPrice
                        AddProductObjectData.selectedAccountDetails = null
                        var paymentOptionList: ArrayList<Int> = ArrayList()
                        if (switchSaudiBankDeposit1.isChecked && selectedAccountDetails != null) {
                            paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_BANk)
                            AddProductObjectData.selectedAccountDetails = selectedAccountDetails
                        }
                        if (switchCashPayment.isChecked) {
                            paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_CASH)
                        }
                        if (switchMadaPayment.isChecked) {
                            paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_Mada)
                        }
                        if (switchCreditCard.isChecked) {
                            paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_MasterCard)
                        }
                        AddProductObjectData.paymentOptionList = paymentOptionList
                        AddProductObjectData.isNegotiablePrice = priceNegotiable_rb3.isChecked


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

    }

    private fun validaterSaleTypeRadiobutton(): Boolean {
//        return if (fixedPriceType_rb1.isChecked or auctionType_rb2.isChecked or priceNegotiable_rb3.isChecked) {
//            true
//        } else {
//            showError(getString(R.string.SelectSaleType))
//            false
//        }

        return if (fixedPriceType_rb1.isChecked or auctionType_rb2.isChecked or priceNegotiable_rb3.isChecked) {
            true
        } else {
            showError(getString(R.string.SelectSaleType))
            false
        }
    }

    private fun checkValidation(): Boolean {
        var status = true
        if (fixedPriceType_rb1.isChecked && !validateCheckPriceBox()) {
            status = false
        } else if (auctionType_rb2.isChecked && !validateStartPriceBox() or !validateReservePriceBox()) {
            status = false
        }
//        when {
//            fixedPriceType_rb1.isChecked -> {
//                //!limitBuyNowPrice()
//                if (!validateCheckPriceBox()) {
//                    return  false
//                }
//            }
//            auctionType_rb2.isChecked -> {
//                if (!validateStartPriceBox() or !validateReservePriceBox() or !validAmount() or !limitReservePrice() or !limitStartPrice())
//                    return false
//            }
////            priceNegotiable_rb3.isChecked -> {
////                if (!validateCheckPriceBox() or !validateStartPriceBox() or !validateReservePriceBox()
////                    or !validAmount() or !limitBuyNowPrice() or !limitReservePrice()
////                    or !limitStartPrice()
////                )
////                    return false
////            }
//        }
        return status
    }

    private fun validateCheckPriceBox(): Boolean {
        val Inputname = buynowprice.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            buynowprice.error = getString(R.string.Fieldcantbeempty)
            //  showError(getString(R.string.Fieldcantbeempty))
            false
        } else {
            true
        }
    }

    private fun validateStartPriceBox(): Boolean {
        val InputStartPrice = startprice.text.toString().trim { it <= ' ' }
        return if (InputStartPrice.isEmpty()) {
            // showError(getString(R.string.Fieldcantbeempty))
            startprice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            true
        }
    }

    private fun validateReservePriceBox(): Boolean {
        val InputReservePrice = reserveprice.text.toString().trim { it <= ' ' }
        return if (InputReservePrice.isEmpty()) {
            reserveprice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            true
        }
    }

    /*************************/


    private fun validateradiobutton(): Boolean {
        var ready = true
        ready = if (switchCashPayment.isChecked || switchSaudiBankDeposit1.isChecked || switchCreditCard.isChecked ||switchMadaPayment.isChecked) {
            true
        } else {
            showError(getString(R.string.Selectanyonepaymentmethod))
            false
        }

        if (switchSaudiBankDeposit1.isChecked && selectedAccountDetails == null) {
            ready = false
            showError(getString(R.string.selectBackAccount))
        }

        return ready
    }


//    private fun validAmount(): Boolean {
//        return if (reserveprice.text.toString().trim()
//                .isNotEmpty() && startprice.text.toString().trim().isNotEmpty()
//        ) {
//            if (startprice.text.toString()
//                    .toBigInteger() < reserveprice.text.toString()
//                    .toBigInteger()
//            ) {
//                true
//            } else {
//                HelpFunctions.ShowLongToast(getString(R.string.Startpriceshouldbesmaller), this)
////                Toast.makeText(context, "Start price should be smaller", Toast.LENGTH_SHORT).show()
//                false
//            }
//        } else {
//            true
//        }
//    }

//    private fun limitBuyNowPrice(): Boolean {
//        return if (buynowprice.text.toString().trim().isNotEmpty()) {
//            if (buynowprice.text.toString().toBigInteger() <= 99999999.toBigInteger()) {
//                true
//            } else {
//                HelpFunctions.ShowLongToast(getString(R.string.Buynowmaxlimitreached), this)
//                false
//            }
//        } else {
//            true
//        }
//
//    }

//    private fun limitReservePrice(): Boolean {
//        return if (reserveprice.text.toString().trim().isNotEmpty()) {
//            if (reserveprice.text.toString().toBigInteger() <= 99999999.toBigInteger()) {
//                true
//            } else {
//                HelpFunctions.ShowLongToast(getString(R.string.Reservemaxlimitreached), this)
////                Toast.makeText(context, "Reserve max limit reached", Toast.LENGTH_SHORT).show()
//                false
//            }
//        } else {
//            true
//        }
//    }
//
//    private fun limitStartPrice(): Boolean {
//        return if (startprice.text.toString().trim().isNotEmpty()) {
//            if (startprice.text.toString()
//                    .toBigInteger() <= 70000000.toBigInteger()
//            ) {
//                true
//            } else {
//
//                HelpFunctions.ShowLongToast(getString(R.string.StartPricemaxlimitreached), this)
//
////                Toast.makeText(context, "Start Price max limit reached", Toast.LENGTH_SHORT).show()
//                false
//            }
//        } else {
//            true
//        }
//    }


}

//    fun addBankAccount(bottomSheetDialog: BottomSheetDialog) {
//
//        HelpFunctions.startProgressBar(this)
//
//        val malqa = getRetrofitBuilder()
//
//        val holder_name = bottomSheetDialog.account_holder_name.text.toString()
//        val nameBank = bottomSheetDialog.bank_name.text.toString()
//        val account_no = bottomSheetDialog.account_number.text.toString()
//        val iban_no = bottomSheetDialog.iban_number.text.toString()
//
//        val addBankAccount = BankListRespone.BankDetail(
//
//            userBank_Name = holder_name,
//            userBankAccount_Title = nameBank,
//            userBankAccount_No = account_no,
//            userBankAccount_IBN_Number = iban_no,
//            userID = ConstantObjects.logged_userid
//
//        )
//        val call: Call<GeneralRespone> = malqa.addbankaccount(addBankAccount)
//
//        call.enqueue(object : retrofit2.Callback<GeneralRespone?> {
//            override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                Toast.makeText(this@PricingActivity, "${t.message}", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onResponse(
//                call: Call<GeneralRespone?>,
//                response: retrofit2.Response<GeneralRespone?>
//            ) {
//                if (response.isSuccessful) {
//
//                    if (response.body() != null) {
//
//                        val respone: GeneralRespone = response.body()!!
//                        if (respone.status_code == 200) {
//                            bottomSheetDialog.dismiss()
//                            //getBankAccount()
//                            addProductViewModel.getBankAccountsList()
//                            Toast.makeText(
//                                this@PricingActivity,
//                                respone.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//
//                        } else {
//
//                            Toast.makeText(
//                                this@PricingActivity,
//                                respone.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//
//                }
//                HelpFunctions.dismissProgressBar()
//            }
//        })
//
//    }

//    fun getBankAccount() {
//
//        HelpFunctions.startProgressBar(this)
//
//        val malqa = getRetrofitBuilder()
//        val call = malqa.getBankDetail(ConstantObjects.logged_userid)
//
//        call.enqueue(object : retrofit2.Callback<BankListRespone?> {
//            override fun onFailure(call: Call<BankListRespone?>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//
//                Toast.makeText(this@PricingActivity, "${t.message}", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onResponse(
//                call: Call<BankListRespone?>,
//                response: retrofit2.Response<BankListRespone?>
//            ) {
//                if (response.isSuccessful) {
//
//                    if (response.body() != null) {
//                        val respone: BankListRespone = response.body()!!
//                        if (respone.status_code == 200) {
//                            bankList = respone.data
//                            addBankAdaptor(respone.data)
//                        } else {
//
//                        }
//                    }
//
//                }
//                HelpFunctions.dismissProgressBar()
//            }
//        })
//
//
//    }