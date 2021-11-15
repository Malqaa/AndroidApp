package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.appliances_category

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.CategoriesItem
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.JsonFileNames
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.boatmarine_category.BoatsMarineAdapter
import kotlinx.android.synthetic.main.fragment_appliances.*
import kotlinx.android.synthetic.main.fragment_boats_marine.*
import java.io.InputStream

class AppliancesFragment : Fragment(), AppliancesAdapter.onAppliancesClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appliances, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_appliances.setTitle("Appliances Category")
        toolbar_appliances.setTitleTextColor(Color.WHITE)
        toolbar_appliances.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_appliances.setNavigationOnClickListener(){activity!!.onBackPressed()}

        setupRecyclerView(parseJson()!!)
    }

    private fun parseJson(): Categories?{
        val appliances = loadJson(activity!!, JsonFileNames.APPLIANCES_CATEGORY)
        val gson = Gson()
        val data =gson.fromJson(appliances, Categories::class.java)
        return data
    }

    private fun setupRecyclerView(data: Categories)
    {
        appliancesCategoryRecyclerView.layoutManager = LinearLayoutManager(getActivity(),
            LinearLayoutManager.VERTICAL,false)
        appliancesCategoryRecyclerView.adapter = AppliancesAdapter(data,this)
    }

    fun loadJson(context: Context, fileName: String): String?
    { var input: InputStream? = null
        val jsonString : String

        try {
            input = context.assets.open(fileName)
            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            jsonString = String(buffer)
            return jsonString;
        }
        catch (ex: Exception){
            ex.printStackTrace()
        }
        finally {
            input?.close()
        }
        return null
    }

    override fun onItemClick(item : CategoriesItem, position:Int)
    {

    }
}