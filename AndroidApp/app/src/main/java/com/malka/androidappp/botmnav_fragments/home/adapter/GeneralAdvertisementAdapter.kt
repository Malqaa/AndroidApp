package com.malka.androidappp.botmnav_fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.servicemodels.AdDetailModel
import kotlinx.android.synthetic.main.product_item.view.*

class GeneralAdvertisementAdapter(
    val listCar: List<AdDetailModel>, val fragment: Fragment, val mcontext: Context
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var onItemClick: ((AdDetailModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.view.run {

            val params: ViewGroup.LayoutParams = fullview.layoutParams
            params.width = resources.getDimension(R.dimen._220sdp).toInt()
            params.height = params.height
            fullview.layoutParams = params
            GenericAdaptor().productAdaptor(listCar.get(position), context, holder,true)
        }


    }


    override fun getItemCount(): Int {
        return listCar.size
    }

}