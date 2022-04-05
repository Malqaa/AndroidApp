package com.malka.androidappp.activities_main.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.add_product.SuccessProduct
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.account_fragment.address.AddAddress
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.CommonBottomSheet
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_address_payment.*
import kotlinx.android.synthetic.main.cart_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class AddressPaymentActivity : BaseActivity() {
    var selectAddress: GetAddressResponse.AddressModel? = null

    companion object {
        var totalAMount = "10"
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
            GenericAdaptor().AdressAdaptor(address_rcv, it, ConstantObjects.Select) {
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
                    CommonBottomSheet().showCardSelection(this, it) {
                        moveSuccessScreen()
                    }
                }

//                CommonBottomSheet().showPaymentOption(this) {
//                    when(it){
//                        getString(R.string.saudi_bank_deposit)->{
//
//                        }
//                        getString(R.string.visa_mastercard)->{
//                            CommonAPI().GetUserCreditCards(this) {
//                                CommonBottomSheet().showCardSelection(this, it) {
//
//                                }
//                            }
//
//                        }
//                    }
//
//
//                }
            }
        }

    }


    fun moveSuccessScreen() {
        startActivity(Intent(this@AddressPaymentActivity, SuccessOrder::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        })
        finish()
    }


    private fun setCategoryAdaptor() {
        cart_rcv.adapter = object : GenericListAdapter<CartItemModel>(
            R.layout.cart_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.advertisements.run {
                        //  prod_type.text=protype
                        //    prod_name.text=proname
                        //   prod_city.text=procity
                        prod_price.text = "$price ${getString(R.string.sar)}"
                        Picasso.get()
                            .load(ApiConstants.IMAGE_URL + image)
                            .into(prod_image)
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
}