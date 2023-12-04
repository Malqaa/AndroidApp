package com.malka.androidappp.newPhase.presentation.sellerDetailsActivity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.EndlessRecyclerViewScrollListener
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.presentation.productsSellerInfoActivity.adapter.SellerRateAdapter
import kotlinx.android.synthetic.main.activity_seller_rate.swipe_to_refresh
import kotlinx.android.synthetic.main.activity_seller_rate.tvError
import kotlinx.android.synthetic.main.fragment_seller_rating.bottom_btns
import kotlinx.android.synthetic.main.fragment_seller_rating.review_type1
import kotlinx.android.synthetic.main.fragment_seller_rating.review_type2
import kotlinx.android.synthetic.main.fragment_seller_rating.rvPakat
import kotlinx.android.synthetic.main.review_dialog_layout.view.filter_application
import kotlinx.android.synthetic.main.review_dialog_layout.view.reset_tv
import kotlinx.android.synthetic.main.review_dialog_layout.view.review_btn
import kotlinx.android.synthetic.main.review_dialog_layout.view.review_type1_rcv
import kotlinx.android.synthetic.main.review_dialog_layout.view.review_type_btn
import kotlinx.android.synthetic.main.review_dialog_layout.view.reviews_type2_rcv
import kotlinx.android.synthetic.main.review_filter_design.view.checkbox
import kotlinx.android.synthetic.main.review_filter_design.view.filter_tv
import kotlinx.android.synthetic.main.review_filter_design.view.review_filter_layout
import kotlinx.android.synthetic.main.toolbar_main.back_btn
import kotlinx.android.synthetic.main.toolbar_main.toolbar_title

