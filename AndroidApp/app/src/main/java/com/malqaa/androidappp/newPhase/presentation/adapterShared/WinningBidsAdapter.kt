package com.malqaa.androidappp.newPhase.presentation.adapterShared

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.WinningCardBinding
import com.malqaa.androidappp.newPhase.data.network.service.SetOnProductItemListeners
import com.malqaa.androidappp.newPhase.domain.models.winningBidsResponse.BidModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.HelpFunctions.Companion.getDifference
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.yariksoffice.lingver.Lingver
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WinningBidsAdapter(
    var bidModel: List<BidModel>,
    private var setOnProductItemListeners: SetOnProductItemListeners,
    private var isMyProduct: Boolean = false
) : RecyclerView.Adapter<WinningBidsAdapter.SellerProductViewHolder>() {

    lateinit var context: Context
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private val INTERVAL: Long = 1000L // 1 second in milliseconds


    class SellerProductViewHolder(var viewBinding: WinningCardBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerProductViewHolder {
        context = parent.context
        return SellerProductViewHolder(
            WinningCardBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    fun onDestroyHandler() {
        handler?.removeCallbacks(runnable!!)
        handler = null
        runnable = null
    }

    override fun getItemCount(): Int = bidModel.size

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SellerProductViewHolder, position: Int) {
        val bid = bidModel[position]

        val layoutParams = holder.itemView.layoutParams

        holder.itemView.layoutParams = layoutParams

        // Handle localization for time bar background
        holder.viewBinding.containerTimeBar.background =
            ContextCompat.getDrawable(
                context,
                if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC)
                    R.drawable.product_attribute_bg1_ar else R.drawable.product_attribute_bg1_en
            )


        if (bid.isMerchant) {
            holder.viewBinding.btnMerchant.visibility = View.VISIBLE
        } else {
            holder.viewBinding.btnPersonal.visibility = View.VISIBLE
        }

        // Set product title and subtitle
        holder.viewBinding.titlenamee.text = bid.productName ?: ""

        val subtitle = bid.subTitle
        holder.viewBinding.subTitlenamee.text = subtitle ?: ""
        holder.viewBinding.subTitlenamee.visibility =
            if (subtitle.isNullOrBlank()) View.GONE else View.VISIBLE

        holder.viewBinding.cityTv.text = bid.region ?: ""
        holder.viewBinding.category.text = bid.productCategory ?: ""
        holder.viewBinding.quantity.text =
            "${context.getString(R.string.quantity)}${": "}${bid.bidQuantity}"

        if (bid.bidDate!!.isEmpty()) {
            holder.viewBinding.dateTv.visibility = View.GONE
        } else {
            holder.viewBinding.dateTv.visibility = View.VISIBLE
            val outputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
            val dateTime =
                LocalDateTime.parse(bid.bidDate.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            holder.viewBinding.dateTv.text = "${context.getString(R.string.bid_date)}${": "}${dateTime.format(outputFormatter).toString()}"
        }

        holder.viewBinding.bidPrice.text = "${bid.bidPrice}${" "}${context.getString(R.string.SAR)}"


        // Load product image
        val imageUrl =
            if (!bid.productImage.isNullOrEmpty()) bid.productImage else bid.productImage
        Extension.loadImgGlide(
            context,
            imageUrl ?: "",
            holder.viewBinding.productimg,
            holder.viewBinding.loader
        )

        // Auction countdown logic
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss")
        val endDateTime = formatter.parseDateTime(bid.auctionClosingTime)

        val millisUntilEnd = endDateTime.millis - DateTime().millis
        if (millisUntilEnd > 0) {
            object : CountDownTimer(millisUntilEnd, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    val days = seconds / (60 * 60 * 24)
                    val hours = (seconds / (60 * 60)) % 24
                    val minutes = (seconds / 60) % 60
                    val secs = seconds % 60

                    holder.viewBinding.containerTimeBar.visibility = View.VISIBLE

                    // Set values or hide views
                    if (days > 0) {
                        holder.viewBinding.daysTv.text = days.toString()
                        holder.viewBinding.daysTv.visibility = View.VISIBLE
                        holder.viewBinding.titleDay.visibility = View.VISIBLE
                    } else {
                        holder.viewBinding.daysTv.visibility = View.GONE
                        holder.viewBinding.titleDay.visibility = View.GONE
                    }
                    if (hours>0){
                        holder.viewBinding.hoursTv.text = hours.toString()
                        holder.viewBinding.hoursTv.visibility = View.VISIBLE
                        holder.viewBinding.titleHour.visibility = View.VISIBLE
                    }
                    else{
                        holder.viewBinding.hoursTv.visibility = View.GONE
                        holder.viewBinding.titleHour.visibility = View.GONE
                    }

                    if (minutes>0){
                        holder.viewBinding.minutesTv.text = minutes.toString()
                        holder.viewBinding.minutesTv.visibility = View.VISIBLE
                        holder.viewBinding.titleMinutes.visibility = View.VISIBLE
                    }
                    else{
                        holder.viewBinding.minutesTv.visibility = View.GONE
                        holder.viewBinding.titleMinutes.visibility = View.GONE
                    }

                    if (secs>0){
                        holder.viewBinding.secondsTv.text = secs.toString()
                        holder.viewBinding.secondsTv.visibility = View.VISIBLE
                        holder.viewBinding.titleSeconds.visibility = View.VISIBLE
                    }
                    else{
                        holder.viewBinding.secondsTv.visibility = View.GONE
                        holder.viewBinding.titleSeconds.visibility = View.GONE
                    }

                }

                override fun onFinish() {
                    holder.viewBinding.containerTimeBar.visibility = View.GONE
                }
            }.start()
        } else {
            holder.viewBinding.containerTimeBar.visibility = View.GONE
        }

        // Load seller image
        val sellerImageUrl =
            if (!bid.sellerImage.isNullOrEmpty()) bid.sellerImage else bid.sellerImage
        Extension.loadImgGlide(
            context,
            sellerImageUrl ?: "",
            holder.viewBinding.userPic,
            holder.viewBinding.loader
        )

        holder.viewBinding.userName.text = bid.sellerName ?: ""
        holder.viewBinding.userRegion.text = bid.sellerRegion ?: ""

        if (bid.auctionClosingTime!!.isEmpty()) {
            holder.viewBinding.auctionDateTv.visibility = View.GONE
        } else {
            holder.viewBinding.auctionDateTv.visibility = View.VISIBLE
            val outputFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
            val dateTime = LocalDateTime.parse(
                bid.auctionClosingTime.toString(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            )

            holder.viewBinding.auctionTitle.text = context.getString(R.string.auction_date)

            holder.viewBinding.auctionDateTv.text = dateTime.format(outputFormatter).toString()
        }

    }

    override fun onViewRecycled(holder: SellerProductViewHolder) {
        super.onViewRecycled(holder)
        onDestroyHandler()  // Stops the countdown when the view is recycled
    }




    // Update adapter method to refresh the product list
    fun updateAdapter(
        bidModel: List<BidModel>,
        isMyProduct: Boolean = false
    ) {
        this.bidModel = bidModel
        this.isMyProduct = isMyProduct
        notifyDataSetChanged()
    }
}