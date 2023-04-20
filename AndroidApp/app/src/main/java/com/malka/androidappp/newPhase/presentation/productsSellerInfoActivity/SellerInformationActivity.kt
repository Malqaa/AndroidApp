package com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.sellerInfoResp.SellerInformation
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import kotlinx.android.synthetic.main.activity_seller_information.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SellerInformationActivity : BaseActivity(), SetOnProductItemListeners,
    SwipeRefreshLayout.OnRefreshListener {
    var sellerInformation: SellerInformation? = null
    private lateinit var sellerViewModel: SellerViewModel
    private lateinit var productAdapter: ProductHorizontalAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var gridLayoutManager: GridLayoutManager
    private var lastUpdateIndex = -1
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_information)

//        getSellerByID(ConstantObjects.logged_userid, ConstantObjects.logged_userid)

        toolbar_title.text = getString(R.string.merchant_page)

        sellerInformation = intent.getParcelableExtra(ConstantObjects.sellerObjectKey)
        sellerInformation?.let {
            setSellerInfo(it)
        }
        setViewClickListeners()
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setAdapterForSellerProduct()
        setupViewModel()
        onRefresh()


    }

    private fun setSellerInfo(it: SellerInformation) {
        println("hhhh seller businessAccountId " + it.businessAccountId.toString())
        println("hhhh seller providerId " + it.providerId.toString())
        sellerInformation = it
        Extension.loadThumbnail(
            this,
            it.image,
            seller_picture,
            null
        )
        sellerName.text = it.name ?: ""
        member_since_Tv.text = HelpFunctions.getViewFormatForDateTrack(
            it.createdAt ?: ""
        )
        seller_city.text = it.city ?: ""
        seller_number.text = it.phone ?: ""
        if (it.isFollowed) {
            ivSellerFollow.setImageResource(R.drawable.notification)
        } else {
            ivSellerFollow.setImageResource(R.drawable.notification_log)
        }
//        if (it.lat != null && it.lon != null) {
//            btnMapSeller.show()
//        } else {
//            btnMapSeller.hide()
//        }
        if (it.instagram != null && it.instagram != "") {
            instagram_btn.show()
        } else {
            instagram_btn.hide()
        }
        if (it.youTube != null && it.youTube != "") {
            youtube_btn.show()
        } else {
            youtube_btn.hide()
        }
        if (it.skype != null && it.skype != "") {
            skype_btn.show()
        } else {
            skype_btn.hide()
        }
        if (it.faceBook != null && it.faceBook != "") {
            facebook_btn.show()
        } else {
            facebook_btn.hide()
        }
        if (it.twitter != null && it.twitter != "") {
            twitter_btn.show()
        } else {
            twitter_btn.hide()
        }
        if (it.linkedIn != null && it.linkedIn != "") {
            linked_in_btn.show()
        } else {
            linked_in_btn.hide()
        }
        if (it.tikTok != null && it.tikTok != "") {
            tiktok_btn.show()
        } else {
            tiktok_btn.hide()
        }
        if (it.snapchat != null && it.snapchat != "") {
            snapChat_btn.show()
        } else {
            snapChat_btn.hide()
        }

    }

    private fun setAdapterForSellerProduct() {
        productList = ArrayList()
        gridLayoutManager = GridLayoutManager(this@SellerInformationActivity, 2)
        productAdapter = ProductHorizontalAdapter(productList, this, 0, false)
        fav_rcv.apply {
            adapter = productAdapter
            layoutManager = gridLayoutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    sellerViewModel.getSellerListProduct(
                        page,
                        sellerInformation?.providerId ?: "",
                        sellerInformation?.businessAccountId ?: ""
                    )
                }
            }
        fav_rcv.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun setupViewModel() {
        sellerViewModel = ViewModelProvider(this).get(SellerViewModel::class.java)
        sellerViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        sellerViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        sellerViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message!!)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        sellerViewModel.sellerProductsRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (productListResp.productList != null && productListResp.productList.isNotEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    productAdapter.notifyDataSetChanged()


                } else {
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        sellerViewModel.isNetworkFailProductToFav.observe(this) {
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

        }
        sellerViewModel.errorResponseObserverProductToFav.observe(this) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        sellerViewModel.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
                productList[lastUpdateIndex].isFavourite = !productList[lastUpdateIndex].isFavourite
                productAdapter.notifyItemChanged(lastUpdateIndex)

            }
        }
        onRefresh()
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        productList.clear()
        productAdapter.notifyDataSetChanged()
        endlessRecyclerViewScrollListener.resetState()
        sellerViewModel.getSellerListProduct(
            1,
            sellerInformation?.providerId ?: "",
            sellerInformation?.businessAccountId ?: ""
        )
    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        btnRate.setOnClickListener {
            startActivity(Intent(this,SellerRateActivity::class.java))
        }

        skype_btn.setOnClickListener {
            if (sellerInformation?.skype != null && sellerInformation?.skype != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.skype!!, this)
            }
        }
        youtube_btn.setOnClickListener {
            if (sellerInformation?.youTube != null && sellerInformation?.youTube != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.youTube!!, this)

            }
        }
        instagram_btn.setOnClickListener {
            if (sellerInformation?.instagram != null && sellerInformation?.instagram != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.instagram!!, this)
            }
        }
        facebook_btn.setOnClickListener {
            if (sellerInformation?.faceBook != null && sellerInformation?.faceBook != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.faceBook!!, this)
            }
        }
        twitter_btn.setOnClickListener {
            if (sellerInformation?.twitter != null && sellerInformation?.twitter != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.twitter!!, this)
            }
        }
        linked_in_btn.setOnClickListener {
            if (sellerInformation?.linkedIn != null && sellerInformation?.linkedIn != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.linkedIn!!, this)
            }
        }
        tiktok_btn.setOnClickListener {
            if (sellerInformation?.tikTok != null && sellerInformation?.tikTok != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.tikTok!!, this)
            }
        }
        snapChat_btn.setOnClickListener {
            if (sellerInformation?.snapchat != null && sellerInformation?.snapchat != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.snapchat!!, this)
            }
        }
        btnMapSeller.setOnClickListener {
            if (sellerInformation?.lat != null && sellerInformation?.lon != null) {
                openLocationInMap(sellerInformation?.lat!!, sellerInformation?.lon!!)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.noLocationFound), this)
            }
            openLocationInMap(0.0, 0.0)
        }

    }

    private fun openLocationInMap(lat: Double, langtiude: Double) {
        val URL = ("http://maps.google.com/maps?saddr=&daddr=$lat,$langtiude&dirflg=d")
        val location = Uri.parse(URL)
        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
        startActivity(
            Intent(this, ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra("Template", "")
            })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            sellerViewModel.addProductToFav(productID)

        } else {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

    }


