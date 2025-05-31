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
import kotlinx.android.synthetic.main.fragment_residential_sub_cat.*


class CommercialSubCat : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commercial_sub_cat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_subCatBb.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_subCatBb.title = "Sub Category"
        toolbar_subCatBb.setTitleTextColor(Color.WHITE)
        toolbar_subCatBb.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        lands.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Lands"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)

            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        shops.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Shops"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        hotels.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Hotels"
            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        factories.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Factories"
            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        offices.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Offices"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        workShops.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Workshops"

            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }

        warehouses.setOnClickListener(){
            StaticClassAdCreate.propertySubCatB = "Warehouses"
            StaticClassAdCreate.subCategoryPath.add(3,StaticClassAdCreate.propertySubCatB)
            //Zack
            //Date: 04/11/2021
            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_3_key] = StaticClassAdCreate.propertySubCatB
            findNavController().navigate(R.id.property_subcatbtoaddphoto)
        }
    }
}