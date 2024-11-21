package com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemShipmentInOrderProductBinding
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class OrderProductAdapter(var dataList: ArrayList<OrderProductFullInfoDto>) :
    Adapter<OrderProductAdapter.OrderProductViewHOlder>() {

    lateinit var context: Context

    class OrderProductViewHOlder(var viewBinding: ItemShipmentInOrderProductBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductViewHOlder {
        context = parent.context
        return OrderProductViewHOlder(
            ItemShipmentInOrderProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: OrderProductViewHOlder, position: Int) {

        Extension.loadImgGlide(
            context,
            dataList[position].iamge ?: "",
            holder.viewBinding.ivProductImage,
            holder.viewBinding.loader
        )
        holder.viewBinding.tvProductType.text = dataList[position].category ?: ""
        holder.viewBinding.tvProductName.text = dataList[position].productName ?: ""
        var location = ""
        if (dataList[position].region != null) {
            location += dataList[position].region
        }
        if (dataList[position].region != null) {
            location += "-${dataList[position].region}"
        }
        holder.viewBinding.tvProductCity.text = location

        val price = (dataList[position].price.toDouble())
        val priceDiscount = (dataList[position].priceDiscount.toDouble())

        if (priceDiscount == 0.0 || priceDiscount >= price) {
            // No discount or invalid discount; show only the current price
            holder.viewBinding.prodPrice.text = "$price ${context.getString(R.string.Rayal)}"
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.hide()
        } else {
            // Discounted price; show both current and old prices
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.show()
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags =
                Paint.STRIKE_THRU_TEXT_FLAG
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.text =
                "$price ${context.getString(R.string.Rayal)}"
            holder.viewBinding.prodPrice.text =
                "$priceDiscount ${context.getString(R.string.Rayal)}"
        }

        holder.viewBinding.tvQuentity.text = dataList[position].quantity.toString()
    }
}