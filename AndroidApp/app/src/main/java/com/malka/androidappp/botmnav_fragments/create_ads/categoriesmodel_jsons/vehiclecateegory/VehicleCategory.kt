package com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.vehiclecateegory

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.CategoriesItem
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.JsonFileNames
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_all_cate.*
import java.io.InputStream


open class VehicleCategory : Fragment(), VehicleAdapter.onVehicleClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_cate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_allcats.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_allcats.title = "Vehicle Category"
        toolbar_allcats.setTitleTextColor(Color.WHITE)
        toolbar_allcats.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            //super.onBackPressed()
            //finish()
        }
        setUpRecyclerView(parseJson()!!)
    }

    private fun parseJson(): Categories? {
        val vehicle = loadJson(requireActivity(), JsonFileNames.VEHICLE_CATEGORY)
        val gson = Gson()
        val data = gson.fromJson(vehicle, Categories::class.java)
        return data
    }

    private fun setUpRecyclerView(data: Categories) {
        vehicleCategoryRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        vehicleCategoryRecyclerView.adapter = VehicleAdapter(data, this)

    }

    fun loadJson(context: Context, fileName: String): String? {
        var input: InputStream? = null
        val jsonString: String

        try {
            // Create InputStream
            input = context.assets.open(fileName)
            val size = input.available()
            // Create a buffer with the size
            val buffer = ByteArray(size)
            // Read data from InputStream into the Buffer
            input.read(buffer)
            // Create a json String
            jsonString = String(buffer)
            return jsonString;
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            // Must close the stream
            input?.close()
        }

        return null
    }

    override fun onItemClick(item: CategoriesItem, position: Int) {
        val getsubcat = item.name
        StaticClassAdCreate.subcat = getsubcat
        StaticClassAdCreate.subCategoryPath.add(1,getsubcat)

        //Zack
        //Date: 04/11/2021
        ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_1_key] = getsubcat
        findNavController().navigate(R.id.vehiclecat_addphoto)
    }
}