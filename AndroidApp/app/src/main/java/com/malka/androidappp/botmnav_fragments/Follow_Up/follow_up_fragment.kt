package com.malka.androidappp.botmnav_fragments.Follow_Up

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.Selection
import kotlinx.android.synthetic.main.favorite_categories_design.view.*
import kotlinx.android.synthetic.main.favorite_search_design.view.*
import kotlinx.android.synthetic.main.favorite_seller_design.view.*
import kotlinx.android.synthetic.main.fragment_follow_up_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*


class follow_up_fragment : Fragment(R.layout.fragment_follow_up_fragment) {
    val favoriteCategory: ArrayList<Selection> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()

        favoriteCategory.apply {
            add(Selection("Hyundai - Cars"))
            add(Selection("Honda - Cars" ))
            add(Selection("Toyota - Cars" ))
            add(Selection("Suzuki - Cars" ))
        }

        favoriteSellerAdapter(favoriteCategory)


    }

    private fun initView() {
        toolbar_title.text = getString(R.string.follow_up)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        add_new_category.setOnClickListener() {
            findNavController().navigate(R.id.newCategory)
        }

        fav_category.setOnClickListener {


            fav_seller.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            fav_search.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            fav_category.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )


            fav_category.setTextColor(Color.parseColor("#FFFFFF"));
            fav_seller.setTextColor(Color.parseColor("#45495E"));
            fav_search.setTextColor(Color.parseColor("#45495E"));
            fav_category_rcv.show()
            fav_seller_rcv.hide()
            fav_search_rcv.hide()
            add_new_category.show()
            favoriteCategoryAdapter(favoriteCategory)

        }



        fav_seller.setOnClickListener {


            fav_category.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            fav_search.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            fav_seller.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )


            fav_seller.setTextColor(Color.parseColor("#FFFFFF"));
            fav_category.setTextColor(Color.parseColor("#45495E"));
            fav_search.setTextColor(Color.parseColor("#45495E"));
            fav_category_rcv.hide()
            fav_seller_rcv.show()
            fav_search_rcv.hide()
            add_new_category.hide()
            favoriteSellerAdapter(favoriteCategory)

        }

        fav_search.setOnClickListener {


            fav_category.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            fav_seller.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            fav_search.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )


            fav_search.setTextColor(Color.parseColor("#FFFFFF"));
            fav_category.setTextColor(Color.parseColor("#45495E"));
            fav_seller.setTextColor(Color.parseColor("#45495E"));
            fav_category_rcv.hide()
            fav_seller_rcv.hide()
            fav_search_rcv.show()
            add_new_category.hide()
            favoriteSearchAdapter(favoriteCategory)

        }

    }


    private fun favoriteCategoryAdapter(list: ArrayList<Selection>) {
        fav_category_rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.favorite_categories_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        category_name_tv.text = name
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


    private fun favoriteSellerAdapter(list: ArrayList<Selection>) {
        fav_seller_rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.favorite_seller_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        seller_name.text = name
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

    private fun favoriteSearchAdapter(list: ArrayList<Selection>) {
        fav_search_rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.favorite_search_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        search_tv.text = name

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