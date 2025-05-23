package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Filter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityAddressPaymentBinding
import com.malqaa.androidappp.databinding.AddCardBinding
import com.malqaa.androidappp.databinding.AllCardsLayoutBinding
import com.malqaa.androidappp.databinding.ItemCardBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.enums.PaymentMethod
import com.malqaa.androidappp.newPhase.domain.enums.ShippingType
import com.malqaa.androidappp.newPhase.domain.models.ErrorAddOrder
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails
import com.malqaa.androidappp.newPhase.domain.models.addOrderResp.ProductOrderPaymentDetailsDto
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartDataObject
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.PaymentOptions
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.AddressViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addAddressActivity.AddAddressActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addressListActivity.AddressesAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.adapter.CartNewAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity3.SuccessOrderActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.viewModel.CartViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter.CurrentOrderAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.ShowAlert
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.ShowDoneAlert
import com.malqaa.androidappp.newPhase.utils.formatAsCardNumber
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@SuppressLint("NotifyDataSetChanged")
class AddressPaymentActivity : BaseActivity<ActivityAddressPaymentBinding>(),
    AddressesAdapter.SetOnSelectedAddress, CartNewAdapter.SetProductNewCartListeners,
    SwipeRefreshLayout.OnRefreshListener,
    CurrentOrderAdapter.SetOnClickListeners {

    private lateinit var addressViewModel: AddressViewModel

    private var deliveryOptionSelect: String = "0"
    private var paymentOptionSelect: Int = 0
    private var paymentDetailsDtoList: ArrayList<Triple<Int, Int, Int>>? = null
    private var addressesAdapter: AddressesAdapter? = null
    private var cartNewAdapter: CartNewAdapter? = null
    private var purchaseOrderAdapter: CurrentOrderAdapter? = null
    var requestType = ""
    private var cartViewModel: CartViewModel? = null
    private var userAddressesList: ArrayList<AddressItem>? = null
    private var productsCartList: ArrayList<CartProductDetails>? = null
    private var cartDataObject: CartDataObject? = null
    private var lastUpdateMainPosition: Int = 0
    private var lastUpdateProductPosition: Int = 0
    lateinit var orderFullInfoDtoList: ArrayList<OrderFullInfoDto>
    private var comeFromCart: Boolean = false
    private var comeFromMyOrders: Boolean = false
    private var comeFromProductDetails: Boolean = false
    private var addressId: Int = 0
    var flagTypeSale = true
    var fromNegotiation = false
    var orderId = 0
    private var lastSelectedDeliveryOption: String? = null
    private var currentPaymentOptions: List<PaymentOptions>? = null


    private var lastSelectedPosition = 0

    private val addAddressLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                userAddressesList?.clear()
                addressesAdapter?.notifyDataSetChanged()
                cartViewModel?.getUserAddress()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityAddressPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _binding = binding

        flagTypeSale = intent.getBooleanExtra("flagTypeSale", true)
        fromNegotiation = intent.getBooleanExtra("fromNegotiation", false)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)
        comeFromCart = intent.getBooleanExtra("comeFromCart", false)
        comeFromProductDetails = intent.getBooleanExtra("comeFromProductDetails", false)
        comeFromMyOrders = intent.getBooleanExtra("comeFromMyOrders", false)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.CheckoutProduct)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setupAdapter()
        setAddressesAdapter()
        setupCartViewModel()
        setupAddressViewModel()
        setViewClickListeners()

        if (flagTypeSale) {
            binding.titleCoupon.visibility = View.VISIBLE
            binding.layActiveCoupon.visibility = View.VISIBLE
        } else {
            binding.titleCoupon.visibility = View.GONE
            binding.layActiveCoupon.visibility = View.GONE
        }

        // =========================================================================================
        setUpViewModel()
        // =========================================================================================

    }

    private fun setupAddressViewModel() {
        addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)

        addressViewModel.isLoadingDeleteAddress.observe(this) {
            if (it) {
                HelpFunctions.startProgressBar(this)
            } else {
                HelpFunctions.dismissProgressBar()
            }
        }

        addressViewModel.deleteUserAddressesObserver.observe(this) { resp ->
            if (resp.status_code == 200) {
                if (lastSelectedPosition < userAddressesList?.size!!) {
                    userAddressesList?.removeAt(lastSelectedPosition)
                    addressesAdapter?.notifyDataSetChanged()
                    if (userAddressesList?.isEmpty() == true) {
                        binding.tvError.show()
                    }
                }
            } else {
                if (resp.message != null) {
                    HelpFunctions.ShowLongToast(resp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }

        observerUserMessage()
    }

    override fun onResume() {
        super.onResume()
        if (!fromNegotiation) {
            setCartNewAdapter()
            getCartList()
        } else {
            setOrderDetailsAdapter()
            cartViewModel?.getCurrentOrderDetailsByMasterID(orderId)

        }

        cartViewModel?.getUserAddress()
    }

    private fun getCartList() {
        cartViewModel?.getCartList(SharedPreferencesStaticClass.getMasterCartId())
    }

    private fun setViewClickListeners() {
        paymentDetailsDtoList = arrayListOf()
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.addNewAdd.setOnClickListener {
            addAddressLaucher.launch(Intent(this, AddAddressActivity::class.java))
        }

        binding.btnConfirmDetails.setOnClickListener {
            // Step 1: Check visibility of payment method layouts
            val visibleChecks = listOf(
                binding.layoutVisaCreditCard to binding.switchVisaCreditCard,
                binding.layoutMadaPayment to binding.switchMadaPayment,
                binding.layoutCashPayment to binding.switchCashPayment,
                binding.layoutBankTransferPayment to binding.switchBankTransferPayment,
                binding.layoutMyWalletPayment to binding.switchMyWalletPayment
            )

            // Step 2: From only visible layouts, check if any corresponding switch is ON
            val isAtLeastOneChecked = visibleChecks.any { (layout, switch) ->
                layout.isVisible && switch.isChecked
            }

            val mergedList = mergeTuplesInList(paymentDetailsDtoList ?: arrayListOf())
            val customList = mergedList.map { triple ->
                ProductOrderPaymentDetailsDto(triple.first, triple.second, triple.third)
            }.toCollection(ArrayList())
            if (flagTypeSale) {
                if (addressId == 0) {
                    HelpFunctions.ShowLongToast(getString(R.string.selectDeliveryAddress), this)
                } else if (deliveryOptionSelect == "0") {
                    HelpFunctions.ShowLongToast(getString(R.string.please_delivery_options), this)
                } else if (productsCartList!!.isEmpty()) {
                    HelpFunctions.ShowLongToast(getString(R.string.empty_cart), this)
                } else if (!isAtLeastOneChecked) { // Step 3: Validate
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_activate_at_least_one_payment_options),
                        this
                    )
                    return@setOnClickListener
                } else {
                    cartViewModel?.addOrder(
                        masterCartId = SharedPreferencesStaticClass.getMasterCartId(),
                        addressId = addressId,
                        productOrderPaymentList = customList,
                        accountDetails = accountDetails,
                        totalAmount = totalAmount,
                        walletAmount = 0,
                        selectedPaymentMethod = selectedPaymentMethod
                    )
                }
            } else {
                if (addressId == 0) {
                    HelpFunctions.ShowLongToast(getString(R.string.selectDeliveryAddress), this)
                } else {
                    cartViewModel?.addPaymentTransaction(
                        totalPriceForCartFinal = cartDataObject!!.totalPriceForCartFinal,
                        totalPriceForCartBeforeDiscount = cartDataObject!!.totalPriceForCartBeforeDiscount,
                        masterCartId = SharedPreferencesStaticClass.getMasterCartId(),
                        addressId = addressId,
                        productOrderPaymentList = customList,
                        accountDetails = accountDetails,
                        totalAmount = totalAmount,
                        walletAmount = 0.0,
                        selectedPaymentMethod = selectedPaymentMethod
                    )
                }
            }
        }

        binding.btnApplyCode.setOnClickListener {
            if (binding.edtCoupon.text.toString().trim() == "") {
                binding.edtCoupon.error = getString(R.string.enter_the_coupon)
            } else {
                cartViewModel?.applyCouponOnCart(
                    SharedPreferencesStaticClass.getMasterCartId(),
                    binding.edtCoupon.text.toString().trim()
                )
            }
        }

        // =========================================================================================
        // VisaCredit
        binding.switchVisaCreditCard.setOnCheckedChangeListener { _, b ->
            if (b) {
                //allCardsBottomSheetDialog()
                addProductViewModel.getBankAccountsList(paymentAccountType = PaymentMethod.CreditCard.paymentCardType)
                selectedPaymentMethod = PaymentMethod.CreditCard

                // hide other
                binding.switchMadaPayment.isChecked = false
                binding.switchMyWalletPayment.isChecked = false
                binding.switchCashPayment.isChecked = false
                binding.switchBankTransferPayment.isChecked = false
            } else {
                binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                    View.GONE
            }
        }

        // Mada
        binding.switchMadaPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))

                addProductViewModel.getBankAccountsList(paymentAccountType = PaymentMethod.Mada.paymentCardType)
                selectedPaymentMethod = PaymentMethod.Mada

                // hide other
                binding.switchVisaCreditCard.isChecked = false
                binding.switchMyWalletPayment.isChecked = false
                binding.switchCashPayment.isChecked = false
                binding.switchBankTransferPayment.isChecked = false
            } else {
                binding.layoutMadaPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvMadaPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility = View.GONE
            }
        }

        // Cash
        binding.switchCashPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.layoutCashPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))
                selectedPaymentMethod = PaymentMethod.Cash

                // show dialog for cash payment
                val cashPaymentFees = 10
                ShowDoneAlert(
                    context = this,
                    alertTitle = "",
                    icon = R.drawable.info,
                    alertMessage = getString(
                        R.string.a_fee_of_riyals_will_be_added_for_the_cash_payment_service,
                        cashPaymentFees.toString()
                    ),
                    button = getString(R.string.done)
                )
                // show cash payment fees
                binding.linearLayoutCashPaymentFees.visibility = View.VISIBLE
                binding.textViewCashPaymentFees.text =
                    "$cashPaymentFees ${getString(R.string.rayal)}"

                // set cart total price
                setCartTotalPrice(isCashPaymentFees = true)

                // hide other
                binding.switchVisaCreditCard.isChecked = false
                binding.switchMadaPayment.isChecked = false
                binding.switchMyWalletPayment.isChecked = false
                binding.switchBankTransferPayment.isChecked = false
            } else {
                binding.layoutCashPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvCashPayment.setTextColor(ContextCompat.getColor(this, R.color.text_color))

                // hide cash payment fees
                binding.linearLayoutCashPaymentFees.visibility = View.GONE
                binding.textViewCashPaymentFees.text = "0 ${getString(R.string.rayal)}"

                // set cart total price
                setCartTotalPrice(isCashPaymentFees = false)
            }
        }

        // Bank Transfer
        binding.switchBankTransferPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                binding.layoutBankTransferPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
                binding.tvBankTransferPayment.setTextColor(ContextCompat.getColor(this, R.color.bg))
                selectedPaymentMethod = PaymentMethod.BankTransfer

                // show cash payment fees
                val cashPaymentFees = 10
                binding.linearLayoutCashPaymentFees.visibility = View.VISIBLE
                binding.textViewCashPaymentFees.text =
                    "$cashPaymentFees ${getString(R.string.rayal)}"

                // hide other
                binding.switchVisaCreditCard.isChecked = false
                binding.switchMadaPayment.isChecked = false
                binding.switchMyWalletPayment.isChecked = false
                binding.switchCashPayment.isChecked = false
            } else {
                binding.layoutBankTransferPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvBankTransferPayment.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_color
                    )
                )

                // hide BankTransfer payment fees
                binding.linearLayoutCashPaymentFees.visibility = View.GONE
                binding.textViewCashPaymentFees.text = "0 ${getString(R.string.rayal)}"
            }
        }

        // My Wallet
        binding.switchMyWalletPayment.setOnCheckedChangeListener { _, b ->
            if (b) {
                addProductViewModel.getWalletBalance()
                selectedPaymentMethod = PaymentMethod.Wallet

                // hide other
                binding.switchVisaCreditCard.isChecked = false
                binding.switchMadaPayment.isChecked = false
                binding.switchCashPayment.isChecked = false
                binding.switchBankTransferPayment.isChecked = false
            } else {
                binding.layoutMyWalletPayment.background =
                    ContextCompat.getDrawable(this, R.drawable.edittext_bg)
                binding.tvMyWalletPayment.setTextColor(
                    ContextCompat.getColor(this, R.color.text_color)
                )
                binding.selectedMyWallet.linearLayoutSelectedPaymentOptions.visibility = View.GONE
            }
        }

        binding.selectedVisaCreditCard.btnChooseAnotherCard.setOnClickListener {
            allCardsBottomSheetDialog()
        }

        binding.selectedMada.btnChooseAnotherCard.setOnClickListener {
            allCardsBottomSheetDialog()
        }
        // =========================================================================================
    }

    @SuppressLint("SetTextI18n")
    private fun setupCartViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel!!.isLoadingAddresses.observe(this) {
            if (it)
                binding.progressBarAddresses.show()
            else
                binding.progressBarAddresses.hide()
        }
        cartViewModel!!.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        cartViewModel!!.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        cartViewModel!!.errorResponseObserver.observe(this) { response ->
            if (response.status != null && response.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                val errorList = response.data as? List<ErrorAddOrder> // Safe cast
                if (errorList.isNullOrEmpty()) { // Handle null or empty list
                    if (response.message != null) {
                        HelpFunctions.ShowLongToast(response.message!!, this)
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                    }
                } else {
                    for (cartItem in productsCartList!!) {
                        for (product in cartItem.listProduct!!) {
                            val error = errorList.find { it.produtId == product.id }
                            if (error != null) {
                                val remainingProduct =
                                    (product.qty ?: 0) - (product.cartProductQuantity ?: 0)
                                val message = getString(
                                    R.string.sold_out_quantity,
                                    product.cartProductQuantity,
                                    error.quantity,
                                    error.productName
                                )
                                product.msgError = if (error.quantity == 0) {
                                    getString(R.string.notFoundQuantity)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                    cartNewAdapter?.updateAdapter(productsCartList!!)
                }
            }
        }

        cartViewModel!!.userAddressesListObserver.observe(this) { userAddressResp ->
            if (userAddressResp.status_code == 200) {
                if (userAddressResp.addressesList != null && userAddressResp.addressesList?.isNotEmpty() == true) {
                    userAddressResp.addressesList!!.forEach { addressItem ->
                        if (addressItem.defaultAddress) {
                            addressId = addressItem.id
                        }
                    }
                    binding.tvAddressError.hide()
                    userAddressesList?.clear()
                    userAddressesList?.addAll(userAddressResp.addressesList!!)
                    addressesAdapter?.notifyDataSetChanged()
                } else {
                    binding.tvAddressError.show()
                }
            }
        }
        cartViewModel!!.increaseCartProductQuantityObserver.observe(this) { increaseProductResp ->
            if (increaseProductResp.status_code == 200) {
                productsCartList?.get(lastUpdateMainPosition)?.listProduct?.get(
                    lastUpdateProductPosition
                )?.let {
                    it.cartProductQuantity = (it.cartProductQuantity ?: 0) + 1
                    cartDataObject?.let { cartDataObject ->
                        cartDataObject.totalPriceForCartFinal += it.priceDiscount
                        cartDataObject.totalPriceForCartBeforeDiscount += it.priceDiscount
                    }
                    productsCartList!![lastUpdateMainPosition].couponAppliedBussinessAccountDto?.let { couponAppliedBussinessAccountDto ->
                        couponAppliedBussinessAccountDto.businessAccountAmountBeforeCoupon += it.priceDiscount
                        couponAppliedBussinessAccountDto.businessAccountAmountAfterCoupon += it.priceDiscount
                        couponAppliedBussinessAccountDto.finalTotalPriceForBusinessAccount += it.priceDiscount
                    }
                }


                cartNewAdapter?.notifyItemChanged(lastUpdateMainPosition)
                setCartTotalPrice()
            }
        }
        cartViewModel!!.decreaseCartProductQuantityObserver.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                productsCartList?.get(lastUpdateMainPosition)?.listProduct?.get(
                    lastUpdateProductPosition
                )
                    ?.let {
                        it.cartProductQuantity = (it.cartProductQuantity ?: 0) - 1
                        cartDataObject?.let { cartDataObject ->
                            cartDataObject.totalPriceForCartFinal -= it.priceDiscount
                            cartDataObject.totalPriceForCartBeforeDiscount -= it.priceDiscount
                        }
                        productsCartList!![lastUpdateMainPosition].couponAppliedBussinessAccountDto?.let { couponAppliedBussinessAccountDto ->
                            couponAppliedBussinessAccountDto.businessAccountAmountBeforeCoupon -= it.priceDiscount
                            couponAppliedBussinessAccountDto.businessAccountAmountAfterCoupon -= it.priceDiscount
                            couponAppliedBussinessAccountDto.finalTotalPriceForBusinessAccount -= it.priceDiscount
                        }
                    }
                cartNewAdapter?.notifyItemChanged(lastUpdateMainPosition)
                setCartTotalPrice()
            }
        }
        cartViewModel?.removeProductFromCartProductsObserver?.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                val price = productsCartList?.get(lastUpdateMainPosition)?.listProduct?.get(
                    lastUpdateProductPosition
                )?.priceDiscount ?: 0f
                cartDataObject?.let { cartDataObject ->
                    cartDataObject.totalPriceForCartFinal -= price
                    cartDataObject.totalPriceForCartBeforeDiscount -= price
                }
                productsCartList?.get(lastUpdateMainPosition)?.couponAppliedBussinessAccountDto?.let { couponAppliedBussinessAccountDto ->
                    couponAppliedBussinessAccountDto.businessAccountAmountBeforeCoupon -= price
                    couponAppliedBussinessAccountDto.businessAccountAmountAfterCoupon -= price
                    couponAppliedBussinessAccountDto.finalTotalPriceForBusinessAccount -= price
                }
                productsCartList?.get(lastUpdateMainPosition)?.listProduct?.removeAt(
                    lastUpdateProductPosition
                )

                cartNewAdapter?.notifyDataSetChanged()
                setCartTotalPrice()
            }
        }

        cartViewModel!!.currentOrderByMusterIdRespObserver.observe(this) { resp ->
            if (resp.status_code == 200) {

                if (resp.orderDetailsByMasterIDData != null) {
                    requestType = resp.orderDetailsByMasterIDData.requestType
                        ?: getString(R.string.current_price_buy)

                    resp.orderDetailsByMasterIDData.orderFullInfoDtoList?.let {
                        orderFullInfoDtoList.clear()
                        orderFullInfoDtoList.addAll(it)
                        purchaseOrderAdapter?.notifyDataSetChanged()
                    }
                    if (cartDataObject == null) {
                        cartDataObject = CartDataObject()
                    }
                    cartDataObject!!.totalPriceForCartFinal =
                        (resp.orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount
                            ?: resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount)!!
                    cartDataObject!!.totalPriceForCartBeforeDiscount =
                        resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount!!
                    Log.i(
                        "TAG",
                        "setCartTotalPrice 01: ${resp.orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount ?: resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount} ${
                            getString(
                                R.string.rayal
                            )
                        }"
                    )
                    binding.totalTv.text =
                        "${resp.orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount ?: resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount} ${
                            getString(
                                R.string.rayal
                            )
                        }"
                    binding.subtotalTv.text =
                        "${resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount} ${
                            getString(
                                R.string.rayal
                            )
                        }"


                }
            } else {
                if (resp.message != null) {
                    HelpFunctions.ShowLongToast(resp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }

        cartViewModel!!.cartListRespObserver.observe(this) { cartListResp ->
            if (cartListResp.status_code == 200) {
                if (cartListResp.cartDataObject?.listCartProducts != null) {
                    cartDataObject = cartListResp.cartDataObject
                    productsCartList?.clear()
                    productsCartList?.addAll(cartListResp.cartDataObject.listCartProducts)
                    cartNewAdapter?.notifyDataSetChanged()
                    setCartTotalPrice()
                    val businessAccountId =
                        cartListResp.cartDataObject.listCartProducts[0].businessAccountId
                    val paymentOptions =
                        cartListResp.cartDataObject.listCartProducts[0].listProduct?.get(0)?.paymentOptions
                    currentPaymentOptions = paymentOptions
                    verifyPaymentOptionsDisplay(
                        comeFromCart = comeFromCart,
                        comeFromProductDetails = comeFromProductDetails,
                        comeFromMyOrders = comeFromMyOrders,
                        businessAccountId = businessAccountId,
                        paymentOptions = paymentOptions
                    )
                    lastSelectedDeliveryOption?.let {
                        applyCashVisibilityBasedOnDeliveryOption(it)
                    }
                }
            } else {
                if (cartListResp.message != null) {
                    HelpFunctions.ShowLongToast(cartListResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)

                }
            }
        }
        cartViewModel!!.applyCouponOnCartObserver.observe(this) { applyCouponResp ->
            if (applyCouponResp.status_code == 200) {
                productsCartList?.clear()
                cartNewAdapter?.notifyDataSetChanged()
                binding.totalTv.text = "0 ${getString(R.string.rayal)}"
                binding.subtotalTv.text = "0 ${getString(R.string.rayal)}"
                binding.discountTv.text = "0 ${getString(R.string.rayal)}"
                if (!fromNegotiation)
                    getCartList()
            } else {
                if (applyCouponResp.message != null) {
                    HelpFunctions.ShowLongToast(applyCouponResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)

                }
            }
        }
        cartViewModel!!.addOrderObserver.observe(this) { addOrderResp ->
            if (addOrderResp.status_code == 200) {
                addOrderResp.addOrderObject?.let {
                    SharedPreferencesStaticClass.clearCardMasterId()
                    SharedPreferencesStaticClass.clearCartCount()
                    SharedPreferencesStaticClass.clearAddressTitle()
                    val intent = Intent(this, SuccessOrderActivity::class.java).apply {
                        putExtra(ConstantObjects.orderNumberKey, it.orderNumber.toString())
                        putExtra(
                            ConstantObjects.orderShippingSectionNumberKey,
                            productsCartList?.size.toString()
                        )
                        putExtra(
                            ConstantObjects.orderPriceKey,
                            cartDataObject?.totalPriceForCartFinal.toString()
                        )
                        putExtra("RequestType", it.requestType)
                        putExtra("payment_method_key", selectedPaymentMethod.value)
                    }
                    startActivity(intent)
                    finish()
                }

            } else {
                if (addOrderResp.message != null) {
                    HelpFunctions.ShowLongToast(addOrderResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)

                }
            }

        }

        cartViewModel!!.paymentTransaction.observe(this) {

            val intent = Intent(this, SuccessOrderActivity::class.java).apply {
                putExtra(ConstantObjects.orderNumberKey, orderId.toString())

                if (!fromNegotiation) {
                    putExtra(
                        ConstantObjects.orderShippingSectionNumberKey,
                        productsCartList?.size.toString()
                    )
                } else {
                    putExtra(
                        ConstantObjects.orderShippingSectionNumberKey,
                        orderFullInfoDtoList.size.toString()
                    )
                }

                putExtra(
                    ConstantObjects.orderPriceKey,
                    cartDataObject?.totalPriceForCartFinal.toString()
                )
                putExtra("RequestType", requestType)

            }
            startActivity(intent)
            finish()
        }

        cartViewModel!!.deleteShipment.observe(this) {
            SharedPreferencesStaticClass.clearCartCount()
            HelpFunctions.ShowLongToast(it.message, this)
        }
    }

    private fun setOrderDetailsAdapter() {
        orderFullInfoDtoList = ArrayList<OrderFullInfoDto>()
        purchaseOrderAdapter = CurrentOrderAdapter(orderFullInfoDtoList, this)
        binding.rvNewCart.apply {
            adapter = purchaseOrderAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


    private fun setCartNewAdapter() {
        productsCartList = ArrayList<CartProductDetails>()
        cartNewAdapter = CartNewAdapter(flagTypeSale, productsCartList ?: arrayListOf(), this)
        binding.rvNewCart.apply {
            adapter = cartNewAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setCartTotalPrice(isCashPaymentFees: Boolean = false) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                cartDataObject?.let { cart ->
                    val finalTotal = cart.totalPriceForCartFinal
                    val beforeDiscount = cart.totalPriceForCartBeforeDiscount
                    val discount = cart.adminCouponcodeDiscount

                    // Calculate fee only if required
                    val cashPaymentFee = if (isCashPaymentFees) finalTotal * 0.10 else 0.0
                    val totalWithCashFee = finalTotal + cashPaymentFee
                    totalAmount = totalWithCashFee.toFloat()

                    // Update UI
                    binding.totalTv.text =
                        "${totalWithCashFee.formatAsPrice()} ${getString(R.string.rayal)}"
                    binding.subtotalTv.text =
                        "${beforeDiscount.formatAsPrice()} ${getString(R.string.rayal)}"
                    binding.discountTv.text =
                        "${discount.formatAsPrice()} ${getString(R.string.rayal)}"
                    binding.textViewCashPaymentFees.text =
                        "${cashPaymentFee.formatAsPrice()} ${getString(R.string.rayal)}"
                    binding.textViewCashPaymentFees.visibility =
                        if (isCashPaymentFees) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun Any.formatAsPrice(): String {
        return when (this) {
            is Int -> String.format("%.2f", this.toDouble())
            is Double -> String.format("%.2f", this)
            is Float -> String.format("%.2f", this)
            else -> this.toString()
        }
    }

    private fun setAddressesAdapter() {
        userAddressesList = ArrayList()
        addressesAdapter = AddressesAdapter(userAddressesList ?: arrayListOf(), this, true)
        binding.rvAddress.apply {
            adapter = addressesAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    override fun onIncreaseQuantityProduct(position: Int, mainPosition: Int) {
        lastUpdateMainPosition = mainPosition
        lastUpdateProductPosition = position
        val productCartId =
            productsCartList?.get(mainPosition)?.listProduct!![position].cartproductId
        cartViewModel?.increaseCartProductQuantity(productCartId.toString())
    }

    override fun onDecreaseQuantityProduct(position: Int, mainPosition: Int) {
        lastUpdateMainPosition = mainPosition
        lastUpdateProductPosition = position
        val productCartId =
            productsCartList?.get(mainPosition)?.listProduct!![position].cartproductId
        cartViewModel!!.decreaseCartProductQuantity(productCartId.toString())
    }

    override fun onDeleteProduct(position: Int, mainPosition: Int) {
        lastUpdateMainPosition = mainPosition
        lastUpdateProductPosition = position
        val productCartId =
            productsCartList?.get(mainPosition)?.listProduct!![position].cartproductId
        cartViewModel!!.removeProductFromCartProducts(productCartId.toString())
    }

    override fun onSelectPayment(productId: Int, paymentSelection: Int) {}
    override fun onSelectDelivery(productId: Int, shippingType: String, deliverySelection: String) {
        lastSelectedDeliveryOption = deliverySelection
        applyCashVisibilityBasedOnDeliveryOption(deliverySelection)
        paymentDetailsDtoList?.add(
            Triple(
                productId,
                paymentOptionSelect,
                shippingType.toInt()
            )
        )
    }
    private fun applyCashVisibilityBasedOnDeliveryOption(deliverySelection: String) {
        val shouldHideCash = deliverySelection == "Free shipping within Saudi Arabia" ||
                deliverySelection == "Integrated shipping company options (e.g. Aramex or DHL)"

        val cashOptionAvailable = currentPaymentOptions?.any { it.paymentOptionId == PaymentMethod.Cash.value } == true

        // نخفيه لو لازم يتخفي
        if (shouldHideCash) {
            binding.layoutCashPayment.hide()
        } else {
            // نعرضه بس لو فعليًا موجود من API
            if (cashOptionAvailable) {
                binding.layoutCashPayment.show()
            } else {
                binding.layoutCashPayment.hide()
            }
        }
    }

    override fun onDeleteShipping(position: Int) {
        if (productsCartList?.get(position)?.businessAccountId == null) {
            cartViewModel?.deleteShipping(
                null,
                productsCartList?.get(position)?.cartMsterId ?: "",
                productsCartList?.get(position)?.providerId!!
            )
        } else {
            cartViewModel!!.deleteShipping(
                productsCartList!![position].businessAccountId,
                productsCartList!![position].cartMsterId ?: "",
                productsCartList!![position].providerId!!
            )
        }

    }

    private fun mergeTuples(
        tuple1: Triple<Int, Int, Int>,
        tuple2: Triple<Int, Int, Int>
    ): Triple<Int, Int, Int> {
        val mergedFirst = tuple1.first // Assuming the first value is always the same
        val mergedSecond = tuple1.second + tuple2.second
        val mergedThird = tuple1.third + tuple2.third
        return Triple(mergedFirst, mergedSecond, mergedThird)
    }

    fun mergeTuplesInList(tupleList: ArrayList<Triple<Int, Int, Int>>): ArrayList<Triple<Int, Int, Int>> {
        val mergedList = ArrayList<Triple<Int, Int, Int>>()

        // Iterate through the list two elements at a time and merge them
        var i = 0
        while (i < tupleList.size) {
            if (i + 1 < tupleList.size) {
                val mergedTuple = mergeTuples(tupleList[i], tupleList[i + 1])
                mergedList.add(mergedTuple)
                i += 2 // Skip to the next pair
            } else {
                // If there's only one tuple left, add it as is
                mergedList.add(tupleList[i])
                i++
            }
        }

        return mergedList
    }

    override fun onApplyBusinessCardCoupon(
        mainPosition: Int,
        businessAccountId: String?,
        coupon: String,
        providerId: String
    ) {
        cartViewModel!!.applyCouponOnCart(
            SharedPreferencesStaticClass.getMasterCartId(),
            coupon,
            providerId,
            businessAccountId!!
        )
    }

    override fun unApplyBusinessCardCoupon(
        mainPosition: Int,
        businessAccountId: String?,
        coupon: String,
        providerId: String
    ) {
        cartViewModel!!.unApplyCouponOnCart(
            SharedPreferencesStaticClass.getMasterCartId(),
            coupon,
            providerId,
            businessAccountId!!
        )
    }

    override fun setOnSelectedAddress(position: Int) {
        MaterialAlertDialogBuilder(this)
            .setMessage(resources.getString(R.string.change_default_address))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which -> }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                // Respond to positive button press
                addressId = userAddressesList?.get(position)?.id ?: 0
                userAddressesList?.get(position)?.let { addressViewModel.setDefaultAddress(it.id) }
            }
            .show()
    }

    override fun setOnSelectedEditAddress(position: Int) {
        addAddressLaucher.launch(Intent(this, AddAddressActivity::class.java).apply {
            putExtra(ConstantObjects.isEditKey, true)
            putExtra(ConstantObjects.addressKey, userAddressesList?.get(position))
        })
    }

    override fun onDeleteAddress(position: Int) {
        lastSelectedPosition = position
        userAddressesList?.get(position)?.let { addressViewModel.deleteUSerAddress(it.id) }
    }

    private fun observerUserMessage() {
        addressViewModel.userMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                onRefresh()
            }
        }
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        productsCartList?.clear()
        cartNewAdapter?.notifyDataSetChanged()
        userAddressesList?.clear()
        addressesAdapter?.notifyDataSetChanged()
        cartViewModel?.getUserAddress()

        if (!fromNegotiation)
            getCartList()
        else
            cartViewModel?.getCurrentOrderDetailsByMasterID(orderId)

    }

    override fun onDestroy() {
        super.onDestroy()
        cartViewModel?.closeAllCall()
        SharedPreferencesStaticClass.clearAddressTitle()

        paymentDetailsDtoList = null
        addressesAdapter = null
        cartNewAdapter = null
        cartViewModel = null
        userAddressesList = null
        productsCartList = null
        cartDataObject = null
    }

    override fun onDownloadInvoiceSelected(position: Int) {
        // TODO: onDownloadInvoiceSelected
    }

    override fun onAddRateToShipmentSelected(position: Int) {}

    override fun onCancelOrder(position: Int) {}

    private lateinit var _binding: ActivityAddressPaymentBinding
    private lateinit var addProductViewModel: AddProductViewModel
    private var bottomSheetDialog: BottomSheetDialog? = null
    private lateinit var selectedPaymentMethod: PaymentMethod
    private var walletBalance: Double? = null
    private var selectedAccountDetails: ArrayList<AccountDetails> = ArrayList()
    private var accountDetails: AccountDetails? = null
    private lateinit var adapterList: GenericListAdapter<AccountDetails>
    private lateinit var allCardsLayoutBinding: AllCardsLayoutBinding
    private lateinit var allCardsBottomSheetDialog: BottomSheetDialog
    private var totalAmount = 0f

    private fun allCardsBottomSheetDialog() {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(this)
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        allCardsLayoutBinding.recyclerViewAllCards.apply {
            layoutManager = LinearLayoutManager(this@AddressPaymentActivity)
            adapter = adapterList
        }
        if (adapterList.itemCount>0){
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
        }else{
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.show()
        }
        // Flag to check if Done was clicked
        var isDoneButtonClicked = false

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener { addCardBottomSheetDialog() }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = this@AddressPaymentActivity
                    )
                    return@setOnClickListener
                } else if (cvv.toString().length !in 3..4) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_enter_cvv),
                        context = this@AddressPaymentActivity
                    )
                    return@setOnClickListener
                }

                isDoneButtonClicked = true // Mark as completed
                allCardsBottomSheetDialog.dismiss()

                when (selectedPaymentMethod) {
                    PaymentMethod.CreditCard -> {
                        _binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedVisaCreditCard.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }

                    PaymentMethod.Mada -> {
                        _binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedMada.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
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

        // 🔥 Set a dismiss listener to run command if nothing was selected
        allCardsBottomSheetDialog.setOnDismissListener {
            if (!isDoneButtonClicked && (accountDetails?.cvv ?: 0) == 0) {
                handleBottomSheetDismissedWithoutAction()
            }
        }

        // Show the dialog
        allCardsBottomSheetDialog.show()
    }

    private fun newAllCardsBottomSheetDialog(list: ArrayList<AccountDetails>?) {
        allCardsLayoutBinding = AllCardsLayoutBinding.inflate(layoutInflater)
        allCardsBottomSheetDialog = BottomSheetDialog(this)
        allCardsBottomSheetDialog.setContentView(allCardsLayoutBinding.root)

        if (list?.size!! >0){
            allCardsLayoutBinding.recyclerViewAllCards.show()
            allCardsLayoutBinding.descText.hide()
            allCardsLayoutBinding.recyclerViewAllCards.apply {
                layoutManager = LinearLayoutManager(this@AddressPaymentActivity)
                adapter = adapterList
            }
        }else{
            allCardsLayoutBinding.recyclerViewAllCards.hide()
            allCardsLayoutBinding.descText.show()
        }


        // Flag to check if Done was clicked
        var isDoneButtonClicked = false

        allCardsLayoutBinding.apply {
            buttonAddNew.setOnClickListener { addCardBottomSheetDialog() }

            buttonDone.setOnClickListener {
                val cvv = accountDetails?.cvv

                if (accountDetails == null) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_choose_a_card),
                        context = this@AddressPaymentActivity
                    )
                    return@setOnClickListener
                } else if (cvv.toString().length !in 3..4) {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.please_enter_cvv),
                        context = this@AddressPaymentActivity
                    )
                    return@setOnClickListener
                }

                isDoneButtonClicked = true // Mark as completed
                allCardsBottomSheetDialog.dismiss()

                when (selectedPaymentMethod) {
                    PaymentMethod.CreditCard -> {
                        _binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedVisaCreditCard.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
                        }
                    }

                    PaymentMethod.Mada -> {
                        _binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedMada.apply {
                            textCardHoldersName.text = accountDetails?.bankHolderName
                            textCardNumber.text = accountDetails?.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails?.expiaryDate
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

        // 🔥 Set a dismiss listener to run command if nothing was selected
        allCardsBottomSheetDialog.setOnDismissListener {
            if (!isDoneButtonClicked && (accountDetails?.cvv ?: 0) == 0) {
                handleBottomSheetDismissedWithoutAction()
            }
        }

        // Show the dialog
        allCardsBottomSheetDialog.show()
    }

    // Your custom logic here
    private fun handleBottomSheetDismissedWithoutAction() {
        // Do whatever you need — show a toast, log, open another dialog, etc.
        val isVisaEnabled = binding.switchVisaCreditCard.isChecked
        val isMadaEnabled = binding.switchMadaPayment.isChecked
        when (selectedPaymentMethod) {
            PaymentMethod.CreditCard -> if (isVisaEnabled) binding.switchVisaCreditCard.isChecked =
                false

            PaymentMethod.Mada -> if (isMadaEnabled) binding.switchMadaPayment.isChecked =
                false

            else -> {}
        }
    }

    private lateinit var addCardBinding: AddCardBinding
    private lateinit var addCardBottomSheetDialog: BottomSheetDialog

    private fun addCardBottomSheetDialog() {
        // Inflate the layout using ViewBinding
        addCardBinding = AddCardBinding.inflate(layoutInflater)

        // Initialize the BottomSheetDialog and set the view using binding.root
        addCardBottomSheetDialog = BottomSheetDialog(this)
        addCardBottomSheetDialog.setContentView(addCardBinding.root)

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

        if (cvv.isEmpty()||cvv.length<3) {
            readyToAdd = false
            binding.cvvTv.error = getString(R.string.please_enter_cvv)
        }

        if (readyToAdd) {
            if (isSaveLater) {
                addProductViewModel.addBackAccountData(
                    bankHolderName = holderName,
                    accountNumber = cardNumber,
                    expiryDate = expiryText,
                    saveForLaterUse = true,
                    paymentAccountType = selectedPaymentMethod.value.toString()
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
                paymentMethod = selectedPaymentMethod
            )

            allCardsLayoutBinding.apply {
                when (selectedPaymentMethod) {
                    PaymentMethod.CreditCard -> {
                        _binding.selectedVisaCreditCard.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedVisaCreditCard.apply {
                            textCardHoldersName.text = accountDetails!!.bankHolderName
                            textCardNumber.text =
                                accountDetails!!.accountNumber.formatAsCardNumber()
                            textExpiryDate.text = accountDetails!!.expiaryDate
                        }
                    }

                    PaymentMethod.Mada -> {
                        _binding.selectedMada.linearLayoutSelectedPaymentOptions.visibility =
                            View.VISIBLE

                        _binding.selectedMada.apply {
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

    @SuppressLint("SetTextI18n")
    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
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
                addProductViewModel.getBankAccountsList(selectedPaymentMethod.value)
            }
        }
        addProductViewModel.walletBalance.observe(this) {
            if (it.status_code == 200) {
                val walletBalance = it.walletBalance?.walletBalance ?: 0.0
                totalAmount = cartDataObject!!.totalPriceForCartFinal

                if (walletBalance >= totalAmount) {
                    Log.d("test #1", "✅ You can pay with wallet!")

                    val productPriceSar =
                        getString(R.string.product_price_sar, walletBalance.toString())
                    val myWalletBalance =
                        getString(R.string.the_wallet_balance_equals, productPriceSar)

                    binding.selectedMyWallet.textMyPointsBalance.text = myWalletBalance
                    this.walletBalance = walletBalance
                    selectedPaymentMethod = PaymentMethod.Wallet

                    binding.layoutMyWalletPayment.background =
                        ContextCompat.getDrawable(
                            this, R.drawable.field_selection_border_enable
                        )
                    binding.tvMyWalletPayment.setTextColor(
                        ContextCompat.getColor(this, R.color.bg)
                    )
                    binding.selectedMyWallet.linearLayoutSelectedPaymentOptions.visibility =
                        View.VISIBLE
                } else {
                    binding.switchMyWalletPayment.isChecked = false

                    // Format the money balance as SAR
                    val productPriceSar =
                        getString(R.string.product_price_sar, walletBalance.toString())

                    // Full message
                    val walletBalanceText =
                        getString(R.string.the_wallet_balance_equals, productPriceSar)
                    val insufficientMessage = getString(R.string.balance_insufficient)
                    val finalMessage = "$walletBalanceText\n$insufficientMessage"

                    ShowAlert(
                        context = this,
                        alertTitle = getString(R.string.my_wallet),
                        icon = R.drawable.failed_message,
                        alertMessage = finalMessage
                    )
                }
            } else {
                binding.switchMyWalletPayment.isChecked = false
            }
        }

        addProductViewModel.isLoading.observe(this) {
            if (it) HelpFunctions.startProgressBar(this)
            else HelpFunctions.dismissProgressBar()
        }
        addProductViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError), this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError), this
                )
            }

        }
        addProductViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(
                        it.message!!, this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError), this
                    )
                }
            }

        }
    }

    private fun verifyPaymentOptionsDisplay(
        comeFromCart: Boolean,
        comeFromProductDetails: Boolean,
        comeFromMyOrders: Boolean,
        businessAccountId: String?,
        paymentOptions: List<PaymentOptions>?
    ) {
        // Helper to show/hide layout
        fun View.show() {
            visibility = View.VISIBLE
        }

        fun View.hide() {
            visibility = View.GONE
        }

        // Always hide all first (reset state)
        with(binding) {
            layoutVisaCreditCard.hide()
            layoutMadaPayment.hide()
            layoutMyWalletPayment.hide()
            layoutCashPayment.hide()
            layoutBankTransferPayment.hide()
        }

        // Case 1: From Cart or ProductDetails with businessAccountId
        if (comeFromCart || ((comeFromProductDetails || comeFromMyOrders) && businessAccountId != null)) {
            binding.layoutVisaCreditCard.show()
            binding.layoutMadaPayment.show()
            binding.layoutMyWalletPayment.show()
            return
        }

        // Case 2: From ProductDetails without businessAccountId
        if ((comeFromProductDetails || comeFromMyOrders) && businessAccountId == null && !paymentOptions.isNullOrEmpty()) {
            for (option in paymentOptions) {
                when (option.paymentOptionId) {
                    PaymentMethod.CreditCard.value -> binding.layoutVisaCreditCard.show()
                    PaymentMethod.Mada.value -> binding.layoutMadaPayment.show()
                    PaymentMethod.Cash.value -> binding.layoutCashPayment.show()
                    PaymentMethod.BankTransfer.value -> binding.layoutBankTransferPayment.show()
                }
            }
        }
    }
}