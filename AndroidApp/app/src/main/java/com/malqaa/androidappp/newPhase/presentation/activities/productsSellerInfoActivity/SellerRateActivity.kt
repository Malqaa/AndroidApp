package com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySellerRateBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.sellerInfoResp.SellerInformation
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.adapter.SellerRateAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.dialog.SellerFilterReviewDialog
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class SellerRateActivity : BaseActivity<ActivitySellerRateBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SellerFilterReviewDialog.ApplySellerReviewFilter {
    private lateinit var linerlayoutManager: LinearLayoutManager
    val list: ArrayList<Reviewmodel> = ArrayList()
    val sampleOption: ArrayList<Selection> = ArrayList()
    val typeOption: ArrayList<Selection> = ArrayList()
    var selection: Selection? = null
    private lateinit var productDetialsViewModel: ProductDetailsViewModel

    //=====
    lateinit var sellerRateAdapter: SellerRateAdapter
    lateinit var sellerRateList: ArrayList<SellerRateItem>
    var sellerInformation: SellerInformation? = null
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    lateinit var sellerFilterReviewDialog: SellerFilterReviewDialog
    var sellerAsASeller: Int = SellerFilterReviewDialog.sellerAsASeller
    var retReviewType: Int = SellerFilterReviewDialog.allReview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySellerRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sellerInformation = intent.getParcelableExtra(ConstantObjects.sellerObjectKey)
        initView()
        setListener()
        setSellerRateAdapter()
        setProductDetailsViewModel()
        onRefresh()
    }

    private fun setProductDetailsViewModel() {
        productDetialsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetialsViewModel.isLoading.observe(this) {
            if (it)
                binding.progressbar.show()
            else
                binding.progressbar.hide()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast((it.message!!), this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }
        productDetialsViewModel.sellerRateListObservable.observe(this) { sellerRateListResp ->
            if (sellerRateListResp.status_code == 200) {

                if (sellerAsASeller == SellerFilterReviewDialog.sellerAsAll) {
                    sellerRateListResp.SellerRateObject?.let {
                        it.rateSellerListDto?.let { it1 -> sellerRateList.addAll(it1) }
                    }
                    sellerRateListResp.SellerRateObject?.let {
                        it.rateBuyerListDto?.let { it1 -> sellerRateList.addAll(it1) }
                    }
                } else if (sellerAsASeller == SellerFilterReviewDialog.sellerAsASeller) {
                    sellerRateListResp.SellerRateObject?.let {
                        it.rateSellerListDto?.let { it1 -> sellerRateList.addAll(it1) }
                    }
                } else {
                    sellerRateListResp.SellerRateObject?.let {
                        it.rateBuyerListDto?.let { it1 -> sellerRateList.addAll(it1) }
                    }
                }

                Log.i("test #1", "sellerRateList size: ${sellerRateList.size}")

                sellerRateAdapter.notifyDataSetChanged()
                if (sellerRateList.isEmpty()) {
                    binding.tvError.show()
                } else {
                    binding.tvError.hide()
                }
            } else {
                if (sellerRateList.isEmpty()) {
                    if (sellerRateListResp.message != null) {
                        HelpFunctions.ShowLongToast((sellerRateListResp.message), this)
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                    }
                } else {
                    binding.tvError.hide()
                }
            }
            //tvError
        }
    }

    private fun initView() {
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        binding.toolbarMain.toolbarTitle.text = getString(R.string.all_reviews)
        sellerFilterReviewDialog = SellerFilterReviewDialog(this, this)
    }

    private fun setSellerRateAdapter() {
        sellerRateList = ArrayList()
        sellerRateAdapter = SellerRateAdapter(this, sellerRateList);
        linerlayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvRate.apply {
            adapter = sellerRateAdapter
            layoutManager = linerlayoutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linerlayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {

                    if (sellerAsASeller == SellerFilterReviewDialog.sellerAsAll) {
                        var sendRate: Int? = null
                        if (retReviewType != SellerFilterReviewDialog.allReview) {
                            sendRate = retReviewType
                        }
                        productDetialsViewModel.getSellerRates2AsSeller(
                            sellerInformation?.providerId ?: "",
                            sellerInformation?.businessAccountId,
                            page,
                            sendRate
                        )
                        productDetialsViewModel.getSellerRates2AsAbuyer(
                            sellerInformation?.providerId ?: "",
                            sellerInformation?.businessAccountId,
                            page,
                            sendRate
                        )
                        Log.i("test #1", "productDetialsViewModel: sellerAsAll")
                    } else if (sellerAsASeller == SellerFilterReviewDialog.sellerAsASeller) {
                        var sendRate: Int? = null
                        if (retReviewType != SellerFilterReviewDialog.allReview) {
                            sendRate = retReviewType
                        }
                        productDetialsViewModel.getSellerRates2AsSeller(
                            sellerInformation?.providerId ?: "",
                            sellerInformation?.businessAccountId,
                            page,
                            sendRate
                        )
                        Log.i("test #1", "productDetialsViewModel: sellerAsASeller")
                    } else {
                        var sendRate: Int? = null
                        if (retReviewType != SellerFilterReviewDialog.allReview) {
                            sendRate = retReviewType
                        }
                        productDetialsViewModel.getSellerRates2AsAbuyer(
                            sellerInformation?.providerId ?: "",
                            sellerInformation?.businessAccountId,
                            page,
                            sendRate
                        )
                        Log.i("test #1", "productDetialsViewModel: sellerAsABuyer")
                    }
                }
            }
        binding.rvRate.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    override fun onRefresh() {
        endlessRecyclerViewScrollListener.resetState()
        binding.swipeToRefresh.isRefreshing = false
        sellerRateList.clear()
        sellerRateAdapter.notifyDataSetChanged()
        binding.tvError.hide()

        if (sellerAsASeller == SellerFilterReviewDialog.sellerAsAll) {
            var sendRate: Int? = null
            if (retReviewType != SellerFilterReviewDialog.allReview) {
                sendRate = retReviewType
            }
            productDetialsViewModel.getSellerRates2AsSeller(
                sellerInformation?.providerId ?: "",
                sellerInformation?.businessAccountId,
                1,
                sendRate
            )
            productDetialsViewModel.getSellerRates2AsAbuyer(
                sellerInformation?.providerId ?: "",
                sellerInformation?.businessAccountId,
                1,
                sendRate
            )
        } else if (sellerAsASeller == SellerFilterReviewDialog.sellerAsASeller) {
            var sendRate: Int? = null
            if (retReviewType != SellerFilterReviewDialog.allReview) {
                sendRate = retReviewType
            }
            productDetialsViewModel.getSellerRates2AsSeller(
                sellerInformation?.providerId ?: "",
                sellerInformation?.businessAccountId,
                1,
                sendRate
            )
        } else {
            var sendRate: Int? = null
            if (retReviewType != SellerFilterReviewDialog.allReview) {
                sendRate = retReviewType
            }
            productDetialsViewModel.getSellerRates2AsAbuyer(
                sellerInformation?.providerId ?: "",
                sellerInformation?.businessAccountId,
                1,
                sendRate
            )
        }
    }

    private fun setListener() {
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.reviewType2.setOnClickListener {
            sellerFilterReviewDialog.show()
            sellerFilterReviewDialog.setSelectedTap(
                SellerFilterReviewDialog.sellerOrBayerFilterTap,
                sellerAsASeller,
                retReviewType
            )

        }
        binding.reviewType1.setOnClickListener {
            sellerFilterReviewDialog.show()
            sellerFilterReviewDialog.setSelectedTap(
                SellerFilterReviewDialog.reviewTypeTap,
                sellerAsASeller,
                retReviewType
            )

        }

    }

    override fun onApplyFilter(reviewType: Int, rateAsSellerOrBuyer: Int) {
        sellerFilterReviewDialog.dismiss()
        sellerAsASeller = rateAsSellerOrBuyer
        retReviewType = reviewType
        when (sellerAsASeller) {
            SellerFilterReviewDialog.sellerAsASeller -> {
                binding.reviewType2.text = getString(R.string.reviews_as_a_seller)
            }

            SellerFilterReviewDialog.sellerAsABuyer -> {
                binding.reviewType2.text = getString(R.string.reviews_as_a_buyer)
            }
        }
        onRefresh()

    }

    override fun onResetFilter() {
        sellerFilterReviewDialog.dismiss()
        sellerAsASeller = SellerFilterReviewDialog.sellerOrBayerFilterTap;
        retReviewType = SellerFilterReviewDialog.allReview
        when (sellerAsASeller) {
            SellerFilterReviewDialog.sellerAsASeller -> {
                binding.reviewType2.text = getString(R.string.reviews_as_a_seller)
            }

            SellerFilterReviewDialog.sellerAsABuyer -> {
                binding.reviewType2.text = getString(R.string.reviews_as_a_buyer)
            }
        }
        onRefresh()

    }

    override fun onDestroy() {
        super.onDestroy()
        productDetialsViewModel.closeAllCall()
    }
}