class SellerRatingFragment : Fragment(R.layout.fragment_seller_rating),
    SwipeRefreshLayout.OnRefreshListener {
    private val sampleOption: ArrayList<Selection> = ArrayList()
    private val typeOption: ArrayList<Selection> = ArrayList()
    private var selection: Selection? = null
    private var sellerRateAdapter: SellerRateAdapter? = null
    private var sellerRateList: ArrayList<SellerRateItem>? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var sellerRatingViewModel: SellerRatingViewModel? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sellerRatingViewModel = ViewModelProvider(this).get(SellerRatingViewModel::class.java)
        initView()
        setListener()

        setSellerRateAdapter()
        onRefresh()
        sampleOption.apply {
            add(Selection(getString(R.string.all), id = 4))
            add(Selection(getString(R.string.positive), id = 3))
            add(Selection(getString(R.string.negatives), id = 1))
            add(Selection(getString(R.string.neutrals), id = 2))

        }

        typeOption.apply {
            add(Selection(getString(R.string.reviews_as_a_seller)))
            add(Selection(getString(R.string.reviews_as_a_buyer)))
        }

        sellerRatingViewModel?.sellerRateListObservable?.observe(this) { sellerRateListResp ->
            if (sellerRateListResp.status_code == 200) {
                sellerRateListResp.SellerRateObject?.let {
                    it.rateSellerListDto?.let { it1 -> sellerRateList?.addAll(it1) }
                }
                sellerRateAdapter?.notifyDataSetChanged()
                if (sellerRateList?.isEmpty() == true) {
                    tvError.show()
                } else {
                    tvError.hide()
                }
            } else {
                if (sellerRateList?.isEmpty() == true) {
                    if (sellerRateListResp.message != null) {
                        HelpFunctions.ShowLongToast((sellerRateListResp.message), requireContext())
                    } else {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.serverError),
                            requireContext()
                        )
                    }
                } else {
                    tvError.hide()
                }
            }
            //tvError
        }

    }

    private fun setSellerRateAdapter() {

        sellerRateList = ArrayList()
        sellerRateAdapter = SellerRateAdapter(requireContext(), sellerRateList ?: arrayListOf())
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvPakat.apply {
            adapter = sellerRateAdapter
            layoutManager = linearLayoutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager!!) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    sellerRatingViewModel?.getSellerRates(
                        page, null
                    )


                }
            }
        rvPakat.addOnScrollListener(endlessRecyclerViewScrollListener!!)
    }

    override fun onRefresh() {
        endlessRecyclerViewScrollListener?.resetState()
        swipe_to_refresh.isRefreshing = false
        sellerRateList?.clear()
        sellerRateAdapter?.notifyDataSetChanged()
        tvError.hide()
        sellerRatingViewModel?.getSellerRates(
            1, null)


    }

    private fun initView() {
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        toolbar_title.text = getString(R.string.rates)
    }


    private fun setListener() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        review_type1.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .create()
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val view = layoutInflater.inflate(R.layout.review_dialog_layout, null)
            builder.setView(view)
            bottom_btns.hide()


            fun reviewAdaptor(list: List<Selection>, rcv: RecyclerView) {
                rcv.adapter = object : GenericListAdapter<Selection>(
                    R.layout.review_filter_design,
                    bind = { element, holder, itemCount, position ->
                        holder.view.run {
                            element.run {
                                checkbox.isChecked = isSelected
                                review_filter_layout.isSelected = isSelected
                                filter_tv.text = name
                                review_filter_layout.setOnClickListener {
                                    list.forEach {
                                        it.isSelected = false

                                    }
                                    list[position].isSelected = true
                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                    selection = element
                                }

                                checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                                    if (isChecked) {
                                        review_filter_layout.performClick()
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

            builder.setCanceledOnTouchOutside(true)
            builder.show()
            builder.setOnCancelListener {
                bottom_btns.show()
            }
            view.filter_application.setOnClickListener {
                sellerRateList?.clear()
                if (typeOption[1].isSelected) {
                    val obj = sampleOption.find { it.isSelected }!!
                    if (obj.id == 4) {
                        sellerRatingViewModel?.getBuyerRates(1, rate = null)
                    } else
                        sellerRatingViewModel?.getBuyerRates(1, obj.id)

                } else {

                    val obj = sampleOption.find { it.isSelected }!!
                    if (obj.id == 4) {
                        sellerRatingViewModel?.getSellerRates(1, rate = null)
                    } else
                        sellerRatingViewModel?.getSellerRates(1, obj.id)



                }

                builder.dismiss()
                bottom_btns.show()
            }
            view.reset_tv.setOnClickListener {
                builder.dismiss()
                bottom_btns.show()
            }

            view.review_type_btn.setOnClickListener {
                builder.dismiss()
                review_type2.performClick()
            }
            reviewAdaptor(sampleOption, view.review_type1_rcv)

        }

        review_type2.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .create()
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val view = layoutInflater.inflate(R.layout.review_dialog_layout, null)
            builder.setView(view)
            bottom_btns.hide()

            fun reviewAdaptor(list: List<Selection>, rcv: RecyclerView) {
                rcv.adapter = object : GenericListAdapter<Selection>(
                    R.layout.review_filter_design,
                    bind = { element, holder, itemCount, position ->
                        holder.view.run {
                            element.run {
                                checkbox.isChecked = isSelected
                                filter_tv.isSelected = isSelected
                                review_filter_layout.isSelected = isSelected
                                filter_tv.text = name
                                review_filter_layout.setOnClickListener {
                                    list.forEach {
                                        it.isSelected = false
                                    }
                                    element.isSelected = true
                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                    selection = element
                                }

                                checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                                    if (isChecked) {
                                        review_filter_layout.performClick()
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

            builder.setCanceledOnTouchOutside(true)
            builder.show()
            builder.setOnCancelListener {
                bottom_btns.show()
            }

            view.filter_application.setOnClickListener {
                sellerRateList?.clear()
                if (typeOption[0].isSelected) {
                    sellerRatingViewModel?.getSellerRates(1, null)
                } else if (typeOption[1].isSelected) {
                    sellerRatingViewModel?.getBuyerRates(1, null)
                }
                builder.dismiss()
                bottom_btns.show()
            }
            view.reset_tv.setOnClickListener {
                builder.dismiss()
                bottom_btns.show()
            }
            view.review_btn.setOnClickListener {
                builder.dismiss()
                review_type1.performClick()
            }

            reviewAdaptor(typeOption, view.reviews_type2_rcv)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        selection = null
        sellerRateAdapter = null
        sellerRateList = null
        sellerRatingViewModel = null
        linearLayoutManager = null
        endlessRecyclerViewScrollListener = null
        sellerRatingViewModel?.closeAllCall()

    }

//    private fun reviewAdaptor(list: ArrayList<Reviewmodel>) {
//        rvPakat.adapter = object : GenericListAdapter<Reviewmodel>(
//            R.layout.item_seller_review,
//            bind = { element, holder, itemCount, position ->
//                element.run {
//                    holder.view.review_name.text = (name)
////                        review_date.text=date
////                        review_rating.text=rating
//                    holder.view.review_comment.text = comment
//                    holder.view.review_profile_pic.setImageResource(image)
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

}