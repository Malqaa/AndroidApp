package com.malka.androidappp.botmnav_fragments.browse_market

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.StaticGetSubcategoryByBrowseCateClick
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.SubcategoriesDialogFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.Selection
import com.malka.androidappp.servicemodels.categorylistings.CategoryResponse
import com.malka.androidappp.servicemodels.categorylistings.SearchRequestModel
import com.malka.androidappp.servicemodels.categorylistings.SearchRespone
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.specification_design.view.*
import kotlinx.android.synthetic.main.specification_sub_design.view.*
import kotlinx.android.synthetic.main.sub_category_design.view.*
import kotlinx.android.synthetic.main.sub_category_layout.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchCategoryActivity : BaseActivity() {

    //Zack
    //Date: 10/29/2020
    var CategoryDesc: String = "";
    var SearchQuery: String = "";
    var browadptxl: GenericProductAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_browse_market)

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
            val builder = AlertDialog.Builder(this@SearchCategoryActivity)
                .create()
            val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
            builder.setView(view)
            bottom_bar.hide()
            view.filter_bar.visibility = View.GONE
            view.price_tv.visibility = View.GONE
            fun subCategoryAdaptor(list: List<Selection>) {
                view.sub_category_rcv.adapter = object : GenericListAdapter<Selection>(
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

            subCategoryAdaptor((shippingOption))

        }

        region.setOnClickListener {
            val builder = AlertDialog.Builder(this@SearchCategoryActivity)
                .create()
            val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
            builder.setView(view)
            bottom_bar.hide()
            view.filter_bar.visibility = View.GONE
            view.price_tv.visibility = View.GONE

            fun regionAdaptor(list: List<Selection>) {
                view.region_rcv.adapter = object : GenericListAdapter<Selection>(
                    R.layout.sub_category_design,
                    bind = { element, holder, itemCount, position ->
                        holder.view.run {
                            element.run {
                                category_tv.text = name
                                imageView_arrow.setImageResource(R.drawable.down_arrow)

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

            regionAdaptor((shippingOption))
        }

        specification.setOnClickListener {
            val builder = AlertDialog.Builder(this@SearchCategoryActivity)
                .create()
            val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
            builder.setView(view)
            bottom_bar.hide()

            fun specificationAdaptor(list: List<Selection>) {
                view.specification_rcv.adapter = object : GenericListAdapter<Selection>(
                    R.layout.specification_design,
                    bind = { element, holder, itemCount, position ->
                        holder.view.run {
                            element.run {
                                header_title.text = name
                                sub_item_rcv.adapter = object : GenericListAdapter<Selection>(
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
                                        list
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
            specificationAdaptor((shippingOption))

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
            FetchCategories(CategoryDesc, "")
            btn1.setOnClickListener { openDialog() }
            btn2.setOnClickListener { openDialog() }
            btn3.setOnClickListener { openDialog() }
        } else if (SearchQuery.trim().length > 0) {
            SetToolbarTitle("Search: " + SearchQuery)
            FetchCategories("", SearchQuery)
            if (HelpFunctions.IsUserLoggedIn()) {
                addSearchQueryFav(SearchQuery)
            }
        }


    }


    var marketpost: ArrayList<AdDetailModel> = ArrayList()


    //Zack
    //Date: 10/29/2020
    fun FetchCategories(category: String, query: String) {
        HelpFunctions.startProgressBar(this)


        val requestbody =
            SearchRequestModel(category, "", "", "", "", "", query, "", query)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<SearchRespone> = malqa.categorylist(requestbody)
        call.enqueue(object : Callback<SearchRespone> {
            override fun onFailure(call: Call<SearchRespone>, t: Throwable) {
                HelpFunctions.ShowAlert(
                    this@SearchCategoryActivity,
                    getString(R.string.Information),
                    getString(R.string.NoRecordFound)
                )
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<SearchRespone>,
                response: Response<SearchRespone>
            ) {

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: SearchRespone = response.body()!!
                        resp.data.forEach {
                            marketpost.add(it._source)
                        }
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
                } else {
                    HelpFunctions.ShowAlert(
                        this@SearchCategoryActivity,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }

    fun SearchCategories(searchquery: String) {
        HelpFunctions.startProgressBar(this)


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<CategoryResponse> = malqa.searchcategorylist(searchquery)
        call.enqueue(object : Callback<CategoryResponse> {
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                HelpFunctions.ShowAlert(
                    this@SearchCategoryActivity,
                    getString(R.string.Information),
                    getString(R.string.NoRecordFound)
                )
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<CategoryResponse>, response: Response<CategoryResponse>
            ) {

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: CategoryResponse = response.body()!!
                        resp.data.forEach {
                            marketpost.add(it)
                        }
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
                } else {
                    HelpFunctions.ShowAlert(
                        this@SearchCategoryActivity,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }

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
}