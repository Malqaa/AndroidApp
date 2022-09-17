package com.malka.androidappp.fragments.my_wallet

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import kotlinx.android.synthetic.main.fragment_my_wallet_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*


class my_wallet_fragment : Fragment(R.layout.fragment_my_wallet_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListenser()
    }

    private fun initView() {
        toolbar_title.text = getString(R.string.my_wallet)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        balance_withdraw.setOnClickListener {
            choose_account.show()


            recharge_the_balance.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            balance_withdraw.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )


            balance_withdraw.setTextColor(Color.parseColor("#FFFFFF"));
            recharge_the_balance.setTextColor(Color.parseColor("#45495E"));


        }
        recharge_the_balance.setOnClickListener {
            choose_account.hide()



            balance_withdraw.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            recharge_the_balance.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )


            recharge_the_balance.setTextColor(Color.parseColor("#FFFFFF"));
            balance_withdraw.setTextColor(Color.parseColor("#45495E"));
        }
    }


}