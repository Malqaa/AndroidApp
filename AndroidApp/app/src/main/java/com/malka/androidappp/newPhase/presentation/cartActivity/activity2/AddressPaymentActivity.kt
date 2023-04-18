package com.malka.androidappp.newPhase.presentation.cartActivity.activity2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.presentation.addressUser.addAddressActivity.AddAddressActivity
import com.malka.androidappp.newPhase.data.network.CommonAPI
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartDataObject
import com.malka.androidappp.newPhase.domain.models.cartListResp.CartProductDetails
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralRespone
import com.malka.androidappp.newPhase.domain.models.servicemodels.GetAddressResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malka.androidappp.newPhase.presentation.cartActivity.SuccessOrder
import com.malka.androidappp.newPhase.presentation.cartActivity.activity1.adapter.CartAdapter
import com.malka.androidappp.newPhase.presentation.cartActivity.activity2.adapter.AddressesAdapter
import com.malka.androidappp.newPhase.presentation.cartActivity.activity2.adapter.CartNewAdapter
import com.malka.androidappp.newPhase.presentation.cartActivity.viewModel.CartViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_address_payment.*
import kotlinx.android.synthetic.main.item_cart_design_new.*
import kotlinx.android.synthetic.main.item_product_in_cart.ivProductImage
import kotlinx.android.synthetic.main.item_product_in_cart.prod_price
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressPaymentActivity : BaseActivity(),
    AddressesAdapter.SetOnSelectedAddress, CartNewAdapter.SetProductNewCartListeners {

    lateinit var addressesAdapter: AddressesAdapter
    lateinit var cartNewAdapter: CartNewAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userAddressesList: ArrayList<AddressItem>
    private var lastUpdateMainPosition: Int = 0
    private var lastUpdateProductPosition: Int = 0
    private lateinit var productsCartList: ArrayList<CartProductDetails>
    private var addressId: Int = 0
    var cartDataObject: CartDataObject? = null

    /****/
    val deliveryOptionList: ArrayList<Selection> = ArrayList()
    val paymentMethodList: ArrayList<Selection> = ArrayList()
    var selectAddress: GetAddressResponse.AddressModel? = null
    var isSelect: Boolean = false
    val cartIds: MutableList<String> = mutableListOf()
    val addAddressLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                userAddressesList.clear()
                addressesAdapter.notifyDataSetChanged()
                cartViewModel.getUserAddress()
            }
        }

    companion object {
        var totalAMount = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_payment)
        toolbar_title.text = getString(R.string.shopping_basket)
        setAddressesAdapter()
        setCartNewAdapter()
        setupCartViewModel()
        setViewClickListeners()
