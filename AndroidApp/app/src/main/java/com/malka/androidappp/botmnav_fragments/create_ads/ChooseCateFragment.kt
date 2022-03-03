package com.malka.androidappp.botmnav_fragments.create_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.item_detail.all_categories.AdapterAllCategories
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.botmnav_fragments.my_product.*
import com.malka.androidappp.design.add_product4
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_choose_cate.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseCateFragment : Fragment(), AdapterSuggestedCategories.OnItemClickListener,
    AdapterAllCategories.OnItemClickListener {


    val suggestedCategories: ArrayList<Data> = ArrayList()
    lateinit var suggestedCategoryText: TextView
    val allCategoryList: ArrayList<AllCategoriesModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        HelpFunctions.startProgressBar(requireActivity())
        suggestedCategories.clear()
        allCategoryList.clear()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_cate, container, false)



    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        add_product_button4.setOnClickListener(){
            if (!allCategoryList[position].isCategory) {

                StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())

                val args = Bundle()
                args.putString("Title", allCategoryList[position].categoryName.toString())

                var templateName = truncateString(allCategoryList[position].template.toString())
                StaticClassAdCreate.template = templateName

//            StaticClassAdCreate.subcat = allCategoryList[position].categoryKey.toString()
                findNavController().navigate(R.id.all_cat_to_add_photo, args)

            }else {

                StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())

                val args = Bundle()
                args.putString("categoryid", allCategoryList[position].categoryKey.toString())
                args.putString("categoryName", allCategoryList[position].categoryName.toString())
                NavHostFragment.findNavController(this@ChooseCateFragment)
                    .navigate(R.id.all_cat_to_sub_cat, args)
            }

        }



        hidekeyboard()

        suggestedCategoryText = requireActivity().findViewById(R.id.textView37)

        toolbar_choosecat.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_choosecat.title = "Choose Category"
        toolbar_choosecat.setTitleTextColor(Color.WHITE)
        toolbar_choosecat.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            //  super.onBackPressed()
            // finish()
        }
        getCategoryTags(StaticClassAdCreate.producttitle)

        toolbar_choosecat.inflateMenu(R.menu.choosecat_search)
        toolbar_choosecat.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_searchh) {
                // do something
                choose_float.visibility = View.VISIBLE
                choose_float.setSearchFocused(true)
            } else {
            }
            true
        }


        /////////////////Select Template/////////////////////////////


        //////////////////////////////////////////////
//        motorcardv.setOnClickListener() {
//            getonAutoMobTemplate()
//            StaticClassAdCreate.mainCategory = "Vehicles"
//            StaticClassAdCreate.subCategoryPath.add(0,"Vehicles")
//            findNavController().navigate(R.id.chosecat_vehiclecat)
//
//        }
//        propertycardv.setOnClickListener() {
//            getonPropertyTemplate()
//            StaticClassAdCreate.mainCategory = "Property"
//
//            StaticClassAdCreate.subCategoryPath.add(0,"Property")
//            findNavController().navigate(R.id.choosecat_propertycat)
//        }
//        computercardv.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_boatsmarine)
//        }
//        homeliv.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_servicecat)
//        }
//        mobilephone.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_electronicgamecat)
//        }
//        pet_animal.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_petsanimcat)
//        }
//        furniture_furnishing.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_futniturecat)
//        }
//        shopping_mall.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_shopcloth)
//        }
//        appliances.setOnClickListener(){
//            getonGeneralTemplate()
//            findNavController().navigate(R.id.choosecat_appliances)
//        }
//        health_beauty.setOnClickListener(){
//
//        }

        //////////////Switch to uppercards to addphotos/////////////////////////////
//        hhh.setOnClickListener() {
//            val gettemplate = allcategorynamegeneral.text.toString().trim()
//            StaticClassAdCreate.template = gettemplate
//            StaticClassAdCreate.subcat = "General"
//            StaticClassAdCreate.mainCategory = "General"
//            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_1_key] = StaticClassAdCreate.mainCategory
//            ConstantObjects.dynamic_json_dictionary[ConstantObjects.subcategory_2_key] = StaticClassAdCreate.subcat
//
//            StaticClassAdCreate.subCategoryPath.add(0,"General")
//
//            switchaddphoto()
//        }

//        hhh2.setOnClickListener(){
//            val gettemplate = catnaamtitlee.getText().toString().trim()
//            StaticClassAdCreate.template = gettemplate
//            switchaddphoto()}

