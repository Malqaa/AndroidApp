package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.property.sub_cat_a

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.item_detail.property.sub_cat_b.CommercialSubCat
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_commercial.*
import kotlinx.android.synthetic.main.fragment_residential__agriculture.*


class Commercial : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commercial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_subCatAa.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_subCatAa.title = "Sub Category"
        toolbar_subCatAa.setTitleTextColor(Color.WHITE)
        toolbar_subCatAa.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        forSale4.setOnClickListener() {
            StaticClassAdCreate.proertySubCatA = "For Sale"

            StaticClassAdCreate.subCategoryPath.add(2,StaticClassAdCreate.proertySubCatA)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.proertySubCatA
            findNavController().navigate(R.id.property_subcatatob)
        }

        forLease.setOnClickListener() {
            StaticClassAdCreate.proertySubCatA = "For Lease"

            StaticClassAdCreate.subCategoryPath.add(2,StaticClassAdCreate.proertySubCatA)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.proertySubCatA
            findNavController().navigate(R.id.property_subcatatob)
        }


    }
}