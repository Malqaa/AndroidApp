package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.PlayActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.Extension.shared
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.productResp.ProductMediaItemDetails
import com.malka.androidappp.newPhase.domain.models.productResp.ProductSpecialityItemDetails
import com.malka.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malka.androidappp.newPhase.domain.models.sellerInfoResp.SellerInformation
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.Question
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.addProductReviewActivity.AddRateProductActivity
import com.malka.androidappp.newPhase.presentation.addProductReviewActivity.ProductReviewsActivity
import com.malka.androidappp.newPhase.presentation.cartActivity.activity1.CartActivity
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.ProductImagesAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.ReviewProductAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.SpecificationAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailHelper
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malka.androidappp.newPhase.presentation.productQuestionActivity.QuestionActivity
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.SellerInformationActivity
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_product_details2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.rating_bar
import kotlinx.android.synthetic.main.atrribute_item.view.*
import kotlinx.android.synthetic.main.item_image_for_product_details.view.*
import kotlinx.android.synthetic.main.item_review_product.*
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ProductDetailsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, QuestionAnswerAdapter.SetonSelectedQuestion {

    var AdvId = ""
    var selectLink = ""
    val attributeList: ArrayList<Attribute> = ArrayList()

    //    var questionList: List<Question> = ArrayList()
    var addProductReviewRequestCode = 1000
    var addSellerReviewRequestCode = 2000
    lateinit var product: Product

    lateinit var productDetailHelper: ProductDetailHelper
    lateinit var questionAnswerAdapter: QuestionAnswerAdapter
    lateinit var reviewProductAdapter: ReviewProductAdapter
    lateinit var smallRatesList: ArrayList<RateReviewItem>
    lateinit var mainRatesList: ArrayList<RateReviewItem>
    lateinit var sellerProductAdapter: ProductHorizontalAdapter
    lateinit var similarProductAdapter: ProductHorizontalAdapter

    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    private var productDetails: Product? = null
    private var productId: Int = -1;
    lateinit var specificationAdapter: SpecificationAdapter
    lateinit var specificationList: ArrayList<ProductSpecialityItemDetails>
    lateinit var productImagesAdapter: ProductImagesAdapter
    lateinit var productImagesList: ArrayList<ProductMediaItemDetails>
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
    var sellerInformation: SellerInformation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details2)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, -1)
      println("hhhh product if $productId")
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
//
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
        contaienrSimilerProduts.hide()
        containerBidOnPrice.hide()
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

