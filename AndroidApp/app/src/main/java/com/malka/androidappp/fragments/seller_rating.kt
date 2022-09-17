package com.malka.androidappp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.Reviewmodel
import com.malka.androidappp.servicemodels.Selection
import kotlinx.android.synthetic.main.fragment_seller_rating.*
import kotlinx.android.synthetic.main.product_review_design.view.*
import kotlinx.android.synthetic.main.product_reviews1.category_rcv
import kotlinx.android.synthetic.main.review_dialog_layout.view.*
import kotlinx.android.synthetic.main.review_filter_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class seller_rating : Fragment(R.layout.fragment_seller_rating) {
    val list: ArrayList<Reviewmodel> = ArrayList()
    val sampleOption: ArrayList<Selection> = ArrayList()
    val typeOption: ArrayList<Selection> = ArrayList()
    var selection: Selection? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()


        list.add(Reviewmodel("Ahmed1", "15/12/2022","Very good and fast delivery", "4.7", R.drawable.profile_pic ))
        list.add(Reviewmodel("Ahmed2", "17/12/2022","Great and fast delivery","4.5", R.drawable.profiledp ))
        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience","5.0", R.drawable.car ))
        list.add(Reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car ))
        list.add(Reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car ))
        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car ))
        list.add(Reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car ))
        list.add(Reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car ))
        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car ))

        reviewAdaptor(list)


        sampleOption.apply {
            add(Selection("option 1"))
            add(Selection("option 2"))
            add(Selection("option 3"))
        }

        typeOption.apply {
            add(Selection("Option 1"))
            add(Selection("Option 2"))

        }


    }

    private fun initView() {
        toolbar_title.text = getString(R.string.all_reviews)
    }


    private fun setListenser() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        review_type1.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .create()
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
                                review_filter_layout.setSelected(isSelected)
                                filter_tv.text=  name
                                review_filter_layout.setOnClickListener {
                                    list.forEach {
                                        it.isSelected = false

                                    }
                                    list.get(position).isSelected=true
                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                    selection=element
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
                                filter_tv.setSelected(isSelected)
                                review_filter_layout.setSelected(isSelected)
                                filter_tv.text=  name
                                review_filter_layout.setOnClickListener {
                                    list.forEach {
                                        it.isSelected = false
                                    }
                                    element.isSelected = true
                                    rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                    selection=element
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



    private fun reviewAdaptor(list: ArrayList<Reviewmodel>) {
        category_rcv.adapter = object : GenericListAdapter<Reviewmodel>(
            R.layout.product_review_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        review_name.text=name
                        review_date.text=date
                        review_rating.text=rating
                        review_comment.text=comment
                        review_profile_pic.setImageResource(image)

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