package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
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
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.*
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity.AddRateProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity.ProductReviewsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.CartActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.AddressPaymentActivity
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
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.BidPersonsDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.ListenerSlider
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.SliderAdaptor
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.shared
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.getDifference
import com.malqaa.androidappp.newPhase.utils.activitiesMain.PlayActivity
import com.malqaa.androidappp.newPhase.utils.helper.*
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_product_details2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import kotlinx.android.synthetic.main.atrribute_item.view.*
import kotlinx.android.synthetic.main.item_image_for_product_details.view.*
import kotlinx.android.synthetic.main.item_review_product.*
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.util.*


@SuppressLint("SetTextI18n")
class ProductDetailsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, QuestionAnswerAdapter.SetonSelectedQuestion,
    BuyCurrentPriceDialog.OnAttachedCartMethodSelected, ListenerSlider {
    val PERMISSION_PHONE = 120
    var addProductReviewRequestCode = 1000
    lateinit var product: Product
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val INTERVAL: Long = 10000 // 1 minute in milliseconds
    var hideBars = MutableLiveData<Boolean>(false)
    lateinit var questionAnswerAdapter: QuestionAnswerAdapter
    lateinit var reviewProductAdapter: ReviewProductAdapter
    lateinit var smallRatesList: ArrayList<RateReviewItem>
    lateinit var mainRatesList: ArrayList<RateReviewItem>
    lateinit var sellerProductAdapter: ProductHorizontalAdapter
    lateinit var similarProductAdapter: ProductHorizontalAdapter

    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    private var productDetails: Product? = null
    private var productId: Int = -1
    var urlImg = ""
    lateinit var specificationAdapter: SpecificationAdapter
    lateinit var specificationList: ArrayList<DynamicSpecificationSentObject>
    lateinit var productImagesAdapter: ProductImagesAdapter
    lateinit var productImagesList: ArrayList<ImageSelectModel>
    lateinit var similerProductList: ArrayList<Product>
    lateinit var sellerSimilerProductList: ArrayList<Product>
    var questionsList: List<QuestionItem> = ArrayList()
    lateinit var subQuestionsList: ArrayList<QuestionItem>

    var imgPosition = 0

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
    var sellerInformation: SellerInformation? = null
    lateinit var priceNegotiationDialog: PriceNegotiationDialog
    var bidCount: Int = 0
    var productPrice: Float = 0f
    var comeFrom = ""

    private lateinit var buyCurrentPriceDialog: BuyCurrentPriceDialog
    val sellerInformationLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent: Intent? = result.data
                if (intent != null) {
                    val isFollow = intent.getBooleanExtra("isFollow", false)
                    sellerInformation?.isFollowed = isFollow
                    if (isFollow) {
                        ivSellerFollow.setImageResource(R.drawable.notification)
                    } else {
                        ivSellerFollow.setImageResource(R.drawable.notification_log)
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details2)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, -1)
        println("hhhh product if $productId")
        comeFrom = intent.getStringExtra("ComeFrom") ?: ""

        isMyProduct = intent.getBooleanExtra(ConstantObjects.isMyProduct, false)
        setViewChanges()
        setProductDetailsViewModel()
        setupViewClickListeners()
        setupViewAdapters()

        onRefresh()
        if (HelpFunctions.isUserLoggedIn()) {
            userData = Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
            productDetialsViewModel.addLastViewedProduct(productId)
        }


    }

    private fun callGetPriceCart(nameProduct: String) {

        if (SharedPreferencesStaticClass.getMasterCartId().toInt() != 0) {
            if (productPrice != 0f) {
                productDetialsViewModel.getCartTotalPrice()
                productDetialsViewModel.getCartPrice.observe(this) {

                    openBuyCurrentPriceDialog(
                        "${
                            productPrice + it.data.toString().toFloat()
                        } ${getString(R.string.sar)}", nameProduct
                    )
                    productDetialsViewModel.getCartPrice.removeObservers(this)

                }
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.productPriceRequire), this)
            }
        } else {
            openBuyCurrentPriceDialog(
                "${productDetails?.priceDisc} ${getString(R.string.sar)}",
                nameProduct
            )
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
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            fbButtonBack.scaleX = 1f
        } else {
            fbButtonBack.scaleX = -1f
        }
        btnMapSeller.hide()
        containerMyBid.hide()
        containerAuctionNumber.hide()
        containerMainProduct.hide()
        other_image_layout.hide()
        btnMoreSpecification.hide()
        btnMoreItemDetails.hide()
        contaienrSimilerProduts.hide()
        containerBidOnPrice.hide()
        tvShippingOptions.hide()
        contianerBankAccount.hide()
        containerMada.hide()
        containerMaster.hide()
        contianerCash.hide()
        containerAuctioncountdownTimer_bar.hide()
        //for reviewa
        tvReviewsError.hide()
        contianerRateText.hide()
        containerSellerInfo.hide()
        containerSellerProduct.hide()
        //====
        tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")
        if (HelpFunctions.isUserLoggedIn()) {
            containerMainAskQuestion.show()
            //  containerBuyButtons.show()
        } else {
            containerMainAskQuestion.hide()
            // containerBuyButtons.hide()
        }
        if (isMyProduct) {
            containerMainAskQuestion.hide()
        } else {
            containerMainAskQuestion.show()
        }
    }

    private fun showProductApiError(message: String) {
        if (productDetails == null) {
            containerMainProduct.hide()
            containerShareAndFav.hide()
        }
        HelpFunctions.ShowLongToast(message, this)
    }


    /**set view listeners*/

    private fun setupViewClickListeners() {

        productimg.setOnClickListener {
//            val customDialog = OpenImgLargeDialog(this, urlImg)
//            if (!customDialog.isShowing)
//                customDialog.show()

            val intent = Intent(this@ProductDetailsActivity, ImageViewLargeActivity::class.java)
            intent.putParcelableArrayListExtra("imgList", productImagesList)
            intent.putExtra("UrlImg", productimg.tag.toString())
            startActivity(intent)
        }

        ivSellerFollow.setOnClickListener {
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
        containerAuctionNumber.setOnClickListener {

            val bidPersonsDialog = BidPersonsDialog(
                "${productDetails?.highestBidPrice} ${getString(R.string.Rayal)}",
                this,
                productId,
                object : BidPersonsDialog.SetOnAddBidOffersListeners {
                    override fun onAddOpenBidOfferDailog(bidsList: List<String>) {
                    }

                    override fun onOpenAuctionDialog() {
                        openBidPrice()
                    }

                }, true
            )
            bidPersonsDialog.show()
        }
        tvAddReview.setOnClickListener {
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
            openLocationInMap(sellerInformation?.branches ?: arrayListOf())

        }



        ivFav.setOnClickListener {
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
        fbButtonBack.setOnClickListener {
            onBackPressed()
        }
        btnPriceNegotiation.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                openPriceNegotiationDialog(productDetails?.qty ?: 0)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        btnShare.setOnClickListener {
            shared("http://advdev-001-site1.dtempurl.com/Home/GetProductById?id=$productId")
            //shared("${Constants.HTTP_PROTOCOL}://${Constants.SERVER_LOCATION}/Advertisement/Detail/$AdvId")
        }

        tvQuestionAndAnswersShowAll.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                    putExtra(ConstantObjects.isMyProduct, isMyProduct)
                })
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }

        }


        tvShowAllReviews.setOnClickListener {
            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)
            })


        }
        btnSellerProducts.setOnClickListener {
            if (containerSellerProduct.isVisible) {
                containerSellerProduct.hide()
                seller_product_tv.text = getString(R.string.view_similar_product_from_seller)
                isSellerProductHide_iv.setImageResource(R.drawable.down_arrow)
            } else {
                containerSellerProduct.show()
                seller_product_tv.text = getString(R.string.showLess)
                isSellerProductHide_iv.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        containerSellerInfo.setOnClickListener {
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
        containerSellerImage.setOnClickListener {
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


//            if (sellerInformation != null) {
//                sellerInformationLaucher.launch(
//                    Intent(
//                        this,
//                        SellerInformationActivity::class.java
//                    ).apply {
//                        putExtra(ConstantObjects.sellerObjectKey, sellerInformation)
//                    })
//            }
        }
        containerCurrentPriceBuy.setOnClickListener {
            callGetPriceCart(productDetails?.name ?: "")

        }

        containerBuyNow.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                productDetialsViewModel.callBuyNow(productDetails?.id ?: 0)
            } else {
                goToSignInActivity()
            }

        }
        containerBidOnPrice.setOnClickListener {
            openBidPrice()

        }
        contianerAskQuestion.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                confirmAskQues()
            } else {
                goToSignInActivity()
            }
        }

        seller_number.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + seller_number.text.toString())

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

        btnNextImage.setOnClickListener {
            try {
                if (productImagesList.size > 0) {
                    val position = getLastVisiblePosition(rvProductImages)
//                println("hhhh "+ position+" "+(productImagesList.size-1))
                    if (position < productImagesList.size - 1) {
                        rvProductImages.smoothScrollToPosition(position + 1)
                    } else {
                        rvProductImages.smoothScrollToPosition(0)
                    }
                }
            } catch (_: Exception) {
            }
        }


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
                    callIntent.data = Uri.parse("tel:" + seller_number.text.toString())
                    startActivity(callIntent)
                } else {
                    HelpFunctions.ShowLongToast("Permission Phone denied", this)
                }
            }
        }
    }

    private fun openBidPrice() {
        if (HelpFunctions.isUserLoggedIn()) {
            productDetails?.let {
                val auctionDialog = AuctionDialog(this,
                    productId,
                    it.auctionStartPrice,
                    it.auctionMinimumPrice,
                    it.highestBidPrice,
                    object : AuctionDialog.SetClickListeners {
                        override fun setOnSuccessListeners(highestBidPrice: Float) {
                            productDetails?.highestBidPrice = highestBidPrice
                            if (productDetails?.highestBidPrice?.toDouble() != 0.0) {
                                Bid_on_price_tv.text =
                                    "${productDetails?.highestBidPrice} ${getString(R.string.Rayal)}"
                            } else {
                                Bid_on_price_tv.text =
                                    "${productDetails?.auctionStartPrice} ${getString(R.string.Rayal)}"
                            }
                            bidCount += 1
                            tvAuctionNumber.text =
                                "${getString(R.string.bidding)} ${bidCount}"
                        }

                    })
                auctionDialog.show()
            }
        } else {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                ).apply {})
        }

    }

    private fun openPriceNegotiationDialog(quantity: Int) {
        priceNegotiationDialog = PriceNegotiationDialog(
            this,
            quantity,
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
        val intent = Intent(this, SuccessAddProductPriceNegotiationActivity::class.java).apply {
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

    private fun openLocationInMap(branches: ArrayList<Branch>) {
//        branches.get(0).location
//        val URL = ("http://maps.google.com/maps?saddr=&daddr=30.2424242,30.54364547&dirflg=d")
//        val location = Uri.parse(URL)
//        val mapIntent = Intent(Intent.ACTION_VIEW, location)
//        // Make the Intent explicit by setting the Google Maps package
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)
//
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
//        hideBars.observe(this){
//            if (it) {
//                containerBidOnPrice.hide()
//            } else
//                containerBidOnPrice.show()
//        }
        productDetialsViewModel.productDetailsObservable.observe(this) { productResp ->

            if (productResp.productDetails != null) {
                productDetails = productResp.productDetails

                productPrice = productResp.productDetails.priceDisc
                setProductData(productDetails)
            } else {
                showProductApiError(productResp.message)
            }
        }
        productDetialsViewModel.addQuestionObservable.observe(this) { questResp ->
            HelpFunctions.ShowLongToast(questResp.message, this)
            if (questResp.status_code == 200) {
                etWriteQuestion.setText("")

                productDetialsViewModel.getListOfQuestions(productId)
            }
        }
        productDetialsViewModel.getSimilarProductObservable.observe(this) { similarProductRes ->
            if (similarProductRes.productList != null) {
                similerProductList.clear()
                similerProductList.addAll(similarProductRes.productList)
                similarProductAdapter.notifyDataSetChanged()
                contaienrSimilerProduts.show()
            }
        }
        productDetialsViewModel.getListOfQuestionsObservable.observe(this) { questionListResp ->
            if (!questionListResp.questionList.isNullOrEmpty()) {
                tvErrorNoQuestion.hide()
                setQuestionsView(questionListResp.questionList)
            } else {
                tvErrorNoQuestion.show()
            }
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

        productDetialsViewModel.getMasterFromBuyNow.observe(this) {
            SharedPreferencesStaticClass.saveMasterCartId(it.data)
            startActivity(Intent(this, AddressPaymentActivity::class.java).apply {
            })

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
        productDetialsViewModel.getRateResponseObservable.observe(this) { rateListResp ->
            if (rateListResp.data.happyCount != 0) {
                linHappy.show()
                txtHappy.text = rateListResp.data.happyCount.toString()
            }
            if (rateListResp.data.satisfiedCount != 0) {
                linSmile.show()
                txtSmile.text = rateListResp.data.satisfiedCount.toString()
            }
            if (rateListResp.data.disgustedCount != 0) {
                linSad.show()
                txtSad.text = rateListResp.data.disgustedCount.toString()
            }

            rating_bar_detail_tv.text =
                "${rateListResp.data.totalRecords} ${getString(R.string.visitors)} "
            if (rateListResp.status_code == 200) {
                setReviewRateView(rateListResp.data.rateProductListDto)
            }
        }
        productDetialsViewModel.addProductToCartObservable.observe(this) { addproductToCartResp ->
            if (addproductToCartResp.status_code == 200) {
                if (SharedPreferencesStaticClass.getCartCount() == 0) {
                    SharedPreferencesStaticClass.saveCartCount(1)
                } else {
                    SharedPreferencesStaticClass.saveCartCount(SharedPreferencesStaticClass.getCartCount() + 1)
                }

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
        productDetialsViewModel.sellerInfoObservable.observe(this) { sellerInfoResp ->
            if (sellerInfoResp.status_code == 200) {
                /**seller info*/
                sellerInfoResp.sellerInformation?.let {
                    setSellerInfo(it)

                }

            }
        }
        productDetialsViewModel.sellerLoading.observe(this) {
            if (it) {
                sellerProgressBar.show()
            } else {
                sellerProgressBar.hide()
            }
        }
        productDetialsViewModel.sellerProductsRespObserver.observe(this) { sellerProductListResp ->
            if (sellerProductListResp.status_code == 200) {
                sellerSimilerProductList.clear()
                sellerProductListResp.productList?.let { sellerSimilerProductList.addAll(it) }
                sellerProductAdapter.notifyDataSetChanged()
                if (sellerSimilerProductList.isEmpty()) {
                    tvErrorNoSellerProduct.show()
                } else {
                    tvErrorNoSellerProduct.hide()
                }
            } else {
                tvErrorNoSellerProduct.show()
            }
        }
        productDetialsViewModel.shippingOptionObserver.observe(this) {
            if (it.status_code == 200) {
                if (!it.shippingOptionObject.isNullOrEmpty()) {
                    when (it.shippingOptionObject[0].shippingOptionId) {
                        ConstantObjects.shippingOption_integratedShippingCompanyOptions -> {
                            tvShippingOptions.show()
                            tvShippingOptions.text = getString(R.string.integratedShippingCompanies)
                        }

                        ConstantObjects.shippingOption_freeShippingWithinSaudiArabia -> {
                            tvShippingOptions.show()
                            tvShippingOptions.text =
                                getString(R.string.free_shipping_within_Saudi_Arabia)
                        }

                        ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer -> {
                            tvShippingOptions.show()
                            tvShippingOptions.text =
                                getString(R.string.arrangementWillBeMadeWithTheBuyer)
                        }
                    }
                } else {
                    tvShippingOptions.show()
                    tvShippingOptions.text = getString(R.string.mustPickUp)
                }
            }
        }
        productDetialsViewModel.paymentOptionObserver.observe(this) {
            if (it.status_code == 200) {
                it.shippingOptionObject?.let { list ->
                    for (item in list) {
                        when (item.paymentOptionId) {
                            AddProductObjectData.PAYMENT_OPTION_CASH -> {
                                contianerCash.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_BANk -> {
                                contianerBankAccount.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_Mada -> {
                                containerMada.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                                containerMaster.show()
                            }
                        }
                    }
                }
            }
        }
        productDetialsViewModel.bidsPersonsObserver.observe(this) {
            if (it.status_code == 200) {
                if (!it.bidPersonsDataList.isNullOrEmpty()) {
                    bidCount = it.bidPersonsDataList.size
                    tvAuctionNumber.text = "${getString(R.string.bidding)} ${bidCount}"
                    if (hideBars.value == true) {
                        containerAuctionNumber.hide()
                    } else
                        containerAuctionNumber.show()
                } else {
                    containerAuctionNumber.hide()
                }
            } else {
                containerAuctionNumber.hide()
            }
        }
        productDetialsViewModel.addSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = true
                ivSellerFollow.setImageResource(R.drawable.notification)
            }
        }
        productDetialsViewModel.removeSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = false
                ivSellerFollow.setImageResource(R.drawable.notification_log)
            }
        }

    }

    private fun setSellerInfo(it: SellerInformation) {
        tvErrorNoSellerProduct.hide()
        productDetialsViewModel.getSellerListProduct(
            it.providerId ?: "",
            it.businessAccountId ?: ""
        )
        sellerInformation = it

        if (it.showUserInformation == ShowUserInfo.EveryOne.name) {
            containerSellerInfo.show()
        } else if (it.showUserInformation == ShowUserInfo.MembersOnly.name) {
            if (HelpFunctions.isUserLoggedIn()) {
                containerSellerInfo.show()
            } else {
                containerSellerInfo.hide()
            }
        } else {
            containerSellerInfo.hide()
        }

        showButtons()
//        containerSellerInfo.show()
        Extension.loadImgGlide(
            this,
            it.image,
            seller_picture,
            loader
        )
        if (it.businessAccountId == null) {
            txtTypeUser.text = getString(R.string.personal)
        } else {
            txtTypeUser.text = getString(R.string.merchant)
        }
        sellerName.text = it.name ?: ""
        member_since_Tv.text = HelpFunctions.getViewFormatForDateTrack(
            it.createdAt ?: "", "dd/MM/yyyy"
        )
        seller_city.text = it.city ?: ""
        seller_number.text = it.phone ?: ""
        if (it.isFollowed) {
            ivSellerFollow.setImageResource(R.drawable.notification)
        } else {
            ivSellerFollow.setImageResource(R.drawable.notification_log)
        }
//        tvRateText.text = it.rate.toString()
        if (it.businessAccountId != "") {
            btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                btnMapSeller.show()
            } else {
                btnMapSeller.hide()
            }
        }

        when (it.rate) {
            3f -> {
                ivRateSeller.setImageResource(R.drawable.happyface_color)
            }

            2f -> {
                ivRateSeller.setImageResource(R.drawable.smileface_color)
            }

            1f -> {
                ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                ivRateSeller.setImageResource(R.drawable.smileface_color)
            }
        }
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
        //println("hhh "+it.providerId+" "+it.businessAccountId)

        if (it.businessAccountId != "") {
            btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                if (it.lat != 0.0 && it.lon != 0.0) {
                    btnMapSeller.show()
                }
            } else {
                btnMapSeller.hide()
            }
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
                tvNumberQuestionNotAnswer.text = getString(
                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
                    numberOfNotAnswerYet.toString()
                )

            }
        }
    }

    private fun setReviewRateView(data: List<RateReviewItem>) {
        mainRatesList.clear()
        mainRatesList.addAll(data)
        val datalist = mainRatesList.take(3)
        smallRatesList.clear()
        smallRatesList.addAll(datalist)
        reviewProductAdapter.notifyDataSetChanged()

        if (mainRatesList.isEmpty()) {
            tvReviewsError.show()
            tvShowAllReviews.hide()
            contianerRateText.hide()
        } else {
            tvShowAllReviews.show()
            tvReviewsError.hide()
            contianerRateText.show()
        }
    }


    private fun setupViewAdapters() {
        setSpeceificationAdapter()
        setQuestionAnswerAdapter()
        setReviewsAdapter()
        setSellerAdapter()
        setSimilarProductAdapter()
        setupProductImagesAdapter()
    }

    private fun setupProductImagesAdapter() {
        productImagesList = ArrayList()
        productImagesAdapter = ProductImagesAdapter(productImagesList,
            object : ProductImagesAdapter.SetOnSelectedImage {
                override fun onSelectImage(position: Int) {
                    urlImg = productImagesList[position].url
                    if (productImagesList[position].type == 2) {     //video
                        startActivity(
                            Intent(
                                this@ProductDetailsActivity,
                                PlayActivity::class.java
                            ).putExtra(
                                "videourl",
                                productImagesList[position].url
                            )
                        )
                    } else {

                        imgPosition = position
                        productimg.tag = productImagesList[position].url
//                        //==zoom image
//                        Extension.loadImgGlide(
//                            this@ProductDetailsActivity,
//                            productImagesList[position].url,
//                            productimg,
//                            loader
//                        )

                        val intent =
                            Intent(this@ProductDetailsActivity, ImageViewLargeActivity::class.java)
                        intent.putParcelableArrayListExtra("imgList", productImagesList)
                        intent.putExtra("UrlImg", productImagesList[position].url)
                        startActivity(intent)
                    }
                }

            })
        rvProductImages.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = productImagesAdapter
        }
    }

    private fun setSimilarProductAdapter() {
        similerProductList = ArrayList()
        similarProductAdapter = ProductHorizontalAdapter(similerProductList, this, 0, true)
        rvSimilarProducts.apply {
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = similarProductAdapter
        }
    }

    private fun setSellerAdapter() {
        sellerSimilerProductList = ArrayList()
        sellerProductAdapter =
            ProductHorizontalAdapter(sellerSimilerProductList, object : SetOnProductItemListeners {
                override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
                    goToProductDetails(productID)
                }

                override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
                    addSellerPorductToFav(position, productID)

                }

                override fun onShowMoreSetting(position: Int, productID: Int, categoryID: Int) {

                }

            }, 0, true)
        rvSellerProduct.apply {
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

    private fun setReviewsAdapter() {
        mainRatesList = ArrayList()
        smallRatesList = ArrayList()
        reviewProductAdapter = ReviewProductAdapter(smallRatesList)
        rv_review.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = reviewProductAdapter
        }
    }

    private fun setSpeceificationAdapter() {
        specificationList = ArrayList()
        specificationAdapter = SpecificationAdapter(specificationList)
        if (specificationList.size != 0)
            laySpec.show()
        rvProductSpecification.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = specificationAdapter
        }
    }

    private fun setQuestionAnswerAdapter() {
        subQuestionsList = ArrayList()
        questionAnswerAdapter = QuestionAnswerAdapter(subQuestionsList, this)
        rvQuestionForProduct.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = questionAnswerAdapter
        }
    }

    /**set product data*/

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        containerMainProduct.hide()
        containerShareAndFav.hide()
        containerSellerInfo.hide()
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
            containerMainProduct.show()
            containerShareAndFav.show()
            /**Action endTime**/

            if (productDetails.acceptQuestion) {
                sectionQs.show()
            } else {
                hintQuestion.show()
                sectionQs.hide()
            }

            showButtons()
            if (productDetails.auctionClosingTime != null) {
                handler = Handler()
                runnable = object : Runnable {
                    override fun run() {
                        val endDate: Date? =
                            HelpFunctions.getAuctionClosingTimeByDate(productDetails.auctionClosingTime)
                        if (endDate != null) {
                            hideBars.value=  getDifference(productDetails.auctionClosingTime,
                                containerAuctioncountdownTimer_bar,titleDay,days,titleHours,hours,titleMinutes,minutes,titleSeconds,seconds,containerAuctionNumber
                            )
                        } else {
                            containerAuctioncountdownTimer_bar.hide()
                        }
                        handler.postDelayed(this, INTERVAL)
                    }
                }
                handler.post(runnable)
            } else {
                containerAuctioncountdownTimer_bar.hide()
            }
            /**product iamges*/
            Extension.loadImgGlide(
                this,
                productDetails.productImage,
                productimg,
                loader
            )
            productimg.setTag(productDetails.productImage)
            if (productDetails.listMedia != null && productDetails.listMedia.size != 0) {
                other_image_layout.show()
                productImagesList.clear()
                if (productDetails.listMedia.isNotEmpty())
                    urlImg = productDetails.listMedia[0].url
                productImagesList.addAll(productDetails.listMedia)
                productImagesAdapter.notifyDataSetChanged()

                setPagerDots(mapImageSelectModelToHomeSliderItem(productDetails.listMedia))

            } else {
                productImagesList.add(ImageSelectModel(url = productDetails.productImage.toString()))
                other_image_layout.hide()
            }
            /**product data*/
            tvProductReview.text =
                "${productDetails.viewsCount} ${getString(R.string.Views)} - #${productDetails.id} - ${
                    HelpFunctions.getViewFormatForDateTrack(
                        productDetails.createdAt, "dd/MM/yyyy"
                    )
                }"
            tvProductItemName.text = productDetails.name ?: ""
            tvProductSubtitle.text = productDetails.subTitle ?: ""

            if (productDetails.description != "") {
                layDetails.show()
                readMoreTextView.text = productDetails.description ?: ""
//                tvProductDescriptionShort.text = productDetails.description ?: ""
//                tvProductDescriptionLong.text = productDetails.description ?: ""
            }
