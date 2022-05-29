package com.malka.androidappp.botmnav_fragments.home.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.order.CartActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.browse_market.SearchCategoryActivity
import com.malka.androidappp.servicemodels.model.AllCategoriesResponseBack
import com.malka.androidappp.servicemodels.model.DynamicList
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.helper.widgets.viewpager2.AutoScrollViewPager
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.home.GetAllAds
import kotlinx.android.synthetic.main.fragment_homee.*
import kotlinx.android.synthetic.main.parenet_category_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_homee),
    AdapterAllCategories.OnItemClickListener {
    private var dotscount = 0
    var dots: ArrayList<ImageView> = arrayListOf()
    var sliderlist: ArrayList<Int> = ArrayList()
    var slider_home_: AutoScrollViewPager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sliderlist.add(R.drawable.slider_demo)
        sliderlist.add(R.drawable.slider_demo)
        sliderlist.add(R.drawable.slider_demo)



        setListenser()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        if (HelpFunctions.IsUserLoggedIn()) {
            HelpFunctions.GetUserWatchlist()
        }


        ic_cart.setOnClickListener {
            if (ConstantObjects.logged_userid.isEmpty()) {
                startActivity(Intent(context, SignInActivity::class.java).apply {
                })
            } else {
                startActivity(Intent(context, CartActivity::class.java))
            }
        }

        if (ConstantObjects.categoryList.size > 0) {
            all_categories_recycler.adapter =
                AdapterAllCategories(
                    ConstantObjects.categoryList,
                    requireContext(),
                    this@HomeFragment
                )

            getAllAdsData()
        } else {

            getAllCategories()
        }
        setPagerDots(sliderlist)
    }
    private fun setListenser() {
        textInputLayout11._view2()
            .setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    SearchAndNavigateToCategoryListing(textInputLayout11.getText())
                    return@OnEditorActionListener true
                }
                true
            })
        textInputLayout11._attachInfoClickListener {
            SearchAndNavigateToCategoryListing(textInputLayout11.getText())

        }
    }


    fun SearchAndNavigateToCategoryListing(searchquery: String) {
        if (!searchquery.isEmpty()) {
            startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
                putExtra("CategoryDesc", "")
                putExtra("SearchQuery", searchquery)
            })
            (requireActivity() as BaseActivity).hideSoftKeyboard(textInputLayout11._view2())
        }
    }


    fun getAllCategories() {
        HelpFunctions.startProgressBar(this.requireActivity())


        val malqaa = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<AllCategoriesResponseBack> =
            malqaa.getAllCategories((requireActivity() as MainActivity).culture())

        call.enqueue(object : Callback<AllCategoriesResponseBack> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<AllCategoriesResponseBack>,
                response: Response<AllCategoriesResponseBack>
            ) {

                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val resp: AllCategoriesResponseBack = response.body()!!
                        ConstantObjects.categoryList = resp.data
                        all_categories_recycler.adapter =
                            AdapterAllCategories(
                                ConstantObjects.categoryList,
                                requireContext(),
                                this@HomeFragment
                            )

                        getAllAdsData()

                    }

                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), context)
                    HelpFunctions.dismissProgressBar()

                }

            }

            override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, context) }

                HelpFunctions.dismissProgressBar()
            }
        })
    }


    fun getAllAdsData() {
        if (ConstantObjects.list.size > 0) {
            setHomeAdaptor()
        } else {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<GetAllAds> = malqa.GetAllAds(ConstantObjects.logged_userid)
            call.enqueue(object : Callback<GetAllAds> {
                override fun onResponse(
                    call: Call<GetAllAds>,
                    response: Response<GetAllAds>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            response.body()!!.data.run {
                                ConstantObjects.list.apply {
                                    add(
                                        DynamicList(
                                            getString(R.string.vehicles),
                                            R.drawable.ic_vechile,
                                            caradvetisement, category_id = "Vehicles"
                                        )
                                    )
                                    add(
                                        DynamicList(
                                            getString(R.string.electronics),
                                            R.drawable.ic_electronic,
                                            generaladvetisement,
                                            category_id = "Electronics & Gaming"
                                        )
                                    )
                                    add(
                                        DynamicList(
                                            getString(R.string.real_estate),
                                            R.drawable.ic_real_estate,
                                            propertyadvetisement, category_id = "Property"
                                        )
                                    )
                                    add(
                                        DynamicList(
                                            getString(R.string.List_Auctions),
                                            R.drawable.ic_vechile,
                                            recentadvetisement,
                                            "list",
                                            getString(R.string.List_Auctions_detail), ""
                                        )
                                    )
                                }

                                setHomeAdaptor()

                            }


                        } else {
                            HelpFunctions.ShowLongToast(getString(R.string.Error), context)
                        }
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.Error), context)
                    }
                    HelpFunctions.dismissProgressBar()

                }

                override fun onFailure(call: Call<GetAllAds>, t: Throwable) {
                    HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
                    HelpFunctions.dismissProgressBar()

                }
            })

        }

    }


    private fun setHomeAdaptor() {
        dynamic_product_rcv.adapter = object : GenericListAdapter<DynamicList>(
            R.layout.parenet_category_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        when (typeName) {
                            "category" -> {
                                category_type!!.show()
                                product_list_layout!!.hide()
                            }
                            "list" -> {
                                category_type!!.hide()
                                product_list_layout!!.show()
                            }
                        }
                        view_all.setOnClickListener {


                            startActivity(
                                Intent(
                                    requireContext(),
                                    SearchCategoryActivity::class.java
                                ).apply {
                                    putExtra("CategoryDesc", category_id)
                                    putExtra("SearchQuery", "")
                                    putExtra("isMapShow", category_id.equals("Property"))
                                })

                        }

                        detail_tv!!.text = detail
                        category_name_tv!!.text = category_name
                        category_name_tv_2!!.text = category_name
                        category_icon_iv!!.setImageResource(category_icon)

                        GenericAdaptor().setHomeProductAdaptor(product, product_rcv)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                ConstantObjects.list
            )
        }

    }

    override fun OnItemClick(position: Int) {
        super.OnItemClick(position)


        startActivity(Intent(requireContext(), SearchCategoryActivity::class.java).apply {
            putExtra("CategoryDesc", ConstantObjects.categoryList[position].categoryName)
            putExtra("SearchQuery", "")
            putExtra("isMapShow", ConstantObjects.categoryList[position].categoryid == 3)

        })
    }



    private fun setPagerDots(list: List<Int>) {

        if (list.size > 0) {
            sliderLayout.show()
            val viewPagerAdapter = SliderAdaptor(requireContext(), list)
            slider_home.adapter = viewPagerAdapter
            dotscount = viewPagerAdapter.getCount()
            for (i in 0 until dotscount) {
                dots.add(ImageView(requireContext()))
            }
            for (i in 0 until dotscount) {
                dots[i] = ImageView(requireContext())
                dots[i].setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.non_active_slider
                    )
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

            slider_home.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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


}






