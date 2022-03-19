package com.malka.androidappp.botmnav_fragments.home.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.speech.RecognizerIntent
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.adapter.CarAdvertisementAdapter
import com.malka.androidappp.botmnav_fragments.home.adapter.ParentCategoryAdaptor
import com.malka.androidappp.botmnav_fragments.home.adapter.PropertyAdvertisementAdapter
import com.malka.androidappp.botmnav_fragments.home.adapter.RecentAdvertisementAdapter
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.botmnav_fragments.home.model.DynamicList
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.design.CartActivity
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.viewpager2.AutoScrollViewPager
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.home.GetAllAds
import com.malka.androidappp.servicemodels.home.favouritecars
import com.malka.androidappp.servicemodels.home.favouriteproperties
import com.malka.androidappp.servicemodels.home.recentlisting
import com.malka.myapplication.Model
import com.malka.myapplication.PostsAdapter
import kotlinx.android.synthetic.main.fragment_homee.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_homee), PostsAdapter.OnPostItemClickListener,
    AdapterAllCategories.OnItemClickListener {
    private var dotscount = 0
    var dots: ArrayList<ImageView> = arrayListOf()
    var sliderlist: ArrayList<Int> = ArrayList()
    var slider_home_: AutoScrollViewPager? = null

    private class SimpleSuggestions : SearchSuggestion {
        private val mData: String

        constructor(string: String) {
            mData = string
        }

        override fun getBody(): String {
            return mData
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(mData)
        }

        private constructor(`in`: Parcel) {
            mData = `in`.readString()!!
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SimpleSuggestions?> =
                object : Parcelable.Creator<SimpleSuggestions?> {
                    override fun createFromParcel(`in`: Parcel): SimpleSuggestions? {
                        return SimpleSuggestions(`in`)
                    }

                    override fun newArray(size: Int): Array<SimpleSuggestions?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    var allCategoryList: List<AllCategoriesModel> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sliderlist.add(R.drawable.slider_demo)
        sliderlist.add(R.drawable.slider_demo)
        sliderlist.add(R.drawable.slider_demo)

        loadLocate()


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity!!.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val mSearchView = floating_search_view


        mSearchView.setOnQueryChangeListener { oldQuery, newQuery ->
            val list: MutableList<SearchSuggestion> = ArrayList()
            //emulating search on dummy data
            val listdata = listOf("One", "Two", "Three", "Four")
            for (item in listdata) {
                if (item.contains(newQuery)) {
                    list.add(SimpleSuggestions(item))
                }
            }
            mSearchView.swapSuggestions(list)
        }


        // Search Mic
        mSearchView.setOnMenuItemClickListener(object : FloatingSearchView.OnMenuItemClickListener {
            fun onMenuItemSelected(item: MenuItem?) {}
            override fun onActionMenuItemSelected(item: MenuItem?) {
                if (item != null) {
                    if (item.itemId == R.id.action_voice_rec) {
                        speak()
                    } else if (item.itemId == R.id.action_search) {
                        SearchAndNavigateToCategoryListing(mSearchView.query.toString())
                    }
                }
            }
        })

        //Zack
        //Date: 03/14/2021
        //If user has logged in
        if (ConstantObjects.logged_userid.trim().length > 0) {
            //Zaack
            //Date: 10/29/2020
               HelpFunctions.GetUserWatchlist();
//            HelpFunctions.GetUserFavourites(this@HomeFragment);
//            HelpFunctions.GetUsersCartList(this@HomeFragment);
//            HelpFunctions.GetUserCreditCards(this@HomeFragment);
//            HelpFunctions.GetUserShippingAddress(this@HomeFragment);
        }

//        BindFavouriteCarData()
//        BindGeneralAdsData()
//        BindFavouritePropertyData()
//        BindRecentAdsData()
//        BindTotalVisitCountData()

//        homecat_motor.setOnClickListener() {
//            NavigateToCategoryListing("Car");
//        }
//        homecat_property.setOnClickListener() {
//            NavigateToCategoryListing("LSOWWFASAR");
//        }
//        homecat_zi.setOnClickListener() {
//            NavigateToCategoryListing("General");
//        }
//        homecat_x.setOnClickListener() {
//            NavigateToCategoryListing("General");
//        }
//
//        homecat_zb.setOnClickListener() {
//            NavigateToCategoryListing("General");
//        }
//
//        homecat_zj.setOnClickListener() {
//            findNavController().navigate(R.id.home_viewallcategories)
//        }

        switchtofeatrcat.setOnClickListener {
            findNavController().navigate(R.id.home_seeall_generalfeaturedads)
        }

        ic_cart.setOnClickListener {
            val  intent = Intent(requireActivity(), CartActivity::class.java)
            startActivity(intent)

        }

        getAllCategories()

        setPagerDots(sliderlist)
    }

    //Zaack
    //Date: 10/29/2020
    fun NavigateToCategoryListing(category: String) {
        val args = Bundle()
        args.putString("CategoryDesc", category);
        args.putString("SearchQuery", "");
        findNavController().navigate(R.id.home_browsemarket, args)
    }

    fun SearchAndNavigateToCategoryListing(searchquery: String) {
        val args = Bundle()
        args.putString("CategoryDesc", "");
        args.putString("SearchQuery", searchquery);
        findNavController().navigate(R.id.home_browsemarket, args)
    }

    override fun onItemClick(item: Model, position: Int) {
        findNavController().navigate(R.id.home_carspec)
    }

    fun BindFavouriteCarData() {
        var recyclerCarfeatured = reyclerviewcarfeature
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetFeaturedMotorsAds()
            val call: Call<favouritecars> = malqa.GetFeaturedMotorsAds()
            try {
                call.enqueue(object : Callback<favouritecars> {
                    override fun onResponse(
                        call: Call<favouritecars>,
                        response: Response<favouritecars>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val _feat_cars: favouritecars = response.body()!!;
                                if (_feat_cars != null && _feat_cars.data != null && _feat_cars.data.size > 0) {
                                    recyclerCarfeatured.layoutManager =
                                        LinearLayoutManager(
                                            activity,
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    var genadpt: CarAdvertisementAdapter =
                                        CarAdvertisementAdapter(_feat_cars.data)
                                    recyclerCarfeatured.adapter = genadpt
                                    genadpt.onItemClick = { indobj ->
                                        HelpFunctions.ViewAdvertismentDetail(
                                            indobj.referenceId,
                                            indobj.template,
                                            requireContext(),HomeFragment()
                                        )
                                        SharedPreferencesStaticClass.ad_userid = indobj.user
                                    }
                                } else {
                                    recyclerCarfeatured.adapter = null
                                }
                            } else {
                                recyclerCarfeatured.adapter = null
                            }
                        } else {
                            recyclerCarfeatured.adapter = null
                        }
                    }

                    override fun onFailure(call: Call<favouritecars>, t: Throwable) {
                        recyclerCarfeatured.adapter = null
                        //HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
                    }
                })
            } catch (e: Exception) {
                recyclerCarfeatured.adapter = null
                HelpFunctions.ShowLongToast(
                    getString(R.string.Somethingwentwrong),
                    requireActivity()
                )
            }

        } catch (ex: Exception) {
            recyclerCarfeatured.adapter = null
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindFavouritePropertyData() {
        var recyclerProperty = reyclerviewfeatureprop
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetFeaturedPropertyAds()
            val call: Call<favouriteproperties> = malqa.GetFeaturedPropertyAds()
            try {
                call.enqueue(object : Callback<favouriteproperties> {
                    override fun onResponse(
                        call: Call<favouriteproperties>,
                        response: Response<favouriteproperties>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val _feat_prop: favouriteproperties = response.body()!!;
                                if (_feat_prop != null && _feat_prop.data != null && _feat_prop.data.size > 0) {
                                    recyclerProperty.layoutManager = LinearLayoutManager(
                                        activity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    var genadpt: PropertyAdvertisementAdapter =
                                        PropertyAdvertisementAdapter(_feat_prop.data)
                                    recyclerProperty.adapter = genadpt
                                    genadpt.onItemClick = { indobj ->
                                        HelpFunctions.ViewAdvertismentDetail(
                                            indobj.referenceId,
                                            indobj.template,
                                            requireContext(),HomeFragment()
                                        )
                                        SharedPreferencesStaticClass.ad_userid = indobj.user
                                    }
                                } else {
                                    recyclerProperty.adapter = null
                                }
                            } else {
                                recyclerProperty.adapter = null
                            }
                        } else {
                            recyclerProperty.adapter = null
                        }
                    }

                    override fun onFailure(call: Call<favouriteproperties>, t: Throwable) {
                        recyclerProperty.adapter = null
                        //HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
                    }
                })
            } catch (e: Exception) {
                recyclerProperty.adapter = null
                HelpFunctions.ShowLongToast(
                    getString(R.string.Somethingwentwrong),
                    requireActivity()
                )
            }

        } catch (ex: Exception) {
            recyclerProperty.adapter = null
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindRecentAdsData() {
        var reyclerviewrecent = reyclerviewrecentads
        try {

            val malqa: MalqaApiService = RetrofitBuilder.GetRecentAds()
            val call: Call<recentlisting> = malqa.GetRecentAds()
            try {
                call.enqueue(object : Callback<recentlisting> {
                    override fun onResponse(
                        call: Call<recentlisting>,
                        response: Response<recentlisting>
                    ) {

                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val _recent: recentlisting = response.body()!!;
                                if (_recent != null && _recent.data != null && _recent.data.size > 0) {
                                    var genadpt: RecentAdvertisementAdapter =
                                        RecentAdvertisementAdapter(_recent.data)

                                    reyclerviewrecent.layoutManager =
                                        LinearLayoutManager(
                                            activity,
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    reyclerviewrecent.adapter = genadpt
                                    genadpt.onItemClick = { indobj ->
                                        HelpFunctions.ViewAdvertismentDetail(
                                            indobj.referenceId,
                                            indobj.template,
                                            requireContext(),HomeFragment()
                                        )
                                        SharedPreferencesStaticClass.ad_userid = indobj.user
                                    }
                                }
                            } else {
                                reyclerviewrecent.adapter = null
                            }
                        } else {
                            reyclerviewrecent.adapter = null
                        }
                    }

                    override fun onFailure(call: Call<recentlisting>, t: Throwable) {
                        reyclerviewrecent.adapter = null
                        //HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
                    }
                })
            } catch (e: Exception) {
                reyclerviewrecent.adapter = null
                HelpFunctions.ShowLongToast(
                    getString(R.string.Somethingwentwrong),
                    requireActivity()
                )
            }

        } catch (ex: Exception) {
            reyclerviewrecent.adapter = null
            HelpFunctions.ReportError(ex)
        }
    }

//    fun BindGeneralAdsData() {
//        var reyclerviewgeneral = reyclerviewgeneralads
//        try {
//            val malqa: MalqaApiService = RetrofitBuilder.GetGeneralAds()
//            val call: Call<generalads> = malqa.GetGeneralAds()
//            try {
//                call.enqueue(object : Callback<generalads> {
//                    override fun onResponse(
//                        call: Call<generalads>,
//                        response: Response<generalads>
//                    ) {
//                        if (response.isSuccessful) {
//                            if (response.body() != null) {
//                                val _generals: generalads = response.body()!!;
//                                if (_generals != null && _generals.data != null && _generals.data.size > 0) {
//                                    var genadpt =
//                                        GeneralAdvertisementAdapter(_generals.data)
//
//                                    reyclerviewgeneral.layoutManager =
//                                        LinearLayoutManager(
//                                            activity,
//                                            LinearLayoutManager.HORIZONTAL,
//                                            false
//                                        )
//                                    reyclerviewgeneral.adapter = genadpt
//                                    genadpt.onItemClick = { indobj ->
//                                        HelpFunctions.ViewAdvertismentDetail(
//                                            indobj.referenceId,
//                                            indobj.template,
//                                            this@HomeFragment
//                                        )
//                                        SharedPreferencesStaticClass.ad_userid = indobj.user
//                                    }
//                                }
//                            } else {
//                                reyclerviewgeneral.adapter = null
//                            }
//                        } else {
//                            reyclerviewgeneral.adapter = null
//                        }
//                    }
//
//                    override fun onFailure(call: Call<generalads>, t: Throwable) {
//                        reyclerviewgeneral.adapter = null
//                        //HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
//                    }
//                })
//            } catch (e: Exception) {
//                reyclerviewgeneral.adapter = null
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.Somethingwentwrong),
//                    requireActivity()
//                )
//            }
//
//        } catch (ex: Exception) {
//            reyclerviewgeneral.adapter = null
//            HelpFunctions.ReportError(ex)
//        }
//    }
//
//    fun BindTotalVisitCountData() {
//        val wormDotsIndicator = piechart_dots_indicator
//        val viewPagerr = viewpagerpiechart
//        val adapterr = PiechartUserNoAdapter(requireActivity())
//
//        try {
//            //
//            val malqa: MalqaApiService = RetrofitBuilder.GetTotalVisitCount()
//            val call: Call<visit_count_object> = malqa.GetTotalVisitCount()
//            try {
//                call.enqueue(object : Callback<visit_count_object> {
//                    override fun onResponse(
//                        call: Call<visit_count_object>,
//                        response: Response<visit_count_object>
//                    ) {
//                        if (response.isSuccessful) {
//                            if (response.body() != null) {
//                                val _visitcount: visit_count_object = response.body()!!;
//                                var visiter_chart: PieChartFrag1 = PieChartFrag1()
//
//                                var lbl_title: String = "Total Visit Count"
//                                var visit_count = 0
//
//                                if (_visitcount != null && _visitcount.data != null) {
//                                    visit_count = _visitcount.data.totalVisitortillNow
//                                    lbl_title = getString(R.string.Visitorstillnow)
//                                } else {
//                                    lbl_title = getString(R.string.ServiceError)
//                                }
//                                PieChartFrag1.lbl_legend_text = lbl_title
//                                if (visit_count > 999) {
//                                    var count = visit_count.toDouble() / 1000
//                                    val rounded = "%.1f".format(count)
//                                    PieChartFrag1.lbl_count = rounded + "K"
//                                } else {
//                                    PieChartFrag1.lbl_count = visit_count.toString()
//                                }
//
//                                adapterr.addFragment(visiter_chart, lbl_title)
//                                adapterr.addFragment(PieChartFrag2(), "Fragment 2")
//                                adapterr.addFragment(PieChartFrag3(), "Fragment 3")
//                                adapterr.addFragment(PieChartFrag4(), "Fragment 4")
//                                viewPagerr.adapter = adapterr
//                                wormDotsIndicator.setViewPager2(viewPagerr)
//                                //visiter_chart.BindValues(visit_count, lbl_title)
//                                HelpFunctions.dismissProgressBar()
//                            } else {
//                                HelpFunctions.dismissProgressBar()
//                                viewPagerr.adapter = null
//                            }
//                        } else {
//                            HelpFunctions.dismissProgressBar()
//                            viewPagerr.adapter = null
//                        }
//                    }
//
//                    override fun onFailure(call: Call<visit_count_object>, t: Throwable) {
//                        HelpFunctions.dismissProgressBar()
//                        viewPagerr.adapter = null
//                        //HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
//                    }
//                })
//            } catch (e: Exception) {
//                viewPagerr.adapter = null
//                //HelpFunctions.ShowLongToast("Something Went Wrong.Please Try Again Later ", requireActivity())
//            }
//
//        } catch (ex: Exception) {
//            viewPagerr.adapter = null
//            HelpFunctions.ReportError(ex)
//        }
//    }

    // Functions to handle Search Mic events
    val REQUEST_CODE_SPEECH_INPUT = 100
    fun speak() {
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.Hisaysomething))

        try {
            startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(this.requireActivity(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    floating_search_view.setSearchText(result?.get(0))
                }
            }
        }
    }

    fun getAllCategories() {
        HelpFunctions.startProgressBar(this.requireActivity())


        val malqaa = RetrofitBuilder.GetRetrofitBuilder2()
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

        val malqa: MalqaApiService = RetrofitBuilder.GetAllAds()
        val call: Call<GetAllAds> = malqa.GetAllAds()
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

                            val genadpt = ParentCategoryAdaptor(list, this@HomeFragment,requireContext())


                            dynamic_product_rcv.adapter = genadpt
                            genadpt.onItemClick = { indobj ->
                                HelpFunctions.ViewAdvertismentDetail(
                                    indobj.referenceId,
                                    indobj.template,
                                    requireContext(),HomeFragment()
                                )
                                SharedPreferencesStaticClass.ad_userid = indobj.user
                            }
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

    override fun OnItemClick(position: Int) {
        super.OnItemClick(position)

        allCategoryList[position].categoryName?.let {
            NavigateToCategoryListing(it)
        }
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

//    private var mLangReceiver: BroadcastReceiver? = null
//    fun setupLangReceiver(): BroadcastReceiver? {
//        if (mLangReceiver == null) {
//            mLangReceiver = object : BroadcastReceiver() {
//                override fun onReceive(context: Context, intent: Intent) {
//                    requireActivity().recreate()
//                }
//            }
//
//            requireActivity().registerReceiver(
//                mLangReceiver,
//                IntentFilter(Intent.ACTION_LOCALE_CHANGED)
//            )
//        }
//        return mLangReceiver
//    }


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






