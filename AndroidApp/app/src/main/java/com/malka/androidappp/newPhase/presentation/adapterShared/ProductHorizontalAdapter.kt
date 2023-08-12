package com.malka.androidappp.newPhase.presentation.adapterShared

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ProductItemBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.Extension.decimalNumberFormat
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_product_details_item_2.*
import java.util.*


class ProductHorizontalAdapter(
    var productList: List<Product>,
    var setOnProductItemListeners: SetOnProductItemListeners,
    var categoryId: Int = 0,
    var isHorizenal: Boolean,
    var isMyProduct: Boolean = false
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
        holder.viewBinding.cityTv.text = productList[position].regionName ?: ""

        if(productList[position].image!=null&&productList[position].image!=""){
            Extension.loadThumbnail(
                context,
                productList[position].image ?: "",
                holder.viewBinding.productimg,
                holder.viewBinding.loader
            )
        }else{
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
        holder.viewBinding.LowestPriceLayout.hide()
        holder.viewBinding.LowestPriceLayout2.hide()
        holder.viewBinding.lisView.hide()

        if (productList[position].priceDisc == productList[position].price
            || productList[position].priceDiscount == productList[position].price)
        {
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
        if(productList[position].auctionClosingTime!=null){
            holder.viewBinding.containerTimeBar.show()
            var endDate: Date?=HelpFunctions.getAuctionClosingTimeByDate(productList[position].auctionClosingTime!!)
//                println("hhhh "+endDate.toString()+" "+Calendar.getInstance().time)
            if(endDate!=null){
                getDifference(Calendar.getInstance().time,endDate,holder)
            }else{
                holder.viewBinding.containerTimeBar.hide()
            }

        }else{
            holder.viewBinding.containerTimeBar.hide()
        }

        if(productList[position].highestBidPrice!=0f){
            holder.viewBinding.LowestPriceLayout.show()
            holder.viewBinding.LowestPriceLayout2.show()
            holder.viewBinding.LowestPrice.text = "${productList[position].highestBidPrice} ${ context.getString(R.string.SAR)}"
            holder.viewBinding.LowestPrice2.text = "${productList[position].highestBidPrice} ${ context.getString(R.string.SAR)}"
        }else{
            holder.viewBinding.LowestPriceLayout.hide()
            holder.viewBinding.LowestPriceLayout2.hide()
        }
    }
    fun getDifference(curretndate: Date, endDate: Date, holder: SellerProductViewHolder){
        //milliseconds
        //milliseconds
        var different: Long = endDate.time - curretndate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = different / daysInMilli
        different = different % daysInMilli

        val elapsedHours = different / hoursInMilli
        different = different % hoursInMilli

        val elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli

        val elapsedSeconds = different / secondsInMilli

        holder.viewBinding.daysTv.text=elapsedDays.toString()
        holder.viewBinding.hoursTv.text=elapsedHours.toString()
        holder.viewBinding.minutesTv.text=elapsedMinutes.toString()

    }


}