//        initView()
//        setListenser()
        /****/
        cartViewModel.getUserAddress()


    }

    override fun onResume() {
        super.onResume()
        cartViewModel.getCartList(SharedPreferencesStaticClass.getMasterCartId())
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        add_new_add.setOnClickListener {
            addAddressLaucher.launch(Intent(this, AddAddressActivity::class.java))
        }

        btn_confirm_details.setOnClickListener {
            if (addressId == 0) {
                HelpFunctions.ShowLongToast(getString(R.string.selectDeliveryAddress), this)
            } else if (productsCartList.isEmpty()) {
                HelpFunctions.ShowLongToast(getString(R.string.empty_cart), this)
            } else {
                cartViewModel.addOrder(SharedPreferencesStaticClass.getMasterCartId(), addressId)
            }

        }
    }

    private fun setupCartViewModel() {
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.isLoadingAddresses.observe(this) {
            if (it)
                progressBarAddresses.show()
            else
                progressBarAddresses.hide()
        }
        cartViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        cartViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        cartViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
        cartViewModel.userAddressesListObserver.observe(this) { userAddressResp ->
            if (userAddressResp.status_code == 200) {
                if (userAddressResp.addressesList != null && userAddressResp.addressesList?.isNotEmpty() == true) {
                    tvAddressError.hide()
                    userAddressesList.clear()
                    userAddressesList.addAll(userAddressResp.addressesList!!)
                    addressesAdapter.notifyDataSetChanged()
                } else {
                    tvAddressError.show()
                }
            }
        }
        cartViewModel.increaseCartProductQuantityObserver.observe(this) { increaseProductResp ->
            if (increaseProductResp.status_code == 200) {
                productsCartList[lastUpdateMainPosition].listProduct?.get(lastUpdateProductPosition)
                    ?.let {
                        it.qty = it.qty + 1
                        cartDataObject?.let { cartDataObject ->
                            cartDataObject.totalPriceForCartFinal += it.priceDiscount
                            cartDataObject.totalPriceForCartBeforeDiscount += it.priceDiscount
                        }
                        productsCartList[lastUpdateMainPosition].couponAppliedBussinessAccountDto?.let{couponAppliedBussinessAccountDto->
                            couponAppliedBussinessAccountDto.businessAccountAmountBeforeCoupon+=it.priceDiscount
                            couponAppliedBussinessAccountDto.businessAccountAmountAfterCoupon+=it.priceDiscount
                            couponAppliedBussinessAccountDto.finalTotalPriceForBusinessAccount+=it.priceDiscount
                        }
                    }


                cartNewAdapter.notifyItemChanged(lastUpdateMainPosition)
                setCartTotalPrice()
            }
        }
        cartViewModel.decreaseCartProductQuantityObserver.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                productsCartList[lastUpdateMainPosition].listProduct?.get(lastUpdateProductPosition)
                    ?.let {
                        it.qty = it.qty - 1
                        cartDataObject?.let { cartDataObject ->
                            cartDataObject.totalPriceForCartFinal -= it.priceDiscount
                            cartDataObject.totalPriceForCartBeforeDiscount -= it.priceDiscount
                        }
                        productsCartList[lastUpdateMainPosition].couponAppliedBussinessAccountDto?.let{couponAppliedBussinessAccountDto->
                            couponAppliedBussinessAccountDto.businessAccountAmountBeforeCoupon-=it.priceDiscount
                            couponAppliedBussinessAccountDto.businessAccountAmountAfterCoupon-=it.priceDiscount
                            couponAppliedBussinessAccountDto.finalTotalPriceForBusinessAccount-=it.priceDiscount
                        }
                    }
                cartNewAdapter.notifyItemChanged(lastUpdateMainPosition)
                setCartTotalPrice()
            }
        }
        cartViewModel.removeProductFromCartProductsObserver.observe(this) { decreaseProductResp ->
            if (decreaseProductResp.status_code == 200) {
                var price=productsCartList[lastUpdateMainPosition].listProduct?.get(lastUpdateProductPosition)?.priceDiscount?:0f
                cartDataObject?.let { cartDataObject ->
                    cartDataObject.totalPriceForCartFinal -= price
                    cartDataObject.totalPriceForCartBeforeDiscount -= price
                }
                productsCartList[lastUpdateMainPosition].couponAppliedBussinessAccountDto?.let{couponAppliedBussinessAccountDto->
                    couponAppliedBussinessAccountDto.businessAccountAmountBeforeCoupon-=price
                    couponAppliedBussinessAccountDto.businessAccountAmountAfterCoupon-=price
                    couponAppliedBussinessAccountDto.finalTotalPriceForBusinessAccount-=price
                }
                productsCartList[lastUpdateMainPosition].listProduct?.removeAt(
                    lastUpdateProductPosition
                )

                cartNewAdapter.notifyDataSetChanged()
                setCartTotalPrice()
            }
        }
        cartViewModel.cartListRespObserver.observe(this) { cartListResp ->
            if (cartListResp.status_code == 200) {
                if (cartListResp.cartDataObject?.listCartProducts != null) {
                    cartDataObject = cartListResp.cartDataObject
                    productsCartList.clear()
                    productsCartList.addAll(cartListResp.cartDataObject.listCartProducts)
                    cartNewAdapter.notifyDataSetChanged()
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
    }

    private fun setCartNewAdapter() {
        productsCartList = ArrayList<CartProductDetails>()
        cartNewAdapter = CartNewAdapter(productsCartList, this)
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
                    total_tv.text = "${cartDataObject!!.totalPriceForCartFinal} ${getString(R.string.Rayal)}"
                }

            }
        }

    }

    private fun setAddressesAdapter() {
        userAddressesList = ArrayList()
        addressesAdapter = AddressesAdapter(userAddressesList, this)
        rvAddress.apply {
            adapter = addressesAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


    override fun onIncreaseQuantityProduct(position: Int, mainPosition: Int) {
        lastUpdateMainPosition = mainPosition
        lastUpdateProductPosition = position
        var productCartId = productsCartList[mainPosition].listProduct!![position].cartproductId
        cartViewModel.increaseCartProductQuantity(productCartId.toString())
    }

    override fun onDecreaseQuantityProduct(position: Int, mainPosition: Int) {
        lastUpdateMainPosition = mainPosition
        lastUpdateProductPosition = position
        var productCartId = productsCartList[mainPosition].listProduct!![position].cartproductId
        cartViewModel.decreaseCartProductQuantity(productCartId.toString())
    }

    override fun onDeleteProduct(position: Int, mainPosition: Int) {
        lastUpdateMainPosition = mainPosition
        lastUpdateProductPosition = position
        var productCartId = productsCartList[mainPosition].listProduct!![position].cartproductId
        cartViewModel.removeProductFromCartProducts(productCartId.toString())
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


    private fun initView() {
        loadAddress()
        setCategoryAdaptor()


    }


    private fun loadAddress() {
        CommonAPI().getAddress(this) {
            GenericAdaptor().AdressAdaptor(
                addAddressLaucher,
                this,
                rvAddress,
                it,
                ConstantObjects.Select
            ) {
                selectAddress = it
            }
        }
    }

    private fun setListenser() {
        add_new_add._view3().setGravity(Gravity.CENTER)


    }

    fun CheckoutUserCart(selectCard: CreditCardModel) {

        val checkoutinfo = CheckoutRequestModel(
            cartId = cartIds,
            addressId = selectAddress!!.id,
            tax = "0",
            totalamount = totalAMount,
            creditCardNo = selectCard.cardnumber!!,
            loginId = ConstantObjects.logged_userid, "", arrayListOf(""), arrayListOf(0)
        )
        PostUserCheckOut(checkoutinfo, this)

    }

    fun PostUserCheckOut(checkoutinfo: CheckoutRequestModel, context: Context) {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.PostUserCheckOut(checkoutinfo)

        call.enqueue(object : Callback<GeneralRespone?> {
            override fun onFailure(call: Call<GeneralRespone?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(
                    getString(R.string.Error),
                    context
                )
            }

            override fun onResponse(
                call: Call<GeneralRespone?>,
                response: Response<GeneralRespone?>
            ) {
                if (response.isSuccessful) {
                    response.body()!!.run {
                        if (!isError) {
                            startActivity(
                                Intent(
                                    this@AddressPaymentActivity,
                                    SuccessOrder::class.java
                                ).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    putExtra("order_number", id)
                                    putExtra("shipments", ConstantObjects.usercart.size.toString())
                                    putExtra("total_order", totalAMount)
                                })
                        } else {
                            HelpFunctions.ShowLongToast(
                                message,
                                context
                            )
                        }
                    }
                }
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    @SuppressLint("ResourceType")
    private fun setCategoryAdaptor() {
        calculation()
        rvNewCart.adapter =
            object : GenericListAdapter<CartItemModel>(
                R.layout.item_cart_design_new,
                bind = { element, holder, itemCount, position ->
                    holder.view.run {
                        element.advertisements.run {
                            //  prod_type.text=protype
                            //    prod_name.text=proname
                            //   prod_city.text=procity
//                            tvQuentity.number = qty
//                            tvQuentity.setOnValueChangeListener { view, oldValue, newValue ->
//                                qty = newValue.toString()
//                                ConstantObjects.usercart.get(position).advertisements.qty =
//                                    newValue.toString()
//
//                                calculation()
//                            }
                            shipment_no_tv.text =
                                "${getString(R.string.shipment_no_1)}${position + 1}"
                            tvSellerName.text = sellername
                            prod_price.text = "$price ${getString(R.string.sar)}"
                            tvTotalPrice.text = "$price ${getString(R.string.sar)}"

                            Picasso.get()
                                .load(Constants.IMAGE_URL + image)
                                .into(ivProductImage)

                            delivery_option._view3().setGravity(Gravity.CENTER)
                            payment_option_btn._view3().setGravity(Gravity.CENTER)


                            if (deliveryOptionSelect == null) {
                                delivery_option._view3().background = ContextCompat.getDrawable(
                                    this@AddressPaymentActivity,
                                    R.drawable.edittext_bg
                                )
                                delivery_option._view3().setTextColor(
                                    ContextCompat.getColor(
                                        this@AddressPaymentActivity,
                                        R.color.hint_color
                                    )
                                );

                            } else {
                                delivery_option._view3().background = ContextCompat.getDrawable(
                                    this@AddressPaymentActivity,
                                    R.drawable.round_btn_ligh
                                )
                                delivery_option._view3()
                                    .setTextColor(getResources().getColor(R.color.bg));

                            }

                            if (paymentOptionSelection == null) {

                                payment_option_btn._view3().background = ContextCompat.getDrawable(
                                    this@AddressPaymentActivity,
                                    R.drawable.edittext_bg
                                )
                                payment_option_btn._view3().setTextColor(
                                    ContextCompat.getColor(
                                        this@AddressPaymentActivity,
                                        R.color.hint_color
                                    )
                                );
                            } else {
                                payment_option_btn._view3().background = ContextCompat.getDrawable(
                                    this@AddressPaymentActivity,
                                    R.drawable.round_btn_ligh
                                )
                                payment_option_btn._view3()
                                    .setTextColor(getResources().getColor(R.color.bg));

                            }

                            payment_option_btn.setOnClickListener {

                                paymentMethodList.apply {
                                    clear()
                                    add(Selection(getString(R.string.Saudiabankdeposit)))
                                    add(Selection(getString(R.string.visa_mastercard)))
                                }
                                paymentMethodList.forEach {
                                    it.isSelected = it.name.equals(paymentOptionSelection)
                                }
                                CommonBottomSheet().commonSelctinDialog(
                                    this@AddressPaymentActivity,
                                    paymentMethodList, getString(R.string.PaymentOptions)
                                ) {
                                    paymentOptionSelection = it.name
                                    rvNewCart.adapter!!.notifyDataSetChanged()

                                }
                            }
                            delivery_option.setOnClickListener {


                                deliveryOptionList.apply {
                                    clear()
                                    add(Selection("Option 1"))
                                    add(Selection("Option 2"))
                                    add(Selection("Option 3"))
                                }
                                deliveryOptionList.forEach {
                                    it.isSelected = it.name.equals(deliveryOptionSelect)
                                }
                                CommonBottomSheet().commonSelctinDialog(
                                    this@AddressPaymentActivity,
                                    deliveryOptionList, getString(R.string.delivery_options)
                                ) {
                                    deliveryOptionSelect = it.name
                                    rvNewCart.adapter!!.notifyDataSetChanged()


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
                    ConstantObjects.usercart
                )
            }
    }

    fun saveSelectedcheckbox() {

        val list = deliveryOptionList.filter {
            it.isSelected == true

        }
        list.forEach {

        }
    }


    private fun calculation() {
        var price = 0.0
        ConstantObjects.usercart.forEach {
            cartIds.add(it.advertisements.id)
            price += it.advertisements.price.toDouble() * it.advertisements.qty.toDouble()
        }
        totalAMount = price.toString()


        val discount = 0.0
        val TaxAmount = price * 0 / 100
        val total = price + TaxAmount - discount
        subtotal_tv.text = "${price} ${getString(R.string.rial)}"
        discount_tv.text = "-${discount} ${getString(R.string.rial)}"
        total_tv.text = "${total} ${getString(R.string.rial)}"
    }

    override fun setOnSelectedAddress(position: Int) {
        for (item in userAddressesList) {
            item.isSelected = false
        }
        userAddressesList[position].isSelected = true
        addressId = userAddressesList[position].id
        addressesAdapter.notifyDataSetChanged()
    }

    override fun setOnSelectedEditAddress(position: Int) {
        addAddressLaucher.launch(Intent(this, AddAddressActivity::class.java).apply {
            putExtra(ConstantObjects.isEditKey, true)
            putExtra(ConstantObjects.addressKey, userAddressesList[position])
        })
    }


}