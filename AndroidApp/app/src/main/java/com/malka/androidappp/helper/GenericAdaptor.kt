package com.malka.androidappp.helper

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.product_detail.ProductDetails
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.Question
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.Extension.decimalNumberFormat
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.android.synthetic.main.question_answer_design.view.*

class GenericAdaptor {

    fun productAdaptor(
        element: AdDetailModel?, context: Context, holder: BaseViewHolder, isGrid: Boolean
    ) {

        holder.view.run {
            element!!.run {

                is_watch_iv. setOnClickListener {
                    if (HelpFunctions.AdAlreadyAddedToWatchList(referenceId)) {
                        HelpFunctions.DeleteAdFromWatchlist(
                            referenceId!!,
                            context
                        ) {
                            is_watch_iv.setImageResource(R.drawable.star)
                        }
                    }else{
                        if (ConstantObjects.logged_userid.isEmpty()) {
                            context. startActivity(Intent(context, SignInActivity::class.java).apply {
                            })
                        } else {

                            HelpFunctions.InsertAdToWatchlist(
                                referenceId!!, 0,
                                context
                            ) {
                                is_watch_iv.setImageResource(R.drawable.starcolor)

                            }

                        }
                    }
                }
                if (HelpFunctions.AdAlreadyAddedToWatchList(referenceId?:id?:"")) {
                    is_watch_iv.setImageResource(R.drawable.starcolor)
                } else {
                    is_watch_iv.setImageResource(R.drawable.star)
                }

                titlenamee.text = title
                city_tv.text = city


                setOnClickListener {
                    SharedPreferencesStaticClass.ad_userid = user!!
                    ConstantObjects.is_watch_iv = is_watch_iv
                    context.startActivity(Intent(context, ProductDetails::class.java).apply {
                        putExtra("AdvId", referenceId?:id?:"")
                        putExtra("Template", template)
                    })
                }
                if (homepageImage.isNullOrEmpty()) {
                    if (!images.isNullOrEmpty()) {
                        val imageURL = ApiConstants.IMAGE_URL + images.get(0)

                        Extension.loadThumbnail(
                            context,
                            imageURL,
                            productimg, loader
                        )
                    } else {
                        productimg.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.gray_light
                            )
                        )

                    }
                } else {
                    val imageURL = ApiConstants.IMAGE_URL + homepageImage
                    Extension.loadThumbnail(
                        context,
                        imageURL,
                        productimg, loader
                    )
                }

                date_tv.text =createdOnFormated
                product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
                    context.getString(
                        R.string.Rayal
                    )
                }"
                LowestPrice.text = "${
                    price!!.toDouble().decimalNumberFormat()
                } ${context.getString(R.string.Rayal)}"


                purchasing_price_tv_2.text =
                    "${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
                LowestPrice_2.text =
                    "${price.toDouble().decimalNumberFormat()} ${context.getString(R.string.Rayal)}"
                if (isGrid) {
                    lisView.hide()
                    gridview.show()
                } else {
                    lisView.show()
                    gridview.hide()
                }
            }
        }
    }



    fun setHomeProductAdaptor(list: List<AdDetailModel>,product_rcv: RecyclerView) {
        product_rcv.adapter = object : GenericListAdapter<AdDetailModel>(
            R.layout.product_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {

                    val params: ViewGroup.LayoutParams = fullview.layoutParams
                    params.width = resources.getDimension(R.dimen._220sdp).toInt()
                    params.height = params.height
                    fullview.layoutParams = params
                    productAdaptor(list.get(position), context, holder,true)
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


    fun questionAnswerAdaptor(quest_ans_rcv:RecyclerView,list: List<Question>) {

        quest_ans_rcv.adapter = object : GenericListAdapter<Question>(
            R.layout.question_answer_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        question_tv.text = question
                        question_time.text = dateTimeFormated
                        if (isAnswered) {
                            question_response_yet.hide()
                            answer_view.show()
                            answer_tv.text = answer.description
                            answer_time.text = answer.dateTimeFormated
                        } else {
                            question_response_yet.show()
                            answer_view.hide()
                            answer_time.hide()
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

}