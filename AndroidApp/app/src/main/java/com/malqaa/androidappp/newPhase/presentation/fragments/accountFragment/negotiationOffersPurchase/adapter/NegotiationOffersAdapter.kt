package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemNegotiationOffersBinding
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferDetails
import kotlinx.android.synthetic.main.activity_product_details_item_2.txtTypeUser


class NegotiationOffersAdapter(
    private var negotiationOfferDetailsList: ArrayList<NegotiationOfferDetails>,
    private var setOnOfferClickListeners: SetOnOfferClickListeners,
    private var saleOrNot: Boolean = false
) :
    Adapter<NegotiationOffersAdapter.NegotiationOffersViewHolder>() {
    private var isSend: Boolean = false
    lateinit var context: Context

    class NegotiationOffersViewHolder(var viewBinding: ItemNegotiationOffersBinding) :
        ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegotiationOffersViewHolder {
        context = parent.context
        return NegotiationOffersViewHolder(
            ItemNegotiationOffersBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = negotiationOfferDetailsList.size

    //@SuppressLint("SetTextI18n")
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NegotiationOffersViewHolder, position: Int) {
//        if(negotiationOfferDetailsList[position].offerExpireHours!=null){
//            holder.viewBinding.tvExpireHours.text="${negotiationOfferDetailsList[position].offerExpireHours} ${context.getString(R.string._3_days_after_the_offer_ends)}"
//        }else{
//            holder.viewBinding.tvExpireHours.text=""
//        }

          holder.viewBinding.productType.text = negotiationOfferDetailsList[position].productCategory
        if( !negotiationOfferDetailsList[position].productName.equals(""))
            holder.viewBinding.productName.visibility=View.VISIBLE
        else
            holder.viewBinding.productName.visibility=View.GONE
        holder.viewBinding.productName.text = negotiationOfferDetailsList[position].productName
        holder.viewBinding.productCity.text = negotiationOfferDetailsList[position].region
        holder.viewBinding.tvOfferedPrice.text="${context.getString(R.string.offerPrice)} ${negotiationOfferDetailsList[position].offerPrice} ${context.getString(R.string.sar)}"
        if (negotiationOfferDetailsList[position].businessAccountId == null) {
            holder.viewBinding.sellerTag.text = context.getString(R.string.personal)
        } else {
            holder.viewBinding.sellerTag.text = context.getString(R.string.merchant)
        }
//        holder.viewBinding.sellerTag.text =
        Extension.loadThumbnail(
            context,
            negotiationOfferDetailsList[position].productImage,
            holder.viewBinding.productImage,
            holder.viewBinding.loader
        )
//        if(negotiationOfferDetailsList[position].senderImage="")
        if(negotiationOfferDetailsList[position].productPrice!=0f){
            holder.viewBinding.tvProductPrice.visibility=View.VISIBLE
        }

        holder.viewBinding.tvProductPrice.text =
            "${negotiationOfferDetailsList[position].offerPrice} ${
                context.getString(
                    R.string.SAR
                )
            }"

        if (saleOrNot) {
            holder.viewBinding.personName.text =
                negotiationOfferDetailsList[position].buyerName ?: ""
            Extension.loadThumbnail(
                context,
                negotiationOfferDetailsList[position].receiverImage,
                holder.viewBinding.sellerPicture,
                holder.viewBinding.loader2
            )
            holder.viewBinding.personName.text =
                negotiationOfferDetailsList[position].sellerName ?: ""
            holder.viewBinding.containerSaleButton.hide()
            holder.viewBinding.btnCancel.hide()

            // my product
            if (isSend) {
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.tvExpireHours.text =
                            context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.hide()
                    }
                    "Canceled" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.OfferCanceled)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.accepted)
                        holder.viewBinding.btnPurchase.hide()
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.show()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.accepted)
                    }
                    "Refused" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.rejected)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.rejected)
                    }
                    "Purchased" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Purchased)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Purchased)
                    }
                    "Expired" ->{
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.expired)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.expired)
                    }

                    "Lost" ->{
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Lost)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Lost)
                    }
                    else -> {
                        holder.viewBinding.tvStatus.text =  ""
                        holder.viewBinding.tvExpireHours.text = ""
                    }
                }

            } else {
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.tvExpireHours.text =
                            context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.containerSaleButton.show()
                    }
                    "Canceled" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.OfferCanceled)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.accepted)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.accepted)
                    }
                    "Refused" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.rejected)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.rejected)
                    }
                    "Purchased" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Purchased)
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Purchased)
                    }
                    "Expired" ->{
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.expired)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.expired)
                    }
                    "Lost" ->{
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Lost)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Lost)
                    }
                    else -> {
                        holder.viewBinding.tvStatus.text =  ""
                        holder.viewBinding.tvExpireHours.text = ""
                    }
                }
            }
        } else {
            Extension.loadThumbnail(
                context,
                negotiationOfferDetailsList[position].senderImage,
                holder.viewBinding.sellerPicture,
                holder.viewBinding.loader2
            )
            holder.viewBinding.personName.text =
                negotiationOfferDetailsList[position].sellerName ?: ""
            holder.viewBinding.btnCancel.hide()
            holder.viewBinding.containerSaleButton.hide()

            // my negotation
            if(isSend){
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.btnCancel.show()
                        holder.viewBinding.btnPurchase.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.waitForYourResponse)
                    }
                    "Canceled" -> {
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.OfferCanceled)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.btnPurchase.show()
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.accepted)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.accepted)
                    }
                    "Refused"->{
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.rejected)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.rejected)
                    }
                    "Purchased" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Purchased)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Purchased)
                    }
                    "Expired" ->{
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.expired)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.expired)
                    }
                    "Lost" ->{
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Lost)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Lost)
                    }
                    else -> {
                        holder.viewBinding.tvExpireHours.text = ""
                        holder.viewBinding.tvStatus.text =  ""
                    }
                }
            }else{
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.containerSaleButton.show()
                        holder.viewBinding.btnCancel.hide()
                    }
                    "Canceled" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.OfferCanceled)
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.accepted)
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.btnPurchase.show()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.accepted)
                    }
                    "Refused"->{
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.rejected)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.rejected)
                    }

                    "Purchased" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Purchased)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Purchased)
                    }
                    "Expired" ->{
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.expired)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.expired)
                    }
                    "Lost" ->{
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text =  context.getString(R.string.Lost)
                        holder.viewBinding.tvExpireHours.text = context.getString(R.string.Lost)
                    }
                    else -> {
                        holder.viewBinding.tvExpireHours.text = ""
                        holder.viewBinding.tvStatus.text =  ""
                    }
                }
            }

        }

        holder.viewBinding.btnPurchase.setOnClickListener {
            setOnOfferClickListeners.onPurchaseOffer( offerID = negotiationOfferDetailsList[position].offerId,
                position)
        }
        holder.viewBinding.btnCancel.setOnClickListener {
            if (saleOrNot) {
                setOnOfferClickListeners.onCancelOffer(
                    saleOrNot,
                    offerID = negotiationOfferDetailsList[position].offerId,
                    position
                )
            }else{
                setOnOfferClickListeners.onCancelOffer(
                    saleOrNot,
                    offerID = negotiationOfferDetailsList[position].offerId,
                    position
                )
            }

        }
        holder.viewBinding.btnAccept.setOnClickListener {
            setOnOfferClickListeners.onAcceptOffer(position)
        }
        holder.viewBinding.btnReject.setOnClickListener {
            setOnOfferClickListeners.onRejectOffer(position)
        }
    }



    fun setIsSend(sent: Boolean) {
        isSend = sent
    }

    interface SetOnOfferClickListeners {
        fun onCancelOffer(type:Boolean ,offerID: Int, position: Int)
        fun onAcceptOffer(position: Int)
        fun onRejectOffer(position: Int)
        fun onPurchaseOffer(offerID: Int, position: Int)
    }
}