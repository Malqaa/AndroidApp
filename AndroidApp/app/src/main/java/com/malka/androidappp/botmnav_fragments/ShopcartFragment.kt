package com.malka.androidappp.botmnav_fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_shopcart.*


class ShopcartFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopcart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_shopcart.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_shopcart.setTitle("Shopping Cart")
        toolbar_shopcart.setTitleTextColor(Color.WHITE)
        toolbar_shopcart.setNavigationOnClickListener({
            //findNavController().navigate(R.id.cart_acc)
            activity!!.onBackPressed()
        })
    }
}
