package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ProductItemRowBinding
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.decimalNumberFormat
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.utils.HelpFunctions

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
        return ProductViewHolder(itemBinding, categoryId, setOnProductItemListeners)
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


        if(requestItem.subTitle==""){
            itemBinding.subTitlenamee.visibility=View.GONE
        }
        itemBinding.subTitlenamee.text=requestItem.subTitle?:""
        itemBinding.productCity.text =
            requestItem.country + " " + HelpFunctions.getViewFormatForDateTrack(
                requestItem.createdAt,
                "dd/MM/yyyy"
            )
        itemBinding.ivSetting.setOnClickListener {
            setOnProductItemListeners.onProductSelect(
                position,
                requestItem.id,
                requestItem.categoryId
            )
        }


        if (requestItem.price.toDouble() == 0.0) {
            itemBinding.titleBuy.visibility = View.INVISIBLE
            itemBinding.purchasingPrice.visibility = View.INVISIBLE

        } else {
            itemBinding.titleBuy.visibility = View.VISIBLE
            itemBinding.purchasingPrice.visibility = View.VISIBLE
            itemBinding.purchasingPrice.text = "${
                requestItem.price.toDouble().decimalNumberFormat()
            } ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"
        }


        if (requestItem.highestBidPrice.toDouble() == 0.0) {
            if (requestItem.auctionStartPrice.toDouble() != 0.0) {
                itemBinding.titlePrice.visibility = View.VISIBLE
                itemBinding.lowestPrice.visibility = View.VISIBLE
                itemBinding.lowestPrice.text = "${
                    requestItem.auctionStartPrice.toDouble().decimalNumberFormat()
                } ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"
            } else {
                itemBinding.titlePrice.visibility = View.GONE
                itemBinding.lowestPrice.visibility = View.GONE
            }
        } else {
            itemBinding.titlePrice.visibility = View.VISIBLE
            itemBinding.lowestPrice.visibility = View.VISIBLE
            itemBinding.lowestPrice.text = "${
                requestItem.highestBidPrice.toDouble().decimalNumberFormat()
            } ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"
        }




        if (requestItem.isAuctionEnabled) {
            itemBinding.typeProduct.text =
                itemBinding.purchasingPrice.context.getString(R.string.auction)
        } else if (requestItem.isNegotiationEnabled) {
            itemBinding.typeProduct.text =
                itemBinding.purchasingPrice.context.getString(R.string.Negotiation)
        } else {

        }


        itemBinding.vmItem = requestItem
    }

}