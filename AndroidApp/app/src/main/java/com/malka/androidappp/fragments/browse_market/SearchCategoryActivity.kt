package com.malka.androidappp.fragments.browse_market

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.fragments.browse_market.popup_subcategories_list.StaticGetSubcategoryByBrowseCateClick
import com.malka.androidappp.fragments.browse_market.popup_subcategories_list.SubcategoriesDialogFragment
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.*
import com.malka.androidappp.servicemodels.model.Category
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.region_item.view.*
import kotlinx.android.synthetic.main.specification_design.view.*
import kotlinx.android.synthetic.main.specification_sub_design.view.*
import kotlinx.android.synthetic.main.sub_category_design.view.*
import kotlinx.android.synthetic.main.sub_category_layout.view.*
import kotlinx.android.synthetic.main.sub_city_item.view.*
import kotlinx.android.synthetic.main.sub_region_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchCategoryActivity : BaseActivity() {

   
    //Date: 10/29/2020
    var CategoryDesc: String = "";
    var SearchQuery: String = "";
    var browadptxl: GenericProductAdapter? = null
    var CategoryID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_browse_market)
        CategoryID = intent?.getStringExtra("CategoryID") ?: ""
        CategoryDesc = intent?.getStringExtra("CategoryDesc").toString()
        SearchQuery = intent?.getStringExtra("SearchQuery").toString()
        intent?.getBooleanExtra("isMapShow", false)?.let {
            if (it) {
                map_button.show()
            }
        }
        StaticGetSubcategoryByBrowseCateClick.getcategory = CategoryDesc;

        back_button.setOnClickListener {
            onBackPressed()

        }
        search_toolbar.setOnClickListener {
            openDialog()

        }

        val shippingOption: ArrayList<Selection> = ArrayList()
        shippingOption.apply {
            add(Selection("option 1"))
            add(Selection("option 2"))
            add(Selection("option 3"))
            add(Selection("option 4"))
            add(Selection("option 5"))
        }

        sub_catgeory.setOnClickListener {
            GetSubCategoryByMainCategory(CategoryID)
        }

        region.setOnClickListener {


            val builder = AlertDialog.Builder(this@SearchCategoryActivity)
                .create()
            val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
            builder.setView(view)
            bottom_bar.hide()
            view.filter_bar.visibility = View.GONE
            view.price_tv.visibility = View.GONE

            fun regionAdaptor(list: List<Country>) {
                view.region_rcv.adapter = object : GenericListAdapter<Country>(
                    R.layout.region_item,
                    bind = { element, holder, itemCount, position ->
                        holder.view.run {
                            element.run {
                                region_tv.text = name
                                setOnClickListener {
                                    CommonAPI().getRegion(id, this@SearchCategoryActivity) {regions->


                                        sub_region_rcv.adapter = object : GenericListAdapter<Country>(
                                            R.layout.sub_region_item,
                                            bind = { element, holder, itemCount, position ->
                                                holder.view.run {
                                                    element.run {
                                                        sub_region_tv.text = name
                                                        setOnClickListener {
                                                            CommonAPI().getCity(id, this@SearchCategoryActivity) {city->


                                                                sub_city_rcv.adapter = object : GenericListAdapter<Country>(
                                                                    R.layout.sub_city_item,
                                                                    bind = { element, holder, itemCount, position ->
                                                                        holder.view.run {
                                                                            element.run {
                                                                                sub_city_tv.text = name


                                                                            }
                                                                        }
                                                                    }
                                                                ) {
                                                                    override fun getFilter(): Filter {
                                                                        TODO("Not yet implemented")
                                                                    }

                                                                }.apply {
                                                                    submitList(
                                                                        city
                                                                    )
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        ) {
                                            override fun getFilter(): Filter {
                                                TODO("Not yet implemented")
                                            }

                                        }.apply {
                                            submitList(
                                                regions
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                ) {
                    override fun getFilter(): Filter {
                        TODO("Not yet implemented")
                    }

                }.apply {
                    submitList(
                        list
                    )
                }
            }

            builder.setCanceledOnTouchOutside(true)
            builder.show()
            builder.setOnCancelListener {
                bottom_bar.show()
            }

            view.sub_category.setOnClickListener {
                builder.dismiss()
                sub_catgeory.performClick()
            }

            view.specification_t.setOnClickListener {
                builder.dismiss()
                specification.performClick()
            }
            view.region.setOnClickListener {
                builder.dismiss()
                region.performClick()
            }

            regionAdaptor(ConstantObjects.countryList)
        }

        specification.setOnClickListener {

            getSpecification(CategoryID)


        }



        browadptxl = GenericProductAdapter(marketpost, this)
        recyclerViewmarket.adapter = browadptxl


        icon_list.setOnClickListener {
            browadptxl!!.updateLayout(false)

            icon_list.setImageResource(R.drawable.ic_icon_list_active)
            icon_grid.setImageResource(R.drawable.icon_grid)
            recyclerViewmarket.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        }
        icon_grid.setOnClickListener {
            browadptxl!!.updateLayout(true)
            icon_grid.setImageResource(R.drawable.ic_icon_grid_active)
            icon_list.setImageResource(R.drawable.icon_list)
            recyclerViewmarket.layoutManager = GridLayoutManager(this, 2)

        }
        icon_grid.performClick()

        if (CategoryDesc.trim().length > 0) {
            SetToolbarTitle(CategoryDesc)
            AdvanceFiltter(mapOf("mainCatId" to CategoryID))
            btn1.setOnClickListener { openDialog() }
            btn2.setOnClickListener { openDialog() }
            btn3.setOnClickListener { openDialog() }
        } else if (SearchQuery.trim().length > 0) {
            SetToolbarTitle("Search: " + SearchQuery)
            AdvanceFiltter(mapOf("productName" to SearchQuery))
            if (HelpFunctions.IsUserLoggedIn()) {
                addSearchQueryFav(SearchQuery)
            }
        }

        follow_category.setOnClickListener {
            FollowCategoryAPI()
        }
    }

    private fun getSpecification(categoryID: String) {
        HelpFunctions.startProgressBar(this)
        val malqaa: MalqaApiService =
            RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<GeneralResponse> =
            malqaa.getSpecification(categoryID)

        call.enqueue(object : Callback<GeneralResponse> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                HelpFunctions.dismissProgressBar()

                if (response.isSuccessful) {

                    response.body()?.run {

                        val list: ArrayList<CategorySpecification> = Gson().fromJson(
                            Gson().toJson(data),
                            object : TypeToken<ArrayList<CategorySpecification>>() {}.type
                        )
                        showSpecification(list)
                    }
                }


            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun showSpecification(specificationList: ArrayList<CategorySpecification>) {
        val builder = AlertDialog.Builder(this@SearchCategoryActivity)
            .create()
        val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
        builder.setView(view)
        bottom_bar.hide()

        fun specificationAdaptor(list: List<CategorySpecification>) {
            view.specification_rcv.adapter = object : GenericListAdapter<CategorySpecification>(
                R.layout.specification_design,
                bind = { element, holder, itemCount, position ->
                    holder.view.run {
                        element.run {
                            header_title.text = name
                            sub_item_rcv.adapter = object : GenericListAdapter<SubSpecification>(
                                R.layout.specification_sub_design,
                                bind = { element, holder, itemCount, position ->
                                    holder.view.run {
                                        element.run {
                                            specification_tv.text = name
                                        }
                                    }
                                }
                            ) {
                                override fun getFilter(): Filter {
                                    TODO("Not yet implemented")
                                }

                            }.apply {
                                submitList(
                                    subSpecifications
                                )
                            }
                        }
                    }
                }
            ) {
                override fun getFilter(): Filter {
                    TODO("Not yet implemented")
                }

            }.apply {
                submitList(
                    list
                )
            }
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
        builder.setOnCancelListener {
            bottom_bar.show()
        }

        view.region.setOnClickListener {
            builder.dismiss()
            region.performClick()
        }

        view.sub_category.setOnClickListener {
            builder.dismiss()
            sub_catgeory.performClick()
        }
        view.specification_t.setOnClickListener {
            builder.dismiss()
            specification.performClick()
        }
        specificationAdaptor(specificationList)
    }

    private fun FollowCategoryAPI() {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

        val call = malqa.AddFollow(CategoryID)

        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {

                        HelpFunctions.ShowLongToast(getString(R.string.follow_catgeory), this@SearchCategoryActivity)


                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })
    }


    var marketpost: ArrayList<Product> = ArrayList()


    fun AdvanceFiltter(filter: Map<String, String>) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<GeneralResponse> = malqa.AdvanceFiltter(filter)
        call.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                HelpFunctions.ShowAlert(
                    this@SearchCategoryActivity,
                    getString(R.string.Information),
                    getString(R.string.NoRecordFound)
                )
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {

                if (response.isSuccessful) {

                    response.body()?.run {

                        marketpost = Gson().fromJson(
                            Gson().toJson(data),
                            object : TypeToken<ArrayList<Product>>() {}.type
                        )
                        if (marketpost.count() > 0) {
                            browadptxl!!.updateData(marketpost)
                            total_result_tv.text =
                                getString(R.string.result, marketpost.count().toString())
                        } else {
                            HelpFunctions.ShowAlert(
                                this@SearchCategoryActivity,
                                getString(R.string.Information),
                                getString(R.string.NoRecordFound)
                            )
                        }

                    }
                }


                HelpFunctions.dismissProgressBar()

            }
        })

    }

//    fun SearchCategories(searchquery: String) {
//        HelpFunctions.startProgressBar(this)
//
//
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val call: Call<CategoryResponse> = malqa.searchcategorylist(searchquery)
//        call.enqueue(object : Callback<CategoryResponse> {
//            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
//                HelpFunctions.ShowAlert(
//                    this@SearchCategoryActivity,
//                    getString(R.string.Information),
//                    getString(R.string.NoRecordFound)
//                )
//                HelpFunctions.dismissProgressBar()
//
//            }
//
//            override fun onResponse(
//                call: Call<CategoryResponse>, response: Response<CategoryResponse>
//            ) {
//
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        val resp: CategoryResponse = response.body()!!
//                        resp.data.forEach {
//                            marketpost.add(it)
//                        }
//                        if (marketpost.count() > 0) {
//                            browadptxl!!.updateData(marketpost)
//                            total_result_tv.text =
//                                getString(R.string.result, marketpost.count().toString())
//                        } else {
//                            HelpFunctions.ShowAlert(
//                                this@SearchCategoryActivity,
//                                getString(R.string.Information),
//                                getString(R.string.NoRecordFound)
//                            )
//                        }
//                    }
//                } else {
//                    HelpFunctions.ShowAlert(
//                        this@SearchCategoryActivity,
//                        getString(R.string.Information),
//                        getString(R.string.NoRecordFound)
//                    )
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//        })
//
//    }

    fun SetToolbarTitle(Category: String) {
        lbl_toolbar_category.text = Category;

    }

    fun openDialog() {
        val exampleDialog = SubcategoriesDialogFragment()
        exampleDialog.show(getSupportFragmentManager(), "example dialog")
    }

    // Add Search to favorites
    fun addSearchQueryFav(searchQuery: String) {
        try {

            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

            val call: Call<ModelAddSearchFav> = malqaa.addSearchFav(
                ModelAddSearchFav(
                    loggedInUserId = ConstantObjects.logged_userid,
                    searchQuery = searchQuery
                )
            )

            call.enqueue(object : Callback<ModelAddSearchFav> {
                override fun onResponse(
                    call: Call<ModelAddSearchFav>, response: Response<ModelAddSearchFav>
                ) {

                }

                override fun onFailure(call: Call<ModelAddSearchFav>, t: Throwable) {
                    t.message?.let { HelpFunctions.ShowLongToast(it, this@SearchCategoryActivity) }
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)

        }
    }

    private fun GetSubCategoryByMainCategory(categoryKey: String) {
        HelpFunctions.startProgressBar(this)
        val malqaa: MalqaApiService =
            RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<GeneralResponse> =
            malqaa.GetSubCategoryByMainCategory(categoryKey)

        call.enqueue(object : Callback<GeneralResponse> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                HelpFunctions.dismissProgressBar()

                if (response.isSuccessful) {

                    response.body()?.run {

                        val list: ArrayList<Category> = Gson().fromJson(
                            Gson().toJson(data),
                            object : TypeToken<ArrayList<Category>>() {}.type
                        )
                        showFilterDialog(list)
                    }
                }


            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }
        })
    }


    private fun showFilterDialog(list: ArrayList<Category>) {
        val builder = AlertDialog.Builder(this@SearchCategoryActivity)
            .create()
        val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
        builder.setView(view)
        bottom_bar.hide()
        view.filter_bar.visibility = View.GONE
        view.price_tv.visibility = View.GONE
        fun subCategoryAdaptor(list: List<Category>) {
            view.sub_category_rcv.adapter = object : GenericListAdapter<Category>(
                R.layout.sub_category_design,
                bind = { element, holder, itemCount, position ->
                    holder.view.run {
                        element.run {
                            category_tv.text = name
                        }
                    }
                }
            ) {
                override fun getFilter(): Filter {
                    TODO("Not yet implemented")
                }

            }.apply {
                submitList(
                    list
                )
            }
        }
        builder.setCanceledOnTouchOutside(true)
        builder.show()
        builder.setOnCancelListener {
            bottom_bar.show()
        }
        view.region.setOnClickListener {
            builder.dismiss()
            region.performClick()
        }

        view.specification_t.setOnClickListener {
            builder.dismiss()
            specification.performClick()
        }
        view.sub_category.setOnClickListener {
            builder.dismiss()
            sub_catgeory.performClick()
        }

        subCategoryAdaptor(list)
    }
}