package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.boatmarine_category

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
import kotlinx.android.synthetic.main.fragment_boats_marine.*
import java.io.InputStream


class BoatsMarineFragment : Fragment() , BoatsMarineAdapter.onBoatsmarineClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boats_marine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_boatmarine.setTitle("Boats And Marine")
        toolbar_boatmarine.setTitleTextColor(Color.WHITE)
        toolbar_boatmarine.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_boatmarine.setNavigationOnClickListener(){
            activity!!.onBackPressed() }

        setupRecyclerView(parseJson()!!)
    }

    private fun parseJson():Categories?{
        val boatsmarine = loadJson(activity!!, JsonFileNames.BOATSMARINE_CATEGORY)
        val gson = Gson()
        val data =gson.fromJson(boatsmarine, Categories::class.java)
        return data
    }

    private fun setupRecyclerView(data: Categories)
    {
        boatmarineCategoryRecyclerView.layoutManager = LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false)
        boatmarineCategoryRecyclerView.adapter = BoatsMarineAdapter(data,this)
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

 override fun onItemClick(item : CategoriesItem , position:Int)
 {

 }
}