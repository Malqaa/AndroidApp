package com.malka.androidappp.fragments.browse_market.popup_subcategories_list

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.fragments.create_ads.categoriesmodel_jsons.Categories
import com.malka.androidappp.fragments.create_ads.categoriesmodel_jsons.CategoriesItem
import com.malka.androidappp.fragments.create_ads.categoriesmodel_jsons.JsonFileNames
import java.io.InputStream


class SubcategoriesDialogFragment : AppCompatDialogFragment(),
    AdapterSubCategories.onPostItemClickLisenter {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.alert_subcategories_context, null)
//
//

        if (StaticGetSubcategoryByBrowseCateClick.getcategory == "Vehicles") {
            val subcategory = loadJson(requireContext(), JsonFileNames.VEHICLE_CATEGORY)
            StaticGetSubcategoryByBrowseCateClick.getsubcategory = subcategory!!
        } else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "Property") {
            val subcategory = loadJson(requireContext(), JsonFileNames.PROPERTY_CATEGORY)
            StaticGetSubcategoryByBrowseCateClick.getsubcategory = subcategory!!
        } else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "General") {
            //else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "Boats & Marine") {
            val subcategory = loadJson(requireContext(), JsonFileNames.BOATSMARINE_CATEGORY)
            StaticGetSubcategoryByBrowseCateClick.getsubcategory = subcategory!!
        }else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "General") {
        //else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "Electronics & video games") {
            val subcategory = loadJson(requireContext(), JsonFileNames.ELECTRONIC_GAME_CATEGORY)
            StaticGetSubcategoryByBrowseCateClick.getsubcategory = subcategory!!
        }else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "General") {
        //else if (StaticGetSubcategoryByBrowseCateClick.getcategory == "Furniture's") {
            val subcategory = loadJson(requireContext(), JsonFileNames.FURNITURE_FURNISH_CATEGORY)
            StaticGetSubcategoryByBrowseCateClick.getsubcategory = subcategory!!
        }

//
//
        val getsubcategory = StaticGetSubcategoryByBrowseCateClick.getsubcategory
        if(getsubcategory!=null && getsubcategory.toString().trim().length>0) {
            val gson = Gson()
            val data = gson.fromJson(getsubcategory, Categories::class.java)
            val subcatRecycler: RecyclerView = view.findViewById(R.id.subcat_recycler)
            subcatRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            subcatRecycler.adapter = AdapterSubCategories(data, this)
        }
        val setTitleText: TextView = view.findViewById(R.id.tx1)
        setTitleText.setText(StaticGetSubcategoryByBrowseCateClick.getcategory)
        builder.setView(view)
        val alertbox: AlertDialog = builder.create()
        alertbox.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return alertbox
    }

    fun loadJson(context: Context, fileName: String): String? {
        var input: InputStream? = null
        val jsonString: String

        try {
            input = context.assets.open(fileName)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            jsonString = String(buffer)
            return jsonString;
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            input?.close()
        }
        return null
    }


    override fun onItemClick(item: CategoriesItem, adapterPosition: Int) {
        val selecteditem = item.name
        Toast.makeText(activity, selecteditem + "Sub-Category is Selected", Toast.LENGTH_LONG)
            .show()
        dismiss()
    }
}
