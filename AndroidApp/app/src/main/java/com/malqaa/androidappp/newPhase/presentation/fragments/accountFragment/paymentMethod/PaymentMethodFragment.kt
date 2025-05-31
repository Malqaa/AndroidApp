package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.paymentMethod

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.BottomSheetPaymentMethodBinding
import com.malqaa.androidappp.databinding.FragmentPaymentMethodBinding
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.BankTransfers
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.BankTransfersListResponse
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.EditBankTransfer
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.ShowDeleteAlert
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.Calendar

class PaymentMethodFragment : Fragment(R.layout.fragment_payment_method) {

    // Declare the binding object
    private var _binding: FragmentPaymentMethodBinding? = null
    private val binding get() = _binding!!

    private lateinit var paymentMethodViewModel: PaymentMethodViewModel

    private lateinit var adapter: PaymentMethodAdapter

    private var bottomSheetDialog: BottomSheetDialog? = null
    private var isEdit: Boolean = false

    private var accountsList: ArrayList<BankTransfers>? = null
    private lateinit var paymentAccountType: PaymentAccountType

    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private var ibanCertificateUri: Uri? = null
    private lateinit var bottomSheetPaymentMethodBinding: BottomSheetPaymentMethodBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the binding object
        _binding = FragmentPaymentMethodBinding.bind(view)

        setUpModel()

        // Use binding to access views
        binding.toolbarMain.toolbarTitle.text = getString(R.string.payment_method)
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // setup recycler-view
        setupRecyclerView()

