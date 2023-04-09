package com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.EndlessRecyclerViewScrollListener
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.popup_subcategories_list.ModelAddSearchFav
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.popup_subcategories_list.SubcategoriesDialogFragment
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.CategorySpecification
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.SubSpecification
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.CategoryProductViewModel
import com.malka.androidappp.newPhase.presentation.searchProductListActivity.browse_market.filterDialog.FilterCategoryProductsDialog
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.fragment_browse_market.progressBar
import kotlinx.android.synthetic.main.fragment_browse_market.progressBarMore
import kotlinx.android.synthetic.main.fragment_browse_market.swipe_to_refresh
import kotlinx.android.synthetic.main.fragment_browse_market.tvError
import kotlinx.android.synthetic.main.fragment_sold_business.*
import kotlinx.android.synthetic.main.item_filter_specification.*
import kotlinx.android.synthetic.main.item_filter_specification_sub_item.*
import kotlinx.android.synthetic.main.item_filtter_sub_category_design.*
import kotlinx.android.synthetic.main.sub_category_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchCategoryActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, FilterCategoryProductsDialog.SetOnClickListeners {


    //Date: 10/29/2020
    var CategoryDesc: String = "";
    var SearchQuery: String = "";
    var browadptxl: GenericProductAdapter? = null
    var categoryID: Int = 0
    lateinit var category: Category
    var countryList: List<Int> = ArrayList()
    var regionList: List<Int> = ArrayList()
    var neighoodList: List<Int> = ArrayList()
    var subCategoryList: List<Int> = ArrayList()
    var specificationList: List<String> = ArrayList()
    var startPrice: Float = 0f
    var endProce: Float = 0f

    //    lateinit var productSearchCategoryAdapter: ProductSearchCategoryAdapter
    lateinit var productSearchCategoryAdapter: ProductHorizontalAdapter

    lateinit var gridViewLayoutManager: GridLayoutManager
    lateinit var linerlayout: LinearLayoutManager
    lateinit var filterCategoryProductsDialog: FilterCategoryProductsDialog
    private lateinit var productsListViewModel: CategoryProductViewModel
    private lateinit var productList: ArrayList<Product>
    private var lastUpdateIndex = -1
    var categoryQuery = "mainCatId="
    var pageCountQuery = "PageRowsCount=10"
    var pageIndexQuery = "pageIndex="
    var langQuery = "lang="
    var queryString = ""

    var added_product_id_to_fav = 0
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    lateinit var endlessRecyclerViewScrollListener2: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_browse_market)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setProductSearchCategoryAdapter()
        setVeiwClickListeners()
        categoryID = intent.getIntExtra("CategoryID", 0)
        filterCategoryProductsDialog = FilterCategoryProductsDialog(
            this,
            FilterCategoryProductsDialog.subCategoryType,
            categoryID,
            this
        )
        setupViewModel()
        queryString =
            "${categoryQuery}${categoryID}&${pageCountQuery}&${langQuery}${ConstantObjects.currentLanguage}&${pageIndexQuery}${1}"
        //println("hhh "+queryString)
//        productsListViewModel.searchForProduct(
//            categoryID,
//            ConstantObjects.currentLanguage,
//            1,
//            countryList,
//            regionList,
//            neighoodList,
//            subCategoryList,
//            specificationList,
//            startPrice,
//            endProce
//        )
        onRefresh()
        /***

        CategoryDesc = intent?.getStringExtra("CategoryDesc").toString()
        SearchQuery = intent?.getStringExtra("SearchQuery").toString()
        intent?.getBooleanExtra("isMapShow", false)?.let {
        if (it) {
        map_button.show()
        }
        }
        StaticGetSubcategoryByBrowseCateClick.getcategory = CategoryDesc;
        GetCategoryById()
         ***/

