package com.malka.androidappp.newPhase.presentation.cartActivity

import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import kotlinx.android.synthetic.main.activity_shipment_rating.*
import kotlinx.android.synthetic.main.product_rating_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ShipmentRating : BaseActivity() {
    val list: ArrayList<Negotiationmodel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment_rating)

        toolbar_title.text = getString(R.string.my_orders)
        back_btn.setOnClickListener {
            finish()

        }


        list.add(Negotiationmodel("Electric - phone","RealMe 8 Pro","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(Negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(Negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car,"Ahmed2","Member since 12/11/2022",R.drawable.profile_pic))

        shipmentRatingAdapter(list)
    }




    private fun shipmentRatingAdapter(list: List<Negotiationmodel>) {
        shipping_rcv.adapter = object : GenericListAdapter<Negotiationmodel>(
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