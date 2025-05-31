package com.malka.androidappp.botmnav_fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_sold.*


class SoldFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_sold.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_sold.title = getString(R.string.Sold)
        toolbar_sold.setTitleTextColor(Color.WHITE)
        toolbar_sold.setNavigationOnClickListener {
            findNavController().navigate(R.id.sold_acc)
        }

        button6.setOnClickListener()
        {
        findNavController().navigate(R.id.sold_unsold)
        }
    }
}