//    private fun getSellerByID(id: String, loggedUserID: String) {
//
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val call: Call<SellerResponseBack> = malqa.getAdSeller(id, loggedUserID)
//
//        call.enqueue(object : Callback<SellerResponseBack> {
//
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(
//                call: Call<SellerResponseBack>,
//                response: Response<SellerResponseBack>
//            ) {
//                if (response.isSuccessful) {
//
//                    if (response.body() != null) {
//                        val respone: SellerResponseBack = response.body()!!
//                        if (respone.status_code == 200) {
//
//                            var sellerAdsList = response.body()!!.data.detailOfUser
//
//                            sellerName.text = sellerAdsList.fullName ?: ""
//                            seller_city.text = sellerAdsList.city
//                            seller_number_tv.text = sellerAdsList.phone
//                            member_sinceTv.text = "${getString(R.string.member_since)}:  ${sellerAdsList.member_since}"
//                            member_ship.text = "${getString(R.string.membership_number)}: "
//
//                            seller_information_recycler.adapter = GenericProductAdapter(
//                                respone.data.advertisements,
//                                this@SellerInformationActivity
//                            )
//
//
//                        } else {
//
//                            HelpFunctions.ShowLongToast(
//                                getString(R.string.ErrorOccur),
//                                this@SellerInformationActivity
//                            )
//                        }
//                    }
//
//                } else {
//                    HelpFunctions.dismissProgressBar()
//
//                }
//            }
//
//            override fun onFailure(call: Call<SellerResponseBack>, t: Throwable) {
//                HelpFunctions.dismissProgressBar()
//                HelpFunctions.ShowLongToast(t.message!!, this@SellerInformationActivity)
//            }
//        })
//    }
}