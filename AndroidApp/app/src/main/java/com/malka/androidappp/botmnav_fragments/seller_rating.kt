package com.malka.androidappp.botmnav_fragments

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.Reviewmodel
import kotlinx.android.synthetic.main.product_review_design.view.*
import kotlinx.android.synthetic.main.product_reviews1.*
import kotlinx.android.synthetic.main.toolbar_main.*

class seller_rating : Fragment(R.layout.fragment_seller_rating) {
    val list: ArrayList<Reviewmodel> = ArrayList()

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

    }

    private fun initView() {
        toolbar_title.text = getString(R.string.all_reviews)
    }


    private fun setListenser() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
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