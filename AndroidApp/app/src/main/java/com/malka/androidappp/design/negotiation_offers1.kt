package com.malka.androidappp.design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.negotiationmodel
import com.malka.androidappp.design.Models.reviewmodel
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.negotiation_offers_design.view.*
import kotlinx.android.synthetic.main.product_review_design.view.*

class negotiation_offers1 : AppCompatActivity() {
    val list: ArrayList<negotiationmodel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.negotiation_offers1)

        list.add(negotiationmodel("Electric - phone","RealMe 8 Pro Infinite","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car1,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car5,"Ahmed2","Member since 12/11/2022",R.drawable.car2))
        list.add(negotiationmodel("Electric - car3","RealMe 8 Infinite","Uk", "9832",R.drawable.car4,"Ahmed3","Member since 5/22/2021",R.drawable.profiledp))
        list.add(negotiationmodel("Electric - jeep","Pro Infinite","Saudia", "6781",R.drawable.car2,"Ahmed5","Member since 1/28/2022",R.drawable.car4))

        setCategoryAdaptor(list)
    }




    private fun setCategoryAdaptor(list: ArrayList<negotiationmodel>) {
        category_rcv.adapter = object : GenericListAdapter<negotiationmodel>(
            R.layout.negotiation_offers_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        product_type.text=protype
                        product_name.text=proname
                        product_city.text=procity
                        product_image.setImageResource(proimage)
                        person_name.text=pername
                        person_since.text=persince
                        person_dp.setImageResource(perdp)
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