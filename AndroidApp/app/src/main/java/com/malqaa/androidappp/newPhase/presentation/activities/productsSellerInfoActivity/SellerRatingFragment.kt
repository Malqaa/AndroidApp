//package com.malqaa.androidappp.newPhase.llerDetailsActivity
//
//import android.app.AlertDialog
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.view.View
//import android.widget.Filter
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.RecyclerView
//import com.malqaa.androidappp.R
//import com.malqaa.androidappp.newPhase.utils.hide
//import com.malqaa.androidappp.newPhase.utils.show
//import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
//import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
//import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
//import kotlinx.android.synthetic.main.fragment_seller_rating.*
//import kotlinx.android.synthetic.main.item_seller_review.*
//import kotlinx.android.synthetic.main.item_seller_review.view.review_comment
//import kotlinx.android.synthetic.main.item_seller_review.view.review_name
//import kotlinx.android.synthetic.main.item_seller_review.view.review_profile_pic
//
//import kotlinx.android.synthetic.main.review_dialog_layout.view.*
//import kotlinx.android.synthetic.main.review_filter_design.view.*
//import kotlinx.android.synthetic.main.toolbar_main.*
//
//class seller_ratng : Fragment(R.layout.fragment_seller_rating) {
//    val list: ArrayList<Reviewmodel> = ArrayList()
//    val sampleOption: ArrayList<Selection> = ArrayList()
//    val typeOption: ArrayList<Selection> = ArrayList()
//    var selection: Selection? = null
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initView()
//        setListener()
//
//
//        list.add(Reviewmodel("Ahmed1", "15/12/2022","Very good and fast delivery", "4.7", R.drawable.profile_pic ))
//        list.add(Reviewmodel("Ahmed2", "17/12/2022","Great and fast delivery","4.5", R.drawable.profiledp ))
//        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience","5.0", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car ))
//
//        reviewAdaptor(list)
//
//
//        sampleOption.apply {
//            add(Selection(getString(R.string.all)))
//            add(Selection(getString(R.string.positive)))
//            add(Selection(getString(R.string.negatives)))
//        }
//
//        typeOption.apply {
//            add(Selection(getString(R.string.reviews_as_a_seller)))
//            add(Selection(getString(R.string.reviews_as_a_buyer)))
//        }
//
//
//    }
//
//    private fun initView() {
//        toolbar_title.text = getString(R.string.rates)
//    }
//
//
//    private fun setListener() {
//        back_btn.setOnClickListener {
//            requireActivity().onBackPressed()
//        }
//
//        review_type1.setOnClickListener {
//            val builder = AlertDialog.Builder(requireContext())
//                .create()
//            builder.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
//
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
//                                filter_tv.text=  name
//                                review_filter_layout.setOnClickListener {
//                                    list.forEach {
//                                        it.isSelected = false
//
//                                    }
//                                    list.get(position).isSelected=true
//                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
//                                    selection=element
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
//                    override fun getFilter(): Filter {
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
//            val builder = AlertDialog.Builder(requireContext())
//                .create()
//            builder.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
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
//                                filter_tv.text=  name
//                                review_filter_layout.setOnClickListener {
//                                    list.forEach {
//                                        it.isSelected = false
//                                    }
//                                    element.isSelected = true
//                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
//                                    selection=element
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
//                    override fun getFilter(): Filter {
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
//
//
//    }
//
//
//
//    private fun reviewAdaptor(list: ArrayList<Reviewmodel>) {
//        rvPakat.adapter = object : GenericListAdapter<Reviewmodel>(
//            R.layout.item_seller_review,
//            bind = { element, holder, itemCount, position ->
//                    element.run {
//                        holder.view.review_name.text=(name)
////                        review_date.text=date
////                        review_rating.text=rating
//                        holder.view.review_comment.text=comment
//                        holder.view.review_profile_pic.setImageResource(image)
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
//
//}