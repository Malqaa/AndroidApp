package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6

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
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityPricingPaymentBinding
import com.malqaa.androidappp.databinding.AddAccountLayoutBinding
import com.malqaa.androidappp.databinding.AddBankLayoutBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData.Companion.selectedCategory
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7.ListingDurationActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class PricingActivity : BaseActivity<ActivityPricingPaymentBinding>() {

    private lateinit var addProductViewModel: AddProductViewModel
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var selectedAccountDetails: ArrayList<AccountDetails> = ArrayList()
    private var isEdit: Boolean = false
    private var selectdate = ""
    private var fm: FragmentManager? = null

    val adapterList = object : GenericListAdapter<AccountDetails>(
        R.layout.add_bank_layout,
        bind = { element, holder, itemCount, position ->
            // Use ViewBinding for the order_detail_design layout
            val itemBinding = AddBankLayoutBinding.bind(holder.itemView)
            holder.view.run {
                element.run {
                    itemBinding.bankName.text = bankName
                    itemBinding.accountNumber.text = accountNumber
                    itemBinding.userName.text = bankHolderName
                    itemBinding.ibanNumber.text = ibanNumber
                    itemBinding.bank.isSelected = isSelected
                    itemBinding.bank.isChecked = isSelected
                }

                itemBinding.bank.setOnClickListener {
                    if (itemBinding.bank.isSelected) {
                        selectedAccountDetails.forEach {
                            if (it.id == element.id)
                                element.isSelected = false
                        }
                        itemBinding.bank.isSelected = false
                        itemBinding.bank.isChecked = false
                        addBankAdaptor(selectedAccountDetails)
                    } else {
                        selectedAccountDetails.forEach {
                            if (it.id == element.id)
                                element.isSelected = true
                        }
                        itemBinding.bank.isSelected = true
                        itemBinding.bank.isChecked = true

                        addBankAdaptor(selectedAccountDetails)
                    }
                }
            }
        }
    ) {
        override fun getFilter(): Filter {
            TODO("Not yet implemented")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityPricingPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.sale_details)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)

        val fixedPriceSaleFee =
            getString(R.string.product_price_sar, selectedCategory?.enableFixedPriceSaleFee ?: 0)
        binding.testFixedPriceHelper.visibility = if((selectedCategory?.enableFixedPriceSaleFee ?: 0f) > 0f) View.VISIBLE else View.GONE
        binding.testFixedPriceHelper.text =getString(
            R.string.please_note_that_activating_it_will_cost_you,
            fixedPriceSaleFee
        )

        val auctionFee =
            getString(R.string.product_price_sar, selectedCategory?.enableAuctionFee ?: 0)
        binding.testAuctionPriceHelper.visibility = if((selectedCategory?.enableAuctionFee ?: 0f) > 0f) View.VISIBLE else View.GONE
        binding.testAuctionPriceHelper.text =getString(
            R.string.please_note_that_activating_it_will_cost_you,
            auctionFee
        )

        val negotiationFee =
            getString(R.string.product_price_sar, selectedCategory?.enableNegotiationFee ?: 0)
        binding.testNegotiationHelper.visibility = if((selectedCategory?.enableNegotiationFee ?: 0f) > 0f) View.VISIBLE else View.GONE
        binding.testNegotiationHelper.text =getString(
            R.string.please_note_that_activating_it_will_cost_you,
            negotiationFee
        )

        setViewClickListeners()
        disableTextFields()
        setUpViewModel()
        if (isEdit) {
            setDataEdit()
        } else {
            setDataInit()
        }
        if (ConstantObjects.isModify) {
            binding.fixedPriceTypeRb1.isEnabled = false
            binding.auctionTypeRb2.isEnabled = false
            binding.priceNegotiableRb3.isEnabled = false
            binding.buynowprice.isEnabled = false
            binding.startprice.isEnabled = false
            binding.reserveprice.isEnabled = false
        }
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
                binding.progressBarBankAccount.show()
            } else {
                binding.progressBarBankAccount.hide()
            }
        }
        addProductViewModel.listBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.accountsList != null) {
                    if (AddProductObjectData.paymentOptionList != null) {
                        AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
                            if (paymentOptionList.contains(AddProductObjectData.PAYMENT_OPTION_BANk) && AddProductObjectData.selectedAccountDetails != null) {
                                if (isEdit) {
                                    for (item2 in AddProductObjectData.selectedAccountDetails
                                        ?: arrayListOf()) {

                                        val matchingItem1 =
                                            it.accountsList.find { it.bankName == item2.bankName }
                                        matchingItem1?.let {
                                            // Update the properties of the matching item in list1
                                            it.isSelected = true
                                        }
                                    }

                                    addBankAdaptor(it.accountsList)
                                } else {
                                    addBankAdaptor(it.accountsList)
                                }
                            } else
                                addBankAdaptor(it.accountsList)
                        }
                    } else {
                        addBankAdaptor(it.accountsList)
                    }
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

    private fun setDataInit() {
        if (AddProductObjectData.selectedCategory?.enableNegotiation != false || AddProductObjectData.selectedCategory?.enableAuction != false || AddProductObjectData.selectedCategory?.enableFixedPrice != false) {
            binding.titleSaleType.isVisible = true
        }
        if (AddProductObjectData.selectedCategory?.enableFixedPrice == true) {
            binding.fixPriceL.isVisible = true
            binding.fixedPriceTypeRb1.isChecked = false
            binding.fixPriceL.setBackgroundResource(R.drawable.edittext_bg)
            binding.fixedPriceTv.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        } else {
            binding.fixedPriceLayout.isVisible = false
            binding.fixedPriceTypeRb1.isChecked = false
            binding.fixPriceL.setBackgroundResource(R.drawable.edittext_bg)
            binding.fixedPriceTv.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        }
        if (AddProductObjectData.selectedCategory?.enableAuction == true) {
            binding.AuctionLayout.isVisible = false
            binding.auctionOption.isVisible = true
            binding.auctionOption.setBackgroundResource(R.drawable.edittext_bg)
            binding.AuctionPriceTv.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        }
        if (AddProductObjectData.selectedCategory?.enableNegotiation == true) {
            binding.switchMustPickUp.isVisible = true
            binding.switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
            binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        }
    }

    private fun setDataEdit() {
        if (AddProductObjectData.priceFixedOption || AddProductObjectData.auctionOption || AddProductObjectData.isNegotiablePrice) {
            binding.titleSaleType.isVisible = true
        }
        if (AddProductObjectData.priceFixedOption) {
            binding.fixedPriceLayout.isVisible = true
            binding.fixedPriceTypeRb1.isChecked = true
            binding.fixPriceL.isVisible = true
            binding.fixedPriceLayout.isVisible = true
            binding.fixPriceL.setBackgroundResource(R.drawable.field_selection_border_enable)
            binding.fixedPriceTv.setTextColor(ContextCompat.getColor(this, R.color.bg))
            binding.buynowprice.setText(AddProductObjectData.priceFixed)
        } else {
            binding.fixedPriceLayout.isVisible = false
            binding.fixedPriceTypeRb1.isChecked = false
            binding.fixPriceL.setBackgroundResource(R.drawable.edittext_bg)
            binding.fixedPriceTv.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        }
        if (AddProductObjectData.auctionOption) {
            binding.AuctionLayout.isVisible = true
            binding.auctionTypeRb2.isChecked = true
            binding.auctionOption.isVisible = true
            binding.auctionOption.setBackgroundResource(R.drawable.field_selection_border_enable)
            binding.AuctionPriceTv.setTextColor(ContextCompat.getColor(this, R.color.bg))
            binding.startprice.setText(AddProductObjectData.auctionStartPrice)
            binding.reserveprice.setText(AddProductObjectData.auctionMinPrice)
        }
        if (AddProductObjectData.isNegotiablePrice) {
            binding.buynowprice.setText(AddProductObjectData.priceFixed)
            binding.priceNegotiableRb3.isChecked = true
            binding.switchMustPickUp.isVisible = true
            binding.switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
            binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        }
        AddProductObjectData.paymentOptionList?.let {
            for (item in it) {
                if (item == AddProductObjectData.PAYMENT_OPTION_CASH) {
                    binding.saudiBankOption.visibility = View.GONE
                    binding.switchSaudiBankDeposit1.isChecked = false
                    binding.switchCashPayment.isChecked = true
                    binding.layoutCashPayment.background =
                        ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                    binding.tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))
                } else if (item == AddProductObjectData.PAYMENT_OPTION_BANk) {
                    binding.switchCashPayment.isChecked = false
                    binding.switchSaudiBankDeposit1.isChecked = true
                    binding.saudiBankOption.visibility = View.VISIBLE
                    addProductViewModel.getBankAccountsList()
                } else if (item == AddProductObjectData.PAYMENT_OPTION_Mada) {
                    binding.switchMadaPayment.isChecked = true
                    binding.saudiBankOption.visibility = View.GONE
                    binding.switchSaudiBankDeposit1.isChecked = false
                    binding.layoutMadaPayment.background =
                        ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                    binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))
                } else if (item == AddProductObjectData.PAYMENT_OPTION_MasterCard) {
                    binding.switchCreditCard.isChecked = true
                    binding.saudiBankOption.visibility = View.GONE
                    binding.switchSaudiBankDeposit1.isChecked = false
                    binding.layoutCreditCard.background =
                        ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                    binding.tvCreditCard.setTextColor(ContextCompat.getColor(this, R.color.bg))
                }
            }
        }
    }

    override fun onBackPressed() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                putExtra("whereCome", "Add")
            })
            finish()
        } else {
            finish()
        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.btnnn.setOnClickListener {
            confirmPricePaymentFrag()
        }
        binding.btnAddNewAccount.setOnClickListener {
            showBottomSheetDialog()
        }
        binding.switchSaudiBankDeposit1.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.saudiBankOption.visibility = View.VISIBLE
                addProductViewModel.getBankAccountsList()
            } else {
                binding.saudiBankOption.visibility = View.GONE
            }
        }

        binding.switchCashPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                //saudi_bank_option.visibility = View.GONE
                // switchSaudiBankDeposit1.isChecked = false
                binding.layoutCashPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                binding.layoutCashPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
        }
        binding.switchMadaPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                //saudi_bank_option.visibility = View.GONE
                // switchSaudiBankDeposit1.isChecked = false
                binding.layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                binding.layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
        }
        binding.switchCreditCard.setOnCheckedChangeListener { _, b ->
            if (b) {
                // saudi_bank_auction.visibility = View.VISIBLE
                //saudi_bank_option.visibility = View.GONE
                // switchSaudiBankDeposit1.isChecked = false
                binding.layoutCreditCard.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvCreditCard.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                binding.layoutCreditCard.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvCreditCard.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
    }

    private fun disableTextFields() {
        binding.fixedPriceTypeRb1.setOnCheckedChangeListener { _, b ->
            if (binding.fixedPriceTypeRb1.isChecked)
                binding.fixedPriceLayout.isVisible = true
            else if (!binding.fixedPriceTypeRb1.isChecked && binding.priceNegotiableRb3.isChecked)
                binding.fixedPriceLayout.isVisible = false
            else if (!binding.fixedPriceTypeRb1.isChecked && !binding.priceNegotiableRb3.isChecked) {
                binding.fixedPriceLayout.isVisible = false
            }

            if (b) {
                binding.fixPriceL.setBackgroundResource(R.drawable.field_selection_border_enable)
                binding.fixedPriceTv.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                binding.fixPriceL.setBackgroundResource(R.drawable.edittext_bg)
                binding.fixedPriceTv.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
        }
        binding.auctionTypeRb2.setOnCheckedChangeListener { _, b ->
            binding.AuctionLayout.isVisible = b
            if (b) {
                binding.auctionOption.setBackgroundResource(R.drawable.field_selection_border_enable)
                binding.AuctionPriceTv.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                binding.auctionOption.setBackgroundResource(R.drawable.edittext_bg)
                binding.AuctionPriceTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_color
                    )
                )
            }
        }
        binding.priceNegotiableRb3.setOnCheckedChangeListener { _, b ->
            if (binding.priceNegotiableRb3.isChecked && binding.fixedPriceTypeRb1.isChecked) {
                binding.fixedPriceLayout.isVisible = true
            } else if (!binding.priceNegotiableRb3.isChecked && !binding.fixedPriceTypeRb1.isChecked) {
                binding.fixedPriceLayout.isVisible = false
            }

            if (b) {
                binding.switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
                binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
            } else {
                binding.switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
                binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
        }

    }

    private fun showBottomSheetDialog() {
        // Inflate the layout using ViewBinding
        val binding = AddAccountLayoutBinding.inflate(layoutInflater)

        // Initialize the BottomSheetDialog and set the view using binding.root
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(binding.root)

        // Handle button click event using ViewBinding
        binding.addAccountBtn.setOnClickListener {
            // Check and add bank account
            checkDataToAddBackAccount(bottomSheetDialog = bottomSheetDialog, binding = binding)
        }

        // Handle expire date click event using ViewBinding
        binding.etExpireDate.setOnClickListener {
            val fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectedDate ->
                selectdate = selectedDate
                binding.etExpireDate.text = selectdate
            }
            dateDialog.show(fm, "")
        }

        // Set background to transparent
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Show the dialog
        bottomSheetDialog.show()
    }

    private fun checkDataToAddBackAccount(
        bottomSheetDialog: BottomSheetDialog,
        binding: AddAccountLayoutBinding
    ) {
        var readyToAdd = true
        if (binding.accountHolderName.text.toString().trim() == "") {
            readyToAdd = false
            binding.accountHolderName.error =
                "${getString(R.string.enter)} ${getString(R.string.account_holder_s_name)}"
        }

        if (binding.bankName.text.toString().trim() == "") {
            readyToAdd = false
            binding.bankName.error =
                "${getString(R.string.enter)} ${getString(R.string.bank_name)}"
        }
        if (binding.accountNumber.text.toString().trim() == "") {
            readyToAdd = false
            binding.accountNumber.error =
                "${getString(R.string.enter)} ${getString(R.string.account_number)}"
        }
        if (binding.etSwiftCode.text.toString().trim() == "") {
            readyToAdd = false
            binding.etSwiftCode.error =
                "${getString(R.string.enter)} ${getString(R.string.swiftCode)}"
        }
        if (binding.ibanNumber.text.toString().trim() == "") {
            readyToAdd = false
            binding.ibanNumber.error =
                "${getString(R.string.enter)} ${getString(R.string.iban)}"
        }
        if (binding.etExpireDate.text.toString().trim() == "") {
            readyToAdd = false
            binding.etExpireDate.error =
                "${getString(R.string.enter)} ${getString(R.string.ExpiryDate)}"
        }

        if (readyToAdd) {
            addProductViewModel.addBackAccountData(
                accountNumber = binding.accountNumber.text.toString().trim(),
                bankName = binding.bankName.text.toString().trim(),
                bankHolderName = binding.accountHolderName.text.toString().trim(),
                ibanNumber = binding.ibanNumber.text.toString().trim(),
                swiftCode = binding.etSwiftCode.text.toString().trim(),
                expiryDate = binding.etExpireDate.text.toString().trim(),
                saveForLaterUse = binding.switchSaveLater.isChecked
            )
            bottomSheetDialog.dismiss()
        }
    }


    @SuppressLint("ResourceType")
    private fun addBankAdaptor(list: ArrayList<AccountDetails>) {
        selectedAccountDetails = list
        adapterList.updateAdapter(list)
        binding.addbankRcv.adapter = adapterList
    }

    /****validate selected data**/
    fun confirmPricePaymentFrag() {
        if (validaterSaleTypeRadiobutton()) {
            if (checkValidation()) {
                if ((AddProductObjectData.priceFixedOption || AddProductObjectData.auctionOption || AddProductObjectData.isNegotiablePrice) == false) {
                    callNextScreen()
                } else
                    if (validateradiobutton()) {
                        callNextScreen()
                    }
            }
        }

    }

    fun callNextScreen() {
        if (binding.switchSaudiBankDeposit1.isChecked && ((selectedAccountDetails.filter { it.isSelected }).isEmpty())) {
            showError(getString(R.string.selectBackAccount))
        } else {
            ////////to get edittext data and save to static class////////
            val priceText: String = binding.buynowprice.text.toString()
            val startPrice: String = binding.startprice.text.toString()
            val reservedPrice: String = binding.reserveprice.text.toString()
            AddProductObjectData.priceFixed = priceText
            AddProductObjectData.priceFixedOption = binding.fixedPriceTypeRb1.isChecked
            AddProductObjectData.auctionOption = binding.auctionTypeRb2.isChecked
            AddProductObjectData.auctionMinPrice = reservedPrice
            AddProductObjectData.auctionStartPrice = startPrice
            AddProductObjectData.selectedAccountDetails = null
            val paymentOptionList: ArrayList<Int> = ArrayList()
            if (binding.switchSaudiBankDeposit1.isChecked && selectedAccountDetails != null) {
                paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_BANk)
                AddProductObjectData.selectedAccountDetails = arrayListOf()
                AddProductObjectData.selectedAccountDetails?.addAll((selectedAccountDetails.filter { it.isSelected }))
            }
            if (binding.switchCashPayment.isChecked) {
                paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_CASH)
            }
            if (binding.switchMadaPayment.isChecked) {
                paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_Mada)
            }
            if (binding.switchCreditCard.isChecked) {
                paymentOptionList.add(AddProductObjectData.PAYMENT_OPTION_MasterCard)
            }
            AddProductObjectData.paymentOptionList = paymentOptionList
            AddProductObjectData.isNegotiablePrice =
                binding.priceNegotiableRb3.isChecked
            startActivity(Intent(this, ListingDurationActivity::class.java).apply {
                putExtra(ConstantObjects.isEditKey, isEdit)
                finish()
            })
        }
    }

    private fun validaterSaleTypeRadiobutton(): Boolean {
        return if (binding.fixedPriceTypeRb1.isChecked or binding.auctionTypeRb2.isChecked or binding.priceNegotiableRb3.isChecked) {
            true
        } else if (binding.titleSaleType.visibility == View.GONE) {

            true
        } else {
            showError(getString(R.string.SelectSaleType))
            false
        }
    }

    private fun checkValidation(): Boolean {
        var status = true
        if (binding.fixedPriceTypeRb1.isChecked && !validateCheckPriceBox()) {
            status = false
        } else if (binding.auctionTypeRb2.isChecked && !validateStartPriceBox() or !validateReservePriceBox()) {
            status = false
        }
        return status
    }

    private fun validateCheckPriceBox(): Boolean {
        val Inputname = binding.buynowprice.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            binding.buynowprice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            true
        }
    }

    private fun validateStartPriceBox(): Boolean {
        val InputStartPrice = binding.startprice.text.toString().trim { it <= ' ' }
        return if (InputStartPrice.isEmpty()) {
            binding.startprice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            true
        }
    }

    private fun validateReservePriceBox(): Boolean {
        val InputReservePrice = binding.reserveprice.text.toString().trim { it <= ' ' }
        return if (InputReservePrice.isEmpty()) {
            binding.reserveprice.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            true
        }
    }

    private fun validateradiobutton(): Boolean {
        var ready = true
        ready =
            if (binding.switchCashPayment.isChecked || binding.switchSaudiBankDeposit1.isChecked || binding.switchCreditCard.isChecked || binding.switchMadaPayment.isChecked) {
                true
            } else {
                showError(getString(R.string.Selectanyonepaymentmethod))
                false
            }

        if (binding.switchSaudiBankDeposit1.isChecked && selectedAccountDetails == null) {
            ready = false
            showError(getString(R.string.selectBackAccount))
        }

        return ready
    }

}
