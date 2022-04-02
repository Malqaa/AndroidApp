package com.malka.androidappp.botmnav_fragments.browse_market

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.StaticGetSubcategoryByBrowseCateClick
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.SubcategoriesDialogFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.categorylistings.CategoryResponse
import com.malka.androidappp.servicemodels.categorylistings.SearchRequestModel
import com.malka.androidappp.servicemodels.categorylistings.SearchRespone
import kotlinx.android.synthetic.main.fragment_browse_market.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BrowseMarketFragment : BaseActivity() {

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
        StaticGetSubcategoryByBrowseCateClick.getcategory = CategoryDesc;

        back_button.setOnClickListener {
            onBackPressed()

        }
        search_toolbar.setOnClickListener {
            openDialog()

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
            FetchCategories(CategoryDesc,"")
            btn1.setOnClickListener { openDialog() }
            btn2.setOnClickListener { openDialog() }
            btn3.setOnClickListener { openDialog() }
        } else if (SearchQuery.trim().length > 0) {
            SetToolbarTitle("Search: " + SearchQuery)
            FetchCategories("",SearchQuery)
            if (HelpFunctions.IsUserLoggedIn()) {
                addSearchQueryFav(SearchQuery)
            }
        }


    }



    var marketpost: ArrayList<AdDetailModel> = ArrayList()


    //Zack
    //Date: 10/29/2020
    fun FetchCategories(category: String,query:String) {
        HelpFunctions.startProgressBar(this)


        val requestbody =
            SearchRequestModel(category, "", "", "", "", "", query, "",query)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<SearchRespone> = malqa.categorylist(requestbody)
        call.enqueue(object : Callback<SearchRespone> {
            override fun onFailure(call: Call<SearchRespone>, t: Throwable) {
                HelpFunctions.ShowAlert(
                    this@BrowseMarketFragment,
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
                                this@BrowseMarketFragment,
                                getString(R.string.Information),
                                getString(R.string.NoRecordFound)
                            )
                        }
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@BrowseMarketFragment,
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
                    this@BrowseMarketFragment,
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
                                this@BrowseMarketFragment,
                                getString(R.string.Information),
                                getString(R.string.NoRecordFound)
                            )
                        }
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@BrowseMarketFragment,
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
                    t.message?.let { HelpFunctions.ShowLongToast(it, this@BrowseMarketFragment) }
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)

        }
    }
}