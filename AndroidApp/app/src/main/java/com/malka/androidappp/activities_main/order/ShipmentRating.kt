package com.malka.androidappp.activities_main.order

import android.os.Bundle
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.negotiationmodel
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_shipment_rating.*
import kotlinx.android.synthetic.main.product_rating_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ShipmentRating : AppCompatActivity() {
    val list: ArrayList<negotiationmodel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment_rating)

        toolbar_title.text = getString(R.string.my_orders)
        back_btn.setOnClickListener {
            finish()

        }


        list.add(negotiationmodel("Electric - phone","RealMe 8 Pro","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car1,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car5,"Ahmed2","Member since 12/11/2022",R.drawable.car2))

        shipmentRatingAdapter(list)
    }




    private fun shipmentRatingAdapter(list: List<negotiationmodel>) {
        shipping_rcv.adapter = object : GenericListAdapter<negotiationmodel>(
            R.layout.product_rating_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        product_name.text=proname

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