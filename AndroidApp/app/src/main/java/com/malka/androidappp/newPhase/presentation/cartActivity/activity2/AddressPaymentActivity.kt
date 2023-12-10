package com.malka.androidappp.newPhase.presentation.cartActivity.activity2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.presentation.addressUser.addAddressActivity.AddAddressActivity
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartDataObject
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.addOrderResp.ProductOrderPaymentDetailsDto
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malka.androidappp.newPhase.presentation.addressUser.addressListActivity.AddressesAdapter
import com.malka.androidappp.newPhase.presentation.cartActivity.activity2.adapter.CartNewAdapter
import com.malka.androidappp.newPhase.presentation.cartActivity.activity3.SuccessOrderActivity
import com.malka.androidappp.newPhase.presentation.cartActivity.viewModel.CartViewModel
import kotlinx.android.synthetic.main.activity_address_payment.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("NotifyDataSetChanged")
class AddressPaymentActivity : BaseActivity(),
    AddressesAdapter.SetOnSelectedAddress, CartNewAdapter.SetProductNewCartListeners,
    SwipeRefreshLayout.OnRefreshListener {
    private var deliveryOptionSelect: String = "0"
    private var paymentOptionSelect: Int = 0
    private var paymentDetailsDtoList: ArrayList<Triple<Int, Int, Int>>? = null
    private var addressesAdapter: AddressesAdapter? = null
    private var cartNewAdapter: CartNewAdapter? = null
    private var cartViewModel: CartViewModel? = null
    private var userAddressesList: ArrayList<AddressItem>? = null
    private var productsCartList: ArrayList<CartProductDetails>? = null
    private var cartDataObject: CartDataObject? = null
    private var lastUpdateMainPosition: Int = 0
    private var lastUpdateProductPosition: Int = 0
    private var addressId: Int = 0
    var flagTypeSale = true
    var orderId = 0

    //    private var paymentDetailsList: ArrayList<ProductOrderPaymentDetailsDto> = ArrayList()
//    val deliveryOptionList: ArrayList<Selection> = ArrayList()
//    val paymentMethodList: ArrayList<Selection> = ArrayList()
//    val cartIds: MutableList<String> = mutableListOf()
//    var selectAddress: GetAddressResponse.AddressModel? = null
    var isSelect: Boolean = false
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
        setContentView(R.layout.activity_address_payment)
        flagTypeSale = intent.getBooleanExtra("flagTypeSale", true)
        orderId = intent.getIntExtra(ConstantObjects.orderNumberKey, 0)

        toolbar_title.text = getString(R.string.shopping_basket)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setAddressesAdapter()
        setCartNewAdapter()
        setupCartViewModel()
        setViewClickListeners()

        if (flagTypeSale) {
            titleCoupon.visibility = View.VISIBLE
            lay_activeCoupon.visibility = View.VISIBLE
        } else {
            titleCoupon.visibility = View.GONE
            lay_activeCoupon.visibility = View.GONE
        }
//        initView()
//        setListener()
        /****/


    }

    override fun onResume() {
        super.onResume()
        getCartList()
        cartViewModel?.getUserAddress()
    }

    private fun getCartList() {
        cartViewModel?.getCartList(SharedPreferencesStaticClass.getMasterCartId())
    }

    private fun setViewClickListeners() {
        paymentDetailsDtoList = arrayListOf()
        back_btn.setOnClickListener {
            finish()
        }
        add_new_add.setOnClickListener {
            addAddressLaucher.launch(Intent(this, AddAddressActivity::class.java))
        }

        btn_confirm_details.setOnClickListener {
//          val objAddress=  userAddressesList.find { isSelect }

            val mergedList = mergeTuplesInList(paymentDetailsDtoList ?: arrayListOf())
            val customList = mergedList.map { triple ->
                ProductOrderPaymentDetailsDto(triple.first, triple.second, triple.third)

            }.toCollection(ArrayList())
            if (flagTypeSale) {

                if (addressId == 0) {
                    HelpFunctions.ShowLongToast(getString(R.string.selectDeliveryAddress), this)
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

        btnApplyCode.setOnClickListener {
            if (edtCoupon.text.toString().trim() == "") {
                edtCoupon.error = getString(R.string.enter_the_coupon)
            } else {
                cartViewModel?.applyCouponOnCart(
                    SharedPreferencesStaticClass.getMasterCartId(),
                    edtCoupon.text.toString().trim()
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupCartViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel!!.isLoadingAddresses.observe(this) {
            if (it)
                progressBarAddresses.show()
            else
                progressBarAddresses.hide()
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
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        cartViewModel!!.userAddressesListObserver.observe(this) { userAddressResp ->
            if (userAddressResp.status_code == 200) {
                if (userAddressResp.addressesList != null && userAddressResp.addressesList?.isNotEmpty() == true) {
                    tvAddressError.hide()
                    userAddressesList?.clear()
                    userAddressesList?.addAll(userAddressResp.addressesList!!)
                    addressesAdapter?.notifyDataSetChanged()
                } else {
                    tvAddressError.show()
                }
            }
        }
        cartViewModel!!.increaseCartProductQuantityObserver.observe(this) { increaseProductResp ->
            if (increaseProductResp.status_code == 200) {
                productsCartList?.get(lastUpdateMainPosition)?.listProduct?.get(
                    lastUpdateProductPosition
                )
                    ?.let {
//                        if(it.qty==null){
                        it.cartProductQuantity = (it.cartProductQuantity ?: 0) + 1
//                        }else{
//                            it.qty = (it.qty?:0) + 1
//                        }
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
                total_tv.text = "0 ${getString(R.string.Rayal)}"
                subtotal_tv.text = "0 ${getString(R.string.Rayal)}"
                discount_tv.text = "0 ${getString(R.string.Rayal)}"
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
            }
            startActivity(intent)
            finish()
        }

        cartViewModel!!.deleteShipment.observe(this) {
            SharedPreferencesStaticClass.clearCartCount()
            HelpFunctions.ShowLongToast(it.message, this)
        }
    }

    private fun setCartNewAdapter() {
        productsCartList = ArrayList<CartProductDetails>()
        cartNewAdapter = CartNewAdapter(flagTypeSale, productsCartList ?: arrayListOf(), this)
        rvNewCart.apply {
            adapter = cartNewAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    //    private fun setCartTotalPrice() {
//        lifecycleScope.launch(Dispatchers.IO) {
//            var totalPrice = 0F
//            for (item in productsCartList) {
//                item.listProduct?.get(0)?.let { product ->
//                    if (product.priceDiscount == product.price) {
//                        totalPrice += (product.price * product.qty)
//                    } else {
//                        totalPrice += (product.priceDiscount * product.qty)
//                    }
//                }
//            }
//            withContext(Dispatchers.Main) {
//                subtotal_tv.text = "$totalPrice ${getString(R.string.Rayal)}"
//                var finalPrice = totalPrice
//                try {
//                    var priceCoupon = discount_tv.text.trim().toString().toFloat()
//                    finalPrice += priceCoupon
//                } catch (e: Exception) {
//                }
//
//                total_tv.text = "$finalPrice ${getString(R.string.Rayal)}"
//
//            }
//        }
//
//    }
    private fun setCartTotalPrice() {
        lifecycleScope.launch(Dispatchers.IO) {

//            for (item in productsCartList) {
//                item.listProduct?.get(0)?.let { product ->
//                    if (product.priceDiscount == product.price) {
//                        totalPrice += (product.price * product.qty)
//                    } else {
//                        totalPrice += (product.priceDiscount * product.qty)
//                    }
//                }
//            }
            withContext(Dispatchers.Main) {
//                subtotal_tv.text = "$totalPrice ${getString(R.string.Rayal)}"
//                var finalPrice = totalPrice
//                try {
//                    var priceCoupon = discount_tv.text.trim().toString().toFloat()
//                    finalPrice += priceCoupon
//                } catch (e: Exception) {
//                }
                if (cartDataObject != null) {
                    total_tv.text =
                        "${cartDataObject!!.totalPriceForCartFinal} ${getString(R.string.Rayal)}"
                    subtotal_tv.text =
                        "${cartDataObject!!.totalPriceForCartBeforeDiscount} ${getString(R.string.Rayal)}"
                    discount_tv.text =
                        "${cartDataObject!!.adminCouponcodeDiscount} ${getString(R.string.Rayal)}"
//                    var discount =
//                        cartDataObject!!.totalPriceForCartBeforeDiscount - cartDataObject!!.totalPriceForCartFinal
//                    discount_tv.text = "$discount ${getString(R.string.Rayal)}"
                }

            }
        }

    }

    private fun setAddressesAdapter() {
        userAddressesList = ArrayList()
        addressesAdapter = AddressesAdapter(userAddressesList ?: arrayListOf(), this, true)
        rvAddress.apply {
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
//        paymentDetailsList.add(
//            ProductOrderPaymentDetailsDto(
//                productId = productId,
//                paymentOption = paymentSelection,
//                shippingOption = deliveryOptionSelect.toInt()
//            )
//        )
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
//        paymentDetailsList.add(
//            ProductOrderPaymentDetailsDto(
//                productId = productId,
//                paymentOption = paymentOptionSelect,
//                shippingOption = deliveryOptionSelect.toInt()
//            )
//        )

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
        for (item in userAddressesList ?: arrayListOf()) {
            item.isSelected = false
        }
        userAddressesList?.get(position)?.isSelected = true
        addressId = userAddressesList?.get(position)?.id ?: 0
        addressesAdapter?.notifyDataSetChanged()
    }

    override fun setOnSelectedEditAddress(position: Int) {
        addAddressLaucher.launch(Intent(this, AddAddressActivity::class.java).apply {
            putExtra(ConstantObjects.isEditKey, true)
            putExtra(ConstantObjects.addressKey, userAddressesList?.get(position))
        })
    }

    override fun onDeleteAddress(position: Int) {
        //not used here
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        productsCartList?.clear()
        cartNewAdapter?.notifyDataSetChanged()
        userAddressesList?.clear()
        addressesAdapter?.notifyDataSetChanged()
        cartViewModel?.getUserAddress()
        getCartList()

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
    /****
     *******************
     *  *******************
     *   *******************
     *    *******************
     *     *******************
     *      *******************
     *       *******************
     */

//
//    private fun initView() {
//        loadAddress()
//        setCategoryAdaptor()
//
//
//    }
//
//
//    private fun loadAddress() {
//        CommonAPI().getAddress(this) {
//            GenericAdaptor().AdressAdaptor(
//                addAddressLaucher,
//                this,
//                rvAddress,
//                it,
//                ConstantObjects.Select
//            ) {
//                selectAddress = it
//            }
//        }
//    }
//
//    private fun setListener() {
//        add_new_add._view3().setGravity(Gravity.CENTER)
//
//
//    }
//
//    fun CheckoutUserCart(selectCard: CreditCardModel) {
//
//        val checkoutinfo = CheckoutRequestModel(
//            cartId = cartIds,
//            addressId = selectAddress!!.id,
//            tax = "0",
//            totalamount = totalAMount,
//            creditCardNo = selectCard.cardnumber!!,
//            loginId = ConstantObjects.logged_userid, "", arrayListOf(""), arrayListOf(0)
//        )
//        PostUserCheckOut(checkoutinfo, this)
//
//    }
//
//    fun PostUserCheckOut(checkoutinfo: CheckoutRequestModel, context: Context) {
//
//        val malqa: MalqaApiService = getRetrofitBuilder()
//        val call = malqa.PostUserCheckOut(checkoutinfo)
//
//        call.enqueue(object : Callback<GeneralRespone?> {
//            override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.Error),
//                    context
//                )
//            }
//
//            override fun onResponse(
//                call: Call<GeneralRespone?>,
//                response: Response<GeneralRespone?>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()!!.run {
//                        if (!isError) {
//                            startActivity(
//                                Intent(
//                                    this@AddressPaymentActivity,
//                                    SuccessOrder::class.java
//                                ).apply {
//                                    flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                    putExtra("order_number", id)
//                                    putExtra("shipments", ConstantObjects.usercart.size.toString())
//                                    putExtra("total_order", totalAMount)
//                                })
//                        } else {
//                            HelpFunctions.ShowLongToast(
//                                message,
//                                context
//                            )
//                        }
//                    }
//                }
//                HelpFunctions.dismissProgressBar()
//            }
//        })
//    }
//
//    @SuppressLint("ResourceType")
//    private fun setCategoryAdaptor() {
//        calculation()
//        rvNewCart.adapter =
//            object : GenericListAdapter<CartItemModel>(
//                R.layout.item_cart_design_new,
//                bind = { element, holder, itemCount, position ->
//                    holder.view.run {
//                        element.advertisements.run {
//                            //  prod_type.text=protype
//                            //    prod_name.text=proname
//                            //   prod_city.text=procity
////                            tvQuentity.number = qty
////                            tvQuentity.setOnValueChangeListener { view, oldValue, newValue ->
////                                qty = newValue.toString()
////                                ConstantObjects.usercart.get(position).advertisements.qty =
////                                    newValue.toString()
////
////                                calculation()
////                            }
//                            shipment_no_tv.text =
//                                "${getString(R.string.shipment_no_1)}${position + 1}"
//                            tvSellerName.text = sellername
//                            prod_price.text = "$price ${getString(R.string.sar)}"
//                            tvTotalPrice.text = "$price ${getString(R.string.sar)}"
//
//                            Picasso.get()
//                                .load(Constants.IMAGE_URL + image)
//                                .into(ivProductImage)
//
//                            delivery_option._view3().setGravity(Gravity.CENTER)
//                            payment_option_btn._view3().setGravity(Gravity.CENTER)
//
//
//                            if (deliveryOptionSelect == null) {
//                                delivery_option._view3().background = ContextCompat.getDrawable(
//                                    this@AddressPaymentActivity,
//                                    R.drawable.edittext_bg
//                                )
//                                delivery_option._view3().setTextColor(
//                                    ContextCompat.getColor(
//                                        this@AddressPaymentActivity,
//                                        R.color.hint_color
//                                    )
//                                );
//
//                            } else {
//                                delivery_option._view3().background = ContextCompat.getDrawable(
//                                    this@AddressPaymentActivity,
//                                    R.drawable.round_btn_ligh
//                                )
//                                delivery_option._view3()
//                                    .setTextColor(getResources().getColor(R.color.bg));
//
//                            }
//
//                            if (paymentOptionSelection == null) {
//
//                                payment_option_btn._view3().background = ContextCompat.getDrawable(
//                                    this@AddressPaymentActivity,
//                                    R.drawable.edittext_bg
//                                )
//                                payment_option_btn._view3().setTextColor(
//                                    ContextCompat.getColor(
//                                        this@AddressPaymentActivity,
//                                        R.color.hint_color
//                                    )
//                                );
//                            } else {
//                                payment_option_btn._view3().background = ContextCompat.getDrawable(
//                                    this@AddressPaymentActivity,
//                                    R.drawable.round_btn_ligh
//                                )
//                                payment_option_btn._view3()
//                                    .setTextColor(getResources().getColor(R.color.bg));
//
//                            }
//
//                            payment_option_btn.setOnClickListener {
//
//                                paymentMethodList.apply {
//                                    clear()
//                                    add(Selection(getString(R.string.Saudiabankdeposit)))
//                                    add(Selection(getString(R.string.visa_mastercard)))
//                                }
//                                paymentMethodList.forEach {
//                                    it.isSelected = it.name.equals(paymentOptionSelection)
//                                }
//                                CommonBottomSheet().commonSelctinDialog(
//                                    this@AddressPaymentActivity,
//                                    paymentMethodList, getString(R.string.PaymentOptions)
//                                ) {
//                                    paymentOptionSelection = it.name
//                                    rvNewCart.adapter!!.notifyDataSetChanged()
//
//                                }
//                            }
//                            delivery_option.setOnClickListener {
//
//
//                                deliveryOptionList.apply {
//                                    clear()
//                                    add(Selection("Option 1"))
//                                    add(Selection("Option 2"))
//                                    add(Selection("Option 3"))
//                                }
//                                deliveryOptionList.forEach {
//                                    it.isSelected = it.name.equals(deliveryOptionSelect)
//                                }
//                                CommonBottomSheet().commonSelctinDialog(
//                                    this@AddressPaymentActivity,
//                                    deliveryOptionList, getString(R.string.delivery_options)
//                                ) {
//                                    deliveryOptionSelect = it.name
//                                    rvNewCart.adapter!!.notifyDataSetChanged()
//
//
//                                }
//
//                            }
//                        }
//                    }
//                }
//            ) {
//                override fun getFilter(): Filter {
//                    TODO("Not yet implemented")
//                }
//
//            }.apply {
//                submitList(
//                    ConstantObjects.usercart
//                )
//            }
//    }
//
//    fun saveSelectedcheckbox() {
//
//        val list = deliveryOptionList.filter {
//            it.isSelected == true
//
//        }
//        list.forEach {
//
//        }
//    }
//
//
//    private fun calculation() {
//        var price = 0.0
//        ConstantObjects.usercart.forEach {
//            cartIds.add(it.advertisements.id)
//            price += it.advertisements.price.toDouble() * it.advertisements.qty.toDouble()
//        }
//        totalAMount = price.toString()
//
//
//        val discount = 0.0
//        val TaxAmount = price * 0 / 100
//        val total = price + TaxAmount - discount
//        subtotal_tv.text = "${price} ${getString(R.string.rial)}"
//        discount_tv.text = "-${discount} ${getString(R.string.rial)}"
//        total_tv.text = "${total} ${getString(R.string.rial)}"
//    }
//
//


}