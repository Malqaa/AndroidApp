package com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.addRateResp.AddRateItem
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.product_reviews1.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductReviewsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {


    private val list: ArrayList<Reviewmodel> = ArrayList()
    private var addReviewRequestrCode = 1000
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
            val addRateItem: AddRateItem? = data?.getParcelableExtra(ConstantObjects.rateObjectKey)
            val editRate = data?.getBooleanExtra(ConstantObjects.editRateKey, false)
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
    override fun onDestroy() {
        super.onDestroy()
        productDetialsViewModel.closeAllCall()
    }
}


