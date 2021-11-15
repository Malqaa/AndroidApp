package com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.service_category

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
import com.malka.androidappp.botmnav_fragments.categoriesmodel_jsons.vehiclecateegory.VehicleAdapter
import kotlinx.android.synthetic.main.fragment_all_cate.*
import kotlinx.android.synthetic.main.fragment_services_category.*
import java.io.InputStream


class ServicesCategoryFragment : Fragment() , ServicesCatAdapter.onServicesClickListener{


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services_category, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_servicecat.setTitle("Service Category")
        toolbar_servicecat.setTitleTextColor(Color.WHITE)
        toolbar_servicecat.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_servicecat.setNavigationOnClickListener(){activity!!.onBackPressed()}

        setUpRecyclerView(parseJson()!!)


    }

    private fun parseJson(): Categories? {
        val services = loadJson(activity!!, JsonFileNames.SERVICES_CATEGORY)
        val gson = Gson()
        val data = gson.fromJson(services, Categories::class.java)
        return data
    }

    private fun setUpRecyclerView(data: Categories) {
        serviceCategoryRecyclerView.layoutManager =
            LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        serviceCategoryRecyclerView.adapter = ServicesCatAdapter(data, this)

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
        //Listen data
    }
}
