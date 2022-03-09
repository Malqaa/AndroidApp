package com.malka.androidappp.design

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.design.Models.itemDetailmodel
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.activity_add_product2.category_rcv
import kotlinx.android.synthetic.main.add_item_details1.*
import kotlinx.android.synthetic.main.add_item_details1.add_product_button7
import kotlinx.android.synthetic.main.add_item_details2.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.*
import kotlinx.android.synthetic.main.item_details2_desgin.view.*

class add_item_details2 : AppCompatActivity() {

    val list: ArrayList<itemDetailmodel> =  ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_details2)


        add_product_button8.setOnClickListener(){

            val  intent = Intent(this@add_item_details2, add_item_checkout::class.java)
            startActivity(intent)
        }



        setCategoryAdaptor(list)
    }




    private fun setCategoryAdaptor(list: List<itemDetailmodel>) {
        category_rcv.adapter = object : GenericListAdapter<itemDetailmodel>(
            R.layout.item_details2_desgin,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        pkg_name.text=packagename
                        pkg_price.text=packageprice
                        pkg_service1.text=packageservice1
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