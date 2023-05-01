package com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.domain.models.sellerInfoResp.SellerInformation
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.adapter.SellerRateAdapter
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.dialog.SellerFilterReviewDialog
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import kotlinx.android.synthetic.main.activity_seller_rate.*
import kotlinx.android.synthetic.main.toolbar_main.*

class SellerRateActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
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
        setContentView(R.layout.activity_seller_rate)
        sellerInformation = intent.getParcelableExtra(ConstantObjects.sellerObjectKey)
        initView()
        setListenser()
        setSellerRateAdapter()
        setProductDetailsViewModel()
        onRefresh()
    }

    private fun setProductDetailsViewModel() {
        productDetialsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetialsViewModel.isLoading.observe(this) {
            if (it)
                progressbar.show()
            else
                progressbar.hide()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast((it.message!!), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        productDetialsViewModel.sellerRateListObservable.observe(this) { sellerRateListResp ->
            if (sellerRateListResp.status_code == 200) {
                sellerRateListResp.SellerRateObject?.let {
                    it.rateSellerListDto?.let { it1 -> sellerRateList.addAll(it1) }
                }
                sellerRateAdapter.notifyDataSetChanged()
                if (sellerRateList.isEmpty()) {
                    tvError.show()
                } else {
                    tvError.hide()
                }
            } else {
                if (sellerRateList.isEmpty()) {
                    if (sellerRateListResp.message != null) {
                        HelpFunctions.ShowLongToast((sellerRateListResp.message), this)
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                    }
                } else {
                    tvError.hide()
                }
            }
            //tvError
        }
    }

    private fun initView() {
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        toolbar_title.text = getString(R.string.all_reviews)
        sellerFilterReviewDialog = SellerFilterReviewDialog(this, this)
    }

    private fun setSellerRateAdapter() {
        sellerRateList = ArrayList()
        sellerRateAdapter = SellerRateAdapter(this, sellerRateList);
        linerlayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvRate.apply {
            adapter = sellerRateAdapter
            layoutManager = linerlayoutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linerlayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (sellerAsASeller == SellerFilterReviewDialog.sellerAsASeller) {
                        var sendRate:Int?=null
                        if(retReviewType!=SellerFilterReviewDialog.allReview){
                            sendRate=retReviewType
                        }
                        productDetialsViewModel.getSellerRates2AsSeller(
                            sellerInformation?.providerId ?: "",
                            sellerInformation?.businessAccountId,
                            page,
                            sendRate
                        )
                    }else{
                        var sendRate:Int?=null
                        if(retReviewType!=SellerFilterReviewDialog.allReview){
                            sendRate=retReviewType
                        }
                        productDetialsViewModel.getSellerRates2AsAbuyer(
                            sellerInformation?.providerId ?: "",
                            sellerInformation?.businessAccountId,
                            page,
                            sendRate
                        )
                    }

                }
            }
        rvRate.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    override fun onRefresh() {
        endlessRecyclerViewScrollListener.resetState()
        swipe_to_refresh.isRefreshing = false
        sellerRateList.clear()
        sellerRateAdapter.notifyDataSetChanged()
        tvError.hide()
        if (sellerAsASeller == SellerFilterReviewDialog.sellerAsASeller) {
            var sendRate:Int?=null
            if(retReviewType!=SellerFilterReviewDialog.allReview){
                sendRate=retReviewType
            }
            productDetialsViewModel.getSellerRates2AsSeller(
                sellerInformation?.providerId ?: "",
                sellerInformation?.businessAccountId,
                1,
                sendRate
            )
        }else{
            var sendRate:Int?=null
            if(retReviewType!=SellerFilterReviewDialog.allReview){
                sendRate=retReviewType
            }
            productDetialsViewModel.getSellerRates2AsAbuyer(
                sellerInformation?.providerId ?: "",
                sellerInformation?.businessAccountId,
                1,
                sendRate
            )
        }

    }

    private fun setListenser() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        review_type2.setOnClickListener {
            sellerFilterReviewDialog.show()
            sellerFilterReviewDialog.setSelectedTap(
                SellerFilterReviewDialog.sellerOrBayerFilterTap,
                sellerAsASeller,
                retReviewType
            )

        }
        review_type1.setOnClickListener {
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
        when(sellerAsASeller){
            SellerFilterReviewDialog.sellerAsASeller->{
                review_type2.text=getString(R.string.reviews_as_a_seller)
            }
            SellerFilterReviewDialog.sellerAsABuyer->{
                review_type2.text=getString(R.string.reviews_as_a_buyer)
            }
        }
        onRefresh()

    }

    override fun onResetFilter() {
        sellerFilterReviewDialog.dismiss()
        sellerAsASeller =SellerFilterReviewDialog.sellerOrBayerFilterTap;
        retReviewType = SellerFilterReviewDialog.allReview
        when(sellerAsASeller){
            SellerFilterReviewDialog.sellerAsASeller->{
                review_type2.text=getString(R.string.reviews_as_a_seller)
            }
            SellerFilterReviewDialog.sellerAsABuyer->{
                review_type2.text=getString(R.string.reviews_as_a_buyer)
            }
        }
        onRefresh()

    }


}