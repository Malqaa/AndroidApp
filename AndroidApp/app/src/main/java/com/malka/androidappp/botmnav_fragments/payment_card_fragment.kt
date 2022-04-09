package com.malka.androidappp.botmnav_fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.getCardDetailsModel
import com.malka.androidappp.helper.CommonBottomSheet
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.creditcard.CreditCardModel
import kotlinx.android.synthetic.main.fragment_payment_card_fragment.*
import kotlinx.android.synthetic.main.payment_card_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class payment_card_fragment : Fragment(R.layout.fragment_payment_card_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCardDetails()
        initView()
        setListenser()

        add_new_card._view3().setGravity(Gravity.CENTER)

    }

    private fun initView() {
        toolbar_title.text = getString(R.string.payment_cards)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        add_new_card.setOnClickListener {
            CommonBottomSheet().addCardBottomSheet(requireContext(), {
//                onConfirm.invoke(selectCard!!)
            }, false, true)
        }

    }


    fun getCardDetails() {

        HelpFunctions.startProgressBar(requireActivity())

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getCardDetails(ConstantObjects.logged_userid)


        call?.enqueue(object : retrofit2.Callback<getCardDetailsModel?> {
            override fun onFailure(call: retrofit2.Call<getCardDetailsModel?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: retrofit2.Call<getCardDetailsModel?>,
                response: retrofit2.Response<getCardDetailsModel?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: getCardDetailsModel = response.body()!!
                        if (respone.status_code == 200) {

                            PaymentCardAdaptor(respone.data)

                        } else {

                            HelpFunctions.ShowLongToast(
                                getString(R.string.ErrorOccur),
                                requireContext()
                            )
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })


    }


    private fun PaymentCardAdaptor(list: List<CreditCardModel>) {
        cardDetails_rcv.adapter = object : GenericListAdapter<CreditCardModel>(
            R.layout.payment_card_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        var s: StringBuilder
                        s = StringBuilder(cardnumberformat())


//                        cardnumber = cardnumber.replace(" " , "")


                        var i = 4
                        while (i < s.length) {
                            s.insert(i, " ")
                            i += 5
                        }
                        card_number.setText(s.toString())

                        expiry_date.text = expiryDate
                        card_user_name.text = card_holder_name ?: "Card Holder Name"


                        edit_card.setOnClickListener {
                            CommonBottomSheet().addCardBottomSheet(requireContext(), {
                            }, true,
                                true ,element, )

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