package com.malka.androidappp.botmnav_fragments.browse_market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.StaticGetSubcategoryByBrowseCateClick
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.SubcategoriesDialogFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.BrowseMarketXLAdap
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.categorylistings.CategoryResponse
import com.malka.androidappp.servicemodels.categorylistings.PropertyModel
import com.malka.androidappp.servicemodels.categorylistings.SearchRequestModel
import com.malka.androidappp.servicemodels.categorylistings.SearchRespone
import kotlinx.android.synthetic.main.fragment_browse_market.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BrowseMarketFragment : Fragment() {

    //Zack
    //Date: 10/29/2020
    var CategoryDesc: String = "";
    var SearchQuery: String = "";
    var browadptxl: BrowseMarketXLAdap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        CategoryDesc = arguments?.getString("CategoryDesc").toString()
        SearchQuery = arguments?.getString("SearchQuery").toString()
        StaticGetSubcategoryByBrowseCateClick.getcategory = CategoryDesc;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button.setOnClickListener {
            requireActivity().onBackPressed()

        }
        search_toolbar.setOnClickListener {
            openDialog()

        }
//        if (HelpFunctions.IsUserLoggedIn()) {
//            addSearchQueryFav(SearchQuery)
//        } else {
//            val intentt = Intent(context, SignInActivity::class.java)
//            startActivity(intentt)
//        }

        browadptxl = BrowseMarketXLAdap(marketpost, requireContext())
        recyclerViewmarket.adapter = browadptxl


        icon_list.setOnClickListener {
            browadptxl!!.updateLayout(false)

            icon_list.setImageResource(R.drawable.ic_icon_list_active)
            icon_grid.setImageResource(R.drawable.icon_grid)
            recyclerViewmarket.layoutManager =
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)

        }
        icon_grid.setOnClickListener {
            browadptxl!!.updateLayout(true)
            icon_grid.setImageResource(R.drawable.ic_icon_grid_active)
            icon_list.setImageResource(R.drawable.icon_list)
            recyclerViewmarket.layoutManager = GridLayoutManager(context, 2)

        }
        icon_grid.performClick()

        if (CategoryDesc != null && CategoryDesc.trim().length > 0) {
            SetToolbarTitle(CategoryDesc);
            FetchCategories(CategoryDesc);
            btn1.setOnClickListener() { openDialog() }
            btn2.setOnClickListener() { openDialog() }
            btn3.setOnClickListener() { openDialog() }
        } else if (SearchQuery != null && SearchQuery.trim().length > 0) {
            SetToolbarTitle("Search: " + SearchQuery)
            SearchCategories(SearchQuery)
        }
    }

    var marketpost: ArrayList<AdDetailModel> = ArrayList()


    //Zack
    //Date: 10/29/2020
    fun FetchCategories(category: String) {
        HelpFunctions.startProgressBar(this.requireActivity())


        val requestbody: SearchRequestModel =
            SearchRequestModel(category, "", "", "", "", "", "", "")

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
        val call: Call<SearchRespone> = malqa.categorylist(requestbody)
        call.enqueue(object : Callback<SearchRespone> {
            override fun onFailure(call: Call<SearchRespone>, t: Throwable) {
                HelpFunctions.ShowAlert(
                    this@BrowseMarketFragment.context,
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
                                this@BrowseMarketFragment.context,
                                getString(R.string.Information),
                                getString(R.string.NoRecordFound)
                            )
                        }
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@BrowseMarketFragment.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }

    fun SearchCategories(searchquery: String) {
        HelpFunctions.startProgressBar(this.requireActivity())


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
        val call: Call<CategoryResponse> = malqa.searchcategorylist(searchquery)
        call.enqueue(object : Callback<CategoryResponse> {
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                HelpFunctions.ShowAlert(
                    this@BrowseMarketFragment.context,
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
                        var resp: CategoryResponse = response.body()!!;
                        var lists: List<PropertyModel> = resp.data;
//                            if (lists != null && lists.count() > 0) {
//                                for (IndProperty in lists) {
//                                    marketpost.add(
//                                        BrowseMarketModel(
//                                            if (IndProperty.title != null) IndProperty.title else "",
//                                            if (IndProperty.description != null) IndProperty.description else "",
//                                            "Reserve not met",
//                                            if (IndProperty.price != null) IndProperty.price else "0",
//                                            if (IndProperty.price != null) IndProperty.price else "0",
//                                            "Buy Now",
//                                            if (IndProperty.homepageImage != null) IndProperty.homepageImage else "",
//                                            "",
//                                            advid = IndProperty.referenceId,
//                                            ItemInWatchlist = HelpFunctions.AdAlreadyAddedToWatchList(
//                                                IndProperty.referenceId
//                                            )
//                                        )
//                                    )
//                                }
//                                ShowGridLayout()
//
//                            } else {
//                                HelpFunctions.ShowAlert(
//                                    this@BrowseMarketFragment.context,
//                                    getString(R.string.Information),
//                                    getString(R.string.NoRecordFound)
//                                )
//                            }
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@BrowseMarketFragment.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }

    //Zack
    //Date: 10/29/2020
    fun SetToolbarTitle(Category: String) {
        if (Category == "LSOWWFASAR")
            lbl_toolbar_category.text = "Property";
        else
            lbl_toolbar_category.text = Category;
    }

    fun openDialog() {
        val exampleDialog = SubcategoriesDialogFragment()
        exampleDialog.show(childFragmentManager, "example dialog")
    }

    // Add Search to favorites
    fun addSearchQueryFav(searchQuery: String) {
        try {

            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()

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
                    if (response.isSuccessful) {

                        HelpFunctions.ShowLongToast(
                            searchQuery + " " + getString(R.string.AddedtoFavorites),
                            activity
                        )
//                        Toast.makeText(
//                            activity,
//                            "$searchQuery Added to Favorites",
//                            Toast.LENGTH_LONG
//                        )
//                            .show()

                    } else {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Failedtoaddfavorites),
                            activity
                        )
//                        Toast.makeText(activity, "", Toast.LENGTH_LONG)
//                            .show()
                    }
                }

                override fun onFailure(call: Call<ModelAddSearchFav>, t: Throwable) {
                    t.message?.let { HelpFunctions.ShowLongToast(it, activity) }
//                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)

        }
    }
}