        binding.addNewCard.setOnClickListener { showBottomSheetDialog() }

        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    ibanCertificateUri = it
                    val fileName = getFileNameFromUri(requireContext(), it)
                    bottomSheetPaymentMethodBinding.textFileName.text = fileName
                }
            }
    }

    private fun setUpModel() {
        paymentMethodViewModel = ViewModelProvider(this).get(PaymentMethodViewModel::class.java)
        paymentMethodViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }

        paymentMethodViewModel.isNetworkFail.observe(viewLifecycleOwner) { isFail ->
            val message = if (isFail) {
                getString(R.string.connectionError)
            } else {
                getString(R.string.serverError)
            }
            HelpFunctions.ShowLongToast(message, requireContext())
        }

        paymentMethodViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireContext())
            } else {
                if (!it.message.isNullOrBlank()) {
                    HelpFunctions.ShowLongToast(it.message!!, requireContext())
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.checkYourDataForamt),
                        requireContext()
                    )
                }
            }
        }

        paymentMethodViewModel.isLoadingBackAccountList.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBarBankAccount.show()
            } else {
                binding.progressBarBankAccount.hide()
            }
        }

        paymentMethodViewModel.listBackAccountObserver.observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                if (it.accountsList != null) {
                    if (AddProductObjectData.paymentOptionList != null) {
                        AddProductObjectData.paymentOptionList?.let { paymentOptionList ->
                            if (paymentOptionList.contains(AddProductObjectData.PAYMENT_OPTION_BANk) && AddProductObjectData.selectedAccountDetails != null) {
                                // using adapter here
                                val paymentMethods = updateList(accountBankListResp = it)
                                adapter.updateList(paymentMethods)

                            } else {
                                // using adapter here
                                val paymentMethods = updateList(accountBankListResp = it)
                                adapter.updateList(paymentMethods)
                            }
                        }
                    } else {
                        // using adapter here
                        val paymentMethods = updateList(accountBankListResp = it)
                        adapter.updateList(paymentMethods)
                    }
                }
            }
        }
        paymentMethodViewModel.addBackAccountObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }
                paymentMethodViewModel.getBankAccountsList()
            }
        }

        paymentMethodViewModel.editBankAccountObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }
                paymentMethodViewModel.getBankAccountsList()
            }
        }

        paymentMethodViewModel.deleteBankAccountObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if (bottomSheetDialog != null) {
                    bottomSheetDialog!!.dismiss()
                }
                paymentMethodViewModel.getBankAccountsList()
            }
        }

        paymentMethodViewModel.getBankAccountsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paymentMethodViewModel.closeAllCall()

        // Clear the binding when the view is destroyed
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = PaymentMethodAdapter(
            onDeleteClick = { id ->
                // Handle delete click
                ShowDeleteAlert(
                    context = requireContext(),
                    icon = R.drawable.info,
                    subtitle = getString(R.string.are_you_sure_to_delete_the_payment_option),
                    onConfirm = {
                        paymentMethodViewModel.deleteBankTransfer(id = id)
                    }
                )
            },
            onEditClick = { selectedItem ->
                val accountToEdit = accountsList?.find { it.id == selectedItem.id }

                if (accountToEdit != null) {
                    lifecycleScope.launch {
                        val downloadedFile = downloadFileFromUrl(
                            url = accountToEdit.ibanCertificate ?: "",
                            context = requireContext(),
                            fileName = "iban_certificate_${accountToEdit.id}.pdf"
                        )

                        val toEditBankTransfer = EditBankTransfer(
                            id = accountToEdit.id,
                            accountNumber = accountToEdit.accountNumber ?: "",
                            bankName = accountToEdit.bankName ?: "",
                            bankHolderName = accountToEdit.bankHolderName ?: "",
                            ibanNumber = accountToEdit.ibanNumber ?: "",
                            swiftCode = accountToEdit.swiftCode ?: "",
                            ibanCertificate = accountToEdit.ibanCertificate ?: "",
                            ibanCertificateFile = downloadedFile, // This is now a File
                            expiryDate = accountToEdit.expiaryDate ?: "",
                            saveForLaterUse = accountToEdit.saveForLaterUse == true,
                            paymentAccountType = PaymentAccountType.fromValue(
                                paymentType = accountToEdit.paymentAccountType ?: ""
                            )?.paymentType.toString(),
                        )

                        showBottomSheetDialog(toEditBankTransfer, isEditMode = true)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Account with ID ${selectedItem.id} not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        )
        binding.recyclerPaymentMethod.adapter = adapter
        binding.recyclerPaymentMethod.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showBottomSheetDialog(
        account: EditBankTransfer? = null,
        isEditMode: Boolean = false
    ) {
        val binding = BottomSheetPaymentMethodBinding.inflate(layoutInflater)
        bottomSheetPaymentMethodBinding = binding
        bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val layoutCardDetails = binding.root.findViewById<LinearLayout>(R.id.layout_card_details)
        val layoutBankTransfer = binding.root.findViewById<LinearLayout>(R.id.layout_bank_transfer)

        // Set radio buttons
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedText =
                binding.root.findViewById<RadioButton>(checkedId)?.text?.toString() ?: ""

            when (selectedText) {
                getString(R.string.visa) -> {
                    layoutCardDetails.visibility = View.VISIBLE
                    layoutBankTransfer.visibility = View.GONE
                    paymentAccountType = PaymentAccountType.VisaMasterCard
                }

                getString(R.string.MadaPayment) -> {
                    layoutCardDetails.visibility = View.VISIBLE
                    layoutBankTransfer.visibility = View.GONE
                    paymentAccountType = PaymentAccountType.Mada
                }

                getString(R.string.bank_transfer) -> {
                    layoutCardDetails.visibility = View.GONE
                    layoutBankTransfer.visibility = View.VISIBLE
                    paymentAccountType = PaymentAccountType.BankAccount
                }
            }
        }

        // Set title and pre-fill fields if in edit mode
        if (isEditMode && account != null) {
            // change title
            when (PaymentAccountType.fromValue(paymentType = account.paymentAccountType)) {
                PaymentAccountType.VisaMasterCard -> {
                    binding.textTitle.text = getString(R.string.edit_visa_master_card)
                    binding.layoutCardDetails.visibility = View.VISIBLE

                    // fill fields
                    binding.editTextCardHolderName.setText(account.bankHolderName)
                    binding.editTextCardNumber.setText(account.accountNumber)
                    binding.editTextExpiryDate.setText(account.expiryDate)
                }

                PaymentAccountType.Mada -> {
                    binding.textTitle.text = getString(R.string.edit_mada_card)
                    binding.layoutCardDetails.visibility = View.VISIBLE

                    // fill fields
                    binding.editTextCardHolderName.setText(account.bankHolderName)
                    binding.editTextCardNumber.setText(account.accountNumber)
                    binding.editTextExpiryDate.setText(account.expiryDate)
                }

                PaymentAccountType.BankAccount -> {
                    binding.textTitle.text = getString(R.string.edit_bank_account)
                    binding.layoutBankTransfer.visibility = View.VISIBLE
                    binding.switchSaveLater.visibility = View.VISIBLE

                    // fill fields
                    binding.editTextBankName.setText(account.bankName)
                    binding.editTextBankAccountNumber.setText(account.accountNumber)
                    binding.editTextBankHolderSName.setText(account.bankHolderName)
                    binding.editTextIban.setText(account.ibanNumber)
                    binding.textFileName.text =
                        getFileNameFromUri(requireContext(), account.ibanCertificate.toUri())
                    binding.editTextSwiftCode.setText(account.swiftCode)
                }
            }

            // hide radio group
            binding.radioGroup.visibility = View.GONE
            // show switch
            binding.switchSaveLater.isChecked = account.saveForLaterUse
        } else {
            binding.textTitle.text = getString(R.string.edit_visa_master_card)
            binding.layoutCardDetails.visibility = View.VISIBLE
        }

        binding.editTextExpiryDate.addTextChangedListener(object : TextWatcher {
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
                binding.editTextExpiryDate.removeTextChangedListener(this)
                binding.editTextExpiryDate.setText(formatted)
                binding.editTextExpiryDate.setSelection(formatted.length)
                binding.editTextExpiryDate.addTextChangedListener(this)
            }
        })

        binding.buttonUpload.setOnClickListener {
            // Handle upload button click
            filePickerLauncher.launch(arrayOf("application/pdf", "image/*"))
        }

        binding.addAccountBtn.text =
            if (isEditMode) getString(R.string.Save) else getString(R.string.Add)

        binding.addAccountBtn.setOnClickListener {
            checkDataToAddBackAccount(
                isEditMode = isEditMode,
                bottomSheetDialog = bottomSheetDialog!!,
                binding = binding,
                paymentAccountType = paymentAccountType,
                editBankTransfer = account
            )
        }

        bottomSheetDialog!!.show()
    }

    private fun checkDataToAddBackAccount(
        isEditMode: Boolean = false,
        editBankTransfer: EditBankTransfer? = null,
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

        val accountNumber = if (cardNumber.isNotEmpty()) cardNumber else bankAccountNumber
        val bankHolderName = if (cardHolderName.isNotEmpty()) cardHolderName else bankHoldersName

        when (paymentAccountType) {
            PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> {
                if (cardHolderName.isEmpty()) {
                    readyToAdd = false
                    binding.editTextCardHolderName.error =
                        "${getString(R.string.enter)} ${getString(R.string.card_holder_s_name)}"
                }

                if (cardNumber.isEmpty()) {
                    readyToAdd = false
                    binding.editTextCardNumber.error =
                        "${getString(R.string.enter)} ${getString(R.string.card_number)}"
                } else if (cardNumber.filter { it.isDigit() }.length < 16) {
                    readyToAdd = false
                    binding.editTextCardNumber.error =
                        getString(R.string.card_number_must_be_16_digits)
                }

                if (expiryDate.isEmpty()) {
                    readyToAdd = false
                    binding.editTextExpiryDate.error =
                        "${getString(R.string.enter)} ${getString(R.string.ExpiryDate)}"
                } else {
                    val regex = Regex("""^(0[1-9]|1[0-2])/(\d{4})$""")
                    if (!regex.matches(expiryDate)) {
                        readyToAdd = false
                        binding.editTextExpiryDate.error =
                            getString(R.string.invalid_date_format)
                    } else {
                        val (monthStr, yearStr) = expiryDate.split("/")
                        val enteredMonth = monthStr.toInt()
                        val enteredYear = yearStr.toInt()
                        val current = Calendar.getInstance()
                        val currentYear = current.get(Calendar.YEAR)
                        val currentMonth = current.get(Calendar.MONTH) + 1

                        if (enteredYear < currentYear || (enteredYear == currentYear && enteredMonth < currentMonth)) {
                            readyToAdd = false
                            binding.editTextExpiryDate.error =
                                getString(R.string.expiry_date_passed)
                        }
                    }
                }
            }

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
            if (isEditMode && editBankTransfer != null) {
                // You should have an update function in your ViewModel like:
                paymentMethodViewModel.editBankTransferData(
                    id = editBankTransfer.id.toString(),
                    accountNumber = accountNumber,
                    bankName = bankName,
                    bankHolderName = bankHolderName,
                    ibanNumber = iban,
                    swiftCode = swiftCode,
                    expiryDate = expiryDate,
                    ibanCertificate = editBankTransfer.ibanCertificate,
                    ibanCertificateFile = editBankTransfer.ibanCertificateFile,
                    saveForLaterUse = binding.switchSaveLater.isChecked,
                    paymentAccountType = paymentAccountType.value.toString()
                )
            } else {
                if (paymentAccountType == PaymentAccountType.BankAccount) {
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
                            paymentAccountType = paymentAccountType.value.toString()
                        )
                    }
                } else {
                    paymentMethodViewModel.addBankAccountData(
                        accountNumber = accountNumber,
                        bankName = bankName,
                        bankHolderName = bankHolderName,
                        ibanNumber = iban,
                        swiftCode = swiftCode,
                        expiryDate = expiryDate,
                        saveForLaterUse = binding.switchSaveLater.isChecked,
                        paymentAccountType = paymentAccountType.value.toString()
                    )
                }
            }
            bottomSheetDialog.dismiss()
        }
    }

    private fun updateList(accountBankListResp: BankTransfersListResponse): List<PaymentMethod>? {
        accountsList = accountBankListResp.accountsList
        return accountBankListResp.accountsList?.map { account ->
            PaymentMethod(
                id = account.id,
                cardTypeImageRes = R.drawable.visa_ic,
                cardNumber = "•••• •••• •••• ${account.accountNumber?.takeLast(4)}",
                userName = account.bankHolderName.orEmpty(),
                expiryDate = account.expiaryDate.orEmpty(),
                bankName = account.bankName,
                holderName = account.bankHolderName,
                iban = account.ibanNumber,
                paymentAccountType = PaymentAccountType.fromValue(
                    paymentType = account.paymentAccountType ?: ""
                )
            )
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

    private suspend fun downloadFileFromUrl(
        url: String,
        context: Context,
        fileName: String
    ): File? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection()
                connection.connect()

                val input = connection.getInputStream()
                val file = File(context.cacheDir, fileName)

                val output = FileOutputStream(file)
                input.copyTo(output)
                output.close()
                input.close()

                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
