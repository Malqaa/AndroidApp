package com.malka.androidappp.design

import android.os.Bundle
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.*

class add_product2 : AppCompatActivity() {

    val list : ArrayList<DummyCategoryModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product2)
        list.add(DummyCategoryModel("Category 1",R.drawable.categorypic))
        list.add(DummyCategoryModel("Category 2",R.drawable.car))

        for (i in 1..5) {
            list.add(DummyCategoryModel("Category 2",R.drawable.car))

        }

        setCategoryAdaptor(list)
    }

    private fun setCategoryAdaptor(list: ArrayList<DummyCategoryModel>) {
        category_rcv.adapter = object : GenericListAdapter<DummyCategoryModel>(
            R.layout.all_categories_cardview,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        category_name_tv.text=categoryName
                        category_icon.setImageResource(categoryImage)
                        view_button.hide()
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

    private fun dummyAdaptor(list: ArrayList<String>) {
        category_rcv.adapter = object : GenericListAdapter<String>(
            R.layout.all_categories_cardview,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

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