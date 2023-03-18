package com.malka.androidappp.newPhase.presentation.homeScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.order.CartActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.data.helper.widgets.viewpager2.AutoScrollViewPager
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.Slider
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.DynamicList
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.HomeProductsResponse
import com.malka.androidappp.newPhase.presentation.homeScreen.adapters.AdapterAllCategories
import com.malka.androidappp.newPhase.presentation.homeScreen.adapters.SliderAdaptor
import com.malka.androidappp.newPhase.presentation.homeScreen.viewModel.HomeViewModel
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.prodctListActivity.browse_market.SearchCategoryActivity
import com.malka.androidappp.newPhase.presentation.searchActivity.SearchActivity
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_homee.*
import kotlinx.android.synthetic.main.parenet_category_item.view.*
import kotlinx.android.synthetic.main.product_item.view.*


class HomeFragment : Fragment(R.layout.fragment_homee), AdapterAllCategories.OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private var dotscount = 0
    var dots: ArrayList<ImageView> = arrayListOf()
    var slider_home_: AutoScrollViewPager? = null
    private lateinit var homeViewModel: HomeViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpView()
        setListenser()
        setupLoginViewModel()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        if (HelpFunctions.isUserLoggedIn()) {
            HelpFunctions.GetUserWatchlist()
        }