//        hhh1.setOnClickListener(){
//            val gettemplate = catnaamtitleeee.getText().toString().trim()
//            StaticClassAdCreate.template = gettemplate
//            switchaddphoto()}

        getAllCategories()

    }

    fun switchaddphoto() {
        findNavController().navigate(R.id.choosecat_addphoto)
        /////Must add framelayout in XML with id // <FrameLayout
        //    android:id="@+id/contentFragment"
        //    android:layout_width="fill_parent"
        //    android:layout_height="fill_parent"
        //    android:layout_weight="1" />
        // or add just id of another xml
        //val fragment = AddPhotoFragment()
        //val fragmentManager = fragmentManager
        //val fragmentTransaction = fragmentManager!!.beginTransaction()
        //fragmentTransaction.setCustomAnimations(R.anim.slide_up_in, R.anim.slide_up_out)
        //fragmentTransaction.replace(R.id.layout_123, fragment)
        //fragmentTransaction.addToBackStack(null)
        //fragmentTransaction.commit()


    }

    fun getonAutoMobTemplate() {
        StaticClassAdCreate.template = "Vehicles"
    }

    fun getonGeneralTemplate() {
        StaticClassAdCreate.template = "General"
    }

    fun getonPropertyTemplate() {
        StaticClassAdCreate.template = "Property"
    }

    // To get suggested categories
    fun getCategoryTags(category: String) {
        try {
            val malqaa: MalqaApiService = RetrofitBuilder.getCategoryTags(category)

            val call: Call<CategoryTagsModel> = malqaa.getCategoryTags(category)

            call.enqueue(object : Callback<CategoryTagsModel> {
                override fun onResponse(
                    call: Call<CategoryTagsModel>, response: Response<CategoryTagsModel>
                ) {
                    if (response.isSuccessful) {

                        if (response.body() != null) {

                            var resp: CategoryTagsModel = response.body()!!
                            var lists: List<Data> = resp.data

                            if (lists != null && lists.count() > 0) {
                                for (IndProperty in lists) {
                                    suggestedCategories.add(
                                        Data(
                                            IndProperty.categoryid,
                                            IndProperty.keyword,
                                            IndProperty.lang,
                                            IndProperty.name,
                                            IndProperty.path,
                                            IndProperty.searchcategory_id,
                                            IndProperty.tags,
                                        )
                                    )
                                }
                                val suggestedCategoriesRecycler: RecyclerView =
                                    requireActivity().findViewById(R.id.recycler_suggested_category)


                                suggestedCategoriesRecycler.adapter =
                                    AdapterSuggestedCategories(
                                        suggestedCategories,
                                        ChooseCateFragment(),
                                        this@ChooseCateFragment
                                    )

                            } else {
                                // Do Something
                                suggestedCategoryText.visibility = View.GONE
                            }
                        }


                    } else {
                        Toast.makeText(activity, "Failed to get tags", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<CategoryTagsModel>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }

    override fun OnItemClick(position: Int) {
        super.OnItemClick(position)
        Toast.makeText(context, "You clicked me !", Toast.LENGTH_SHORT).show()
//        val args = Bundle()
//        args.putString("AdvId", myProductPosts[position].id)
//        args.putString("sellerID", myProductPosts[position].userId)
//        NavHostFragment.findNavController(this@MyProduct)
//            .navigate(R.id.my_product_to_product_detail, args)
    }

    // To get all categories
    fun getAllCategories() {

        try {
            val malqaa: MalqaApiService = RetrofitBuilder.getAllCategories()
            val call: Call<AllCategoriesResponseBack> = malqaa.getAllCategories()

            call.enqueue(object : Callback<AllCategoriesResponseBack> {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onResponse(
                    call: Call<AllCategoriesResponseBack>,
                    response: Response<AllCategoriesResponseBack>
                ) {

                    if (response.isSuccessful) {

                        if (response.body() != null) {

                            var resp: AllCategoriesResponseBack = response.body()!!
                            var lists: List<AllCategoriesModel> = resp.data

                            if (lists != null && lists.count() > 0) {
                                for (IndCategories in lists) {
                                    allCategoryList.add(
                                        AllCategoriesModel(
                                            IndCategories.id ?: "0",
                                            IndCategories.categoryid ?: 0,
                                            IndCategories.categoryName ?: "",
                                            IndCategories.categoryKey ?: "0",
                                            IndCategories.categoryParentId ?: 0,
                                            IndCategories.isCategory,
                                            IndCategories.isActive,
                                            IndCategories.createdBy ?: "0",
                                            IndCategories.createdOn ?: "0",
                                            IndCategories.template ?: ""
                                        )
                                    )
                                }
                                val activity: Activity = requireActivity()
                                if (activity != null) {
                                    val allCategoriesRecyclerView: RecyclerView =
                                        requireActivity().findViewById(R.id.recycler_all_category)

                                   if(allCategoryList.size>0){

                                       allCategoryList.get(0).is_select=true
                                   }
                                    allCategoriesRecyclerView.adapter =
                                        AdapterAllCategories(
                                            allCategoryList,
                                            ChooseCateFragment(),
                                            this@ChooseCateFragment
                                        )
                                }
                                HelpFunctions.dismissProgressBar()
                            } else {
                                HelpFunctions.dismissProgressBar()
                                Toast.makeText(context, "No Categories found", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } else {
                        HelpFunctions.dismissProgressBar()
                        Toast.makeText(context, "No Categories found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }

    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)


    }

    fun hidekeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {

        var position=0

        fun truncateString(str: String): String {
            var res = ""
            for (i in str.indices) {
                if (str[i].toString() == "-") {
                    res = str.removeRange(i, str.length)
                    break
                } else {
                    res = str[i].toString()
                }
            }
            return res
        }
    }


}
