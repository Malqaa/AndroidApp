package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity

import android.os.Bundle
import android.widget.Filter
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityShipmentRatingBinding
import com.malqaa.androidappp.databinding.ProductRatingDesignBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter

class ShipmentRating : BaseActivity<ActivityShipmentRatingBinding>() {
    val list: ArrayList<Negotiationmodel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityShipmentRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_orders)
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }

        list.add(
            Negotiationmodel(
                "Electric - phone",
                "RealMe 8 Pro",
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

        shipmentRatingAdapter(list)
    }

    private fun shipmentRatingAdapter(list: List<Negotiationmodel>) {
        binding.shippingRcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.product_rating_design,
            bind = { element, holder, itemCount, position ->

                // Use ViewBinding for the product_rating_design layout
                val itemBinding = ProductRatingDesignBinding.bind(holder.itemView)

                itemBinding.run {
                    element.run {
                        // Access views via ViewBinding
                        productName.text = proname
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