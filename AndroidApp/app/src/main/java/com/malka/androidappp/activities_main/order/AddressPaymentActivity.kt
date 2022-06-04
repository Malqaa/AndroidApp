package com.malka.androidappp.activities_main.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.account_fragment.address.AddAddress
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.CommonBottomSheet
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.GeneralRespone
import com.malka.androidappp.servicemodels.GetAddressResponse
import com.malka.androidappp.servicemodels.Selection
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.servicemodels.checkout.CheckoutRequestModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_address_payment.*
import kotlinx.android.synthetic.main.cart_design_new.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressPaymentActivity : BaseActivity() {
    val deliveryOptionList: ArrayList<Selection> = ArrayList()
    val paymentMethodList: ArrayList<Selection> = ArrayList()
    var selectAddress: GetAddressResponse.AddressModel? = null
    var isSelect: Boolean = false
    val cartIds: MutableList<String> = mutableListOf()

    companion object {
        var totalAMount = "0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_payment)
        initView()
        setListenser()
    }


    private fun initView() {

        toolbar_title.text = getString(R.string.shopping_basket)

        loadAddress()
        setCategoryAdaptor()


    }

    val addAddressLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadAddress()
            }
        }

    private fun loadAddress() {
        CommonAPI().getAddress(this) {
            GenericAdaptor().AdressAdaptor(
                addAddressLaucher,
                this,
                address_rcv,
                it,
                ConstantObjects.Select
            ) {
                selectAddress = it
            }
        }
    }

    private fun setListenser() {
        add_new_add._view3().setGravity(Gravity.CENTER)
        add_new_add.setOnClickListener {
            addAddressLaucher.launch(Intent(this, AddAddress::class.java))
        }

        back_btn.setOnClickListener {
            finish()
        }
        btn_confirm_details.setOnClickListener {
            if (selectAddress == null) {
                showError(getString(R.string.Please_select, getString(R.string.ShippingAddress)))
            } else {
                val dd = R.id.shipping_address_to_payment
                CommonAPI().GetUserCreditCards(this) {
                    CommonBottomSheet().showCardSelection(this, it, {
                        CheckoutUserCart(it)
                    }) {
                        btn_confirm_details.performClick()
                    }
                }

            }
        }


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

    private fun setCategoryAdaptor() {

        calculation()


        cart_new_rcv.adapter = object : GenericListAdapter<CartItemModel>(
            R.layout.cart_design_new,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.advertisements.run {
                        //  prod_type.text=protype
                        //    prod_name.text=proname
                        //   prod_city.text=procity
                        item_quantity.number = qty
                        item_quantity.setOnValueChangeListener { view, oldValue, newValue ->
                            qty = newValue.toString()
                            ConstantObjects.usercart.get(position).advertisements.qty =
                                newValue.toString()

                            calculation()
                        }
                        shipment_no_tv.text = "${getString(R.string.shipment_no_1)}${position + 1}"
                        sellername_tv.text = sellername
                        prod_price.text = "$price ${getString(R.string.sar)}"
                        total_price.text = "$price ${getString(R.string.sar)}"

                        Picasso.get()
                            .load(ApiConstants.IMAGE_URL + image)
                            .into(prod_image)

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

                        if(paymentOptionSelection==null){

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
                        }else{
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
                                cart_new_rcv.adapter!!.notifyDataSetChanged()

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
                                cart_new_rcv.adapter!!.notifyDataSetChanged()


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

}