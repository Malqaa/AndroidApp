package com.malka.androidappp.newPhase.presentation.addProductReviewActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateItem
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.product_reviews1.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductReviewsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {


    val list: ArrayList<Reviewmodel> = ArrayList()
    var addReviewRequestrCode = 1000
    lateinit var reviewsadapter: RateAdapter
    lateinit var mainRatesList: ArrayList<RateReviewItem>
    var productId = 0
    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_reviews1)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, 0)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setReviewsAdapter()
        toolbar_title.text = getString(R.string.reviews)
        setProductDetailsViewModel()
        onRefresh()
        back_btn.setOnClickListener {
            finish()
        }
        floatingActionButtonBottm.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivityForResult(Intent(this, AddRateProductActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                }, addReviewRequestrCode)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        //getRates()
    }

    private fun setProductDetailsViewModel() {
        productDetialsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetialsViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                showErrorText(getString(R.string.connectionError))
            } else {
                showErrorText(getString(R.string.serverError))
            }

        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
            if(it.status!=null && it.status=="409"){
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            }else {
                if (it.message != null) {
                    showErrorText(it.message!!)
                } else {
                    showErrorText(getString(R.string.serverError))
                }
            }
        }
        productDetialsViewModel.getRateResponseObservable.observe(this) { rateListResp ->
            // println("hhhh "+rateListResp.status_code+" "+rateListResp.data)
            if (rateListResp.status_code == 200) {
                mainRatesList.clear()

                mainRatesList.addAll(rateListResp.data.rateProductListDto)
                reviewsadapter.notifyDataSetChanged()
                if (mainRatesList.isEmpty()) {
                    showErrorText(getString(R.string.no_Reviews_Found))
                } else {
                    tvError.hide()
                }
            } else {
                showErrorText(rateListResp.message)
            }
        }


    }

    private fun showErrorText(string: String) {
        tvError.text = string
        tvError.show()
    }

    private fun setReviewsAdapter() {
        mainRatesList = ArrayList()
        reviewsadapter = RateAdapter(this, mainRatesList)
        rvAllReviews.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = reviewsadapter
        }
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        mainRatesList.clear()
        reviewsadapter.notifyDataSetChanged()
        productDetialsViewModel.getProductRatesForActivity(productId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == addReviewRequestrCode) {
            var addRateItem: AddRateItem? = data?.getParcelableExtra(ConstantObjects.rateObjectKey)
            var editRate = data?.getBooleanExtra(ConstantObjects.editRateKey, false)
            if (editRate == true) {
                addRateItem?.let {
                    searchForEditRate(addRateItem)
                }

            } else {
                productDetialsViewModel.getProductRatesForProductDetails(productId)
            }

        }

    }

    private fun searchForEditRate(addRateItem: AddRateItem) {
        lifecycleScope.launch(Dispatchers.IO) {
            var selectedIndex = -1
            for ((index, element) in mainRatesList.withIndex()) {
                if (element.id == addRateItem.id) {
                    selectedIndex = index
                    element.rate = addRateItem.rate
                    element.comment = addRateItem.comment
                    break
                }
            }
            withContext(Dispatchers.Main) {
                if (selectedIndex != -1)
                    reviewsadapter.notifyItemChanged(selectedIndex)
            }

        }
    }

//        private fun getRates() {
//            val apiBuilder = getRetrofitBuilder()
//            val Rates = apiBuilder.getRates(15)
//            Rates.enqueue(object : Callback<RateResponse1> {
//                override fun onResponse(
//                    call: Call<RateResponse1>,
//                    response: Response<RateResponse1>
//                ) {
//                    val rates = response.body()
//                    if (rates != null) {
//                        Log.d("Api", rates.toString())
//                        val reviewList = findViewById<RecyclerView>(R.id.rvPakat)
//                        adapter = RateAdapter(this@ProductReviewsActivity, rates.data)
//                        reviewList.adapter = adapter
//                        reviewList.layoutManager = LinearLayoutManager(this@ProductReviewsActivity)
//
//
//                    }
//
//                }
//
//                override fun onFailure(call: Call<RateResponse1>, t: Throwable) {
//                    Log.d("Api", "Error in fetching rates", t)
//                }
//
//            })
//
//        }
}


