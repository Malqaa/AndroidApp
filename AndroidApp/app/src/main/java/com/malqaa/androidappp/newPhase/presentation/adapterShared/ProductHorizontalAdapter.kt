package com.malqaa.androidappp.newPhase.presentation.adapterShared

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ProductItemBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.Extension.decimalNumberFormat
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.productResp.Product
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.util.*


class ProductHorizontalAdapter(
    var productList: List<Product>,
    private var setOnProductItemListeners: SetOnProductItemListeners,
    var categoryId: Int = 0,
    private var isHorizenal: Boolean,
    private var isMyProduct: Boolean = false
) : RecyclerView.Adapter<ProductHorizontalAdapter.SellerProductViewHolder>() {
    lateinit var context: Context

    class SellerProductViewHolder(var viewBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        context = parent.context
        return SellerProductViewHolder(
            ProductItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = productList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
        if (isHorizenal) {
            val params: ViewGroup.LayoutParams = holder.viewBinding.fullview.layoutParams
            params.width = context.resources.getDimension(R.dimen._220sdp).toInt()
            params.height = params.height
            holder.viewBinding.fullview.layoutParams = params
        }

        if (productList[position].isFreeDelivery) {
            holder.viewBinding.btnFreeDelivery.show()
        } else {
            holder.viewBinding.btnFreeDelivery.hide()
        }
        if (productList[position].isMerchant) {
            holder.viewBinding.btnMerchant.show()
        } else {
            holder.viewBinding.btnMerchant.hide()
        }

        if (isMyProduct) {
            holder.viewBinding.ivFav.hide()
            holder.viewBinding.ivSetting.show()
            holder.viewBinding.ivSetting.setOnClickListener {
                setOnProductItemListeners.onShowMoreSetting(
                    position,
                    productList[position].id,
                    categoryId
                )
            }
        } else {
            holder.viewBinding.ivFav.show()
            holder.viewBinding.ivSetting.hide()
            if (HelpFunctions.isUserLoggedIn()) {
                if (productList[position].isFavourite) {
                    holder.viewBinding.ivFav.setImageResource(R.drawable.starcolor)
                } else {
                    holder.viewBinding.ivFav.setImageResource(R.drawable.star)
                }
            } else {
                holder.viewBinding.ivFav.setImageResource(R.drawable.star)
            }
        }

        if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC) {
            holder.viewBinding.containerTimeBar.background =
                ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_ar)
        } else {
            holder.viewBinding.containerTimeBar.background =
                ContextCompat.getDrawable(context, R.drawable.product_attribute_bg1_en)
        }
        holder.viewBinding.titlenamee.text = productList[position].name ?: ""
        if (productList[position].isAuctionEnabled) {
            holder.viewBinding.typeProduct.text = context.getString(R.string.auction)
        } else if (productList[position].isNegotiationEnabled) {
            holder.viewBinding.typeProduct.text = context.getString(R.string.Negotiation)
        }

        holder.viewBinding.cityTv.text = productList[position].regionName ?: ""

        if (productList[position].image != null && productList[position].image != "") {
            Extension.loadThumbnail(
                context,
                productList[position].image ?: "",
                holder.viewBinding.productimg,
                holder.viewBinding.loader
            )
        } else {
            Extension.loadThumbnail(
                context,
                productList[position].productImage ?: "",
                holder.viewBinding.productimg,
                holder.viewBinding.loader
            )
        }
//        if (categoryId != 0) {
//            Extension.loadThumbnail(
//                context,
//                productList[position].image ?: "",
//                holder.viewBinding.productimg,
//                holder.viewBinding.loader
//            )
//        } else {
//            Extension.loadThumbnail(
//                context,
//                productList[position].productImage ?: "",
//                holder.viewBinding.productimg,
//                holder.viewBinding.loader
//            )
//        }
//        holder.viewBinding.LowestPriceLayout.hide()
//        holder.viewBinding.LowestPriceLayout2.hide()
        holder.viewBinding.lisView.hide()

        if (productList[position].priceDisc == productList[position].price
            || productList[position].priceDiscount == productList[position].price
        ) {
            holder.viewBinding.tvProductPrice.text =
                "${productList[position].price.toDouble()} ${
                    context.getString(
                        R.string.SAR
                    )
                }"
            holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.hide()
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.hide()
        } else {
            if (isHorizenal) {
                // for Horizental View
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.show()
                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.hide()
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags =
                    holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.text =
                    "${productList[position].price.toDouble()} ${
                        context.getString(
                            R.string.SAR
                        )
                    }"
                holder.viewBinding.tvProductPrice.text =
                    "${productList[position].priceDiscount.toDouble()} ${
                        context.getString(
                            R.string.SAR
                        )
                    }"
            } else {
                // for Vertical View
                holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.hide()
                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.show()
//                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.paintFlags =
//                    holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.text =
//                    "${productList[position].price.toDouble()} ${
//                        context.getString(
//                            R.string.SAR
//                        )
//                    }"
//                holder.viewBinding.tvProductPrice.text =
//                    "${productList[position].priceDisc.toDouble()} ${
//                        context.getString(
//                            R.string.SAR
//                        )
//                    }"
            }


        }

        holder.viewBinding.purchasingPriceTv2.text = "${
            productList[position].price.toDouble().decimalNumberFormat()
        } ${context.getString(R.string.SAR)}"

        holder.viewBinding.fullview.setOnClickListener {
            setOnProductItemListeners.onProductSelect(
                position,
                productList[position].id,
                categoryId
            )
        }
        holder.viewBinding.ivFav.setOnClickListener {
            setOnProductItemListeners.onAddProductToFav(
                position,
                productList[position].id,
                categoryId
            )
        }
