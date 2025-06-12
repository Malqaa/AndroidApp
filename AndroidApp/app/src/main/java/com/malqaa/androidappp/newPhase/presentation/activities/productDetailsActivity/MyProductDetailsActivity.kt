package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityMyProductDetails2Binding
import com.malqaa.androidappp.databinding.ActivityProductDetailsItem2Binding
import com.malqaa.androidappp.databinding.MyProductDetailsBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.enums.ShowUserInfo
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.Branch
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderItem
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malqaa.androidappp.newPhase.domain.models.sellerInfoResp.SellerInformation
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity.AddRateProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity.ProductReviewsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.CartActivity
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.ProductImagesAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.ReviewProductAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.SpecificationAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.productQuestionActivity.QuestionActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.SellerInformationActivity
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.currentPriceDialog.BuyCurrentPriceDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.AddDiscountDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.ListenerSlider
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.SliderAdaptor
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.shared
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.activitiesMain.PlayActivity
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.util.Date

class MyProductDetailsActivity : BaseActivity<MyProductDetailsBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, QuestionAnswerAdapter.SetonSelectedQuestion,
    BuyCurrentPriceDialog.OnAttachedCartMethodSelected, ListenerSlider {

    lateinit var myProductDetails2Binding: ActivityMyProductDetails2Binding
    lateinit var productDetailsItem2Binding: ActivityProductDetailsItem2Binding

    var addProductReviewRequestCode = 1000
    lateinit var product: Product
    var urlImg = ""
    lateinit var questionAnswerAdapter: QuestionAnswerAdapter
    lateinit var sellerProductAdapter: ProductHorizontalAdapter
    lateinit var similarProductAdapter: ProductHorizontalAdapter

    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    private var productDetails: Product? = null
    private var productId: Int = -1;
    lateinit var specificationAdapter: SpecificationAdapter
    lateinit var reviewProductAdapter: ReviewProductAdapter
    lateinit var specificationList: ArrayList<DynamicSpecificationSentObject>
    lateinit var productImagesAdapter: ProductImagesAdapter
    lateinit var productImagesList: ArrayList<ImageSelectModel>
    var imgPosition = 0
    lateinit var similerProductList: ArrayList<Product>
    lateinit var sellerSimilerProductList: ArrayList<Product>
    var questionsList: List<QuestionItem> = ArrayList()
    lateinit var subQuestionsList: ArrayList<QuestionItem>

    /****/
    val added_from_product_Destails_status = 1
    val added_from_last_similerProducts_status = 2
    val added_from_last_seller_Products_status = 3
    var added_position_from_last_Product = 0
    var status_product_added_to_fav_from = 0
    var productfavStatus = false
    var favAddingChange = false
    private var userData: LoginUser? = null
    var isMyProduct = false
    var isMyProductForSale = false
    var providerID = ""
    var businessID = ""

    lateinit var priceNegotiationDialog: PriceNegotiationDialog
    var productPrice: Float = 0f

    lateinit var smallRatesList: ArrayList<RateReviewItem>
    lateinit var mainRatesList: ArrayList<RateReviewItem>
    var comeFrom = ""

    var sellerInformation: SellerInformation? = null
    val sellerInformationLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                if (intent != null) {
                    val isFollow = intent.getBooleanExtra("isFollow", false)
                    sellerInformation?.isFollowed = isFollow
                    if (isFollow) {
                        myProductDetails2Binding.ivSellerFollow.setImageResource(R.drawable.notification)
                    } else {
                        myProductDetails2Binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
                    }
                }
            }
        }

    private lateinit var buyCurrentPriceDialog: BuyCurrentPriceDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = MyProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myProductDetails2Binding = binding.containerMainProduct
        productDetailsItem2Binding = ActivityProductDetailsItem2Binding.inflate(layoutInflater)

        productId = intent.getIntExtra(ConstantObjects.productIdKey, -1)
        comeFrom = intent.getStringExtra("ComeFrom") ?: ""

        isMyProduct = intent.getBooleanExtra(ConstantObjects.isMyProduct, false)
        isMyProductForSale = intent.getBooleanExtra("isMyProductForSale", false)
        providerID = intent.getStringExtra("providerIdKey").toString()
        businessID = intent.getStringExtra("businessAccountIdKey").toString()

        setViewChanges()
        setProductDetailsViewModel()
        setupViewClickListeners()
        setupViewAdapters()

        onRefresh()
//        if (HelpFunctions.isUserLoggedIn()) {
        userData = Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
        productDetialsViewModel.addLastViewedProduct(productId)
