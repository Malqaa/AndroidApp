package com.malka.androidappp.design

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.servicemodels.AdDetailModel

class GenericProductAdapterNew (
    var soldOrderDetail: List<AdDetailModel>,
    var context: Context
) : RecyclerView.Adapter<BaseViewHolder>() {
    var isGrid:Boolean=true


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.sold_order_details, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount() = soldOrderDetail.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {


        GenericAdaptor().productAdaptor(soldOrderDetail.get(position), context, holder,isGrid)


    }


}