//        if (ConstantObjects.categoryList.size > 0) {
//            all_categories_recycler.adapter = AdapterAllCategories(ConstantObjects.categoryList, requireContext(), this@HomeFragment)
//            getAllAdsData()
//        } else {
//            getAllCategories()
//        }

    }

    private fun settingUpView() {
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            ivNewImage.scaleX = 1f
        } else {
            ivNewImage.scaleX = -1f
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
            if (it.message != null) {
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
        homeViewModel.sliderObserver.observe(viewLifecycleOwner, Observer { userLoginResp ->
            if (userLoginResp != null) {
                if (userLoginResp.status_code == 200) {
                    var sliderList: ArrayList<Slider> = Gson().fromJson(
                        Gson().toJson(userLoginResp.data),
                        object : TypeToken<ArrayList<Slider>>() {}.type
                    )
                    setPagerDots(sliderList)
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
            HelpFunctions.ShowLongToast(it.message.toString(), context)

        }
        homeViewModel.homeCategoryProductObserver.observe(viewLifecycleOwner) { homeCategoriesProdcutResp ->
            val homeProduct: ArrayList<HomeProductsResponse.HomeCategory> = Gson().fromJson(
                Gson().toJson(homeCategoriesProdcutResp.data),
                object : TypeToken<ArrayList<HomeProductsResponse.HomeCategory>>() {}.type
            )
            ConstantObjects.categoryProductHomeList.clear()
            homeProduct.forEach {
                ConstantObjects.categoryProductHomeList.apply {
                    add(
                        DynamicList(
                            it.name,
                            it.image,
                            it.listProducts,
                            it.catId
                        )
                    )
                }
            }
            setHomeCategoryProductAdaptor()
        }
        homeViewModel.homeCategoryProductErrorResponseObserver.observe(viewLifecycleOwner) {
            //HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), context)
            HelpFunctions.ShowLongToast(it.message.toString(), context)

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

    @SuppressLint("ResourceType")
    private fun setHomeCategoryProductAdaptor() {
        dynamic_product_rcv.adapter = object : GenericListAdapter<DynamicList>(
            R.layout.parenet_category_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        when (typeName) {
                            "category" -> {
                                categoryHeaderContainer!!.show()
                                product_list_layout!!.hide()
                            }
                            "list" -> {
                                categoryHeaderContainer!!.hide()
                                product_list_layout!!.show()
                            }
                        }
                        Extension.loadThumbnail(
                            requireContext(),
                            category_icon,
                            category_icon_iv,
                            null
                        )
                        detail_tv!!.text = detail
                        category_name_tv!!.text = category_name
                        category_name_tv_2!!.text = category_name
//                        viewAllCategoriesProduct.setOnClickListener {
//                            startActivity(
//                                Intent(requireContext(), SearchCategoryActivity::class.java).apply {
//                                    putExtra("CategoryDesc", category_id)
//                                    putExtra("SearchQuery", "")
//                                    putExtra("isMapShow", category_id.equals("Property"))
//                                })
                        // }
                        setHomeProductAdaptor(product, product_rcv)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                ConstantObjects.categoryProductHomeList
            )
        }

    }
    @SuppressLint("ResourceType")
    fun setHomeProductAdaptor(list: List<Product>, product_rcv: RecyclerView) {
        product_rcv.adapter = object : GenericListAdapter<Product>(
            R.layout.product_item, bind = { element, holder, itemCount, position ->
                holder.view.run {
                    val params: ViewGroup.LayoutParams = fullview.layoutParams
                    params.width = resources.getDimension(R.dimen._220sdp).toInt()
                    params.height = params.height
                    fullview.layoutParams = params
                    productAdaptor(list[position], context, holder, true)
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(list)
        }
    }

    fun productAdaptor(
        product: Product, context: Context, holder: BaseViewHolder, isGrid: Boolean
    ) {
        holder.view.run {
            product.run {
                ivFav.setOnClickListener {
                    if (Paper.book().read<Boolean>(SharedPreferencesStaticClass.islogin) == true) {
                        HelpFunctions.ShowLongToast(
                            "adding to wish list not implemented yet",
                            requireActivity()
                        )

                    } else {
                        requireActivity().startActivity(
                            Intent(
                                context,
                                SignInActivity::class.java
                            ).apply {})
                    }
//                    if (HelpFunctions.AdAlreadyAddedToWatchList(id.toString())) {
//                        HelpFunctions.DeleteAdFromWatchlist(id.toString(), context) { is_watch_iv.setImageResource(R.drawable.star) }
//                    } else {
//                        if (ConstantObjects.logged_userid.isEmpty()) {
//                             context.startActivity( Intent(context, SignInActivity::class.java).apply {})
//
//                        } else {
//
//                            HelpFunctions.InsertAdToWatchlist(
//                                id.toString(),0,
//                                context
//                            ) {
//                                is_watch_iv.setImageResource(R.drawable.starcolor)
//
//                            }
//
//                        }
//                    }
                }
                if (HelpFunctions.isUserLoggedIn()) {
                    if (isFavourite) {
                        ivFav.setImageResource(R.drawable.starcolor)
                    } else {
                        ivFav.setImageResource(R.drawable.star)
                    }
                } else {
                    ivFav.setImageResource(R.drawable.star)
                }

                if (name != null) {
                    titlenamee.text = name
                } else {
                    titlenamee.text = ""
                }

                if (regionName != null) {
                    city_tv.text = regionName
                } else {
                    city_tv.text = "regionName"
                }
                setOnClickListener {
                    SharedPreferencesStaticClass.ad_userid = ""
                    ConstantObjects.is_watch_iv = ivFav
                    context.startActivity(Intent(context, ProductDetailsActivity::class.java).apply {
                        putExtra(ConstantObjects.productIdKey, id)
                        putExtra("Template", "")
                    })
                }
                Extension.loadThumbnail(
                    context,
                    image,
                    productimg,
                    loader
                )
                LowestPrice_layout.invisible()
                LowestPrice_layout_2.hide()
                product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
                purchasing_price_tv_2.text = "${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"

                if (isGrid) {
                    lisView.hide()
                    gridview.show()
                } else {
                    lisView.show()
                    gridview.hide()
                }

                if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
                    containerTimeBar.background =
                        ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_ar)
                } else {
                    containerTimeBar.background =
                        ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_en)
                }

                date_tv.text = HelpFunctions.getViewFormatForDateTrack(createdAt)
//                val listingTypeFormated="1"
//                when (listingTypeFormated) {
//                    "1" -> {
//                        LowestPrice_layout.invisible()
//                        LowestPrice_layout_2.hide()
//                        product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
//                            context.getString(
//                                R.string.Rayal
//                            )
//                        }"
//                        purchasing_price_tv_2.text =
//                            "${
//                                price.toDouble().decimalNumberFormat()
//                            } ${context.getString(R.string.Rayal)}"
//                    }
//                    "12", "2" -> {
////                        LowestPrice_layout.show()
////                        LowestPrice_layout_2.show()
////
////                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
////                            context.getString(
////                                R.string.Rayal
////                            )
////                        }"
////                        purchasing_price_tv_2.text =
////                            "${
////                                startingPrice.toDouble().decimalNumberFormat()
////                            } ${context.getString(R.string.Rayal)}"
////
////
////                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
////                            context.getString(
////                                R.string.Rayal
////                            )
////                        }"
////
////                        LowestPrice.text = "${
////                            reservePrice!!.toDouble().decimalNumberFormat()
////                        } ${context.getString(R.string.Rayal)}"
////                        LowestPrice_2.text =
////                            "${
////                                reservePrice.toDouble().decimalNumberFormat()
////                            } ${context.getString(R.string.Rayal)}"
//                    }
//                }
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

    private fun setPagerDots(list: ArrayList<Slider>) {
        if (list.size > 0) {
            sliderLayout.show()
            val viewPagerAdapter = SliderAdaptor(requireContext(), list)
            slider_home.adapter = viewPagerAdapter
            dotscount = viewPagerAdapter.count
            for (i in 0 until dotscount) {
                dots.add(ImageView(requireContext()))
            }
            for (i in 0 until dotscount) {
                dots[i] = ImageView(requireContext())
                dots[i].setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.non_active_slider)
                )
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                SliderDots!!.addView(dots[i], params)
            }

            dots[0].setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.active_slider
                )
            )

            slider_home.addOnPageChangeListener(object :
                ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                    try {
                        for (i in 0 until dotscount) {
                            dots[i].setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.non_active_slider
                                )
                            )
                        }
                        dots[position].setImageDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.active_slider
                            )
                        )
                    } catch (error: Exception) {
                        //  slider_home.stopAutoScroll()
                    }


                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
            slider_home_ = slider_home
            slider_home.startAutoScroll()
        }
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        dotscount = 0
        dots.clear()
        SliderDots.removeAllViews()
        homeViewModel.getSliderData()
        homeViewModel.getAllCategories()

    }


}