//        }






        myProductDetails2Binding.editProduct.setOnClickListener {
            ConstantObjects.isModify = true
            ConstantObjects.isRepost = false
            startActivity(
                Intent(
                    this@MyProductDetailsActivity,
                    ConfirmationAddProductActivity::class.java
                ).apply {
                    putExtra("productID", productId)
                    putExtra("whereCome", "repost")
                    putExtra(ConstantObjects.isEditKey, true)

                })
        }
        myProductDetails2Binding.discountProduct.setOnClickListener {
            openDiscountDialog(productId)
        }
        myProductDetails2Binding.manageProduct.setOnClickListener {
            productDetialsViewModel!!.changeBusinessAccount(productDetails?.businessAccountId!!)
            SharedPreferencesStaticClass.saveAccountId(productDetails?.businessAccountId!!)
        }
    }

    private fun openDiscountDialog(productID: Int) {
        val addDiscountDialog =
            AddDiscountDialog(
                this,
                productDetails?.price!!,
                this.supportFragmentManager,
                object : AddDiscountDialog.SetonClickListeners {
                    override fun onAddDiscount(finaldate: String, newPrice: Float) {

                        productDetialsViewModel.addDiscount(productID, newPrice, finaldate)

                    }

                })
        addDiscountDialog.show()
    }

    private fun setPagerDots(list: List<HomeSliderItem>) {
        if (list.isNotEmpty()) {
            val viewPagerAdapter = SliderAdaptor(this, list, true, this)
            binding.sliderMyDetails.adapter = viewPagerAdapter
        }
    }

    private fun mapImageSelectModelToHomeSliderItem(imageSelectModels: ArrayList<ImageSelectModel>): List<HomeSliderItem> {
        return imageSelectModels.filter { it.type != 2 }.map { imageSelectModel ->
            HomeSliderItem(
                id = imageSelectModel.id,
                img = imageSelectModel.url,
                type = imageSelectModel.type
            )
        }
    }

    private fun callGetPriceCart(nameProduct: String) {
        if (SharedPreferencesStaticClass.getMasterCartId().toInt() != 0) {
            productDetialsViewModel.getCartTotalPrice()
            productDetialsViewModel.getCartPrice.observe(this) {
                openBuyCurrentPriceDialog(
                    "${
                        productPrice + it.data.toString().toFloat()
                    } ${getString(R.string.sar)}", nameProduct
                )
            }
        } else {
            openBuyCurrentPriceDialog("0 ${getString(R.string.sar)}", nameProduct)
        }
    }

    private fun openBuyCurrentPriceDialog(price: String, nameProduct: String) {
        buyCurrentPriceDialog = BuyCurrentPriceDialog(this, price, nameProduct, this)
        if (!buyCurrentPriceDialog.isShowing)
            buyCurrentPriceDialog.show()
    }


    override fun setOnGoCart() {
        productDetialsViewModel.addProductToCart(
            SharedPreferencesStaticClass.getMasterCartId(),
            productId
        )
        buyCurrentPriceDialog.dismiss()
    }

    override fun setOnFollowShopping() {
        buyCurrentPriceDialog.dismiss()
    }

    /**set view changes*/
    private fun setViewChanges() {
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            binding.fbButtonBack.scaleX = 1f
        } else {
            binding.fbButtonBack.scaleX = -1f
        }
        // Get the container from the included layout
        val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
        containerMainProduct.hide()
        binding.otherImageLayout.hide()
        myProductDetails2Binding.btnMoreSpecification.hide()
        myProductDetails2Binding.btnMoreItemDetails.hide()
        myProductDetails2Binding.tvShippingOptions.hide()
        myProductDetails2Binding.tvShippingOptionsTwo.hide()
        myProductDetails2Binding.contianerBankAccount.hide()
        myProductDetails2Binding.containerMada.hide()
        myProductDetails2Binding.containerMaster.hide()
        myProductDetails2Binding.contianerCash.hide()
        myProductDetails2Binding.containerAuctioncountdownTimerBar.hide()
        //for reviewa
        myProductDetails2Binding.tvReviewsError.hide()
        myProductDetails2Binding.contianerRateText.hide()
        //====
        myProductDetails2Binding.tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")


    }

    private fun showProductApiError(message: String) {
        if (productDetails == null) {
            // Get the container from the included layout
            val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
            containerMainProduct.hide()
            binding.containerShareAndFav.hide()
        }
        HelpFunctions.ShowLongToast(message, this)
    }


    /**set view listeners*/

    private fun setupViewClickListeners() {
        binding.myProductimg.setOnClickListener {
            val intent = Intent(this@MyProductDetailsActivity, ImageViewLargeActivity::class.java)
            intent.putParcelableArrayListExtra("imgList", productImagesList)
            intent.putExtra("UrlImg", binding.myProductimg.tag.toString())
            startActivity(intent)
        }

        myProductDetails2Binding.ivSellerFollow.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                sellerInformation?.let {
                    if (it.isFollowed) {
                        productDetialsViewModel.removeSellerToFav(
                            it.providerId,
                            it.businessAccountId
                        )
                    } else {
                        productDetialsViewModel.addSellerToFav(it.providerId, it.businessAccountId)
                    }
                }
            } else {
                startActivity(
                    Intent(
                        this,
                        SignInActivity::class.java
                    ).apply {})
            }


        }

        myProductDetails2Binding.skypeBtn.setOnClickListener {
            if (sellerInformation?.skype != null && sellerInformation?.skype != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.skype!!, this)
            }
        }
        myProductDetails2Binding.youtubeBtn.setOnClickListener {
            if (sellerInformation?.youTube != null && sellerInformation?.youTube != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.youTube!!, this)

            }
        }
        myProductDetails2Binding.instagramBtn.setOnClickListener {
            if (sellerInformation?.instagram != null && sellerInformation?.instagram != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.instagram!!, this)
            }
        }
        myProductDetails2Binding.facebookBtn.setOnClickListener {
            if (sellerInformation?.faceBook != null && sellerInformation?.faceBook != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.faceBook!!, this)
            }
        }
        myProductDetails2Binding.twitterBtn.setOnClickListener {
            if (sellerInformation?.twitter != null && sellerInformation?.twitter != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.twitter!!, this)
            }
        }
        myProductDetails2Binding.linkedInBtn.setOnClickListener {
            if (sellerInformation?.linkedIn != null && sellerInformation?.linkedIn != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.linkedIn!!, this)
            }
        }
        myProductDetails2Binding.tiktokBtn.setOnClickListener {
            if (sellerInformation?.tikTok != null && sellerInformation?.tikTok != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.tikTok!!, this)
            }
        }
        myProductDetails2Binding.snapChatBtn.setOnClickListener {
            if (sellerInformation?.snapchat != null && sellerInformation?.snapchat != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.snapchat!!, this)
            }
        }
        myProductDetails2Binding.btnMapSeller.setOnClickListener {
            openLocationInMapSec(sellerInformation?.branches ?: arrayListOf())

        }

        myProductDetails2Binding.containerSellerInfo.setOnClickListener {
            if (sellerInformation != null) {
                sellerInformationLaucher.launch(
                    Intent(
                        this,
                        SellerInformationActivity::class.java
                    ).apply {
                        if (sellerInformation?.branches == null)
                            sellerInformation?.branches = arrayListOf()

                        putExtra(ConstantObjects.sellerObjectKey, sellerInformation)
                    })
//                startActivity(Intent(this, SellerInformationActivity::class.java))
            }
        }
        myProductDetails2Binding.containerSellerImage.setOnClickListener {
            if (sellerInformation != null) {
                sellerInformationLaucher.launch(
                    Intent(
                        this,
                        SellerInformationActivity::class.java
                    ).apply {
                        if (sellerInformation?.branches == null)
                            sellerInformation?.branches = arrayListOf()

                        putExtra(ConstantObjects.sellerObjectKey, sellerInformation)
                    })
            }
        }

        myProductDetails2Binding.btnSellerProducts.setOnClickListener {
            if (myProductDetails2Binding.containerSellerProduct.isVisible) {
                myProductDetails2Binding.containerSellerProduct.hide()
                myProductDetails2Binding.sellerProductTv.text =
                    getString(R.string.view_similar_product_from_seller)
                myProductDetails2Binding.isSellerProductHideIv.setImageResource(R.drawable.down_arrow)
            } else {
                myProductDetails2Binding.containerSellerProduct.show()
                myProductDetails2Binding.sellerProductTv.text = getString(R.string.showLess)
                myProductDetails2Binding.isSellerProductHideIv.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        myProductDetails2Binding.tvAddReview.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivityForResult(Intent(this, AddRateProductActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                }, addProductReviewRequestCode)
            } else {
                startActivity(
                    Intent(
                        this,
                        SignInActivity::class.java
                    ).apply {})
            }
        }

        binding.ivFav.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                status_product_added_to_fav_from = added_from_product_Destails_status
                productDetialsViewModel.addProductToFav(productId)
            } else {
                startActivity(
                    Intent(
                        this,
                        SignInActivity::class.java
                    ).apply {})
            }

        }
        binding.fbButtonBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnShare.setOnClickListener {
            shared("http://advdev-001-site1.dtempurl.com/Home/GetProductById?id=$productId")
        }

        myProductDetails2Binding.tvQuestionAndAnswersShowAll.setOnClickListener {
            if (!questionsList.isNullOrEmpty())
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                    putExtra(ConstantObjects.isMyProduct, isMyProduct)
                })
        }

        myProductDetails2Binding.tvShowAllReviews.setOnClickListener {
            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)
            })
        }

        binding.btnNextImage.setOnClickListener {
            try {
                if (productImagesList.size > 0) {
                    var position = getLastVisiblePosition(binding.rvProductImages);
                    if (position < productImagesList.size - 1) {
                        binding.rvProductImages.smoothScrollToPosition(position + 1);
                    } else {
                        binding.rvProductImages.smoothScrollToPosition(0);
                    }
                }
            } catch (e: Exception) {
            }
        }


    }

    private fun openPriceNegotiationDialog() {
        priceNegotiationDialog = PriceNegotiationDialog(
            this,
            1,
            productId,
            object : PriceNegotiationDialog.SetClickListeners {
                override fun setOnSuccessListeners(highestBidPrice: String) {
                    priceNegotiationDialog.dismiss()
                    openPriceNegotiationDoneSuccess(highestBidPrice)
                }

            })
        priceNegotiationDialog.show()
    }

    private fun openPriceNegotiationDoneSuccess(highestBidPrice: String) {
        println("hhhh success")
        var intent = Intent(this, SuccessAddProductPriceNegotiationActivity::class.java).apply {
            putExtra("price", highestBidPrice)
        }
        startActivity(intent)
    }

    private fun openLocationInMap(lat: Double, langtiude: Double) {
        val URL = ("http://maps.google.com/maps?saddr=&daddr=$lat,$langtiude&dirflg=d")
        val location = Uri.parse(URL)
        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun openLocationInMapSec(branches: ArrayList<Branch>) {
        startActivity(Intent(this, ShowBranchesMapActivity::class.java).apply {
            putParcelableArrayListExtra("customBranches", branches)

        })
    }

    private fun getLastVisiblePosition(rv: RecyclerView?): Int {
        if (rv != null) {
            val layoutManager = rv.layoutManager
            if (layoutManager is LinearLayoutManager) {
                return layoutManager.findLastCompletelyVisibleItemPosition()
            }
        }
        return 0
    }

    /**set up view model*/
    private fun setProductDetailsViewModel() {
        productDetialsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetialsViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
            if (it != null)
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

        productDetialsViewModel.sellerInfoObservable.observe(this) { sellerInfoResp ->
            if (sellerInfoResp.status_code == 200) {
                /**seller info*/
                sellerInfoResp.sellerInformation?.let {
                    setSellerInfo(it)

                }

            }
        }
        productDetialsViewModel.addSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = true
                myProductDetails2Binding.ivSellerFollow.setImageResource(R.drawable.notification)
            }
        }
        productDetialsViewModel.removeSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = false
                myProductDetails2Binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
            }
        }
        productDetialsViewModel.getRateResponseObservable.observe(this) { rateListResp ->
            if (rateListResp.status_code == 200) {
                if (rateListResp.data.happyCount != 0) {
                    myProductDetails2Binding.linHappy.show()
                    myProductDetails2Binding.txtHappy.text = rateListResp.data.happyCount.toString()
                }
                if (rateListResp.data.satisfiedCount != 0) {
                    myProductDetails2Binding.linSmile.show()
                    myProductDetails2Binding.txtSmile.text =
                        rateListResp.data.satisfiedCount.toString()
                }
                if (rateListResp.data.disgustedCount != 0) {
                    myProductDetails2Binding.linSad.show()
                    myProductDetails2Binding.txtSad.text =
                        rateListResp.data.disgustedCount.toString()
                }

                myProductDetails2Binding.ratingBarDetailTv.text =
                    "${rateListResp.data.totalRecords} ${getString(R.string.visitors)} "
                if (!rateListResp.data.rateProductListDto.isNullOrEmpty()) {
                    setReviewRateView(rateListResp.data.rateProductListDto)
                } else {
                    myProductDetails2Binding.containerReviews.hide()
                }
            }
        }
        productDetialsViewModel.productDetailsObservable.observe(this) { productResp ->
            if (productResp.productDetails != null) {
                productDetails = productResp.productDetails
                if (productDetails?.priceDisc != productDetails?.price)
                    myProductDetails2Binding.layDiscount.show()
                setProductData(productDetails)
            } else {
                showProductApiError(productResp.message)
            }

        }


        productDetialsViewModel.removeProductObserver.observe(this) {
            HelpFunctions.ShowLongToast(getString(R.string.removeProductSuccessfully), this)
            finish()
        }
        productDetialsViewModel.isNetworkFailProductToFav.observe(this) {
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
        productDetialsViewModel.errorResponseObserverProductToFav.observe(this) {
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
        productDetialsViewModel.addProductToFavObserver.observe(this) {
            if (it.status_code == 200) {
                when (status_product_added_to_fav_from) {
                    added_from_last_similerProducts_status -> {
                        if (added_position_from_last_Product < similerProductList.size) {
                            similerProductList[added_position_from_last_Product].isFavourite =
                                !similerProductList[added_position_from_last_Product].isFavourite
                            similarProductAdapter.notifyItemChanged(
                                added_position_from_last_Product
                            )
                        }
                    }

                    added_from_last_seller_Products_status -> {
                        if (added_position_from_last_Product < similerProductList.size) {
                            sellerSimilerProductList[added_position_from_last_Product].isFavourite =
                                !sellerSimilerProductList[added_position_from_last_Product].isFavourite
                            sellerProductAdapter.notifyItemChanged(
                                added_position_from_last_Product
                            )
                        }
                    }

                    added_from_product_Destails_status -> {
                        favAddingChange = true
                        productDetails?.let { it ->
                            it.isFavourite = !it.isFavourite
                            setProductData(productDetails)
                        }
                    }
                }


            }
        }

        productDetialsViewModel.sellerLoading.observe(this) {
            if (it) {
                myProductDetails2Binding.sellerProgressBar.show()
            } else {
                myProductDetails2Binding.sellerProgressBar.hide()
            }
        }
        productDetialsViewModel.sellerProductsRespObserver.observe(this) { sellerProductListResp ->
            if (sellerProductListResp.status_code == 200) {
                sellerSimilerProductList.clear()
                sellerProductListResp.productList?.let { sellerSimilerProductList.addAll(it) }
                sellerProductAdapter.notifyDataSetChanged()
                if (sellerSimilerProductList.isEmpty()) {
                    myProductDetails2Binding.tvErrorNoSellerProduct.show()
                } else {
                    myProductDetails2Binding.tvErrorNoSellerProduct.hide()
                }
            } else {
                myProductDetails2Binding.tvErrorNoSellerProduct.show()
            }
        }

        productDetialsViewModel.addDiscountObserver.observe(this) { addDiscountResp ->
            if (addDiscountResp.status_code == 200) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.discountAddedSuccessfully),
                    this
                )
                onRefresh()
            } else {
                if (addDiscountResp.message != null && addDiscountResp.message != "") {
                    HelpFunctions.ShowLongToast(addDiscountResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this
                    )
                }

            }
        }


        productDetialsViewModel.addProductToCartObservable.observe(this) { addproductToCartResp ->
            if (addproductToCartResp.status_code == 200) {
                addproductToCartResp.addProductToCartData?.let {
                    SharedPreferencesStaticClass.saveMasterCartId(it.cartMasterId)
                }
                HelpFunctions.ShowLongToast(getString(R.string.productAddedToCart), this)
                startActivity(Intent(this, CartActivity::class.java))

            } else {
                if (addproductToCartResp.message != null) {
                    HelpFunctions.ShowLongToast(addproductToCartResp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)

                }
            }
        }

        productDetialsViewModel.getListOfQuestionsObservable.observe(this) { questionListResp ->
            if (!questionListResp.questionList.isNullOrEmpty()) {
                myProductDetails2Binding.tvErrorNoQuestion.hide()
                setQuestionsView(questionListResp.questionList)
            } else {
                myProductDetails2Binding.tvErrorNoQuestion.show()
                myProductDetails2Binding.tvQuestionAndAnswersShowAll.hide()
            }
        }

        productDetialsViewModel.shippingOptionObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.shippingOptionObject != null && it.shippingOptionObject.isNotEmpty()) {
                    if (it.shippingOptionObject.size===1){
                        myProductDetails2Binding.tvShippingOptions.show()
                        myProductDetails2Binding.tvShippingOptions.text =
                            it.shippingOptionObject[0].shippingOptionName.toString()
                    }
                    else if (it.shippingOptionObject.size===2){
                        myProductDetails2Binding.tvShippingOptions.show()
                        myProductDetails2Binding.tvShippingOptions.text =
                            it.shippingOptionObject[0].shippingOptionName.toString()
                        myProductDetails2Binding.tvShippingOptionsTwo.show()
                        myProductDetails2Binding.tvShippingOptionsTwo.text =
                            it.shippingOptionObject[1].shippingOptionName.toString()
                    }

                }
                else{
                    myProductDetails2Binding.tvShippingOptions.hide()
                    myProductDetails2Binding.tvShippingOptionsTwo.hide()
                }
            }
        }
        productDetialsViewModel.paymentOptionObserver.observe(this) {
            if (it.status_code == 200) {
                it.shippingOptionObject?.let { list ->
                    for (item in list) {
                        when (item.paymentOptionId) {
                            AddProductObjectData.PAYMENT_OPTION_CASH -> {
                                myProductDetails2Binding.contianerCash.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_BANk -> {
                                myProductDetails2Binding.contianerBankAccount.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_Mada -> {
                                myProductDetails2Binding.containerMada.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                                myProductDetails2Binding.containerMaster.show()
                            }
                        }
                    }
                }
            }
        }

        productDetialsViewModel!!.changeBusinessAccountObserver.observe(this) {
            if (it.status_code == 200 && it.businessAccount != null) {
                if (it.businessAccount.chanegeAccountUrl != null && it.businessAccount.chanegeAccountUrl != "")
                    openExternalLInk(
                        it.businessAccount.chanegeAccountUrl,
                        it.businessAccount.token ?: ""
                    )
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message, this@MyProductDetailsActivity)
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this@MyProductDetailsActivity
                    )
                }
            }
        }

    }

    private fun setSellerInfo(it: SellerInformation) {
        myProductDetails2Binding.tvErrorNoSellerProduct.hide()
        productDetialsViewModel.getSellerListProduct(
            it.providerId ?: "",
            it.businessAccountId ?: ""
        )
        sellerInformation = it

        if (it.showUserInformation == ShowUserInfo.EveryOne.name) {
            myProductDetails2Binding.containerSellerInfo.show()
        } else if (it.showUserInformation == ShowUserInfo.MembersOnly.name) {
            if (HelpFunctions.isUserLoggedIn()) {
                myProductDetails2Binding.containerSellerInfo.show()
            } else {
                myProductDetails2Binding.containerSellerInfo.hide()
            }
        } else {
            myProductDetails2Binding.containerSellerInfo.hide()
        }

        Extension.loadImgGlide(
            this,
            it.image,
            myProductDetails2Binding.sellerPicture,
            binding.loader
        )
        if (it.businessAccountId == null) {
            myProductDetails2Binding.txtTypeUser.text = getString(R.string.personal)
        } else {
            myProductDetails2Binding.txtTypeUser.text = getString(R.string.merchant)
        }
        myProductDetails2Binding.sellerName.text = it.name ?: ""
        myProductDetails2Binding.memberSinceTv.text = HelpFunctions.getViewFormatForDateTrack(
            it.createdAt ?: "", "dd/MM/yyyy"
        )
        myProductDetails2Binding.sellerCity.text = it.city ?: ""
        myProductDetails2Binding.sellerNumber.text = it.phone ?: ""
        if (it.isFollowed) {
            myProductDetails2Binding.ivSellerFollow.setImageResource(R.drawable.notification)
        } else {
            myProductDetails2Binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
        }
        if (it.businessAccountId != "") {
            myProductDetails2Binding.btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                myProductDetails2Binding.btnMapSeller.show()
            } else {
                myProductDetails2Binding.btnMapSeller.hide()
            }
        }

        when (it.rate) {
            3f -> {
                myProductDetails2Binding.ivRateSeller.setImageResource(R.drawable.happyface_color)
            }

            2f -> {
                myProductDetails2Binding.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }

            1f -> {
                myProductDetails2Binding.ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                myProductDetails2Binding.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }
        }
        if (it.instagram != null && it.instagram != "") {
            myProductDetails2Binding.instagramBtn.show()
        } else {
            myProductDetails2Binding.instagramBtn.hide()
        }
        if (it.youTube != null && it.youTube != "") {
            myProductDetails2Binding.youtubeBtn.show()
        } else {
            myProductDetails2Binding.youtubeBtn.hide()
        }
        if (it.skype != null && it.skype != "") {
            myProductDetails2Binding.skypeBtn.show()
        } else {
            myProductDetails2Binding.skypeBtn.hide()
        }
        if (it.faceBook != null && it.faceBook != "") {
            myProductDetails2Binding.facebookBtn.show()
        } else {
            myProductDetails2Binding.facebookBtn.hide()
        }
        if (it.twitter != null && it.twitter != "") {
            myProductDetails2Binding.twitterBtn.show()
        } else {
            myProductDetails2Binding.twitterBtn.hide()
        }
        if (it.linkedIn != null && it.linkedIn != "") {
            myProductDetails2Binding.linkedInBtn.show()
        } else {
            myProductDetails2Binding.linkedInBtn.hide()
        }
        if (it.tikTok != null && it.tikTok != "") {
            myProductDetails2Binding.tiktokBtn.show()
        } else {
            myProductDetails2Binding.tiktokBtn.hide()
        }
        if (it.snapchat != null && it.snapchat != "") {
            myProductDetails2Binding.snapChatBtn.show()
        } else {
            myProductDetails2Binding.snapChatBtn.hide()
        }

        if (it.businessAccountId != "") {
            myProductDetails2Binding.btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                if (it.lat != 0.0 && it.lon != 0.0) {
                    myProductDetails2Binding.btnMapSeller.show()
                }
            } else {
                myProductDetails2Binding.btnMapSeller.hide()
            }
        }
    }


    private fun setSellerAdapter() {
        sellerSimilerProductList = ArrayList()
        sellerProductAdapter =
            ProductHorizontalAdapter(sellerSimilerProductList, object : SetOnProductItemListeners {
                override fun onProductSelect(position: Int,productID:Int,categoryID:Int,userId:String,providerId:String,businessAccountId:String) {
                    goToProductDetails(productID)
                }

                override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
                    addSellerPorductToFav(position, productID)

                }

                override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

                }

            }, 0, true)
        productDetailsItem2Binding.rvSellerProduct.apply {
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = sellerProductAdapter
        }
    }

    private fun addSellerPorductToFav(position: Int, productID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_last_seller_Products_status
            added_position_from_last_Product = position
            productDetialsViewModel.addProductToFav(productID)
        } else {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                ).apply {})
        }
    }

    private fun setupViewAdapters() {
        setSpecificationAdapter()
        setupProductImagesAdapter()
        setReviewsAdapter()
        setQuestionAnswerAdapter()
        setSellerAdapter()
    }

    private fun setQuestionAnswerAdapter() {
        subQuestionsList = ArrayList()
        questionAnswerAdapter = QuestionAnswerAdapter(subQuestionsList, this)
        myProductDetails2Binding.rvQuestionForProduct.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = questionAnswerAdapter
        }
    }

    private fun setQuestionsView(data: List<QuestionItem>) {
        questionsList = data
        val datalist = questionsList.take(3)
        subQuestionsList.clear()
        subQuestionsList.addAll(datalist)
        questionAnswerAdapter.notifyDataSetChanged()

        lifecycleScope.launch(Dispatchers.IO) {
            var numberOfNotAnswerYet = 0
            for (question in questionsList) {
                if (question.answer == null || question.answer == "") {
                    numberOfNotAnswerYet += 1
                }
            }
            withContext(Dispatchers.Main) {
                myProductDetails2Binding.tvNumberQuestionNotAnswer.text = getString(
                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
                    numberOfNotAnswerYet.toString()
                )

            }
        }
    }

    private fun setReviewsAdapter() {
        mainRatesList = ArrayList()
        smallRatesList = ArrayList()
        reviewProductAdapter = ReviewProductAdapter(smallRatesList)
        myProductDetails2Binding.rvReview.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = reviewProductAdapter
        }
    }

    private fun setReviewRateView(data: List<RateReviewItem>) {
        mainRatesList.clear()
        mainRatesList.addAll(data)
        var datalist = mainRatesList.take(3)
        smallRatesList.clear()
        smallRatesList.addAll(datalist)
        reviewProductAdapter.notifyDataSetChanged()

        if (mainRatesList.isEmpty()) {
            myProductDetails2Binding.tvReviewsError.show()
            myProductDetails2Binding.contianerRateText.hide()
        } else {
            myProductDetails2Binding.tvReviewsError.hide()
            myProductDetails2Binding.contianerRateText.show()
        }
    }

    private fun setupProductImagesAdapter() {
        productImagesList = ArrayList()
        productImagesAdapter = ProductImagesAdapter(
            productImagesList,
            object : ProductImagesAdapter.SetOnSelectedImage {
                override fun onSelectImage(position: Int) {
                    urlImg = productImagesList[position].url
                    if (productImagesList[position].type == 2) {

                        //video
                        startActivity(
                            Intent(
                                this@MyProductDetailsActivity,
                                PlayActivity::class.java
                            ).putExtra(
                                "videourl",
                                productImagesList[position].url
                            )
                        )
                    } else {

                        imgPosition = position
                        binding.myProductimg.tag = productImagesList[position].url
                        //==zoom image
                        val intent = Intent(
                            this@MyProductDetailsActivity,
                            ImageViewLargeActivity::class.java
                        )
                        intent.putParcelableArrayListExtra("imgList", productImagesList)
                        intent.putExtra("UrlImg", productImagesList[position].url)
                        startActivity(intent)
                    }
                }

            })
        binding.rvProductImages.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = productImagesAdapter
        }
    }


    private fun setSpecificationAdapter() {
        specificationList = ArrayList()
        specificationAdapter = SpecificationAdapter(specificationList)

        if (specificationList.isNullOrEmpty()) {
            // Hide the RecyclerView and layout if the list is empty
            myProductDetails2Binding.linearItemSpecification.visibility = View.GONE
            myProductDetails2Binding.relativeProductSpecification.visibility = View.GONE
            myProductDetails2Binding.linearDividerForSpecification.visibility = View.GONE
            productDetailsItem2Binding.laySpec.hide()
        } else {
            // Show the RecyclerView and layout if the list has items
            myProductDetails2Binding.linearItemSpecification.visibility = View.VISIBLE
            myProductDetails2Binding.relativeProductSpecification.visibility = View.VISIBLE
            myProductDetails2Binding.linearDividerForSpecification.visibility = View.VISIBLE
            productDetailsItem2Binding.laySpec.show()
        }

        // Set up RecyclerView
        myProductDetails2Binding.rvProductSpecification.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = specificationAdapter
        }
    }

    /**set product data*/

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        // Get the container from the included layout
        val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
        containerMainProduct.hide()
        binding.containerShareAndFav.hide()
        productDetialsViewModel.getProductDetailsById(productId)
        productDetialsViewModel.getSimilarProduct(productId, 1)
        productDetialsViewModel.getListOfQuestions(productId)
        productDetialsViewModel.getProductRatesForProductDetails(productId)
        productDetialsViewModel.getSellerInfo(productId)
        productDetialsViewModel.getProductShippingOptions(productId)
        productDetialsViewModel.getProductPaymentOptions(productId)
        productDetialsViewModel.getBidsPersons(productId)
    }


    @SuppressLint("SetTextI18n")
    private fun setProductData(productDetails: Product?) {
        if (productDetails != null) {
            val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
            containerMainProduct.hide()
            containerMainProduct.show()
            binding.containerShareAndFav.show()
            /**Action endTime**/
            if (productDetails.auctionClosingTime != null) {
                myProductDetails2Binding.containerAuctioncountdownTimerBar.show()
                var endDate: Date? =
                    HelpFunctions.getAuctionClosingTimeByDate(productDetails.auctionClosingTime)
                if (endDate != null) {
                    timeDifferent(productDetails.auctionClosingTime)
                } else {
                    myProductDetails2Binding.containerAuctioncountdownTimerBar.hide()
                }

            } else {
                myProductDetails2Binding.containerAuctioncountdownTimerBar.hide()
            }
            /**product iamges*/
            Extension.loadThumbnail(
                this,
                productDetails.productImage,
                binding.myProductimg,
                binding.loader
            )


            myProductDetails2Binding.txtCountNegotiation.text =
                productDetails.negotiationOffersCount.toString()
            myProductDetails2Binding.txtCountPurchase.text =
                productDetails.purchasedQuantity.toString()
            myProductDetails2Binding.txtCountFav.text =
                productDetails.addedToFavoritsCount.toString()
            myProductDetails2Binding.tvPriceProductDisc.text =
                "${productDetails.priceDisc} ${getString(R.string.rial)}"

            productPrice = productDetails.priceDisc

            binding.myProductimg.setTag(productDetails.productImage)
            if (productDetails.listMedia != null) {
                binding.otherImageLayout.show()
                productImagesList.clear()
                if (productDetails.listMedia.isNotEmpty())
                    urlImg = productDetails.listMedia[0].url
                productImagesList.addAll(productDetails.listMedia)
                productImagesAdapter.notifyDataSetChanged()
                setPagerDots(mapImageSelectModelToHomeSliderItem(productDetails.listMedia))

            } else {
                binding.otherImageLayout.hide()
            }
            /**product data*/
            myProductDetails2Binding.tvProductReview.text =
                "${productDetails.viewsCount} ${getString(R.string.Views)} - #${productDetails.id} - ${
                    HelpFunctions.getViewFormatForDateTrack(
                        productDetails.createdAt, "dd/MM/yyyy"
                    )
                }"
            myProductDetails2Binding.tvProductItemName.text = productDetails.name ?: ""
            myProductDetails2Binding.tvProductSubtitle.text = productDetails.subTitle ?: ""


            val description = productDetails.description ?: ""
            if (description.isNullOrBlank()) {
                myProductDetails2Binding.textItemDetails.visibility = View.GONE
                myProductDetails2Binding.relativeItemDetails.visibility = View.GONE
                myProductDetails2Binding.btnMoreItemDetails.visibility = View.GONE
                myProductDetails2Binding.linearDividerForItemDetails.visibility = View.GONE
            } else {
                myProductDetails2Binding.textItemDetails.visibility = View.VISIBLE
                myProductDetails2Binding.relativeItemDetails.visibility = View.VISIBLE
                myProductDetails2Binding.btnMoreItemDetails.visibility = View.VISIBLE
                myProductDetails2Binding.linearDividerForItemDetails.visibility = View.VISIBLE
            }

            myProductDetails2Binding.tvProductDescriptionShort.text =
                productDetails.description ?: ""
            myProductDetails2Binding.tvProductDescriptionLong.text =
                productDetails.description ?: ""
            val isEllipsize: Boolean =
                myProductDetails2Binding.tvProductDescriptionShort.text.toString()
                    .trim() != productDetails.description?.trim()
            if (isEllipsize) {
                myProductDetails2Binding.btnMoreItemDetails.show()
            } else {
                myProductDetails2Binding.btnMoreItemDetails.hide()
            }

            myProductDetails2Binding.txtHighAuction.text =
                "${productDetails.auctionMinimumPrice} ${getString(R.string.Rayal)}"
            myProductDetails2Binding.txtLowAuction.text =
                "${productDetails.auctionStartPrice} ${getString(R.string.Rayal)}"



            myProductDetails2Binding.txtPrice.text =
                "${productDetails.price} ${getString(R.string.Rayal)}"

            myProductDetails2Binding.btnMoreItemDetails.setOnClickListener {

                if (getString(R.string.Showmore) == myProductDetails2Binding.btnMoreItemDetails.text.toString() && isEllipsize) {
                    myProductDetails2Binding.btnMoreItemDetails.text = getString(R.string.showLess)
                    myProductDetails2Binding.tvProductDescriptionLong.show()
                    myProductDetails2Binding.tvProductDescriptionShort.hide()
                } else if (getString(R.string.showLess) == myProductDetails2Binding.btnMoreItemDetails.text.toString()) {
                    myProductDetails2Binding.btnMoreItemDetails.text = getString(R.string.Showmore)
                    myProductDetails2Binding.tvProductDescriptionLong.hide()
                    myProductDetails2Binding.tvProductDescriptionShort.show()
                }

            }
            /**specification*/
            if (productDetails.listProductSep != null) {
                myProductDetails2Binding.tvErrorNoSpecification.hide()
                specificationList.clear()
                specificationList.addAll(productDetails.listProductSep)
                specificationAdapter.notifyDataSetChanged()
            } else {
                myProductDetails2Binding.tvErrorNoSpecification.show()
            }
            myProductDetails2Binding.containerDeleteProduct.setOnClickListener {
                if (productDetails.isFixedPriceEnabled) {
                    productDetialsViewModel.removeProduct(productDetails.id)
                } else {

                }
            }
            /**pidding views*/
            productfavStatus = productDetails.isFavourite
            if (productDetails.isFavourite) {
                binding.ivFav.setImageResource(R.drawable.starcolor)
                binding.ivFav.setColorFilter(resources.getColor(R.color.orange))
            } else {
                binding.ivFav.setImageResource(R.drawable.star)
            }

            if (productDetails.isFixedPriceEnabled) {
                if (productDetails.price.toDouble() == 0.0) {
                    myProductDetails2Binding.layPrice.visibility = View.GONE
                } else
                    myProductDetails2Binding.layPrice.visibility = View.VISIBLE

                myProductDetails2Binding.containerDeleteProduct.text =
                    getString(R.string.deleteProduct)
            }

            if (productDetails.isAuctionEnabled) {
                myProductDetails2Binding.layAuction.show()
                myProductDetails2Binding.layPrice.hide()
                myProductDetails2Binding.containerDeleteProduct.text =
                    getString(R.string.deleteAuction)
            } else {
                myProductDetails2Binding.layAuction.hide()
            }



            if (isMyProductForSale) {
                if (productDetails.businessAccountId != null && productDetails.providerId == ConstantObjects.logged_userid) {
                    myProductDetails2Binding.layInfo.hide()
                    myProductDetails2Binding.editProduct.hide()
                    myProductDetails2Binding.discountProduct.hide()
                    myProductDetails2Binding.containerDeleteProduct.hide()
                    myProductDetails2Binding.manageProduct.show()
                } else {
                    myProductDetails2Binding.layInfo.show()
                    myProductDetails2Binding.editProduct.show()
                    myProductDetails2Binding.containerDeleteProduct.show()
                    val fixedPriceEnabled = productDetails.isFixedPriceEnabled
                    if (fixedPriceEnabled)
                        myProductDetails2Binding.discountProduct.show()
                    else
                        myProductDetails2Binding.discountProduct.hide()
                    myProductDetails2Binding.manageProduct.hide()
                }
            } else {
                myProductDetails2Binding.layInfo.hide()
                myProductDetails2Binding.editProduct.hide()
                myProductDetails2Binding.discountProduct.hide()
                myProductDetails2Binding.containerDeleteProduct.hide()
                myProductDetails2Binding.manageProduct.hide()
                binding.containerShareAndFav.hide()
            }


        } else {
            showError(getString(R.string.serverError))
        }
    }

    private fun timeDifferent(targetDateTimeString: String) {
        // Specify the target date and time

        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
        val targetDateTime = formatter.parseDateTime(targetDateTimeString)

        // Get the current date and time
        val currentDateTime = DateTime()

        // Calculate the duration between the current time and the target time
        val duration = Duration(currentDateTime, targetDateTime)

        // Get the difference in days, hours, and minutes as Long values
        val daysDifference = duration.standardDays
        val hoursDifference = duration.standardHours % 24
        val minutesDifference = duration.standardMinutes % 60

        // Display the difference
        val differenceMessage = String.format(
            "Difference: %d days, %d hours, %d minutes",
            daysDifference, hoursDifference, minutesDifference
        )


        myProductDetails2Binding.days.text = daysDifference.toString()
        myProductDetails2Binding.hours.text = hoursDifference.toString()
        myProductDetails2Binding.minutes.text = minutesDifference.toString()
    }


    override fun onProductSelect(
        position: Int,
        productID: Int,
        categoryID: Int,
        userId: String,
        providerId: String,
        businessAccountId: String
    ) {
        goToProductDetails(productID)
    }

    private fun goToProductDetails(productID: Int) {
        startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, productID)
            putExtra("Template", "")
        })
        finish()
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_last_similerProducts_status
            added_position_from_last_Product = position
            productDetialsViewModel.addProductToFav(productID)
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
        var returnIntent = Intent()
        returnIntent.putExtra(ConstantObjects.productIdKey, productId)
        returnIntent.putExtra(ConstantObjects.productFavStatusKey, productfavStatus)
        if (favAddingChange) {
            returnIntent.getBooleanExtra(ConstantObjects.isSuccess, false).let {
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java).apply {})
                } else {
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }

        } else {
            returnIntent.getBooleanExtra(ConstantObjects.isSuccess, false).let {
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java).apply {})
                } else {
                    finish()
                }
            }
        }


    }

    override fun onSelectQuestion(position: Int) {
        if (isMyProduct) {
            val answerDialog = AnswerQuestionDialog(
                productDetialsViewModel,
                this,
                questionsList[position],
                position,
                object : AnswerQuestionDialog.SetOnSendAnswer {
                    override fun onAnswerSuccess(questionItem: QuestionItem, position: Int) {
                        subQuestionsList[position] = questionItem
                        questionAnswerAdapter.notifyItemChanged(position)
                    }
                })
            answerDialog.show()


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sellerProductAdapter.onDestroyHandler()
        productDetialsViewModel.closeAllCall()
        productDetialsViewModel.baseCancel()
    }

    override fun onClickImage(url: String) {

    }

    private fun openExternalLInk(
        chanegeAccountUrl: String,
        token: String
    ) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(chanegeAccountUrl)
            val bundle = Bundle()
            bundle.putString("Authorization", "Bearer $token")
            i.putExtra(Browser.EXTRA_HEADERS, bundle)
            startActivity(i)
        } catch (e: java.lang.Exception) {
            HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
        }

    }

}

