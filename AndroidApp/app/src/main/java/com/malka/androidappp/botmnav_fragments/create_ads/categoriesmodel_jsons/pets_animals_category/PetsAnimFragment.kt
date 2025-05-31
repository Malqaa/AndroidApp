package com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.pets_animals_category

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.Categories
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.CategoriesItem
import com.malka.androidappp.botmnav_fragments.create_ads.categoriesmodel_jsons.JsonFileNames
import kotlinx.android.synthetic.main.fragment_pets_anim.*
import java.io.InputStream


class PetsAnimFragment : Fragment(), PetsAnimAdapter.onPetsAnimClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pets_anim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_petsanim.setTitle("Pets And Animals Category")
        toolbar_petsanim.setTitleTextColor(Color.WHITE)
        toolbar_petsanim.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_petsanim.setNavigationOnClickListener(){activity!!.onBackPressed()}

        setUpRecyclerView(parseJson()!!)
    }

    private fun parseJson(): Categories? {
        val petsanim = loadJson(activity!!, JsonFileNames.PETS_ANIMALS_CATEGORY)
        val gson = Gson()
        val data = gson.fromJson(petsanim, Categories::class.java)
        return data
    }

    private fun setUpRecyclerView(data: Categories) {
        petsanimCategoryRecyclerView.layoutManager =
            LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        petsanimCategoryRecyclerView.adapter = PetsAnimAdapter(data, this)

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
        val getsubcat = item.name
        StaticClassAdCreate.subcat = getsubcat
        findNavController().navigate(R.id.petanimal_addphoto)
    }
}