//            val isEllipsize: Boolean = tvProductDescriptionShort.text.toString().trim() != productDetails.description?.trim()
//            if (isEllipsize) {
////                btnMoreItemDetails.show()
//            } else {
//                btnMoreItemDetails.hide()
//            }
            btnMoreItemDetails.setOnClickListener {

//                if (getString(R.string.Showmore) == btnMoreItemDetails.text.toString() && isEllipsize) {
//                    btnMoreItemDetails.text = getString(R.string.showLess)
//                    tvProductDescriptionLong.show()
//                    tvProductDescriptionShort.hide()
//                } else if (getString(R.string.showLess) == btnMoreItemDetails.text.toString()) {
//                    btnMoreItemDetails.text = getString(R.string.Showmore)
//                    tvProductDescriptionLong.hide()
//                    tvProductDescriptionShort.show()
//                }

            }

            current_price_buy_tv.text =
                "${productDetails.priceDisc.toString()} ${getString(R.string.sar)}"
            Bid_on_price_tv.text = " ${getString(R.string.sar)}"

            /**specification*/
            if (productDetails.listProductSep != null) {
                tvErrorNoSpecification.hide()
                if (productDetails.listProductSep.isNotEmpty())
                    laySpec.show()
                specificationList.clear()
                specificationList.addAll(productDetails.listProductSep)
                specificationAdapter.notifyDataSetChanged()
            } else {
                tvErrorNoSpecification.show()
            }
            /**pidding views*/
            tvAuctionNumber.text = "${getString(R.string.bidding)} "
            productfavStatus = productDetails.isFavourite
            if (productDetails.isFavourite) {
                ivFav.setImageResource(R.drawable.starcolor)
                ivFav.setColorFilter(resources.getColor(R.color.orange))
            } else {
                ivFav.setImageResource(R.drawable.star)
            }
            if (productDetails.isNegotiationEnabled) {
                btnPriceNegotiation.show()
            } else {
                btnPriceNegotiation.hide()
            }

