package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Filter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.activities_main.PlayActivity
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.activities_main.order.CartActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.fragments.cardetail_page.ModelSellerDetails
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.data.helper.Extension.loadThumbnail
import com.malka.androidappp.newPhase.data.helper.Extension.shared
import com.malka.androidappp.newPhase.data.helper.GenericAdaptor
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.HelpFunctions.Companion.openExternalLInk
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.swipe.MessageSwipeController
import com.malka.androidappp.newPhase.data.helper.swipe.SwipeControllerActions
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.addtocart.InsertToCartRequestModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.Question
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.AttributeAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.ReviewProductAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailHelper
import com.malka.androidappp.newPhase.presentation.productQuestionActivity.QuestionActivity
import com.malka.androidappp.newPhase.presentation.productReviewActivity.ProductReviewsActivity
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.SellerInformationActivity
import com.squareup.picasso.Picasso
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_product_details.fbButtonBack
import kotlinx.android.synthetic.main.activity_product_details.current_price_buy_tv
import kotlinx.android.synthetic.main.activity_product_details.image_rcv
import kotlinx.android.synthetic.main.activity_product_details.next_image
import kotlinx.android.synthetic.main.activity_product_details.other_image_layout
import kotlinx.android.synthetic.main.activity_product_details2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import kotlinx.android.synthetic.main.activity_product_details_item_2.rating_bar
import kotlinx.android.synthetic.main.atrribute_item.view.*
import kotlinx.android.synthetic.main.bid_alert_box.view.*
import kotlinx.android.synthetic.main.bid_confirmation.view.*
import kotlinx.android.synthetic.main.image_item.view.*
import kotlinx.android.synthetic.main.image_item.view.loader
import kotlinx.android.synthetic.main.item_review_product.*
import kotlinx.android.synthetic.main.product_item.view.*

import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ProductDetailsActivity : BaseActivity() {

    var reviewlist: ArrayList<Reviewmodel> = ArrayList()
    var AdvId = ""
    var selectLink = ""
    val attributeList: ArrayList<Attribute> = ArrayList()
    var questionList: List<Question> = ArrayList()
    lateinit var product: Product
    lateinit var attributesAdapter: AttributeAdapter

    lateinit var productDetailHelper: ProductDetailHelper
    lateinit var questionAnswerAdapter:QuestionAnswerAdapter
    lateinit var reviewProductAdapter:ReviewProductAdapter
    lateinit var sellerProductAdapter: ProductHorizontalAdapter
    lateinit var similarProductAdapter: ProductHorizontalAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details2)
        setViewChanges()
        setProductData()
        setupViewClickListeners()
        setupViewAdapters()


