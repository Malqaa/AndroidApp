package com.malka.androidappp.newPhase.presentation.cartActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import kotlinx.android.synthetic.main.activity_order_detail1.*
import kotlinx.android.synthetic.main.item_user_order.view.*
import kotlinx.android.synthetic.main.order_detail_design_1.view.*

import kotlinx.android.synthetic.main.toolbar_main.*

class OrderDetail_1 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val list: ArrayList<Negotiationmodel> = ArrayList()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail1)
        order_item.complete_order_btn.hide()
        toolbar_title.text = getString(R.string.my_orders)
        back_btn.setOnClickListener {
            finish()

        }


        list.add(Negotiationmodel("Electric - phone","RealMe 8 Pro Infinite","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(Negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(Negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car,"Ahmed2","Member since 12/11/2022",R.drawable.profile_pic))

        OrderDetailsAdapter(list, true)

    }



    private fun OrderDetailsAdapter(list: List<Negotiationmodel>, isCurrent: Boolean = true) {
        order_detail_rcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.order_detail_design_1,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        product_name.text=proname

                        shipment.setOnClickListener {
                            val intent = Intent(this@OrderDetail_1, ShipmentRating::class.java)
                            startActivity(intent)

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