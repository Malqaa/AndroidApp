package com.malka.androidappp.activities_main.order

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.MainActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.design.Models.negotiationmodel
import com.malka.androidappp.helper.CommonBottomSheet
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.order_detail_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*

class OrderDetail : BaseActivity() {


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
        val list: ArrayList<negotiationmodel> = ArrayList()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        toolbar_title.text = getString(R.string.my_orders)
        back_btn.setOnClickListener {
            onBackPressed()
        }



        list.add(negotiationmodel("Electric - phone","RealMe 8 Pro ","Riyadh", "1653",R.drawable.detailpic1,"Ahmed","Member since 5/23/2020",R.drawable.profiledp))
        list.add(negotiationmodel("Electric - bus","RealMe 8 ","Dubai", "1567",R.drawable.car1,"Ali", "Member since 2/21/2020",R.drawable.profile_pic))
        list.add(negotiationmodel("Electric - car","RealMe 8 Pro ","Dubai3", "1711",R.drawable.car5,"Ahmed2","Member since 12/11/2022",R.drawable.car2))

        OrderDetailsAdapter(list)



    }



    private fun OrderDetailsAdapter(list: List<negotiationmodel>, isCurrent: Boolean = true) {

        negotiation_rcv.adapter = object : GenericListAdapter<negotiationmodel>(
            R.layout.order_detail_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        product_name.text=proname


                        seller_account_number._view3().setGravity(Gravity.CENTER)



                        seller_account_number.setOnClickListener {
                            CommonBottomSheet().bankaAccountBottomSheet(this@OrderDetail, list)


                        }
                        to_be_sure.setOnClickListener {
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
            submitList(

                list
            )
        }
    }


}