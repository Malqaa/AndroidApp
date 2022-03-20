package com.malka.androidappp.activities_main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.QuesAnsFragment
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.design.ProductReviews
import com.malka.androidappp.helper.Extension.loadThumbnail
import com.malka.androidappp.helper.Extension.shared
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.Attribute
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.ProductImage
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.atrribute_item.view.*
import kotlinx.android.synthetic.main.image_item.view.*
import kotlinx.android.synthetic.main.image_item.view.loader
import kotlinx.android.synthetic.main.parenet_category_item.view.*
import kotlinx.android.synthetic.main.product_detail_2.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductDetails : BaseActivity() {

    var AdvId = ""
    var template = ""
    var selectLink = ""
    val attributeList: ArrayList<Attribute> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)


        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<View>(bottom_sheet)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {


            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    icon_layout.setVisibility(View.GONE)
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    icon_layout.setVisibility(View.GONE)
                } else {
                    icon_layout.setVisibility(View.VISIBLE)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        behavior.peekHeight =getResources().getDimension(R.dimen._360sdp).toInt()

        mainContainer.isVisible = false
        AdvId = intent.getStringExtra("AdvId") ?: "uajl8160XC"
        template = intent.getStringExtra("Template") ?: "Car"

        getadbyidapi(AdvId, template)

        back_button.setOnClickListener {
            onBackPressed()
        }
        btn_share.setOnClickListener {
            shared("URL OF PRODUCT DETAIL PAGE")
        }

        next_image.setOnClickListener {

        }
        ask_question.setOnClickListener {
            startActivity(Intent(this, QuesAnsFragment::class.java).apply {
                putExtra("AdvId", AdvId)
            })
        }

        all_reviews.setOnClickListener {
            startActivity(Intent(this, ProductReviews::class.java).apply {

            })
        }



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
                            product_image.borderColor=  ContextCompat.getColor(
                                this@ProductDetails,
                                R.color.bg
                            )
                        } else {
                            product_image.borderColor=  ContextCompat.getColor(
                                this@ProductDetails,
                                R.color.white
                            )
                        }

                        
                        val imageURL = ApiConstants.IMAGE_URL + link
                        if (isVideo) {
                            loadThumbnail(
                                this@ProductDetails,
                                link,
                                product_image,loader
                            ) {
                                is_video_iv.isVisible = true
                            }
                        } else {
                            is_video_iv.isVisible = false

                            loadThumbnail(
                                this@ProductDetails,
                                imageURL,
                                product_image,loader
                            )

                        }
                        setOnClickListener {
                           
                            if (isVideo) {
                                startActivity(
                                    Intent(
                                        this@ProductDetails,
                                        PlayActivity::class.java
                                    ).putExtra(
                                        "videourl",
                                        link
                                    )
                                )

                            } else {
                                list.forEach {
                                    it.is_select=false
                                }
                                is_select=true
                                image_rcv.post({ image_rcv.adapter!!.notifyDataSetChanged() })
                                selectLink = imageURL
                                loadThumbnail(
                                    this@ProductDetails,
                                    imageURL,
                                    productimg,loader
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
        product_attribute.adapter = object : GenericListAdapter<Attribute>(
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
                                    this@ProductDetails,
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

    fun getadbyidapi(advid: String, template: String) {
        HelpFunctions.startProgressBar(this)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()
        val call = malqa.getAdDetailById(advid, template,ConstantObjects.logged_userid)
        call.enqueue(object : Callback<JsonObject> {
            @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    val product =
                        Gson().fromJson(response.body().toString(), AdDetailModel::class.java)
                    val jsonObject = response.body()


                    product!!.run {
                        if (!template.isNullOrEmpty()) {
                            val json_string = HelpFunctions.GetTemplatesJson(
                                this@ProductDetails,
                                "$template-${culture()}.js"
                            )

                            if (json_string != null && json_string.trim().length > 0) {
                                val parsed_data = JSONObject(json_string)
                                val controls_array: JSONArray = parsed_data.getJSONArray("data")
                                if (controls_array.length() > 0) {
                                    for (i in 0 until controls_array.length()) {
                                        val IndControl = controls_array.getJSONObject(i)
                                        val id = IndControl.getString("id").lowercase()

                                        if (jsonObject!!.has(id)) {
                                            val key = IndControl.getString("title") ?: ""
                                            if (!jsonObject.get(id).isJsonNull) {
                                                val value = jsonObject.get(id).asString ?: ""
                                                if (value.isNotEmpty()) {
                                                    attributeList.add(Attribute(key, value))

                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }


                        SharedPreferencesStaticClass.ad_userid = product.user!!
                        if (images!!.size > 0) {
                            val imageURL = ApiConstants.IMAGE_URL + images.get(0)
                            selectLink = imageURL
                            loadThumbnail(
                                this@ProductDetails,
                                imageURL,
                                productimg,loader
                            )
//                            productimg.setOnClickListener {
//                                startActivity(
//                                    Intent(
//                                        this@ProductDetails,
//                                        FullImageActivity::class.java
//                                    ).putExtra(
//                                        "imageUri",
//                                        selectLink
//                                    )
//                                )
//                            }

                        }
                        val productImage: ArrayList<ProductImage> = ArrayList()

                        if (!video.isNullOrEmpty()) {
                            productImage.add(ProductImage(video, true))
                        }
                        images.forEachIndexed { index, s ->
                            if(index==0){
                                productImage.add(ProductImage(s, is_select = true))
                            }else{
                                productImage.add(ProductImage(s))
                            }
                        }

                        if (productImage.size > 1) {
                            setCategoryAdaptor(productImage)
                        } else {
                            other_image_layout.isVisible = false
                        }
                        attributeList.add(
                            Attribute(
                                getString(R.string.item_condition),
                                brand_new_item ?: ""
                            )
                        )
                        attributeList.add(Attribute(getString(R.string.quantity), quantity ?: ""))
                        attributeAdaptor(attributeList)
                        ads_id_tv.text = "#$id"
                        product_title_tv.text = producttitle
                        subtitle_tv.text = subtitle
                        description_tv.text = description
                        itemviews_tv.text =
                            getString(R.string.itemView, itemviews.toString().toInt())
                        date.text = HelpFunctions.FormatDateTime(
                            createdOn!!,
                            HelpFunctions.datetimeformat_24hrs_7milliseconds_timezone,
                            HelpFunctions.datetimeformat_mmddyyyy
                        )
                        is_watch_iv. setOnClickListener {
                            if (HelpFunctions.AdAlreadyAddedToWatchList(AdvId)) {
                                HelpFunctions.DeleteAdFromWatchlist(
                                    AdvId,
                                    this@ProductDetails
                                ) {
                                    is_watch_iv.setImageResource(R.drawable.star)
                                    ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.star)
                                }
                            }else{
                                if (ConstantObjects.logged_userid.isEmpty()) {
                                    startActivity(Intent(this@ProductDetails, SignInActivity::class.java).apply {
                                    })
                                } else {

                                    HelpFunctions.InsertAdToWatchlist(
                                        AdvId, 0,
                                        this@ProductDetails
                                    ) {
                                        activeWatch(is_watch_iv)
                                        ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.starcolor)
                                    }

                                }
                            }
                        }
                        if (HelpFunctions.AdAlreadyAddedToWatchList(AdvId)) {
                            activeWatch(is_watch_iv)
                            ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.starcolor)

                        } else {
                            is_watch_iv.setImageResource(R.drawable.star)
                            ConstantObjects.is_watch_iv!!.setImageResource(R.drawable.star)
                        }
                    }

                    val list: ArrayList<AdDetailModel> = ArrayList()
                    list.add(product)
                    list.add(product)
                    list.add(product)
                    list.add(product)
                    list.add(product)
                    list.add(product)

                    var isSellerProductHide=true
                    seller_product_layout.setOnClickListener {
                        if(isSellerProductHide){
                            isSellerProductHide=false
                            seller_product_rcv.isVisible=true
                            isSellerProductHide_iv.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                            seller_product_tv.text=getText(R.string.view_similar_product_from_seller)

                        }else{
                            isSellerProductHide=true
                            seller_product_rcv.isVisible=false
                            isSellerProductHide_iv.setImageResource(R.drawable.down_arrow)
                            seller_product_tv.text=getText(R.string.view_similar_product_from_seller)


                        }
                    }
                    GenericAdaptor().setHomeProductAdaptor(list, similar_products_rcv)
                    GenericAdaptor().setHomeProductAdaptor(list, seller_product_rcv)

                    mainContainer.isVisible = true

                } else {
                    HelpFunctions.ShowAlert(
                        this@ProductDetails,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()

            }



            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                HelpFunctions.ShowLongToast(t.message!!, this@ProductDetails)
                HelpFunctions.dismissProgressBar()

            }
        })

    }

    private fun activeWatch(view:FloatingActionButton) {
        val myFabSrc= ContextCompat.getDrawable(this@ProductDetails, R.drawable.starcolor)
        val willBeWhite = myFabSrc!!.constantState!!.newDrawable()
        willBeWhite.mutate()
            .setColorFilter(ContextCompat.getColor(this@ProductDetails, R.color.bg), PorterDuff.Mode.MULTIPLY)
        view.setImageDrawable(willBeWhite)
    }




}