//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val call = malqa.ListCategoryFollow()
//        call.enqueue(object : Callback<GeneralResponse?> {
//            override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//            }
//
//            override fun onResponse(
//                call: Call<GeneralResponse?>,
//                response: Response<GeneralResponse?>
//            ) {
//                if (response.isSuccessful) {
//
//                    if (response.body() != null) {
//
//
//                    }
//
//                }
//                HelpFunctions.dismissProgressBar()
//            }
//        })

        /*
             fbButtonBack.setOnClickListener {
                 onBackPressed()

             }
             search_toolbar.setOnClickListener {
                 openDialog()

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
                                         CommonAPI().getRegion(
                                             id,
                                             this@SearchCategoryActivity
                                         ) { regions ->
                                             sub_region_rcv.adapter =
                                                 object : GenericListAdapter<Country>(
                                                     R.layout.sub_region_item,
                                                     bind = { element, holder, itemCount, position ->
                                                         holder.view.run {
                                                             element.run {
                                                                 sub_region_tv.text = name
                                                                 setOnClickListener {
                                                                     CommonAPI().getCity(
                                                                         id,
                                                                         this@SearchCategoryActivity
                                                                     ) { city ->


                                                                         sub_city_rcv.adapter = object :
                                                                             GenericListAdapter<Country>(
                                                                                 R.layout.sub_city_item,
                                                                                 bind = { element, holder, itemCount, position ->
                                                                                     holder.view.run {
                                                                                         element.run {
                                                                                             sub_city_tv.text =
                                                                                                 name


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
             if (SearchQuery.trim().length > 0) {
                 SetToolbarTitle("Search: " + SearchQuery)
                 AdvanceFiltter(mapOf("productName" to SearchQuery, "mainCatId" to CategoryID))
                 if (HelpFunctions.isUserLoggedIn()) {
                     addSearchQueryFav(SearchQuery)
                 }
             } else {
                 AdvanceFiltter(mapOf("mainCatId" to CategoryID))
                 SetToolbarTitle(CategoryDesc)
             }
     */
    }

    private fun setupViewModel() {
        productsListViewModel = ViewModelProvider(this).get(CategoryProductViewModel::class.java)
        productsListViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }


        productsListViewModel.isloadingMore.observe(this) {
            if (it)
                progressBarMore.show()
            else
                progressBarMore.hide()
        }
        productsListViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productsListViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productsListViewModel.productListRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (productListResp.productList != null && productListResp.productList.isNotEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    productSearchCategoryAdapter.notifyDataSetChanged()
                } else {
                    if (productList.isEmpty())
                        showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        productsListViewModel.isNetworkFailProductToFav.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        productsListViewModel.errorResponseObserverProductToFav.observe(this) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        productsListViewModel.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
//                if (lastUpdateIndex < productList.size) {
//                    productList[lastUpdateIndex].isFavourite =
//                        !productList[lastUpdateIndex].isFavourite
//                    productSearchCategoryAdapter.notifyItemChanged(lastUpdateIndex)
//                    productSearchCategoryAdapter.notifyDataSetChanged()
//                }
                lifecycleScope.launch(Dispatchers.IO) {
                    var selectedSimilerProduct: Product? = null
                    for (product in productList) {
                        if (product.id == added_product_id_to_fav) {
                            product.isFavourite = !product.isFavourite
                            selectedSimilerProduct = product
                            break
                        }
                    }
                    withContext(Dispatchers.Main) {
                        /**update similer product*/
                        /**update similer product*/
                        selectedSimilerProduct?.let { product ->
                            productSearchCategoryAdapter.notifyItemChanged(
                                productList.indexOf(
                                    product
                                )
                            )
                        }
                    }
                }
            }
        }

    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    private fun setVeiwClickListeners() {
        fbButtonBack.setOnClickListener {
            onBackPressed()
        }
        icon_grid.setOnClickListener {
            //browadptxl!!.updateLayout(true)
            icon_grid.setImageResource(R.drawable.ic_icon_grid_active)
            icon_list.setImageResource(R.drawable.icon_list)
            recyclerViewMarket.layoutManager = gridViewLayoutManager
            productSearchCategoryAdapter.notifyDataSetChanged()
        }
        icon_list.setOnClickListener {
            //browadptxl!!.updateLayout(false)
            icon_list.setImageResource(R.drawable.ic_icon_list_active)
            icon_grid.setImageResource(R.drawable.icon_grid)
            recyclerViewMarket.layoutManager = linerlayout
            productSearchCategoryAdapter.notifyDataSetChanged()
        }
        btnSubCatgeoryFilter.setOnClickListener {
            //GetSubCategoryByMainCategory(CategoryID)

            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.subCategoryType)
        }
        btnRegion.setOnClickListener {
            //GetSubCategoryByMainCategory(CategoryID)
            // var filterCategoryProductsDialog = FilterCategoryProductsDialog(this,FilterCategoryProductsDialog.regionType,categoryID)
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.regionType)
        }
        btnSpecification.setOnClickListener {
            //GetSubCategoryByMainCategory(CategoryID)
            // var filterCategoryProductsDialog = FilterCategoryProductsDialog(this,FilterCategoryProductsDialog.specificationType,categoryID)
            filterCategoryProductsDialog.show()
            filterCategoryProductsDialog.setSelectedTap(FilterCategoryProductsDialog.specificationType)
        }
