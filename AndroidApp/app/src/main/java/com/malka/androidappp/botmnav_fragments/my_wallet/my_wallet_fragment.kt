package com.malka.androidappp.botmnav_fragments.my_wallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
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

    }


}