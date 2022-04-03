package com.malka.androidappp.activities_main.order

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Filter
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.account_fragment.address.AddAddress
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.addtocart.CartItemModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardRequestModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_address_payment.*
import kotlinx.android.synthetic.main.add_card_layout.*
import kotlinx.android.synthetic.main.bottom_sheet1.*
import kotlinx.android.synthetic.main.bottom_sheet2.*
import kotlinx.android.synthetic.main.cart_design.view.*
import kotlinx.android.synthetic.main.fragment_add_new_card.*
import kotlinx.android.synthetic.main.toolbar_main.*

class AddressPaymentActivity : BaseActivity() {
    var selectAddress: GetAddressResponse.AddressModel? = null

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
            GenericAdaptor().AdressAdaptor(category_rcv, it, ConstantObjects.Select) {
                selectAddress = it
            }
        }
    }

    private fun setListenser() {
        add_new_add.setOnClickListener {
            addAddressLaucher.launch(Intent(this, AddAddress::class.java))
        }


        back_btn.setOnClickListener {
            finish()
        }
        btn_confirm_details.setOnClickListener {
            if (selectAddress == null) {
                val dd = com.malka.androidappp.R.id.shipping_address_to_payment

                showBottomSheetDialog()
            } else {
                showError(getString(R.string.Please_select, getString(R.string.ShippingAddress)))
            }
        }

    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet1)
        bottomSheetDialog.check_saudi_bank_deposit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bottomSheetDialog.check_visa_mastercard.isChecked = false
            }
        }
        bottomSheetDialog.check_visa_mastercard.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bottomSheetDialog.check_saudi_bank_deposit.isChecked = false

            }
        }
        bottomSheetDialog.bottom_sheet_btn1.setOnClickListener {
            bottomSheetDialog.dismiss()
            showBottomShee2tDialog()
        }
        bottomSheetDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()
    }

    private fun showBottomShee2tDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet2)
        bottomSheetDialog.bottom_sheet_btn2.setOnClickListener {
            bottomSheetDialog.dismiss()


        }

        bottomSheetDialog.add_a_new_account.setOnClickListener {
            bottomSheetDialog.dismiss()
            addCardBottomSheet()
        }
        bottomSheetDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()
    }

    private fun addCardBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.add_card_layout)
        bottomSheetDialog.Confirm_btn.setOnClickListener {
            bottomSheetDialog.dismiss()
            AddNewUserCard(bottomSheetDialog)
        }

        bottomSheetDialog.add_a_new_account.setOnClickListener {
            bottomSheetDialog.dismiss()

        }
        bottomSheetDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()
    }


    fun AddNewUserCard(view: BottomSheetDialog) {
        if (view.Cardno_tv.text.toString().isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Cardno)))
        } else if (view.card_holder_tv.text.toString().isEmpty()) {
            showError(
                getString(
                    R.string.Please_enter,
                    getString(R.string.card_holder_s_name)
                )
            )
        } else if (view.card_expiry_tv.text.toString().isEmpty()) {
            showError(
                getString(
                    R.string.Please_enter,
                    getString(R.string.expiry_date)
                )
            )
        } else if (view.card_cvv_tv.text.toString().isEmpty()) {
            showError(
                getString(
                    R.string.Please_enter,
                    getString(R.string.cvv)
                )
            )
        } else {
            val cardinfo = CreditCardRequestModel(
                card_number = view.Cardno_tv.text.toString(),
                expiryDate = view.card_expiry_tv.text.toString(),
                cvcNumber = view.card_cvv_tv.text.toString().toInt(),
                userId = ConstantObjects.logged_userid,
                id = ""
            )
            val resp = HelpFunctions.InsertUserCreditCard(cardinfo, this@AddressPaymentActivity)
            if (resp) {

            }



        }
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