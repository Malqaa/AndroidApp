package com.malka.androidappp.newPhase.data.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.cartActivity.activity2.AddressPaymentActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.BasicResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import kotlinx.android.synthetic.main.add_card_layout.*
import kotlinx.android.synthetic.main.add_card_layout.title_bottom_sheet
import kotlinx.android.synthetic.main.bank_accounts.*
import kotlinx.android.synthetic.main.bank_bottom_design.view.*
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.card_selection_layout.*
import kotlinx.android.synthetic.main.delivery_option.*
import kotlinx.android.synthetic.main.delivery_option.view.*
import kotlinx.android.synthetic.main.delivery_option_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonBottomSheet {


    fun showPaymentOption(title:String,list: ArrayList<Selection>,context: Context, onConfirm: (type: Selection) -> Unit) {
        BottomSheetDialog(context).apply {
            setContentView(R.layout.delivery_option)
            tvTitleAr.text=title
            btn_confirm_delivery.text=context.getText(R.string.the_next)
            close_delivery_alert.hide()
            btn_confirm_delivery.setOnClickListener {
                if (selection == null) {
                    (context as BaseActivity).showError(context.getString(R.string.choose_your_payment_method))
                } else {
                    onConfirm.invoke(selection!!)
                    dismiss()

                }
            }
            setDeliveryOptionAdapter(list, delivery_option_rcv)
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

    }

    var selectCard: CreditCardModel? = null

    fun showCardSelection(
        context: Context,
        list: List<CreditCardModel>,
        onConfirm: (selectCard: CreditCardModel) -> Unit,
        onNewCardAdded: () -> Unit
    ) {
        selectCard = null
        BottomSheetDialog(context).apply {
            setContentView(R.layout.card_selection_layout)
            btnAddNewAccount._view3().setGravity(Gravity.CENTER)
            cardAdaptor(card_rcv, list)
            order_amount_tv.text =
                "${AddressPaymentActivity.totalAMount} ${context.getString(R.string.SAR)}"
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

            btnAddNewAccount.setOnClickListener {
                addCardBottomSheet(context, {
                    onNewCardAdded.invoke()
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
                        card_number_tv.text = cardnumber!!.getFilteredNumber()
                        main_layout.setSelected(isSelected)
                        card_number_tv.setSelected(isSelected)
                        select_card_radio.isChecked = isSelected
                        main_layout.setOnClickListener {
                            list.forEach {
                                it.isSelected = false
                            }
                            list.get(position).isSelected=true
                            rcv.post { rcv.adapter?.notifyDataSetChanged() }
                            selectCard = element
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

    fun addCardBottomSheet(
        context: Context,
        onSuccess: () -> Unit,
        isUpdate: Boolean = false,
        isSaveLater: Boolean = false,
        oldCard: CreditCardModel? = null
    ) {
        BottomSheetDialog(context).apply {
            setContentView(R.layout.add_card_layout)
            card_expiry_tv._view().addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(
                    p0: CharSequence?,
                    start: Int,
                    removed: Int,
                    added: Int
                ) {
                    if (start == 1 && start + added == 2 && p0?.contains('/') == false) {
                        card_expiry_tv.setText(p0.toString() + "/")
                        card_expiry_tv._setSelection(card_expiry_tv.text.toString().length)
                    } else if (start == 3 && start - removed == 2 && p0?.contains('/') == true) {
                        card_expiry_tv.setText(p0.toString().replace("/", ""))
                        card_expiry_tv._setSelection(card_expiry_tv.text.toString().length)
                    }
                }
            })
            if (isUpdate) {
                oldCard!!.run {
                    Cardno_tv.text = cardnumberformat()
                    card_expiry_tv.text = expiryDate
                    card_holder_tv.text = card_holder_name
                    card_cvv_tv.text = cvcNumber
                    id
                    title_bottom_sheet.setText(context.getString(R.string.update_card))
                }


            }
            if (isSaveLater) {
                switch_save_later.visibility = View.GONE
            }


            Confirm_btn.setOnClickListener {
                AddNewUserCard(context, this, isUpdate, oldCard, onSuccess)
            }
            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }

    }


    fun bankaAccountBottomSheet(context: Context, list: List<Negotiationmodel>) {
        BottomSheetDialog(context).apply {
            setContentView(R.layout.bank_accounts)

            userBankAdapter(bank_rcv, list, false)

            getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }


    }


    private fun userBankAdapter(
        rcv: RecyclerView,
        banklist: List<Negotiationmodel>,
        isSelect: Boolean
    ) {

        rcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.bank_bottom_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        bank_name.text = proname


//                        if (is_selectb) {
//                            bank_detail.show()
//
//
//                        } else {
//                            bank_detail.hide()
//
//
//                        }
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(

                banklist
            )
        }
    }

    fun AddNewUserCard(
        context: Context,
        view: BottomSheetDialog,
        isUpdate: Boolean,
        oldCard: CreditCardModel? = null,
        onSuccess: () -> Unit,

        ) {
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


            if (isUpdate) {
                val cardinfo = CreditCardModel(
                    card_number = view.Cardno_tv.text.toString(),
                    expiryDate = view.card_expiry_tv.text.toString(),
                    card_holder_name = view.card_holder_tv.text.toString(),
                    cvcNumber = view.card_cvv_tv.text.toString(),
                    userId = ConstantObjects.logged_userid,
                    id = oldCard!!.id!!
                )
                UpdateUserCreditCard(cardinfo, context, onSuccess)
            } else {
                val cardinfo = CreditCardModel(
                    card_number = view.Cardno_tv.text.toString(),
                    expiryDate = view.card_expiry_tv.text.toString(),
                    card_holder_name = view.card_holder_tv.text.toString(),
                    cvcNumber = view.card_cvv_tv.text.toString(),
                    userId = ConstantObjects.logged_userid,
                    id = ""
                )
                InsertUserCreditCard(cardinfo, context, view, onSuccess)
            }


        }
    }

    fun InsertUserCreditCard(
        cardinfo: CreditCardModel,
        context: Context,
        view: BottomSheetDialog, onSuccess: () -> Unit,
    ) {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<BasicResponse> = malqa.InsertUserCreditCard(cardinfo)

        call.enqueue(object : Callback<BasicResponse?> {
            override fun onFailure(call: Call<BasicResponse?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<BasicResponse?>,
                response: Response<BasicResponse?>
            ) {
                if (response.isSuccessful) {
                    val resp: BasicResponse = response.body()!!;
                    if (resp.status_code == 200 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                        view.dismiss()
                        onSuccess.invoke()
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
        cardinfo: CreditCardModel,
        context: Context,
        onSuccess: () -> Unit,
    ) {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<BasicResponse> = malqa.UpdateUserCreditCard(cardinfo)

        call.enqueue(object : Callback<BasicResponse?> {
            override fun onFailure(call: Call<BasicResponse?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<BasicResponse?>,
                response: Response<BasicResponse?>
            ) {
                if (response.isSuccessful) {
                    val resp: BasicResponse = response.body()!!
                    if (resp.status_code == 200) {
                        onSuccess.invoke()
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

    var selection: Selection? = null

    fun commonSelctinDialog(
        context: Context,
        list: ArrayList<Selection>,
        title: String,
        onConfirm: (type: Selection) -> Unit
    ) {
        selection = null
       val filtelistr= list.filter {
           it.isSelected
        }
        if(filtelistr.size>0){
            selection=filtelistr.get(0)
        }
        val builder = AlertDialog.Builder(context)
            .create()

        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = (context as Activity).layoutInflater.inflate(R.layout.delivery_option, null)
        builder.setView(view)


        view.apply {
            tvTitleAr.text=title
            close_delivery_alert.setOnClickListener {
                builder.dismiss()
            }
            btn_confirm_delivery.setOnClickListener {
                if (selection == null) {
                    (context as BaseActivity).showError(context.getString(R.string.Selectanyoneoption))
                } else {
                    onConfirm.invoke(selection!!)
                    builder.dismiss()

                }
            }

            setDeliveryOptionAdapter(list, delivery_option_rcv)
        }


        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }

    fun setDeliveryOptionAdapter(list: ArrayList<Selection>, rcv: RecyclerView) {
        rcv.adapter =
            object : GenericListAdapter<Selection>(
                R.layout.delivery_option_layout,
                bind = { element, holder, itemCount, position ->
                    holder.view.run {
                        element.run {
                            delivery_layout.setSelected(isSelected)
                            delivery_option_tv.setSelected(isSelected)
                            delivery_option_rb.isChecked=isSelected
                            delivery_option_tv.text = name
                            delivery_layout.setOnClickListener {
                                list.forEach {
                                    it.isSelected = false
                                }
                                element.isSelected = true
                                rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                selection=element
                            }
                            delivery_option_rb.setOnCheckedChangeListener { buttonView, isChecked ->
                                if (isChecked) {
                                    delivery_layout.performClick()
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



}