package com.malqaa.androidappp.newPhase.presentation.adapterShared

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ProductItemBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.decimalNumberFormat
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.getDifference
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver
import java.util.Date


class ProductHorizontalAdapter(
    var productList: List<Product>,
    private var setOnProductItemListeners: SetOnProductItemListeners,
    var categoryId: Int = 0,
    private var isHorizontal: Boolean,
    private var isMyProduct: Boolean = false
) : RecyclerView.Adapter<ProductHorizontalAdapter.SellerProductViewHolder>() {

    lateinit var context: Context
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private val INTERVAL: Long = 1000L // 1 second in milliseconds

    class SellerProductViewHolder(var viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        context = parent.context
        return SellerProductViewHolder(
            ProductItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun onDestroyHandler() {
        handler?.removeCallbacks(runnable!!)
        handler = null
        runnable = null
    }

    override fun getItemCount(): Int = productList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
        val product = productList[position]

        // Handle horizontal or vertical layout based on the flag
        if (isHorizontal) {
            val params: ViewGroup.LayoutParams = holder.viewBinding.fullview.layoutParams
            params.width = context.resources.getDimension(R.dimen._220sdp).toInt()
            holder.viewBinding.fullview.layoutParams = params
        }

        // Handle visibility of free delivery and merchant buttons
        holder.viewBinding.btnFreeDelivery.visibility =
            if (product.isFreeDelivery) View.VISIBLE else View.GONE
        holder.viewBinding.btnMerchant.visibility =
            if (product.isMerchant) View.VISIBLE else View.GONE

        // Handle favorite or settings icon based on product ownership
        if (isMyProduct) {
            holder.viewBinding.ivFav.visibility = View.GONE
            holder.viewBinding.ivSetting.visibility = View.VISIBLE
            holder.viewBinding.ivSetting.setOnClickListener {
                setOnProductItemListeners.onShowMoreSetting(
                    position,
                    product.id,
                    categoryId
                )
            }
        } else {
            holder.viewBinding.ivFav.visibility = View.VISIBLE
            holder.viewBinding.ivSetting.visibility = View.GONE
            holder.viewBinding.ivFav.setImageResource(
                if (product.isFavourite) R.drawable.starcolor else R.drawable.star
            )
        }

        // Handle localization for time bar background
        holder.viewBinding.containerTimeBar.background =
            ContextCompat.getDrawable(
                context,
                if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC)
                    R.drawable.product_attribute_bg1_ar else R.drawable.product_attribute_bg1_en
            )

        // Set product title and subtitle
        holder.viewBinding.titlenamee.text = product.name ?: ""
        holder.viewBinding.subTitlenamee.text = product.subTitle ?: ""

        // Auction and Negotiation flags
        holder.viewBinding.typeProduct.text = when {
            product.isAuctionEnabled -> context.getString(R.string.auction)
            product.isNegotiationEnabled -> context.getString(R.string.Negotiation)
            else -> ""
        }

        // Load product image
        val imageUrl = if (!product.productImage.isNullOrEmpty()) product.productImage else product.image
        Extension.loadImgGlide(context, imageUrl ?: "", holder.viewBinding.productimg, holder.viewBinding.loader)

        // Handle product price and discounts
        if (product.price == product.priceDisc || product.price == product.priceDiscount) {
            holder.viewBinding.tvProductPrice.text = "${product.price.toDouble()} ${context.getString(R.string.SAR)}"
            holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.visibility = View.GONE
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.visibility = View.GONE
        } else {
            if (isHorizontal) {
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.visibility = View.VISIBLE
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags =
                    holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.text =
                    "${product.price.toDouble()} ${context.getString(R.string.SAR)}"
                holder.viewBinding.tvProductPrice.text =
                    "${product.priceDisc.toDouble()} ${context.getString(R.string.SAR)}"
            } else {
                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.visibility = View.VISIBLE
                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.paintFlags =
                    holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.text =
                    "${product.price.toDouble()} ${context.getString(R.string.SAR)}"
                holder.viewBinding.tvProductPrice.text =
                    "${product.priceDisc.toDouble()} ${context.getString(R.string.SAR)}"
            }
        }

        // Show/hide auction start or highest bid price
        holder.viewBinding.LowestPriceLayout.visibility = if (product.highestBidPrice.toDouble() != 0.0) {
            holder.viewBinding.LowestPrice.text = "${product.highestBidPrice} ${context.getString(R.string.SAR)}"
            View.VISIBLE
        } else if (product.auctionStartPrice.toDouble() != 0.0) {
            holder.viewBinding.LowestPrice.text = "${product.auctionStartPrice} ${context.getString(R.string.SAR)}"
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

        // Click listeners for product item and favorite icon
        holder.viewBinding.fullview.setOnClickListener {
            setOnProductItemListeners.onProductSelect(position, product.id, categoryId)
        }
        holder.viewBinding.ivFav.setOnClickListener {
            setOnProductItemListeners.onAddProductToFav(position, product.id, categoryId)
        }

        // Auction countdown logic
        if (!product.auctionClosingTime.isNullOrEmpty() && !product.auctionClosingTime.contains("T00:00:00")) {
            startCountdown(holder, product.auctionClosingTime)
        } else {
            holder.viewBinding.containerTimeBar.visibility = View.GONE
            onDestroyHandler() // Stop handler if the countdown is not needed
        }
    }

    override fun onViewRecycled(holder: SellerProductViewHolder) {
        super.onViewRecycled(holder)
        onDestroyHandler()  // Stops the countdown when the view is recycled
    }


    private fun startCountdown(holder: SellerProductViewHolder, auctionClosingTime: String) {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                holder.viewBinding.containerTimeBar.visibility = View.VISIBLE
                val endDate = HelpFunctions.getAuctionClosingTimeByDate(auctionClosingTime)
                if (endDate != null) {
                    getDifference(
                        auctionClosingTime,
                        holder.viewBinding.containerTimeBar,
                        holder.viewBinding.titleDay,
                        holder.viewBinding.daysTv,
                        holder.viewBinding.titleHour,
                        holder.viewBinding.hoursTv,
                        holder.viewBinding.titleMinutes,
                        holder.viewBinding.minutesTv,
                        holder.viewBinding.titleSeconds,
                        holder.viewBinding.secondsTv,
                        null
                    )
                } else {
                    holder.viewBinding.containerTimeBar.visibility = View.GONE
                }
                handler?.postDelayed(this, INTERVAL)
            }
        }
        runnable?.let { handler?.post(it) }
    }

    // Update adapter method to refresh the product list
    fun updateAdapter(
        productList: List<Product>,
        isHorizontal: Boolean = false,
        isMyProduct: Boolean = false
    ) {
        this.productList = productList
        this.isHorizontal = isHorizontal
        this.isMyProduct = isMyProduct
        notifyDataSetChanged()
    }
}
