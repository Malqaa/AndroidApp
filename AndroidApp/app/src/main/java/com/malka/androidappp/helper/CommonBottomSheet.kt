package com.malka.androidappp.helper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.order.AddressPaymentActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.Basicresponse
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.servicemodels.creditcard.CreditCardRequestModel
import kotlinx.android.synthetic.main.activity_signup_pg4.*
import kotlinx.android.synthetic.main.add_card_layout.*
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.card_selection_layout.*
import kotlinx.android.synthetic.main.carspec_card8.*
import kotlinx.android.synthetic.main.paymnet_option_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonBottomSheet {


    fun showPaymentOption(context: Context, onConfirm: (type: String) -> Unit) {
        BottomSheetDialog(context).apply {
            setContentView(R.layout.paymnet_option_layout)
            check_saudi_bank_deposit.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    check_visa_mastercard.isChecked = false
                }
            }
            check_visa_mastercard.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    check_saudi_bank_deposit.isChecked = false

                }
            }
            bottom_sheet_btn1.setOnClickListener {
                if (check_saudi_bank_deposit.isChecked) {
                    dismiss()
                    onConfirm.invoke(context.getString(R.string.saudi_bank_deposit))

                } else if (check_visa_mastercard.isChecked) {
                    dismiss()
                    onConfirm.invoke(context.getString(R.string.visa_mastercard))
                } else {
                    (context as BaseActivity).showError(
                        context.getString(
                            R.string.Please,
                            context.getString(R.string.choose_your_payment_method)
                        )
                    )
                }

            }
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

    }
    var selectCard: CreditCardModel? = null

    fun showCardSelection(
        context: Context,
        list: List<CreditCardModel>,
        onConfirm: (selectCard: CreditCardModel) -> Unit
    ) {
        BottomSheetDialog(context).apply {
            setContentView(R.layout.card_selection_layout)
            add_a_new_account._view3().setGravity(Gravity.CENTER)
            cardAdaptor(card_rcv, list)
            order_amount_tv.text = "${AddressPaymentActivity.totalAMount} ${context.getString(R.string.SAR)}"
            select_card_btn.setOnClickListener {
                if (selectCard == null) {
                        (context as BaseActivity).showError(
                        context.getString(
                            R.string.Please_enter,
                            context.getString(R.string.Card)
                        )
                    )
                } else {
                    dismiss()
                    onConfirm.invoke(selectCard!!)
                }


            }

            add_a_new_account.setOnClickListener {
                addCardBottomSheet(context,{
                    onConfirm.invoke(selectCard!!)
                })
            }
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

    }

    private fun cardAdaptor(rcv: RecyclerView, list: List<CreditCardModel>) {
        rcv.adapter = object : GenericListAdapter<CreditCardModel>(
            R.layout.card_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        card_number_tv.text = cardnumber.getFilteredNumber()
                        main_layout.setSelected(isSelected)
                        card_number_tv.setSelected(isSelected)
                        select_card_radio.isChecked = isSelected
                        main_layout.setOnClickListener {
                            list.forEach {
                                it.isSelected = false
                            }
                            element.isSelected = true
                            rcv.post { rcv.adapter?.notifyDataSetChanged() }
                            selectCard=element
                        }
                        select_card_radio.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                main_layout.performClick()
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
                list
            )
        }
    }
    fun String.getFilteredNumber(): String {
        try {
            if (this.length > 4) {
                val endIndex = this.length - 4
                var replace = ""
                for (i in 1..endIndex) {
                    replace += "*"
                }
                return this.replaceRange(0, endIndex, replace)
            } else {
                return this
            }
        } catch (error: Exception) {
            return ""
        }
    }
    fun addCardBottomSheet(context: Context, onConfirm: (selectCard: CreditCardModel) -> Unit,
                           isUpdate: Boolean = false,
                           isSaveLater: Boolean = false,
                           oldCard: CreditCardModel ?= null) {
        BottomSheetDialog(context).apply {
            setContentView(R.layout.add_card_layout)
            card_expiry_tv._view().addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                    if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                        card_expiry_tv.setText(p0.toString() + "/")
                        card_expiry_tv._setSelection(card_expiry_tv.text.toString().length)
                    } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                        card_expiry_tv.setText(p0.toString().replace("/", ""))
                        card_expiry_tv._setSelection(card_expiry_tv.text.toString().length)
                    }
                }
            })
            if (isUpdate){
                oldCard!!.run {
                    Cardno_tv.text= cardnumberformat()
                    card_expiry_tv.text = expiryDate
                    card_holder_tv.text = card_holder_name
                    card_cvv_tv.text = cvcNumber
                    id
                    title_bottom_sheet.setText(context.getString(R.string.update_card))
                }



            }
            if (isSaveLater){
                switch_save_later.visibility = View.GONE
            }


            Confirm_btn.setOnClickListener {
                AddNewUserCard(context, this,onConfirm, isUpdate, oldCard)
            }
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

    }

    fun AddNewUserCard(context: Context, view: BottomSheetDialog,onConfirm: (selectCard: CreditCardModel) -> Unit,
                       isUpdate: Boolean, oldCard: CreditCardModel? = null) {
        if (view.Cardno_tv.text.toString().isEmpty()) {
                (context as BaseActivity).showError(
                context.getString(
                    R.string.Please_enter,
                    context.getString(R.string.Cardno)
                )
            )
        } else if (view.card_holder_tv.text.toString().isEmpty()) {
            (context as BaseActivity).showError(
                context.getString(
                    R.string.Please_enter,
                    context.getString(R.string.card_holder_s_name)
                )
            )
        } else if (view.card_expiry_tv.text.toString().isEmpty()) {
            (context as BaseActivity).showError(
                context.getString(
                    R.string.Please_enter,
                    context.getString(R.string.expiry_date)
                )
            )
        } else if (view.card_cvv_tv.text.toString().isEmpty()) {
            (context as BaseActivity).showError(
                context.getString(
                    R.string.Please_enter,
                    context.getString(R.string.cvv)
                )
            )
        } else {


            if (isUpdate){
                val cardinfo = CreditCardRequestModel(
                    card_number = view.Cardno_tv.text.toString(),
                    expiryDate = view.card_expiry_tv.text.toString(),
                    cvcNumber = view.card_cvv_tv.text.toString().toInt(),
                    userId = ConstantObjects.logged_userid,
                    id = oldCard!!.id
                )
                UpdateUserCreditCard(cardinfo, context )
            }else{
                val cardinfo = CreditCardRequestModel(
                    card_number = view.Cardno_tv.text.toString(),
                    expiryDate = view.card_expiry_tv.text.toString(),
                    cvcNumber = view.card_cvv_tv.text.toString().toInt(),
                    userId = ConstantObjects.logged_userid,
                    id = ""
                )
                InsertUserCreditCard(cardinfo, context, view,onConfirm)
            }



        }
    }

    fun InsertUserCreditCard(
        cardinfo: CreditCardRequestModel,
        context: Context,
        view: BottomSheetDialog,onConfirm: (selectCard: CreditCardModel) -> Unit
    ) {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<Basicresponse> = malqa.InsertUserCreditCard(cardinfo)

        call.enqueue(object : Callback<Basicresponse?> {
            override fun onFailure(call: Call<Basicresponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<Basicresponse?>,
                response: Response<Basicresponse?>
            ) {
                if (response.isSuccessful) {
                    val resp: Basicresponse = response.body()!!;
                    if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                        view. dismiss()
                        CommonAPI().GetUserCreditCards(context) {
                            showCardSelection(context, it) {
                                onConfirm.invoke(it)
                            }
                        }
                        HelpFunctions.ShowLongToast(
                            "Added Successfully",
                            context
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.Error),
                        context
                    );
                }
                HelpFunctions.dismissProgressBar()
            }
        })


    }




    fun UpdateUserCreditCard(
        cardinfo: CreditCardRequestModel,
        context: Context,

    ) {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<Basicresponse> = malqa.UpdateUserCreditCard(cardinfo)

        call.enqueue(object : Callback<Basicresponse?> {
            override fun onFailure(call: Call<Basicresponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<Basicresponse?>,
                response: Response<Basicresponse?>
            ) {
                if (response.isSuccessful) {
                    val resp: Basicresponse = response.body()!!;
                    if (resp.status_code == 200)
                    {
                        HelpFunctions.ShowLongToast(
                            "Update Successfully",
                            context
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.Error),
                        context
                    );
                }
                HelpFunctions.dismissProgressBar()
            }
        })


    }


}