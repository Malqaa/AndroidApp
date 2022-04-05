package com.malka.androidappp.botmnav_fragments.my_points

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_my_points_fragment.*


class my_points_fragment : Fragment(R.layout.fragment_my_points_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textView.setPaintFlags(textView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }


}