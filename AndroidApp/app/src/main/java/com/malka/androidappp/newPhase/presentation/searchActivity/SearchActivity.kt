package com.malka.androidappp.newPhase.presentation.searchActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.presentation.homeScreen.viewModel.HomeViewModel
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), SearchProductAdapter.OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var searchQuery:String?=null
    private lateinit var viewManagerProduct:GridLayoutManager
    private lateinit var searchAdapter:SearchProductAdapter
    private lateinit var productArrayList:ArrayList<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchQuery=intent.getStringExtra(ConstantObjects.searchQueryKey)
        setupSaerchListAdapter()
        setupSearchViewModel()
        if(searchQuery!=null) {
            homeViewModel.doSearch(mapOf("productName" to searchQuery!!,))
        }
        setClickListeners()

    }

    private fun setClickListeners() {
        fbButtonBack.setOnClickListener {
            onBackPressed()
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                homeViewModel.doSearch(mapOf("productName" to p0.toString()!!,))
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        ivSearch.setOnClickListener {
            if(etSearch.text.toString().trim()!=""){
                homeViewModel.doSearch(mapOf("productName" to etSearch.text.toString().trim()))
            }else{
                etSearch.error = getString(R.string.want_to_search_for_a_commodity_or_an_auction)
            }
        }
    }

    private fun setupSaerchListAdapter() {
        productArrayList = ArrayList()
        viewManagerProduct = GridLayoutManager(this, 2)
        searchAdapter= SearchProductAdapter(productArrayList,this,this)
        recyclerProduct.apply {
            layoutManager=viewManagerProduct
            adapter=searchAdapter
        }
    }

    private fun setupSearchViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.isLoading.observe(this, Observer {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        })
        homeViewModel.isNetworkFail.observe(this, Observer {
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

        })
        homeViewModel.errorResponseObserver.observe(this, Observer {
//            if (it.message != null) {
//                HelpFunctions.ShowLongToast(
//                    it.message,
//                    this
//                )
//            } else {
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.serverError),
//                    this
//                )
//            }

        })
        homeViewModel.searchObserver.observe(this){ searchResp->
            if(searchResp!=null){
                if(searchResp.status_code==200) {
                    val list: ArrayList<Product> = Gson().fromJson(Gson().toJson(searchResp.data), object : TypeToken<ArrayList<Product>>() {}.type)
                    productArrayList.clear()
                    productArrayList.addAll(list)
                    searchAdapter.notifyDataSetChanged()
                }
            }

        }


    }

    override fun onSelectItem(position: Int) {

    }

    override fun addToWishList(position: Int) {
        if(Paper.book().read<Boolean>(SharedPreferencesStaticClass.islogin) == true){
            HelpFunctions.ShowLongToast("adding to wish list not implemented yet",this)
        }else{
            startActivity( Intent(this, SignInActivity::class.java).apply {})
        }
    }

}