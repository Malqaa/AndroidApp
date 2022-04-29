package com.malka.androidappp.helper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.order.OrderDetail
import com.malka.androidappp.activities_main.order.OrderDetail_1
import com.malka.androidappp.servicemodels.getCartModel
import kotlinx.android.synthetic.main.order_item.view.*

class GenericOrderAdapter(
    var soldOrderDetail: List<getCartModel.Data>,
    var context: Context,
    var isCurrent: Boolean = true
) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount() = soldOrderDetail.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {


        holder.view.run {
            soldOrderDetail.get(position).run {
                order_number_tv.text = "#$orderNumber"
                order_status_tv.text = orderStatus
                order_time_tv.text = createddateFormated
                total_order_tv.text = "${advertisements.price} ${context.getString(R.string.rial)}"
                if (isCurrent) {
                    complete_order_btn.show()
                    setOnClickListener {
                        val intentt = Intent(context, OrderDetail::class.java)
                        context.startActivity(intentt)
                    }
                } else {
                    complete_order_btn.hide()
                    setOnClickListener {
                        val intentt = Intent(context, OrderDetail_1::class.java)
                        context.startActivity(intentt)
                    }
                }
            }


        }


    }


}