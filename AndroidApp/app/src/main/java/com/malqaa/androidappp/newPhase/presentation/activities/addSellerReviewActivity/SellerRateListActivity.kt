package com.malqaa.androidappp.newPhase.presentation.activities.addSellerReviewActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySellerRateListBinding
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.adapter.SellerRateAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class SellerRateListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    lateinit var binding: ActivitySellerRateListBinding

    var providerId: String = ""
    var businessAccountId: String = ""
    lateinit var sellerRateList: ArrayList<SellerRateItem>
    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    lateinit var sellerRateAdapter: SellerRateAdapter
    var addSellerReviewRequestCode = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init binding
        binding = ActivitySellerRateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.sellerRate)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
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
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.floatingActionButtonBottm.hide()
        binding.floatingActionButtonBottm.setOnClickListener {
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
        sellerRateList = ArrayList()
        sellerRateAdapter = SellerRateAdapter(this, sellerRateList)
        binding.rvAllReviews.apply {
            adapter = sellerRateAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
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
                    binding.tvError.hide()
                }
            } else {
                showErrorText(sellerRateListResp.message)
            }
        }


    }

    private fun showErrorText(string: String) {
        binding.tvError.text = string
        binding.tvError.show()
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        sellerRateList.clear()
        sellerRateAdapter.notifyDataSetChanged()
        productDetialsViewModel.getSellerRates(providerId, businessAccountId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == addSellerReviewRequestCode) {
            var editRate = data?.getBooleanExtra(ConstantObjects.editRateKey, false)
            if (editRate == true) {
            } else {
                productDetialsViewModel.getSellerRates(providerId, businessAccountId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetialsViewModel.closeAllCall()
    }
}