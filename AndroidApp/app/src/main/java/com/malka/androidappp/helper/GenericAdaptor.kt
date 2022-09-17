package com.malka.androidappp.helper

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.activities_main.product_detail.ProductDetails
import com.malka.androidappp.fragments.account_fragment.address.AddAddress
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.Extension.decimalNumberFormat
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.constants.ApiConstants
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.GetAddressResponse
import com.malka.androidappp.servicemodels.questionModel.Question
import kotlinx.android.synthetic.main.add_address_design.view.*
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.android.synthetic.main.question_answer_design.view.*
import java.text.SimpleDateFormat
import java.util.*

class GenericAdaptor {


    fun productAdaptor(
        element: AdDetailModel?, context: Context, holder: BaseViewHolder, isGrid: Boolean
    ) {

        holder.view.run {
            element!!.run {

                is_watch_iv.setOnClickListener {
                    if (HelpFunctions.AdAlreadyAddedToWatchList(referenceId)) {
                        HelpFunctions.DeleteAdFromWatchlist(
                            referenceId!!,
                            context
                        ) {
                            is_watch_iv.setImageResource(R.drawable.star)
                        }
                    } else {
                        if (ConstantObjects.logged_userid.isEmpty()) {
                            context.startActivity(
                                Intent(
                                    context,
                                    SignInActivity::class.java
                                ).apply {
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
                if (HelpFunctions.AdAlreadyAddedToWatchList(referenceId ?: id ?: "")) {
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
                        putExtra("AdvId", referenceId ?: id ?: "")
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
                        val imageURL = ApiConstants.IMAGE_URL + image
                        Extension.loadThumbnail(
                            context,
                            imageURL,
                            productimg, loader
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

                date_tv.text = createdOnFormated

                when (listingTypeFormated) {
                    "1" -> {
                        LowestPrice_layout.invisible()
                        LowestPrice_layout_2.hide()
                        product_price.text = "${price!!.toDouble().decimalNumberFormat()} ${
                            context.getString(
                                R.string.Rayal
                            )
                        }"
                        purchasing_price_tv_2.text =
                            "${
                                price.toDouble().decimalNumberFormat()
                            } ${context.getString(R.string.Rayal)}"
                    }
                    "12", "2" -> {
                        LowestPrice_layout.show()
                        LowestPrice_layout_2.show()

                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
                            context.getString(
                                R.string.Rayal
                            )
                        }"
                        purchasing_price_tv_2.text =
                            "${
                                startingPrice.toDouble().decimalNumberFormat()
                            } ${context.getString(R.string.Rayal)}"


                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
                            context.getString(
                                R.string.Rayal
                            )
                        }"

                        LowestPrice.text = "${
                            reservePrice!!.toDouble().decimalNumberFormat()
                        } ${context.getString(R.string.Rayal)}"
                        LowestPrice_2.text =
                            "${
                                reservePrice.toDouble().decimalNumberFormat()
                            } ${context.getString(R.string.Rayal)}"
                    }
                }


                if (isGrid) {
                    lisView.hide()
                    gridview.show()
                } else {
                    lisView.show()
                    gridview.hide()
                }

                try {
                    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                    if (endTime.equals("") || endTime == null) {
                        time_bar.visibility = View.GONE
                    } else {
                        time_bar.visibility = View.VISIBLE
                        val endDate = format.parse("$endTime")


                       val currentDate = format.parse(format.format(Date()))

                        if (endDate.before(currentDate)) {
                            days_tv.text = "0"
                            hours_tv.text = "0"
                            minutes_tv.text = "0"
                        } else {
                            var diff: Long = endDate!!.getTime() - currentDate!!.getTime()
                            val secondsInMilli: Long = 1000
                            val minutesInMilli = secondsInMilli * 60
                            val hoursInMilli = minutesInMilli * 60
                            val daysInMilli = hoursInMilli * 24

                            val elapsedDays = diff / daysInMilli
                            diff %= daysInMilli

                            val elapsedHours = diff / hoursInMilli
                            diff %= hoursInMilli

                            val elapsedMinutes = diff / minutesInMilli
                            diff %= minutesInMilli


                            days_tv.text = elapsedDays.toString()
                            hours_tv.text = elapsedHours.toString()
                            minutes_tv.text = elapsedMinutes.toString()
                        }

                    }
                } catch (error: Exception) {
                    time_bar.visibility = View.GONE
                }
            }
        }
    }


    fun setHomeProductAdaptor(list: List<AdDetailModel>, product_rcv: RecyclerView) {
        product_rcv.adapter = object : GenericListAdapter<AdDetailModel>(
            R.layout.product_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {

                    val params: ViewGroup.LayoutParams = fullview.layoutParams
                    params.width = resources.getDimension(R.dimen._220sdp).toInt()
                    params.height = params.height
                    fullview.layoutParams = params
                    productAdaptor(list.get(position), context, holder, true)
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


    fun questionAnswerAdaptor(quest_ans_rcv: RecyclerView, list: List<Question>) {

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

    fun AdressAdaptor(
        addAddressLaucher: ActivityResultLauncher<Intent>,
        context: Context,
        category_rcv: RecyclerView,
        list: List<GetAddressResponse.AddressModel>,
        type: String, onItemClick:
            (address: GetAddressResponse.AddressModel) -> Unit
    ) {
        category_rcv.adapter = object : GenericListAdapter<GetAddressResponse.AddressModel>(
            R.layout.add_address_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        when (type) {
                            ConstantObjects.View -> {
                                is_select_.isVisible = false
                            }

                            ConstantObjects.Select -> {
                                is_select_.isVisible = true
                                is_select_.isChecked = is_select
                                is_select_.setOnCheckedChangeListener { buttonView, isChecked ->

                                    if (isChecked) {
                                        list.forEach {
                                            it.is_select = false
                                        }
                                        list.get(position).is_select = true
                                        onItemClick.invoke(element)
                                        category_rcv.post { category_rcv.adapter?.notifyDataSetChanged() }
                                    }


                                }
                            }
                        }
                        address_name.text = "Delivery to my current address"
                        country_name.text = "$country - $region -$city"
                        phonenum.text = mobileNo
                        address_tv.text = address

                        edit_address_btn.setOnClickListener {

                            addAddressLaucher.launch(Intent(context, AddAddress::class.java).apply {
                                putExtra("isEdit", true)
                                putExtra("addressObject", Gson().toJson(element))
                            })
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