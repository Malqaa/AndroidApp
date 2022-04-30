package com.malka.androidappp.botmnav_fragments.selling_option

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_selling_option.*


class SellingOption : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selling_option, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_sellingopt.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_sellingopt.title = getString(R.string.SellingOptions)
        toolbar_sellingopt.navigationIcon?.isAutoMirrored = true
        toolbar_sellingopt.setTitleTextColor(Color.WHITE)
        toolbar_sellingopt.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        rr1.setOnClickListener() {
            rd1.setChecked(true)
            rd2.setChecked(false)
            r6_sellingopt3.setBackgroundColor(resources.getColor(R.color.greenApp))
            r7_sellingopt3.setBackgroundColor(resources.getColor(R.color.graishhh))
        }

        rr2.setOnClickListener() {
            rd2.setChecked(true)
            rd1.setChecked(false)
            r7_sellingopt3.setBackgroundColor(resources.getColor(R.color.greenApp))
            r6_sellingopt3.setBackgroundColor(resources.getColor(R.color.graishhh))

        }


        rd1.setOnClickListener() {
            rd1.setChecked(true)
            rd2.setChecked(false)
            r6_sellingopt3.setBackgroundColor(resources.getColor(R.color.greenApp))
            r7_sellingopt3.setBackgroundColor(resources.getColor(R.color.graishhh))
        }

        rd2.setOnClickListener() {
            rd2.setChecked(true)
            rd1.setChecked(false)
            r7_sellingopt3.setBackgroundColor(resources.getColor(R.color.greenApp))
            r6_sellingopt3.setBackgroundColor(resources.getColor(R.color.graishhh))
        }



        r2_sellingopt1.setOnClickListener() {
            findNavController().navigate(R.id.sellingopt_blacklist)
        }

    }
}