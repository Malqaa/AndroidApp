package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.property.sub_cat_a

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_commercial.*
import kotlinx.android.synthetic.main.fragment_residential.*

class Residential : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_residential, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_subCatAaa.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_subCatAaa.title = "Sub Category"
        toolbar_subCatAaa.setTitleTextColor(Color.WHITE)
        toolbar_subCatAaa.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        forSaleR.setOnClickListener() {
            StaticClassAdCreate.proertySubCatA = "For Sale"
            StaticClassAdCreate.subCategoryPath.add(2,StaticClassAdCreate.proertySubCatA)
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.proertySubCatA
            findNavController().navigate(R.id.property_subcatatobR)
        }

        forRentR.setOnClickListener() {
            StaticClassAdCreate.proertySubCatA = "For Rent"
            StaticClassAdCreate.subCategoryPath.add(2,StaticClassAdCreate.proertySubCatA)
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.proertySubCatA
            findNavController().navigate(R.id.property_subcatatobR)
        }


    }
}

