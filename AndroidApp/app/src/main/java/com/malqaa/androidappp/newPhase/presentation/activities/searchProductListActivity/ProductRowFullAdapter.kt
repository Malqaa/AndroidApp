package com.malqaa.androidappp.newPhase.presentation.activities.searchProductListActivity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ProductItemRowBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.decimalNumberFormat
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.getDifference
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class ProductRowFullAdapter(
    private var mItemsList: List<Product>,
    var categoryId: Int = 0,
    var setOnProductItemListeners: SetOnProductItemListeners
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = DataBindingUtil.inflate<ProductItemRowBinding>(
            layoutInflater,
            R.layout.product_item_row,
            parent,
            false
        )
        return ProductViewHolder(itemBinding, categoryId, setOnProductItemListeners)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(mItemsList[position])
    }

    override fun getItemCount(): Int = mItemsList.size

    override fun onViewRecycled(holder: ProductViewHolder) {
        super.onViewRecycled(holder)
        holder.onDestroyHandler() // Stop the countdown when the ViewHolder is recycled
    }

    fun updateAdapter(newItemsList: List<Product>) {
        val diffCallback = ProductDiffCallback(mItemsList, newItemsList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.mItemsList = newItemsList
        diffResult.dispatchUpdatesTo(this)
    }
}

class ProductViewHolder(
    private val itemBinding: ProductItemRowBinding,
    private var categoryId: Int = 0,
    private var setOnProductItemListeners: SetOnProductItemListeners
) : RecyclerView.ViewHolder(itemBinding.root) {

    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private val INTERVAL: Long = 1000L // 1 second in milliseconds

    private var requestItem: Product? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun bind(requestItem: Product) {
        this.requestItem = requestItem
        Extension.loadImgGlide(
            itemBinding.purchasingPrice.context,
            requestItem.productImage ?: "",
            itemBinding.productImage,
            itemBinding.loader
        )

        itemBinding.subTitlenamee.visibility =
            if (requestItem.subTitle.isNullOrEmpty()) View.GONE else View.VISIBLE
        itemBinding.subTitlenamee.text = requestItem.subTitle ?: ""
        itemBinding.productCity.text = "${requestItem.country} ${
            HelpFunctions.getViewFormatForDateTrack(
                requestItem.createdAt,
                "dd/MM/yyyy"
            )
        }"

        itemBinding.ivSetting.setOnClickListener {
            setOnProductItemListeners.onProductSelect(
                adapterPosition, // Use adapterPosition instead of position
                requestItem.id,
                requestItem.categoryId,
                ConstantObjects.logged_userid,
                requestItem.providerId.toString(),
                requestItem.businessAccountId.toString()
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
            itemBinding.titlePrice.visibility =
                if (requestItem.auctionStartPrice.toDouble() != 0.0) View.VISIBLE else View.GONE
            itemBinding.lowestPrice.visibility = itemBinding.titlePrice.visibility
            itemBinding.lowestPrice.text = "${
                requestItem.auctionStartPrice.toDouble().decimalNumberFormat()
            } ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"
        } else {
            itemBinding.titlePrice.visibility = View.VISIBLE
            itemBinding.lowestPrice.visibility = View.VISIBLE
            itemBinding.lowestPrice.text = "${
                requestItem.highestBidPrice.toDouble().decimalNumberFormat()
            } ${itemBinding.purchasingPrice.context.getString(R.string.SAR)}"
        }

        itemBinding.typeProduct.text = when {
            requestItem.isAuctionEnabled -> itemBinding.purchasingPrice.context.getString(R.string.auction)
            requestItem.isNegotiationEnabled -> itemBinding.purchasingPrice.context.getString(R.string.Negotiation)
            else -> ""
        }

        if (requestItem.isFreeDelivery) itemBinding.btnFreeDelivery.show() else itemBinding.btnFreeDelivery.hide()
        if (requestItem.isMerchant) itemBinding.btnMerchant.show() else itemBinding.btnMerchant.hide()
        if (requestItem.isFeatured){
            itemBinding.linearLayoutFeatured.visibility = View.VISIBLE
            itemBinding.borderId.setBackgroundResource(R.drawable.orange_border) // أو null
        }
        else{
            itemBinding.linearLayoutFeatured.visibility = View.GONE
            itemBinding.borderId.background = null
        }
        // Handle auction closing time countdown
        if (!requestItem.auctionClosingTime.isNullOrEmpty() && !requestItem.auctionClosingTime.contains("T00:00:00")) {
            val dateTime = LocalDateTime.parse(requestItem.auctionClosingTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)


            val day = dateTime.dayOfMonth.toString().padStart(2, '0')
            val hour = dateTime.hour.toString().padStart(2, '0')
            val minute = dateTime.minute.toString().padStart(2, '0')
            val second = dateTime.second.toString().padStart(2, '0')

            itemBinding.daysTv.text = day
            itemBinding.hoursTv.text = hour
            itemBinding.minutesTv.text = minute
            itemBinding.secondsTv.text = second
        } else {
            itemBinding.containerTimeBar.hide()
            onDestroyHandler() // Stop handler if the countdown is not needed
        }

        itemBinding.vmItem = requestItem
    }

    private fun startCountdown(auctionClosingTime: String) {
        // Avoid creating a new handler if one is already running
        if (handler != null && runnable != null) {
            return
        }

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                itemBinding.containerTimeBar.show()
                val endDate: Date? = HelpFunctions.getAuctionClosingTimeByDate(auctionClosingTime)
                if (endDate != null) {
                    getDifference(
                        auctionClosingTime,
                        itemBinding.containerTimeBar,
                        itemBinding.titleDay,
                        itemBinding.daysTv,
                        itemBinding.titleHour,
                        itemBinding.hoursTv,
                        itemBinding.titleMinutes,
                        itemBinding.minutesTv,
                        itemBinding.titleSeconds,
                        itemBinding.secondsTv,
                        null
                    )
                } else {
                    itemBinding.containerTimeBar.hide()
                }
                handler?.postDelayed(this, INTERVAL)
            }
        }
        runnable?.let { handler?.post(it) }
    }

    fun onDestroyHandler() {
        handler?.removeCallbacks(runnable!!)
        handler = null
        runnable = null
    }
}

class ProductDiffCallback(
    private val oldList: List<Product>,
    private val newList: List<Product>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}