//            tvNegotiationPrice.text = productDetails.price.toString()

            if (productDetails.isAuctionEnabled) {
                if (productDetails.highestBidPrice.toDouble() != 0.0) {
                    Bid_on_price_tv.text =
                        "${productDetails.highestBidPrice} ${getString(R.string.Rayal)}"
                } else {
                    Bid_on_price_tv.text =
                        "${productDetails.auctionStartPrice} ${getString(R.string.Rayal)}"
                }

                if (hideBars.value==true) {
                    containerBidOnPrice.hide()
                } else
                    containerBidOnPrice.show()
            } else {
                containerBidOnPrice.hide()
            }
            if (productDetails.myBid != 0f) {
                containerMyBid.show()
                tvMyBidPrice.text = "${productDetails.myBid} ${getString(R.string.sar)}"
            } else {
                containerMyBid.hide()
            }

        } else {
            showError(getString(R.string.serverError))
        }
    }

    private fun setPagerDots(list: List<HomeSliderItem>) {
        if (list.isNotEmpty()) {

            val viewPagerAdapter = SliderAdaptor(this, list, true, this)
            slider_details.adapter = viewPagerAdapter
//            dots_indicator.attachTo(slider_details)
//            slider_details.startAutoScroll()
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


    @SuppressLint("SetTextI18n")
    private fun showButtons() {
        if (productDetails?.isFixedPriceEnabled == true) {
            containerBuyNow.show()
            txtPriceNow.text = "${productDetails?.priceDisc} ${getString(R.string.sar)}"
            if (sellerInformation?.businessAccountId != null) {
                containerCurrentPriceBuy.show() // AddToCart
            } else {
                containerCurrentPriceBuy.hide()
            }
        } else {
            containerBuyNow.hide()
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
        val secondDifference = duration.standardSeconds % 60

        // Display the difference
        val differenceMessage = String.format(
            "Difference: %d days, %d hours, %d minutes",
            daysDifference, hoursDifference, minutesDifference
        )


        if (daysDifference <= 0 && (hoursDifference <= 0) && (minutesDifference <= 0)) {
            hideBars.value = true
            containerAuctionNumber.hide()
            containerAuctioncountdownTimer_bar.hide()
        } else {
            hideBars.value = false
            containerAuctionNumber.show()
            containerAuctioncountdownTimer_bar.show()
        }

        if (daysDifference == 0L || (daysDifference < 0L)) {
            days.visibility = View.GONE
            titleDay.visibility = View.GONE
        }
        if (hoursDifference == 0L || (hoursDifference < 0L)) {
            hours.visibility = View.GONE
            titleHours.visibility = View.GONE
        }

        if (minutesDifference == 0L || (minutesDifference < 0L)) {
            minutes.visibility = View.GONE
            titleMinutes.visibility = View.GONE
        }


        days.text = daysDifference.toString()
        hours.text = hoursDifference.toString()
        minutes.text = minutesDifference.toString()
        seconds.text = secondDifference.toString()
    }

    /**send QUestion for sller**/
    private fun confirmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            productDetialsViewModel.addQuestion(productId, etWriteQuestion.text.trim().toString())
        }

    }

    private fun validateAskQuesInputText(): Boolean {
        val Inputemail = etWriteQuestion!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Question)))
            false
        } else {
            true
        }
    }


    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
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
        val returnIntent = Intent()
        returnIntent.putExtra(ConstantObjects.productIdKey, productId)
        returnIntent.putExtra(ConstantObjects.productFavStatusKey, productfavStatus)
        if (favAddingChange) {
            returnIntent.getBooleanExtra(ConstantObjects.isSuccess, false).let {
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java).apply {})
                    finish()
                } else {
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }

        } else {
            returnIntent.getBooleanExtra(ConstantObjects.isSuccess, false).let {
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java).apply {})
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java).apply {})
                    finish()
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == addProductReviewRequestCode) {
            //var addRateItem: AddRateItem? =data?.getParcelableExtra(ConstantObjects.rateObjectKey)
            productDetialsViewModel.getProductRatesForProductDetails(productId)

        }

    }

    override fun onSelectQuestion(position: Int) {
        if (isMyProduct) {
            val answerDialog = AnswerQuestionDialog(productDetialsViewModel,
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
        productDetialsViewModel.closeAllCall()
        productDetialsViewModel.baseCancel()
        handler.removeCallbacks(runnable)
        sellerProductAdapter.onDestroyHandler()
        similarProductAdapter.onDestroyHandler()
    }

    override fun onClickImage(url: String) {
        val intent = Intent(this@ProductDetailsActivity, ImageViewLargeActivity::class.java)
        intent.putParcelableArrayListExtra("imgList", productImagesList)
        intent.putExtra("UrlImg", url)
        startActivity(intent)
    }


//    private fun setListener() {
//
//        fbButtonBack.setOnClickListener {
//            onBackPressed()
//        }
//
//        next_image.setOnClickListener {
//
//        }
//        sellerName.setOnClickListener {
//
//        }
////        tvShowAllReviews.setOnClickListener {
////            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {
////
////            })
////        }
//
//
////        Bid_on_price.setOnClickListener {
////
////            val builder = AlertDialog.Builder(this@ProductDetailsActivity)
////                .create()
////            val view = layoutInflater.inflate(R.layout.bid_alert_box, null)
////            builder.setView(view)
////            view.close_alert_bid.setOnClickListener {
////                builder.dismiss()
////            }
////
////            builder.setCanceledOnTouchOutside(false)
////            builder.show()
////
////
////            view.btn_bid.setOnClickListener {
////                builder.dismiss()
////                AlertDialog.Builder(this@ProductDetailsActivity)
////                    .create().apply {
////                        layoutInflater.inflate(R.layout.bid_confirmation, null).also {
////                            this.setView(it)
////                            it.apply {
////                                close_alert.setOnClickListener {
////                                    dismiss()
////                                }
////
////                                back_to_shopping.setOnClickListener {
////                                    dismiss()
////                                    startActivity(
////                                        Intent(
////                                            this@ProductDetailsActivity,
////                                            MainActivity::class.java
////                                        ).apply {
////                                            flags =
////                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////
////                                        })
////                                    finish()
////
////                                }
////                                manage_bid.setOnClickListener {
////                                    dismiss()
////                                    startActivity(
////                                        Intent(
////                                            this@ProductDetailsActivity,
////                                            MainActivity::class.java
////                                        ).apply {
////                                            flags =
////                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
////                                            putExtra(ConstantObjects.isBid, true)
////                                        })
////                                    finish()
////                                }
////
////                            }
////
////                        }
////                        setCanceledOnTouchOutside(false)
////                        show()
////                    }
////
////
////            }
////
////        }
////        add_to_cart.setOnClickListener {
////            current_price_buy.performClick()
////        }
////        current_price_buy.setOnClickListener {
////            AddToCart()
////        }
//
//
//        btnMapSeller.setOnClickListener {
//            val uri: String =
//                java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", 33.7295, 73.0372)
//            startActivity(
//                Intent(
//                    Intent.ACTION_VIEW, Uri.parse(uri)
//                )
//            )
//        }
//
//    }
//    private fun replyItemClicked(question: Question) {
//
//        answerLayout.isVisible = true
//        cross_reply_layout.setOnClickListener {
//            answerLayout.isVisible = false
//        }
//        quetsion_tv.text = question.question
//        ReplyAnswer_btn.setOnClickListener {
//            ReplyAnswer.text.toString().let {
//                if (it.isEmpty()) {
//                    showError(getString(R.string.Please_enter, getString(R.string.Answer)))
//                } else {
//                    PostAnsApi(question._id, it)
//                }
//            }
//        }
//    }
//    fun getIgnoreCase(jobj: JsonObject, key: String?): String {
//        val iter: Iterator<String> = jobj.keySet().iterator()
//        while (iter.hasNext()) {
//            val key1 = iter.next()
//            if (key1.equals(key, ignoreCase = true)) {
//                if (!jobj[key1].isJsonNull) {
//                    return jobj[key1].asString ?: ""
//                }
//            }
//        }
//        return ""
//    }
//    private fun activeWatch(view: FloatingActionButton) {
//        val myFabSrc = ContextCompat.getDrawable(this@ProductDetailsActivity, R.drawable.starcolor)
//        val willBeWhite = myFabSrc!!.constantState!!.newDrawable()
//        willBeWhite.mutate()
//            .setColorFilter(
//                ContextCompat.getColor(this@ProductDetailsActivity, R.color.bg),
//                PorterDuff.Mode.MULTIPLY
//            )
//        view.setImageDrawable(willBeWhite)
//    }
//    fun PostAnsApi(questionId: String, answer: String) {
//        productDetailHelper.PostAnsApi(questionId, answer) { respone ->
//            if (respone.status_code >= 200 || respone.status_code <= 299) {
//                answerLayout.isVisible = false
//                ReplyAnswer.setText("")
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.Answerhasbeenposted),
//                    this@ProductDetailsActivity
//                )
//
//            }
//        }
//
//    }


}
