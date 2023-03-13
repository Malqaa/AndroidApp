package com.malka.androidappp.recycler_browsecat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.BaseViewHolder
import com.malka.androidappp.newPhase.data.helper.GenericAdaptor
import com.malka.androidappp.newPhase.domain.models.servicemodels.Product


class GenericProductAdapter(
    var marketposts: List<Any>, var context: Context) : RecyclerView.Adapter<BaseViewHolder>() {
    var isGrid:Boolean=true




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount() = marketposts.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        GenericAdaptor().productAdaptor(
            marketposts.get(position) as Product,
            context,
            holder,
            isGrid
        )
    }

    fun updateLayout(isGrid:Boolean){
        this.isGrid=isGrid
        notifyDataSetChanged()
    }

    fun updateData(marketposts:List<Product>){
        this.marketposts=marketposts
        notifyDataSetChanged()
    }
}