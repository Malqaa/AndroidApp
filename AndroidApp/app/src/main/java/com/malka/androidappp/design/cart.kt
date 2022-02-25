package com.malka.androidappp.design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.cartmdel
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.cart_design.view.*

class cart : AppCompatActivity() {
    val list: ArrayList<cartmdel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        list.add(cartmdel("Electric - phone","RealMe 8 Pro Infinite","Riyadh", "1653",R.drawable.detailpic1,))
        list.add(cartmdel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car1,))
        list.add(cartmdel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car5,))
        list.add(cartmdel("Electric - car3","RealMe 8 Infinite","Uk", "9832",R.drawable.car4,))
        list.add(cartmdel("Electric - jeep","Pro Infinite","Saudia", "6781",R.drawable.car2,))

        setCategoryAdaptor(list)

    }


    private fun setCategoryAdaptor(list: ArrayList<cartmdel>) {
        category_rcv.adapter = object : GenericListAdapter<cartmdel>(
            R.layout.cart_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        prod_type.text=protype
                        prod_name.text=proname
                        prod_city.text=procity
                        prod_price.text=proprice
                        prod_image.setImageResource(proimage)


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