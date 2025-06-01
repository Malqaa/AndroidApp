package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myWalletFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.AddCardBinding
import com.malqaa.androidappp.databinding.AllCardsLayoutBinding
import com.malqaa.androidappp.databinding.BottomSheetPaymentMethodBinding
import com.malqaa.androidappp.databinding.FragmentMyWalletFragmentBinding
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AccountObject
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetails
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletPendingOrders
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletTransactionsDetails
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.presentation.adapterShared.AccountDetailsAdapter
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.paymentMethod.PaymentMethodViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.formatAsCardNumber
import com.malqaa.androidappp.newPhase.utils.getMonth
import com.malqaa.androidappp.newPhase.utils.getYear
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import java.io.File
import java.util.Calendar


class MyWalletFragment : Fragment(R.layout.fragment_my_wallet_fragment),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var _binding: FragmentMyWalletFragmentBinding
    private val binding get() = _binding

    private lateinit var recentOperationsAdapter: RecentOperationsAdapter
    private lateinit var walletTransactionsList: ArrayList<WalletTransactionsDetails>
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var paymentMethodViewModel: PaymentMethodViewModel
    private var transactionType: String = ConstantObjects.transactionType_Out

    private lateinit var paymentAccountType: PaymentAccountType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyWalletFragmentBinding.bind(view)
        initView()
        setRecentOperationAdapter()
        setUpViewModel()
        setUpAddProductViewModel()
        setViewsClickListeners()
        setViewClickListeners()
        accountViewModel.getWalletDetailsInWallet()
    }

    private fun setData(walletDetails: WalletDetails) {
        binding.tvTotalBalance.text = walletDetails.walletBalance.toString()
        binding.tvPendingBalance.text = walletDetails.pendingBalance.toString()
        walletDetails.walletTransactionslist?.let {
            walletTransactionsList.clear()
            walletTransactionsList.addAll(it)
            recentOperationsAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        accountViewModel.isNetworkFail.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
        accountViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            it.message?.let { message ->
                HelpFunctions.ShowLongToast(message, requireActivity())
            } ?: HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
        }
        accountViewModel.walletDetailsObserver.observe(viewLifecycleOwner) { walletDetailsResp ->
            if (walletDetailsResp.status_code == 200) {
                AccountObject.walletDetails = walletDetailsResp.walletDetails
                binding.pendingLayout.setOnClickListener {

                    if (walletDetailsResp.walletDetails?.walletPendingOrders!!.isEmpty()) {
                        HelpFunctions.ShowLongToast("لا يوجد بيانات", requireContext())
                    } else {
                        showOrdersPopup(
                            it,
                            walletDetailsResp.walletDetails.walletPendingOrders,
                            requireContext()
                        )
                    }
                }
                walletDetailsResp.walletDetails?.let { setData(it) }
            }
        }
        accountViewModel.addWalletTransactionObserver.observe(viewLifecycleOwner) { addWalletTransaction ->
            if (addWalletTransaction.status_code == 200) {
                binding.etAmount.text = null
                onRefresh()
            } else {
                HelpFunctions.ShowLongToast(
                    addWalletTransaction.message ?: getString(R.string.serverError),
                    requireActivity()
                )
            }
        }
        accountViewModel.transferWalletToPointsObserver.observe(viewLifecycleOwner) { transferWalletToPoints ->
            if (transferWalletToPoints.status_code == 200) {
                HelpFunctions.ShowSucessAlert(
                    context = requireActivity(),
                    alertTitle = getString(R.string.convert_to_points),
                    showFailedMessage = true,
                    alertMessage = "${transferWalletToPoints.data.transactionAmount} ${"converted successfully to points"}"
                )
                binding.walletBalance.text = null
                binding.tvTotalBalance.text =
                    transferWalletToPoints.data.totalWalletBalance.toString()
                onRefresh()
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
    }

    private fun setUpAddProductViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        paymentMethodViewModel = ViewModelProvider(this).get(PaymentMethodViewModel::class.java)

        addProductViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        addProductViewModel.isNetworkFail.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
        addProductViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            it.message?.let { message ->
                HelpFunctions.ShowLongToast(message, requireActivity())
            } ?: HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
        }

        // =========================================================================================

        paymentMethodViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        paymentMethodViewModel.isNetworkFail.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
        paymentMethodViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            it.message?.let { message ->
                HelpFunctions.ShowLongToast(message, requireActivity())
            } ?: HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
        }
        paymentMethodViewModel.addBackAccountObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }

                allCardsBottomSheetDialog.dismiss()

                when (paymentAccountType) {
                    PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
                        addProductViewModel.getBankAccountsList(creditCards = true)
                    }

                    PaymentAccountType.BankAccount -> {
                        addProductViewModel.getBankAccountsList(paymentAccountType = PaymentAccountType.BankAccount.value)
                    }

                    else -> {
                        addProductViewModel.getBankAccountsList(creditCards = true)
                    }
                }
            }
        }

        // =========================================================================================
        addProductViewModel.listBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.accountsList != null) {
                    newAllCardsBottomSheetDialog(it.accountsList)
                    if (AddProductObjectData.paymentOptionList != null) {
                        AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
                            if (paymentOptionList.contains(AddProductObjectData.PAYMENT_OPTION_BANk) && AddProductObjectData.selectedAccountDetails != null) {
                                addAllCardsAdaptor(it.accountsList)
                            } else
                                addAllCardsAdaptor(it.accountsList)
                        }
                    } else {
                        addAllCardsAdaptor(it.accountsList)
                    }
                }
            }
        }
        addProductViewModel.addBackAccountObserver.observe(this) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }

                allCardsBottomSheetDialog.dismiss()
                addProductViewModel.getBankAccountsList(paymentAccountType.value)
            }
        }
    }

    private fun setViewClickListeners() {
        binding.chooseCard.setOnClickListener {
            paymentAccountType = PaymentAccountType.VisaMasterCard
            addProductViewModel.getBankAccountsList(creditCards = true)
        }

        binding.chooseAccount.setOnClickListener {
            paymentAccountType = PaymentAccountType.BankAccount
            addProductViewModel.getBankAccountsList(paymentAccountType = PaymentAccountType.BankAccount.value)
        }

        binding.selectedCard.btnChooseAnotherCard.setOnClickListener {
            addProductViewModel.getBankAccountsList(creditCards = true)
        }

        binding.selectedBankAccount.btnChooseAnotherCard.setOnClickListener {
            addProductViewModel.getBankAccountsList(paymentAccountType = PaymentAccountType.BankAccount.value)
        }
    }

    private fun setRecentOperationAdapter() {
        walletTransactionsList = ArrayList()
        recentOperationsAdapter = RecentOperationsAdapter(walletTransactionsList)
        binding.rvWalletRecentOperation.apply {
            adapter = recentOperationsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            isNestedScrollingEnabled = false
        }
    }

    private fun initView() {
        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_wallet)
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)

        accountDetailsAdapter = AccountDetailsAdapter { selectedItem ->
            accountDetails = selectedItem // Save selected item globally
            Log.d("test #1", "accountDetails: $selectedItem")
        }

        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    ibanCertificateUri = it
                    val fileName = getFileNameFromUri(requireContext(), it)
                    bottomSheetPaymentMethodBinding.textFileName.text = fileName
                }
            }
    }

    private fun setViewsClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.balanceWithdraw.setOnClickListener {
            transactionType = ConstantObjects.transactionType_Out
            binding.chooseAccount.show()
            binding.chooseCard.hide()
            binding.selectedCard.linearLayoutSelectedPaymentOptions.hide()


            binding.rechargeTheBalance.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.balanceWithdraw.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.btnAddTranasction.text = getString(R.string.withdraw_a_balance)
            binding.balanceWithdraw.setTextColor("#FFFFFF".toColorInt())
            binding.rechargeTheBalance.setTextColor("#45495E".toColorInt())
        }

        binding.rechargeTheBalance.setOnClickListener {
            transactionType = ConstantObjects.transactionType_In
            binding.chooseCard.show()
            binding.chooseAccount.hide()
            binding.selectedBankAccount.linearLayoutSelectedPaymentOptions.hide()

            binding.balanceWithdraw.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.rechargeTheBalance.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.btnAddTranasction.text = getString(R.string.recharge_balance)
            binding.rechargeTheBalance.setTextColor("#FFFFFF".toColorInt())
            binding.balanceWithdraw.setTextColor("#45495E".toColorInt())
        }

        binding.btnAddTranasction.setOnClickListener {
            if (binding.etAmount.text.toString().trim().isEmpty()) {
                binding.etAmount.error = getString(R.string.enterAmount)
            } else {
                val amount = binding.etAmount.text.toString().toFloat()
                if (transactionType == ConstantObjects.transactionType_Out && amount > binding.tvTotalBalance.text.toString()
                        .toFloat()
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cannotWithdrawal),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (::paymentAccountType.isInitialized || accountDetails != null) {
                        when (paymentAccountType) {
                            PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
                                accountViewModel.addWalletTransaction(
                                    transactionSource = if (transactionType == ConstantObjects.transactionType_Out) "3" else "1",
                                    transactionType = transactionType,
                                    transactionAmount = binding.etAmount.text.toString().trim()
                                        .toDouble(),
                                    paymentCardNumber = accountDetails?.accountNumber,
                                    paymentCardExpiryMonth = accountDetails?.expiaryDate?.getMonth(),
                                    paymentCardExpiryYear = accountDetails?.expiaryDate?.getYear(),
                                    paymentCardSecurityCode = accountDetails?.cvv.toString(),
                                    paymentCardHolderName = accountDetails?.bankHolderName,
                                    paymentMethodId = accountDetails?.paymentAccountType?.value.toString(),
                                    totalAmount = binding.etAmount.text.toString().trim()
                                        .toDouble(),
                                )
                            }

                            PaymentAccountType.BankAccount -> {
                                accountViewModel.addWalletTransaction(
                                    transactionSource = if (transactionType == ConstantObjects.transactionType_Out) "3" else "1",
                                    transactionType = transactionType,
                                    transactionAmount = binding.etAmount.text.toString().trim()
                                        .toDouble(),
                                    withdrawBankTAccountId = accountDetails?.id
                                )
                            }
                        }
                    } else {
                        // Handle the case where paymentAccountType is not initialized
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.please_choose_a_card),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
        }
        binding.btnConvertion.setOnClickListener {
            if (binding.walletBalance.text.toString().trim().isEmpty()) {
                binding.walletBalance.error = getString(R.string.enterAmount)
            } else {
                accountViewModel.getTransferWalletToPoints(
                    binding.walletBalance.text.toString().toInt()
                )
            }
        }
    }

    private fun showOrdersPopup(
        anchorView: View,
        orders: List<WalletPendingOrders>,
        context: Context
    ) {
        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_purchase_orders, null)
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.ordersRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PurchaseOrderAdapter(orders)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(anchorView, 0, 10)
    }

    private lateinit var allCardsLayoutBinding: AllCardsLayoutBinding
    private lateinit var allCardsBottomSheetDialog: BottomSheetDialog
    private var accountDetails: AccountDetails? = null
    private lateinit var accountDetailsAdapter: AccountDetailsAdapter
    private lateinit var addProductViewModel: AddProductViewModel
    private var selectedAccountDetails: ArrayList<AccountDetails> = ArrayList()
    private var bottomSheetDialog: BottomSheetDialog? = null

    private fun newAllCardsBottomSheetDialog(list: ArrayList<AccountDetails>?) {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(requireContext())
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        val (title, description) = when (paymentAccountType) {
            PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> getString(R.string.all_cards) to getString(
                R.string.there_are_no_cards_saved
            )

            PaymentAccountType.BankAccount -> getString(R.string.choose_the_bank_account) to getString(
                R.string.there_are_no_bank_accounts_saved
            )

            else -> getString(R.string.all_cards) to getString(R.string.there_are_no_cards_saved)
        }

        allCardsLayoutBinding.title.text = title

        if (list?.size!! > 0) {
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
            allCardsLayoutBinding.recyclerViewAllCards.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = accountDetailsAdapter
            }
        } else {
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.text = description
            allCardsLayoutBinding.descText.show()
        }

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener {
                when (paymentAccountType) {
                    PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
                        addCardBottomSheetDialog()
                    }

                    PaymentAccountType.BankAccount -> {
                        showBottomSheetDialog()
                    }
                }
            }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = requireContext()
                    )
                    return@setOnClickListener
                }

                when (paymentAccountType) {
                    PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
                        if (cvv.toString().length !in 3..4) {
                            HelpFunctions.ShowLongToast(
                                getString(R.string.please_enter_cvv),
                                context = requireContext()
                            )
                            return@setOnClickListener
                        }

                        _binding.chooseCard.hide()
                        _binding.selectedCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedCard.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }

                    PaymentAccountType.BankAccount -> {
                        _binding.chooseAccount.hide()
                        _binding.selectedBankAccount.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedBankAccount.apply {
                            textAccountNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textBankName.text = accountDetails!!.bankName
                            textBankHolders.text = accountDetails!!.bankHolderName
                            textIban.text = accountDetails!!.ibanNumber
                            textSwiftCode.text = accountDetails!!.swiftCode
                        }
                    }
                }

                allCardsBottomSheetDialog.dismiss()
            }
        }

        addProductViewModel.isLoadingBackAccountList.observe(this) {
            if (it) {
                allCardsLayoutBinding.progressBarAllCards.show()
            } else {
                allCardsLayoutBinding.progressBarAllCards.hide()
            }
        }

        // Set background to transparent
        allCardsBottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Show the dialog
        allCardsBottomSheetDialog.show()
    }

    private lateinit var addCardBinding: AddCardBinding
    private lateinit var addCardBottomSheetDialog: BottomSheetDialog

    private fun addCardBottomSheetDialog() {
        // Inflate the layout using ViewBinding
        addCardBinding = AddCardBinding.inflate(layoutInflater)

        // Initialize the BottomSheetDialog and set the view using binding.root
        addCardBottomSheetDialog = BottomSheetDialog(requireContext())
        addCardBottomSheetDialog.setContentView(addCardBinding.root)

        paymentAccountType = PaymentAccountType.VisaMasterCard
        // Set radio buttons
        addCardBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedText =
                addCardBinding.root.findViewById<RadioButton>(checkedId)?.text?.toString() ?: ""

            when (selectedText) {
                getString(R.string.visa_credit_card) -> {
                    paymentAccountType = PaymentAccountType.VisaMasterCard
                }

                getString(R.string.MadaPayment) -> {
                    paymentAccountType = PaymentAccountType.Mada
                }
            }
        }

        addCardBinding.cardExpiryTv.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return

                val input = s.toString().replace("/", "")
                if (input == current || input.length > 6) return

                val formatted = buildString {
                    for (i in input.indices) {
                        append(input[i])
                        if (i == 1 && input.length > 2) append("/")
                    }
                }

                current = input
                addCardBinding.cardExpiryTv.removeTextChangedListener(this)
                addCardBinding.cardExpiryTv.setText(formatted)
                addCardBinding.cardExpiryTv.setSelection(formatted.length)
                addCardBinding.cardExpiryTv.addTextChangedListener(this)
            }
        })


        // Handle button click event using ViewBinding
        addCardBinding.buttonAdd.setOnClickListener {
            val isSaveLater = addCardBinding.switchSaveLater.isChecked

            // Check and add bank account
            checkAddCard(
                bottomSheetDialog = addCardBottomSheetDialog,
                binding = addCardBinding,
                isSaveLater = isSaveLater
            )

        }

        addCardBinding.buttonCancel.setOnClickListener {
            addCardBottomSheetDialog.dismiss()
        }

        // Set background to transparent
        addCardBottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Show the dialog
        addCardBottomSheetDialog.show()
    }

    // TODO 01: handel this function (check bank & card)
    private fun checkAddCard(
        bottomSheetDialog: BottomSheetDialog,
        binding: AddCardBinding,
        isSaveLater: Boolean
    ) {
        var readyToAdd = true

        val holderName = binding.cardHolderTv.text.toString().trim()
        val cardNumber = binding.CardnoTv.text.toString().trim()
        val expiryText = binding.cardExpiryTv.text.toString().trim()
        val cvv = binding.cvvTv.text.toString().trim()

        if (holderName.isEmpty()) {
            readyToAdd = false
            binding.cardHolderTv.error =
                "${getString(R.string.enter)} ${getString(R.string.account_holder_s_name)}"
        }

        if (cardNumber.isEmpty()) {
            readyToAdd = false
            binding.CardnoTv.error =
                "${getString(R.string.enter)} ${getString(R.string.Cardno)}"
        } else if (cardNumber.filter { it.isDigit() }.length < 16) {
            readyToAdd = false
            binding.CardnoTv.error = getString(R.string.card_number_must_be_16_digits)
        }

        if (expiryText.isEmpty()) {
            readyToAdd = false
            binding.cardExpiryTv.error =
                "${getString(R.string.enter)} ${getString(R.string.ExpiryDate)}"
        } else {
            // Validate format: MM/YYYY
            val regex = Regex("""^(0[1-9]|1[0-2])/(\d{4})$""")
            if (!regex.matches(expiryText)) {
                readyToAdd = false
                binding.cardExpiryTv.error =
                    getString(R.string.invalid_date_format) // add this string resource
            } else {
                // Check if expiry date is in the future
                val (monthStr, yearStr) = expiryText.split("/")
                val enteredMonth = monthStr.toInt()
                val enteredYear = yearStr.toInt()

                val current = Calendar.getInstance()
                val currentYear = current.get(Calendar.YEAR)
                val currentMonth = current.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-based

                if (enteredYear < currentYear || (enteredYear == currentYear && enteredMonth < currentMonth)) {
                    readyToAdd = false
                    binding.cardExpiryTv.error =
                        getString(R.string.expiry_date_passed) // add this string resource
                }
            }
        }

        if (cvv.isEmpty() || cvv.length < 3) {
            readyToAdd = false
            binding.cvvTv.error = getString(R.string.please_enter_cvv)
        }

        if (readyToAdd) {
            if (isSaveLater) {
                // You should have an update function in your ViewModel like:
                paymentMethodViewModel.addBankAccountData(
                    accountNumber = cardNumber,
                    bankHolderName = holderName,
                    expiryDate = expiryText,
                    saveForLaterUse = binding.switchSaveLater.isChecked,
                    paymentAccountType = paymentAccountType.value.toString()
                )
            }

            accountDetails = AccountDetails(
                id = 1,
                bankAccountId = 1,
                isSelected = true,
                bankHolderName = holderName,
                accountNumber = cardNumber,
                expiaryDate = expiryText,
                cvv = cvv.toInt(),
                paymentAccountType = paymentAccountType
            )

            allCardsLayoutBinding.apply {
                when (paymentAccountType) {
                    PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
                        _binding.chooseCard.hide()
                        _binding.selectedCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedCard.apply {
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }

                    PaymentAccountType.BankAccount -> {
                        _binding.chooseAccount.hide()
                        _binding.selectedBankAccount.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedBankAccount.apply {
                            textAccountNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textBankName.text = accountDetails!!.bankName
                            textBankHolders.text = accountDetails!!.bankHolderName
                            textIban.text = accountDetails!!.ibanNumber
                            textSwiftCode.text = accountDetails!!.swiftCode
                        }
                    }
                }
            }

            bottomSheetDialog.dismiss()
            allCardsBottomSheetDialog.dismiss()
        }
    }

    @SuppressLint("ResourceType")
    private fun addAllCardsAdaptor(list: ArrayList<AccountDetails>) {
        selectedAccountDetails = list
        accountDetailsAdapter.updateAdapter(list)
    }

    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private var ibanCertificateUri: Uri? = null
    private lateinit var bottomSheetPaymentMethodBinding: BottomSheetPaymentMethodBinding

    private fun showBottomSheetDialog() {
        val binding = BottomSheetPaymentMethodBinding.inflate(layoutInflater)
        bottomSheetPaymentMethodBinding = binding
        bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val layoutCardDetails = binding.root.findViewById<LinearLayout>(R.id.layout_card_details)
        val layoutBankTransfer = binding.root.findViewById<LinearLayout>(R.id.layout_bank_transfer)

        // init data set
        binding.textTitle.text = getString(R.string.add_new_bank_account)
        binding.radioGroup.visibility = View.GONE
        layoutCardDetails.visibility = View.GONE
        layoutBankTransfer.visibility = View.VISIBLE
        paymentAccountType = PaymentAccountType.BankAccount

        binding.buttonUpload.setOnClickListener {
            // Handle upload button click
            filePickerLauncher.launch(arrayOf("application/pdf", "image/*"))
        }

        binding.addAccountBtn.text = getString(R.string.Add)

        binding.addAccountBtn.setOnClickListener {
            checkDataToAddBackAccount(
                bottomSheetDialog = bottomSheetDialog!!,
                binding = binding,
                paymentAccountType = paymentAccountType,
            )
        }

        bottomSheetDialog!!.show()
    }

    private fun checkDataToAddBackAccount(
        bottomSheetDialog: BottomSheetDialog,
        binding: BottomSheetPaymentMethodBinding,
        paymentAccountType: PaymentAccountType = PaymentAccountType.BankAccount
    ) {
        var readyToAdd = true

        // Visa & Mada Fields
        val cardHolderName = binding.editTextCardHolderName.text.toString().trim()
        val cardNumber = binding.editTextCardNumber.text.toString().trim()
        val expiryDate = binding.editTextExpiryDate.text.toString().trim()

        // Bank Transfer Fields
        val bankName = binding.editTextBankName.text.toString().trim()
        val bankAccountNumber = binding.editTextBankAccountNumber.text.toString().trim()
        val bankHoldersName = binding.editTextBankHolderSName.text.toString().trim()
        val iban = binding.editTextIban.text.toString().trim()
        val swiftCode = binding.editTextSwiftCode.text.toString().trim()

        val accountNumber = cardNumber.ifEmpty { bankAccountNumber }
        val bankHolderName = cardHolderName.ifEmpty { bankHoldersName }

        when (paymentAccountType) {
            PaymentAccountType.BankAccount -> {
                if (bankName.isEmpty()) {
                    readyToAdd = false
                    binding.editTextBankName.error =
                        "${getString(R.string.enter)} ${getString(R.string.bank_name)}"
                }
                if (bankAccountNumber.isEmpty()) {
                    readyToAdd = false
                    binding.editTextBankAccountNumber.error =
                        "${getString(R.string.enter)} ${getString(R.string.bank_account_number)}"
                }
                if (bankHoldersName.isEmpty()) {
                    readyToAdd = false
                    binding.editTextBankHolderSName.error =
                        "${getString(R.string.enter)} ${getString(R.string.bank_holder_s_name)}"
                }
                if (iban.isEmpty()) {
                    readyToAdd = false
                    binding.editTextIban.error =
                        "${getString(R.string.enter)} ${getString(R.string.iban)}"
                }
                if (ibanCertificateUri == null) {
                    readyToAdd = false
                    binding.textFileName.error =
                        "${getString(R.string.enter)} ${getString(R.string.iban_certificate)}"
                }
            }
        }

        if (readyToAdd) {
            ibanCertificateUri?.let { uri ->
                val fileName = getFileNameFromUri(requireContext(), uri)

                // OPTIONAL: Copy content to temp file if API needs a java.io.File
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val tempFile = File(requireContext().cacheDir, fileName)
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                paymentMethodViewModel.addBankAccountData(
                    accountNumber = accountNumber,
                    bankName = bankName,
                    bankHolderName = bankHolderName,
                    ibanNumber = iban,
                    swiftCode = swiftCode,
                    expiryDate = expiryDate,
                    ibanCertificate = fileName,
                    ibanCertificateFile = tempFile, // This is a real java.io.File
                    saveForLaterUse = binding.switchSaveLater.isChecked,
                    paymentAccountType = PaymentAccountType.BankAccount.value.toString()
                )
            }

            bottomSheetDialog.dismiss()
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var name = ""
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)
        }
        return name
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        accountViewModel.getWalletDetailsInWallet()
    }
}
