package com.malka.androidappp.fragments.nofication_fragments

import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.notification_fragment_design.view.*
import kotlinx.android.synthetic.main.toolbar_main.*


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    val new_order = "New_Order"
    val new_product = "New_Product"
    val new_product_fav = "New_Product_Fav"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_title.text = getString(R.string.Notifications)
        back_btn.setOnClickListener {
            findNavController().popBackStack()
        }
        val notification: ArrayList<Selection> = ArrayList()

        notification.apply {
            add(Selection(new_order))
            add(Selection(new_product))
            add(Selection(new_product_fav))
            add(Selection(new_order))
            add(Selection(new_product))
            add(Selection(new_product_fav))

        }
        notificationAdaptor(notification)
    }

    private fun notificationAdaptor(list: ArrayList<Selection>) {
        notification_rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.notification_fragment_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        when(name){
                            new_order->{
                                new_order_layout.show()
                                new_product_added.hide()
                                fav_merchant_add_product.hide()
                            }
                            new_product->{
                                new_order_layout.hide()
                                new_product_added.show()
                                fav_merchant_add_product.hide()

                            }
                            new_product_fav->{
                                new_order_layout.hide()
                                new_product_added.hide()
                                fav_merchant_add_product.show()

                            }
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
