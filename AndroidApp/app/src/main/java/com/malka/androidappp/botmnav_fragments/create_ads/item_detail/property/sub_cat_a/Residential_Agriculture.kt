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
import kotlinx.android.synthetic.main.fragment_property.*
import kotlinx.android.synthetic.main.fragment_property.toolbar_property
import kotlinx.android.synthetic.main.fragment_residential__agriculture.*


class Residential_Agriculture : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_residential__agriculture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_subCatA.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_subCatA.title = "Sub Category"
        toolbar_subCatA.setTitleTextColor(Color.WHITE)
        toolbar_subCatA.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        forSale.setOnClickListener(){
            StaticClassAdCreate.proertySubCatA = "For Sale"
            StaticClassAdCreate.propertySubCatB = "Agriculture For Sale"

            StaticClassAdCreate.subCategoryPath.add(2,StaticClassAdCreate.proertySubCatA)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.proertySubCatA
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_Asubcatbtoaddphoto)
        }

        forRent.setOnClickListener(){
            StaticClassAdCreate.proertySubCatA = "For rent"
            StaticClassAdCreate.propertySubCatB = "Agriculture For Rent"

            StaticClassAdCreate.subCategoryPath.add(2,StaticClassAdCreate.proertySubCatA)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.proertySubCatA
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_Asubcatbtoaddphoto)
        }

    }
}