//        productDetailHelper = ProductDetailHelper(this)
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

    }

    private fun setViewChanges() {
        if(Lingver.getInstance().getLanguage()==ConstantObjects.ARABIC) {
            fbButtonBack.scaleX=1f
        }else{
            fbButtonBack.scaleX=-1f
        }
    }

    private fun setupViewAdapters() {

        setAttributesAdapter()
        setQuestionAnswerAdapter()
        setReviewsAdapter()
        setSellerAdapter()
        setSimilarProduct()
    }

    private fun setSimilarProduct() {
     similarProductAdapter= ProductHorizontalAdapter()
        rv_similar_products.apply {
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = similarProductAdapter
        }
    }

    private fun setSellerAdapter() {
        sellerProductAdapter= ProductHorizontalAdapter()
        rv_seller_product.apply {
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
            adapter = sellerProductAdapter
        }
    }

    private fun   setReviewsAdapter(){
        reviewProductAdapter = ReviewProductAdapter()
        rv_review.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = reviewProductAdapter
        }
    }
    private fun setAttributesAdapter() {
        attributesAdapter = AttributeAdapter()
        rvProductAttribute.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = attributesAdapter
        }
    }
    private fun setQuestionAnswerAdapter(){
        questionAnswerAdapter= QuestionAnswerAdapter()
        rv_quest_ans.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = questionAnswerAdapter
        }
    }

    private fun setProductData() {
        tvProductReview.text = "0 ${getString(R.string.Views)} - #180 /12/2022"
        tvProductItemName.text = "product Name"
        tvProductSubtitle.text = "It has no Subtitle"
        containerAuctioncountdownTimer_bar.show()
        tvAuctionNumber.text = "${getString(R.string.bidding)} not implemented"
        tvProductDescription.text = "details"
    }

    private fun setupViewClickListeners() {
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
        tvShowAllReviews.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        btnSellerProducts.setOnClickListener {
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        containerCurrentPriceBuy.setOnClickListener{
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
        containerBidOnPrice.setOnClickListener{
            HelpFunctions.ShowLongToast("not implemented yey", this)
        }
    }

    /****************/
    private fun getSimilarproducts() {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<BasicResponse> = malqa.getsimilar()

        call.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    val similarData = response.body()
                    similarData?.let {
                        it.run {

                            if (status_code == 200) {
                                if (data != null) {
                                    val similarList: ArrayList<Product> = Gson().fromJson(
                                        Gson().toJson(data),
                                        object :
                                            com.google.gson.reflect.TypeToken<ArrayList<Product>>() {}.type

                                    )
                                    GenericAdaptor().setHomeProductAdaptor(
                                        similarList,
                                        rv_similar_products
                                    )
                                }
                            }


                        }
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

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





        ask_question.setOnClickListener {
            confrmAskQues()
        }

        tvShowAllReviews.setOnClickListener {
            startActivity(Intent(this, ProductReviewsActivity::class.java).apply {

            })
        }

        tvQuestionAndAnswersShowAll.setOnClickListener {
            startActivity(Intent(this, QuestionActivity::class.java).apply {
                putExtra("AdvId", AdvId)
            })
        }


        Bid_on_price.setOnClickListener {

            val builder = AlertDialog.Builder(this@ProductDetailsActivity)
                .create()
            val view = layoutInflater.inflate(R.layout.bid_alert_box, null)
            builder.setView(view)
            view.close_alert_bid.setOnClickListener {
                builder.dismiss()
            }

            builder.setCanceledOnTouchOutside(false)
            builder.show()


            view.btn_bid.setOnClickListener {
                builder.dismiss()
                AlertDialog.Builder(this@ProductDetailsActivity)
                    .create().apply {
                        layoutInflater.inflate(R.layout.bid_confirmation, null).also {
                            this.setView(it)
                            it.apply {
                                close_alert.setOnClickListener {
                                    dismiss()
                                }

                                back_to_shopping.setOnClickListener {
                                    dismiss()
                                    startActivity(
                                        Intent(
                                            this@ProductDetailsActivity,
                                            MainActivity::class.java
                                        ).apply {
                                            flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                        })
                                    finish()

                                }
                                manage_bid.setOnClickListener {
                                    dismiss()
                                    startActivity(
                                        Intent(
                                            this@ProductDetailsActivity,
                                            MainActivity::class.java
                                        ).apply {
                                            flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            putExtra(ConstantObjects.isBid, true)
                                        })
                                    finish()
                                }

                            }

                        }
                        setCanceledOnTouchOutside(false)
                        show()
                    }


            }

        }
        add_to_cart.setOnClickListener {
            current_price_buy.performClick()
        }
        current_price_buy.setOnClickListener {
            AddToCart()
        }

        youtube_btn.setOnClickListener({

            openExternalLInk(
                "https://www.youtube.com/watch?v=KioO9frme6c", this
            )
        })

        instagram_btn.setOnClickListener({

            openExternalLInk(
                "https://www.instagram.com/reel/CcGMHEwjSAV/?utm_source=ig_web_copy_link", this
            )
        })

        skype_btn.setOnClickListener({

            openExternalLInk(
                "https://www.skype.com/", this
            )
        })

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

    override fun onBackPressed() {
        intent.getBooleanExtra(ConstantObjects.isSuccess, false).let {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java).apply {
                })
                finish()
            } else {
                finish()
            }
        }

    }

    private fun setCategoryAdaptor(list: List<ProductImage>) {
        next_image.isVisible = list.size > 3

        image_rcv.adapter = object : GenericListAdapter<ProductImage>(
            R.layout.image_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        if (is_select) {
                            product_image.borderColor = ContextCompat.getColor(
                                this@ProductDetailsActivity,
                                R.color.bg
                            )
                        } else {
                            product_image.borderColor = ContextCompat.getColor(
                                this@ProductDetailsActivity,
                                R.color.white
                            )
                        }


                        val imageURL = Constants.IMAGE_URL + link
                        if (isVideo) {
                            loadThumbnail(
                                this@ProductDetailsActivity,
                                link,
                                product_image, loader
                            ) {
                                is_video_iv.isVisible = true
                            }
                        } else {
                            is_video_iv.isVisible = false

                            loadThumbnail(
                                this@ProductDetailsActivity,
                                imageURL,
                                product_image, loader
                            )

                        }
                        setOnClickListener {

                            if (isVideo) {
                                startActivity(
                                    Intent(
                                        this@ProductDetailsActivity,
                                        PlayActivity::class.java
                                    ).putExtra(
                                        "videourl",
                                        link
                                    )
                                )

                            } else {
                                list.forEach {
                                    it.is_select = false
                                }
                                is_select = true
                                image_rcv.post({ image_rcv.adapter!!.notifyDataSetChanged() })
                                selectLink = imageURL
                                loadThumbnail(
                                    this@ProductDetailsActivity,
                                    imageURL,
                                    productimg, loader
                                )
                            }


                        }
                    }

                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }

    private fun attributeAdaptor(list: List<Attribute>) {
        rvProductAttribute.adapter = object : GenericListAdapter<Attribute>(
            R.layout.atrribute_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        key_tv.text = key
                        value_tv.text = value
                        if (position % 2 == 0) {
                            main_layout.background = null
                        } else {
                            main_layout.background =
                                ContextCompat.getDrawable(
                                    this@ProductDetailsActivity,
                                    R.drawable.product_attribute_bg
                                )
                        }
                    }

                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }

    private fun enableSwipeToDeleteAndUndo() {
        val messageSwipeController =
            MessageSwipeController(this, object : SwipeControllerActions {
                override fun showReplyUI(position: Int) {
                    val question = questionList.get(position)
                    replyItemClicked(question)
                    vibration()
                }
            })
        val itemTouchHelper = ItemTouchHelper(messageSwipeController)
        itemTouchHelper.attachToRecyclerView(rv_quest_ans)

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

    fun getadbyidapi(advid: String) {
        HelpFunctions.startProgressBar(this)
        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getAdDetailById(advid)
        call.enqueue(object : Callback<GeneralResponse> {
            @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {


                if (response.isSuccessful) {
                    response.body()?.run {
                        if (status_code == 200) {
                            product = Gson().fromJson(
                                Gson().toJson(data),
                                object : TypeToken<Product>() {}.type
                            )
                            product.run {
                                checkPriceLayout()
                                getSellerByID("")
                                when ("listingtype") {
                                    "1" -> {
                                        add_to_cart.show()
                                        current_price_buy_tv_2.text =
                                            "${price!!.toDouble().decimalNumberFormat()} ${
                                                getString(
                                                    R.string.sar
                                                )
                                            }"
                                    }
                                    "2" -> {
                                        Bid_on_price.show()
//                                        Bid_on_price_tv.text =
//                                            "${startingPrice!!.toDouble().decimalNumberFormat()} ${
//                                                getString(
//                                                    R.string.sar
//                                                )
//                                            }"

                                    }
                                    "12" -> {
                                        current_price_buy.show()
                                        Bid_on_price.show()
                                        current_price_buy_tv.text =
                                            "${price!!.toDouble().decimalNumberFormat()} ${
                                                getString(
                                                    R.string.sar
                                                )
                                            }"

//                                        Bid_on_price_tv.text =
//                                            "${startingPrice!!.toDouble().decimalNumberFormat()} ${
//                                                getString(
//                                                    R.string.sar
//                                                )
//                                            }"

                                    }
                                }


                                if (!"template".isNullOrEmpty()) {
                                    HelpFunctions.GetTemplatesJson(
                                        "js"
                                    ) { json_string ->
                                        if (json_string.trim().length > 0) {
                                            val parsed_data = JSONObject(json_string)
                                            val controls_array: JSONArray =
                                                parsed_data.getJSONArray("data")
                                            for (i in 0 until controls_array.length()) {
                                                val IndControl = controls_array.getJSONObject(i)
                                                val id = IndControl.getString("id")
//                                                getIgnoreCase(jsonObject!!, id).let { value ->
//                                                    if (!value.isEmpty()) {
//                                                        val key = IndControl.getString("title") ?: ""
//                                                        attributeList.add(Attribute(key, value))
//
//                                                    }
//                                                }

                                            }
//                                            attributeList.add(
//                                                Attribute(
//                                                    getString(R.string.item_condition),
//                                                    brand_new_item ?: ""
//                                                )
//                                            )
//                                            attributeList.add(
//                                                Attribute(
//                                                    getString(R.string.quantity),
//                                                    quantity ?: ""
//                                                )
//                                            )
//                                            attributeAdaptor(attributeList)
                                        }
                                    }


                                }


                                //SharedPreferencesStaticClass.ad_userid = product.user!!
                                if (listMedia!!.size > 0) {
//                                    val imageURL = Constants.IMAGE_URL + images.get(0)
//                                    selectLink = imageURL
//                                    loadThumbnail(
//                                        this@ProductDetails,
//                                        imageURL,
//                                        productimg, loader
//                                    )
//                                    productimg.setOnClickListener {
//                                        startActivity(
//                                            Intent(
//                                                this@ProductDetails,
//                                                FullImageActivity::class.java
//                                            ).putExtra(
//                                                "imageUri",
//                                                selectLink
//                                            )
//                                        )
//                                    }

                                }
                                val productImage: ArrayList<ProductImage> = ArrayList()

//                                if (!video.isNullOrEmpty()) {
//                                    productImage.add(ProductImage(video, true))
//                                }
//                                images.forEachIndexed { index, s ->
//                                    if (index == 0) {
//                                        productImage.add(ProductImage(s, is_select = true))
//                                    } else {
//                                        productImage.add(ProductImage(s))
//                                    }
//                                }

                                if (productImage.size > 1) {
                                    setCategoryAdaptor(productImage)
                                } else {
                                    other_image_layout.isVisible = false
                                }

                                tvProductItemName.text = name
                                tvProductSubtitle.text = subTitle
                                tvProductDescription.text = description
                                tvProductReview.text =
                                    getString(R.string.itemView, countView.toString().toInt())
                                tvProductReview.text = createdOnFormated
                                tvProductReview.text = "#$id"

//
//                                try {
//                                    val format= SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
//                                    if (endTime.equals("") || endTime == null) {
//                                        countdownTimer_bar.visibility = View.GONE
//                                    } else {
//                                        countdownTimer_bar.visibility = View.VISIBLE
//                                        val endDate = format.parse(endTime)
//
//
//                                        val currentDate = format.parse(format.format(Date()))
//
//                                        if (endDate.before(currentDate)) {
//                                            days.text = "0"
//                                            hours.text = "0"
//                                            minutes.text = "0"
//                                        } else {
//                                            var diff: Long = endDate!!.getTime() - currentDate!!.getTime()
//
//                                            val secondsInMilli: Long = 1000
//                                            val minutesInMilli = secondsInMilli * 60
//                                            val hoursInMilli = minutesInMilli * 60
//                                            val daysInMilli = hoursInMilli * 24
//
//                                            val elapsedDays = diff / daysInMilli
//                                            diff %= daysInMilli
//
//                                            val elapsedHours = diff / hoursInMilli
//                                            diff %= hoursInMilli
//
//                                            val elapsedMinutes = diff / minutesInMilli
//                                            diff %= minutesInMilli
//
//
//                                            days.text = elapsedDays.toString()
//                                            hours.text = elapsedHours.toString()
//                                            minutes.text = elapsedMinutes.toString()
//                                        }
//
//                                    }
//                                } catch (error: Exception) {
//                                    countdownTimer_bar.visibility = View.GONE
//                                }
//


                                ivFav.setOnClickListener {
                                    if (HelpFunctions.AdAlreadyAddedToWatchList(AdvId)) {
                                        HelpFunctions.DeleteAdFromWatchlist(
                                            AdvId,
                                            this@ProductDetailsActivity
                                        ) {
                                            ivFav.setImageResource(R.drawable.star)
                                            ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.star)
                                        }
                                    } else {
                                        if (ConstantObjects.logged_userid.isEmpty()) {
                                            startActivity(
                                                Intent(
                                                    this@ProductDetailsActivity,
                                                    SignInActivity::class.java
                                                ).apply {
                                                })
                                        } else {

                                            HelpFunctions.InsertAdToWatchlist(
                                                AdvId, 0,
                                                this@ProductDetailsActivity
                                            ) {
                                                activeWatch(ivFav)
                                                ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.starcolor)
                                            }

                                        }
                                    }
                                }
                                if (HelpFunctions.AdAlreadyAddedToWatchList(AdvId)) {
                                    activeWatch(ivFav)
                                } else {
                                    ivFav.setImageResource(R.drawable.star)
                                }
                            }


                            var isSellerProductHide = true
                            btnSellerProducts.setOnClickListener {
                                if (isSellerProductHide) {
                                    isSellerProductHide = false
                                    rv_seller_product.isVisible = true
                                    isSellerProductHide_iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                                    seller_product_tv.text =
                                        getText(R.string.view_similar_product_from_seller)

                                } else {
                                    isSellerProductHide = true
                                    rv_seller_product.isVisible = false
                                    isSellerProductHide_iv.setImageResource(R.drawable.down_arrow)
                                    seller_product_tv.text =
                                        getText(R.string.view_similar_product_from_seller)


                                }
                            }

//                    GenericAdaptor().setHomeProductAdaptor(list, seller_product_rcv)


                            mainContainer.isVisible = true
                        }
                    }


                } else {
                    HelpFunctions.ShowAlert(
                        this@ProductDetailsActivity,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()

            }


            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                HelpFunctions.ShowLongToast(t.message!!, this@ProductDetailsActivity)
                HelpFunctions.dismissProgressBar()

            }
        })

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

    private fun validateAskQuesInputText(): Boolean {
        val Inputemail = editTextque!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Question)))
            false
        } else {
            true
        }
    }


    fun confrmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            askquesApi()
        }

    }

    fun askquesApi() {
        productDetailHelper.askquesApi(editTextque.text.toString(), AdvId, {
            editTextque.setText("")
            quesAnss()
        })
    }


    fun quesAnss() {
        productDetailHelper.quesAnss(AdvId) {
            it.run {
                tvNumberQuestionNotAnswer.text = getString(
                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
                    unAnswered.toString()
                )

                questionList = questions
                questionList = questionList.take(3)
                GenericAdaptor().questionAnswerAdaptor(rv_quest_ans, questionList)
                if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
                    askques_bottom.visibility = View.GONE
                    enableSwipeToDeleteAndUndo()

                } else if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
                    askques_bottom.visibility = View.VISIBLE

                }
            }

        }

    }


    fun PostAnsApi(questionId: String, answer: String) {


        productDetailHelper.PostAnsApi(questionId, answer) { respone ->


            if (respone.status_code >= 200 || respone.status_code <= 299) {
                answerLayout.isVisible = false
                ReplyAnswer.setText("")
                quesAnss()
                HelpFunctions.ShowLongToast(
                    getString(R.string.Answerhasbeenposted),
                    this@ProductDetailsActivity
                )

            }
        }

    }


    private fun getSellerByID(id: String) {


        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelSellerDetails> = malqa.getAdSellerByID(id)

        call.enqueue(object : Callback<ModelSellerDetails> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ModelSellerDetails>,
                response: Response<ModelSellerDetails>
            ) {
                if (response.isSuccessful) {
                    val sellerData = response.body()!!.data
                    sellerData?.let {
                        it.run {
                            sellerName.text = fullName ?: ""
                            seller_city.text = city
                            seller_number.text = phone
                            member_since_Tv.text =
                                "${getString(R.string.member_since)}: $member_since"
                            val imageLink = image
                            if (!imageLink.isNullOrEmpty()) {
                                Picasso.get().load(imageLink)
                                    .error(R.drawable.profiledp).placeholder(R.drawable.profiledp)
                                    .into(seller_picture)
                            } else {
                                seller_picture.setImageResource(R.drawable.profiledp)
                            }
                        }
                    } ?: kotlin.run {
                        seller_information.isVisible = false
                    }


                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.NoRecordFound),
                        this@ProductDetailsActivity
                    )
                }
            }

            override fun onFailure(call: Call<ModelSellerDetails>, t: Throwable) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.Somethingwentwrong),
                    this@ProductDetailsActivity
                )
            }
        })
    }

    val loginLuncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                checkPriceLayout()
            }
        }

    private fun checkPriceLayout() {
        if (HelpFunctions.IsUserLoggedIn()) {
            //price_layout.isVisible = ConstantObjects.logged_userid != product.user
        }
    }


    fun AddToCart() {
        if (HelpFunctions.IsUserLoggedIn()) {
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
                            startActivity(Intent(this@ProductDetailsActivity, CartActivity::class.java))
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

    private fun setReviewsAdapter(list: ArrayList<Reviewmodel>) {
        rv_review.adapter = object : GenericListAdapter<Reviewmodel>(
            R.layout.item_review_product,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        review_name_tv.text = name

                        rating_bar.rating = rating.toFloat()
                        comment_tv.text = comment


                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(

                list.take(3)
            )
        }
    }

}