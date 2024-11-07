package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityAddressPaymentBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.ErrorAddOrder
import com.malqaa.androidappp.newPhase.domain.models.addOrderResp.ProductOrderPaymentDetailsDto
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartDataObject
import com.malqaa.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.AddressViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addAddressActivity.AddAddressActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addressListActivity.AddressesAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.adapter.CartNewAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity3.SuccessOrderActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.viewModel.CartViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter.CurrentOrderAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private var addressId: Int = 0
    var flagTypeSale = true
    var fromNegotiation = false
    var orderId = 0

    private var lastSelectedPosition = 0

    private val addAddressLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                userAddressesList?.clear()
                addressesAdapter?.notifyDataSetChanged()
                cartViewModel?.getUserAddress()
            }
        }

    companion object {
        var totalAMount = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityAddressPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        flagTypeSale = intent.getBooleanExtra("flagTypeSale", true)
        fromNegotiation = intent.getBooleanExtra("fromNegotiation", false)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.CheckoutProduct)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
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
            val mergedList = mergeTuplesInList(paymentDetailsDtoList ?: arrayListOf())
            val customList = mergedList.map { triple ->
                ProductOrderPaymentDetailsDto(triple.first, triple.second, triple.third)

            }.toCollection(ArrayList())
            if (flagTypeSale) {

                if (addressId == 0) {
                    HelpFunctions.ShowLongToast(getString(R.string.selectDeliveryAddress), this)
                } else if (deliveryOptionSelect == "0") {
                    HelpFunctions.ShowLongToast(getString(R.string.please_delivery_options), this)
                } else if (paymentOptionSelect == 0) {
                    HelpFunctions.ShowLongToast(getString(R.string.please_payment_options), this)
                } else if (productsCartList!!.isEmpty()) {
                    HelpFunctions.ShowLongToast(getString(R.string.empty_cart), this)
                } else {
                    cartViewModel?.addOrder(
                        SharedPreferencesStaticClass.getMasterCartId(),
                        addressId,
                        paymentOptionSelect,
                        deliveryOptionSelect,
                        customList
                    )

                }
            } else {
                if (addressId == 0) {
                    HelpFunctions.ShowLongToast(getString(R.string.selectDeliveryAddress), this)
                } else {
                    cartViewModel?.addPaymentTransaction(
                        cartDataObject!!.totalPriceForCartFinal,
                        cartDataObject!!.totalPriceForCartBeforeDiscount,
                        SharedPreferencesStaticClass.getMasterCartId(),
                        addressId,
                        paymentOptionSelect,
                        deliveryOptionSelect,
                        customList
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
        cartViewModel!!.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                val errorList = it.data as List<ErrorAddOrder>
                if (errorList.isEmpty()) {
                    if (it.message != null) {
                        HelpFunctions.ShowLongToast(it.message!!, this)
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                    }
                } else {
                    for (i in productsCartList!!) {
                        for (product in i.listProduct!!) {
                            val error = errorList.find { product.id == it.produtId }
                            if (error != null) {
                                val remainingProduct =
                                    (product.qty ?: 0) - (product.cartProductQuantity ?: 0)
                                val message = getString(
                                    R.string.sold_out_quantity,
                                    product.cartProductQuantity,
                                    error.quantity,
                                    error.productName
                                )
                                if (error.quantity == 0) {
                                    product.msgError = getString(R.string.notFoundQuantity)
                                } else
                                    product.msgError = message
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
//                    cartDataObject = resp.orderDetailsByMasterIDData
//                    productsCartList?.clear()
//                    productsCartList?.addAll(resp.orderDetailsByMasterIDData )
//                    cartNewAdapter?.notifyDataSetChanged()
//                    setCartTotalPrice()
//                    resp.orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount
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
                    binding.totalTv.text =
                        "${resp.orderDetailsByMasterIDData.totalOrderMasterAmountAfterDiscount ?: resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount} ${
                            getString(
                                R.string.Rayal
                            )
                        }"
                    binding.subtotalTv.text =
                        "${resp.orderDetailsByMasterIDData.totalOrderMasterAmountBeforDiscount} ${
                            getString(
                                R.string.Rayal
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
                binding.totalTv.text = "0 ${getString(R.string.Rayal)}"
                binding.subtotalTv.text = "0 ${getString(R.string.Rayal)}"
                binding.discountTv.text = "0 ${getString(R.string.Rayal)}"
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
                putExtra(
                    ConstantObjects.orderShippingSectionNumberKey,
                    productsCartList?.size.toString()
                )
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

    private fun setCartTotalPrice() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (cartDataObject != null) {
                    binding.totalTv.text =
                        "${cartDataObject!!.totalPriceForCartFinal} ${getString(R.string.Rayal)}"
                    binding.subtotalTv.text =
                        "${cartDataObject!!.totalPriceForCartBeforeDiscount} ${getString(R.string.Rayal)}"
                    binding.discountTv.text =
                        "${cartDataObject!!.adminCouponcodeDiscount} ${getString(R.string.Rayal)}"
                }

            }
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

    override fun onSelectPayment(productId: Int, paymentSelection: Int) {
        paymentOptionSelect = paymentSelection
        paymentDetailsDtoList?.add(
            Triple(
                productId,
                paymentSelection,
                deliveryOptionSelect.toInt()
            )
        )
    }

    override fun onSelectDelivery(productId: Int, deliverySelection: String) {
        deliveryOptionSelect = deliverySelection
        paymentDetailsDtoList?.add(
            Triple(
                productId,
                paymentOptionSelect,
                deliveryOptionSelect.toInt()
            )
        )
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
}