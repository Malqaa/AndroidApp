package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.property.sub_cat_b

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
import kotlinx.android.synthetic.main.fragment_commercial_sub_cat.*
import kotlinx.android.synthetic.main.fragment_residential__agriculture.*
import kotlinx.android.synthetic.main.fragment_residential_sub_cat.*


class ResidentialSubCat : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_residential_sub_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_subCatB.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_subCatB.title = "Sub Category"
        toolbar_subCatB.setTitleTextColor(Color.WHITE)
        toolbar_subCatB.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }


        land.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Land"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        villas.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Villas"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        buildings.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Buildings"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        apartments.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Apartments"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        holidayHouse.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Holiday House"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)

            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }


    }

}