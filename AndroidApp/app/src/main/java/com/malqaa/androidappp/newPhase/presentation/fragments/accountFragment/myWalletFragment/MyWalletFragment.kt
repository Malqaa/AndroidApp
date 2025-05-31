package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myWalletFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
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
import com.malqaa.androidappp.databinding.FragmentMyWalletFragmentBinding
import com.malqaa.androidappp.databinding.ItemCardBinding
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AccountObject
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetails
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletPendingOrders
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletTransactionsDetails
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.paymentMethod.PaymentMethodViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.formatAsCardNumber
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
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
                // TODO 01: change this code
                paymentMethodViewModel.getBankAccountsList()
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
                addProductViewModel.getBankAccountsList(paymentAccountType.value)
            }
        }
    }

    private fun setViewClickListeners() {
        binding.chooseCard.setOnClickListener {
            addProductViewModel.getBankAccountsList(creditCards = true)
            paymentAccountType = PaymentAccountType.VisaMasterCard
        }

        binding.chooseAccount.setOnClickListener {
            paymentAccountType = PaymentAccountType.BankAccount
            addProductViewModel.getBankAccountsList(paymentAccountType = PaymentAccountType.BankAccount.value)
        }

        binding.selectedCard.btnChooseAnotherCard.setOnClickListener {
            addProductViewModel.getBankAccountsList(creditCards = true)
            allCardsBottomSheetDialog()
        }

        // TODO: add selected bank account
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

        setupAdapter()
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
                    accountViewModel.addWalletTransaction(
                        if (transactionType == ConstantObjects.transactionType_Out) "3" else "1",
                        transactionType,
                        binding.etAmount.text.toString().trim()
                    )
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
    private lateinit var adapterList: GenericListAdapter<AccountDetails>
    private var accountDetails: AccountDetails? = null
    private lateinit var addProductViewModel: AddProductViewModel
    private var selectedAccountDetails: ArrayList<AccountDetails> = ArrayList()
    private var bottomSheetDialog: BottomSheetDialog? = null

    private fun allCardsBottomSheetDialog() {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(requireContext())
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        if (adapterList.itemCount > 0) {
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
            allCardsLayoutBinding.recyclerViewAllCards.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterList
            }
        } else {
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.show()
        }

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener { addCardBottomSheetDialog() }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = requireContext()
                    )
                    return@setOnClickListener
                } else if (cvv.toString().length !in 3..4) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_enter_cvv),
                        context = requireContext()
                    )
                    return@setOnClickListener
                }

                allCardsBottomSheetDialog.dismiss()

                when (paymentAccountType) {
                    PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
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
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }
                }
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

    private fun newAllCardsBottomSheetDialog(list: ArrayList<AccountDetails>?) {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(requireContext())
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        if (list?.size!! > 0) {
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
            allCardsLayoutBinding.recyclerViewAllCards.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterList
            }
        } else {
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.show()
        }

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener { addCardBottomSheetDialog() }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = requireContext()
                    )
                    return@setOnClickListener
                } else if (cvv.toString().length !in 3..4) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_enter_cvv),
                        context = requireContext()
                    )
                    return@setOnClickListener
                }

                allCardsBottomSheetDialog.dismiss()

                when (paymentAccountType) {
                    PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
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
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }
                }
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
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }
                }

            }

            bottomSheetDialog.dismiss()
            allCardsBottomSheetDialog.dismiss()
        }
    }

    private fun setupAdapter() {
        adapterList = object : GenericListAdapter<AccountDetails>(
            R.layout.item_card,
            bind = { element, holder, itemCount, position ->
                val itemBinding = ItemCardBinding.bind(holder.itemView)

                holder.view.run {
                    element.run {
                        itemBinding.textCardHoldersName.text = bankHolderName
                        itemBinding.textCardNumber.text = accountNumber.formatAsCardNumber()
                        itemBinding.textExpiryDate.text = expiaryDate
                        itemBinding.radioButtonCard.isSelected = isSelected
                        itemBinding.radioButtonCard.isChecked = isSelected

                        itemBinding.editTextCvv.setText(
                            if (cvv != 0) cvv.toString() else ""
                        )

                        // Enable/disable based on selection
                        itemBinding.editTextCvv.isEnabled = element.isSelected
                        itemBinding.editTextCvv.isFocusable = element.isSelected
                        itemBinding.editTextCvv.isFocusableInTouchMode = element.isSelected
                    }

                    itemBinding.editTextCvv.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val newCvv = s?.toString()?.toIntOrNull()
                            if (newCvv != null && newCvv.toString().length <= 3) {
                                element.cvv = newCvv
                            }
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }
                    })

                    itemBinding.radioButtonCard.setOnClickListener {
                        val enteredCvv = itemBinding.editTextCvv.text.toString().toIntOrNull()
                        if (enteredCvv != null) {
                            element.cvv = enteredCvv
                        }

                        val previousSelectedPosition =
                            selectedAccountDetails.indexOfFirst { it.isSelected }
                        selectedAccountDetails.forEach { it.isSelected = false }
                        element.isSelected = true
                        accountDetails = element

                        if (previousSelectedPosition != -1) {
                            adapterList.notifyItemChanged(previousSelectedPosition)
                        }
                        adapterList.notifyItemChanged(position)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun addAllCardsAdaptor(list: ArrayList<AccountDetails>) {
        selectedAccountDetails = list
        adapterList.updateAdapter(list)
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        accountViewModel.getWalletDetailsInWallet()
    }
}
