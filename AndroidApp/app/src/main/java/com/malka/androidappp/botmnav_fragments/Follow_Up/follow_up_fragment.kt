package com.malka.androidappp.botmnav_fragments.Follow_Up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.toolbar_main.*


class follow_up_fragment : Fragment(R.layout.fragment_follow_up_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()

    }

    private fun initView() {
        toolbar_title.text = getString(R.string.payment_cards)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }


}