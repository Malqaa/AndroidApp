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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.browse_market.BrowseMarketFragment
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.botmnav_fragments.home.model.DynamicList
import com.malka.androidappp.activities_main.order.CartActivity
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

    var allCategoryList: List<AllCategoriesModel> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sliderlist.add(R.drawable.slider_demo)
        sliderlist.add(R.drawable.slider_demo)
        sliderlist.add(R.drawable.slider_demo)

        loadLocate()


        setListenser()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)







        //Zack
        //Date: 03/14/2021
        //If user has logged in
        if (ConstantObjects.logged_userid.trim().length > 0) {

              HelpFunctions.GetUserWatchlist();
           // HelpFunctions.GetUsersCartList(requireContext());
          //  HelpFunctions.GetUserCreditCards(this@HomeFragment);
          //  HelpFunctions.GetUserShippingAddress(this@HomeFragment);
        }



        ic_cart.setOnClickListener {
            if (ConstantObjects.logged_userid.isEmpty()) {
               startActivity(Intent(context, SignInActivity::class.java).apply {
                })
            } else {
                val  intent = Intent(requireActivity(), CartActivity::class.java)
                startActivity(intent)
            }

        }

        getAllCategories()

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
        if(!searchquery.isEmpty()){
            startActivity(Intent(requireContext(), BrowseMarketFragment::class.java).apply {
                putExtra("CategoryDesc", "")
                putExtra("SearchQuery", searchquery)
            })
        }
    }







    fun getAllCategories() {
        HelpFunctions.startProgressBar(this.requireActivity())


        val malqaa = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<AllCategoriesResponseBack> = malqaa.getAllCategories()

        call.enqueue(object : Callback<AllCategoriesResponseBack> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<AllCategoriesResponseBack>,
                response: Response<AllCategoriesResponseBack>
            ) {

                if (response.isSuccessful) {

                    if (response.body() != null) {

                        val resp: AllCategoriesResponseBack = response.body()!!
                        allCategoryList = resp.data
                        if (allCategoryList.count() > 0) {
                            ConstantObjects.categoryList=allCategoryList

                            all_categories_recycler.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                            all_categories_recycler.adapter =
                                AdapterAllCategories(
                                    allCategoryList,
                                    requireContext(),
                                    this@HomeFragment
                                )

                            getAllAdsData()


                        } else {
                            HelpFunctions.ShowLongToast(
                                getString(R.string.NoCategoriesfound),
                                context
                            )
                            HelpFunctions.dismissProgressBar()

                        }
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
                            val list: ArrayList<DynamicList> = ArrayList()

                            list.add(
                                DynamicList(
                                    getString(R.string.vehicles),
                                    R.drawable.ic_vechile,
                                    caradvetisement
                                )
                            )
                            list.add(
                                DynamicList(
                                    getString(R.string.electronics),
                                    R.drawable.ic_electronic,
                                    generaladvetisement
                                )
                            )
                            list.add(
                                DynamicList(
                                    getString(R.string.real_estate),
                                    R.drawable.ic_real_estate,
                                    propertyadvetisement
                                )
                            )
                            list.add(
                                DynamicList(
                                    getString(R.string.List_Auctions),
                                    R.drawable.ic_vechile,
                                    recentadvetisement,
                                    "list",
                                    getString(R.string.List_Auctions_detail)
                                )
                            )


                            setHomeAdaptor(list)

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



    private fun setHomeAdaptor(list: List<DynamicList>) {
        dynamic_product_rcv.adapter = object : GenericListAdapter<DynamicList>(
            R.layout.parenet_category_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        when(typeName){
                            "category"->{
                                category_type!!.show()
                                product_list_layout!!.hide()
                            }
                            "list"->{
                                category_type!!.hide()
                                product_list_layout!!.show()
                            }
                        }
                        detail_tv!!.text=detail
                        category_name_tv!!.text=category_name
                        category_name_tv_2!!.text=category_name
                        category_icon_iv!!.setImageResource(category_icon)

                        GenericAdaptor().  setHomeProductAdaptor(product,product_rcv)
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

    override fun OnItemClick(position: Int) {
        super.OnItemClick(position)


        startActivity(Intent(requireContext(), BrowseMarketFragment::class.java).apply {
            putExtra("CategoryDesc", allCategoryList[position].categoryName)
            putExtra("SearchQuery", "")
        })
    }

    //Methods For language
    private fun setLocate(Lang: String) {

        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        requireActivity().baseContext.resources.updateConfiguration(
            config,
            requireActivity().baseContext.resources.displayMetrics
        )

        val editor = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }

    private fun loadLocate() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocate(language)
        }
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






