package com.malka.androidappp.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.CommonBottomSheet
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.creditcard.CreditCardModel
import kotlinx.android.synthetic.main.fragment_payment_card_fragment.*
import kotlinx.android.synthetic.main.payment_card_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class PaymentCardFragment : Fragment(R.layout.fragment_payment_card_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()

        add_new_card._view3().setGravity(Gravity.CENTER)
        CommonAPI().GetUserCreditCards(requireContext()) {
            PaymentCardAdaptor(it)
        }
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
                CommonAPI().GetUserCreditCards(requireContext()) {
                    PaymentCardAdaptor(it)
                }
            }, false, true)
        }

    }


    private fun PaymentCardAdaptor(list: List<CreditCardModel>) {
        cardDetails_rcv.adapter = object : GenericListAdapter<CreditCardModel>(
            R.layout.payment_card_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        val s = StringBuilder(cardnumberformat())
                        var i = 4
                        while (i < s.length) {
                            s.insert(i, " ")
                            i += 5
                        }
                        card_number_tv.setText(s.toString())
                        expiry_date.text = expiryDate
                        card_user_name.text = card_holder_name
                        edit_card.setOnClickListener {
                            CommonBottomSheet().addCardBottomSheet(
                                requireContext(),
                                {},
                                true, true,
                                element,
                            )

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