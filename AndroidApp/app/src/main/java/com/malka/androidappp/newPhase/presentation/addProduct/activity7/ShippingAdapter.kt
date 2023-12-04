package com.malka.androidappp.newPhase.presentation.addProduct.activity7

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.RowShippingBinding
import com.malka.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionObject

class ShippingAdapter(
    var dataList: List<ShippingOptionObject>,
    var setOnSelectedShipping: SetOnSelectedShipping,
) :
    RecyclerView.Adapter<ShippingAdapter.ShippingViewHolder>() {
    lateinit var context: Context

    class ShippingViewHolder(var viewBinding: RowShippingBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingViewHolder {
        context = parent.context
        return ShippingViewHolder(
            RowShippingBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ShippingViewHolder, position: Int) {
        val details: ShippingOptionObject = dataList[position]
        holder.viewBinding.tvMustPickUp.text= details.shippingOptionName

        if(dataList[position].selected){
            holder.viewBinding.rbMustPickup.isChecked=true
            dataList[position].selected=true
            holder.viewBinding.switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
            holder.viewBinding.tvMustPickUp.setTextColor(ContextCompat.getColor(holder.viewBinding.tvMustPickUp.context, R.color.bg))
        }else{
            holder.viewBinding.rbMustPickup.isChecked=false
            dataList[position].selected=false
            holder.viewBinding.switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
            holder.viewBinding.tvMustPickUp.setTextColor(ContextCompat.getColor(holder.viewBinding.tvMustPickUp.context, R.color.grey))
        }

        holder.viewBinding.rbMustPickup.setOnClickListener {
            updateAdapter(dataList[position],dataList)
            setOnSelectedShipping.setOnSelectedShipping(dataList[position])
        }

    }
    fun updateAdapter(item :ShippingOptionObject,dataList: List<ShippingOptionObject>){
        for(i in dataList){
            i.selected = item.id==i.id&&( !item.selected)
        }

        this.dataList=dataList
        notifyDataSetChanged()
    }

    interface SetOnSelectedShipping {
        fun setOnSelectedShipping(shippingItem: ShippingOptionObject)
    }
}