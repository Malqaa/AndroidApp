package com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivitySellerInformationBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.Branch
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.sellerInfoResp.SellerInformation
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ShowBranchesMapActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.viewModel.SellerViewModel
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class SellerInformationActivity : BaseActivity<ActivitySellerInformationBinding>(),
    SetOnProductItemListeners,
    SwipeRefreshLayout.OnRefreshListener {
    val PERMISSION_PHONE = 120
    var sellerInformation: SellerInformation? = null
    private var sellerViewModel: SellerViewModel? = null
    private lateinit var productAdapter: ProductHorizontalAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var gridLayoutManager: GridLayoutManager
    private var lastUpdateIndex = -1
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivitySellerInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMapSeller.hide()

        binding.toolbarMain.toolbarTitle.text = getString(R.string.merchant_page)

        sellerInformation = intent.getParcelableExtra(ConstantObjects.sellerObjectKey)
        sellerInformation?.let {
            setSellerInfo(it)
        }
        setViewClickListeners()
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        setAdapterForSellerProduct()
        setupViewModel()
        onRefresh()
    }

    private fun setSellerInfo(it: SellerInformation) {
        println("hhhh seller businessAccountId " + it.businessAccountId.toString())
        println("hhhh seller providerId " + it.providerId.toString())
        sellerInformation = it
        Extension.loadImgGlide(
            this,
            it.image,
            binding.sellerPicture,
            null
        )
        binding.sellerName.text = it.name ?: ""
        binding.memberSinceTv.text = HelpFunctions.getViewFormatForDateTrack(
            it.createdAt ?: "", "dd/MM/yyyy"
        )
        if (it.businessAccountId == null) {
            binding.txtTypeUser.text = getString(R.string.personal)
        } else {
            binding.txtTypeUser.text = getString(R.string.merchant)
        }
        binding.sellerCity.text = it.city ?: ""
        binding.sellerNumber.text = it.phone ?: ""
        if (it.isFollowed) {
            binding.ivSellerFollow.setImageResource(R.drawable.notification)
        } else {
            binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
        }
        //   tvRateText.text=it.rate.toString()
        when (it.rate) {
            3f -> {
                binding.ivRateSeller.setImageResource(R.drawable.happyface_color)
            }

            2f -> {
                binding.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }

            1f -> {
                binding.ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                binding.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }
        }
        if (it.businessAccountId != "") {
            binding.btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                binding.btnMapSeller.show()
            } else {
                binding.btnMapSeller.hide()
            }
        }


        if (it.instagram != null && it.instagram != "") {
            binding.instagramBtn.show()
        } else {
            binding.instagramBtn.hide()
        }
        if (it.youTube != null && it.youTube != "") {
            binding.youtubeBtn.show()
        } else {
            binding.youtubeBtn.hide()
        }
        if (it.skype != null && it.skype != "") {
            binding.skypeBtn.show()
        } else {
            binding.skypeBtn.hide()
        }
        if (it.faceBook != null && it.faceBook != "") {
            binding.facebookBtn.show()
        } else {
            binding.facebookBtn.hide()
        }
        if (it.twitter != null && it.twitter != "") {
            binding.twitterBtn.show()
        } else {
            binding.twitterBtn.hide()
        }
        if (it.linkedIn != null && it.linkedIn != "") {
            binding.linkedInBtn.show()
        } else {
            binding.linkedInBtn.hide()
        }
        if (it.tikTok != null && it.tikTok != "") {
            binding.tiktokBtn.show()
        } else {
            binding.tiktokBtn.hide()
        }
        if (it.snapchat != null && it.snapchat != "") {
            binding.snapChatBtn.show()
        } else {
            binding.snapChatBtn.hide()
        }


        if (it.businessAccountId != "") {
            binding.btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                if (it.lat != 0.0 && it.lon != 0.0) {
                    binding.btnMapSeller.show()
                }
            } else {
                binding.btnMapSeller.hide()
            }
        }
    }

    private fun setAdapterForSellerProduct() {
        productList = ArrayList()
        gridLayoutManager = GridLayoutManager(this@SellerInformationActivity, 2)
        productAdapter = ProductHorizontalAdapter(productList, this, 0, false)
        binding.favRcv.apply {
            adapter = productAdapter
            layoutManager = gridLayoutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    sellerViewModel!!.getSellerListProduct(
                        page,
                        sellerInformation?.providerId ?: "",
                        sellerInformation?.businessAccountId ?: ""
                    )
                }
            }
        binding.favRcv.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun setupViewModel() {
        sellerViewModel = ViewModelProvider(this).get(SellerViewModel::class.java)
        sellerViewModel!!.isLoading.observe(this) {
            if (it)
                binding.progressBar.show()
            else
                binding.progressBar.hide()
        }
        sellerViewModel!!.sellerLoading.observe(this) {
            if (it) {
                binding.sellerProgressBar.show()
            } else {
                binding.sellerProgressBar.hide()
            }
        }
        sellerViewModel!!.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        sellerViewModel!!.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    showProductApiError(it.message!!)
                } else {
                    showProductApiError(getString(R.string.serverError))
                }
            }
        }
        sellerViewModel!!.sellerProductsRespObserver.observe(this) { productListResp ->
            if (productListResp.status_code == 200) {
                if (!productListResp.productList.isNullOrEmpty()) {
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    productAdapter.notifyDataSetChanged()


                } else {
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        sellerViewModel!!.isNetworkFailProductToFav.observe(this) {
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
        sellerViewModel!!.errorResponseObserverProductToFav.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
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
        }
        sellerViewModel!!.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
                productList[lastUpdateIndex].isFavourite = !productList[lastUpdateIndex].isFavourite
                productAdapter.notifyItemChanged(lastUpdateIndex)

            }
        }
        sellerViewModel!!.addSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = true
                binding.ivSellerFollow.setImageResource(R.drawable.notification)
            }
        }
        sellerViewModel!!.removeSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = false
                binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
            }
        }

        onRefresh()
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
        productList.clear()
        productAdapter.notifyDataSetChanged()
        endlessRecyclerViewScrollListener.resetState()
        sellerViewModel!!.getSellerListProduct(
            1,
            sellerInformation?.providerId ?: "",
            sellerInformation?.businessAccountId ?: ""
        )
    }

    private fun showProductApiError(message: String) {
        binding.tvError.show()
        binding.tvError.text = message
    }

    private fun setViewClickListeners() {
        binding.ivSellerFollow.setOnClickListener {
            sellerInformation?.let {
                if (it.isFollowed) {
                    sellerViewModel!!.removeSellerToFav(it.providerId, it.businessAccountId)
                } else {
                    sellerViewModel!!.addSellerToFav(it.providerId, it.businessAccountId)
                }
            }
        }
        binding.sellerNumber.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + binding.sellerNumber.text.toString())

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(Manifest.permission.CALL_PHONE),
                    PERMISSION_PHONE
                )

            } else {
                startActivity(callIntent)
            }
        }
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.btnRate.setOnClickListener {
            startActivity(Intent(this, SellerRateActivity::class.java).apply {
                putExtra(ConstantObjects.sellerObjectKey, sellerInformation)
            })
        }

        binding.skypeBtn.setOnClickListener {
            if (sellerInformation?.skype != null && sellerInformation?.skype != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.skype!!, this)
            }
        }
        binding.youtubeBtn.setOnClickListener {
            if (sellerInformation?.youTube != null && sellerInformation?.youTube != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.youTube!!, this)

            }
        }
        binding.instagramBtn.setOnClickListener {
            if (sellerInformation?.instagram != null && sellerInformation?.instagram != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.instagram!!, this)
            }
        }
        binding.facebookBtn.setOnClickListener {
            if (sellerInformation?.faceBook != null && sellerInformation?.faceBook != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.faceBook!!, this)
            }
        }
        binding.twitterBtn.setOnClickListener {
            if (sellerInformation?.twitter != null && sellerInformation?.twitter != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.twitter!!, this)
            }
        }
        binding.linkedInBtn.setOnClickListener {
            if (sellerInformation?.linkedIn != null && sellerInformation?.linkedIn != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.linkedIn!!, this)
            }
        }
        binding.tiktokBtn.setOnClickListener {
            if (sellerInformation?.tikTok != null && sellerInformation?.tikTok != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.tikTok!!, this)
            }
        }
        binding.snapChatBtn.setOnClickListener {
            if (sellerInformation?.snapchat != null && sellerInformation?.snapchat != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.snapchat!!, this)
            }
        }
        binding.btnMapSeller.setOnClickListener {
            openLocationInMap(sellerInformation?.branches!!)
        }

    }

    private fun openLocationInMap(branches: ArrayList<Branch>) {
        startActivity(Intent(this, ShowBranchesMapActivity::class.java).apply {
            putParcelableArrayListExtra("customBranches", branches)

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_PHONE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + binding.sellerNumber.text.toString())
                    startActivity(callIntent)
                } else {
                    HelpFunctions.ShowLongToast("Permission Phone denied", this)
                }
            }
        }
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
            sellerViewModel!!.addProductToFav(productID)

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

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("isFollow", sellerInformation?.isFollowed)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        sellerViewModel!!.closeAllCall()
        sellerViewModel!!.baseCancel()
        productAdapter.onDestroyHandler()
        sellerViewModel = null
    }
}