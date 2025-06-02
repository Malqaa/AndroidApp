package com.malka.androidappp.botmnav_fragments.home_view_allcategories

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.cardetail_page.ModelAddSellerFav
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_view_all_categories.*
import kotlinx.android.synthetic.main.view_allcategories_cardview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewAllCategories : Fragment(), ViewAllCatAdapter.onViewAllcateClickListener {

    val viewAllCatePosts: ArrayList<ModelViewCategories> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_all_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_viewallcategories.title = "All Categories"
        toolbar_viewallcategories.setTitleTextColor(Color.WHITE)
        toolbar_viewallcategories.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_viewallcategories.setNavigationOnClickListener { requireActivity().onBackPressed() }




        viewAllCatePosts.add(ModelViewCategories(R.drawable.vehicle, "Vehicle"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.home_property, "Property"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.boatsmarine, "Boats & marine"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.home_services, "Services"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.electronics, "Electronics & Gaming"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.petsanimals, "Pet & Animals"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.furniture, "Furniture's & Furnishings"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.fashion, "Shop Clothing &, Jewellery"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.appliances, "Appliances"))
        viewAllCatePosts.add(ModelViewCategories(R.drawable.health, "Health & Beauty"))

        val viewAllCatRecycler: RecyclerView =
            requireActivity().findViewById(R.id.viewAllCate_recyclerView)

        viewAllCatRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewAllCatRecycler.adapter = ViewAllCatAdapter(viewAllCatePosts, this@ViewAllCategories)

    }

    override fun onItemClick(item: ModelViewCategories, position: Int) {

        Toast.makeText(activity, item.textallcate + " is Clicked", Toast.LENGTH_LONG).show()

    }

    override fun addCatfav(item: ModelViewCategories, position: Int) {
        addCatFav(item.textallcate!!)
    }

    // Add cat to favorites
    fun addCatFav(catName: String) {
        try {

            val malqaa: MalqaApiService = RetrofitBuilder.addCatToFav()

            val call: Call<ModelAddCatFav> = malqaa.addCatFav(
                ModelAddCatFav(
                    categoryName = catName,
                    loggedInUserId = ConstantObjects.logged_userid
                )
            )

            call.enqueue(object : Callback<ModelAddCatFav> {
                override fun onResponse(
                    call: Call<ModelAddCatFav>, response: Response<ModelAddCatFav>
                ) {
                    if (response.isSuccessful) {

                        Toast.makeText(activity, "$catName Added to Favorites", Toast.LENGTH_LONG)
                            .show()

                    } else {
                        Toast.makeText(activity, "Failed to add favorites", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ModelAddCatFav>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            throw ex

        }
    }

}