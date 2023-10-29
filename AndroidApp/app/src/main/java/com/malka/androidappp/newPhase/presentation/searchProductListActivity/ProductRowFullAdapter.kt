package com.malka.androidappp.newPhase.presentation.searchProductListActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ProductItemRowBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners

class ProductRowFullAdapter(
    private var mItemsList: List<Product>,
    var categoryId: Int = 0,
    var setOnProductItemListeners: SetOnProductItemListeners
) : RecyclerView.Adapter<ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        val itemBinding = DataBindingUtil.inflate<ProductItemRowBinding>(
            layoutInflater,
            R.layout.product_item_row,
            parent,
            false
        )
        return ProductViewHolder(itemBinding,categoryId,setOnProductItemListeners)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder, position: Int
    ) {
        holder.bind(mItemsList[position])
    }

    override fun getItemCount(): Int = mItemsList.size

    fun updateAdapter(mItemsList: List<Product>) {
        this.mItemsList = mItemsList
        notifyDataSetChanged()

    }
}

class ProductViewHolder(
    private val itemBinding: ProductItemRowBinding,
    var categoryId: Int = 0,
    var setOnProductItemListeners: SetOnProductItemListeners,
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var requestItem: Product? = null


    @SuppressLint("SetTextI18n")
    fun bind(requestItem: Product) {
        this.requestItem = requestItem
        Extension.loadThumbnail(
            itemBinding.purchasingPrice.context,
            requestItem.productImage ?: "",
            itemBinding.productImage,
            itemBinding.loader
        )
        itemBinding.purchasingPrice.text = "${
            requestItem.price.toDouble().decimalNumberFormat()
        } ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"

        itemBinding.lowestPrice.text =
            "${requestItem.highestBidPrice} ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"

        itemBinding.ivSetting.setOnClickListener {
            setOnProductItemListeners.onProductSelect(
                position,
                requestItem.id,
                requestItem.categoryId
            )
        }

        itemBinding.vmItem = requestItem
    }

}