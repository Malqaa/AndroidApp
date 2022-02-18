package com.malka.androidappp.design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.activity_add_product2.category_rcv
import kotlinx.android.synthetic.main.activity_add_product3.*
import kotlinx.android.synthetic.main.activity_selection_product.view.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.*

class add_product3 : AppCompatActivity() {


    val list : ArrayList<DummyCategoryModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product3)

        list.add(DummyCategoryModel("Android 1", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 2", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 3", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 4", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 5", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 6", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 7", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 8", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 9", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 10", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 11", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 12", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 13", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 14", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 15", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 16", R.drawable.vectorlog))
        list.add(DummyCategoryModel("Android 17", R.drawable.vectorlog))

        setCategoryAdaptor(list)

    }


    private fun setCategoryAdaptor(list: ArrayList<DummyCategoryModel>) {
        category_rcv.adapter = object : GenericListAdapter<DummyCategoryModel>(
            R.layout.activity_selection_product,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        add_product_tv.text=categoryName
                        add_product_ic.setImageResource(categoryImage)
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