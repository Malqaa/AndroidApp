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
import com.malka.androidappp.activities_main.order.CartActivity
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.Extension.shared
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.productResp.ProductMediaItemDetails
import com.malka.androidappp.newPhase.domain.models.productResp.ProductSpecialityItemDetails
import com.malka.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malka.androidappp.newPhase.domain.models.ratingResp.RateReviewItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.Question
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.ProductImagesAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.ReviewProductAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.SpecificationAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailHelper
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malka.androidappp.newPhase.presentation.productQuestionActivity.QuestionActivity
import com.malka.androidappp.newPhase.presentation.productReviewActivity.ProductReviewsActivity
import com.malka.androidappp.newPhase.presentation.productReviewActivity.AddRateProductActivity
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.SellerInformationActivity
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_product_details2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.rating_bar
import kotlinx.android.synthetic.main.atrribute_item.view.*
import kotlinx.android.synthetic.main.bid_alert_box.view.*
import kotlinx.android.synthetic.main.bid_confirmation.view.*
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
import kotlin.collections.ArrayList


class ProductDetailsActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners, QuestionAnswerAdapter.SetonSelectedQuestion {

    var AdvId = ""
    var selectLink = ""
    val attributeList: ArrayList<Attribute> = ArrayList()

    //    var questionList: List<Question> = ArrayList()
    var addReviewRequestrCode = 1000
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
    var added_position_from_last_similerProduct = 0
    var status_product_added_to_fav_from = 0
    var productfavStatus = false
    var favAddingChange = false
    private var userData: LoginUser? = null
    var isMyProduct = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details2)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, -1)
        isMyProduct = intent.getBooleanExtra(ConstantObjects.isMyProduct, false)
        setViewChanges()
        setProductDetailsViewModel()
        setupViewClickListeners()
        setupViewAdapters()

        onRefresh()
        //println("hhhh " + productId)
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
        //for reviewa
        tvReviewsError.hide()
        contianerRateText.hide()
        //====
        tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")
        if (HelpFunctions.isUserLoggedIn()) {
            containerMainAskQuestion.show()
            containerBuyButtons.show()
        } else {
            containerMainAskQuestion.hide()
            containerBuyButtons.hide()
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
        tvAddReview.setOnClickListener {
            if (HelpFunctions.isUserLoggedIn()) {
                startActivityForResult(Intent(this, AddRateProductActivity::class.java).apply {
                    putExtra(ConstantObjects.productIdKey, productId)
                }, addReviewRequestrCode)
            } else {
                startActivity(
                    Intent(
                        this,
                        SignInActivity::class.java
                    ).apply {})
            }
        }
        skype_btn.setOnClickListener {
            if (productDetails?.sellerInformation?.skype != null && productDetails?.sellerInformation?.skype != "") {
                HelpFunctions.openExternalLInk(productDetails?.sellerInformation?.skype!!, this)
            }
        }
        youtube_btn.setOnClickListener {
            if (productDetails?.sellerInformation?.youTube != null && productDetails?.sellerInformation?.youTube != "") {
                HelpFunctions.openExternalLInk(productDetails?.sellerInformation?.youTube!!, this)
            }
        }
        instagram_btn.setOnClickListener {
            if (productDetails?.sellerInformation?.instagram != null && productDetails?.sellerInformation?.instagram != "") {
                HelpFunctions.openExternalLInk(productDetails?.sellerInformation?.instagram!!, this)
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
//            if (HelpFunctions.AdAlreadyAddedToWatchList(AdvId)) {
//                HelpFunctions.DeleteAdFromWatchlist(
//                    AdvId,
//                    this@ProductDetailsActivity
//                ) {
//                    ivFav.setImageResource(R.drawable.star)
//                    ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.star)
//                }
//            } else {
//                if (ConstantObjects.logged_userid.isEmpty()) {
//                    startActivity(
//                        Intent(
//                            this@ProductDetailsActivity,
//                            SignInActivity::class.java
//                        ).apply {
//                        })
//                } else {
//                    HelpFunctions.InsertAdToWatchlist(
//                        AdvId, 0,
//                        this@ProductDetailsActivity
//                    ) {
//                        activeWatch(ivFav)
//                        ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.starcolor)
//                    }
//
//                }
//            }
//        }
//        if (HelpFunctions.AdAlreadyAddedToWatchList(AdvId)) {
//            activeWatch(ivFav)
//        } else {
//            ivFav.setImageResource(R.drawable.star)
//        }
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
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        containerCurrentPriceBuy.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        containerBidOnPrice.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
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
            if (it.message != null) {
                showProductApiError(it.message)
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
                    it.message,
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
                        if (added_position_from_last_similerProduct < similerProductList.size) {
                            similerProductList[added_position_from_last_similerProduct].isFavourite =
                                !similerProductList[added_position_from_last_similerProduct].isFavourite
                            similarProductAdapter.notifyItemChanged(
                                added_position_from_last_similerProduct
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
        sellerProductAdapter = ProductHorizontalAdapter(sellerSimilerProductList, this, 0, true)
        rv_seller_product.apply {
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = sellerProductAdapter
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
        productDetialsViewModel.getProductDetailsById(productId)
        productDetialsViewModel.getSimilarProduct(productId, 1)
        productDetialsViewModel.getListOfQuestions(productId)
        productDetialsViewModel.getProductRatesForProductDetails(productId)
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
            tvProductDescription.text = productDetails.description ?: ""
            current_price_buy_tv.text =
                "${productDetails.price.toString()} ${getString(R.string.sar)}"
            Bid_on_price_tv.text = " ${getString(R.string.sar)}"
            /**seller info*/
            if (productDetails.sellerInformation != null) {
                containerSellerInfo.show()
                Extension.loadThumbnail(
                    this,
                    productDetails.sellerInformation.image,
                    seller_picture,
                    loader
                )
                sellerName.text = productDetails.sellerInformation.name ?: ""
                member_since_Tv.text = HelpFunctions.getViewFormatForDateTrack(
                    productDetails.sellerInformation.createdAt ?: ""
                )
                seller_city.text = productDetails.sellerInformation.city ?: ""
                seller_number.text = productDetails.sellerInformation.phone ?: ""
            } else {
                containerSellerInfo.hide()
            }
            if (productDetails?.sellerInformation?.instagram != null && productDetails?.sellerInformation?.instagram != "") {
                instagram_btn.show()
            } else {
                instagram_btn.hide()
            }
            if (productDetails?.sellerInformation?.youTube != null && productDetails?.sellerInformation?.youTube != "") {
                youtube_btn.show()
            } else {
                youtube_btn.hide()
            }
            if (productDetails?.sellerInformation?.skype != null && productDetails?.sellerInformation?.skype != "") {
                skype_btn.show()
            } else {
                skype_btn.hide()
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
            /**pidding views*/
            containerAuctioncountdownTimer_bar.show()
            tvAuctionNumber.text = "${getString(R.string.bidding)} "
            productfavStatus = productDetails.isFavourite
            if (productDetails.isFavourite) {
                ivFav.setImageResource(R.drawable.starcolor)
            } else {
                ivFav.setImageResource(R.drawable.star)
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
        startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, productID)
            putExtra("Template", "")
        })
        finish()
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            status_product_added_to_fav_from = added_from_last_similerProducts_status
            added_position_from_last_similerProduct = position
            productDetialsViewModel.addProductToFav(productID)
        } else {
            startActivity(
                Intent(
                    this,
                    SignInActivity::class.java
                ).apply {})
        }


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


        maps_btn.setOnClickListener({
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
        if (resultCode == RESULT_OK && requestCode == addReviewRequestrCode) {
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

    /** NotNeed Function delete latter**/

    //    fun getadbyidapi(advid: String) {
//        HelpFunctions.startProgressBar(this)
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val call = malqa.getAdDetailById2(advid)
//        call.enqueue(object : Callback<GeneralResponse> {
//            @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
//            override fun onResponse(
//                call: Call<GeneralResponse>,
//                response: Response<GeneralResponse>
//            ) {
//
//
//                if (response.isSuccessful) {
//                    response.body()?.run {
//                        if (status_code == 200) {
//                            product = Gson().fromJson(
//                                Gson().toJson(data),
//                                object : TypeToken<Product>() {}.type
//                            )
//                            product.run {
//                                checkPriceLayout()
//                                getSellerByID("")
//                                when ("listingtype") {
//                                    "1" -> {
//                                        add_to_cart.show()
//                                        current_price_buy_tv_2.text =
//                                            "${price!!.toDouble().decimalNumberFormat()} ${
//                                                getString(
//                                                    R.string.sar
//                                                )
//                                            }"
//                                    }
//                                    "2" -> {
//                                        Bid_on_price.show()
////                                        Bid_on_price_tv.text =
////                                            "${startingPrice!!.toDouble().decimalNumberFormat()} ${
////                                                getString(
////                                                    R.string.sar
////                                                )
////                                            }"
//
//                                    }
//                                    "12" -> {
//                                        current_price_buy.show()
//                                        Bid_on_price.show()
//                                        current_price_buy_tv.text =
//                                            "${price!!.toDouble().decimalNumberFormat()} ${
//                                                getString(
//                                                    R.string.sar
//                                                )
//                                            }"
//
////                                        Bid_on_price_tv.text =
////                                            "${startingPrice!!.toDouble().decimalNumberFormat()} ${
////                                                getString(
////                                                    R.string.sar
////                                                )
////                                            }"
//
//                                    }
//                                }
//
//
//                                if (!"template".isNullOrEmpty()) {
//                                    HelpFunctions.GetTemplatesJson(
//                                        "js"
//                                    ) { json_string ->
//                                        if (json_string.trim().length > 0) {
//                                            val parsed_data = JSONObject(json_string)
//                                            val controls_array: JSONArray =
//                                                parsed_data.getJSONArray("data")
//                                            for (i in 0 until controls_array.length()) {
//                                                val IndControl = controls_array.getJSONObject(i)
//                                                val id = IndControl.getString("id")
////                                                getIgnoreCase(jsonObject!!, id).let { value ->
////                                                    if (!value.isEmpty()) {
////                                                        val key = IndControl.getString("title") ?: ""
////                                                        attributeList.add(Attribute(key, value))
////
////                                                    }
////                                                }
//
//                                            }
////                                            attributeList.add(
////                                                Attribute(
////                                                    getString(R.string.item_condition),
////                                                    brand_new_item ?: ""
////                                                )
////                                            )
////                                            attributeList.add(
////                                                Attribute(
////                                                    getString(R.string.quantity),
////                                                    quantity ?: ""
////                                                )
////                                            )
////                                            attributeAdaptor(attributeList)
//                                        }
//                                    }
//
//
//                                }
//
//
//                                //SharedPreferencesStaticClass.ad_userid = product.user!!
//                                if (listMedia!!.size > 0) {
////                                    val imageURL = Constants.IMAGE_URL + images.get(0)
////                                    selectLink = imageURL
////                                    loadThumbnail(
////                                        this@ProductDetails,
////                                        imageURL,
////                                        productimg, loader
////                                    )
////                                    productimg.setOnClickListener {
////                                        startActivity(
////                                            Intent(
////                                                this@ProductDetails,
////                                                FullImageActivity::class.java
////                                            ).putExtra(
////                                                "imageUri",
////                                                selectLink
////                                            )
////                                        )
////                                    }
//
//                                }
//                                val productImage: ArrayList<ProductImage> = ArrayList()
//
////                                if (!video.isNullOrEmpty()) {
////                                    productImage.add(ProductImage(video, true))
////                                }
////                                images.forEachIndexed { index, s ->
////                                    if (index == 0) {
////                                        productImage.add(ProductImage(s, is_select = true))
////                                    } else {
////                                        productImage.add(ProductImage(s))
////                                    }
////                                }
//
//                                if (productImage.size > 1) {
//                                    setCategoryAdaptor(productImage)
//                                } else {
//                                    other_image_layout.isVisible = false
//                                }
//
//                                tvProductItemName.text = name
//                                tvProductSubtitle.text = subTitle
//                                tvProductDescription.text = description
//                                tvProductReview.text =
//                                    getString(R.string.itemView, countView.toString().toInt())
//                                tvProductReview.text = createdOnFormated
//                                tvProductReview.text = "#$id"
//
////
////                                try {
////                                    val format= SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
////                                    if (endTime.equals("") || endTime == null) {
////                                        countdownTimer_bar.visibility = View.GONE
////                                    } else {
////                                        countdownTimer_bar.visibility = View.VISIBLE
////                                        val endDate = format.parse(endTime)
////
////
////                                        val currentDate = format.parse(format.format(Date()))
////
////                                        if (endDate.before(currentDate)) {
////                                            days.text = "0"
////                                            hours.text = "0"
////                                            minutes.text = "0"
////                                        } else {
////                                            var diff: Long = endDate!!.getTime() - currentDate!!.getTime()
////
////                                            val secondsInMilli: Long = 1000
////                                            val minutesInMilli = secondsInMilli * 60
////                                            val hoursInMilli = minutesInMilli * 60
////                                            val daysInMilli = hoursInMilli * 24
////
////                                            val elapsedDays = diff / daysInMilli
////                                            diff %= daysInMilli
////
////                                            val elapsedHours = diff / hoursInMilli
////                                            diff %= hoursInMilli
////
////                                            val elapsedMinutes = diff / minutesInMilli
////                                            diff %= minutesInMilli
////
////
////                                            days.text = elapsedDays.toString()
////                                            hours.text = elapsedHours.toString()
////                                            minutes.text = elapsedMinutes.toString()
////                                        }
////
////                                    }
////                                } catch (error: Exception) {
////                                    countdownTimer_bar.visibility = View.GONE
////                                }
////
//
//

//
//
//                            var isSellerProductHide = true
//                            btnSellerProducts.setOnClickListener {
//                                if (isSellerProductHide) {
//                                    isSellerProductHide = false
//                                    rv_seller_product.isVisible = true
//                                    isSellerProductHide_iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
//                                    seller_product_tv.text =
//                                        getText(R.string.view_similar_product_from_seller)
//
//                                } else {
//                                    isSellerProductHide = true
//                                    rv_seller_product.isVisible = false
//                                    isSellerProductHide_iv.setImageResource(R.drawable.down_arrow)
//                                    seller_product_tv.text =
//                                        getText(R.string.view_similar_product_from_seller)
//
//
//                                }
//                            }
//
////                    GenericAdaptor().setHomeProductAdaptor(list, seller_product_rcv)
//
//
//                            mainContainer.isVisible = true
//                        }
//                    }
//
//
//                } else {
//                    HelpFunctions.ShowAlert(
//                        this@ProductDetailsActivity,
//                        getString(R.string.Information),
//                        getString(R.string.NoRecordFound)
//                    )
//                }
//                HelpFunctions.dismissProgressBar()
//
//            }
//
//
//            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
//                HelpFunctions.ShowLongToast(t.message!!, this@ProductDetailsActivity)
//                HelpFunctions.dismissProgressBar()
//
//            }
//        })
//
//    }

//private fun attributeAdaptor(list: List<Attribute>) {
//    rvProductSpecification.adapter = object : GenericListAdapter<Attribute>(
//        R.layout.atrribute_item,
//        bind = { element, holder, itemCount, position ->
//            holder.view.run {
//                element.run {
//                    key_tv.text = key
//                    value_tv.text = value
//                    if (position % 2 == 0) {
//                        main_layout.background = null
//                    } else {
//                        main_layout.background =
//                            ContextCompat.getDrawable(
//                                this@ProductDetailsActivity,
//                                R.drawable.product_attribute_bg_gray
//                            )
//                    }
//                }
//
//            }
//        }
//    ) {
//        override fun getFilter(): Filter {
//            TODO("Not yet implemented")
//        }
//
//    }.apply {
//        submitList(
//            list
//        )
//    }
//}


//    private fun setCategoryAdaptor(list: List<ProductImage>) {
//        next_image.isVisible = list.size > 3
//
//        rvProductImages.adapter = object : GenericListAdapter<ProductImage>(
//            R.layout.item_image_for_product_details,
//            bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    element.run {
//                        if (is_select) {
//                            product_image.borderColor = ContextCompat.getColor(
//                                this@ProductDetailsActivity,
//                                R.color.bg
//                            )
//                        } else {
//                            product_image.borderColor = ContextCompat.getColor(
//                                this@ProductDetailsActivity,
//                                R.color.white
//                            )
//                        }
//
//
//                        val imageURL = Constants.IMAGE_URL + link
//                        if (isVideo) {
//                            loadThumbnail(
//                                this@ProductDetailsActivity,
//                                link,
//                                product_image, loader
//                            )
//                            {
//                                is_video_iv.isVisible = true
//                            }
//                        } else {
//                            is_video_iv.isVisible = false
//
//                            loadThumbnail(
//                                this@ProductDetailsActivity,
//                                imageURL,
//                                product_image, loader
//                            )
//
//                        }
//                        setOnClickListener {
//                            if (isVideo) {
//                                startActivity(
//                                    Intent(
//                                        this@ProductDetailsActivity,
//                                        PlayActivity::class.java
//                                    ).putExtra(
//                                        "videourl",
//                                        link
//                                    )
//                                )
//
//                            } else {
//                                list.forEach {
//                                    it.is_select = false
//                                }
//                                is_select = true
//                                rvProductImages.post({ rvProductImages.adapter!!.notifyDataSetChanged() })
//                                selectLink = imageURL
//                                loadThumbnail(
//                                    this@ProductDetailsActivity,
//                                    imageURL,
//                                    productimg, loader
//                                )
//                            }
//
//
//                        }
//                    }
//
//                }
//            }
//        ) {
//            override fun getFilter(): Filter {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(
//                list
//            )
//        }
//    }

//    private fun getSimilarproducts() {
//
//        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
//        val call: Call<BasicResponse> = malqa.getsimilar()
//
//        call.enqueue(object : Callback<BasicResponse> {
//            override fun onResponse(
//                call: Call<BasicResponse>,
//                response: Response<BasicResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val similarData = response.body()
//                    similarData?.let {
//                        it.run {
//
//                            if (status_code == 200) {
//                                if (data != null) {
//                                    val similarList: ArrayList<Product> = Gson().fromJson(
//                                        Gson().toJson(data),
//                                        object :
//                                            com.google.gson.reflect.TypeToken<ArrayList<Product>>() {}.type
//
//                                    )
//                                    GenericAdaptor().setHomeProductAdaptor(
//                                        similarList,
//                                        rvSimilarProducts
//                                    )
//                                }
//                            }
//
//
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }
    //    private fun enableSwipeToDeleteAndUndo() {
//        val messageSwipeController =
//            MessageSwipeController(this, object : SwipeControllerActions {
//                override fun showReplyUI(position: Int) {
//                    val question = questionList.get(position)
//                    replyItemClicked(question)
//                    vibration()
//                }
//            })
//        val itemTouchHelper = ItemTouchHelper(messageSwipeController)
//        itemTouchHelper.attachToRecyclerView(rvQuestionForProduct)
//
//    }



//productDetailHelper = ProductDetailHelper(this)
//        product_attribute.isVisible = true
//        quest_ans_rcv.isVisible = true
//        answerLayout.isVisible = false
//        getSimilarproducts()
//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<View>(bottom_sheet)
//        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    icon_layout.setVisibility(View.GONE)
//                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    icon_layout.setVisibility(View.GONE)
//                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                } else {
//                    icon_layout.setVisibility(View.VISIBLE)
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//        })
//        behavior.peekHeight = getResources().getDimension(R.dimen._360sdp).toInt()
//        mainContainer.isVisible = false
//        AdvId = intent.getIntExtra("AdvId",-1).toString()
//
//        getadbyidapi(AdvId)
//
//        quesAnss()
//
//        setListenser()
//
//
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed3",
//                "12/12/2022",
//                "Good and fast delivery",
//                "2.9",
//                R.drawable.car
//            )
//        )
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed4",
//                "16/12/2022",
//                "Great Experience ",
//                "3.0",
//                R.drawable.car
//            )
//        )
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed5",
//                "10/12/2022",
//                "Excelent fast delivery",
//                "3.6",
//                R.drawable.car
//            )
//        )
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed6",
//                "5/12/2022",
//                "Amazing and fast delivery",
//                "4.9",
//                R.drawable.car
//            )
//        )
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed3",
//                "12/12/2022",
//                "Good and fast delivery",
//                "2.9",
//                R.drawable.car
//            )
//        )
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed4",
//                "16/12/2022",
//                "Great Experience ",
//                "2.0",
//                R.drawable.car
//            )
//        )
//        reviewlist.add(
//            Reviewmodel(
//                "Ahmed4",
//                "16/12/2022",
//                "Great Experience ",
//                "4.0",
//                R.drawable.car
//            )
//        )
//
//        var totalRating = 0.0
//        reviewlist.forEach {
//            totalRating += it.rating.toDouble()
//
//        }
//        val average = totalRating / reviewlist.size
//        rating_bar.rating = average.toFloat()
//        rating_bar_detail_tv.text = getString(
//            R.string._4_9_from_00_visitors,
//            rating_bar.rating.toString().format("%.2f"),
//            reviewlist.size.toString()
//        )
//        setReviewsAdapter(reviewlist)


//private fun setReviewsAdapter(list: ArrayList<Reviewmodel>) {
//    rv_review.adapter = object : GenericListAdapter<Reviewmodel>(
//        R.layout.item_review_product,
//        bind = { element, holder, itemCount, position ->
//            holder.view.run {
//                element.run {
//                    review_name_tv.text = name
//
//                    rating_bar.rating = rating.toFloat()
//                    comment_tv.text = comment
//
//
//                }
//            }
//        }
//    ) {
//        override fun getFilter(): Filter {
//            TODO("Not yet implemented")
//        }
//
//    }.apply {
//        submitList(
//
//            list.take(3)
//        )
//    }
//}