//        containerRateSeller.setOnClickListener {
//            startActivityForResult(Intent(this, SellerRateListActivity::class.java).apply {
//                putExtra(
//                    ConstantObjects.providerIdKey,
//                    sellerInformation?.providerId ?: ""
//                )
//                putExtra(
//                    ConstantObjects.businessAccountIdKey,
//                    sellerInformation?.businessAccountId ?: ""
//                )
//
//            }, addSellerReviewRequestCode)
//        }
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
            if (sellerInformation?.lat != null && sellerInformation?.lon != null) {
                openLocationInMap(sellerInformation?.lat!!, sellerInformation?.lon!!)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.noLocationFound), this)
            }
            openLocationInMap(0.0, 0.0)
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
        tvShowAllPidding.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        btnPriceNegotiation.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        btnShare.setOnClickListener {
            shared("${Constants.HTTP_PROTOCOL}://${Constants.SERVER_LOCATION}/Advertisement/Detail/$AdvId")
        }
        tvQuestionAndAnswersShowAll.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        tvQuestionAndAnswersShowAll.setOnClickListener {
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
                startActivity(Intent(this, SellerInformationActivity::class.java).apply {
                    putExtra(ConstantObjects.sellerObjectKey, sellerInformation)
                })
            }
        }
        containerSellerImage.setOnClickListener {
            if (sellerInformation != null) {
                startActivity(Intent(this, SellerInformationActivity::class.java).apply {
                    putExtra(ConstantObjects.sellerObjectKey, sellerInformation)
                })
            }
        }
        containerCurrentPriceBuy.setOnClickListener {
            productDetialsViewModel.addProductToCart(
                SharedPreferencesStaticClass.getMasterCartId(),
                productId
            )
        }
        containerBidOnPrice.setOnClickListener {
            productDetails?.let {
                var auctionDialog: AuctionDialog = AuctionDialog(this,
                    productId,
                    it.auctionStartPrice,
                    it.auctionMinimumPrice,
                    it.auctionNegotiatePrice,
                    object : AuctionDialog.SetClickListeners {
                        override fun setOnSuccessListeners() {
                            onRefresh()
                        }

                    })
                auctionDialog.show()
            }

//            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        contianerAskQuestion.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                confirmAskQues()
            } else {
                goToSignInActivity()
            }
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
            if (it.message != null) {
                showProductApiError(it.message!!)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productDetialsViewModel.productDetailsObservable.observe(this) { productResp ->
            if (productResp.productDetails != null) {
                productDetails = productResp.productDetails
                setProductData(productDetails)
            } else {
                showProductApiError(productResp.message)
            }
        }
        productDetialsViewModel.addQuestionObservable.observe(this) { questResp ->
            HelpFunctions.ShowLongToast(questResp.message, this)
            if (questResp.status_code == 200) {
                etWriteQuestion.setText("")
                resetQuestionAndAnswerAdapter()
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
            if (questionListResp.questionList != null && questionListResp.questionList.isNotEmpty()) {
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
        productDetialsViewModel.errorResponseObserverProductToFav.observe(this) {
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
            if (rateListResp.status_code == 200) {
                setReviewRateView(rateListResp.data)
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
    }

    private fun setSellerInfo(it: SellerInformation) {
        tvErrorNoSellerProduct.hide()
        productDetialsViewModel.getSellerListProduct(
            it.providerId ?: "",
            it.businessAccountId ?: ""
        )
        sellerInformation = it
        containerSellerInfo.show()
        Extension.loadThumbnail(
            this,
            it.image,
            seller_picture,
            loader
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
//        tvRateText.text = it.rate.toString()
//        if (it.lat != null && it.lon != null) {
//            btnMapSeller.show()
//        } else {
//            btnMapSeller.hide()
//        }
        when (it.rate) {
            1f -> {
                ivRateSeller.setImageResource(R.drawable.smile3)
            }
            2f -> {
                ivRateSeller.setImageResource(R.drawable.neutral)
            }
            3f -> {
                ivRateSeller.setImageResource(R.drawable.sad)
            }
            else -> {
                ivRateSeller.setImageResource(R.drawable.smile3)
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

    }

    private fun setQuestionsView(data: List<QuestionItem>) {
        questionsList = data
        var datalist = questionsList.take(3)
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
        var datalist = mainRatesList.take(3)
        smallRatesList.clear()
        smallRatesList.addAll(datalist)
        reviewProductAdapter.notifyDataSetChanged()

        lifecycleScope.launch(Dispatchers.IO) {
            var totalRating = 0.0
            mainRatesList.forEach {
                totalRating += it.rate.toDouble()

            }
            val average = totalRating / mainRatesList.size
            withContext(Dispatchers.Main) {
                rating_bar.rating = average.toFloat()
                rating_bar_detail_tv.text = getString(
                    R.string._4_9_from_00_visitors,
                    rating_bar.rating.toString().format("%.2f"),
                    mainRatesList.size.toString()
                )
            }
        }
        if (mainRatesList.isEmpty()) {
            tvReviewsError.show()
            contianerRateText.hide()
        } else {
            tvReviewsError.hide()
            contianerRateText.show()
        }
    }

    private fun setNewAddedItem(data: RateReviewItem) {
        mainRatesList.add(data)
        smallRatesList.add(data)
        reviewProductAdapter.notifyDataSetChanged()

        lifecycleScope.launch(Dispatchers.IO) {
            var totalRating = 0.0
            mainRatesList.forEach {
                totalRating += it.rate.toDouble()

            }
            val average = totalRating / mainRatesList.size
            withContext(Dispatchers.Main) {
                rating_bar.rating = average.toFloat()
                rating_bar_detail_tv.text = getString(
                    R.string._4_9_from_00_visitors,
                    rating_bar.rating.toString().format("%.2f"),
                    mainRatesList.size.toString()
                )
            }
        }
        if (mainRatesList.isEmpty()) {
            tvReviewsError.show()
            contianerRateText.hide()
        } else {
            tvReviewsError.hide()
            contianerRateText.show()
        }
    }


    /**set recycler views Adapters */


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
                    if (productImagesList[position].type == 2) {

                        //video
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
                        //==zoom image
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
    }


    @SuppressLint("SetTextI18n")
    private fun setProductData(productDetails: Product?) {
        if (productDetails != null) {
            containerMainProduct.show()
            containerShareAndFav.show()
            /**product iamges*/
            Extension.loadThumbnail(
                this,
                productDetails.productImage,
                productimg,
                loader
            )
            if (productDetails.listMedia != null) {
                other_image_layout.show()
                productImagesList.clear()
                productImagesList.addAll(productDetails.listMedia)
                productImagesAdapter.notifyDataSetChanged()
            } else {
                other_image_layout.hide()
            }
            /**product data*/
            tvProductReview.text =
                "${productDetails.countView} ${getString(R.string.Views)} - #${productDetails.id} - ${
                    HelpFunctions.getViewFormatForDateTrack(
                        productDetails.createdAt
                    )
                }"
            tvProductItemName.text = productDetails.name ?: ""
            tvProductSubtitle.text = productDetails.subTitle ?: ""
            tvProductDescriptionShort.text =
                productDetails.description ?: ""
            tvProductDescriptionLong.text =
                productDetails.description  ?: ""
            val isEllipsize: Boolean = tvProductDescriptionShort.text.toString().trim() != productDetails.description?.trim()
            if(isEllipsize) {
                btnMoreItemDetails.show()
            }else{
                btnMoreItemDetails.hide()
            }
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

            current_price_buy_tv.text =
                "${productDetails.priceDisc.toString()} ${getString(R.string.sar)}"
            Bid_on_price_tv.text = " ${getString(R.string.sar)}"

            /**specification*/
            if (productDetails.listProductSep != null) {
                tvErrorNoSpecification.hide()
                specificationList.clear()
                specificationList.addAll(productDetails.listProductSep)
                specificationAdapter.notifyDataSetChanged()
            } else {
                tvErrorNoSpecification.show()
            }
            /**pidding views*/
            containerAuctioncountdownTimer_bar.show()
            tvAuctionNumber.text = "${getString(R.string.bidding)} "
            productfavStatus = productDetails.isFavourite
            if (productDetails.isFavourite) {
                ivFav.setImageResource(R.drawable.starcolor)
            } else {
                ivFav.setImageResource(R.drawable.star)
            }
            if (productDetails.isNegotiationEnabled) {
                btnPriceNegotiation.show()
            } else {
                btnPriceNegotiation.hide()
            }

            if (productDetails.isAuctionEnabled) {
                Bid_on_price_tv.text =
                    "${productDetails.auctionNegotiatePrice} ${getString(R.string.Rayal)}"
                containerBidOnPrice.show()
            } else {
                containerBidOnPrice.hide()
            }

        } else {
            showError(getString(R.string.serverError))
        }
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


    private fun resetQuestionAndAnswerAdapter() {
        /**question List**/
//        productDetailHelper.quesAnss(AdvId) {
//            it.run {
//                tvNumberQuestionNotAnswer.text = getString(
//                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
//                    unAnswered.toString()
//                )
//                questionList = questions
//                questionList = questionList.take(3)
//                GenericAdaptor().questionAnswerAdaptor(rvQuestionForProduct, questionList)
//                if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
//                    askques_bottom.visibility = View.GONE
//                    enableSwipeToDeleteAndUndo()
//
//                } else if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
//                    askques_bottom.visibility = View.VISIBLE
//
//                }
//            }
//
//        }

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
    /****************/
    /**** /****************/************/
    /****************/
    /****************/
    /****************/
    private fun setListenser() {

        fbButtonBack.setOnClickListener {
            onBackPressed()
        }

        next_image.setOnClickListener {

        }
        sellerName.setOnClickListener {
            startActivity(Intent(this, SellerInformationActivity::class.java).apply {

            })
        }
//        tvShowAllReviews.setOnClickListener {
//            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {
//
//            })
//        }


//        Bid_on_price.setOnClickListener {
//
//            val builder = AlertDialog.Builder(this@ProductDetailsActivity)
//                .create()
//            val view = layoutInflater.inflate(R.layout.bid_alert_box, null)
//            builder.setView(view)
//            view.close_alert_bid.setOnClickListener {
//                builder.dismiss()
//            }
//
//            builder.setCanceledOnTouchOutside(false)
//            builder.show()
//
//
//            view.btn_bid.setOnClickListener {
//                builder.dismiss()
//                AlertDialog.Builder(this@ProductDetailsActivity)
//                    .create().apply {
//                        layoutInflater.inflate(R.layout.bid_confirmation, null).also {
//                            this.setView(it)
//                            it.apply {
//                                close_alert.setOnClickListener {
//                                    dismiss()
//                                }
//
//                                back_to_shopping.setOnClickListener {
//                                    dismiss()
//                                    startActivity(
//                                        Intent(
//                                            this@ProductDetailsActivity,
//                                            MainActivity::class.java
//                                        ).apply {
//                                            flags =
//                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//                                        })
//                                    finish()
//
//                                }
//                                manage_bid.setOnClickListener {
//                                    dismiss()
//                                    startActivity(
//                                        Intent(
//                                            this@ProductDetailsActivity,
//                                            MainActivity::class.java
//                                        ).apply {
//                                            flags =
//                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                            putExtra(ConstantObjects.isBid, true)
//                                        })
//                                    finish()
//                                }
//
//                            }
//
//                        }
//                        setCanceledOnTouchOutside(false)
//                        show()
//                    }
//
//
//            }
//
//        }
//        add_to_cart.setOnClickListener {
//            current_price_buy.performClick()
//        }
//        current_price_buy.setOnClickListener {
//            AddToCart()
//        }


        btnMapSeller.setOnClickListener({
            val uri: String =
                java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", 33.7295, 73.0372)
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(uri)
                )
            )
        })

    }


    private fun replyItemClicked(question: Question) {

        answerLayout.isVisible = true
        cross_reply_layout.setOnClickListener {
            answerLayout.isVisible = false
        }
        quetsion_tv.text = question.question
        ReplyAnswer_btn.setOnClickListener {
            ReplyAnswer.text.toString().let {
                if (it.isEmpty()) {
                    showError(getString(R.string.Please_enter, getString(R.string.Answer)))
                } else {
                    PostAnsApi(question._id, it)
                }
            }
        }
    }


    fun getIgnoreCase(jobj: JsonObject, key: String?): String {
        val iter: Iterator<String> = jobj.keySet().iterator()
        while (iter.hasNext()) {
            val key1 = iter.next()
            if (key1.equals(key, ignoreCase = true)) {
                if (!jobj[key1].isJsonNull) {
                    return jobj[key1].asString ?: ""
                }
            }
        }
        return ""
    }

    private fun activeWatch(view: FloatingActionButton) {
        val myFabSrc = ContextCompat.getDrawable(this@ProductDetailsActivity, R.drawable.starcolor)
        val willBeWhite = myFabSrc!!.constantState!!.newDrawable()
        willBeWhite.mutate()
            .setColorFilter(
                ContextCompat.getColor(this@ProductDetailsActivity, R.color.bg),
                PorterDuff.Mode.MULTIPLY
            )
        view.setImageDrawable(willBeWhite)
    }


    fun PostAnsApi(questionId: String, answer: String) {
        productDetailHelper.PostAnsApi(questionId, answer) { respone ->
            if (respone.status_code >= 200 || respone.status_code <= 299) {
                answerLayout.isVisible = false
                ReplyAnswer.setText("")
                resetQuestionAndAnswerAdapter()
                HelpFunctions.ShowLongToast(
                    getString(R.string.Answerhasbeenposted),
                    this@ProductDetailsActivity
                )

            }
        }

    }


    val loginLuncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                checkPriceLayout()
            }
        }

    private fun checkPriceLayout() {
        if (HelpFunctions.isUserLoggedIn()) {
            //price_layout.isVisible = ConstantObjects.logged_userid != product.user
        }
    }


    fun AddToCart() {
        if (HelpFunctions.isUserLoggedIn()) {
            AddToUserCart(this)
        } else {
            loginLuncher.launch(Intent(this, SignInActivity::class.java))
        }
    }

    fun AddToUserCart(context: Context) {
        HelpFunctions.startProgressBar(this)
        val cartobj = InsertToCartRequestModel()
        cartobj.advertisementId = AdvId
        cartobj.userid = ConstantObjects.logged_userid

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<BasicResponse> = malqa.AddToUserCart(cartobj)
        call.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>, response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: BasicResponse = response.body()!!
                        if (resp.status_code == 200 || resp.status_code == 403 && (resp.data == true || resp.data == 1 || resp.data == 1.0)) {
                            startActivity(
                                Intent(
                                    this@ProductDetailsActivity,
                                    CartActivity::class.java
                                )
                            )
                        } else {
                            HelpFunctions.ShowLongToast(
                                resp.message,
                                context
                            )
                        }
                    } else {
                        HelpFunctions.ShowLongToast(
                            "Error",
                            context
                        );
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        "Error",
                        context
                    );
                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Toast.makeText(this@ProductDetailsActivity, t.message, Toast.LENGTH_LONG).show()
                HelpFunctions.dismissProgressBar()

            }
        })


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
            var answerDialog = AnswerQuestionDialog(
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
}
