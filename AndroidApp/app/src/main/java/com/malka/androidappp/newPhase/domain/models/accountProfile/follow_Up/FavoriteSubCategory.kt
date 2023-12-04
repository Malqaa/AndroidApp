package com.malka.androidappp.fragments.follow_Up

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import kotlinx.android.synthetic.main.favorite_sub_category_layout.view.*
import kotlinx.android.synthetic.main.fragment_favorite_sub_category.*
import kotlinx.android.synthetic.main.toolbar_main.*

class FavoriteSubCategory : Fragment(R.layout.fragment_favorite_sub_category) {
    val subCategory: ArrayList<Selection> = ArrayList()
//    lateinit var onItemClick: (position:Int) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()
        FavoriteSubCategoryArgs.fromBundle(requireArguments()).categoryId

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
        toolbar_title.visibility = View.GONE
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
                        category_name_tv.text = name
                        if (subCategory[position].isSelected) {

                            bgline.show()
                            is_selectimage.show()
                            category_icon.borderColor = ContextCompat.getColor(
                                context,
                                R.color.bg
                            )

                        } else {
                            bgline.hide()
                            is_selectimage.hide()
                            category_icon.borderColor = ContextCompat.getColor(
                                context,
                                R.color.white
                            )
                        }

                        holder.itemView.setOnClickListener {

                            subCategory[position].isSelected =
                                !subCategory[position].isSelected
                            all_sub_category_rcv.adapter?.notifyDataSetChanged()
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