//        btnRegion.setOnClickListener {
//
//            fun regionAdaptor(list: List<Country>) {
//                region_rcv.adapter = object : GenericListAdapter<Country>(
//                    R.layout.region_item,
//                    bind = { element, holder, itemCount, position ->
//                        holder.view.run {
//                            element.run {
//                                region_tv.text = name
//                                setOnClickListener {
//                                    CommonAPI().getRegion(
//                                        id,
//                                        this@SearchCategoryActivity
//                                    ) { regions ->
//                                        sub_region_rcv.adapter =
//                                            object : GenericListAdapter<Country>(
//                                                R.layout.sub_region_item,
//                                                bind = { element, holder, itemCount, position ->
//                                                    holder.view.run {
//                                                        element.run {
//                                                            sub_region_tv.text = name
//                                                            setOnClickListener {
//                                                                CommonAPI().getCity(
//                                                                    id,
//                                                                    this@SearchCategoryActivity
//                                                                ) { city ->
//
//
//                                                                    sub_city_rcv.adapter = object :
//                                                                        GenericListAdapter<Country>(
//                                                                            R.layout.sub_city_item,
//                                                                            bind = { element, holder, itemCount, position ->
//                                                                                holder.view.run {
//                                                                                    element.run {
//                                                                                        sub_city_tv.text =
//                                                                                            name
//
//
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                        ) {
//                                                                        override fun getFilter(): Filter {
//                                                                            TODO("Not yet implemented")
//                                                                        }
//
//                                                                    }.apply {
//                                                                        submitList(
//                                                                            city
//                                                                        )
//                                                                    }
//                                                                }
//                                                            }
//
//                                                        }
//                                                    }
//                                                }
//                                            ) {
//                                                override fun getFilter(): Filter {
//                                                    TODO("Not yet implemented")
//                                                }
//
//                                            }.apply {
//                                                submitList(
//                                                    regions
//                                                )
//                                            }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                ) {
//                    override fun getFilter(): Filter {
//                        TODO("Not yet implemented")
//                    }
//
//                }.apply {
//                    submitList(
//                        list
//                    )
//                }
//            }
//
//            builder.setCanceledOnTouchOutside(true)
//            builder.show()
//            builder.setOnCancelListener {
//                bottom_bar.show()
//            }
//
//            view.sub_category.setOnClickListener {
//                builder.dismiss()
//                sub_catgeory.performClick()
//            }
//
//            view.specification_t.setOnClickListener {
//                builder.dismiss()
//                specification.performClick()
//            }
//            view.region.setOnClickListener {
//                builder.dismiss()
//                region.performClick()
//            }
//
//            regionAdaptor(ConstantObjects.countryList)
//        }
    }

    private fun setProductSearchCategoryAdapter() {
        productList = ArrayList()
        icon_list.setImageResource(R.drawable.ic_icon_list_active)
        icon_grid.setImageResource(R.drawable.icon_grid)
        gridViewLayoutManager = GridLayoutManager(this, 2)
        linerlayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productSearchCategoryAdapter = ProductHorizontalAdapter(productList, this, 0, false, false)
        recyclerViewMarket.apply {
            adapter = productSearchCategoryAdapter
            layoutManager = linerlayout
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linerlayout) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    println("hhh page liner  " + page)
                    productsListViewModel.searchForProduct(
                        categoryID,
                        ConstantObjects.currentLanguage,
                        page,
                        countryList,
                        regionList,
                        neighoodList,
                        subCategoryList,
                        specificationList,
                        startPrice,
                        endProce
                    )

                }
            }
        endlessRecyclerViewScrollListener2 =
            object : EndlessRecyclerViewScrollListener(gridViewLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    println("hhh page gride " + page)
                    productsListViewModel.searchForProduct(
                        categoryID,
                        ConstantObjects.currentLanguage,
                        page,
                        countryList,
                        regionList,
                        neighoodList,
                        subCategoryList,
                        specificationList,
                        startPrice,
                        endProce
                    )

                }
            }
        recyclerViewMarket.addOnScrollListener(endlessRecyclerViewScrollListener)
        recyclerViewMarket.addOnScrollListener(endlessRecyclerViewScrollListener2)
    }

    override fun onRefresh() {
        endlessRecyclerViewScrollListener.resetState()

        endlessRecyclerViewScrollListener2.resetState()
        swipe_to_refresh.isRefreshing = false
        productList.clear()
        productSearchCategoryAdapter.notifyDataSetChanged()
        productsListViewModel.searchForProduct(
            categoryID,
            ConstantObjects.currentLanguage,
            1,
            countryList,
            regionList,
            neighoodList,
            subCategoryList,
            specificationList,
            startPrice,
            endProce
        )
    }

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {

    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            added_product_id_to_fav = productList[position].id
            productsListViewModel.addProductToFav(productList[position].id)

        } else {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

    }

    override fun onApplyFilter(
        countryList: List<Int>,
        regionList: List<Int>,
        neighoodList: List<Int>,
        subCategoryList: List<Int>,
        specificationList: List<String>,
        startPrice: Float,
        endProce: Float
    ) {
        productList.clear()
        productSearchCategoryAdapter.notifyDataSetChanged()
        this.countryList = countryList
        this.regionList = countryList
        this.neighoodList = neighoodList
        this.subCategoryList = subCategoryList
        this.specificationList = specificationList
        this.startPrice = startPrice
        this.endProce = endProce
        onRefresh()
        // productsListViewModel.searchForProduct(categoryID, ConstantObjects.currentLanguage, 1,countryList,regionList,neighoodList,subCategoryList,specificationList,startPrice,endProce)
    }

    override fun resetFilter() {
        productList.clear()
        productSearchCategoryAdapter.notifyDataSetChanged()
        this.countryList = ArrayList()
        this.regionList = ArrayList()
        this.neighoodList = ArrayList()
        this.subCategoryList = ArrayList()
        this.specificationList = ArrayList()
        this.startPrice = 0f
        this.endProce = 0f
        onRefresh()
        //  productsListViewModel.searchForProduct(categoryID, ConstantObjects.currentLanguage, 1,countryList,regionList,neighoodList,subCategoryList,specificationList,startPrice,endProce)

    }


    /***********
     * ***********
     * *********
     * *********
     * ******
     * *******/
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

    @SuppressLint("ResourceType")
    private fun showSpecification(specificationList: ArrayList<CategorySpecification>) {
        val builder = AlertDialog.Builder(this@SearchCategoryActivity)
            .create()
        val view = layoutInflater.inflate(R.layout.sub_category_layout, null)
        builder.setView(view)
        bottom_bar.hide()

        fun specificationAdaptor(list: List<CategorySpecification>) {
            view.specification_rcv.adapter =
                object : GenericListAdapter<CategorySpecification>(
                    R.layout.item_filter_specification,
                    bind = { element, holder, itemCount, position ->
                        holder.view.run {
                            element.run {
                                header_title.text = name
                                sub_item_rcv.adapter =
                                    object : GenericListAdapter<SubSpecification>(
                                        R.layout.item_filter_specification_sub_item,
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

        view.btn_region.setOnClickListener {
            builder.dismiss()
            btnRegion.performClick()
        }

        view.btnSubCategory.setOnClickListener {
            builder.dismiss()
            btnSubCatgeoryFilter.performClick()
        }
        view.btnSpecification.setOnClickListener {
            builder.dismiss()
            btnSpecification.performClick()
        }
        specificationAdaptor(specificationList)
    }

    private fun FollowCategoryAPI() {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.AddFollow(arrayListOf(categoryID.toInt()))
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
                        HelpFunctions.ShowLongToast(
                            getString(R.string.follow_catgeory),
                            this@SearchCategoryActivity
                        )
                        category.isFollow = true
                        checkFollowIcon(category.isFollow)
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun RemoveFollow() {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.RemoveFollow(categoryID.toInt())
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
                        HelpFunctions.ShowLongToast(
                            getString(R.string.follow_catgeory),
                            this@SearchCategoryActivity
                        )
                        category.isFollow = false
                        checkFollowIcon(category.isFollow)

                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun GetCategoryById() {
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.GetCategoryById(categoryID.toInt())
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                if (response.isSuccessful) {

                    response.body()?.run {
                        category = Gson().fromJson(
                            Gson().toJson(data),
                            object : TypeToken<Category>() {}.type
                        )

                        follow_category.setOnClickListener {
                            if (!HelpFunctions.isUserLoggedIn()) {
                                startActivity(
                                    Intent(
                                        this@SearchCategoryActivity,
                                        SignInActivity::class.java
                                    )
                                )
                            } else {
                                if (category.isFollow) {
                                    RemoveFollow()
                                } else {
                                    FollowCategoryAPI()
                                }
                            }
                        }
                        checkFollowIcon(category.isFollow)
                    }
                }

                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun checkFollowIcon(isFollow: Boolean) {
        val img = if (isFollow) {
            ContextCompat.getDrawable(
                this,
                R.drawable.notification
            )
        } else {
            ContextCompat.getDrawable(
                this,
                R.drawable.notification_log
            )
        }
        // follow_category.setCompoundDrawables(img, null, null, null)
        follow_category.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

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

    @SuppressLint("ResourceType")
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
        view.btn_region.setOnClickListener {
            builder.dismiss()
            btnRegion.performClick()
        }

        view.btnSpecification.setOnClickListener {
            builder.dismiss()
            btnSpecification.performClick()
        }
        view.btnSubCategory.setOnClickListener {
            builder.dismiss()
            btnSubCatgeoryFilter.performClick()
        }

        subCategoryAdaptor(list)
    }


}