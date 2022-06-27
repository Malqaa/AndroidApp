package com.malka.androidappp.botmnav_fragments.my_points

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_my_points_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*


class my_points_fragment : Fragment(R.layout.fragment_my_points_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView.setPaintFlags(textView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        initView()
        setListenser()
    }

    private fun initView() {
        toolbar_title.text = getString(R.string.my_points)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

}