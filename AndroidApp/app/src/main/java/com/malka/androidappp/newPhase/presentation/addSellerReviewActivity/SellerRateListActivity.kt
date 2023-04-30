package com.malka.androidappp.newPhase.presentation.addSellerReviewActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.addRateResp.AddRateItem
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.adapter.SellerRateAdapter
import kotlinx.android.synthetic.main.activity_seller_rate_list.progressBar
import kotlinx.android.synthetic.main.activity_seller_rate_list.rvAllReviews
import kotlinx.android.synthetic.main.activity_seller_rate_list.swipe_to_refresh
import kotlinx.android.synthetic.main.activity_seller_rate_list.tvError
import kotlinx.android.synthetic.main.product_reviews1.*
import kotlinx.android.synthetic.main.toolbar_main.*

class SellerRateListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    var providerId: String = ""
    var businessAccountId: String = ""
    lateinit var sellerRateList: ArrayList<SellerRateItem>
    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    lateinit var sellerRateAdapter: SellerRateAdapter
    var addSellerReviewRequestCode = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_rate_list)
        toolbar_title.text = getString(R.string.sellerRate)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        if (intent.getStringExtra(ConstantObjects.providerIdKey) != null) {
            providerId = intent.getStringExtra(ConstantObjects.providerIdKey)!!
        }
        if (intent.getStringExtra(ConstantObjects.businessAccountIdKey) != null) {
            businessAccountId = intent.getStringExtra(ConstantObjects.businessAccountIdKey)!!
        }
        setSellerRatesAdapter()
        setProductDetailsViewModel()
        onRefresh()
        println("hhhh $providerId $businessAccountId")
        //**view click Listeners***/
        back_btn.setOnClickListener {
            finish()
        }
        floatingActionButtonBottm.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivityForResult(Intent(this, AddRateSellerActivity::class.java).apply {
                    putExtra(ConstantObjects.providerIdKey, providerId)
                    putExtra(ConstantObjects.businessAccountIdKey, businessAccountId)

                }, addSellerReviewRequestCode)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }

    private fun setSellerRatesAdapter() {
        sellerRateList= ArrayList()
        sellerRateAdapter = SellerRateAdapter(this,sellerRateList)
        rvAllReviews.apply {
            adapter = sellerRateAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
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
            if (it.message != null) {
                showErrorText(it.message!!)
            } else {
                showErrorText(getString(R.string.serverError))
            }

        }
        productDetialsViewModel.sellerRateListObservable.observe(this) { sellerRateListResp ->
            // println("hhhh "+rateListResp.status_code+" "+rateListResp.data)
            if (sellerRateListResp.status_code == 200) {
                sellerRateList.clear()
                sellerRateListResp.SellerRateObject?.rateSellerListDto?.let {
                    sellerRateList.addAll(
                        it
                    )
                }
                sellerRateAdapter.notifyDataSetChanged()
                if (sellerRateList.isEmpty()) {
                    showErrorText(getString(R.string.no_Reviews_Found))
                } else {
                    tvError.hide()
                }
            } else {
                showErrorText(sellerRateListResp.message)
            }
        }


    }

    private fun showErrorText(string: String) {
        tvError.text = string
        tvError.show()
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        sellerRateList.clear()
        sellerRateAdapter.notifyDataSetChanged()
        productDetialsViewModel.getSellerRates(providerId, businessAccountId)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == addSellerReviewRequestCode) {
            var addRateItem: AddRateItem? = data?.getParcelableExtra(ConstantObjects.rateObjectKey)
            var editRate = data?.getBooleanExtra(ConstantObjects.editRateKey, false)
            if (editRate == true) {
//                addRateItem?.let {
//                    searchForEditRate(addRateItem)
//                }

            } else {
                productDetialsViewModel.getSellerRates(providerId,businessAccountId)
            }
        }

    }
}