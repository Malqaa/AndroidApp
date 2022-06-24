package com.malka.androidappp.botmnav_fragments.Follow_Up

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.Selection
import kotlinx.android.synthetic.main.favorite_sub_category_layout.view.*
import kotlinx.android.synthetic.main.fragment_favorite_sub_category.*
import kotlinx.android.synthetic.main.toolbar_main.*

class favorite_sub_category : Fragment(R.layout.fragment_favorite_sub_category) {
    val subCategory: ArrayList<Selection> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()


        subCategory.apply {
            add(Selection("Hyundai - Cars"))
            add(Selection("Honda - Cars" ))
            add(Selection("Toyota - Cars" ))
            add(Selection("Suzuki - Cars" ))
            add(Selection("Honda - Cars" ))
            add(Selection("Toyota - Cars" ))
            add(Selection("Suzuki - Cars" ))
            add(Selection("Hyundai - Cars"))

        }

        subCategoryAdapter(subCategory)

    }

    private fun initView() {
        toolbar_title.text = getString(R.string.favorite_subcategories)
    }


    private fun setListenser() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    private fun subCategoryAdapter(list: ArrayList<Selection>) {
        all_sub_category_rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.favorite_sub_category_layout,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        sub_category_tv.text = name
                        if (subCategory.get(position).isSelected){


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