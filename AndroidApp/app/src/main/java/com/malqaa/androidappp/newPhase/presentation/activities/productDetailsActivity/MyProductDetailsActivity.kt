package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.activitiesMain.PlayActivity
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.shared
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malqaa.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog.AddDiscountDialog
import com.malqaa.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.homeSilderResp.HomeSliderItem
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity.AddRateProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProductReviewActivity.ProductReviewsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity1.CartActivity
import com.malqaa.androidappp.newPhase.presentation.dialogsShared.currentPriceDialog.BuyCurrentPriceDialog
import com.malqaa.androidappp.newPhase.presentation.activities.loginScreen.SignInActivity
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.ProductImagesAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.ReviewProductAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.SpecificationAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.presentation.activities.productQuestionActivity.QuestionActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.ListenerSlider
import com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.SliderAdaptor
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_my_product_details2.btnMoreItemDetails
import kotlinx.android.synthetic.main.activity_my_product_details2.btnMoreSpecification
import kotlinx.android.synthetic.main.activity_my_product_details2.containerAuctioncountdownTimer_bar
import kotlinx.android.synthetic.main.activity_my_product_details2.containerDeleteProduct
import kotlinx.android.synthetic.main.activity_my_product_details2.containerMada
import kotlinx.android.synthetic.main.activity_my_product_details2.containerMaster
import kotlinx.android.synthetic.main.activity_my_product_details2.containerReviews
import kotlinx.android.synthetic.main.activity_my_product_details2.contianerBankAccount
import kotlinx.android.synthetic.main.activity_my_product_details2.contianerCash
import kotlinx.android.synthetic.main.activity_my_product_details2.contianerRateText
import kotlinx.android.synthetic.main.activity_my_product_details2.days
import kotlinx.android.synthetic.main.activity_my_product_details2.discountProduct
import kotlinx.android.synthetic.main.activity_my_product_details2.editProduct
import kotlinx.android.synthetic.main.activity_my_product_details2.hours
import kotlinx.android.synthetic.main.activity_my_product_details2.layDiscount
import kotlinx.android.synthetic.main.activity_my_product_details2.layInfo
import kotlinx.android.synthetic.main.activity_my_product_details2.layPrice
import kotlinx.android.synthetic.main.activity_my_product_details2.lay_auction
import kotlinx.android.synthetic.main.activity_my_product_details2.minutes
import kotlinx.android.synthetic.main.activity_my_product_details2.rating_bar_detail_tv
import kotlinx.android.synthetic.main.activity_my_product_details2.rvProductSpecification
import kotlinx.android.synthetic.main.activity_my_product_details2.rv_review
import kotlinx.android.synthetic.main.activity_my_product_details2.tvAddReview
import kotlinx.android.synthetic.main.activity_my_product_details2.tvErrorNoQuestion
import kotlinx.android.synthetic.main.activity_my_product_details2.tvErrorNoSpecification
import kotlinx.android.synthetic.main.activity_my_product_details2.tvNumberQuestionNotAnswer
import kotlinx.android.synthetic.main.activity_my_product_details2.tvProductDescriptionLong
import kotlinx.android.synthetic.main.activity_my_product_details2.tvProductDescriptionShort
import kotlinx.android.synthetic.main.activity_my_product_details2.tvProductItemName
import kotlinx.android.synthetic.main.activity_my_product_details2.tvProductReview
import kotlinx.android.synthetic.main.activity_my_product_details2.tvProductSubtitle
import kotlinx.android.synthetic.main.activity_my_product_details2.tvQuestionAndAnswersShowAll
import kotlinx.android.synthetic.main.activity_my_product_details2.tvReviewsError
import kotlinx.android.synthetic.main.activity_my_product_details2.tvShippingOptions
import kotlinx.android.synthetic.main.activity_my_product_details2.tvShowAllReviews
import kotlinx.android.synthetic.main.activity_my_product_details2.txtPrice
import kotlinx.android.synthetic.main.activity_my_product_details2.txt_highAuction
import kotlinx.android.synthetic.main.activity_my_product_details2.txt_lowAuction
import kotlinx.android.synthetic.main.activity_my_product_details2.linHappy
import kotlinx.android.synthetic.main.activity_my_product_details2.linSad
import kotlinx.android.synthetic.main.activity_my_product_details2.linSmile
import kotlinx.android.synthetic.main.activity_my_product_details2.rvQuestionForProduct
import kotlinx.android.synthetic.main.activity_my_product_details2.tvPriceProductDisc
import kotlinx.android.synthetic.main.activity_my_product_details2.txtCountFav
import kotlinx.android.synthetic.main.activity_my_product_details2.txtCountNegotiation
import kotlinx.android.synthetic.main.activity_my_product_details2.txtCountPurchase
import kotlinx.android.synthetic.main.activity_my_product_details2.txtHappy
import kotlinx.android.synthetic.main.activity_my_product_details2.txtSad
import kotlinx.android.synthetic.main.activity_my_product_details2.txtSmile
import kotlinx.android.synthetic.main.activity_product_details2.productimg
import kotlinx.android.synthetic.main.my_product_details.myProductimg
import kotlinx.android.synthetic.main.activity_product_details_item_2.laySpec
import kotlinx.android.synthetic.main.activity_product_details_item_2.readMoreTextView
import kotlinx.android.synthetic.main.my_product_details.btnNextImage
import kotlinx.android.synthetic.main.my_product_details.btnShare
import kotlinx.android.synthetic.main.my_product_details.containerMainProduct
import kotlinx.android.synthetic.main.my_product_details.containerShareAndFav
import kotlinx.android.synthetic.main.my_product_details.fbButtonBack
import kotlinx.android.synthetic.main.my_product_details.ivFav
import kotlinx.android.synthetic.main.my_product_details.loader
import kotlinx.android.synthetic.main.my_product_details.next_image
import kotlinx.android.synthetic.main.my_product_details.other_image_layout
import kotlinx.android.synthetic.main.my_product_details.rvProductImages
import kotlinx.android.synthetic.main.my_product_details.slider_my_details
import kotlinx.android.synthetic.main.my_product_details.swipe_to_refresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.util.Date


class MyProductDetailsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, QuestionAnswerAdapter.SetonSelectedQuestion,
    BuyCurrentPriceDialog.OnAttachedCartMethodSelected , ListenerSlider {

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
    var imgPosition =0
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
    lateinit var priceNegotiationDialog: PriceNegotiationDialog
    var productPrice: Float = 0f

    lateinit var smallRatesList: ArrayList<RateReviewItem>
    lateinit var mainRatesList: ArrayList<RateReviewItem>
    var comeFrom = ""

    private lateinit var buyCurrentPriceDialog: BuyCurrentPriceDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_product_details)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, -1)
        println("hhhh product if $productId")
        comeFrom = intent.getStringExtra("ComeFrom") ?: ""

        isMyProduct = intent.getBooleanExtra(ConstantObjects.isMyProduct, false)
        isMyProductForSale = intent.getBooleanExtra("isMyProductForSale", false)

        setViewChanges()
        setProductDetailsViewModel()
        setupViewClickListeners()
        setupViewAdapters()

        onRefresh()
        if (HelpFunctions.isUserLoggedIn()) {
            userData = Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
            productDetialsViewModel.addLastViewedProduct(productId)
        }

        if (isMyProductForSale) {
            editProduct.show()
            layInfo.show()
            discountProduct.show()
        } else {
            layInfo.hide()
            editProduct.hide()
            discountProduct.hide()
        }
        editProduct.setOnClickListener {
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
        discountProduct.setOnClickListener {
            openDiscountDialog(productId)
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
            val viewPagerAdapter = SliderAdaptor(this, list,true,this)
            slider_my_details.adapter = viewPagerAdapter
//            dots_indicator.attachTo(slider_details)
//            slider_my_details.startAutoScroll()
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
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            fbButtonBack.scaleX = 1f
        } else {
            fbButtonBack.scaleX = -1f
        }
        containerMainProduct.hide()
        other_image_layout.hide()
        btnMoreSpecification.hide()
        btnMoreItemDetails.hide()
        tvShippingOptions.hide()
        contianerBankAccount.hide()
        containerMada.hide()
        containerMaster.hide()
        contianerCash.hide()
        containerAuctioncountdownTimer_bar.hide()
        //for reviewa
        tvReviewsError.hide()
        contianerRateText.hide()
        //====
        tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")


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
        myProductimg.setOnClickListener {
////            val customDialog = OpenImgLargeDialog(this, urlImg)
//            startActivity(Intent(this, OpenImgLargeDialog::class.java).apply {
//                putExtra("urlImg", urlImg)
//            })
            val intent = Intent(this@MyProductDetailsActivity, ImageViewLargeActivity::class.java)
            intent.putParcelableArrayListExtra("imgList", productImagesList)
            intent.putExtra("UrlImg", myProductimg.tag.toString())
            startActivity(intent)
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
        btnShare.setOnClickListener {
            shared("http://advdev-001-site1.dtempurl.com/Home/GetProductById?id=$productId")
            //shared("${Constants.HTTP_PROTOCOL}://${Constants.SERVER_LOCATION}/Advertisement/Detail/$AdvId")
        }

        tvQuestionAndAnswersShowAll.setOnClickListener {
            if (!questionsList.isNullOrEmpty())
                startActivity(Intent(this, QuestionActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                    putExtra(ConstantObjects.isMyProduct, isMyProduct)
                })
        }


        tvShowAllReviews.setOnClickListener {
            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productId)
            })
        }

        btnNextImage.setOnClickListener {
            try {
                if (productImagesList.size > 0) {
                    var position = getLastVisiblePosition(rvProductImages);
//                println("hhhh "+ position+" "+(productImagesList.size-1))
                    if (position < productImagesList.size - 1) {
                        rvProductImages.smoothScrollToPosition(position + 1);
                    } else {
                        rvProductImages.smoothScrollToPosition(0);
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
//        productDetialsViewModel.sellerInfoLoadingObservable.observe(this){
//            if(it){
//                progressBarSellerInfo.show()
//            }else{
//                progressBarSellerInfo.hide()
//            }
//        }
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
        productDetialsViewModel.getRateResponseObservable.observe(this) { rateListResp ->
            if (rateListResp.status_code == 200) {
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
                if (!rateListResp.data.rateProductListDto.isNullOrEmpty()) {
                    setReviewRateView(rateListResp.data.rateProductListDto)
                } else {
                    containerReviews.hide()
                }
            }
        }
        productDetialsViewModel.productDetailsObservable.observe(this) { productResp ->
            if (productResp.productDetails != null) {
                productDetails = productResp.productDetails
                if (productDetails?.priceDisc != productDetails?.price)
                    layDiscount.show()
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
//                    if (HelpFunctions.isUserLoggedIn()) {
//                        SharedPreferencesStaticClass.saveAssignCartToUser(true)
//                    }else{
//                        SharedPreferencesStaticClass.saveAssignCartToUser(false)
//                    }
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
                tvErrorNoQuestion.hide()
                setQuestionsView(questionListResp.questionList)
            } else {
                tvErrorNoQuestion.show()
                tvQuestionAndAnswersShowAll.hide()
            }
        }

        productDetialsViewModel.shippingOptionObserver.observe(this) {
            if (it.status_code == 200) {
                if (it.shippingOptionObject != null && it.shippingOptionObject.isNotEmpty()) {
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

    }


    private fun setupViewAdapters() {
        setSpeceificationAdapter()
        setupProductImagesAdapter()
        setReviewsAdapter()
        setQuestionAnswerAdapter()
    }

    private fun setQuestionAnswerAdapter() {
        subQuestionsList = ArrayList()
        questionAnswerAdapter = QuestionAnswerAdapter(subQuestionsList, this)
        rvQuestionForProduct.apply {
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
                tvNumberQuestionNotAnswer.text = getString(
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
        rv_review.apply {
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

//        lifecycleScope.launch(Dispatchers.IO) {
//            var totalRating = 0.0
//            mainRatesList.forEach {
//                totalRating += it.rate.toDouble()
//
//            }
//            val average = totalRating / mainRatesList.size
//            withContext(Dispatchers.Main) {
//                rating_bar.rating = average.toFloat()
//                rating_bar_detail_tv.text = getString(
//                    R.string._4_9_from_00_visitors,
//                    rating_bar.rating.toString().format("%.2f"),
//                    mainRatesList.size.toString()
//                )
//            }
//        }
        if (mainRatesList.isEmpty()) {
            tvReviewsError.show()
            contianerRateText.hide()
        } else {
            tvReviewsError.hide()
            contianerRateText.show()
        }
    }

    private fun setupProductImagesAdapter() {
        productImagesList = ArrayList()
        productImagesAdapter = ProductImagesAdapter(productImagesList,
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
                        myProductimg.tag = productImagesList[position].url
                        //==zoom image
                        val intent = Intent(this@MyProductDetailsActivity, ImageViewLargeActivity::class.java)
                        intent.putParcelableArrayListExtra("imgList", productImagesList)
                        intent.putExtra("UrlImg",  productImagesList[position].url)
                        startActivity(intent)
                    }
                }

            })
        rvProductImages.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = productImagesAdapter
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


    /**set product data*/

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        containerMainProduct.hide()
        containerShareAndFav.hide()
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
            if (productDetails.auctionClosingTime != null) {
                containerAuctioncountdownTimer_bar.show()
                var endDate: Date? =
                    HelpFunctions.getAuctionClosingTimeByDate(productDetails.auctionClosingTime)
//                println("hhhh "+endDate.toString()+" "+Calendar.getInstance().time)
                if (endDate != null) {
                    timeDifferent(productDetails.auctionClosingTime)
                } else {
                    containerAuctioncountdownTimer_bar.hide()
                }

            } else {
                containerAuctioncountdownTimer_bar.hide()
            }
            /**product iamges*/
            Extension.loadThumbnail(
                this,
                productDetails.productImage,
                myProductimg,
                loader
            )


            txtCountNegotiation.text = productDetails.negotiationOffersCount.toString()
            txtCountPurchase.text = productDetails.purchasedQuantity.toString()
            txtCountFav.text = productDetails.addedToFavoritsCount.toString()

            tvPriceProductDisc.text = "${productDetails.priceDisc} ${getString(R.string.rial)}"

            productPrice = productDetails.priceDisc

            myProductimg.setTag(productDetails.productImage)
            if (productDetails.listMedia != null) {
                other_image_layout.show()
                productImagesList.clear()
                if (productDetails.listMedia.isNotEmpty())
                    urlImg = productDetails.listMedia[0].url
                productImagesList.addAll(productDetails.listMedia)
                productImagesAdapter.notifyDataSetChanged()
                setPagerDots( mapImageSelectModelToHomeSliderItem(productDetails.listMedia))

            } else {
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


            tvProductDescriptionShort.text =
                productDetails.description ?: ""
            tvProductDescriptionLong.text =
                productDetails.description ?: ""
            val isEllipsize: Boolean = tvProductDescriptionShort.text.toString()
                .trim() != productDetails.description?.trim()
            if (isEllipsize) {
                btnMoreItemDetails.show()
            } else {
                btnMoreItemDetails.hide()
            }

            txt_highAuction.text =
                "${productDetails.auctionMinimumPrice} ${getString(R.string.Rayal)}"
            txt_lowAuction.text =
                "${productDetails.auctionStartPrice} ${getString(R.string.Rayal)}"



            txtPrice.text = "${productDetails.price} ${getString(R.string.Rayal)}"

            btnMoreItemDetails.setOnClickListener {

                if (getString(R.string.Showmore) == btnMoreItemDetails.text.toString() && isEllipsize) {
                    btnMoreItemDetails.text = getString(R.string.showLess)
                    tvProductDescriptionLong.show()
                    tvProductDescriptionShort.hide()
                } else if (getString(R.string.showLess) == btnMoreItemDetails.text.toString()) {
                    btnMoreItemDetails.text = getString(R.string.Showmore)
                    tvProductDescriptionLong.hide()
                    tvProductDescriptionShort.show()
                }

            }
            /**specification*/
            if (productDetails.listProductSep != null) {
                tvErrorNoSpecification.hide()
                specificationList.clear()
                specificationList.addAll(productDetails.listProductSep)
                specificationAdapter.notifyDataSetChanged()
            } else {
                tvErrorNoSpecification.show()
            }
            containerDeleteProduct.setOnClickListener {
                if (productDetails.isFixedPriceEnabled) {
                    productDetialsViewModel.removeProduct(productDetails.id)
                } else {

                }
            }
            /**pidding views*/
            productfavStatus = productDetails.isFavourite
            if (productDetails.isFavourite) {
                ivFav.setImageResource(R.drawable.starcolor)
                ivFav.setColorFilter(resources.getColor(R.color.orange))
            } else {
                ivFav.setImageResource(R.drawable.star)
            }

            if (productDetails.isFixedPriceEnabled) {
                if (productDetails.price.toDouble() == 0.0) {
                    layPrice.visibility = View.GONE
                } else
                    layPrice.visibility = View.VISIBLE

                containerDeleteProduct.text = getString(R.string.deleteProduct)
            }

            if (productDetails.isAuctionEnabled) {
                lay_auction.show()
                layPrice.hide()
                containerDeleteProduct.text = getString(R.string.deleteAuction)
            } else {
                lay_auction.hide()
            }

//            if (productDetails.myBid != 0f) {
//
//            }

        } else {
            showError(getString(R.string.serverError))
        }
    }

    fun getDifference(curretndate: Date, endDate: Date) {
        //milliseconds
        //milliseconds
        var different: Long = endDate.time - curretndate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli

        val elapsedSeconds = different / secondsInMilli

//        Toast.makeText(
//            this,
//            "$elapsedDays $elapsedHours $elapsedMinutes $elapsedSeconds",
//            Toast.LENGTH_LONG
//        ).show()
        days.text = elapsedDays.toString()
        hours.text = elapsedHours.toString()
        minutes.text = elapsedMinutes.toString()

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


        days.text = daysDifference.toString()
        hours.text = hoursDifference.toString()
        minutes.text = minutesDifference.toString()
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

    private fun setListener() {

        fbButtonBack.setOnClickListener {
            onBackPressed()
        }

        next_image.setOnClickListener {

        }


    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == addProductReviewRequestCode) {
//            //var addRateItem: AddRateItem? =data?.getParcelableExtra(ConstantObjects.rateObjectKey)
//            productDetialsViewModel.getProductRatesForProductDetails(productId)
//
//        }
//
//    }

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
        productDetialsViewModel.closeAllCall()
        productDetialsViewModel.baseCancel()
    }

    override fun onClickImage(url: String) {

    }
}

