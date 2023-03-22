package com.malka.androidappp.newPhase.data.helper

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
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity
import com.malka.androidappp.fragments.account_fragment.address.AddAddress
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GetAddressResponse
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.Question
import kotlinx.android.synthetic.main.add_address_design.view.*
import kotlinx.android.synthetic.main.item_question_answer_design.view.*
import kotlinx.android.synthetic.main.product_item.view.*


class GenericAdaptor {

    fun setHomeProductAdaptor(list: List<Product>, product_rcv: RecyclerView) {
        product_rcv.adapter = object : GenericListAdapter<Product>(
            R.layout.product_item, bind = { element, holder, itemCount, position ->
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

        }.apply { submitList(list)
        }
    }
    fun productAdaptor(
        element: Product?, context: Context, holder: BaseViewHolder, isGrid: Boolean
    ) {
        holder.view.run {
            element!!.run {
                ivFav.setOnClickListener {
                    if (HelpFunctions.AdAlreadyAddedToWatchList(id.toString())) {
                        HelpFunctions.DeleteAdFromWatchlist(
                            id.toString(),
                            context
                        ) {
                            ivFav.setImageResource(R.drawable.star)
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
                                id.toString(),0,
                                context
                            ) {
                                ivFav.setImageResource(R.drawable.starcolor)

                            }

                        }
                    }
                }
                if (HelpFunctions.AdAlreadyAddedToWatchList(id .toString())) {
                    ivFav.setImageResource(R.drawable.starcolor)
                } else {
                    ivFav.setImageResource(R.drawable.star)
                }

                titlenamee.text = name
                city_tv.text = ""


                setOnClickListener {
                    SharedPreferencesStaticClass.ad_userid = ""
                    ConstantObjects.is_watch_iv = ivFav
                    context.startActivity(Intent(context, ProductDetailsActivity::class.java).apply {
                        putExtra("AdvId", id)
                        putExtra("Template", "")
                    })
                }
//                Extension.loadThumbnail(
//                    context,
//                    image,
//                    productimg, loader
//                )

                // date_tv.text = date
                val listingTypeFormated="1"
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
//                        LowestPrice_layout.show()
//                        LowestPrice_layout_2.show()
//
//                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
//                            context.getString(
//                                R.string.Rayal
//                            )
//                        }"
//                        purchasing_price_tv_2.text =
//                            "${
//                                startingPrice.toDouble().decimalNumberFormat()
//                            } ${context.getString(R.string.Rayal)}"
//
//
//                        product_price.text = "${startingPrice!!.toDouble().decimalNumberFormat()} ${
//                            context.getString(
//                                R.string.Rayal
//                            )
//                        }"
//
//                        LowestPrice.text = "${
//                            reservePrice!!.toDouble().decimalNumberFormat()
//                        } ${context.getString(R.string.Rayal)}"
//                        LowestPrice_2.text =
//                            "${
//                                reservePrice.toDouble().decimalNumberFormat()
//                            } ${context.getString(R.string.Rayal)}"
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
//                    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
//                    if (endTime.equals("") || endTime == null) {
//                        time_bar.visibility = View.GONE
//                    } else {
//                        time_bar.visibility = View.VISIBLE
//                        val endDate = format.parse("$endTime")
//
//
//                       val currentDate = format.parse(format.format(Date()))
//
//                        if (endDate.before(currentDate)) {
//                            days_tv.text = "0"
//                            hours_tv.text = "0"
//                            minutes_tv.text = "0"
//                        } else {
//                            var diff: Long = endDate!!.getTime() - currentDate!!.getTime()
//                            val secondsInMilli: Long = 1000
//                            val minutesInMilli = secondsInMilli * 60
//                            val hoursInMilli = minutesInMilli * 60
//                            val daysInMilli = hoursInMilli * 24
//
//                            val elapsedDays = diff / daysInMilli
//                            diff %= daysInMilli
//
//                            val elapsedHours = diff / hoursInMilli
//                            diff %= hoursInMilli
//
//                            val elapsedMinutes = diff / minutesInMilli
//                            diff %= minutesInMilli
//
//
//                            days_tv.text = elapsedDays.toString()
//                            hours_tv.text = elapsedHours.toString()
//                            minutes_tv.text = elapsedMinutes.toString()
//                        }
//
//                    }
                } catch (error: Exception) {
                    containerTimeBar.visibility = View.GONE
                }
            }
        }
    }

    fun questionAnswerAdaptor(quest_ans_rcv: RecyclerView, list: List<Question>) {

        quest_ans_rcv.adapter = object : GenericListAdapter<Question>(
            R.layout.item_question_answer_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        tvQuestion.text = question
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