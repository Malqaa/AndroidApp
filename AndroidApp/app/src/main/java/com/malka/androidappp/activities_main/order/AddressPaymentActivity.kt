package com.malka.androidappp.activities_main.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.account_fragment.address.AddAddress
import com.malka.androidappp.servicemodels.GetAddressResponse
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.CommonBottomSheet
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.BasicResponse
import com.malka.androidappp.servicemodels.ConstantObjects
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
    var price = 0.0
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
        val call: Call<BasicResponse> = malqa.PostUserCheckOut(checkoutinfo)

        call.enqueue(object : Callback<BasicResponse?> {
            override fun onFailure(call: Call<BasicResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(
                    getString(R.string.Error),
                    context
                )
            }

            override fun onResponse(
                call: Call<BasicResponse?>,
                response: Response<BasicResponse?>
            ) {
                if (response.isSuccessful) {
                    val resp: BasicResponse = response.body()!!;
                    if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {

                        startActivity(
                            Intent(
                                this@AddressPaymentActivity,
                                SuccessOrder::class.java
                            ).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                putExtra("shipments", ConstantObjects.usercart.size.toString())
                                putExtra("total_order", totalAMount)
                            })
                        finish()
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.Error),
                        context
                    )
                }
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun setCategoryAdaptor() {
        if (ConstantObjects.usercart.size > 0) {
            for (IndItem in ConstantObjects.usercart) {
                cartIds.add(IndItem.advertisements.id)
                price += IndItem.advertisements.price.toDouble()
            }
        }
        totalAMount = price.toString()
        calculation(price)


        cart_new_rcv.adapter = object : GenericListAdapter<CartItemModel>(
            R.layout.cart_design_new,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.advertisements.run {
                        //  prod_type.text=protype
                        //    prod_name.text=proname
                        //   prod_city.text=procity
                        shipment_no_tv.text = "${getString(R.string.shipment_no_1)}${position + 1}"
                        sellername_tv.text = sellername
                        prod_price.text = "$price ${getString(R.string.sar)}"
                        total_price.text = "$price ${getString(R.string.sar)}"

                        Picasso.get()
                            .load(ApiConstants.IMAGE_URL + image)
                            .into(prod_image)

                        delivery_option._view3().setGravity(Gravity.CENTER)
                        payment_option_btn._view3().setGravity(Gravity.CENTER)

                        payment_option_btn.setOnClickListener {
                            paymentMethodList.apply {
                                clear()
                                add(Selection(getString(R.string.Saudiabankdeposit)))
                                add(Selection(getString(R.string.visa_mastercard)))
                            }
                            CommonBottomSheet().commonSelctinDialog(
                                this@AddressPaymentActivity,
                                paymentMethodList,getString(R.string.PaymentOptions)
                            ) {

                            }
                        }



                        delivery_option.setOnClickListener {
                            deliveryOptionList.apply {
                                clear()
                                add(Selection("Option 1"))
                                add(Selection("Option 2"))
                                add(Selection("Option 3"))
                            }
                            CommonBottomSheet().commonSelctinDialog(
                                this@AddressPaymentActivity,
                                deliveryOptionList,getString(R.string.delivery_options)
                            ) {

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

    private fun calculation(totalPrice: Double) {
        val discount = 0.0
        val TaxAmount = totalPrice * 0 / 100
        val total = totalPrice + TaxAmount - discount
        subtotal_tv.text = "${totalPrice} ${getString(R.string.rial)}"
        discount_tv.text = "-${discount} ${getString(R.string.rial)}"
        total_tv.text = "${total} ${getString(R.string.rial)}"
    }


}