package com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ProductReviews1Binding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addRateResp.AddRateItem
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductReviewsActivity : BaseActivity<ProductReviews1Binding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val list: ArrayList<Reviewmodel> = ArrayList()
    private var addReviewRequestrCode = 1000
    lateinit var reviewsadapter: RateAdapter
    lateinit var mainRatesList: ArrayList<RateReviewItem>
    var productId = 0
    private lateinit var productDetialsViewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ProductReviews1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra(ConstantObjects.productIdKey, 0)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setReviewsAdapter()
        binding.toolbarMain.toolbarTitle.text = getString(R.string.reviews)
        setProductDetailsViewModel()
        onRefresh()
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.floatingActionButtonBottm.setOnClickListener {
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
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                showErrorText(getString(R.string.connectionError))
            } else {
                showErrorText(getString(R.string.serverError))
            }

        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    showErrorText(it.message!!)
                } else if (it.message2 != null) {
                    showErrorText(it.message2!!)
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
                    binding.tvError.hide()
                }
            } else {
                showErrorText(rateListResp.message)
            }
        }


    }

    private fun showErrorText(string: String) {
        binding.tvError.text = string
        binding.tvError.show()
    }

    private fun setReviewsAdapter() {
        mainRatesList = ArrayList()
        reviewsadapter = RateAdapter(this, mainRatesList)
        binding.rvAllReviews.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = reviewsadapter
        }
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
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
