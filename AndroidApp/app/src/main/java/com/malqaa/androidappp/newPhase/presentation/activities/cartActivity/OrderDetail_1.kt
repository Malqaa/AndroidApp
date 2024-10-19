package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityOrderDetail1Binding
import com.malqaa.androidappp.databinding.OrderDetailDesign1Binding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide

class OrderDetail_1 : BaseActivity<ActivityOrderDetail1Binding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val list: ArrayList<Negotiationmodel> = ArrayList()
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityOrderDetail1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.orderItem.completeOrderBtn.hide()
        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_orders)
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }

        list.add(
            Negotiationmodel(
                "Electric - phone",
                "RealMe 8 Pro Infinite",
                "Riyadh",
                "1653",
                R.drawable.detailpic1,
                "Ahmed",
                "Member since 5/23/2020",
                R.drawable.profiledp
            )
        )
        list.add(
            Negotiationmodel(
                "Electric - bus",
                "RealMe 8 ",
                "Dubai",
                "1567",
                R.drawable.car,
                "Ali",
                "Member since 2/21/2020",
                R.drawable.profile_pic
            )
        )
        list.add(
            Negotiationmodel(
                "Electric - car",
                "RealMe 8 Pro ",
                "Dubai3",
                "1711",
                R.drawable.car,
                "Ahmed2",
                "Member since 12/11/2022",
                R.drawable.profile_pic
            )
        )

        OrderDetailsAdapter(list, true)

    }

    private fun OrderDetailsAdapter(list: List<Negotiationmodel>, isCurrent: Boolean = true) {
        binding.orderDetailRcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.order_detail_design_1,
            bind = { element, holder, itemCount, position ->

                // Use ViewBinding for the order_detail_design_1 layout
                val itemBinding = OrderDetailDesign1Binding.bind(holder.itemView)

                itemBinding.run {
                    element.run {
                        // Access views via ViewBinding
                        productName.text = proname

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
            submitList(list)
        }
    }

}