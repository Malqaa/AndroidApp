package com.malka.androidappp.botmnav_fragments.browse_market

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.StaticGetSubcategoryByBrowseCateClick
import com.malka.androidappp.botmnav_fragments.browse_market.popup_subcategories_list.SubcategoriesDialogFragment
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.BrowseMarketXLAdap
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
        toolbar_browsemarket.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_browsemarket.setTitleTextColor(Color.BLACK)
        toolbar_browsemarket.title = getString(R.string.Browse)
        toolbar_browsemarket.inflateMenu(R.menu.browse_category_menu)
        toolbar_browsemarket.navigationIcon?.isAutoMirrored = true
        //toolbar_browsemarket.setOnMenuItemClickListener() {}
        toolbar_browsemarket.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }
        toolbar_browsemarket.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.heart_favitem) {
                    // do something
                    if (HelpFunctions.IsUserLoggedIn()) {
                        addSearchQueryFav(SearchQuery)
                    } else {
                        val intentt = Intent(context, SignInActivity::class.java)
                        startActivity(intentt)
                    }
//                    HelpFunctions.InsertToFavourite(
//                        "",
//                        CategoryDesc,
//                        "",
//                        this@BrowseMarketFragment
//                    )
                } else if (item.itemId == R.id.search_item) {
                    openDialog()
                } else {
                    // do something
                }
                return false
            }
        })

        //Zack
        //Date: 10/29/2020
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
//        gridbtn.setOnClickListener(View.OnClickListener {
//            ShowGridLayout()
//        })
    }

    var count = 1
    var marketpost: List<SearchRespone.Data> = ArrayList()
    fun ShowGridLayout() {
        try {

            if (count == 0) {
                recyclerViewmarket.layoutManager =
                    LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
              //  gridbtn.setBackgroundResource(R.drawable.icon_list)
                count++
            } else {
                recyclerViewmarket.layoutManager = GridLayoutManager(context, 2)
               // gridbtn.setBackgroundResource(R.drawable.icon_grid)
                count--
            }
            val browadptxl = BrowseMarketXLAdap(marketpost,requireContext() )
            recyclerViewmarket.adapter = browadptxl
            browadptxl.onItemClick = { indobj ->
//                    HelpFunctions.ViewAdvertismentDetail(
//                        indobj.advid,
//                        CategoryDesc,
//                        this@BrowseMarketFragment
//                    )
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    //Zack
    //Date: 10/29/2020
    fun FetchCategories(category: String) {
        try {
            val requestbody: SearchRequestModel =
                SearchRequestModel(category, "", "", "", "", "", "", "")

            val malqa: MalqaApiService = RetrofitBuilder.getcategory()
            val call: Call<SearchRespone> = malqa.categorylist(requestbody)
            call.enqueue(object : Callback<SearchRespone> {
                override fun onFailure(call: Call<SearchRespone>, t: Throwable) {
                    HelpFunctions.ShowAlert(
                        this@BrowseMarketFragment.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }

                override fun onResponse(
                    call: Call<SearchRespone>,
                    response: Response<SearchRespone>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var resp: SearchRespone = response.body()!!;
                            marketpost = resp.data;
                            if (marketpost != null && marketpost.count() > 0) {
//                                for (IndProperty in lists) {
//                                    IndProperty._source.run {
//                                        marketpost.add(
//                                            BrowseMarketModel(
//                                                if (title != null) IndPropert else "",
//                                                if (IndProperty.description != null) IndProperty.description else "",
//                                                "Reserve not met",
//                                                if (IndProperty.price != null) IndProperty.price else "0",
//                                                if (IndProperty.price != null) IndProperty.price else "0",
//                                                "Buy Now",
//                                                if (IndProperty.homepageImage != null) IndProperty.homepageImage else "",
//                                                "",
//                                                advid = IndProperty.id,
//                                                ItemInWatchlist = HelpFunctions.AdAlreadyAddedToWatchList(
//                                                    IndProperty.id
//                                                )
//                                            )
//                                        )
//                                    }
//
//                                }
                                ShowGridLayout()

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
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun SearchCategories(searchquery: String) {
        try {
            val malqa: MalqaApiService = RetrofitBuilder.searchcategorylist()
            val call: Call<CategoryResponse> = malqa.searchcategorylist(searchquery)
            call.enqueue(object : Callback<CategoryResponse> {
                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    HelpFunctions.ShowAlert(
                        this@BrowseMarketFragment.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
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
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
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

            val malqaa: MalqaApiService = RetrofitBuilder.addSearchToFav()

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
                            searchQuery +" "+getString(R.string.AddedtoFavorites),
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