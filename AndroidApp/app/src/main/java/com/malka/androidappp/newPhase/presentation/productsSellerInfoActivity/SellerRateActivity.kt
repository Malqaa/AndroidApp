package com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.adapter.SellerRateAdapter
import kotlinx.android.synthetic.main.activity_seller_rate.*
import kotlinx.android.synthetic.main.toolbar_main.*

class SellerRateActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    val list: ArrayList<Reviewmodel> = ArrayList()
    val sampleOption: ArrayList<Selection> = ArrayList()
    val typeOption: ArrayList<Selection> = ArrayList()
    var selection: Selection? = null

    //=====
    lateinit var sellerRateAdapter: SellerRateAdapter
    lateinit var sellerRateList: ArrayList<SellerRateItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_rate)
        initView()
        setListenser()
        setSellerRateAdapter()

//        list.add(
//            Reviewmodel(
//                "Ahmed1",
//                "15/12/2022",
//                "Very good and fast delivery",
//                "4.7",
//                R.drawable.profile_pic
//            )
//        )
//        list.add(
//            Reviewmodel(
//                "Ahmed2",
//                "17/12/2022",
//                "Great and fast delivery",
//                "4.5",
//                R.drawable.profiledp
//            )
//        )
//        list.add(
//            Reviewmodel(
//                "Ahmed3",
//                "12/12/2022",
//                "Good and fast delivery",
//                "4.9",
//                R.drawable.car
//            )
//        )
//        list.add(Reviewmodel("Ahmed4", "16/12/2022", "Great Experience", "5.0", R.drawable.car))
//        list.add(
//            Reviewmodel(
//                "Ahmed5",
//                "10/12/2022",
//                "Excelent fast delivery",
//                "4.6",
//                R.drawable.car
//            )
//        )
//        list.add(
//            Reviewmodel(
//                "Ahmed6",
//                "5/12/2022",
//                "Amazing and fast delivery",
//                "4.9",
//                R.drawable.car
//            )
//        )
//        list.add(
//            Reviewmodel(
//                "Ahmed3",
//                "12/12/2022",
//                "Good and fast delivery",
//                "4.9",
//                R.drawable.car
//            )
//        )
//        list.add(Reviewmodel("Ahmed4", "16/12/2022", "Great Experience ", "5.0", R.drawable.car))
//        list.add(
//            Reviewmodel(
//                "Ahmed5",
//                "10/12/2022",
//                "Excelent fast delivery",
//                "4.6",
//                R.drawable.car
//            )
//        )
//        list.add(
//            Reviewmodel(
//                "Ahmed6",
//                "5/12/2022",
//                "Amazing and fast delivery",
//                "4.9",
//                R.drawable.car
//            )
//        )
//        list.add(
//            Reviewmodel(
//                "Ahmed3",
//                "12/12/2022",
//                "Good and fast delivery",
//                "4.9",
//                R.drawable.car
//            )
//        )
//        list.add(Reviewmodel("Ahmed4", "16/12/2022", "Great Experience ", "5.0", R.drawable.car))
//
//        reviewAdaptor(list)
//
//
//        sampleOption.apply {
//            add(Selection("option 1"))
//            add(Selection("option 2"))
//            add(Selection("option 3"))
//        }
//
//        typeOption.apply {
//            add(Selection("Option 1"))
//            add(Selection("Option 2"))
//
//        }

    }

    private fun initView() {
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        toolbar_title.text = getString(R.string.all_reviews)
    }

    private fun setSellerRateAdapter() {
        sellerRateList = ArrayList()
        sellerRateAdapter = SellerRateAdapter(this, sellerRateList);
        rvRate.apply {
            adapter = sellerRateAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }


    private fun setListenser() {
        back_btn.setOnClickListener {
            onBackPressed()
        }

//        review_type1.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//                .create()
//            val view = layoutInflater.inflate(R.layout.review_dialog_layout, null)
//            builder.setView(view)
//            bottom_btns.hide()
//
//
//            fun reviewAdaptor(list: List<Selection>, rcv: RecyclerView) {
//                rcv.adapter = object : GenericListAdapter<Selection>(
//                    R.layout.review_filter_design,
//                    bind = { element, holder, itemCount, position ->
//                        holder.view.run {
//                            element.run {
//                                checkbox.isChecked = isSelected
//                                review_filter_layout.setSelected(isSelected)
//                                filter_tv.text = name
//                                review_filter_layout.setOnClickListener {
//                                    list.forEach {
//                                        it.isSelected = false
//
//                                    }
//                                    list.get(position).isSelected = true
//                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
//                                    selection = element
//                                }
//
//                                checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//                                    if (isChecked) {
//                                        review_filter_layout.performClick()
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                ) {
//                    override fun getFilter(): android.widget.Filter? {
//                        TODO("Not yet implemented")
//                    }
//
//                }.apply {
//                    submitList(
//                        list
//                    )
//                }
//            }
//
//            builder.setCanceledOnTouchOutside(true)
//            builder.show()
//            builder.setOnCancelListener {
//                bottom_btns.show()
//            }
//            view.filter_application.setOnClickListener {
//                builder.dismiss()
//                bottom_btns.show()
//            }
//            view.reset_tv.setOnClickListener {
//                builder.dismiss()
//                bottom_btns.show()
//            }
//
//            view.review_type_btn.setOnClickListener {
//                builder.dismiss()
//                review_type2.performClick()
//            }
//            reviewAdaptor(sampleOption, view.review_type1_rcv)
//
//        }
//
//        review_type2.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//                .create()
//            val view = layoutInflater.inflate(R.layout.review_dialog_layout, null)
//            builder.setView(view)
//            bottom_btns.hide()
//
//            fun reviewAdaptor(list: List<Selection>, rcv: RecyclerView) {
//                rcv.adapter = object : GenericListAdapter<Selection>(
//                    R.layout.review_filter_design,
//                    bind = { element, holder, itemCount, position ->
//                        holder.view.run {
//                            element.run {
//                                checkbox.isChecked = isSelected
//                                filter_tv.setSelected(isSelected)
//                                review_filter_layout.setSelected(isSelected)
//                                filter_tv.text = name
//                                review_filter_layout.setOnClickListener {
//                                    list.forEach {
//                                        it.isSelected = false
//                                    }
//                                    element.isSelected = true
//                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
//                                    selection = element
//                                }
//
//                                checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
//                                    if (isChecked) {
//                                        review_filter_layout.performClick()
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                ) {
//                    override fun getFilter(): android.widget.Filter? {
//                        TODO("Not yet implemented")
//                    }
//
//                }.apply {
//                    submitList(
//                        list
//                    )
//                }
//            }
//
//            builder.setCanceledOnTouchOutside(true)
//            builder.show()
//            builder.setOnCancelListener {
//                bottom_btns.show()
//            }
//
//            view.filter_application.setOnClickListener {
//                builder.dismiss()
//                bottom_btns.show()
//            }
//            view.reset_tv.setOnClickListener {
//                builder.dismiss()
//                bottom_btns.show()
//            }
//            view.review_btn.setOnClickListener {
//                builder.dismiss()
//                review_type1.performClick()
//            }
//
//            reviewAdaptor(typeOption, view.reviews_type2_rcv)
//        }


    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        sellerRateList.clear()
        sellerRateAdapter.notifyDataSetChanged()

    }


//    private fun reviewAdaptor(list: ArrayList<Reviewmodel>) {
//        rvPakat.adapter = object : GenericListAdapter<Reviewmodel>(
//            R.layout.item_seller_review,
//            bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    element.run {
////                        review_name.text = name
////                        review_date.text = date
////                        review_rating.text = rating
////                        review_comment.text = comment
//                      //  review_profile_pic.setImageResource(image)
//
//                    }
//                }
//            }
//        ) {
//            override fun getFilter(): android.widget.Filter? {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(
//                list
//            )
//        }
//    }

}