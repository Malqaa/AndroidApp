package com.malqaa.androidappp.newPhase.presentation.activities.shipmentRateActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemProductInRateOrderBinding
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto

class ProductRateAdapter(
    var dataList: ArrayList<OrderProductFullInfoDto>,
    var setOnClickListeners: SetOnClickListeners
) : Adapter<ProductRateAdapter.ProductRateViewHolder>() {
    lateinit var context: Context
    var selectedPosition = 0

    class ProductRateViewHolder(var viewBinding: ItemProductInRateOrderBinding) :
        ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRateViewHolder {
        context = parent.context
        return ProductRateViewHolder(
            ItemProductInRateOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ProductRateViewHolder, position: Int) {
        Extension.loadImgGlide(
            context,
            dataList[position].iamge ?: "",
            holder.viewBinding.ivProductImage,
            holder.viewBinding.loader
        )
        holder.viewBinding.tvProductType.text = dataList[position].category ?: ""
        holder.viewBinding.tvProductName.text = dataList[position].productName ?: ""
        holder.viewBinding.mainContainer.setOnClickListener {
            selectedPosition = position
            setOnClickListeners.onProductRateSelected(position)
            notifyDataSetChanged()
        }
        if (selectedPosition == position) {
            holder.viewBinding.fullview.setBackgroundResource(R.drawable.product_attribute_bg4_orange)
        } else {
            holder.viewBinding.fullview.setBackgroundResource(R.color.white)
        }

    }

    interface SetOnClickListeners {
        fun onProductRateSelected(position: Int)
    }
}