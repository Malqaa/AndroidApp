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
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.malqaa.androidappp.databinding.ActivityProductDetails2Binding
import com.malqaa.androidappp.databinding.ActivityProductDetailsItem2Binding
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
import com.malqaa.androidappp.newPhase.utils.VideoDialogFragment
import com.malqaa.androidappp.newPhase.utils.WebViewPlayerDialogFragment
import com.malqaa.androidappp.newPhase.utils.YouTubePlayerDialogFragment
import com.malqaa.androidappp.newPhase.utils.extractYouTubeId
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.isVideoLink
import com.malqaa.androidappp.newPhase.utils.isYouTubeLink
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
import java.util.regex.Pattern

@SuppressLint("SetTextI18n")
class ProductDetailsActivity : BaseActivity<ActivityProductDetails2Binding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, QuestionAnswerAdapter.SetonSelectedQuestion,
    BuyCurrentPriceDialog.OnAttachedCartMethodSelected, ListenerSlider {

    val PERMISSION_PHONE = 120
    var addProductReviewRequestCode = 1000
    lateinit var product: Product
    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    lateinit var productDetailsItem2Binding: ActivityProductDetailsItem2Binding

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
                        productDetailsItem2Binding.ivSellerFollow.setImageResource(R.drawable.notification)
                    } else {
                        productDetailsItem2Binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
                    }
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the main layout using the main binding
        binding = ActivityProductDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root) // Use the main binding to set the content view

        // Inflate the item binding for a specific section if necessary
        productDetailsItem2Binding =
            binding.containerMainProduct // If it's nested in the main layout as an include

        productId = intent.getIntExtra(ConstantObjects.productIdKey, -1)
        comeFrom = intent.getStringExtra("ComeFrom") ?: ""

        isMyProduct = intent.getBooleanExtra(ConstantObjects.isMyProduct, false)

        setViewChanges()
        setProductDetailsViewModel()
        setupViewClickListeners()
        setupViewAdapters()

        if (HelpFunctions.isUserLoggedIn()) {
            userData = Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
            productDetialsViewModel.addLastViewedProduct(productId)
        }

        onRefresh()
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
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            binding.fbButtonBack.scaleX = 1f
        } else {
            binding.fbButtonBack.scaleX = -1f
        }
        productDetailsItem2Binding.btnMapSeller.hide()
        binding.containerMyBid.hide()
        productDetailsItem2Binding.containerAuctionNumber.hide()
        val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
        containerMainProduct.hide()
        binding.otherImageLayout.hide()
        productDetailsItem2Binding.btnMoreSpecification.hide()
        productDetailsItem2Binding.btnMoreItemDetails.hide()
        productDetailsItem2Binding.contaienrSimilerProduts.hide()
        binding.containerBidOnPrice.hide()
        productDetailsItem2Binding.tvShippingOptions.hide()
        productDetailsItem2Binding.contianerBankAccount.hide()
        productDetailsItem2Binding.containerMada.hide()
        productDetailsItem2Binding.containerMaster.hide()
        productDetailsItem2Binding.contianerCash.hide()
        productDetailsItem2Binding.containerAuctioncountdownTimerBar.hide()
        //for reviewa
        productDetailsItem2Binding.tvReviewsError.hide()
        productDetailsItem2Binding.contianerRateText.hide()
        productDetailsItem2Binding.containerSellerInfo.hide()
        productDetailsItem2Binding.containerSellerProduct.hide()
        //====
        productDetailsItem2Binding.tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")
        if (HelpFunctions.isUserLoggedIn()) {
            productDetailsItem2Binding.containerMainAskQuestion.show()
            //  containerBuyButtons.show()
        } else {
            productDetailsItem2Binding.containerMainAskQuestion.hide()
            // containerBuyButtons.hide()
        }
        if (isMyProduct) {
            productDetailsItem2Binding.containerMainAskQuestion.hide()
        } else {
            productDetailsItem2Binding.containerMainAskQuestion.show()
        }
    }

    private fun showProductApiError(message: String) {
        if (productDetails == null) {
            val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
            containerMainProduct.hide()
            binding.containerShareAndFav.hide()
        }
        HelpFunctions.ShowLongToast(message, this)
    }


    /**set view listeners*/

    private fun setupViewClickListeners() {

        binding.productimg.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, ImageViewLargeActivity::class.java)
            intent.putParcelableArrayListExtra("imgList", productImagesList)
            intent.putExtra("UrlImg", binding.productimg.tag.toString())
            startActivity(intent)
        }

        productDetailsItem2Binding.ivSellerFollow.setOnClickListener {
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
        productDetailsItem2Binding.containerAuctionNumber.setOnClickListener {

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
        productDetailsItem2Binding.tvAddReview.setOnClickListener {
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
        productDetailsItem2Binding.skypeBtn.setOnClickListener {
            if (sellerInformation?.skype != null && sellerInformation?.skype != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.skype!!, this)
            }
        }
        productDetailsItem2Binding.youtubeBtn.setOnClickListener {
            if (sellerInformation?.youTube != null && sellerInformation?.youTube != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.youTube!!, this)

            }
        }
        productDetailsItem2Binding.instagramBtn.setOnClickListener {
            if (sellerInformation?.instagram != null && sellerInformation?.instagram != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.instagram!!, this)
            }
        }
        productDetailsItem2Binding.facebookBtn.setOnClickListener {
            if (sellerInformation?.faceBook != null && sellerInformation?.faceBook != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.faceBook!!, this)
            }
        }
        productDetailsItem2Binding.twitterBtn.setOnClickListener {
            if (sellerInformation?.twitter != null && sellerInformation?.twitter != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.twitter!!, this)
            }
        }
        productDetailsItem2Binding.linkedInBtn.setOnClickListener {
            if (sellerInformation?.linkedIn != null && sellerInformation?.linkedIn != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.linkedIn!!, this)
            }
        }
        productDetailsItem2Binding.tiktokBtn.setOnClickListener {
            if (sellerInformation?.tikTok != null && sellerInformation?.tikTok != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.tikTok!!, this)
            }
        }
        productDetailsItem2Binding.snapChatBtn.setOnClickListener {
            if (sellerInformation?.snapchat != null && sellerInformation?.snapchat != "") {
                HelpFunctions.openExternalLInk(sellerInformation?.snapchat!!, this)
            }
        }
        productDetailsItem2Binding.btnMapSeller.setOnClickListener {
            openLocationInMap(sellerInformation?.branches ?: arrayListOf())

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
        productDetailsItem2Binding.btnPriceNegotiation.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                openPriceNegotiationDialog(productDetails?.qty ?: 0)
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
        binding.btnShare.setOnClickListener {
            shared("http://advdev-001-site1.dtempurl.com/Home/GetProductById?id=$productId")
            //shared("${Constants.HTTP_PROTOCOL}://${Constants.SERVER_LOCATION}/Advertisement/Detail/$AdvId")
        }

        productDetailsItem2Binding.tvQuestionAndAnswersShowAll.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                    putExtra(ConstantObjects.isMyProduct, isMyProduct)
                })
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
            }

        }


        productDetailsItem2Binding.tvShowAllReviews.setOnClickListener {
            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)
            })


        }
        productDetailsItem2Binding.btnSellerProducts.setOnClickListener {
            if (productDetailsItem2Binding.containerSellerProduct.isVisible) {
                productDetailsItem2Binding.containerSellerProduct.hide()
                productDetailsItem2Binding.sellerProductTv.text =
                    getString(R.string.view_similar_product_from_seller)
                productDetailsItem2Binding.isSellerProductHideIv.setImageResource(R.drawable.down_arrow)
            } else {
                productDetailsItem2Binding.containerSellerProduct.show()
                productDetailsItem2Binding.sellerProductTv.text = getString(R.string.showLess)
                productDetailsItem2Binding.isSellerProductHideIv.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        productDetailsItem2Binding.containerSellerInfo.setOnClickListener {
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
        productDetailsItem2Binding.containerSellerImage.setOnClickListener {
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
        binding.containerCurrentPriceBuy.setOnClickListener {
            callGetPriceCart(productDetails?.name ?: "")

        }

        binding.containerBuyNow.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                productDetialsViewModel.callBuyNow(productDetails?.id ?: 0)
            } else {
                goToSignInActivity()
            }

        }
        binding.containerBidOnPrice.setOnClickListener {
            openBidPrice()

        }
        productDetailsItem2Binding.contianerAskQuestion.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                confirmAskQues()
            } else {
                goToSignInActivity()
            }
        }

        productDetailsItem2Binding.sellerNumber.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data =
                Uri.parse("tel:" + productDetailsItem2Binding.sellerNumber.text.toString())

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

        binding.btnNextImage.setOnClickListener {
            try {
                if (productImagesList.size > 0) {
                    val position = getLastVisiblePosition(binding.rvProductImages)
//                println("hhhh "+ position+" "+(productImagesList.size-1))
                    if (position < productImagesList.size - 1) {
                        binding.rvProductImages.smoothScrollToPosition(position + 1)
                    } else {
                        binding.rvProductImages.smoothScrollToPosition(0)
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
                    callIntent.data =
                        Uri.parse("tel:" + productDetailsItem2Binding.sellerNumber.text.toString())
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
                                binding.BidOnPriceTv.text =
                                    "${productDetails?.highestBidPrice} ${getString(R.string.Rayal)}"
                            } else {
                                binding.BidOnPriceTv.text =
                                    "${productDetails?.auctionStartPrice} ${getString(R.string.Rayal)}"
                            }
                            bidCount += 1
                            productDetailsItem2Binding.tvAuctionNumber.text =
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
                productDetailsItem2Binding.etWriteQuestion.setText("")

                productDetialsViewModel.getListOfQuestions(productId)
            }
        }
        productDetialsViewModel.getSimilarProductObservable.observe(this) { similarProductRes ->
            if (similarProductRes.productList != null) {
                similerProductList.clear()
                similerProductList.addAll(similarProductRes.productList)
                similarProductAdapter.notifyDataSetChanged()
                productDetailsItem2Binding.contaienrSimilerProduts.show()
            }
        }
        productDetialsViewModel.getListOfQuestionsObservable.observe(this) { questionListResp ->
            if (!questionListResp.questionList.isNullOrEmpty()) {
                productDetailsItem2Binding.tvErrorNoQuestion.hide()
                setQuestionsView(questionListResp.questionList)
            } else {
                productDetailsItem2Binding.tvErrorNoQuestion.show()
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
                productDetailsItem2Binding.linHappy.show()
                productDetailsItem2Binding.txtHappy.text = rateListResp.data.happyCount.toString()
            }
            if (rateListResp.data.satisfiedCount != 0) {
                productDetailsItem2Binding.linSmile.show()
                productDetailsItem2Binding.txtSmile.text =
                    rateListResp.data.satisfiedCount.toString()
            }
            if (rateListResp.data.disgustedCount != 0) {
                productDetailsItem2Binding.linSad.show()
                productDetailsItem2Binding.txtSad.text = rateListResp.data.disgustedCount.toString()
            }

            productDetailsItem2Binding.ratingBarDetailTv.text =
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
                productDetailsItem2Binding.sellerProgressBar.show()
            } else {
                productDetailsItem2Binding.sellerProgressBar.hide()
            }
        }
        productDetialsViewModel.sellerProductsRespObserver.observe(this) { sellerProductListResp ->
            if (sellerProductListResp.status_code == 200) {
                sellerSimilerProductList.clear()
                sellerProductListResp.productList?.let { sellerSimilerProductList.addAll(it) }
                sellerProductAdapter.notifyDataSetChanged()
                if (sellerSimilerProductList.isEmpty()) {
                    productDetailsItem2Binding.tvErrorNoSellerProduct.show()
                } else {
                    productDetailsItem2Binding.tvErrorNoSellerProduct.hide()
                }
            } else {
                productDetailsItem2Binding.tvErrorNoSellerProduct.show()
            }
        }
        productDetialsViewModel.shippingOptionObserver.observe(this) {
            if (it.status_code == 200) {
                if (!it.shippingOptionObject.isNullOrEmpty()) {
                    when (it.shippingOptionObject[0].shippingOptionId) {
                        ConstantObjects.shippingOption_integratedShippingCompanyOptions -> {
                            productDetailsItem2Binding.tvShippingOptions.show()
                            productDetailsItem2Binding.tvShippingOptions.text =
                                getString(R.string.integratedShippingCompanies)
                        }

                        ConstantObjects.shippingOption_freeShippingWithinSaudiArabia -> {
                            productDetailsItem2Binding.tvShippingOptions.show()
                            productDetailsItem2Binding.tvShippingOptions.text =
                                getString(R.string.free_shipping_within_Saudi_Arabia)
                        }

                        ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer -> {
                            productDetailsItem2Binding.tvShippingOptions.show()
                            productDetailsItem2Binding.tvShippingOptions.text =
                                getString(R.string.arrangementWillBeMadeWithTheBuyer)
                        }
                    }
                } else {
                    productDetailsItem2Binding.tvShippingOptions.show()
                    productDetailsItem2Binding.tvShippingOptions.text =
                        getString(R.string.mustPickUp)
                }
            }
        }
        productDetialsViewModel.paymentOptionObserver.observe(this) {
            if (it.status_code == 200) {
                it.shippingOptionObject?.let { list ->
                    for (item in list) {
                        when (item.paymentOptionId) {
                            AddProductObjectData.PAYMENT_OPTION_CASH -> {
                                productDetailsItem2Binding.contianerCash.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_BANk -> {
                                productDetailsItem2Binding.contianerBankAccount.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_Mada -> {
                                productDetailsItem2Binding.containerMada.show()
                            }

                            AddProductObjectData.PAYMENT_OPTION_MasterCard -> {
                                productDetailsItem2Binding.containerMaster.show()
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
                    productDetailsItem2Binding.tvAuctionNumber.text =
                        "${getString(R.string.bidding)} ${bidCount}"
                    if (hideBars.value == true) {
                        productDetailsItem2Binding.containerAuctionNumber.hide()
                    } else
                        productDetailsItem2Binding.containerAuctionNumber.show()
                } else {
                    productDetailsItem2Binding.containerAuctionNumber.hide()
                }
            } else {
                productDetailsItem2Binding.containerAuctionNumber.hide()
            }
        }
        productDetialsViewModel.addSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = true
                productDetailsItem2Binding.ivSellerFollow.setImageResource(R.drawable.notification)
            }
        }
        productDetialsViewModel.removeSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                sellerInformation?.isFollowed = false
                productDetailsItem2Binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
            }
        }

    }

    private fun setSellerInfo(it: SellerInformation) {
        productDetailsItem2Binding.tvErrorNoSellerProduct.hide()
        productDetialsViewModel.getSellerListProduct(
            it.providerId ?: "",
            it.businessAccountId ?: ""
        )
        sellerInformation = it

        if (it.showUserInformation == ShowUserInfo.EveryOne.name) {
            productDetailsItem2Binding.containerSellerInfo.show()
        } else if (it.showUserInformation == ShowUserInfo.MembersOnly.name) {
            if (HelpFunctions.isUserLoggedIn()) {
                productDetailsItem2Binding.containerSellerInfo.show()
            } else {
                productDetailsItem2Binding.containerSellerInfo.hide()
            }
        } else {
            productDetailsItem2Binding.containerSellerInfo.hide()
        }

        showButtons()

        Extension.loadImgGlide(
            this,
            it.image,
            productDetailsItem2Binding.sellerPicture,
            binding.loader
        )
        if (it.businessAccountId == null) {
            productDetailsItem2Binding.txtTypeUser.text = getString(R.string.personal)
        } else {
            productDetailsItem2Binding.txtTypeUser.text = getString(R.string.merchant)
        }
        productDetailsItem2Binding.sellerName.text = it.name ?: ""
        productDetailsItem2Binding.memberSinceTv.text = HelpFunctions.getViewFormatForDateTrack(
            it.createdAt ?: "", "dd/MM/yyyy"
        )
        productDetailsItem2Binding.sellerCity.text = it.city ?: ""
        productDetailsItem2Binding.sellerNumber.text = it.phone ?: ""
        if (it.isFollowed) {
            productDetailsItem2Binding.ivSellerFollow.setImageResource(R.drawable.notification)
        } else {
            productDetailsItem2Binding.ivSellerFollow.setImageResource(R.drawable.notification_log)
        }
        if (it.businessAccountId != "") {
            productDetailsItem2Binding.btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                productDetailsItem2Binding.btnMapSeller.show()
            } else {
                productDetailsItem2Binding.btnMapSeller.hide()
            }
        }

        when (it.rate) {
            3f -> {
                productDetailsItem2Binding.ivRateSeller.setImageResource(R.drawable.happyface_color)
            }

            2f -> {
                productDetailsItem2Binding.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }

            1f -> {
                productDetailsItem2Binding.ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                productDetailsItem2Binding.ivRateSeller.setImageResource(R.drawable.smileface_color)
            }
        }
        if (it.instagram != null && it.instagram != "") {
            productDetailsItem2Binding.instagramBtn.show()
        } else {
            productDetailsItem2Binding.instagramBtn.hide()
        }
        if (it.youTube != null && it.youTube != "") {
            productDetailsItem2Binding.youtubeBtn.show()
        } else {
            productDetailsItem2Binding.youtubeBtn.hide()
        }
        if (it.skype != null && it.skype != "") {
            productDetailsItem2Binding.skypeBtn.show()
        } else {
            productDetailsItem2Binding.skypeBtn.hide()
        }
        if (it.faceBook != null && it.faceBook != "") {
            productDetailsItem2Binding.facebookBtn.show()
        } else {
            productDetailsItem2Binding.facebookBtn.hide()
        }
        if (it.twitter != null && it.twitter != "") {
            productDetailsItem2Binding.twitterBtn.show()
        } else {
            productDetailsItem2Binding.twitterBtn.hide()
        }
        if (it.linkedIn != null && it.linkedIn != "") {
            productDetailsItem2Binding.linkedInBtn.show()
        } else {
            productDetailsItem2Binding.linkedInBtn.hide()
        }
        if (it.tikTok != null && it.tikTok != "") {
            productDetailsItem2Binding.tiktokBtn.show()
        } else {
            productDetailsItem2Binding.tiktokBtn.hide()
        }
        if (it.snapchat != null && it.snapchat != "") {
            productDetailsItem2Binding.snapChatBtn.show()
        } else {
            productDetailsItem2Binding.snapChatBtn.hide()
        }

        if (it.businessAccountId != "") {
            productDetailsItem2Binding.btnMapSeller.show()
        } else {
            if (it.lat != null && it.lon != null) {
                if (it.lat != 0.0 && it.lon != 0.0) {
                    productDetailsItem2Binding.btnMapSeller.show()
                }
            } else {
                productDetailsItem2Binding.btnMapSeller.hide()
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
                productDetailsItem2Binding.tvNumberQuestionNotAnswer.text = getString(
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
            productDetailsItem2Binding.tvReviewsError.show()
            productDetailsItem2Binding.tvShowAllReviews.hide()
            productDetailsItem2Binding.contianerRateText.hide()
        } else {
            productDetailsItem2Binding.tvShowAllReviews.show()
            productDetailsItem2Binding.tvReviewsError.hide()
            productDetailsItem2Binding.contianerRateText.show()
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
                    if (productImagesList[position].type == 2) {
                        // "https://video.blender.org/download/videos/3d95fb3d-c866-42c8-9db1-fe82f48ccb95-804.mp4"
                        // "https://www.youtube.com/watch?v=QfiH796sMAE"
                        // "https://www.sumologic.com/live-demo/"
                        // productImagesList[position].url
                        val selectedUrl = productImagesList[position].url
                        Log.i("test #1", "videoUrl: $selectedUrl")

                        // Check if selectedUrl is not null or empty
                        if (selectedUrl.isNotEmpty()) {
                            when {
                                isYouTubeLink(selectedUrl) -> {
                                    // Extract video ID for YouTube
                                    val videoId = extractYouTubeId(selectedUrl)
                                    val dialogFragment =
                                        YouTubePlayerDialogFragment.newInstance(videoId!!)
                                    dialogFragment.show(
                                        supportFragmentManager,
                                        "YouTubePlayerDialogFragment"
                                    )
                                }

                                isVideoLink(selectedUrl) -> {
                                    val videoDialogFragment =
                                        VideoDialogFragment.newInstance(selectedUrl)
                                    videoDialogFragment.show(supportFragmentManager, "videoDialog")
                                }

                                else -> {
                                    // Open WebViewPlayerDialogFragment for any other link
                                    val webViewDialogFragment =
                                        WebViewPlayerDialogFragment.newInstance(selectedUrl)
                                    webViewDialogFragment.show(
                                        supportFragmentManager,
                                        "webViewPlayerDialog"
                                    )
                                }
                            }
                        } else {
                            // Handle the case where the URL is null or empty, if necessary
                            Log.e("test #1", "selectedUrl is null or empty")
                            Toast.makeText(
                                this@ProductDetailsActivity,
                                getString(R.string.the_selected_url_is_not_available_or_is_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        imgPosition = position
                        binding.productimg.tag = productImagesList[position].url

                        val intent = Intent(
                            this@ProductDetailsActivity,
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

    private fun setSimilarProductAdapter() {
        similerProductList = ArrayList()
        similarProductAdapter = ProductHorizontalAdapter(similerProductList, this, 0, true)
        productDetailsItem2Binding.rvSimilarProducts.apply {
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

    private fun setReviewsAdapter() {
        mainRatesList = ArrayList()
        smallRatesList = ArrayList()
        reviewProductAdapter = ReviewProductAdapter(smallRatesList)
        productDetailsItem2Binding.rvReview.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = reviewProductAdapter
        }
    }

    private fun setSpeceificationAdapter() {
        specificationList = ArrayList()
        specificationAdapter = SpecificationAdapter(specificationList)
        if (specificationList.size != 0)
            productDetailsItem2Binding.laySpec.show()
        productDetailsItem2Binding.rvProductSpecification.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = specificationAdapter
        }
    }

    private fun setQuestionAnswerAdapter() {
        subQuestionsList = ArrayList()
        questionAnswerAdapter = QuestionAnswerAdapter(subQuestionsList, this)
        productDetailsItem2Binding.rvQuestionForProduct.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = questionAnswerAdapter
        }
    }

    /**set product data*/

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        val containerMainProduct = binding.root.findViewById<View>(R.id.containerMainProduct)
        containerMainProduct.hide()
        binding.containerShareAndFav.hide()
        productDetailsItem2Binding.containerSellerInfo.hide()
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
            containerMainProduct.show()
            binding.containerShareAndFav.show()

            // Handling the auction closing time
            if (productDetails.auctionClosingTime != null) {
                runnable = object : Runnable {
                    override fun run() {
                        val endDate: Date? =
                            HelpFunctions.getAuctionClosingTimeByDate(productDetails.auctionClosingTime)
                        if (endDate != null) {
                            hideBars.value = getDifference(
                                productDetails.auctionClosingTime,
                                productDetailsItem2Binding.containerAuctioncountdownTimerBar,
                                productDetailsItem2Binding.titleDay,
                                productDetailsItem2Binding.days,
                                productDetailsItem2Binding.titleHours,
                                productDetailsItem2Binding.hours,
                                productDetailsItem2Binding.titleMinutes,
                                productDetailsItem2Binding.minutes,
                                productDetailsItem2Binding.titleSeconds,
                                productDetailsItem2Binding.seconds,
                                productDetailsItem2Binding.containerAuctionNumber
                            )
                        } else {
                            productDetailsItem2Binding.containerAuctioncountdownTimerBar.hide()
                        }
                        handler.postDelayed(this, INTERVAL)
                    }
                }
                handler.post(runnable)
            } else {
                productDetailsItem2Binding.containerAuctioncountdownTimerBar.hide()
            }

            /**product iamges*/
            Extension.loadImgGlide(
                this,
                productDetails.productImage,
                binding.productimg,
                binding.loader
            )
            binding.productimg.setTag(productDetails.productImage)
            if (productDetails.listMedia != null && productDetails.listMedia.size != 0) {
                binding.otherImageLayout.show()
                productImagesList.clear()
                if (productDetails.listMedia.isNotEmpty())
                    urlImg = productDetails.listMedia[0].url
                productImagesList.addAll(productDetails.listMedia)
                productImagesAdapter.notifyDataSetChanged()

                setPagerDots(mapImageSelectModelToHomeSliderItem(productDetails.listMedia))

            } else {
                productImagesList.add(ImageSelectModel(url = productDetails.productImage.toString()))
                binding.otherImageLayout.hide()
            }
            /**product data*/
            productDetailsItem2Binding.tvProductReview.text =
                "${productDetails.viewsCount} ${getString(R.string.Views)} - #${productDetails.id} - ${
                    HelpFunctions.getViewFormatForDateTrack(
                        productDetails.createdAt, "dd/MM/yyyy"
                    )
                }"
            productDetailsItem2Binding.tvProductItemName.text = productDetails.name ?: ""
            productDetailsItem2Binding.tvProductSubtitle.text = productDetails.subTitle ?: ""

            if (productDetails.description != "") {
                productDetailsItem2Binding.layDetails.show()
                productDetailsItem2Binding.readMoreTextView.text = productDetails.description ?: ""
            }
            productDetailsItem2Binding.btnMoreItemDetails.setOnClickListener {}

            binding.currentPriceBuyTv.text =
                "${productDetails.priceDisc.toString()} ${getString(R.string.sar)}"
            binding.BidOnPriceTv.text = " ${getString(R.string.sar)}"

            /**specification*/
            if (productDetails.listProductSep != null) {
                productDetailsItem2Binding.tvErrorNoSpecification.hide()
                if (productDetails.listProductSep.isNotEmpty())
                    productDetailsItem2Binding.laySpec.show()
                specificationList.clear()
                specificationList.addAll(productDetails.listProductSep)
                specificationAdapter.notifyDataSetChanged()
            } else {
                productDetailsItem2Binding.tvErrorNoSpecification.show()
            }
            /**pidding views*/
            productDetailsItem2Binding.tvAuctionNumber.text = "${getString(R.string.bidding)} "
            productfavStatus = productDetails.isFavourite
            if (productDetails.isFavourite) {
                binding.ivFav.setImageResource(R.drawable.starcolor)
                binding.ivFav.setColorFilter(resources.getColor(R.color.orange))
            } else {
                binding.ivFav.setImageResource(R.drawable.star)
            }
            if (productDetails.isNegotiationEnabled) {
                productDetailsItem2Binding.btnPriceNegotiation.show()
            } else {
                productDetailsItem2Binding.btnPriceNegotiation.hide()
            }

            if (productDetails.isAuctionEnabled) {
                if (productDetails.highestBidPrice.toDouble() != 0.0) {
                    binding.BidOnPriceTv.text =
                        "${productDetails.highestBidPrice} ${getString(R.string.Rayal)}"
                } else {
                    binding.BidOnPriceTv.text =
                        "${productDetails.auctionStartPrice} ${getString(R.string.Rayal)}"
                }

                if (hideBars.value == true) {
                    binding.containerBidOnPrice.hide()
                } else
                    binding.containerBidOnPrice.show()
            } else {
                binding.containerBidOnPrice.hide()
            }
            if (productDetails.myBid != 0f) {
                binding.containerMyBid.show()
                binding.tvMyBidPrice.text = "${productDetails.myBid} ${getString(R.string.sar)}"
            } else {
                binding.containerMyBid.hide()
            }

            if (productDetails.acceptQuestion) {
                productDetailsItem2Binding.sectionQs.show()
            } else {
                productDetailsItem2Binding.sectionQs.hide()
            }

        } else {
            showError(getString(R.string.serverError))
        }
    }

    private fun setPagerDots(list: List<HomeSliderItem>) {
        if (list.isNotEmpty()) {

            val viewPagerAdapter = SliderAdaptor(this, list, true, this)
            binding.sliderDetails.adapter = viewPagerAdapter
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
            binding.containerBuyNow.show()
            binding.txtPriceNow.text = "${productDetails?.priceDisc} ${getString(R.string.sar)}"
            if (sellerInformation?.businessAccountId != null) {
                binding.containerCurrentPriceBuy.show() // AddToCart
            } else {
                binding.containerCurrentPriceBuy.hide()
            }
        } else {
            binding.containerBuyNow.hide()
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

        if (daysDifference <= 0 && (hoursDifference <= 0) && (minutesDifference <= 0)) {
            hideBars.value = true
            productDetailsItem2Binding.containerAuctionNumber.hide()
            productDetailsItem2Binding.containerAuctioncountdownTimerBar.hide()
        } else {
            hideBars.value = false
            productDetailsItem2Binding.containerAuctionNumber.show()
            productDetailsItem2Binding.containerAuctioncountdownTimerBar.show()
        }

        if (daysDifference == 0L || (daysDifference < 0L)) {
            productDetailsItem2Binding.days.visibility = View.GONE
            productDetailsItem2Binding.titleDay.visibility = View.GONE
        }
        if (hoursDifference == 0L || (hoursDifference < 0L)) {
            productDetailsItem2Binding.hours.visibility = View.GONE
            productDetailsItem2Binding.titleHours.visibility = View.GONE
        }

        if (minutesDifference == 0L || (minutesDifference < 0L)) {
            productDetailsItem2Binding.minutes.visibility = View.GONE
            productDetailsItem2Binding.titleMinutes.visibility = View.GONE
        }


        productDetailsItem2Binding.days.text = daysDifference.toString()
        productDetailsItem2Binding.hours.text = hoursDifference.toString()
        productDetailsItem2Binding.minutes.text = minutesDifference.toString()
        productDetailsItem2Binding.seconds.text = secondDifference.toString()
    }

    /**send QUestion for sller**/
    private fun confirmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            productDetialsViewModel.addQuestion(
                productId,
                productDetailsItem2Binding.etWriteQuestion.text.trim().toString()
            )
        }

    }

    private fun validateAskQuesInputText(): Boolean {
        val Inputemail =
            productDetailsItem2Binding.etWriteQuestion!!.text.toString().trim { it <= ' ' }

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
        val returnIntent = Intent().apply {
            putExtra(ConstantObjects.productIdKey, productId)
            putExtra(ConstantObjects.productFavStatusKey, productfavStatus)
        }

        // Check if the current activity is the root of the task (no other activities to go back to)
        if (isTaskRoot) {
            // If it's the root, go to the main activity
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(mainIntent)
            finish()
        } else {
            // Not the root, return to the previous activity in the stack
            setResult(Activity.RESULT_OK, returnIntent)
            super.onBackPressed()  // Proceed with normal back navigation
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

        if (this::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }

        sellerProductAdapter.onDestroyHandler()
        similarProductAdapter.onDestroyHandler()
    }

    override fun onClickImage(url: String) {
        val intent = Intent(this@ProductDetailsActivity, ImageViewLargeActivity::class.java)
        intent.putParcelableArrayListExtra("imgList", productImagesList)
        intent.putExtra("UrlImg", url)
        startActivity(intent)
    }
}