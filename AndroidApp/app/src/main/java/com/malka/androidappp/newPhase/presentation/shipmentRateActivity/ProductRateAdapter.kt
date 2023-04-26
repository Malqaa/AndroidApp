package com.malka.androidappp.newPhase.presentation.shipmentRateActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.databinding.ItemProductInRateOrderBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto

class ProductRateAdapter(var dataList: ArrayList<OrderProductFullInfoDto>,var setOnClickListeners:SetOnClickListeners):Adapter<ProductRateAdapter.ProductRateViewHolder>() {
    lateinit var context:Context
    class ProductRateViewHolder(var viewBinding:ItemProductInRateOrderBinding):ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRateViewHolder {
        context=parent.context
        return ProductRateViewHolder(
            ItemProductInRateOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int =dataList.size

    override fun onBindViewHolder(holder: ProductRateViewHolder, position: Int) {
        Extension.loadThumbnail(
            context,
            dataList[position].iamge ?: "",
            holder.viewBinding.ivProductImage,
            holder.viewBinding.loader
        )
        holder.viewBinding.tvProductType.text = dataList[position].category ?: ""
        holder.viewBinding.tvProductName.text = dataList[position].productName ?: ""
        holder.viewBinding.mainContainer.setOnClickListener {
            setOnClickListeners.onProductRateSelected(position)
        }
    }
    interface SetOnClickListeners{
        fun onProductRateSelected(position: Int)
    }
}