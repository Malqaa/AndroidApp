package com.malka.androidappp.design

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.order.OrderDetail
import com.malka.androidappp.activities_main.order.OrderDetail_1
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.servicemodels.getCartModel
import kotlinx.android.synthetic.main.sold_order_details.view.*

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
            LayoutInflater.from(parent.context).inflate(R.layout.sold_order_details, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount() = soldOrderDetail.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, ) {


        holder.view.run {



            if (isCurrent){

                butt554.show()

                butt554.setOnClickListener {
                    val intentt = Intent(context, OrderDetail::class.java)
                    context.startActivity(intentt)

                }
            }else{
                butt554.hide()
                item_view.setOnClickListener {
                    val intentt = Intent(context, OrderDetail_1::class.java)
                    context.startActivity(intentt)
                }
            }

        }



//        GenericAdaptor().productAdaptor(soldOrderDetail.get(position), context, holder,isGrid)


    }


}