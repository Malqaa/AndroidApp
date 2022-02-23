package com.malka.androidappp.design

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.activity_add_product2.add_product_button3
import kotlinx.android.synthetic.main.activity_add_product2.category_rcv
import kotlinx.android.synthetic.main.add_product4.*
import kotlinx.android.synthetic.main.add_product_imgs.view.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.category_name_tv

class add_product4 : AppCompatActivity() {

    val list : ArrayList<DummyCategoryModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product4)


        add_product_button5.setOnClickListener(){

            val  intent = Intent(this@add_product4, add_product5::class.java)
            startActivity(intent)
        }



        list.add(DummyCategoryModel("Honda", R.drawable.car))
        list.add(DummyCategoryModel("Civic", R.drawable.car4))
        list.add(DummyCategoryModel("Suzuki", R.drawable.car2))
        list.add(DummyCategoryModel("Ferrari", R.drawable.car3))
        list.add(DummyCategoryModel("Suzuki", R.drawable.car2))
        list.add(DummyCategoryModel("Changan", R.drawable.car4))
        list.add(DummyCategoryModel("Ferrari", R.drawable.car3))
        list.add(DummyCategoryModel("Changan", R.drawable.car4))
        list.add(DummyCategoryModel("Kia", R.drawable.car5))

        setCategoryAdaptor(list)


    }

    private fun setCategoryAdaptor(list: ArrayList<DummyCategoryModel>) {
        category_rcv.adapter = object : GenericListAdapter<DummyCategoryModel>(
            R.layout.add_product_imgs,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        add_product_ctv.text=categoryName
                        add_img_ic.setImageResource(categoryImage)
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