//        if(productList[position].isFixedPriceEnabled && productList[position].isAuctionEnabled){
//            holder.viewBinding.containerTimeBar.show()
//        }else if(productList[position].isAuctionEnabled){
//            holder.viewBinding.containerTimeBar.show()
//        }else{
//            holder.viewBinding.containerTimeBar.hide()
//        }

        if (productList[position].auctionClosingTime != null) {
            if (!productList[position].auctionClosingTime!!.contains("T00:00:00")) {
                holder.viewBinding.containerTimeBar.show()
                val endDate: Date? =
                    HelpFunctions.getAuctionClosingTimeByDate(productList[position].auctionClosingTime!!)
//                println("hhhh "+endDate.toString()+" "+Calendar.getInstance().time)
                if (endDate != null) {
                    getDifference(productList[position].auctionClosingTime!!, holder)
                } else {
                    holder.viewBinding.containerTimeBar.hide()
                }
            } else {
                holder.viewBinding.containerTimeBar.hide()
            }
        } else {
            holder.viewBinding.containerTimeBar.hide()
        }

//        if (productList[position].highestBidPrice != 0f) {
//            holder.viewBinding.LowestPriceLayout.show()
//            holder.viewBinding.LowestPriceLayout2.show()
//            holder.viewBinding.LowestPrice.text =
//                "${productList[position].highestBidPrice} ${context.getString(R.string.SAR)}"
//            holder.viewBinding.LowestPrice2.text =
//                "${productList[position].highestBidPrice} ${context.getString(R.string.SAR)}"
//        } else {
//            holder.viewBinding.LowestPriceLayout.hide()
//            holder.viewBinding.LowestPriceLayout2.hide()
//        }



        if(productList[position].price.toDouble()!=0.0){
            holder.viewBinding.purchaseContainer.visibility = View.VISIBLE
            holder.viewBinding.tvOldPRiceProductPriceForHorizentalView.text =
                "${productList[position].price.toDouble()} ${
                    context.getString(
                        R.string.SAR
                    )
                }"
            holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.paintFlags =
                holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.viewBinding.tvOldPRiceProductPriceForVertiaclView.text =
                "${productList[position].price.toDouble()} ${
                    context.getString(
                        R.string.SAR
                    )
                }"
            holder.viewBinding.tvProductPrice.text =
                "${productList[position].priceDisc.toDouble()} ${
                    context.getString(
                        R.string.SAR
                    )
                }"
        }else{
            holder.viewBinding.purchaseContainer.visibility = View.INVISIBLE

        }
        if(productList[position].highestBidPrice.toDouble()!=0.0){
            holder.viewBinding.LowestPriceLayout.visibility = View.VISIBLE
            holder.viewBinding.LowestPrice.text =
                "${productList[position].highestBidPrice} ${holder.viewBinding.typeProduct.context.getString(R.string.SAR)}"

        }else{
            holder.viewBinding.LowestPriceLayout.visibility = View.INVISIBLE
        }

        if (productList[position].isAuctionEnabled) {
//            holder.viewBinding.purchaseContainer.visibility = View.GONE
//            holder.viewBinding.LowestPriceLayout.visibility = View.VISIBLE

            holder.viewBinding.typeProduct.text =
                holder.viewBinding.typeProduct.context.getString(R.string.auction)
//            holder.viewBinding.LowestPrice.text =
//                "${productList[position].highestBidPrice} ${holder.viewBinding.typeProduct.context.getString(R.string.SAR)}"
        } else if (productList[position].isNegotiationEnabled) {

//            holder.viewBinding.purchaseContainer.visibility = View.GONE
//            holder.viewBinding.LowestPriceLayout.visibility = View.VISIBLE

            holder.viewBinding.typeProduct.text =
                holder.viewBinding.typeProduct.context.getString(R.string.Negotiation)
//            holder.viewBinding.LowestPrice.text =
//                "${productList[position].highestBidPrice} ${holder.viewBinding.typeProduct.context.getString(R.string.SAR)}"
        } else {
//            if(productList[position].price.toDouble()!=0.0)
//            holder.viewBinding.purchaseContainer.visibility = View.VISIBLE
//            else{
//                holder.viewBinding.purchaseContainer.visibility = View.INVISIBLE
//            }
//            holder.viewBinding.LowestPriceLayout.visibility = View.GONE
//
//            holder.viewBinding.tvProductPrice.text = "${
//                productList[position].price.toDouble().decimalNumberFormat()
//            } ${holder.viewBinding.tvProductPrice.context.getString(R.string.SAR)}"
        }
    }

    fun updateAdapter(
        productList: List<Product>,
        isHorizontal: Boolean = false,
        isMyProduct: Boolean = false
    ) {
        this.productList = productList
        isHorizenal = isHorizontal
        this.isMyProduct = isMyProduct
        notifyDataSetChanged()
    }

    private fun getDifference(targetDateTimeString: String, holder: SellerProductViewHolder) {
        try {

            val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
            val targetDateTime = formatter.parseDateTime(targetDateTimeString)

            // Get the current date and time
            val currentDateTime = DateTime()

            // Calculate the duration between the current time and the target time
            val duration = Duration(currentDateTime, targetDateTime)

            // Get the difference in days, hours, and minutes as Long values
            val daysDifference = duration.standardDays
            val hoursDifference = duration.standardHours % 24
            val minutesDifference = duration.standardMinutes % 60


            if (daysDifference <= 0 && (hoursDifference <= 0) && (minutesDifference <= 0)) {
                holder.viewBinding.containerTimeBar.hide()
            } else
                holder.viewBinding.containerTimeBar.show()

            holder.viewBinding.daysTv.text = daysDifference.toString()
            holder.viewBinding.hoursTv.text = hoursDifference.toString()
            holder.viewBinding.minutesTv.text = minutesDifference.toString()

        } catch (e: Exception) {

        }
    }


}