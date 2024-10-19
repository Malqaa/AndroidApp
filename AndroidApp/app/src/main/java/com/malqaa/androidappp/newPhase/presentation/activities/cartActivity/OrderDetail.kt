package com.malqaa.androidappp.newPhase.presentation.activities.cartActivity

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Filter
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityOrderDetailBinding
import com.malqaa.androidappp.databinding.OrderDetailDesignBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import com.malqaa.androidappp.newPhase.presentation.MainActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.helper.CommonBottomSheet
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide

class OrderDetail : BaseActivity<ActivityOrderDetailBinding>() {

    override fun onBackPressed() {
        intent.getBooleanExtra(ConstantObjects.isSuccess, false).let {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java).apply {
                })
                finish()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val list: ArrayList<Negotiationmodel> = ArrayList()
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.orderItem.completeOrderBtn.hide()

        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_orders)
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }

        list.add(
            Negotiationmodel(
                "Electric - phone",
                "RealMe 8 Pro ",
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

        OrderDetailsAdapter(list)
    }

    private fun OrderDetailsAdapter(list: List<Negotiationmodel>, isCurrent: Boolean = true) {
        binding.negotiationRcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.order_detail_design,
            bind = { element, holder, itemCount, position ->

                // Use ViewBinding for the order_detail_design layout
                val itemBinding = OrderDetailDesignBinding.bind(holder.itemView)

                itemBinding.run {
                    element.run {
                        // Directly use the views from the binding
                        productName.text = proname

                        sellerAccountNumber._view3().gravity = Gravity.CENTER
                        sellerAccountNumber.setOnClickListener {
                            CommonBottomSheet().bankaAccountBottomSheet(this@OrderDetail, list)
                        }

                        toBeSure.setOnClickListener {
                            val intent = Intent(this@OrderDetail, AttachInvoice::class.java)
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