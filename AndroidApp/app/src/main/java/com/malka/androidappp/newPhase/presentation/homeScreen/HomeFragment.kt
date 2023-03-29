package com.malka.androidappp.newPhase.presentation.homeScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.order.CartActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.widgets.viewpager2.AutoScrollViewPager
import com.malka.androidappp.newPhase.domain.models.homeCategoryProductResp.CategoryProductItem
import com.malka.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderItem
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.homeScreen.adapters.AdapterAllCategories
import com.malka.androidappp.newPhase.presentation.homeScreen.adapters.CategoryProductAdapter
import com.malka.androidappp.newPhase.presentation.homeScreen.adapters.SliderAdaptor
import com.malka.androidappp.newPhase.presentation.homeScreen.viewModel.HomeViewModel
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.SearchCategoryActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.malka.androidappp.newPhase.presentation.searchActivity.SearchActivity
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.fragment_homee.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(R.layout.fragment_homee), AdapterAllCategories.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener, SetOnProductItemListeners,
    CategoryProductAdapter.SetOnSelectedProductInCategory {
    private var dotscount = 0
    var dots: ArrayList<ImageView> = arrayListOf()
    var slider_home_: AutoScrollViewPager? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var lastviewedPorductAdatper: ProductHorizontalAdapter
    lateinit var lastviewedPorductList: ArrayList<Product>

    //===== 1 added from product in category  , 2 added from product in category
    val added_from_product_in_category = 1
    val added_from_last_product_view = 2
    var status_product_added_to_fav_from = 0
    var added_product_id_to_fav = 0
    var selected_category_product_added_to_fav = 0

    lateinit var categoryProductHomeList: ArrayList<CategoryProductItem>
    lateinit var categoryPrductAdapter: CategoryProductAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpView()
        setListenser()
        setupLastViewedPorductsAdapter()
        setUpCategoryProductAdapetr()
        setupLoginViewModel()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


    }


    private fun settingUpView() {
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            ivNewImage.scaleX = 1f
        } else {
            ivNewImage.scaleX = -1f
        }
        containerLastView.hide()
    }

    private fun setupLastViewedPorductsAdapter() {
        lastviewedPorductList = ArrayList()
        lastviewedPorductAdatper = ProductHorizontalAdapter(lastviewedPorductList, this, 0, true)
        rvLastViewedProducts.apply {
            adapter = lastviewedPorductAdatper
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
        }

    }

    private fun setUpCategoryProductAdapetr() {
        categoryProductHomeList = ArrayList()
        categoryPrductAdapter = CategoryProductAdapter(categoryProductHomeList, this)
        dynamic_product_rcv.apply {
            adapter = categoryPrductAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setupLoginViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        })
        homeViewModel.isLoadingAllCategory.observe(viewLifecycleOwner, Observer {
            if (it)
                progressBarAllCAtegory.show()
            else
                progressBarAllCAtegory.hide()
        })

        homeViewModel.isNetworkFail.observe(viewLifecycleOwner, Observer {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        })
        homeViewModel.errorResponseObserver.observe(viewLifecycleOwner, Observer {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        })

        homeViewModel.sliderObserver.observe(viewLifecycleOwner, Observer { homeSliderResp ->
            if (homeSliderResp != null) {
                if (homeSliderResp.status_code == 200) {
                    homeSliderResp.sliderList?.let {
                        //var sliderList: ArrayList<HomeSliderResp> = Gson().fromJson(Gson().toJson(userLoginResp.data), object : TypeToken<ArrayList<Slider>>() {}.type)
                        setPagerDots(it)
                    }


                }
            }
        })
        homeViewModel.searchObserver.observe(viewLifecycleOwner) { searchResp ->
            if (searchResp != null) {
                if (searchResp.status_code == 200) {
                    var list: ArrayList<Product> = Gson().fromJson(
                        Gson().toJson(searchResp.data),
                        object : TypeToken<ArrayList<Product>>() {}.type
                    )
                    textInputLayout11.updateList(list)
                }
            }

        }
        homeViewModel.categoriesObserver.observe(viewLifecycleOwner) { categoriesResp ->
            ConstantObjects.categoryList = Gson().fromJson(
                Gson().toJson(categoriesResp.data),
                object : TypeToken<ArrayList<Category>>() {}.type
            )
            all_categories_recycler.adapter = AdapterAllCategories(
                ConstantObjects.categoryList,
                requireContext(),
                this@HomeFragment
            )
            homeViewModel.getListHomeCategoryProduct()
        }
        homeViewModel.categoriesErrorResponseObserver.observe(viewLifecycleOwner) {
            //HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), context)
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        }
        homeViewModel.homeCategoryProductObserver.observe(viewLifecycleOwner) { homeCategoriesProdcutResp ->

            if (homeCategoriesProdcutResp.status_code == 200) {
                categoryProductHomeList.clear()
                homeCategoriesProdcutResp.categoryProductList?.let {
                    categoryProductHomeList.addAll(it)
                }
                categoryPrductAdapter.notifyDataSetChanged()
            }
        }
        homeViewModel.homeCategoryProductErrorResponseObserver.observe(viewLifecycleOwner) {
            //HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), context)
            HelpFunctions.ShowLongToast(it.message.toString(), context)

        }
        homeViewModel.lastViewProductsObserver.observe(viewLifecycleOwner) { productListResp ->
            if (productListResp.productList != null) {
                if (productListResp.productList.isNotEmpty()) {
                    containerLastView.show()
                    lastviewedPorductList.clear()
                    lastviewedPorductList.addAll(productListResp.productList)
                    lastviewedPorductAdatper.notifyDataSetChanged()
                } else {
                    containerLastView.hide()
                }
            }

        }
        homeViewModel.isNetworkFailProductToFav.observe(viewLifecycleOwner, Observer {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        })
        homeViewModel.errorResponseObserverProductToFav.observe(viewLifecycleOwner, Observer {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        })
        homeViewModel.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                when (status_product_added_to_fav_from) {
                    added_from_product_in_category -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            var changedCategoryItemPosition = -1
                            var changedProductItemPosition = -1
                            for ((catIndex, category) in categoryProductHomeList.withIndex()) {
                                if (category.catId == selected_category_product_added_to_fav) {
                                    category.listProducts?.let {
                                        for ((index, product) in it.withIndex()) {
                                            if (product.id == added_product_id_to_fav) {
                                                product.isFavourite = !product.isFavourite
                                                changedProductItemPosition = index
                                                changedCategoryItemPosition = catIndex
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                            withContext(Dispatchers.Main) {
                                try {
                                    if (changedCategoryItemPosition != -1 && changedProductItemPosition != -1) {
                                        // 1. get ith item of the parent recyclerView
                                        val ithChildViewHolder: CategoryProductAdapter.CategoryProductHolder =
                                            dynamic_product_rcv.findViewHolderForAdapterPosition(
                                                changedCategoryItemPosition
                                            ) as CategoryProductAdapter.CategoryProductHolder
                                        val ithChildsRecyclerView: RecyclerView =
                                            ithChildViewHolder.viewBinding.productRcv

                                        // 3. get ithRecyclerView's adapter
                                        val ithChildAdapter: ProductHorizontalAdapter? =
                                            ithChildsRecyclerView.adapter as ProductHorizontalAdapter?
                                        ithChildAdapter?.let { ithChildAdapter ->
                                            ithChildAdapter.notifyItemChanged(
                                                changedProductItemPosition
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    //println("hhh "+e.message)
                                    categoryPrductAdapter.notifyDataSetChanged()
                                }

                                // categoryPrductAdapter.notifyDataChange(changedCategoryItemPosition,changedProductItemPosition)
                                //categoryPrductAdapter.notifyItemChanged(changedCategoryItemPosition)
                            }
                        }
                        /***for similer product*/
                        lifecycleScope.launch(Dispatchers.IO) {
                            var selectedSimilerProduct: Product? = null
                            for (product in lastviewedPorductList) {
                                if (product.id == added_product_id_to_fav) {
                                    product.isFavourite = !product.isFavourite
                                    selectedSimilerProduct = product
                                    break
                                }
                            }
                            withContext(Dispatchers.Main) {
                                /**update similer product*/
                                selectedSimilerProduct?.let { product ->
                                    lastviewedPorductAdatper.notifyItemChanged(
                                        lastviewedPorductList.indexOf(
                                            product
                                        )
                                    )
                                }
                            }
                        }
                    }
                    added_from_last_product_view -> {
                        /***for similer product*/
                        lifecycleScope.launch(Dispatchers.IO) {
                            var selectedSimilerProduct: Product? = null
                            for (product in lastviewedPorductList) {
                                if (product.id == added_product_id_to_fav) {
                                    product.isFavourite = !product.isFavourite
                                    selectedSimilerProduct = product
                                    break
                                }
                            }
                            withContext(Dispatchers.Main) {
                                /**update similer product*/
                                selectedSimilerProduct?.let { product ->
                                    lastviewedPorductAdatper.notifyItemChanged(
                                        lastviewedPorductList.indexOf(
                                            product
                                        )
                                    )
                                }
                            }
                        }
                        /**for category products*/
                        lifecycleScope.launch(Dispatchers.IO) {
                            var changedCategoryItemPosition = -1
                            var changedProductItemPosition = -1
                            for ((catIndex, category) in categoryProductHomeList.withIndex()) {
                                if (category.catId == selected_category_product_added_to_fav) {
                                    category.listProducts?.let {
                                        for ((index, product) in it.withIndex()) {
                                            if (product.id == added_product_id_to_fav) {
                                                product.isFavourite = !product.isFavourite
                                                changedProductItemPosition = index
                                                changedCategoryItemPosition = catIndex
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                            withContext(Dispatchers.Main) {
                                /**update  product in Category*/
                                try {
                                    if (changedCategoryItemPosition != -1 && changedProductItemPosition != -1) {
                                        // 1. get ith item of the parent recyclerView
                                        val ithChildViewHolder: CategoryProductAdapter.CategoryProductHolder =
                                            dynamic_product_rcv.findViewHolderForAdapterPosition(
                                                changedCategoryItemPosition
                                            ) as CategoryProductAdapter.CategoryProductHolder
                                        val ithChildsRecyclerView: RecyclerView =
                                            ithChildViewHolder.viewBinding.productRcv

                                        // 3. get ithRecyclerView's adapter
                                        val ithChildAdapter: ProductHorizontalAdapter? =
                                            ithChildsRecyclerView.adapter as ProductHorizontalAdapter?
                                        ithChildAdapter?.let { ithChildAdapter ->
                                            ithChildAdapter.notifyItemChanged(
                                                changedProductItemPosition
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    //println("hhh "+e.message)
                                    categoryPrductAdapter.notifyDataSetChanged()
                                }

                                // categoryPrductAdapter.notifyDataChange(changedCategoryItemPosition,changedProductItemPosition)
                                //categoryPrductAdapter.notifyItemChanged(changedCategoryItemPosition)
                            }
                        }
                    }
                }
            }
        }
        onRefresh()
    }


    private fun setListenser() {
        ic_cart.setOnClickListener {
            if (ConstantObjects.logged_userid.isEmpty()) {
                startActivity(Intent(context, SignInActivity::class.java).apply {
                })
            } else {
                startActivity(Intent(context, CartActivity::class.java))
            }
        }
/*       textInputLayout11._view2()
//            .setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    SearchAndNavigateToCategoryListing(textInputLayout11.getText())
//                    return@OnEditorActionListener true
//                }
//                true
//            })
//        textInputLayout11._attachInfoClickListener {
//            SearchAndNavigateToCategoryListing(textInputLayout11.getText())
//
//        }

 */
        textInputLayout11.onClickListener {
//            startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
//                println("hhhh "+it.name)
//                println("hhhh "+it.categoryId.toString())
//                putExtra("SearchQuery", it.name)
//                putExtra("CategoryID", it.categoryId.toString())
//            })
//            (requireActivity() as BaseActivity).hideSoftKeyboard(textInputLayout11._view2())

            startActivity(Intent(requireContext(), SearchActivity::class.java).apply {
                putExtra(ConstantObjects.searchQueryKey, it.name)
            })
            (requireActivity() as BaseActivity).hideSoftKeyboard(textInputLayout11._view2())
        }
        textInputLayout11._onChange { query ->
            if (!query.isEmpty()) {
                homeViewModel.doSearch(mapOf("productName" to query))
            } else {
                textInputLayout11.updateList(arrayListOf())
            }
        }
    }


    override fun pnCategorySelected(position: Int) {
        startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
            putExtra("CategoryDesc", ConstantObjects.categoryList[position].name)
            putExtra("CategoryID", ConstantObjects.categoryList[position].id.toString())
            putExtra("SearchQuery", "")
            putExtra("isMapShow", ConstantObjects.categoryList[position].id == 3)

        })
    }

    private fun setPagerDots(list: List<HomeSliderItem>) {
        if (list.size > 0) {
            sliderLayout.show()
            val viewPagerAdapter = SliderAdaptor(requireContext(), list)
            slider_home.adapter = viewPagerAdapter
            dots_indicator.attachTo(slider_home)
            slider_home.startAutoScroll()
        }
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        dotscount = 0
        dots.clear()
        SliderDots.removeAllViews()
        homeViewModel.getSliderData(1)
        homeViewModel.getAllCategories()
        if (HelpFunctions.isUserLoggedIn()) {
            homeViewModel.getLastViewedProduct()
            //  HelpFunctions.GetUserWatchlist()
        }
    }

    override fun onProductSelect(position: Int, productId: Int, categoryID: Int) {
//        SharedPreferencesStaticClass.ad_userid = ""
//        ConstantObjects.is_watch_iv = ivFav
        goToProductDetails(productId)

    }

    override fun onAddProductToFav(position: Int, productId: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_last_product_view
            added_product_id_to_fav = productId
            homeViewModel.addProductToFav(productId)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onSelectedProductInCategory(position: Int, productID: Int, categoryID: Int) {
//        SharedPreferencesStaticClass.ad_userid = ""
//        ConstantObjects.is_watch_iv = ivFav
//        startActivity(
//            Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
//                putExtra(ConstantObjects.productIdKey, productID)
//                putExtra("Template", "")
//            })
        goToProductDetails(productID)

    }

    override fun onAddProductInCategoryToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_product_in_category
            added_product_id_to_fav = productID
            selected_category_product_added_to_fav = categoryID
            homeViewModel.addProductToFav(productID)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }
    /**open activity product detials functions**/
    val productDetailsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val productId:Int= result.data?.getIntExtra(ConstantObjects.productIdKey,0) ?: 0
                val productFavStatusKey:Boolean= result.data?.getBooleanExtra(ConstantObjects.productFavStatusKey,false) ?:false
                refreshFavProductStatus(productId,productFavStatusKey)
            }
        }
    private fun goToProductDetails(productId: Int) {
        productDetailsLauncher.launch(Intent(context, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, productId)
            putExtra("Template", "")
        })
    }

    private fun refreshFavProductStatus(productId: Int, productFavStatusKey: Boolean) {
        /***for similer product*/
        lifecycleScope.launch(Dispatchers.IO) {
            var selectedSimilerProduct: Product? = null
            for (product in lastviewedPorductList) {
                if (product.id == productId) {
                    product.isFavourite = productFavStatusKey
                    selectedSimilerProduct = product
                    break
                }
            }
            withContext(Dispatchers.Main) {
                /**update similer product*/
                selectedSimilerProduct?.let { product ->
                    lastviewedPorductAdatper.notifyItemChanged(
                        lastviewedPorductList.indexOf(
                            product
                        )
                    )
                }
            }
        }
        /**for category products*/
        lifecycleScope.launch(Dispatchers.IO) {
            var changedCategoryItemPosition = -1
            var changedProductItemPosition = -1
            for ((catIndex, category) in categoryProductHomeList.withIndex()) {
                if (category.catId == selected_category_product_added_to_fav) {
                    category.listProducts?.let {
                        for ((index, product) in it.withIndex()) {
                            if (product.id == productId) {
                                product.isFavourite =productFavStatusKey
                                changedProductItemPosition = index
                                changedCategoryItemPosition = catIndex
                                break
                            }
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                /**update  product in Category*/
                try {
                    if (changedCategoryItemPosition != -1 && changedProductItemPosition != -1) {
                        // 1. get ith item of the parent recyclerView
                        val ithChildViewHolder: CategoryProductAdapter.CategoryProductHolder =
                            dynamic_product_rcv.findViewHolderForAdapterPosition(
                                changedCategoryItemPosition
                            ) as CategoryProductAdapter.CategoryProductHolder
                        val ithChildsRecyclerView: RecyclerView =
                            ithChildViewHolder.viewBinding.productRcv

                        // 3. get ithRecyclerView's adapter
                        val ithChildAdapter: ProductHorizontalAdapter? =
                            ithChildsRecyclerView.adapter as ProductHorizontalAdapter?
                        ithChildAdapter?.let { ithChildAdapter ->
                            ithChildAdapter.notifyItemChanged(
                                changedProductItemPosition
                            )
                        }
                    }
                } catch (e: Exception) {
                    //println("hhh "+e.message)
                    categoryPrductAdapter.notifyDataSetChanged()
                }

                // categoryPrductAdapter.notifyDataChange(changedCategoryItemPosition,changedProductItemPosition)
                //categoryPrductAdapter.notifyItemChanged(changedCategoryItemPosition)
            }
        }
    }


}

//@SuppressLint("ResourceType")
//    private fun setHomeCategoryProductAdaptor() {
//        dynamic_product_rcv.adapter = object : GenericListAdapter<CategoryProductItem>(
//            R.layout.parenet_category_item,
//            bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    element.run {
//                        when (typeName) {
//                            "category" -> {
//                                categoryHeaderContainer!!.show()
//                                product_list_layout!!.hide()
//                            }
//                            "list" -> {
//                                categoryHeaderContainer!!.hide()
//                                product_list_layout!!.show()
//                            }
//                        }
//                        Extension.loadThumbnail(
//                            requireContext(),
//                            category_icon,
//                            category_icon_iv,
//                            null
//                        )
//                        detail_tv!!.text = detail
//                        category_name_tv!!.text = category_name
//                        category_name_tv_2!!.text = category_name
////                        viewAllCategoriesProduct.setOnClickListener {
////                            startActivity(
////                                Intent(requireContext(), SearchCategoryActivity::class.java).apply {
////                                    putExtra("CategoryDesc", category_id)
////                                    putExtra("SearchQuery", "")
////                                    putExtra("isMapShow", category_id.equals("Property"))
////                                })
//                        // }
//                        setHomeProductAdaptor(product, product_rcv, category_id)
//                    }
//                }
//            }
//        ) {
//            override fun getFilter(): Filter {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(
//                ConstantObjects.categoryProductHomeList
//            )
//        }
//
//    }

//@SuppressLint("ResourceType")
//    fun setHomeProductAdaptor(list: List<Product>, product_rcv: RecyclerView, category_id: Int) {
//        product_rcv.adapter = object : GenericListAdapter<Product>(
//            R.layout.product_item, bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    val params: ViewGroup.LayoutParams = fullview.layoutParams
//                    params.width = resources.getDimension(R.dimen._220sdp).toInt()
//                    params.height = params.height
//                    fullview.layoutParams = params
//                    productAdaptor(list[position], context, holder, true, category_id)
//                }
//            }
//        ) {
//            override fun getFilter(): Filter {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(list)
//        }
//    }
//
//    fun productAdaptor(
//        product: Product,
//        context: Context,
//        holder: BaseViewHolder,
//        isGrid: Boolean,
//        category_id: Int
//    ) {
//        holder.view.run {
//            product.run {
//                ivFav.setOnClickListener {
//                    if (HelpFunctions.isUserLoggedIn()) {
////                        HelpFunctions.ShowLongToast(
////                            "adding to wish list not implemented yet",
////                            requireActivity()
////                        )
//                        status_product_added_to_fav_from = added_from_product_in_category
//                        added_product_id_to_fav = product.id
//                        selected_category_product_added_to_fav = category_id
//                        homeViewModel.addProductToFav(product.id)
//
//                    } else {
//                        requireActivity().startActivity(
//                            Intent(
//                                context,
//                                SignInActivity::class.java
//                            ).apply {})
//                    }
////                    if (HelpFunctions.AdAlreadyAddedToWatchList(id.toString())) {
////                        HelpFunctions.DeleteAdFromWatchlist(id.toString(), context) { is_watch_iv.setImageResource(R.drawable.star) }
////                    } else {
////                        if (ConstantObjects.logged_userid.isEmpty()) {
////                             context.startActivity( Intent(context, SignInActivity::class.java).apply {})
////
////                        } else {
////
////                            HelpFunctions.InsertAdToWatchlist(
////                                id.toString(),0,
////                                context
////                            ) {
////                                is_watch_iv.setImageResource(R.drawable.starcolor)
////
////                            }
////
////                        }
////                    }
//                }
//                if (HelpFunctions.isUserLoggedIn()) {
//                    if (isFavourite) {
//                        ivFav.setImageResource(R.drawable.starcolor)
//                    } else {
//                        ivFav.setImageResource(R.drawable.star)
//                    }
//                } else {
//                    ivFav.setImageResource(R.drawable.star)
//                }
//
//                if (name != null) {
//                    titlenamee.text = name
//                } else {
//                    titlenamee.text = ""
//                }
//
//                if (regionName != null) {
//                    city_tv.text = regionName
//                } else {
//                    city_tv.text = "regionName"
//                }
//                setOnClickListener {
//                    SharedPreferencesStaticClass.ad_userid = ""
//                    ConstantObjects.is_watch_iv = ivFav
//                    context.startActivity(
//                        Intent(
//                            context,
//                            ProductDetailsActivity::class.java
//                        ).apply {
//                            putExtra(ConstantObjects.productIdKey, id)
//                            putExtra("Template", "")
//                        })
//                }
//                Extension.loadThumbnail(
//                    context,
//                    image,
//                    productimg,
//                    loader
//                )
//                LowestPrice_layout.invisible()
//                LowestPrice_layout_2.hide()
//                product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
//                    context.getString(
//                        R.string.Rayal
//                    )
//                }"
//                purchasing_price_tv_2.text =
//                    "${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
//
//                if (isGrid) {
//                    lisView.hide()
//                    gridview.show()
//                } else {
//                    lisView.show()
//                    gridview.hide()
//                }
//
//                if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
//                    containerTimeBar.background =
//                        ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_ar)
//                } else {
//                    containerTimeBar.background =
//                        ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_en)
//                }
//
//                date_tv.text = HelpFunctions.getViewFormatForDateTrack(createdAt)
////                val listingTypeFormated="1"
////                when (listingTypeFormated) {
////                    "1" -> {
////                        LowestPrice_layout.invisible()
////                        LowestPrice_layout_2.hide()
////                        product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
////                            context.getString(
////                                R.string.Rayal
////                            )
////                        }"
////                        purchasing_price_tv_2.text =
////                            "${
////                                price.toDouble().decimalNumberFormat()
////                            } ${context.getString(R.string.Rayal)}"
////                    }
////                    "12", "2" -> {
//////                        LowestPrice_layout.show()
//////                        LowestPrice_layout_2.show()
//////
//////                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
//////                            context.getString(
//////                                R.string.Rayal
//////                            )
//////                        }"
//////                        purchasing_price_tv_2.text =
//////                            "${
//////                                startingPrice.toDouble().decimalNumberFormat()
//////                            } ${context.getString(R.string.Rayal)}"
//////
//////
//////                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
//////                            context.getString(
//////                                R.string.Rayal
//////                            )
//////                        }"
//////
//////                        LowestPrice.text = "${
//////                            reservePrice!!.toDouble().decimalNumberFormat()
//////                        } ${context.getString(R.string.Rayal)}"
//////                        LowestPrice_2.text =
//////                            "${
//////                                reservePrice.toDouble().decimalNumberFormat()
//////                            } ${context.getString(R.string.Rayal)}"
////                    }
////                }
//            }
//        }
//    }